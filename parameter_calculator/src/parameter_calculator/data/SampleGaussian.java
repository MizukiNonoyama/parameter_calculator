package parameter_calculator.data;

import java.util.List;

public class SampleGaussian {
	private final double vx;
	private final double vy;
	private final double omega;
	private List<Double> input;
	
	public SampleGaussian(double vx,double vy,double omega,List<Double> list) {
		this.input = list;
		this.vx = vx;
		this.vy = vy;
		this.omega = omega;
	}
	
	public double getVX() {
		return this.vx;
	}
	
	public double getVY() {
		return this.vy;
	}
	
	public double getOmega() {
		return this.omega;
	}
	
	public List<Double> getInput() {
		return this.input;
	}
}
