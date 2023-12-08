package parameter_calculator.gui;

public class GuiConfig {
	public static double resolution = 0.01;
	
	public static double min_plot_x = -5.0;
	public static double max_plot_x = 10.0;
	public static double max_plot_y = 800.0;
	public static double min_plot_y = -max_plot_y / 5.0;
	
	public static double scale_x = 2.0;
	public static double scale_x_small = 0.0;
	public static double scale_y = max_plot_y / 5.0;
	public static double scale_y_small = scale_y / 10.0;
}
