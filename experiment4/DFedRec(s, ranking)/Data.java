import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
	// === Configurations
	// the number of latent dimensions
	public static int d = 20;

	// tradeoff $\lambda$
	public static float lambda = 0.01f;

	// learning rate $\gamma$
	public static float gamma = 0.5f;

	// filling parameter $\rho$
	public static float rho = 0;

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
	public static int iter = 1;
	public static int inner_iter = 1;
	public static int T = 100;
	public static int T_prime = 50;

	// === training data
	public static Set<Integer> trainUserNo;
	// training tuple (user id, (item id, rating value))
	public static HashMap<Integer, HashMap<Integer, Double>> traningDataMap = new HashMap<Integer, HashMap<Integer, Double>>();

	public static int sizeOfTrain = 0;
	
	// === test data
	public static int[] indexUserTest;
	public static int[] indexItemTest;
	public static float[] ratingTest;

	public static ConcurrentHashMap<Integer, HashSet<Integer>> I_u; // item set rated by user u

	// set of users who have truly or virtually rated item i
	public static ConcurrentHashMap<Integer, ArrayList<Integer>> U_i; 

	public static int s;

	public static HashSet<Integer> []I_u_;
//	public static int topK = 5;
	public static  HashSet<Integer>[]I_u_no;

	public static  HashSet<Integer> []I_u_test;

	public static  LinkedList<Integer>[]I_u_no_copy;


	public static HashSet<Integer> I; // item set

	// === model parameters to learn, start from index "1"
	public static float[][] U;
	public static float[][] V;
	public static int topK = 5;
	// clients
	public static volatile Client client[];

	// === file operation
	public static FileWriter fw;
	public static BufferedWriter bw;
}  