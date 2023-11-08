package parameter_calculator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.config.Config;
import parameter_calculator.data.DataGaussian;
import parameter_calculator.data.Sample;
import parameter_calculator.data.SampleGaussian;
import parameter_calculator.utils.DataUtils;
import parameter_calculator.utils.MathHelper;

public class Main {
	public static void main(String[] args) {
		MatrixDouble matrix1 = new MatrixDouble(new Double[][] {{1.0,2.0,3.0},{1.0,2.0,4.0},{2.0,4.0,8.0},{1.0,5.0,6.0}});
		MatrixDouble matrix2 = new MatrixDouble(new Double[][] {{2.0,5.0},{2.0,7.0},{2.0,9.0},{2.0,3.0}});
		System.out.println(((MatrixDouble)(matrix2.transform().dot(matrix1))).toString());
		Double[] array = new Double[] {1.0,1.1,1.21,1.61,5.0,63.5};
		/*
		List<Sample> samples = new ArrayList<Sample>();
		for(File file : DataUtils.getFilesFromDir("/home/miuzki/robocup/ai-server/build/my_gr/logs/measurement", ".csv")) samples.addAll(DataUtils.makeSamples(file, ",", 1, 0, 1, 2, 12, 13, 14));
		DataGaussian data = DataUtils.makeGaussian(samples);
		double avg = data.getAverage();
		double std = data.getStd();
		List<SampleGaussian> sampleTest = new ArrayList<SampleGaussian>(data.getSample().subList(0, (int)(data.getSample().size() * Config.ratio_test)));
		List<SampleGaussian> sampleTrain = new ArrayList<SampleGaussian>(data.getSample().subList((int)(data.getSample().size() * Config.ratio_test), data.getSample().size()));
		int loop_per_epoch = sampleTrain.size() / Config.batch_size;
		*/
		System.out.println(MathHelper.arrangeRandom(MathHelper.makeIntList(10)));
	}
}
