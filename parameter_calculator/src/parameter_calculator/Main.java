package parameter_calculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import parameter_calculator.config.Config;
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
		
		Prototype p = new Prototype(pro, Config.data_vertical_size, Config.vision_delay_cycle, Config.mov_avg_range, Config.cycle_delay, Config.robot_marker_radius);
		List<List<Double>> list = DataUtils.makeRawParamsFromFile(DataUtils.getFilesFromDir("/home/miuzki/robocup/ai-server/build/my_gr/logs/test", ".csv").get(0), ",", 1, 0, 1, 2, 12, 13, 14);

		try {
			String name = "log.csv";
			BufferedWriter bw = new BufferedWriter(new FileWriter(name));
			
			for(List<Double> input : list) {
				p.read_test(new double[] {input.get(0),input.get(1),input.get(2)});
				List<Double> output = new ArrayList<Double>();
				output.addAll(input);
				output.addAll(p.generate());
				String s = "";
				for(double d : output) {
					s += d + ",";
				}
				bw.write(s + "\n");
			}
			
			bw.close();
		}
		catch (IOException e) {
			System.out.println("Error with writer");
		}
	}
}
