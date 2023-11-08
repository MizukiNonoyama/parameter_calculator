package parameter_calculator.data;

import java.util.List;

public class DataGaussian {
	private final List<SampleGaussian> sample;
	private final double avg;
	private final double std;
	
	public DataGaussian(List<SampleGaussian> sample, double avg, double std) {
		this.sample = sample;
		this.avg = avg;
		this.std = std;
	}
	
	public List<SampleGaussian> getSample() {
		return this.sample;
	}
	
	public double getAverage() {
		return this.avg;
	}
	
	public double getStd() {
		return this.std;
	}
	
	public String toString() {
		return this.avg + "," + this.std;
	}
}
