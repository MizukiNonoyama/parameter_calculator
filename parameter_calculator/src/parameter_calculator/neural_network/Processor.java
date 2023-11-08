package parameter_calculator.neural_network;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.config.Config;
import parameter_calculator.neural_network.layer.BaseLayer;
import parameter_calculator.neural_network.layer.MiddleLayer;
import parameter_calculator.neural_network.layer.OutputLayer;
import parameter_calculator.utils.MathHelper;
import parameter_calculator.utils.Predicates;

public class Processor {
	public final BaseLayer[] layers;
	
	public Processor() {
		this.layers = new BaseLayer[] {
				new MiddleLayer(Config.data_horizontal_size * 
						Config.data_vertical_size, Config.neuron_middle_size),
				new MiddleLayer(Config.neuron_middle_size, Config.neuron_middle_size),
				new OutputLayer(Config.neuron_middle_size, Config.neuron_output_size)
		};
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
}
