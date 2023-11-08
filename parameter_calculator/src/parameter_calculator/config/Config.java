package parameter_calculator.config;

import parameter_calculator.utils.MathHelper;

public class Config {
	public static double vision_delay = 0.080;
	public static double cycle_delay = 0.0166666667;
	public static int vision_delay_cycle = MathHelper.round(vision_delay / cycle_delay);
	
	public static double sample_time = 1.0;
	public static int sample_cycle = MathHelper.round(sample_time / cycle_delay);
	
	public static int data_vertical_size = sample_cycle;
	public static int data_horizontal_size = 3;
	
	public static int neuron_middle_size = 16;
	public static int neuron_output_size = 3;
	
	public static double ratio_test = 0.25;
	
	public static int batch_size = 32;
	public static int epochs = 100;
	
	public static double eta = 0.001;
}
