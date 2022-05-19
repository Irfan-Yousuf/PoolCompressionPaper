package LayeredLabelPropagation;

import java.util.ArrayList;

class Utility{
	
	
	
	public void EliasCode(ArrayList<Node> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){
			val = vList.get(i).id;
			cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval = 2*cval + 1;
			sum = sum + cval;
			for(int j=0; j<vList.get(i).neighbors.size(); j++){
				val = vList.get(i).neighbors.get(j).intValue();
				cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
				cval = 2*cval + 1;
				sum = sum + cval;
			}
		}
		System.out.println("Bits Required for Elias coding of extraNodes: " + sum);
	}
}
