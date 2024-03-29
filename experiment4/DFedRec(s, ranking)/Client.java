import java.util.HashSet;

public class Client {
	private int No; // No of Client u
	private HashSet<Integer> Q_u = new HashSet<Integer>();
	private HashSet<Integer> Q_u_prime = new HashSet<Integer>();
	private HashSet<Integer> Q_u_bar = new HashSet<Integer>();
	private float[][] Q_u_test = new float[Data.m + 1][Data.d];

	// initialization of client u
	Client(int No) {
		this.No = No;
	}

	public void sendData() {
		for (int itemID : this.Q_u) {
			if (Data.I_u.get(this.No).contains(itemID)) {
				// prediction value

				float pred = 0;
				for (int f = 0; f < Data.d; f++) {
					pred += Data.U[this.No][f] * Data.V[itemID][f];
				}

				float error = (float) (1.0 - pred);

				int idx3 =  (int)(Math.random()*Data.I_u_no_copy[this.No].size());
				int j = Data.I_u_no_copy[this.No].get(idx3);

				float r_j = 0.0f;
				float pred_j = 0;
				for (int f = 0; f < Data.d; f++) {
					pred_j += Data.U[this.No][f] * Data.V[j][f];
				}

				float error_j = (float) (r_j - pred_j);


				for (int f = 0; f < Data.d; f++) {
					Data.U[this.No][f] -= Data.gamma * (-error * Data.V[itemID][f] + Data.lambda * Data.U[this.No][f]);
					Data.V[itemID][f] -= Data.gamma * (-error * Data.U[this.No][f] + Data.lambda * Data.V[itemID][f]);

					Data.U[this.No][f] -= Data.gamma * (-error_j * Data.V[j][f] + Data.lambda * Data.U[this.No][f]);
					Data.V[j][f] -= Data.gamma * (-error_j * Data.U[this.No][f] + Data.lambda * Data.V[j][f]);
				}

			}
		}

		if (Data.iter >= Data.T_prime && Data.inner_iter == Data.s) {
			sendV2All();
		}

		HashSet<Integer> tmp_item = new HashSet<Integer>();
		for (int itemID : this.Q_u_prime) {
			if (Math.random() < (float) Data.U_i.get(itemID).size() / (float) Data.s) {
				int randomIndex = (int) (Math.random() * Data.U_i.get(itemID).size());
				int userID = Data.U_i.get(itemID).get(randomIndex);
				Data.client[userID].receiveV(itemID);
			} else {
				tmp_item.add(itemID);
			}
		}
		this.Q_u_prime.clear();
		for (Integer itemID : tmp_item) {
			this.Q_u_prime.add(itemID);
		}

		for (int itemID : this.Q_u) {
			if (Math.random() < (float) Data.U_i.get(itemID).size() / (float) Data.s) {
				int randomIndex = (int) (Math.random() * Data.U_i.get(itemID).size());
				int userID = Data.U_i.get(itemID).get(randomIndex);
				Data.client[userID].receiveV(itemID);
			} else {
				this.Q_u_prime.add(itemID);
			}
		}
		this.Q_u.clear();
	}

	public void receiveData() {
		for (int itemID : this.Q_u_bar) {
			this.Q_u.add(itemID);
		}
		this.Q_u_bar.clear();
	}

	public void receiveV(Integer itemID) {
		this.Q_u_bar.add(itemID);
	}

	private void sendV(Integer itemID) {
		for (int f = 0; f < Data.d; ++f) {
			this.Q_u_test[itemID][f] = Data.V[itemID][f];
		}
		for (Integer userID : Data.trainUserNo) {
			if (userID != this.No) {
				Data.client[userID].receiveVFromAll(itemID, Data.V[itemID]);
			}
		}
	}

	public void sendV2All() {
		for (Integer itemID : this.Q_u) {
			sendV(itemID);
		}
		for (Integer itemID : this.Q_u_prime) {
			sendV(itemID);
		}
	}

	public void receiveVFromAll(int itemID, float[] V) {
		for (int f = 0; f < Data.d; ++f) {
			this.Q_u_test[itemID][f] = V[f];
		}
	}

	public float[] getV(Integer itemID) {
		return this.Q_u_test[itemID];
	}
}  