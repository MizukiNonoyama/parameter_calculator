package parameter_calculator.api;

public class MatrixDouble extends MatrixNumber<Double> 
{	
	public MatrixDouble() {
		super();
	}
	
	public MatrixDouble(MatrixDouble value) {
		super(value);
	}
	
	public MatrixDouble(Double[][] value) {
		super(value);
	} 
	
	@Override
	protected Double sum(Double a, Double b) {
		return a + b;
	}

	@Override
	protected Double product(Double a, Double b) {
		return a * b;
	}

	@Override
	protected Double empty() {
		return 0.0D;
	}

	@Override
	protected MatrixNumber<Double> base(Matrix<Double> value) {
		MatrixDouble mat = new MatrixDouble();
		mat.setMatrix(value.getMatrix());
		return mat;
	}
	
	@Override
	public MatrixDouble dot(Matrix<Double> value) {
		return (MatrixDouble) super.dot(value);
	}
	
	@Override
	public MatrixDouble sum(Matrix<Double> value) {
		return (MatrixDouble) super.sum(value);
	}
	
	@Override
	public MatrixDouble productToAll(Double value) {
		return (MatrixDouble) super.productToAll(value);
	}
	
	@Override
	public MatrixDouble product(Matrix<Double> value) {
		return (MatrixDouble) super.product(value);
	}
	
	@Override
	public MatrixDouble transform() {
		return (MatrixDouble) super.transform();
	}
	
	@Override
	public MatrixDouble applyAll(Predicate<Double> f) {
		return (MatrixDouble) super.applyAll(f);
	}
}
