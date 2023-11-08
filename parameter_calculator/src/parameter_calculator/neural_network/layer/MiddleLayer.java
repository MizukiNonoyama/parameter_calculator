package parameter_calculator.neural_network.layer;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.utils.MathHelper;

public class MiddleLayer extends BaseLayer {
	
	public MiddleLayer(int n_upper,int n_self) {
		this.w = (MatrixDouble) MathHelper.randGaussianMatrix(n_upper, n_self).productToAll(Math.sqrt(2 / n_upper));
		this.b = (MatrixDouble) MathHelper.zeroMatrix(n_upper, n_self);
	}
	
	public void forward(MatrixDouble x) {
		this.x = x;
		this.u = this.x.dot(this.w).sum(this.b);
		this.y = MathHelper.applyReLU(this.u);
	}
	
	public void backward(MatrixDouble grad_y) {
		MatrixDouble delta = MathHelper.applyReLUGrad(this.u).product(grad_y);
		this.grad_w = this.x.transform().dot(delta);
		this.grad_b = MathHelper.makeSumVertical(delta);
		this.grad_x = delta.dot(this.w.transform());
	}
}
