package SlashBurn;

import java.util.ArrayList;

class Utility{
	
	
	
	public void EliasCode(ArrayList<Vertex> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){
			val = vList.get(i).vertexID;
			cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval = 2*cval + 1;
			sum = sum + cval;
			for(int j=0; j<vList.get(i).NeighborList.size(); j++){
				val = vList.get(i).NeighborList.get(j).neighborID;
				cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
				cval = 2*cval + 1;
				sum = sum + cval;
			}
		}
		System.out.println("Bits Required for Elias coding of AL: " + sum);
	}
	
	public void EliasCode_noID(ArrayList<Vertex> vList){
		
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
		System.out.println("Bits Required for Elias coding of AL: " + sum);
	}
	public void EliasCodeInfo(ArrayList<Integer> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){
			val = vList.get(i);
			cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval = 2*cval + 1;
			sum = sum + cval;			
		}
		System.out.println("Bits Required for Elias coding of IL: " + sum);
	}	
}
