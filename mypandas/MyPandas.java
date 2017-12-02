package mypandas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPandas {
	
	public static void main(String[] args) throws IOException {
		String path = "/Users/luca/Dropbox/MSiA/MSIA-422_Python-and-Java/"
				+ "Java/Java_Luca-Logan/mypandas/";
		path = path + "shortbaby.csv";
		MyDataFrame testDF = readCSV(path);
		System.out.println(testDF);
		
		System.out.println(testDF.head(5));
		System.out.println(testDF.tail(3));
	}

	@SuppressWarnings("unchecked")
	public static MyDataFrame readCSV(String filename) throws IOException {
		String line = null;
		List<Object> rows = new ArrayList <Object>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		while((line = bufferedReader.readLine()) != null) {
			List<Object> row = new ArrayList <Object>();
			String[] row_arr = line.split(",");
			for (int j = 0; j<row_arr.length; j++){
				if (isInteger(row_arr[j])) {
					int x = Integer.parseInt(row_arr[j]); 
					row.add(x);
				} else {
					String x = row_arr[j].replace("\"", "");
					row.add(x);
				}
            } rows.add(row);
		}
		return new MyDataFrame(rows);
	}

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(Exception e) { 
	        return false; 
	    }
	    return true;
	}

}
