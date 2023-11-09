package parameter_calculator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parameter_calculator.data.Sample;
import parameter_calculator.neural_network.Processor;
import parameter_calculator.utils.DataUtils;

public class Main {
	public static void main(String[] args) {
		List<Sample> samples = new ArrayList<Sample>();
		for(File file : DataUtils.getFilesFromDir("/home/miuzki/robocup/ai-server/build/my_gr/logs/measurement", ".csv")) samples.addAll(DataUtils.makeSamples(file, ",", 1, 0, 1, 2, 12, 13, 14));
		Processor pro = new Processor(samples);
		pro.process();
	}
}
