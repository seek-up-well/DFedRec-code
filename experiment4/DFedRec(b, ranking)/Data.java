
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.LinkedList;

public class Data 
{
	// === Configurations	
	// the number of latent dimensions
	public static int d = 20; 
	
	// tradeoff $\lambda$
	public static float lambda = 0.01f;
	
	// learning rate $\gamma$
	public static float gamma = 0.5f;
	
	// === Input data files
	public static String fnTrainingData = "";
	public static String fnTestData = "";
	public static String fnOutputData = "";
    
	public static int n = 943; // number of users
	public static int m = 1682; // number of items
	public static int num_test; // number of test triples of (user,item,rating)

	public static float MinRating = 1.0f; // minimum rating value (1 for ML100K, ML1M)
	public static float MaxRating = 5.0f; // maximum rating value
	
	// scan number over the whole data
	public static int num_iterations = 100;
    
	// the number of rating
	public static int rating_num = 0;
	
	// === training data 
	public static Set<Integer> trainUserNo;

	public static HashMap<Integer, HashMap<Integer, Double>> traningDataMap = new HashMap<Integer, HashMap<Integer, Double>>();
     
	// === test data
	public static int[] indexUserTest;
	public static int[] indexItemTest;
	public static float[] ratingTest;


	// === some statistics, start from index "1"
	public static HashSet<Integer>[] I_u;  //item set rated by user u


	public static int topK = 5;
	public static  HashSet<Integer> []I_u_no;

	public static  HashSet<Integer> []I_u_test;


	public static  LinkedList<Integer>[]I_u_no_copy;


	// === model parameters to learn, start from index "1"
	public static float[][] U;
	
	// clients
	public static Client client[];


	// === file operation
	public static FileWriter fw ;
	public static BufferedWriter bw;

}  