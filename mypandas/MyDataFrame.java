package mypandas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MyDataFrame {
	String headers = new String();
	List<Object> rows = new ArrayList<>();
	List<Object> columns = new ArrayList<>();

	public MyDataFrame(List<Object> rows, List<Object> columns, String headers) {
		this.headers = headers;
		this.rows = rows;
		this.columns = columns;
	}
	
	@SuppressWarnings("unchecked")
	public String toString() {
		String result = this.headers + "\n";
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
		for (int i = 0; i < n; i++) {
			hrows.add(rows.get(i));
		}
		return fromrows(hrows, this.headers);
	}
	
	public MyDataFrame tail(int n) {
		List<Object> trows = new ArrayList<>();
		for (int i = this.rows.size() - n; i < this.rows.size(); i++) {
			trows.add(rows.get(i));
		}
		return fromrows(trows, this.headers);
	}
	
	public MyDataFrame loc(int index) {
		List<Object> srows = new ArrayList<>();
		srows.add(rows.get(index));
		return fromrows(srows, this.headers);
	}
	
	public MyDataFrame loc(int from, int to) {
		List<Object> srows = new ArrayList<>();
		for (int i = from; i < to; i++) {
			srows.add(rows.get(i));
		}
		return fromrows(srows, this.headers) ;
	}
	
	public MyDataFrame slice(int index) {
		String newhead = headers.split(" ")[index];
		List<Object> scols = new ArrayList<>();
		scols.add(columns.get(index));
		return fromcols(scols, newhead);
	}
	
	public MyDataFrame slice(String name) {
		int idx = Arrays.asList(headers.split(" ")).indexOf(name);
		String newhead = headers.split(" ")[idx];
		List<Object> scols = new ArrayList<>();
		scols.add(columns.get(idx));
		return fromcols(scols, newhead);
	}
	
	public MyDataFrame slice(int[] indexArr) {
		String newhead = new String();
		List<Object> scols = new ArrayList<>();
		StringJoiner joiner = new StringJoiner(" ");
		
		int idx = -1;
		for(int i = 0; i < indexArr.length; i++) {
			idx = indexArr[i];
			joiner.add(headers.split(" ")[idx]);
			scols.add(columns.get(idx));
		}
		newhead = joiner.toString();
		return fromcols(scols, newhead);
	}
	
	public MyDataFrame slice(String[] nameArr) {
		String newhead = new String();
		List<Object> scols = new ArrayList<>();
		StringJoiner joiner = new StringJoiner(" ");
		int idx = -1;
		
		for(int i = 0; i < nameArr.length; i++) {
			idx = Arrays.asList(headers.split(" ")).indexOf(nameArr[i]);
			joiner.add(headers.split(" ")[idx]);
			scols.add(columns.get(idx));
		}
		newhead = joiner.toString();
		return fromcols(scols, newhead);
	}
	
	
	@SuppressWarnings("unchecked")
	public static MyDataFrame fromcols(List<Object> cols, String headers) {
		List<Object> rows = new ArrayList<>();
		Object FirstCol = cols.get(0);
		for (Object r: (ArrayList<Object>) FirstCol) {
			List<Object> row = new ArrayList <Object>();
			row.add(r);
			rows.add(row);
		}
		
		for (int i = 1; i < headers.split(" ").length; i++) {
			Object col = cols.get(i);
			int j = 0;
			for (Object r: (ArrayList<Object>) col) {
				ArrayList<Object> row = (ArrayList<Object>) rows.get(j);
				row.add(r);
		        rows.set(j, row);
		        j++;
			}
		}
		return new MyDataFrame(rows, cols, headers) ;
	}
	
	@SuppressWarnings("unchecked")
	public static MyDataFrame fromrows(List<Object> rows, String headers) {
		List<Object> columns = new ArrayList<>();
		for (int i = 0; i < headers.split(" ").length; i++) {
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
		return new MyDataFrame(rows, columns, headers);
	}
}


