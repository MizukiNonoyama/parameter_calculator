package parameter_calculator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import parameter_calculator.api.MatrixDouble;
import parameter_calculator.api.Pair;

public class MathHelper {
	
	public static final Random RAND = new Random();
	
	public static int round(double value) {
		return (int)(value + 0.5);
	}
	
	public static MatrixDouble randGaussianMatrix(int row,int column) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(row, column, null);
		for(int i = 0;i < row;i++) {
			for(int j = 0;j < column;j++) {
				mat.set(i, j, RAND.nextGaussian());
			}
		}
		return mat;
	}
	
	public static MatrixDouble zeroMatrix(int row,int column) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(row, column, null);
		for(int i = 0;i < row;i++) {
			for(int j = 0;j < column;j++) {
				mat.set(i, j, 0.0);
			}
		}
		return mat;
	}
	
	public static MatrixDouble applyReLU(MatrixDouble value) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(value.getRowSize(), value.getColumnSize(), null);
		for(int i = 0;i < value.getRowSize();i++) {
			for(int j = 0;j < value.getColumnSize();j++) {
				mat.set(i, j, MathHelper.ReLU.apply(value.get(i, j)));
			}
		}
		return mat;
	}
	
	public static MatrixDouble applyReLUGrad(MatrixDouble value) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(value.getRowSize(), value.getColumnSize(), null);
		for(int i = 0;i < value.getRowSize();i++) {
			for(int j = 0;j < value.getColumnSize();j++) {
				mat.set(i, j, MathHelper.ReLU.applyGrad(value.get(i, j)));
			}
		}
		return mat;
	}
	
	public static MatrixDouble makeSumVertical(MatrixDouble value) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(value.getRowSize(), value.getColumnSize(), null);
		for(int j = 0;j < value.getColumnSize();j++) {
			double total = 0.0;
			for(int i = 0;i < value.getRowSize();i++) {
				total += value.get(i, j);
			}
			
			for(int i = 0;i < value.getRowSize();i++) {
				mat.set(i, j, total);
			}
		}
		return mat;
	}
	
	public static MatrixDouble makeSumHorizontal(MatrixDouble value) {
		MatrixDouble mat = new MatrixDouble();
		mat.resizeWith(value.getRowSize(), value.getColumnSize(), null);
		for(int i = 0;i < value.getRowSize();i++) {
			double total = 0.0;
			for(int j = 0;j < value.getColumnSize();j++) {
				total += value.get(i, j);
			}
			
			for(int j = 0;j < value.getColumnSize();j++) {
				mat.set(i, j, total);
			}
		}
		return mat;
	}
	
	public static double sumAll(MatrixDouble value) {
		double result = 0.0;
		for(int i = 0;i < value.getRowSize();i++) {
			for(int j = 0;j < value.getColumnSize();j++) {
				result += value.get(i, j);
			}
		}
		return result;
	}
	
	public static <T> T[] reverse(T[] value) {
		T[] output = value.clone();
		for(int i = 0, l = value.length - 1;i <= l;i++,l--) {
			output[i] = value[l];
			output[l] = value[i];
		}
		return output;
	}
	
	public static class ReLU {
		public static Double apply(double value) {
			if(value <= 0) {
				return 0.0D;
			}
			return value;
		}
		
		public static Double applyGrad(double value) {
			if(value <= 0) {
				return 0.0D;
			}
			return 1.0D;
		}
	}
	
	public static double getStandardization(double value, double avg, double std) {
		return (value - avg) / std;
	}
	
	public static double getOrigin(double value, double avg, double std) {
		return value * std + avg;
	}
	
	public static Pair<Double,Double> getAvgStd(List<Double> datas) {
		double total = 0.0;
		for(double value : datas) {
			total += value;
		}
		double avg = total / datas.size();
		total = 0.0;
		for(double value : datas) {
			total += Math.pow(value - avg,2);
		}
		double std = Math.sqrt(total / datas.size());
		return new Pair<Double,Double>(avg, std);
	}
	
	public static List<Integer> makeIntList(int value) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i < value;i++) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Integer> arrangeRandom(List<Integer> value) {
		List<Integer> source = new ArrayList<Integer>(value);
		List<Integer> list = new ArrayList<Integer>();
		while(source.size() > 0) {
			int size = source.size();
			int index = RAND.nextInt(size);
			list.add(source.get(index));
			source.remove(index);
		}
		return list;
	}
}
