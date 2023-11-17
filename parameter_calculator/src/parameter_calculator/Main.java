package parameter_calculator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import parameter_calculator.data.Sample;
import parameter_calculator.gui.GuiMain;
import parameter_calculator.neural_network.Processor;
import parameter_calculator.utils.DataUtils;

public class Main {
	public static void main(String[] args) {
		GuiMain win = new GuiMain("Graph",640,640);
		win.setVisible(true);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		List<Sample> samples = new ArrayList<Sample>();
		for(File file : DataUtils.getFilesFromDir("/home/miuzki/robocup/ai-server/build/my_gr/logs/measurement", ".csv")) samples.addAll(DataUtils.makeSamples(file, ",", 1, 0, 1, 2, 12, 13, 14));
		Processor pro = new Processor(samples, win);
		pro.process();
	}
}
