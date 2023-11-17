package parameter_calculator.gui.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import parameter_calculator.api.Pair;

public class PlotData {
	private List<Pair<Double,Double>> datas;
	private int color;
	private boolean makeL;
	
	public PlotData(List<Pair<Double,Double>> datas, int color, boolean makeLine) {
		this.datas = datas;
		this.color = color;
		this.makeL = makeLine;
	}
	
	public List<Pair<Double,Double>> getData() {
		return this.datas;
	}
	
	public void setData(List<Pair<Double,Double>> data) {
		this.datas = data;
	}
	
	public Color getColor() {
		return new Color(this.color);
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public boolean makeLine() {
		return this.makeL;
	}
	
	public void setLine(boolean value) {
		this.makeL = value;
	}
}
