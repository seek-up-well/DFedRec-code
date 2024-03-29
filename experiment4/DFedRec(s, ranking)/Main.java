import java.io.IOException;
import java.util.concurrent.*;

public class Main {

	public static void main(String[] args) {

		try {

			// 1. read configurations
			ReadConfigurations.readConfigurations(args);

			// 2. read training data and test data
			ReadData.readData();

			// 3. apply initialization
			Initialization.initialization();

			// 4. start server
			Server server = new Server();

			// 5. train
			long TIME_START_READ_DATA = System.currentTimeMillis();
			server.train(Data.T);
			long TIME_FINISH_READ_DATA = System.currentTimeMillis();
			System.out.println("Elapsed Time (train):"
					+ Float.toString((TIME_FINISH_READ_DATA - TIME_START_READ_DATA) / 1000F) + "s");
			try {
				Data.bw.write("Elapsed Time (train):"
						+ Float.toString((TIME_FINISH_READ_DATA - TIME_START_READ_DATA) / 1000F) + "s\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 6. test
			// Test.test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}  