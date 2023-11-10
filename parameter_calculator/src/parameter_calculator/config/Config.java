package parameter_calculator.config;

import parameter_calculator.utils.MathHelper;

public class Config {
	public static double vision_delay = 0.080;
	public static double cycle_delay = 0.0166666667;
	public static int vision_delay_cycle = MathHelper.round(vision_delay / cycle_delay);
	
	public static double robot_marker_radius = 65.0;
	
	public static double sample_time = 0.075;
	public static int sample_cycle = MathHelper.round(sample_time / cycle_delay);
	
	public static int data_vertical_size = sample_cycle;
	public static int data_horizontal_size = 3;
	
	public static int neuron_middle_size = 100;
	public static int neuron_output_size = 3;
	
	public static double ratio_test = 0.25;
	
	public static int batch_size = 100;
	public static int epochs = 1000;
	
	public static double eta = 0.001;
	
	public static double c = 20.0;
}
