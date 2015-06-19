package newtonfractal;


import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Object[] columnNames; 
	private Object[][] data; 
	private int cellsNotEdited;

	public MyTableModel(int numCelulasEsquerdasNaoEditaveis, Object[][] data2, Object[] columnNames2) {
		data = data2;
		columnNames = columnNames2;
		cellsNotEdited = numCelulasEsquerdasNaoEditaveis;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col < cellsNotEdited) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	public Object[][] getData() { 
		return data;	
	}

}