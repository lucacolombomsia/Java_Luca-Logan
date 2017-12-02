package mypandas;

import java.util.ArrayList;
import java.util.List;

public class MyDataFrame {
	List<Object> rows;
	List<Object> columns;

	public MyDataFrame(List<Object> rows, List<Object> columns) {
		// TODO Auto-generated constructor stub
		this.rows = rows;
		this.columns = columns;
	}
	
	@SuppressWarnings("unchecked")
	public String toString() {
		String result = "";
		for (Object r : rows) {
			for (Object e : (ArrayList<Object>) r) {
				result = result + e+" ";
			}
			result = result + "\n";
		}
		return result;		
	}

}
