
import java.io.IOException;
import java.util.*;
import java.lang.Math;
public class Train
{
	public static void train() throws IOException 
	{    	
		// =================================================================
		for (int iter = 0; iter < Data.num_iterations; iter++)
		{	
			// output each iteration result
			try {
				Data.bw.write("Iter:" + Integer.toString(iter) + "| ");
				Data.bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("Iter:" + Integer.toString(iter) + "| ");

			try {
				Test.test();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ====================================

			// ===========================================
			LinkedList<Integer> []I_u_copy = new LinkedList[Data.n + 1];
			for (int i = 1; i < I_u_copy.length; i++) {
				I_u_copy[i] = new LinkedList<Integer>(Data.I_u[i]);
			}


			LinkedList<Integer> []I_u_no_copy = new LinkedList[Data.n + 1];
			for (int i = 1; i < I_u_no_copy.length; i++) {
				I_u_no_copy[i] = new LinkedList<Integer>(Data.I_u_no[i]);
			}


			
			int rating_num = 0;
			while(rating_num < Data.rating_num) {
				float [][] received_grad = new float[Data.m+1][Data.d];
				float [] vnum = new float[Data.m+1];
				for (int userID : Data.trainUserNo) {
					
					// --- Calculate gradient via rated items 

					if(I_u_copy[userID].size() != 0)
					{
						int index = (int)(Math.random()*I_u_copy[userID].size());
						int index_neg =  (int)(Math.random()*I_u_no_copy[userID].size());

						int itemID = I_u_copy[userID].remove(index);
						int itemID_neg = I_u_no_copy[userID].get(index_neg);


						rating_num++;

						// prediction and error
						float pred = 0;
						float err = 0;
						for (int f=0; f<Data.d; f++)
						{
							pred += Data.U[userID][f] * Data.V[itemID][f];
						}
						err = 1.0f - pred;

						// prediction and error
						float pred_neg = 0;
						float err_neg = 0;
						for (int f=0; f<Data.d; f++)
						{
							pred_neg += Data.U[userID][f] * Data.V[itemID_neg][f];
						}
						err_neg = 0.0f - pred_neg;


						// --- update U, V
						for(int f=0; f<Data.d; f++)
						{
							float grad_U_f = -err * Data.V[itemID][f] + Data.lambda * Data.U[userID][f];
							float grad_V_f = -err * Data.U[userID][f] + Data.lambda * Data.V[itemID][f];

							float grad_U_f_neg = -err_neg * Data.V[itemID_neg][f] + Data.lambda * Data.U[userID][f];
							float grad_V_f_neg = -err_neg * Data.U[userID][f] + Data.lambda * Data.V[itemID_neg][f];

							Data.U[userID][f] = Data.U[userID][f] - Data.gamma * grad_U_f;
							Data.U[userID][f] = Data.U[userID][f] - Data.gamma * grad_U_f_neg;


							received_grad[itemID][f] = received_grad[itemID][f] + grad_V_f;
							received_grad[itemID_neg][f] = received_grad[itemID_neg][f] + grad_V_f_neg;

							/* Data.V[itemID][f] = Data.V[itemID][f] - Data.gamma * grad_V_f;	 */
						}
						vnum[itemID] = vnum[itemID] + 1 ;
						vnum[itemID_neg] = vnum[itemID_neg] + 1 ;
					}

				}

				for(int e = 0;e <= Data.m;e++)
					{
						if (vnum[e] != 0)
						{
							for(int f=0; f<Data.d; f++)
							{
								Data.V[e][f]  = Data.V[e][f]  - Data.gamma  * received_grad[e][f]/vnum[e];
							} 
						}
					}
			}
			// -----------------------	  

			Data.gamma = Data.gamma * 0.96f;
			// ===========================================
		}
	}
}
  