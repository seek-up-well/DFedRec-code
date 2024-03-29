
import java.io.*;

public class Main
{
    public static void main(String[] args) throws IOException 
	{


		System.out.println(args);
    	
		// 1. read configurations		
		ReadConfigurations.readConfigurations(args);

		// 2. read training data and test data
        ReadData.readData();
        
		// 3. apply initialization
		Initialization.initialization();

		// 4. training
		Train.train();
		
		// 5. test
		Test.test();		
    }
}
  