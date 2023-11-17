package parameter_calculator.gui.util;

public class MathHelper {
	public static int makeX(double x, double min_x,double max_x,int width,int posX) {
		return (int)((x - min_x) / (max_x - min_x) * width) + posX;
	}
	
	public static int makeY(double y, double min_y,double max_y,int height,int posY) {
		return (int)((max_y - y) / (max_y - min_y) * height) + posY;
	}
	
	public static int roundDown(double value) {
		return (int)value;
	}
	
	public static int roundUp(double value) {
		return (value - (double)((int)value) == 0.0) ? (int)value : (int)value + (int)(value / Math.abs(value));
	}
	
	public static boolean isIn(double x,double y,double min_x,double min_y,double max_x,double max_y) {
		return min_x <= x && x <= max_x && min_y <= y && y <= max_y; 
	}
}

