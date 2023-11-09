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
	public List<T> getColumn(int column);
	public List<T> getRow(int row);
	public void setColumn(List<T> value, int column);
	public void setRow(List<T> value, int row);
	public void setMatrix(List<List<T>> value);
	public Matrix<T> applyAll(Predicate<T> value);
}
