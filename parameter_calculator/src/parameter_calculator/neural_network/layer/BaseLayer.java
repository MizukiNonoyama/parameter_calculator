package parameter_calculator.neural_network.layer;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.utils.Predicates;

public abstract class BaseLayer {
	public MatrixDouble w = new MatrixDouble();
	public MatrixDouble x = new MatrixDouble();
	public MatrixDouble u = new MatrixDouble();
	public MatrixDouble b = new MatrixDouble();
	public MatrixDouble y = new MatrixDouble();
	public MatrixDouble grad_w = new MatrixDouble();
	public MatrixDouble grad_x = new MatrixDouble();
	public MatrixDouble grad_b = new MatrixDouble();
	
	public void update(double eta) {
		this.w = this.w.sum(this.grad_w.productToAll(eta).applyAll(Predicates.MINUS));
		this.b = this.b.sum(this.grad_b.productToAll(eta).applyAll(Predicates.MINUS));
	}
	
	public abstract void forward(MatrixDouble value);
	
	public abstract void backward(MatrixDouble value);
}
