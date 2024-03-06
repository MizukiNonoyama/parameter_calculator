package parameter_calculator.neural_network;

import java.util.ArrayList;
import java.util.List;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.api.Pair;
import parameter_calculator.config.Config;
import parameter_calculator.data.DataGaussian;
import parameter_calculator.data.Sample;
import parameter_calculator.data.SampleGaussian;
import parameter_calculator.gui.GuiMain;
import parameter_calculator.gui.util.PlotData;
import parameter_calculator.neural_network.layer.BaseLayer;
import parameter_calculator.neural_network.layer.MiddleLayer;
import parameter_calculator.neural_network.layer.OutputLayer;
import parameter_calculator.utils.DataUtils;
import parameter_calculator.utils.MathHelper;
import parameter_calculator.utils.Predicates;

public class Processor {
	private final BaseLayer[] layers;
	private final List<Sample> samples;
	private final DataGaussian data;
	private final GuiMain gui;
	
	public Processor(List<Sample> samples, GuiMain gui) {
		this.layers = new BaseLayer[Config.middle_layer + 1];
		this.layers[0] = new MiddleLayer(Config.data_horizontal_size * 
				Config.data_vertical_size, Config.neuron_middle_size);
		for(int i = 1;i < Config.middle_layer;i++) {
			this.layers[i] = new MiddleLayer(Config.neuron_middle_size, Config.neuron_middle_size);
		}
		this.layers[Config.middle_layer] = new OutputLayer(Config.neuron_middle_size, Config.neuron_output_size);
		/*{
				new MiddleLayer(Config.data_horizontal_size * 
						Config.data_vertical_size, Config.neuron_middle_size),
				new MiddleLayer(Config.neuron_middle_size, Config.neuron_middle_size),
				new OutputLayer(Config.neuron_middle_size, Config.neuron_output_size)
		};
		*/
		this.samples = new ArrayList<Sample>(samples);
		this.data = DataUtils.makeGaussian(this.samples);
		this.gui = gui;
	}
	
	public MatrixDouble forward_propagation(MatrixDouble x) {
		MatrixDouble k = new MatrixDouble(x);
		for(BaseLayer layer : this.layers) {
			layer.forward(k);
			k = new MatrixDouble(layer.y);
		}
		return k;
	}
	
	public MatrixDouble back_propagation(MatrixDouble t) {
		MatrixDouble grad_y = new MatrixDouble(t);
		for(BaseLayer layer : MathHelper.reverse(this.layers)) {
			layer.backward(grad_y);
			grad_y = new MatrixDouble(layer.grad_x);
		}
		return grad_y;
	}
	
	public void update_params(int epoch) {
		for(BaseLayer layer : this.layers) {
			layer.update(Config.getEta(epoch));
		}
	}
	
	public double getError(MatrixDouble x, MatrixDouble t) {
		MatrixDouble y = this.forward_propagation(x);
		//System.out.println(y + " : " + t);
		return MathHelper.sumAll(y.sum(t.applyAll(Predicates.MINUS)).applyAll(Predicates.SQUARE)) / 2.0;
	}
	
	public List<Double> generateCommand(List<Double> input, double std, double avg, double robot_radius) {
		List<Double> output = new ArrayList<Double>();
		MatrixDouble md = new MatrixDouble();
		if(Config.data_horizontal_size * Config.data_vertical_size != input.size()) {
			System.out.println("Error1 : input_size{" + input.size() + "}");
			return output;
		} 
		md.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
		md.setRow(input, 0);
		MatrixDouble generated = this.forward_propagation(md);
		if(generated.getColumnSize() != 3) {
			System.out.println("Error2");
			return output;
		} 
		for(int j = 0;j < 3;j++) {
			output.add(MathHelper.getOrigin(generated.get(0, j),avg,std) / ((j == 2) ? robot_radius : 1.0));
		}
		return output;
	}
	
	public double getAvg() {
		return this.data.getAverage();
	}
	
