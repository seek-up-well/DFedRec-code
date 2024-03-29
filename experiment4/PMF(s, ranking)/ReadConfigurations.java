import java.io.*;
import java.util.Arrays;

 
public class ReadConfigurations
{
	public static void readConfigurations(String[]args) throws IOException 
	{
        // ==========================================================
		// read the configurations
		for (int k=0; k < args.length; k++)
		{
			if (args[k].equals("-d")) Data.d = Integer.parseInt(args[++k]);
			else if (args[k].equals("-lambda")) Data.lambda = Float.parseFloat(args[++k]);        		
			else if (args[k].equals("-gamma")) Data.gamma = Float.parseFloat(args[++k]);
			else if (args[k].equals("-fnTrainingData")) Data.fnTrainingData = args[++k];
			else if (args[k].equals("-fnTestData")) Data.fnTestData = args[++k];
			else if (args[k].equals("-MinRating")) Data.MinRating = Float.parseFloat(args[++k]);
			else if (args[k].equals("-MaxRating")) Data.MaxRating = Float.parseFloat(args[++k]);    		
			else if (args[k].equals("-n")) Data.n = Integer.parseInt(args[++k]);
			else if (args[k].equals("-m")) Data.m = Integer.parseInt(args[++k]);
			else if (args[k].equals("-num_iterations")) Data.num_iterations = Integer.parseInt(args[++k]); 		

		}
        // ==========================================================
        
        // ==========================================================
		// print the configurations
		System.out.println(Arrays.toString(args));
		System.out.println("d: " + Integer.toString(Data.d));    	
		System.out.println("lambda: " + Float.toString(Data.lambda));
		System.out.println("gamma: " + Float.toString(Data.gamma));    		
		System.out.println("fnTrainingData: " + Data.fnTrainingData);
		System.out.println("fnTestData: " + Data.fnTestData);
		System.out.println("MinRating: " + Float.toString(Data.MinRating));
		System.out.println("MaxRating: " + Float.toString(Data.MaxRating));
		System.out.println("n: " + Integer.toString(Data.n));
		System.out.println("m: " + Integer.toString(Data.m));    	    	
		System.out.println("num_iterations: " + Integer.toString(Data.num_iterations));
        // ==========================================================
	}
}
  