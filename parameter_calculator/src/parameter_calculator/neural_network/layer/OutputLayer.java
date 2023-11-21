package parameter_calculator.neural_network.layer;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.config.Config;
import parameter_calculator.utils.MathHelper;
import parameter_calculator.utils.Predicates;

public class OutputLayer extends BaseLayer {
	public OutputLayer(int n_upper,int n_self) {
		this.w = (MatrixDouble) MathHelper.randGaussianMatrix(n_upper, n_self).productToAll(Math.sqrt(1.0 / n_upper));
		this.b = (MatrixDouble) MathHelper.zeroMatrix(1, n_self);
	}
	
	public void forward(MatrixDouble x) {
		this.x = x;
		MatrixDouble xw = this.x.dot(this.w);
		this.u = xw.sum(MathHelper.cloneRowWithNewMatrix(this.b, 0, xw.getRowSize()));
		this.y = this.u.applyAll(Predicates.ASINH);
	}
	
	public void backward(MatrixDouble t) {
		MatrixDouble ones = new MatrixDouble();
		ones.resizeWith(t.getRowSize(), t.getColumnSize(), 1.0D);
		MatrixDouble delta = this.y.sum(t.applyAll(Predicates.MINUS)).product(this.u.applyAll(Predicates.ASINH_GRAD));
		this.grad_w = this.x.transform().dot(delta);
		this.grad_b = MathHelper.cloneRowWithNewMatrix(MathHelper.makeSumVertical(delta), 0, 1);
		this.grad_x = delta.dot(this.w.transform());
	}
}
