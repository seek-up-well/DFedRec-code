import java.io.IOException;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;

public class Server {

	Server() {
	}

	public void train(int num_iterations) throws InterruptedException, BrokenBarrierException {
		// --- Construct Clients
		if (Data.fnTrainingData.contains("Netflix5K5K")) {
			for (int u = 1; u <= Data.n; u++) {
				Data.client[u] = new Client(u);
			}
		} else {
			for (int u : Data.trainUserNo) {
				Data.client[u] = new Client(u);
			}
		}

		// create user graph
		for (int u = 1; u <= Data.n; ++u) {
			ArrayList<Integer> list_I_u = new ArrayList<Integer>(Data.I_u.get(u));
			ArrayList<Integer> I_u_sample = new ArrayList<Integer>(Data.I);
			I_u_sample.removeAll(list_I_u);
			int sample_number = (int) (Data.rho * list_I_u.size() < I_u_sample.size() ? Data.rho * list_I_u.size()
					: I_u_sample.size());
			int fake_item_num = 0;
			while (fake_item_num < sample_number) {
				int randomIndex = (int) (Math.random() * I_u_sample.size());
				int itemID = I_u_sample.get(randomIndex);
				if (!Data.U_i.get(itemID).contains(u)) {
					Data.U_i.get(itemID).add(u);
					fake_item_num++;
				}
			}
		}

		Data.s = 0;
		for (int i = 1; i <= Data.m; ++i) {
			int size = Data.U_i.get(i).size();
			if (size > Data.s)
				Data.s = size;
		}
		System.out.println("s = " + Data.s);
		try {
			Data.bw.write("s = " + Data.s + "\n");
			Data.bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i <= Data.m; ++i) {
			if (!Data.U_i.get(i).isEmpty()) {
				ArrayList<Integer> list_U_i = new ArrayList<Integer>(Data.U_i.get(i));
				int randomIndex = (int) (Math.random() * list_U_i.size());
				int userID = list_U_i.get(randomIndex);
				Data.client[userID].receiveV(i);

			}
		}

		System.out.println("Iter\tMAE\tRMSE\tNaN");
		try {
			Data.bw.write("Iter\tMAE\tRMSE\tNaN\n");
			Data.bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Data.iter = 1; Data.iter <= num_iterations; Data.iter++) {
			// =======================================================
			// --- Each client download master model
			// All the users begin to update the user-specific latent feature and send back
			// intermediate gradient
			List<Integer> userNoList = new ArrayList<Integer>(Data.trainUserNo);

			for (Data.inner_iter = 1; Data.inner_iter <= Data.s; Data.inner_iter++)  {
				for (int u : userNoList) {
					Data.client[u].receiveData();
				}
				for (int u : userNoList) {
					Data.client[u].sendData();
				}
			}
			Data.gamma *= 0.96f;
			if (Data.iter >= Data.T_prime) {
				try {
					System.out.print(Integer.toString(Data.iter) + "\t");
					try {
						Data.bw.write(Integer.toString(Data.iter) + "\t");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Test.test();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} // out of iterations
	} // out of train
}  