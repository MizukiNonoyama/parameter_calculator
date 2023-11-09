package parameter_calculator.api;

import java.util.ArrayList;
import java.util.List;

public class HashMatrix<T> implements Matrix<T> 
{
	// row
	private int m;
	// column
	private int n;
	
	private List<List<T>> matrix;
	
	public HashMatrix() {
		this.m = 0;
		this.n = 0;
		this.matrix = new ArrayList<List<T>>();
	}
	
	public HashMatrix(T[][] value) {
		this.m = value.length;
		this.n = value[0].length;
		this.matrix = new ArrayList<List<T>>();
		this.resizeWith(this.m, this.n, null);
		for(int i = 0;i < this.m;i++) {
			for(int j = 0;j < this.n;j++) {
				this.set(i, j, value[i][j]);
			}
		}
	}
	
	public HashMatrix(Matrix<T> value) {
		this.setMatrix(value.getMatrix());
	}
	
	@Override
	public T get(int row, int column) {
		return matrix.get(row).get(column);
	}

	@Override
	public boolean isEmpty() {
		return matrix.size() == 0 || matrix.get(0).size() == 0;
	}

	@Override
	public int getRowSize() {
		return this.m;
	}

	@Override
	public int getColumnSize() {
		return this.n;
	}

	@Override
	public Matrix<T> transform() {
		Matrix<T> mat = new HashMatrix<T>();
		mat.resizeWith(this.getColumnSize(), this.getRowSize(), null);
		for(int i = 0;i < this.getRowSize();i++) {
			for(int j = 0;j < this.getColumnSize();j++) {
				mat.set(j, i, this.get(i, j));
			}
		}
		return mat;
	}

	@Override
	public void resizeWith(int row, int column, T value) {
		this.m = row;
		this.n = column;
		List<List<T>> mat = new ArrayList<List<T>>();
		for(int i = 0;i < row;i++) {
			List<T> list = new ArrayList<T>();
			for(int j = 0;j < column;j++) {
				list.add(value);
			}
			mat.add(list);
		}
		this.matrix = mat;
	}

	@Override
	public void set(int row, int column, T value) {
		this.matrix.get(row).set(column, value);
	}

	@Override
	public List<List<T>> getMatrix() {
		return this.matrix;
	}

	@Override
	public List<T> getColumn(int column) {
		List<T> list = new ArrayList<T>();
		for(int i = 0;i < this.getRowSize();i++) {
			list.add(this.get(i, column));
		}
		return list;
	}

	@Override
	public List<T> getRow(int row) {
		List<T> list = new ArrayList<T>();
		for(int i = 0;i < this.getColumnSize();i++) {
			list.add(this.get(row, i));
		}
		return list;
	}
	
	public String toString() {
		String result = "[";
		for(int i = 0;i < this.getRowSize();i++) {
			result += "[";
			for(int j = 0;j < this.getColumnSize();j++) {
				result += this.get(i, j).toString();
				if(this.getColumnSize() - 1 == j) break;
				result += ",";
			}
			result += "]";
			if(this.getRowSize() - 1 == i) break;
			result += ",";
		}
		result += "]";
		return result;
	}

	@Override
	public void setMatrix(List<List<T>> value) {
		this.m = value.size();
		this.n = value.get(0).size();
		this.matrix = new ArrayList<List<T>>();
		for(List<T> row : value) {
			this.matrix.add(new ArrayList<T>(row));
		}
	}

	@Override
	public Matrix<T> applyAll(Predicate<T> f) {
		Matrix<T> mat = new HashMatrix<T>();
		mat.resizeWith(this.getRowSize(), this.getColumnSize(), null);
		for(int i = 0;i < this.getRowSize();i++) {
			for(int j = 0;j < this.getColumnSize();j++) {
				mat.set(i, j, f.apply(this.get(i, j)));
			}
		}
		return mat;
	}

	@Override
	public void setColumn(List<T> value, int column) {
		if(column < this.getColumnSize() && value.size() == this.getRowSize()) {
			for(int i = 0;i < this.getRowSize();i++) {
				this.set(i, column, value.get(i));
			}
		}
	}

	@Override
	public void setRow(List<T> value, int row) {
		if(row < this.getRowSize() && value.size() == this.getColumnSize()) {
			for(int i = 0;i < this.getColumnSize();i++) {
				this.set(row, i, value.get(i));
			}
		}
	}
}
