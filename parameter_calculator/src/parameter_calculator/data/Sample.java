package parameter_calculator.data;

import java.util.List;

public class Sample {
	
	private final List<Parameters> input;
	private final double vx;
	private final double vy;
	private final double omega;
	
	public Sample(double vx,double vy,double omega,List<Parameters> list) {
		this.input = list;
		this.vx = vx;
		this.vy = vy;
		this.omega = omega;
	}

	public Sample(Parameters parameters, List<Parameters> list) {
		this.input = list;
		this.vx = parameters.getVX();
		this.vy = parameters.getVY();
		this.omega = parameters.getOmega();
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
	
	public List<Parameters> getArray() {
		return this.input;
	}
	
	public String toString() {
		String str = "";
		for(int i = 0;i < this.input.size();i++) {
			str += "{ " + this.input.get(i).getParam()[0] + ", " + this.input.get(i).getParam()[1] + ", " + this.input.get(i).getParam()[2] + " }";
			if(i < this.input.size() - 1) {
				str += ", ";
			}
		}
		return "{ " + this.vx + ", " + this.vy + ", " + this.omega + ", [" + str + "]}";
	}
}
