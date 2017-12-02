package mypandas;

import java.util.ArrayList;
import java.util.List;

public class MyDataFrame {
	List<Object> rows;
	List<Object> columns = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public MyDataFrame(List<Object> rows) {
		// TODO Auto-generated constructor stub
		this.rows = rows;
		
		Object FirstRow = this.rows.get(0);
		for (Object e : (ArrayList<Object>) FirstRow) {
			List<Object> col = new ArrayList <Object>();
			columns.add(col);
		}
		
		
		for (Object r : rows) {
			int j = 0;
			for (Object e : (ArrayList<Object>) r) {
				ArrayList<Object> column = (ArrayList<Object>) columns.get(j);
		        column.add(e);
		        columns.set(j, column);
		        j++;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public String toString() {
		String result = "";
		for (Object r : rows) {
			for (Object e : (ArrayList<Object>) r) {
				result = result + e + " ";
			}
			result = result + "\n";
		}
		return result;		
	}
	
	public MyDataFrame head(int n) {
		List<Object> hrows = new ArrayList<>();
		//start at line 1 and end at line n+1 because we have the header
		for (int i = 1; i < n+1; i++) {
			hrows.add(rows.get(i));
		}
		return new MyDataFrame(hrows);
	}
	
	public MyDataFrame tail(int n) {
		List<Object> trows = new ArrayList<>();
		//start at line 1 and end at line n+1 because we have the header
		for (int i = this.rows.size() - n; i < this.rows.size(); i++) {
			trows.add(rows.get(i));
		}
		return new MyDataFrame(trows);
	}
	
}
