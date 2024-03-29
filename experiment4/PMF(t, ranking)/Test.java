
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import  java.lang.Math;

import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Test
{


    public static void test() throws IOException 
	{

//		float sum_fenzi = 0.0f;
//		float sum_fenmu = 0.0f;
//		float user_rec = 0.0f;
//
//		for(int u=1; u<=Data.n; u++){
//			//没有测试数据就忽略
//			if (Data.I_u_test[u] == null){
//				continue;
//			}
//
//			HashMap<Integer, Float> item2Prediction = new HashMap<Integer, Float>();
//			item2Prediction.clear();
//
//			for(int i=1; i<=Data.m; i++)
//			{
//				if ( Data.I_u_no[u].contains(i) ){
//					// --- prediction via inner product
//					float pred = 0f;
//					for (int f=0; f<Data.d; f++)
//					{
//						pred += Data.U[u][f]*Data.V[i][f];
//					}
//					item2Prediction.put(i, pred);
//				}
//			}
//
//
//			List<Map.Entry<Integer, Float>> sortedList = item2Prediction.entrySet().stream()
//					.sorted(Map.Entry.<Integer, Float>comparingByValue().reversed())
//					.limit(5)
//					.collect(Collectors.toList());
//			HashSet<Integer> resultSet = new HashSet<>();
//			for (Map.Entry<Integer, Float> entry : sortedList){
//				resultSet.add(entry.getKey());
//			}
//
//			float temp1 = 0.0f;
//			float temp2 = 0.0f;
//			for (Integer num : resultSet){
//				if(Data.I_u_test[u].contains(num)){
//					temp1 += 1.0;
//				}
//			}
//			temp2 = temp1 / Data.I_u_test[u].size();
//			temp1  = temp1 / 5;
//
//			sum_fenzi += temp1;
//			user_rec += temp2;
//			sum_fenmu += 1.0;
//		}
//
//
//		System.out.println("pre@5:" + Float.toString(sum_fenzi/sum_fenmu));
//		System.out.println("rec@5:" + Float.toString(user_rec/sum_fenmu));






		// ==========================================================
		float[] PrecisionSum = new float[Data.topK+1];
		float[] RecallSum = new float[Data.topK+1];
		float[] F1Sum = new float[Data.topK+1];
		float[] NDCGSum = new float[Data.topK+1];
		float[] OneCallSum = new float[Data.topK+1];
		float MRRSum = 0;
		float MAPSum = 0;
		float ARPSum = 0;
		float AUCSum = 0;

		// --- calculate the best DCG, which can be used later
		float[] DCGbest = new float[Data.topK+1];
		for (int k=1; k<=Data.topK; k++)
		{
			DCGbest[k] = DCGbest[k-1];
			DCGbest[k] += 1/Math.log(k+1);
		}

		int  UserNum_TestData = 0;
		for(int u=1; u<=Data.n; u++) {

			if (Data.I_u_test[u] != null) {
				UserNum_TestData += 1;
			}
		}




		for(int u=1; u<=Data.n; u++)
		{
			//没有测试数据就忽略
			if (Data.I_u_test[u] == null){
				continue;
			}

			int ItemNum_u_TestData = Data.I_u_test[u].size();
			HashSet<Integer> ItemSet_u_TestData = Data.I_u_test[u];


			HashMap<Integer, Float> item2Prediction = new HashMap<Integer, Float>();
			item2Prediction.clear();

			for(int i=1; i<=Data.m; i++)
			{

				if ( !Data.I_u_no[u].contains(i) )
					continue;

				// --- prediction via inner product
				float pred = 0f;
				for (int f=0; f<Data.d; f++)
				{
					pred += Data.U[u][f]*Data.V[i][f];
				}
				item2Prediction.put(i, pred);
			}

			// --- sort
			List<Map.Entry<Integer,Float>> listY =
					new ArrayList<Map.Entry<Integer,Float>>(item2Prediction.entrySet());
			Collections.sort(listY, new Comparator<Map.Entry<Integer,Float>>()
			{
				public int compare( Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2 )
				{
					return o2.getValue().compareTo( o1.getValue() );
				}
			});


			// ===========================================================
			// === Evaluation: TopK Result
			// --- Extract the topK recommended items
			int k=1;
			int[] TopKResult = new int [Data.topK+1];
			Iterator<Entry<Integer, Float>> iter = listY.iterator();
			while (iter.hasNext())
			{
				if(k>Data.topK)
					break;

				Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) iter.next();
				int itemID = entry.getKey();
				TopKResult[k] = itemID;
				k++;
			}

			// --- TopK evaluation
			int HitSum = 0;
			float[] DCG = new float[Data.topK+1];
			float[] DCGbest2 = new float[Data.topK+1];
			for(k=1; k<=Data.topK; k++)
			{
				// ---
				DCG[k] = DCG[k-1];
				int itemID = TopKResult[k];
				if ( ItemSet_u_TestData.contains(itemID) )
				{
					HitSum += 1;
					DCG[k] += 1 / Math.log(k+1);
				}
				// --- precision, recall, F1, 1-call
				float prec = (float) HitSum / k;
				float rec = (float) HitSum / ItemNum_u_TestData;
				float F1 = 0;
				if (prec+rec>0)
					F1 = 2 * prec*rec / (prec+rec);
				PrecisionSum[k] += prec;
				RecallSum[k] += rec;
				F1Sum[k] += F1;
				// --- in case the the number relevant items is smaller than k
				if (ItemSet_u_TestData.size()>=k)
					DCGbest2[k] = DCGbest[k];
				else
					DCGbest2[k] = DCGbest2[k-1];
				NDCGSum[k] += DCG[k]/DCGbest2[k];
				// ---
				OneCallSum[k] += HitSum>0 ? 1:0;
			}
			// ===========================================================

			// ===========================================================
			// === Evaluation: Reciprocal Rank
			if (true) {
				int p = 1;
				iter = listY.iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) iter.next();
					int itemID = entry.getKey();

					// --- we only need the position of the first relevant item
					if (ItemSet_u_TestData.contains(itemID))
						break;

					p += 1;
				}
				MRRSum += 1 / (float) p;
			}
			// ===========================================================

			// ===========================================================
			// === Evaluation: Average Precision
			if (true) {
				int p = 1; // the current position
				float AP = 0;
				int HitBefore = 0; // number of relevant items before the current item
				iter = listY.iterator();
				while (iter.hasNext()) {
					Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) iter.next();
					int itemID = entry.getKey();

					if (ItemSet_u_TestData.contains(itemID)) {
						AP += 1 / (float) p * (HitBefore + 1);
						HitBefore += 1;
					}
					p += 1;
				}
				MAPSum += AP / ItemNum_u_TestData;
			}
			// ===========================================================

			// ===========================================================
			// --- Evaluation: Relative Precision
			if (true){
				int p = 1; // the current position
				float RP = 0;
				iter = listY.iterator();
				while (iter.hasNext())
				{
					Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) iter.next();
					int itemID = entry.getKey();

					if(ItemSet_u_TestData.contains(itemID))
						RP += p;
					p += 1;
				}
				// ARPSum += RP / ItemSetWhole.size() / ItemNum_u_TestData;
				ARPSum += RP / item2Prediction.size() / ItemNum_u_TestData;
			}


			// ===========================================================

			// ===========================================================
			// --- Evaluation: AUC
			if (true) {
				int AUC = 0;
				for (int i : ItemSet_u_TestData) {
					float r_ui = item2Prediction.get(i);

					for (int j : item2Prediction.keySet()) {
						if (!ItemSet_u_TestData.contains(j)) {
							float r_uj = item2Prediction.get(j);
							if (r_ui > r_uj) {
								AUC += 1;
							}
						}
					}
				}

				AUCSum += (float) AUC / (item2Prediction.size() - ItemNum_u_TestData) / ItemNum_u_TestData;
			}
			// ===========================================================

		}


