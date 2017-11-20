package hw6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

public class Hash_changedir {
	
	//ask user to manually input the number of buckets the hash function needs to map the data into
	//this method is used inside the next method, which performs input validation
	public static int get_size() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Type the number of buckets you want the hash function to divide data into. \n"
				+ "The possible choices are 100 or 200: ");
		int dim = scanner.nextInt();
		return dim;
	}
	
	/**
	  Method to set the number of buckets. It performs user input validation to make sure that
	  size is correctly specified (either 100 or 200).
	*/
	public static int set_size() {
		//will ask the user to reinput size if the user did not provide a valid size
		//input validation is such that it can deal with integers not equal to 100 or 200 as well as with
		//any other data type
		int size = 0;
		while (true) {
			try {
				size = get_size();
				if (size != 100 && size!= 200) {
			        throw new Exception();
			    }
				break;
			} catch (Exception e) {
				System.out.println("Not a valid entry.");
			}
		}
		return size;
	}
	
	
	/**
	  Reads the input file.
	  @param path The path where the file is located
	*/
	public static ArrayList<String> read_file(String path) throws IOException {
		File file = new File(path);
		ArrayList<String> input = new ArrayList<>();
		String line = new String(); //
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			while ((line = bufferedReader.readLine()) != null) {
				input.add(line);
			}
		}
		return input;
	}
	

	/**
	  This method takes an empty Array of buckets and uses the specified hashing function
	  to hash values from the input ArrayList into the various buckets.
	  It then returns the buckets with the values hashed into them.
	  @param fct The hashing function to be used
	  @param size The number of buckets the hash function needs to map the data into
	  @param values The input values that will be hashed into the buckets
	*/
	public static String[] fill_buckets(String fct, int size, ArrayList<String> values) {
		String buckets[] = new String[size];
		int key = -1;
		for (String value : values) {
			if (fct == "hashCode") {
				key = Math.abs(value.hashCode() % size);
			} if (fct == "ASCII") {
				key = ASCIIHash(value, size);
			} if (fct == "MyHash") {
				key = MyHash(value, size);
			}
			
			//if a bucket is empty, put the value in the bucket
			//if the bucket is not empty (ie if there is a collision) append the new value to the ones
			//that were already in the bucket, separated by a comma and a space
			if (buckets[key] == null) {
				buckets[key] = value;
			} else {
				StringJoiner joiner = new StringJoiner(", ");
				joiner.add(buckets[key]);
				joiner.add(value);
				buckets[key] = joiner.toString();
			}
		}
		return buckets;
	}
	
	
	//we want a txt file for each hashing function that contains the output of the fill_buckets method
	//this will be a list of buckets, some of which will be filled and some empty
	/**
	  This method writes to a file the output of a specified hashing function.
	  @param fct The name of the hashing function that was used
	  @param buckets The Array of filled buckets (output of fill_buckets method)
	  @param path The path of the file we want to write the output to
	*/
	public static void write_out(String fct, String[] buckets, String path) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		
		bw.write("Hashing using " + fct + " hash function:" + "\n" + "\n");
		for (int i = 0; i < buckets.length; i++) {
			if (buckets[i] == null) {
				bw.write(i+1 + ": EMPTY LINE..." + "\n" + "\n");
			} else {
				bw.write(i+1 + ": " + buckets[i] + "\n" + "\n");
			}
		}
		
		bw.close();
	}
	
	
	//the next few methods are all needed to calculate the population variance
	//this is one of the metrics that will be used to compare the performances of multiple hashing functions
	/**
	  This method returns the sum of the elements of an ArrayList of integers.
	  @param list The ArrayList of integers
	*/
	public static int sum(ArrayList<Integer> list) {
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i);
		}
		return sum;
	}
	
	/**
	  This method returns the sum of the elements of an ArrayList of doubles.
	  @param list The ArrayList of doubles
	*/
	public static double sum_dub(ArrayList<Double> list) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i);
		}
		return sum;
	}

	/**
	  This method returns the average of the elements of an ArrayList of integers.
	  @param list The ArrayList of integers
	*/
	public static double average(ArrayList<Integer> list) {
		double average = 1.0* sum(list) / list.size();
		return average;
	}

	/**
	  This method returns the population variance of the elements of an ArrayList of integers.
	  @param list The ArrayList of integers
	*/
	public static double variance(ArrayList<Integer> list) {
		ArrayList<Double> diff = new ArrayList<>();
		double mean = average(list);
		double x = 0;
		for (int n : list) {
			x = (1.0*n - mean) * (1.0*n - mean);
			diff.add(x);
		}
	    return sum_dub(diff) / list.size();
	}
	
	/*
	This method takes a series of buckets filled with a given hash function and computes the performance
	of the hashing function
	This method is used in combination with the next one to print the performance in a txt file
	We have two measures of the performance of the hashing function:
	1) % of filled buckets over total number of buckets. The higher the %, the better the performance.
	2) standard deviation of the number of entries per filled bucket. The smaller the standard deviation, 
  	   the better the performance (we want hash function to distribute entries uniformely across buckets).
	*/
	public static String[] perf_measure(String name, String[] buckets) {
		
		//count the number of filled buckets, the ones that are not empty
		//create an array lists that contains the number of entries in each non empty bucket
		int filled = 0;
		ArrayList<Integer> count = new ArrayList<>();
		for (int i = 0; i < buckets.length; i++) {
			if (buckets[i] != null) {
				filled++ ;
				count.add(buckets[i].split(", ").length);
			}
		}
		
		//divide the number of filled bucket by the total number of buckets to get % of filled buckets
		//round the percentage to one decimal
		double perc = 100.0*filled/buckets.length;
		perc = Math.round(perc * 10);
		perc = perc/10;
		
		//use the variance method defined above to compute the standard deviation in the number of entries across
		//the various filled buckets
		//the standard deviation is rounded to two decimals
		double stddev = Math.sqrt(variance(count));
		stddev = Math.round(stddev * 100);
		stddev = stddev/100;
		
		//the next lines of code prepare the output that will be printed in a new file by the next method
		String[] mystr = new String[3];
		mystr[0] = "Performance measures for the " + name + " hashing function.";
		mystr[1] = perc + "% of the buckets are filled.";
		mystr[2] = "The standard deviation in the number of entries for the filled buckets is " + stddev;
		return mystr;
	}
	
	//this method is a wrapper for previous method
	//it takes the output of the previous method (an Array of 3 strings) and prints it into a file
	public static void perf_measure_wrapper(String[] text, BufferedWriter bw) throws IOException {
		bw.write(text[0] + "\n" + text[1] + "\n" + text[2] + "\n" + "\n");
	}
	
	/**
	  This method performs the ASCII-based hashing requested in the text of the homework.
	  @param word The word that should be associated with a hash key
	  @param size The number of buckets in which the words need to be hashed into
	*/
	public static int ASCIIHash(String word, int size) {
		
		int key = 0 ;
		for (int i = 0; i < word.length(); i++) 
		{
			char character = word.charAt(i);
			key = key + (int) character;
		}
		
		return key % size;
	}
	
	
	/**
	  This method contains our custom hashing function.
	  This hashing function was inspired by a post on StackOverflow.
	  It is a variation of the hashCode() hashing function. 
	  @param word The word that should be associated with a hash key
	  @param size The number of buckets in which the words need to be hashed into
	*/
	public static int MyHash(String word, int size) {
		int hash = 7;
		for (int i = 0; i < word.length(); i++) {
			char character = word.charAt(i);
		    hash = hash*31 + (int) character;
		}
		
		return Math.abs(hash % size);
	}
	
	
	public static void main(String[] args) throws IOException {
		//only need to change the following line for this to work on your machine!
		String path = "/Users/luca/Dropbox/MSiA/MSIA-422_Python-and-Java/Java/HW6/";
		
		//read the data to be hashed into buckets
		ArrayList<String> inputs = read_file(path + "input.txt");
		
		//set size to 100 or 200
		int size = set_size();
		
		//fill the buckets
		String[] result1 = fill_buckets("ASCII", size, inputs);
		String[] result2 = fill_buckets("hashCode", size, inputs);
		String[] result3 = fill_buckets("MyHash", size, inputs);
		
		//write filled buckets to file
		write_out("ASCII", result1, path + "output1.txt");
		write_out("hashCode", result2, path + "output2.txt");
		write_out("MyHash custom", result3, path + "output3.txt");
		
		//write the performance measure to a new file
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + "perf-measure.txt"));
		perf_measure_wrapper(perf_measure("ASCII", result1), bufferedWriter);
		perf_measure_wrapper(perf_measure("hashCode", result2), bufferedWriter);
		perf_measure_wrapper(perf_measure("MyHash custom", result3), bufferedWriter);
		bufferedWriter.close();
		
	}
}

