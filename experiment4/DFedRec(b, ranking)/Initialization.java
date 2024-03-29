public class Initialization
{ 
    public static void initialization()
	{
    	// --- model parameters to learn, start from index "1"
        Data.U = new float[Data.n+1][Data.d];
        
    	// ======================================================    	
    	// --- initialization of U
    	for (int u=1; u<Data.n+1; u++)
    	{
    		for (int f=0; f<Data.d; f++)
    		{
    			Data.U[u][f] = (float) ( (Math.random()-0.5)*0.01 );
    		}
    	}

    }
}
  