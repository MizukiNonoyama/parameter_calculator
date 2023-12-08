package parameter_calculator.neural_network;

import java.io.File;
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
	private final GuiMain gui;
	
	public Processor(List<Sample> samples, GuiMain gui) {
		this.layers = new BaseLayer[] {
				new MiddleLayer(Config.data_horizontal_size * 
						Config.data_vertical_size, Config.neuron_middle_size),
				new MiddleLayer(Config.neuron_middle_size, Config.neuron_middle_size),
				new OutputLayer(Config.neuron_middle_size, Config.neuron_output_size)
		};
		this.samples = new ArrayList<Sample>(samples);
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
		System.out.println(y + " : " + t);
		return MathHelper.sumAll(y.sum(t.applyAll(Predicates.MINUS)).applyAll(Predicates.SQUARE)) / 2.0;
	}
	
	public void process() {
		DataGaussian data = DataUtils.makeGaussian(this.samples);
		double avg = data.getAverage();
		double std = data.getStd();
		List<SampleGaussian> sampleTest = new ArrayList<SampleGaussian>(data.getSample().subList(0, (int)(data.getSample().size() * Config.ratio_test)));
		List<SampleGaussian> sampleTrain = new ArrayList<SampleGaussian>(data.getSample().subList((int)(data.getSample().size() * Config.ratio_test), data.getSample().size()));
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
				for(int ri : random_index) {
					for(int row = 0;row < Config.batch_size;row++) {
						x_md.setRow(sampleTrain.get(ri).getInput(), row);
						t_md.setRow(sampleTrain.get(ri).getOutput(),row);
					}
				}
				this.forward_propagation(x_md);
				this.back_propagation(t_md);
				
				this.update_params(epoch + 1);
			}
			
			MatrixDouble x_error_train = new MatrixDouble();
			MatrixDouble t_error_train = new MatrixDouble();
			x_error_train.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
			t_error_train.resizeWith(1, Config.neuron_output_size, null);
			//int i = indexes.get(0);
			x_error_train.setRow(sampleTrain.get(50).getInput(), 0);
			t_error_train.setRow(sampleTrain.get(50).getOutput(),0);
			double error_train = Math.sqrt(this.getError(x_error_train, t_error_train)) * std;
			System.out.println("Train : " + error_train);
			if(!this.gui.getPlot().hasData("Train")) {
				this.gui.getPlot().setData("Train", new PlotData(new ArrayList<Pair<Double,Double>>(), 0xAA0000, true));
			}
			this.gui.getPlot().getData("Train").getData().add(new Pair<Double,Double>((double)(epoch + 1), error_train));
			
			MatrixDouble x_error_test = new MatrixDouble();
			MatrixDouble t_error_test = new MatrixDouble();
			x_error_test.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
			t_error_test.resizeWith(1, Config.neuron_output_size, null);
			//int j = MathHelper.RAND.nextInt(sampleTest.size());
			x_error_test.setRow(sampleTest.get(50).getInput(), 0);
			t_error_test.setRow(sampleTest.get(50).getOutput(),0);
			double error_test = Math.sqrt(this.getError(x_error_test, t_error_test)) * std;
			System.out.println("Test : " + error_test);
			if(!this.gui.getPlot().hasData("Test")) {
				this.gui.getPlot().setData("Test", new PlotData(new ArrayList<Pair<Double,Double>>(), 0x00AA00, true));
			}
			this.gui.getPlot().getData("Test").getData().add(new Pair<Double,Double>((double)(epoch + 1), error_test));
			this.gui.getPlot().setMaxX(epoch + 10);
			this.gui.getPlot().setScaleX((double)(epoch + 10) / 5.0);
			this.gui.repaint();
		}
		
		List<Sample> samples = new ArrayList<Sample>();
		samples.addAll(DataUtils.makeSamples(DataUtils.getFilesFromDir("/home/miuzki/robocup/ai-server/build/my_gr/logs/test", ".csv").get(0), ",", 1, 0, 1, 2, 12, 13, 14));
		DataGaussian dg = DataUtils.makeGaussian(samples, avg, std);
		for(SampleGaussian sg : dg.getSample()) {
			String output = "";
			for(double v : sg.inputs) {
				output += v + ",";
			}
			MatrixDouble md = new MatrixDouble();
			md.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
			md.setRow(sg.getInput(), 0);
			MatrixDouble generated = this.forward_propagation(md);
			for(int j = 0;j < generated.getColumnSize();j++) {
				output += (generated.get(0, j) * std + avg) + ",";
			}
			System.out.println(output);
		}
	}
}
