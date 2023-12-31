package parameter_calculator.neural_network;

import java.util.ArrayList;
import java.util.List;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.config.Config;
import parameter_calculator.data.DataGaussian;
import parameter_calculator.data.Sample;
import parameter_calculator.data.SampleGaussian;
import parameter_calculator.neural_network.layer.BaseLayer;
import parameter_calculator.neural_network.layer.MiddleLayer;
import parameter_calculator.neural_network.layer.OutputLayer;
import parameter_calculator.utils.DataUtils;
import parameter_calculator.utils.MathHelper;
import parameter_calculator.utils.Predicates;

public class Processor {
	private final BaseLayer[] layers;
	private final List<Sample> samples;
	
	public Processor(List<Sample> samples) {
		this.layers = new BaseLayer[] {
				new MiddleLayer(Config.data_horizontal_size * 
						Config.data_vertical_size, Config.neuron_middle_size),
				new MiddleLayer(Config.neuron_middle_size, Config.neuron_middle_size),
				new OutputLayer(Config.neuron_middle_size, Config.neuron_output_size)
		};
		this.samples = new ArrayList<Sample>(samples);
	}
	
	public MatrixDouble forward_propagation(MatrixDouble x) {
		MatrixDouble k = x;
		for(BaseLayer layer : this.layers) {
			layer.forward(k);
			k = layer.y;
		}
		return k;
	}
	
	public MatrixDouble back_propagation(MatrixDouble t) {
		MatrixDouble grad_y = t;
		for(BaseLayer layer : MathHelper.reverse(this.layers)) {
			layer.backward(grad_y);
			grad_y = layer.grad_x;
		}
		return grad_y;
	}
	
	public void update_params() {
		for(BaseLayer layer : this.layers) {
			layer.update(Config.eta);
		}
	}
	
	public double getError(MatrixDouble x, MatrixDouble t) {
		MatrixDouble y = this.forward_propagation(x);
		return MathHelper.sumAll(y.sum(t.applyAll(Predicates.MINUS)).applyAll(Predicates.SQUARE));
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
				
				this.update_params();
			}
			
			MatrixDouble x_error_train = new MatrixDouble();
			MatrixDouble t_error_train = new MatrixDouble();
			x_error_train.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
			t_error_train.resizeWith(1, Config.neuron_output_size, null);
			x_error_train.setRow(sampleTrain.get(indexes.get(0)).getInput(), 0);
			t_error_train.setRow(sampleTrain.get(indexes.get(0)).getOutput(),0);
			System.out.println("Train : " + this.getError(x_error_train, t_error_train));
			
			MatrixDouble x_error_test = new MatrixDouble();
			MatrixDouble t_error_test = new MatrixDouble();
			x_error_test.resizeWith(1, Config.data_horizontal_size * Config.data_vertical_size, null);
			t_error_test.resizeWith(1, Config.neuron_output_size, null);
			x_error_test.setRow(sampleTest.get(0).getInput(), 0);
			t_error_test.setRow(sampleTest.get(0).getOutput(),0);
			System.out.println("Test : " + this.getError(x_error_test, t_error_test));
		}
	}
}
