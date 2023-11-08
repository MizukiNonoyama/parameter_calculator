package parameter_calculator.data;

public class Parameters {
	private final double[] param;
	private final double actual_vx;
	private final double actual_vy;
	private final double actual_omega;
	public Parameters(double comm_vx,double comm_vy,double comm_omega, double real_vx,double real_vy,double real_omega) {
		this.param = new double[] {comm_vx,comm_vy,comm_omega};
		this.actual_omega = real_omega;
		this.actual_vx = real_vx;
		this.actual_vy = real_vy;
	}
	
	public double[] getParam() {
		return this.param;
	}
	
	public double getVX() {
		return this.actual_vx;
	}
	
	public double getVY() {
		return this.actual_vy;
	}
	
	public double getOmega() {
		return this.actual_omega;
	}
	
	public String toString() {
		return this.param.toString() + "," + this.actual_vx + "," + this.actual_vy + "," + this.actual_omega; 
	}
}
