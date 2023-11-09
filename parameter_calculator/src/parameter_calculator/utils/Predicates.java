package parameter_calculator.utils;

import parameter_calculator.api.Predicate;

public class Predicates {
	public static final Predicate<Double> MINUS = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return -value;
		}	
	};
	
	public static final Predicate<Double> RECIPROCAL = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return (value != 0) ? 1.0 / value : Double.MAX_VALUE;
		}	
	};
	
	public static final Predicate<Double> EXP = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return Math.exp(value);
		}	
	};
	
	public static final Predicate<Double> SQUARE = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return Math.pow(value,2);
		}
	};
	
	public static final Predicate<Double> RELU = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			if(value <= 0) {
				return 0.0D;
			}
			return value;
		}	
	};
	
	public static final Predicate<Double> RELU_GRAD = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			if(value <= 0) {
				return 0.0D;
			}
			return 1.0;
		}	
	};
	
	public static final Predicate<Double> TANH = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return Math.tanh(value);
		}	
	};
	
	public static final Predicate<Double> TANH_GRAD = new Predicate<Double>() {
		@Override
		public Double apply(Double value) {
			return 1 / (Math.cosh(value) * Math.cosh(value));
		}	
	};
}
