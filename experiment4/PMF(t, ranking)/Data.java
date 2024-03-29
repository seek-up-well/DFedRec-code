
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Data 
{
	// === Configurations	
	// the number of latent dimensions
	public static int d = 20; 
	
    // tradeoff $\lambda$
	public static float lambda = 0.01f;
	 
	// learning rate $\gamma$
	public static float gamma = 0.01f;

	
	// === Input data files
	public static String fnTrainingData = "";
	public static String fnTestData = "";
	public static String fnOutputData = "";

	public static int n; // number of users
	public static int m; // number of items
	public static int num_test; // number of test triples of (user,item,rating)

	public static float MinRating = 1.0f; // minimum rating value (1 for ML100K and ML1M)
	public static float MaxRating = 5.0f; // maximum rating value

	// === scan number over the whole data
	public static int num_iterations = 0; 

	// the number of rating
	public static int rating_num = 0;
	
	// === training data 
	public static Set<Integer> trainUserNo;
	
	// === items rated by user u
	public static HashSet<Integer> []I_u;

	public static int topK = 5;
	public static  HashSet<Integer> []I_u_no;

	public static  HashSet<Integer> []I_u_test;


	// === training data
	public static float[][] ratings;

	// === test data
	public static int[] indexUserTest;
	public static int[] indexItemTest;
	public static float[] ratingTest;

	// === some statistics, start from index "1"
	public static int[] user_rating_number;
	
	// === model parameters to learn, start from index "1"
	public static float[][] U;
	public static float[][] V;
	
	// === file operation
	public static FileWriter fw ;
	public static BufferedWriter bw;
}
  