	public double getStd() {
		return this.data.getStd();
	}
	
	public void process() {
		double std = data.getStd();
		List<SampleGaussian> sampleTest = new ArrayList<SampleGaussian>(data.getSample().subList(0, (int)(data.getSample().size() * Config.ratio_test)));
		List<SampleGaussian> sampleTrain = new ArrayList<SampleGaussian>(data.getSample().subList((int)(data.getSample().size() * Config.ratio_test), data.getSample().size()));
		//for(int index = 0;index < sampleTrain.size();index++) System.out.println(index + " : " + sampleTrain.get(index).getOutput());
		System.out.println("Sample Size : " + sampleTrain.size() + ", Std : " + std + ", Avg : " + data.getAverage());
		int loop_times = sampleTrain.size() / Config.batch_size;
		List<Integer> indexes = MathHelper.makeIntList(sampleTrain.size());
		for(int epoch = 0;epoch < Config.epochs;epoch++) {
			indexes = MathHelper.arrangeRandom(indexes);
			for(int j = 0;j < loop_times;j++) {
				MatrixDouble x_md = new MatrixDouble();
				MatrixDouble t_md = new MatrixDouble();
				x_md.resizeWith(Config.batch_size, Config.data_horizontal_size * 
					Config.data_vertical_size, null);
				t_md.resizeWith(Config.batch_size, Config.neuron_output_size, null);
				List<Integer> random_index = new ArrayList<Integer>(indexes.subList(j * Config.batch_size, (j + 1) * Config.batch_size));
				for(int row = 0;row < Config.batch_size;row++) {
					x_md.setRow(sampleTrain.get(random_index.get(row)).getInput(), row);
					t_md.setRow(sampleTrain.get(random_index.get(row)).getOutput(),row);
				}
				this.forward_propagation(x_md);
				this.back_propagation(t_md);
				
				this.update_params(epoch + 1);
			}
			
			double error_train = this.getErrorFromSample(sampleTrain, (Config.useAvg) ? -1 : 18011, std); //18011,62111
			if(!this.gui.getPlot().hasData("Train")) {
				this.gui.getPlot().setData("Train", new PlotData(new ArrayList<Pair<Double,Double>>(), 0xFF4B00, true));
			}
			this.gui.getPlot().getData("Train").getData().add(new Pair<Double,Double>((double)(epoch + 1), error_train));
			
			double error_test = this.getErrorFromSample(sampleTest, (Config.useAvg) ? -1 : 30, std);
			if(!this.gui.getPlot().hasData("Test")) {
				this.gui.getPlot().setData("Test", new PlotData(new ArrayList<Pair<Double,Double>>(), 0x03AF7A, true));
			}
			this.gui.getPlot().getData("Test").getData().add(new Pair<Double,Double>((double)(epoch + 1), error_test));
			this.gui.getPlot().setMaxX(epoch + 10);
			this.gui.getPlot().setScaleX((double)(epoch + 10) / 5.0);
			this.gui.repaint();
		}
	}
	
	public double getErrorFromSample(List<SampleGaussian> samples, int index, double std) {
		MatrixDouble x_error = new MatrixDouble();
		MatrixDouble t_error = new MatrixDouble();
		x_error.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
		t_error.resizeWith(1, Config.neuron_output_size, null);
		
		double error = 0;
		if(index > 0) {
			x_error.setRow(samples.get(index).getInput(), 0);
			t_error.setRow(samples.get(index).getOutput(),0);
			System.out.println(t_error + " | " + this.forward_propagation(x_error));
			error = Math.sqrt(this.getError(x_error, t_error)) * std;
		}
		else {
			for(int i = 0;i < samples.size();i++) {
				x_error.setRow(samples.get(i).getInput(), 0);
				t_error.setRow(samples.get(i).getOutput(),0);
				error += Math.sqrt(this.getError(x_error, t_error)) * std;
			}
			error /= samples.size();
		}
		return error;
	}
}
