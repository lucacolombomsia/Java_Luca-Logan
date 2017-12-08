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
		//set working directory
		String path = "/Users/luca/Dropbox/MSiA/MSIA-422_Python-and-Java/"
				+ "Java/Java_Luca-Logan/mypandas/";
		
		//read data from csv
		String readpath = path + "shortbaby.csv";
		MyDataFrame testDF = readCSV(readpath);
		
		//write the data to a csv
		String writepath = path + "output.csv";
		writeCSV(testDF, writepath);
		
		//read more data and concatenate with data read above
		//print concatenated dataframe to console
		readpath = path + "test.csv";
		MyDataFrame testDF2 = readCSV(readpath);
		System.out.println(concat(testDF, testDF2));
		
		//print first 2 rows
		System.out.println(testDF.head(2));
		
		//print last 5 rows
		System.out.println(testDF.tail(5));
		
		//print to console the type of two columns
		System.out.println(testDF.dType(4)+"\n");
		System.out.println(testDF.dType("name")+"\n");
		
		//slice the dataframe
		System.out.println(testDF.slice("state"));
		System.out.println(testDF.slice(2).tail(1));
		System.out.println(testDF.slice(new int[]{0,3,4}).head(1));
		System.out.println(testDF.slice(new String[]{"state", "name"}).head(1));
			
		//filter dataframe based on condition
		System.out.println(testDF.filter("sex", "=", "F"));
		System.out.println(testDF.filter("year", "=", 1939));
		
		//retrieve specific rows using indexing
		System.out.println(testDF.loc(4));
		System.out.println(testDF.loc(2,4));

		//sort the dataframe
		System.out.println(testDF.sort("name"));
		System.out.println(testDF.sort(4));
		
		//get minimum and maximum
		System.out.println(testDF.getMin(4)+"\n");
		System.out.println(testDF.getMax("name")+"\n");		
	}

	/**
	  Read a csv file row by row and use MyDataFrame.rows to convert it into a
	  MyDataFrame object.
	  @param filename The path where the file is located
	  @return A data frame with the information from the csv
	*/
	public static MyDataFrame readCSV(String filename) throws IOException {
		String line = null;
		List<Object> rows = new ArrayList <Object>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		
		//save headers
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
	
	
	/**
	  Write a dataframe to a csv file.
	  @param data The dataframe to be written to file
	  @param path The path where the file will be saved
	*/
	public static void writeCSV(MyDataFrame data, String path) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write(data.toString().replace(" ", ","));
		bw.close();
	}
	
	/**
	  Take two dataframes, concatenate them and return a new, longer dataframe.
	  @param df1 The first dataframe; it will be at the top of the concatenated dataframe
	  @param df2 The second dataframe; it will be appended below df1
	  @return The dataframe that contains all rows from df1 and df2
	*/
	public static MyDataFrame concat(MyDataFrame df1, MyDataFrame df2) {
		List<Object> rows = df1.rows;
		for (Object r : df2.rows) {
			rows.add(r);
		}
		return MyDataFrame.fromrows(rows, df1.headers);
	}
}