//		// --- number of test cases
//    	float mae=0;
//    	float rmse=0;
//
//    	for(int t=0; t<Data.num_test; t++)
//    	{
//    		int userID = Data.indexUserTest[t];
//    		int itemID = Data.indexItemTest[t];
//    		float rating = Data.ratingTest[t];
//
//    		// ===========================================
//    		// --- prediction via inner product
//    		float pred = 0;
//    		for (int f=0; f<Data.d; f++)
//    		{
//    			pred += Data.U[userID][f]*Data.V[itemID][f];
//    		}
//    		// ===========================================
//
//    		// ===========================================
//    		if(pred < Data.MinRating) pred = Data.MinRating;
//    		if(pred > Data.MaxRating) pred = Data.MaxRating;
//
//			float err = pred-rating;
//			mae += Math.abs(err);
//			rmse += err*err;
//    		// ===========================================
//    	}
//
//

		// =========================================================
		// --- the number of users in the test data
		System.out.println( "The number of users in the test data: " + Integer.toString(UserNum_TestData) );

		// --- precision@k
		for(int k=1; k<=Data.topK; k++)
		{
			float prec = PrecisionSum[k]/UserNum_TestData;
			System.out.println("Prec@"+Integer.toString(k)+":"+Float.toString(prec));
		}
		// --- recall@k
		for(int k=1; k<=Data.topK; k++)
		{
			float rec = RecallSum[k]/UserNum_TestData;
			System.out.println("Rec@"+Integer.toString(k)+":"+Float.toString(rec));
		}
		// --- F1@k
		for(int k=1; k<=Data.topK; k++)
		{
			float F1 = F1Sum[k]/UserNum_TestData;
			System.out.println("F1@"+Integer.toString(k)+":"+Float.toString(F1));
		}
		// --- NDCG@k
		for(int k=1; k<=Data.topK; k++)
		{
			float NDCG = NDCGSum[k]/UserNum_TestData;
			System.out.println("NDCG@"+Integer.toString(k)+":"+Float.toString(NDCG));
		}
		// --- 1-call@k
		for(int k=1; k<=Data.topK; k++)
		{
			float OneCall = OneCallSum[k]/UserNum_TestData;
			System.out.println("1-call@"+Integer.toString(k)+":"+Float.toString(OneCall));
		}
		// --- MRR
		float MRR = MRRSum/UserNum_TestData;
		System.out.println("MRR:" + Float.toString(MRR));
		// --- MAP
		float MAP = MAPSum/UserNum_TestData;
		System.out.println("MAP:" + Float.toString(MAP));
		// --- ARP
		float ARP = ARPSum/UserNum_TestData;
		System.out.println("ARP:" + Float.toString(ARP));
		// --- AUC
		float AUC = AUCSum/UserNum_TestData;
		System.out.println("AUC:" + Float.toString(AUC));
		// =========================================================


//    	float MAE = 0.1f;
//    	float RMSE = 0.1f;
//
//    	//output result
//    	String result = "MAE:" + Float.toString(MAE) +  "| RMSE:" + Float.toString(RMSE);
//    	System.out.println(result);
//    	try {
//			Data.bw.write(result +"\r\n");
//			Data.bw.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
    // =============================================================
}  