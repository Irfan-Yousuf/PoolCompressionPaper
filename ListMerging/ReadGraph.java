package ListMerging;


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
}//end class