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
	
	public double[] getInputValues() {
		Parameters param = this.input.get(this.input.size() - 1);
		return new double[] {param.getParam()[0],param.getParam()[1],param.getParam()[2],param.getVX(),param.getVY(),param.getOmega()};
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
	
	public int getArraySize() {
		return this.input.size();
	}
	
	public double get(int array_index, int data_type) {
		if(data_type < 3) return this.input.get(array_index).getParam()[data_type];
		else if(data_type == 3) return this.input.get(array_index).getVX();
		else if(data_type == 4) return this.input.get(array_index).getVY();
		return this.input.get(array_index).getOmega();
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
	
	public static enum Type {
		COMMAND_VX(0),
		COMMAND_VY(1),
		COMMAND_OMEGA(2),
		ACTUAL_VX(3),
		ACTUAL_VY(4),
		ACTUAL_OMEGA(5);
		
		private int id;
		Type(int id) {
			this.id = id;
		}
		
		public static int getID(Type type) {
			return type.id;
		}
		
		@SuppressWarnings("static-access")
		public Type getValue(int id) {
			return this.values()[id];
		}
	}
}
