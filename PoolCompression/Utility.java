package PoolCompression;

import java.util.ArrayList;


class Utility{
	
	/***************************************************************
	 * Calculate the number of bits required to store a graph
	 * under Elias Gamma Encoding Scheme.
	 * Input: A graph (Adjacency List representation)
	 * Output: Number of Bits
	 * @param vList
	 ***************************************************************/
	
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
		System.out.println("Bits Required for Elias coding of Original Graph: " + sum);
	}
	/*****************************************************************************
	 * Elias Coding of Pool Data. We do not actually encode it rather we only
	 * calculate the number of bits required for Elias Coding. We encode both the number of nodes
	 * in a block (i.e. pool) and list of members in that block.
	 * Input: Pool Data after Delta Encoding (Gap Encoding)
	 * Output: Number of Bits required to store this data.
	 * @param vList
	 ****************************************************************************/
	public void EliasCodePoolData(ArrayList<Vertex> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){
			val = vList.get(i).NeighborList.size(); //number of nodes in the block (or pool)
			cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval = 2*cval + 1;
			sum = sum + cval;
			for(int j=0; j<vList.get(i).NeighborList.size(); j++){
				val = vList.get(i).NeighborList.get(j).neighborID; // pool members
				cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
				cval = 2*cval + 1;
				sum = sum + cval;
			}
		}
		System.out.println("Bits Required for Elias coding of PoolDelta: " + sum);
	}
	/*****************************************************************************
	 * Elias Coding of Position Data. We do not actually encode it rather we only
	 * calculate the number of bits required for Elias Coding. We encode both the degree
	 * of a node and its neighbors.
	 * Input: Position Data after Delta Encoding (Gap Encoding)
	 * Output: Number of Bits required to store this data.
	 * @param vList
	 ****************************************************************************/
	public void EliasCodePositionData(ArrayList<Vertex> vList){
		
		int val = 0;
		int cval = 0;   
		int sum = 0;
		for(int i =0; i< vList.size(); i++){
			val = vList.get(i).degree; // node degree
			cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
			cval = 2*cval + 1;
			sum = sum + cval;			
			for(int j=0; j<vList.get(i).NeighborList.size(); j++){
				val = vList.get(i).NeighborList.get(j).neighborID; // neighbor ID
				cval = (int) java.lang.Math.floor(java.lang.Math.log(val)/java.lang.Math.log(2));
				cval = 2*cval + 1;
				sum = sum + cval;
			}
		}
		System.out.println("Bits Required for Elias Coding of PositionData: " + sum);
	}

}
