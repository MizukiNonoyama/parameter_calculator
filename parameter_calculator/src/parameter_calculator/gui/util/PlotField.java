package parameter_calculator.gui.util;

public class PlotField {
	private double minx;
	private double miny;
	private double maxx;
	private double maxy;
	private int posX;
	private int posY;
	private int width;
	private int height;
	public PlotField(double min_x,double min_y,double max_x,double max_y,int posX,int posY,int width,int height) {
		this.minx = min_x;
		this.miny = min_y;
		this.maxx  = max_x;
		this.maxy = max_y;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	public int getX(double x) {
		return MathHelper.makeX(x, minx, maxx, width, posX);
	}
	
	public int getY(double y) {
		return MathHelper.makeY(y, miny, maxy, height, posY);
	}
}
