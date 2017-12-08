package mypandas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class MyDataFrame {
	/*
	 Our datastructure is as follows.
	 A dataframe is at the same time a collection of rows and a collection of columns.
	 It can be constructed from a collection of rows (using the fromrows method)
	 or from a collection of columns (using the fromcolumns method).
	 Certain methods are applied to columns, while others are applied to rows.
	 Each dataframe also has a set of headers.
	 */
	String headers = new String();
	List<Object> rows = new ArrayList<>();
	List<Object> columns = new ArrayList<>();
	
	//constructor
	public MyDataFrame(List<Object> rows, List<Object> columns, String headers) {
		this.headers = headers;
		this.rows = rows;
		this.columns = columns;
	}
	
	//print to console
	//when printing, always include the headers
	@SuppressWarnings("unchecked")
	public String toString() {
		String result = this.headers + "\n";
		for (Object r : rows) {
			for (Object e : (ArrayList<Object>) r) {
				result = result + e + " ";
			}
			result = result.substring(0, result.length() - 1) + "\n";
		}
		return result;		
	}
	
	//head of dataframe = first n rows
	public MyDataFrame head(int n) {
		List<Object> hrows = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			hrows.add(rows.get(i));
		}
		return fromrows(hrows, this.headers);
	}
	
	//head of dataframe = last n rows
	public MyDataFrame tail(int n) {
		List<Object> trows = new ArrayList<>();
		for (int i = this.rows.size() - n; i < this.rows.size(); i++) {
			trows.add(rows.get(i));
		}
		return fromrows(trows, this.headers);
	}
	
	@SuppressWarnings("unchecked")
	public String dType(int index) {
		ArrayList<Object> column = (ArrayList<Object>) columns.get(index);
		if (column.get(0) instanceof Integer) {
			for (Object o : column) {
				if (o instanceof String) {
					return "String";
				}
			}
			return "Integer";
		} else {
			return "String";
		}
	}
	
	@SuppressWarnings("unchecked")
	public String dType(String colname) {
		int idx = Arrays.asList(headers.split(" ")).indexOf(colname);
		ArrayList<Object> column = (ArrayList<Object>) columns.get(idx);
		if (column.get(0) instanceof Integer) {
			for (Object o : column) {
				if (o instanceof String) {
					return "String";
				}
			}
			return "Integer";
		} else {
			return "String";
		}
	}
	
	//select rows of interest and use them to build new dataframe
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
	
	//select columns of interest and use them to build new dataframe
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
	
	//method to build dataframe starting from arraylist of columns
	//loop through each column and add elements to the corresponding row using indexes
	//when going through first row, create the rows and add the first element
	//when going through next columns, retrieve the partially filled row and add new element
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
	
	
	//method to build dataframe starting from arraylist of rows
	//start by determining the number of columns, which is equal to the number of elements in
	//the header; create the necessary number of empty columns
	//loop through each row and add elements to the corresponding column using indexes
	//when going through rows, retrieve the row of interest and add new element
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
	
	@SuppressWarnings("unchecked")
	public MyDataFrame sort(int index) {
		List<Object> new_columns = new ArrayList<>();
		ArrayList<Integer> sorted_indexes = new ArrayList<Integer>();
		if (dType(index).equals("String")) {
			ArrayList<String> column = (ArrayList<String>) columns.get(index);
			ArrayList<String> sorted_column = (ArrayList<String>) column.clone();
			ArrayList<String> unsorted_column = (ArrayList<String>) column.clone();
			Collections.sort(sorted_column);
			for (int i=0; i<sorted_column.size();i++) {
				String sort_entry = sorted_column.get(i);
				for (int j=0; j<unsorted_column.size();j++) {
					String unsort_entry = unsorted_column.get(j);
					if (unsort_entry.equals(sort_entry)) {
						if (!sorted_indexes.contains(j)) {
							sorted_indexes.add(j);
							break;
						}
					}
				}
			}
		} else {
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			ArrayList<Integer> sorted_column = (ArrayList<Integer>) column.clone();
			ArrayList<Integer> unsorted_column = (ArrayList<Integer>) column.clone();
			Collections.sort(sorted_column);
			for (int i=0; i<sorted_column.size();i++) {
				int sort_entry = sorted_column.get(i);
				for (int j=0; j<unsorted_column.size();j++) {
					int unsort_entry = unsorted_column.get(j);
					if (unsort_entry == sort_entry) {
						if (!sorted_indexes.contains(j)) {
							sorted_indexes.add(j);
							break;
						}
					}
				}
			}
		}
		for (Object c : columns) {
			ArrayList<Object> unsort_col = (ArrayList<Object>) c;
			ArrayList<Object> sort_col = new ArrayList<Object>();
			for (int idx : sorted_indexes) {
				sort_col.add(unsort_col.get(idx));
			}
			new_columns.add(sort_col);
		}
		return fromcols(new_columns,headers);
	}
	
	public MyDataFrame sort(String colname) {
		int index = Arrays.asList(headers.split(" ")).indexOf(colname);
		return sort(index);
	}
	
	@SuppressWarnings("unchecked")
	public MyDataFrame filter(String colname, String operator, String value) {
		int index = Arrays.asList(headers.split(" ")).indexOf(colname);
		List<Object> new_columns = new ArrayList<>();
		ArrayList<Integer>filtered_indexes = new ArrayList<Integer>();
		if (operator.equals("=")){
			ArrayList<String>column = (ArrayList<String>) columns.get(index);
			for (int i=0; i<column.size(); i++) {
				String entry = column.get(i);
				if (entry.equals(value)) {
					filtered_indexes.add(i);
				}
			}
		} else {
			System.out.println("Invalid operator");
		}
		for (Object c : columns) {
			ArrayList<Object> unfiltered_col = (ArrayList<Object>) c;
			ArrayList<Object> filtered_col = new ArrayList<Object>();
			for (int idx : filtered_indexes) {
				filtered_col.add(unfiltered_col.get(idx));
			}
			new_columns.add(filtered_col);
		}
		return fromcols(new_columns,headers);
	}
	
	@SuppressWarnings("unchecked")
	public MyDataFrame filter(String colname, String operator, int value) {
		int index = Arrays.asList(headers.split(" ")).indexOf(colname);
		List<Object> new_columns = new ArrayList<>();
		ArrayList<Integer>filtered_indexes = new ArrayList<Integer>();
		if (operator.equals("=")){
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			for (int i=0; i<column.size(); i++) {
				int entry = column.get(i);
				if (entry == value) {
					filtered_indexes.add(i);
				}
			}
		} else {
			System.out.println("Invalid operator");
		}
		for (Object c : columns) {
			ArrayList<Object> unfiltered_col = (ArrayList<Object>) c;
			ArrayList<Object> filtered_col = new ArrayList<Object>();
			for (int idx : filtered_indexes) {
				filtered_col.add(unfiltered_col.get(idx));
			}
			new_columns.add(filtered_col);
		}
		return fromcols(new_columns,headers);
	}
	
	@SuppressWarnings("unchecked")
	public Object getMin(int index) {
		if (new MyDataFrame(rows,columns,headers).dType(index)=="Integer") {
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			int min = (int) column.get(0);
			for(Integer i: column) {
			    if(i < min) min = i;
			}
			return min;
		} else {
			ArrayList<String>column = (ArrayList<String>) columns.get(index);
			String min = column.get(0);
			for(String i: column) {
			    if (i.compareTo(min)<0) {
			    	min = i;
			    }
			}
			return min;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getMin(String colname) {
		int index = Arrays.asList(headers.split(" ")).indexOf(colname);
		if (new MyDataFrame(rows,columns,headers).dType(index)=="Integer") {
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			int min = (int) column.get(0);
			for(Integer i: column) {
			    if(i < min) min = i;
			}
			return min;
		} else {
			ArrayList<String>column = (ArrayList<String>) columns.get(index);
			String min = column.get(0);
			for(String i: column) {
			    if (i.compareTo(min)<0) {
			    	min = i;
			    }
			}
			return min;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getMax(int index) {
		if (new MyDataFrame(rows,columns,headers).dType(index)=="Integer") {
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			int max = (int) column.get(0);
			for(Integer i: column) {
			    if(i > max) max = i;
			}
			return max;
		} else {
			ArrayList<String>column = (ArrayList<String>) columns.get(index);
			String max = column.get(0);
			for(String i: column) {
			    if (i.compareTo(max)>0) {
			    	max = i;
			    }
			}
			return max;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getMax(String colname) {
		int index = Arrays.asList(headers.split(" ")).indexOf(colname);
		if (new MyDataFrame(rows,columns,headers).dType(index)=="Integer") {
			ArrayList<Integer>column = (ArrayList<Integer>) columns.get(index);
			int max = (int) column.get(0);
			for(Integer i: column) {
			    if(i > max) max = i;
			}
			return max;
		} else {
			ArrayList<String>column = (ArrayList<String>) columns.get(index);
			String max = column.get(0);
			for(String i: column) {
			    if (i.compareTo(max)>0) {
			    	max = i;
			    }
			}
			return max;
		}
	}
}


