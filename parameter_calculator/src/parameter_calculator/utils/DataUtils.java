package parameter_calculator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parameter_calculator.api.Pair;
import parameter_calculator.config.Config;
import parameter_calculator.data.DataGaussian;
import parameter_calculator.data.Parameters;
import parameter_calculator.data.Sample;
import parameter_calculator.data.SampleGaussian;

public class DataUtils {
	
	private static int sample_offset = 0;//Config.vision_delay_cycle - 1;
	
	private static List<Sample> makeSamples(List<Parameters> inputFromFile) {
		List<Sample> result = new ArrayList<Sample>();
		if(inputFromFile.size() < Config.sample_cycle + Config.vision_delay_cycle) return result;
		int index = 0;
		while(Config.sample_cycle + Config.vision_delay_cycle + index <= inputFromFile.size()) {
			List<Parameters> list = new ArrayList<Parameters>(inputFromFile.subList(index, Config.sample_cycle + index + sample_offset));
			boolean invalid = false;
			for(Parameters param : list) {
				if(param == null) { 
					invalid = true;
					break;
				}
			}
			Parameters param = inputFromFile.get(index + Config.sample_cycle + Config.vision_delay_cycle - 1);
			if(!invalid && param != null) result.add(new Sample(param, list));	
			index++;
		}
		return result;
	}
	
	public static boolean isZero(Parameters param) {
		return param.getParam()[0] == 0.0 && param.getParam()[1] == 0.0 && param.getParam()[2] == 0.0 && 
				-5 < param.getVX() && param.getVX() < 5 &&
				-5 < param.getVY() && param.getVY() < 5 &&
				-5 < param.getOmega() && param.getOmega() < 5;
	}
	
	public static DataGaussian makeGaussian(List<Sample> sampleAll) {
		List<Double> allValues = new ArrayList<Double>();
		for(Sample sample : sampleAll) {
			allValues.add(sample.getOmega());
			allValues.add(sample.getVX());
			allValues.add(sample.getVY());
			for(int i = 0;i < sample.getArray().size() - sample_offset;i++) {
				allValues.add(sample.getArray().get(i).getParam()[0]);
				allValues.add(sample.getArray().get(i).getParam()[1]);
				allValues.add(sample.getArray().get(i).getParam()[2]);
				allValues.add(sample.getArray().get(i + sample_offset).getOmega());
				allValues.add(sample.getArray().get(i + sample_offset).getVX());
				allValues.add(sample.getArray().get(i + sample_offset).getVY());
			}
		}
		
		Pair<Double,Double> avgStd = MathHelper.getAvgStd(allValues);
		double avg = avgStd.getKey();
		double std = avgStd.getValue();
		
		List<SampleGaussian> list = new ArrayList<SampleGaussian>();
		for(Sample sample : sampleAll) {
			List<Double> inputs = new ArrayList<Double>();
			for(int i = 0;i < sample.getArray().size() - sample_offset;i++) {
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i).getParam()[0],avg,std));
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i).getParam()[1],avg,std));
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i).getParam()[2],avg,std));
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i + sample_offset).getVX(),avg,std));
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i + sample_offset).getVY(),avg,std));
				inputs.add(MathHelper.getStandardization(sample.getArray().get(i + sample_offset).getOmega(),avg,std));
			}
			list.add(new SampleGaussian(MathHelper.getStandardization(sample.getVX(),avg,std),
					MathHelper.getStandardization(sample.getVY(),avg,std),
					MathHelper.getStandardization(sample.getOmega(),avg,std),inputs));
		}
		
		return new DataGaussian(list,avg,std);
	}
	
	private static List<Parameters> makeParamsFromFile(File file, String splitter,int start_row_index,int command_vx_index,int command_vy_index,int command_omega_index,int actual_vx_index, int actual_vy_index, int actual_omega_index) {
		List<Parameters> list = new ArrayList<Parameters>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String str;
			int rows = 0;
			while((str = br.readLine()) != null) {
				if(start_row_index <= rows) {
					String[] splitted = str.split(splitter,0);
					try {
						list.add(new Parameters(Double.valueOf(splitted[command_vx_index]),Double.valueOf(splitted[command_vy_index]),Double.valueOf(splitted[command_omega_index]) * Config.robot_marker_radius,Double.valueOf(splitted[actual_vx_index]),Double.valueOf(splitted[actual_vy_index]),Double.valueOf(splitted[actual_omega_index]) * Config.robot_marker_radius));
					} catch(NumberFormatException e) {
						list.add(null);
					}
				}
				rows++;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<Sample> makeSamples(File file, String splitter,int start_row_index,int command_vx_index,int command_vy_index,int command_omega_index,int actual_vx_index, int actual_vy_index, int actual_omega_index) {
		return makeSamples(makeParamsFromFile(file, splitter, start_row_index, command_vx_index, command_vy_index, command_omega_index, actual_vx_index, actual_vy_index, actual_omega_index));
	}
	
	public static List<File> getFilesFromDir(String path, String fileType) {
		List<File> files = new ArrayList<File>();
		File directory = new File(path);
		if(directory.listFiles() == null) return files;
		for(File file : directory.listFiles()) {
			if(file.getName().contains(fileType)) {
				files.add(file);
			}
		}
		return files;
	}
}
