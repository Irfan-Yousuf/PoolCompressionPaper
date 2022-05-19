package BackLinks;

import java.util.ArrayList;

class Utility{
	
		
	public void EliasCodeEN(ArrayList<Vertex> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){			
			for(int j=0; j<vList.get(i).NeighborList.size(); j++){
				val = vList.get(i).NeighborList.get(j).neighborID;
				cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
				cval = 2*cval + 1;
				sum = sum + cval;
			}
		}
		sum = sum + vList.size(); //Sign Bits
		System.out.println("Bits Required for Elias coding of ExtraNodes: " + sum);		
	}
}
