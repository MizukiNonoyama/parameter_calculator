package parameter_calculator.api;

public abstract class MatrixNumber<T> extends HashMatrix<T> 
{
	public MatrixNumber() {
		super();
	}
	
	public MatrixNumber(T[][] value) {
		super(value);
	} 
	
	protected abstract T sum(T a,T b);
	
	protected abstract T product(T a,T b);
	
	protected abstract T empty();
	
	protected abstract MatrixNumber<T> base(Matrix<T> value);
	
	public MatrixNumber<T> sum(Matrix<T> value) {
		Matrix<T> result = new HashMatrix<T>();
		if(value.getColumnSize() == this.getColumnSize() && value.getRowSize() == this.getRowSize()) {
			result.resizeWith(this.getRowSize(), getColumnSize(), null);
			for(int i = 0;i < this.getRowSize();i++) {
				for(int j = 0;j < this.getColumnSize();j++) {
					result.set(i, j, this.sum(this.get(i, j), value.get(i, j)));
				}
			}
		}
		return this.base(result);
	}
	
	public MatrixNumber<T> dot(Matrix<T> value) {
		Matrix<T> result = new HashMatrix<T>();
		if(value.getRowSize() == this.getColumnSize()) {
			result.resizeWith(this.getRowSize(), value.getColumnSize(), null);
			for(int i0 = 0;i0 < this.getRowSize();i0++) {
				for(int i1 = 0;i1 < value.getColumnSize();i1++) {
					T sum = this.empty();
					for(int j = 0;j < this.getColumnSize();j++) {
						sum = this.sum(sum, this.product(this.get(i0, j), value.get(j, i1)));
					}
					result.set(i0, i1, sum);
				}
			}
		}
		return this.base(result);
	}
	
	public MatrixNumber<T> productToAll(T value) {
		Matrix<T> result = new HashMatrix<T>();
		result.resizeWith(this.getRowSize(), this.getColumnSize(), null);
		for(int i = 0;i < this.getRowSize();i++) {
			for(int j = 0;j < this.getColumnSize();j++) {
				result.set(i, j, this.product(value,this.get(i, j)));
			}
		}
		return this.base(result);
	}
	
	public MatrixNumber<T> product(Matrix<T> value) {
		Matrix<T> result = new HashMatrix<T>();
		result.resizeWith(this.getRowSize(), this.getColumnSize(), null);
		for(int i = 0;i < this.getRowSize();i++) {
			for(int j = 0;j < this.getColumnSize();j++) {
				result.set(i, j, this.product(this.get(i, j),value.get(i, j)));
			}
		}
		return this.base(result);
	}
	
	public MatrixNumber<T> transform() {
		return this.base(super.transform());
	}
	
	public MatrixNumber<T> applyAll(Predicate<T> f) {
		return this.base(super.applyAll(f));
	}
}
