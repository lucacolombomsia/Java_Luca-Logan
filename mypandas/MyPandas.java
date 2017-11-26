package mypandas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPandas {
	
	public static void main(String[] args) throws IOException {
		MyDataFrame testDF = readCSV("test.csv");
		System.out.println(testDF);
	}

	@SuppressWarnings("unchecked")
	public static MyDataFrame readCSV(String filename) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		String line = null;
		List<Object> rows = new ArrayList <Object>();
		List<Object> columns = new ArrayList <Object>();
		int nrows =0;
		int ncols = 0;
		while((line = bufferedReader.readLine()) != null) {
			nrows++;
			ncols = line.split(",").length;
		}
		for (int n=0; n<ncols; n++) {
			List<Object> col = new ArrayList <Object>();
			columns.add(col);
		}

		bufferedReader.close();
		bufferedReader = new BufferedReader(new FileReader(filename));
		for (int i = 0; i<nrows;i++) {
			line = bufferedReader.readLine();
			List<Object> row = new ArrayList <Object>();
			String[] row_arr = line.split(",");
			for (int j = 0;j<row_arr.length;j++){
				if (isInteger(row_arr[j])) {
					int x = Integer.parseInt(row_arr[j]); 
					row.add(x);
					ArrayList<Object> column = (ArrayList<Object>) columns.get(j);
			        column.add(i,x);
			        columns.set(j, column);
				} else {
					String x = row_arr[j];
					row.add(x);
					ArrayList<Object> column = (ArrayList<Object>) columns.get(j);
			        column.add(i,x);
			        columns.set(j, column);
				}
            } rows.add(row);
		}
		return new MyDataFrame(rows,columns);
		
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

}
