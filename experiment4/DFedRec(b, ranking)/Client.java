import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Client{
	private int userID;  // user ID of Client u
	private float gamma; // learning rate
	private int iter;  // iteration number
	// === model parameters to learn, start from index "1"
	private float[][] V;
	private List<Integer> I_u;   // rated item set of client u

	private List<Integer> I_u_copy;

	// key: itemID, value: the number of item of receiving from neighborhood
	private HashMap<Integer, Integer> itemNum;

	private float [][] received_V;


	// initialization of client u
	Client(int userID){

		this.userID = userID;
		this.gamma = Data.gamma;
		this.I_u = new LinkedList<Integer> (Data.I_u[this.userID]);
		this.I_u_copy = new LinkedList<Integer> (Data.I_u[this.userID]);

		this.received_V = new float[Data.m+1][Data.d];
        this.V = new float[Data.m+1][Data.d];
    	for (int i=1; i<Data.m+1; i++)
    	{
    		for (int f=0; f<Data.d; f++)
    		{
    			this.V[i][f] = (float) ( (Math.random()-0.5)*0.01 );
    		}
    	}

	}


	public void  Received_V(int itemID,float received_V[])
	{

		for (int f = 0; f < Data.d; f++) {
			this.received_V[itemID][f] +=  received_V[f];
		}

		if(itemNum.containsKey(itemID))
		{
			itemNum.put(itemID, itemNum.get(itemID) + 1);
		}
		else
		{
			itemNum.put(itemID, 1);
		}
	}

	public void updateV()
	{
		for (Entry<Integer, Integer> entrys : itemNum.entrySet()){
			int itemID = entrys.getKey();
			int num = entrys.getValue();

			for(int f=0; f<Data.d; f++)
			{
				this.received_V[itemID][f] /=  num;
				this.V[itemID][f] = this.received_V[itemID][f];
			}
		}
	}

	public void updatGamma()
	{
		this.gamma = (float) (this.gamma * 0.96);  //Decrease $\gamma$
		this.iter++;
	}

	public float[][] getV()
	{
		return this.V;
	}
	
	public List<Integer> getIu()
	{
		return this.I_u;
	}

	public List<Integer> getIu_copy()
	{
		return this.I_u_copy;
	}
	
	public float getGamma()
	{
		return this.gamma;
	}

	public void setReceived_V(float[][] received_V) {
		this.received_V = received_V;
	}

	public void setItemNum(HashMap<Integer, Integer> itemNum) {
		this.itemNum = itemNum;
	}
	public void setIv(LinkedList<Integer> I_u) {
		this.I_u = I_u;
	}
}  