
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;

public class Train{

	Train() {

	}

	public void train(int num_iterations) throws InterruptedException, BrokenBarrierException {
		// ==========================================================
		// --- Construct Clients
		Data.client = new Client[Data.n + 1];
		for (int u=1; u<Data.n+1; u++) {
			Data.client[u] = new Client(u);
		}

		// ----------------------------------------------------
		// --- Train
		for (int iter = 0; iter < num_iterations; iter++){

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

			
			for (int u=1; u<Data.n+1; u++) {
				Data.client[u].setIv(new LinkedList<Integer>(Data.I_u[u]));
			}

			int rating_num = 0;
			while(rating_num < Data.rating_num) {
				
				for (int u=1; u<Data.n+1; u++) {
					Data.client[u].setItemNum(new HashMap<Integer, Integer>());
					Data.client[u].setReceived_V(new float[Data.m+1][Data.d]);

				}

				for (int u=1; u<Data.n+1; u++) {

					// --- Calculate gradient via rated items 
					if(Data.client[u].getIu().size() != 0)
					{

						int index = (int)(Math.random()*Data.client[u].getIu().size());
						int i = Data.client[u].getIu().remove(index);

						int index_neg =  (int)(Math.random()*Data.I_u_no_copy[u].size());
						int itemID_neg = Data.I_u_no_copy[u].get(index_neg);


						rating_num++;
						float pred = 0; 
						for (int f=0; f<Data.d; f++)
						{
							pred += Data.U[u][f] * Data.client[u].getV()[i][f];
						}
						float error = (float) (1.0f - pred);


						// prediction and error
						float pred_neg = 0;
						float error_neg = 0;
						for (int f=0; f<Data.d; f++)
						{
							pred_neg += Data.U[u][f] * Data.client[u].getV()[itemID_neg][f];
						}
						error_neg = 0.0f - pred_neg;


						float grad_U[] = new float[Data.d];
						float grad_V[] = new float[Data.d];

						float grad_U_neg[] = new float[Data.d];
						float grad_V_neg[] = new float[Data.d];

						for(int f=0; f<Data.d; f++)
						{	
							// --- Calculate the gradients of user and item
							grad_U[f] = -error * Data.client[u].getV()[i][f] + Data.lambda * Data.U[u][f];
							grad_V[f] = -error * Data.U[u][f] + Data.lambda * Data.client[u].getV()[i][f];

							grad_U_neg[f] = -error_neg * Data.client[u].getV()[itemID_neg][f] + Data.lambda * Data.U[u][f];
							grad_V_neg[f] = -error_neg * Data.U[u][f] + Data.lambda * Data.client[u].getV()[itemID_neg][f];


							// --- Update user and item specific latent feature locally
							Data.U[u][f] = (float) (Data.U[u][f] - Data.client[u].getGamma() * grad_U[f]);
							Data.U[u][f] = (float) (Data.U[u][f] - Data.client[u].getGamma() * grad_U_neg[f]);


							Data.client[u].getV()[i][f] = (float) (Data.client[u].getV()[i][f] - Data.client[u].getGamma() * grad_V[f]);
							Data.client[u].getV()[itemID_neg][f] = (float) (Data.client[u].getV()[itemID_neg][f] - Data.client[u].getGamma() * grad_V_neg[f]);
//							received_grad[itemID_neg][f] = received_grad[itemID_neg][f] + grad_V_f_neg;



						}


//						if(iter < 19)
//						{
//							for (int j=1; j<Data.n+1; j++) {
//
//								if(Data.client[j].getIu_copy().contains(i))
//								{
//									Data.client[j].Received_V(i,Data.client[u].getV()[i]);
//								}
//
//							}
//
//						}else
//						{
						for (int j=1; j<Data.n+1; j++) {
							Data.client[j].Received_V(i, Data.client[u].getV()[i]);
							Data.client[j].Received_V(itemID_neg, Data.client[u].getV()[itemID_neg]);
						}
//						}

					}
				}

				for (int u=1; u<Data.n+1; u++) {
					// --- update Vi after all clients have exchanged their item gradients
					Data.client[u].updateV();
				}

			}

			for (int u=1; u<Data.n+1; u++) {
				Data.client[u].updatGamma(); 
			}
		}

	}
}  