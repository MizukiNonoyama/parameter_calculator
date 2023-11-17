package parameter_calculator.gui;

import javax.swing.JFrame;

import parameter_calculator.gui.plot.PanelPlot;
import parameter_calculator.gui.plot.PanelString;

@SuppressWarnings("serial")
public class GuiMain extends JFrame {
	
	private PanelString string = new PanelString("Epoch-Error", 500, 550, 2);
	private PanelPlot plot = new PanelPlot(string, 
			GuiConfig.min_plot_x, GuiConfig.min_plot_y, GuiConfig.max_plot_x, GuiConfig.max_plot_y, 
			GuiConfig.scale_x, GuiConfig.scale_y, GuiConfig.scale_x_small, GuiConfig.scale_y_small, 70, 70, 500, 500);
	
	public GuiMain(String title, int sizeX, int sizeY) {
		this.setSize(sizeX, sizeY);
		this.setTitle(title);
		this.plot.setTitle(this.string);
		this.add(this.plot);
	}

	public PanelPlot getPlot() {
		return this.plot;
	}
}

