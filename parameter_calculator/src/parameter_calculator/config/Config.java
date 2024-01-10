package parameter_calculator.config;

import parameter_calculator.utils.MathHelper;

public class Config {
	public static double vision_delay = 0.080;
	public static double cycle_delay = 0.0166666667;
	public static int vision_delay_cycle = MathHelper.round(vision_delay / cycle_delay);
	
	public static double robot_marker_radius = 65.0;
	
	public static double sample_time = 0.1;
	public static int sample_cycle = MathHelper.round(sample_time / cycle_delay);
	
	public static int mov_avg_range = 4;
	
	public static int data_vertical_size = sample_cycle;
	public static final int data_horizontal_size = 12;
	
	public static int neuron_middle_size = 16;
	public static int neuron_output_size = 3;
	
	public static double ratio_test = 0.25;
	
	public static int batch_size = 100;
	public static int epochs = 3000;
	
	public static int middle_layer = 2;
	
	public static boolean useAcc = true;
	
	public static int readable_line_row = 51;
	
	public static double eta = 0.001;
	
	public static double getEta(int epoch) {
		return  0.05 * Math.pow(2, -0.0003 * epoch);//0.05 / Math.sqrt((double)epoch / 10.0 + 1.0) + 0.00001;
	}
	
	public static double c = 15.0;
	public static double d = 5.0;
}
