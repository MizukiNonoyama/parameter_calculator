package parameter_calculator.api;

import java.util.List;

public interface Matrix<T> {
	public T get(int i,int j);
	public boolean isEmpty();
	public Matrix<T> transform();
	public int getRowSize();
	public int getColumnSize();
	public void resizeWith(int m,int n,T value);
	public void set(int i,int j,T value);
	List<List<T>> getMatrix();
	public Matrix<T> getColumn(int column);
	public Matrix<T> getRow(int row);
	public void setMatrix(List<List<T>> value);
	public Matrix<T> applyAll(Predicate<T> value);
}
