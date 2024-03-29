import java.util.HashMap;
import java.util.HashSet;

public class Data {
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

	public static int n = 943; // number of users
	public static int m = 1682; // number of items
	public static int num_test; // number of test triples of (user,item,rating)
	public static int num_received_clients = 1;
	public static float MinRating = 1.0f; // minimum rating value (1 for ML100K, ML1M)
	public static float MaxRating = 5.0f; // maximum rating value

	public static int num_train_target = 0;
	public static int[] indexUserTrain; // start from index "0"
	public static int[] indexItemTrain;
	public static float[] ratingTrain;

	// scan number over the whole data
	public static int num_iterations = 500;

	// === training data
	// training tuple (user id, (item id, rating value))
	public static HashMap<Integer, HashMap<Integer, Double>> traningDataMap = new HashMap<Integer, HashMap<Integer, Double>>();

	public static int sizeOfTrain = 0;


	public static HashSet<Integer> []I_u;
	public static int topK = 5;
	public static  HashSet<Integer>[]I_u_no;

	public static  HashSet<Integer> []I_u_test;




	// === test data
	public static int[] indexUserTest;
	public static int[] indexItemTest;
	public static float[] ratingTest;

	// === model parameters to learn, start from index "1"
	public static float[][] U;
	public static float[][] V;
}  