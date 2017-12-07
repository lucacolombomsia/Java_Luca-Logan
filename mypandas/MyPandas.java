package mypandas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPandas {
	
	public static void main(String[] args) throws IOException {
		String path = "/Users/luca/Dropbox/MSiA/MSIA-422_Python-and-Java/"
				+ "Java/Java_Luca-Logan/mypandas/";
		String readpath = path + "shortbaby.csv";
		MyDataFrame testDF = readCSV(readpath);
		
		String writepath = path + "output.csv";
		writeCSV(testDF, writepath);
		
		readpath = path + "test.csv";
		MyDataFrame testDF2 = readCSV(readpath);
		System.out.println(concat(testDF, testDF2));
		
		System.out.println(testDF.head(5));
		System.out.println(testDF.loc(4));
		System.out.println(testDF.loc(2,4));
		
		System.out.println(testDF.slice("state"));
		System.out.println(testDF.slice(new int[]{0,3,4}));
		System.out.println(testDF.slice(new String[]{"state", "name"}));
		
		System.out.println(testDF.dType("occurrences"));
		System.out.println(testDF.dType("name"));
		
		System.out.println(testDF.getMin(4));
		System.out.println(testDF.getMax("name"));
		
	}

	public static MyDataFrame readCSV(String filename) throws IOException {
		String line = null;
		List<Object> rows = new ArrayList <Object>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		
		String head = bufferedReader.readLine().replace(",", " ").replace("\"", "");
		
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
		bufferedReader.close();
		return MyDataFrame.fromrows(rows, head);
	}

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(Exception e) { 
	        return false; 
	    }
	    return true;
	}
	
	public static void writeCSV(MyDataFrame data, String path) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write(data.toString().replace(" ", ","));
		bw.close();
	}
	
	public static MyDataFrame concat(MyDataFrame df1, MyDataFrame df2) {
		List<Object> rows = df1.rows;
		for (Object r : df2.rows) {
			rows.add(r);
		}
		return MyDataFrame.fromrows(rows, df1.headers);
	}
}
