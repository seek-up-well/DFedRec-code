import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class ReadData {
	public static void readData() throws IOException {
		// ==========================================================
		// --- some statistics, start from index "1"
		Data.I_u = new ConcurrentHashMap<Integer, HashSet<Integer>>();
		for (int u = 1; u <= Data.n; ++u) {
			Data.I_u.put(u, new HashSet<Integer>());
		}
		Data.I_u_no = new HashSet[Data.n + 1];
		Data.I_u_test = new HashSet[Data.n + 1];
		Data.I_u_ = new HashSet[Data.n + 1];


		Data.U_i = new ConcurrentHashMap<Integer, ArrayList<Integer>>();
		for (int i = 1; i <= Data.m; ++i) {
			Data.U_i.put(i, new ArrayList<Integer>());
		}
		// ----------------------------------------------------

		// ==========================================================
		Data.trainUserNo = new HashSet<Integer>();
		Data.I = new HashSet<Integer>();
		for (int i = 1; i <= Data.m; ++i) {
			Data.I.add(i);
		}
		// ----------------------------------------------------

		// ==========================================================
		// --- number of test records
		Data.num_test = 0;
		BufferedReader brTest = new BufferedReader(new FileReader(Data.fnTestData));
		String line = null;
		while ((line = brTest.readLine()) != null) {
			Data.num_test += 1;
		}
		System.out.println("num_test: " + Data.num_test);
		try {
			Data.bw.write("num_test: " + Data.num_test + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ----------------------------------------------------

		// ==========================================================
		// --- test data
		Data.indexUserTest = new int[Data.num_test];
		Data.indexItemTest = new int[Data.num_test];
		Data.ratingTest = new float[Data.num_test];
		// ----------------------------------------------------

		// ==========================================================
		// Training data: (userID,itemID,rating)
		BufferedReader brTrain = new BufferedReader(new FileReader(Data.fnTrainingData));
		line = null;
		while ((line = brTrain.readLine()) != null) {
			String[] terms = line.split("\\s+|,|;");
			int userID = Integer.parseInt(terms[0]);
			int itemID = Integer.parseInt(terms[1]);
//			float rating = Float.parseFloat(terms[2]);
			Data.I_u.get(userID).add(itemID);
			Data.U_i.get(itemID).add(userID);

			if (Data.I_u_[userID] == null) Data.I_u_[userID] = new HashSet<Integer>();
			Data.I_u_[userID].add(itemID);



			if (Data.traningDataMap.containsKey(userID)) {
				HashMap<Integer, Double> itemRatingMap = Data.traningDataMap.get(userID);
				itemRatingMap.put(itemID, (double) 1);
				Data.traningDataMap.put(userID, itemRatingMap);
			} else {
				HashMap<Integer, Double> itemRatingMap = new HashMap<Integer, Double>();
				itemRatingMap.put(itemID, (double) 1);
				Data.traningDataMap.put(userID, itemRatingMap);
			}
			Data.trainUserNo.add(userID);
			Data.sizeOfTrain++;
		}

		for(int j = 1 ; j <= Data.n;j++)
		{
			if (Data.I_u_[j] == null) Data.I_u_[j] = new HashSet<Integer>();
		}
		HashSet<Integer> set_all = new HashSet<Integer>();

		for (int j = 1; j <= Data.m; j++) {
			set_all.add(j);
		}
//		set_all.removeAll(Data.I_u);

		for(int j = 1 ; j <= Data.n;j++){
			Data.I_u_no[j] = new HashSet<Integer>();
			HashSet<Integer> copiedSet = new HashSet<Integer>(set_all);
			copiedSet.removeAll(Data.I_u_[j]);
			Data.I_u_no[j] = copiedSet;
		}

		Data.I_u_no_copy = new LinkedList[Data.n + 1];
		for (int i = 1; i < Data.I_u_no_copy.length; i++) {
			Data.I_u_no_copy[i] = new LinkedList<Integer>(Data.I_u_no[i]);
		}




		brTrain.close();
		System.out.println("Finished reading the training data");
		try {
			Data.bw.write("Finished reading the training data" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ==========================================================

		// ==========================================================
		// Test data: (userID,itemID,rating)
		int id_case = 0; // initialize it to zero
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
		try {
			Data.bw.write("Finished reading the test data" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ----------------------------------------------------

	}
}
  