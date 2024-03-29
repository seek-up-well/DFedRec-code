import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.LinkedList;

public class Server {

	Server() {
	}

	public void train(int num_iterations) throws InterruptedException, BrokenBarrierException {

		// --- Train
		System.out.println("Iter\tMAE\tRMSE\tNaN");

		LinkedList<Integer>[]I_u_no_copy = new LinkedList[Data.n + 1];
		for (int i = 1; i < I_u_no_copy.length; i++) {
			I_u_no_copy[i] = new LinkedList<Integer>(Data.I_u_no[i]);
		}

		for (int iter = 1; iter <= num_iterations; iter++) {
			// =======================================================
			for (int it = 0; it < Data.num_train_target; it++) {
				int idx2 = (int) Math.floor(Math.random() * Data.num_train_target);
				int u = Data.indexUserTrain[idx2];
				int i = Data.indexItemTrain[idx2];

				int idx3 =  (int)(Math.random()*I_u_no_copy[u].size());
				int j =  I_u_no_copy[u].get(idx3);


				float r = 1.0f;
				float pred = 0;
				for (int f = 0; f < Data.d; f++) {
					pred += Data.U[u][f] * Data.V[i][f];
				}


				float r_j = 0.0f;
				float pred_j = 0;
				for (int f = 0; f < Data.d; f++) {
					pred_j += Data.U[u][f] * Data.V[j][f];
				}


				float error = (float) (r - pred);
				float error_j = (float) (r_j - pred_j);

				for (int f = 0; f < Data.d; f++) {
					Data.U[u][f] -= Data.gamma * (-error * Data.V[i][f] + Data.lambda * Data.U[u][f]);
					Data.V[i][f] -= Data.gamma * (-error * Data.U[u][f] + Data.lambda * Data.V[i][f]);

					Data.U[u][f] -= Data.gamma * (-error_j * Data.V[j][f] + Data.lambda * Data.U[u][f]);
					Data.V[j][f] -= Data.gamma * (-error_j * Data.U[u][f] + Data.lambda * Data.V[j][f]);

				}
			}
			Data.gamma *= 0.96f;
			System.out.print(Integer.toString(iter) + "\t");
			try {
				Test.test();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}