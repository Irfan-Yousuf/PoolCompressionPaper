package BackLinks;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class ReadGraph{
		
		public ArrayList<Vertex> ReadAL(String file){
		
		ArrayList<Vertex>  aGraph = new ArrayList<Vertex>();
		
		System.out.println("Reading Graph: "+file);
		OutputFile out = new OutputFile();
		   try (BufferedReader br = new BufferedReader(new FileReader(out.outPutPath+file))) {			
			    String line;
			    String delimit = "\\s+";			   
			    while ((line = br.readLine()) != null) {
			   
			      String tokens[] =  line.split(delimit);
			      if(line!=null){
			    	  aGraph.add(new Vertex(Integer.parseInt(tokens[0])));
			  		  for(int i=1; i<tokens.length; i++){
			  			aGraph.get(aGraph.size()-1).NeighborList.add(new Neighbor(Integer.parseInt(tokens[i])));
			  		}
			      }
			    }
			} catch (Exception e) {	e.printStackTrace();} 
		   System.out.println("Graph Size: "+aGraph.size());
		   return aGraph;
	   }
		
	public static Object cloneAL(ArrayList<Vertex> AL){
			ArrayList<Vertex>  copy = new ArrayList<Vertex>();
			 Vertex v;
			 Neighbor nr;
			 for(int i=0; i<AL.size(); i++){
				 v = new Vertex(AL.get(i).vertexID);
				 copy.add(v);
				 for(int j=0; j<AL.get(i).NeighborList.size(); j++){
					 nr = new Neighbor(AL.get(i).NeighborList.get(j).neighborID);
					 copy.get(i).NeighborList.add(nr);
				 }
			 }		
			 return copy;		
		}
}//end class