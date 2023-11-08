package parameter_calculator.neural_network.layer;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.utils.MathHelper;
import parameter_calculator.utils.Predicates;

public class OutputLayer extends BaseLayer {
	public OutputLayer(int n_upper,int n_self) {
		this.w = (MatrixDouble) MathHelper.randGaussianMatrix(n_upper, n_self).productToAll(Math.sqrt(1 / n_upper));
		this.b = (MatrixDouble) MathHelper.zeroMatrix(n_upper, n_self);
	}
	
	public void forward(MatrixDouble x) {
		this.x = x;
		this.u = this.x.dot(this.w).sum(this.b);
		this.y = this.u.applyAll(Predicates.EXP).product(MathHelper.makeSumHorizontal(this.u.applyAll(Predicates.EXP)).applyAll(Predicates.RECIPROCAL));
	}
	
	public void backward(MatrixDouble t) {
		MatrixDouble delta = this.y.sum(t.applyAll(Predicates.MINUS));
		this.grad_w = this.x.transform().dot(delta);
		this.grad_b = MathHelper.makeSumVertical(delta);
		this.grad_x = delta.dot(this.w.transform());
	}
}
