import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class ReadData {
	public static void readData() throws IOException {
		// ==========================================================
		// --- number of target training records

		Data.I_u_no = new HashSet[Data.n + 1];
		Data.I_u_test = new HashSet[Data.n + 1];
		Data.I_u = new HashSet[Data.n + 1];

    	Data.num_train_target = 0;
    	BufferedReader brTrain_num = new BufferedReader(new FileReader(Data.fnTrainingData));
    	String line = null;
    	while ((line = brTrain_num.readLine())!=null)
    	{
    		Data.num_train_target += 1;
    	}
    	System.out.println("num_train_target: " + Data.num_train_target);
		brTrain_num.close();

		// ----------------------------------------------------

		// ==========================================================
		// --- number of test records
		Data.num_test = 0;
		BufferedReader brTest = new BufferedReader(new FileReader(Data.fnTestData));
		line = null;
		while ((line = brTest.readLine()) != null) {
			Data.num_test += 1;
		}
		System.out.println("num_test: " + Data.num_test);
		// ----------------------------------------------------

		// ==========================================================

		// --- test data
		Data.indexUserTest = new int[Data.num_test];
		Data.indexItemTest = new int[Data.num_test];
		Data.ratingTest = new float[Data.num_test];
		// ----------------------------------------------------

		Data.indexUserTrain = new int[Data.num_train_target]; // start from index "0"
        Data.indexItemTrain = new int[Data.num_train_target];
        Data.ratingTrain = new float[Data.num_train_target];

		// ==========================================================
		// Training data: (userID,itemID,rating)
		int id_case=0;
		BufferedReader brTrain = new BufferedReader(new FileReader(Data.fnTrainingData));
		line = null;
		while ((line = brTrain.readLine()) != null) {
			String[] terms = line.split("\\s+|,|;");
			int userID = Integer.parseInt(terms[0]);
			int itemID = Integer.parseInt(terms[1]);
//			float rating = Float.parseFloat(terms[2]);

			if (Data.I_u[userID] == null) Data.I_u[userID] = new HashSet<Integer>();
			Data.I_u[userID].add(itemID);


			Data.indexUserTrain[id_case] = userID;
    		Data.indexItemTrain[id_case] = itemID;
    		Data.ratingTrain[id_case] = 1;
    		id_case+=1;
		}
		for(int j = 1 ; j <= Data.n;j++)
		{
			if (Data.I_u[j] == null) Data.I_u[j] = new HashSet<Integer>();
		}
		HashSet<Integer> set_all = new HashSet<Integer>();

		for (int j = 1; j <= Data.m; j++) {
			set_all.add(j);
		}
//		set_all.removeAll(Data.I_u);

		for(int j = 1 ; j <= Data.n;j++){
			Data.I_u_no[j] = new HashSet<Integer>();
			HashSet<Integer> copiedSet = new HashSet<Integer>(set_all);
			copiedSet.removeAll(Data.I_u[j]);
			Data.I_u_no[j] = copiedSet;
		}

		brTrain.close();
		System.out.println("Finished reading the training data");
		// ==========================================================

		// ==========================================================
		// Test data: (userID,itemID,rating)
		id_case = 0; // initialize it to zero
		brTest = new BufferedReader(new FileReader(Data.fnTestData));
		line = null;
		while ((line = brTest.readLine()) != null) {
			String[] terms = line.split("\\s+|,|;");
			int userID = Integer.parseInt(terms[0]);
			int itemID = Integer.parseInt(terms[1]);
//			float rating = Float.parseFloat(terms[2]);
			Data.indexUserTest[id_case] = userID;
			Data.indexItemTest[id_case] = itemID;
			Data.ratingTest[id_case] = 1;
			id_case += 1;
		}

		for (int t=0; t<Data.num_test; t++)
		{
			int userID = Data.indexUserTest[t];
			int itemID = Data.indexItemTest[t];
			if (Data.I_u_test[userID] == null) Data.I_u_test[userID] = new HashSet<Integer>();
			Data.I_u_test[userID].add(itemID);
		}


		brTest.close();
		System.out.println("Finished reading the test data");
		// ----------------------------------------------------

	}
}
  