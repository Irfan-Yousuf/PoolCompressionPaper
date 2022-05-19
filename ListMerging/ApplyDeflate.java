package ListMerging;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

class ApplyDeflate{
	
	
	public void deflate(ArrayList<Vertex> mergeL, ArrayList<VertexLong> flagL){
		
		int inputBytes = 0, comBytes=0;
		ArrayList<Byte> byteData = new ArrayList<Byte>();
		ArrayList<Long> outData = new ArrayList<Long>();
		
		System.out.println("Applying Deflate Compressor....");
		for(int i=0; i<mergeL.size(); i++){ //Concatenate outB, outF
			for(int j=0; j<mergeL.get(i).NeighborList.size(); j++){
				outData.add((long) mergeL.get(i).NeighborList.get(j).neighborID);				
			}
			outData.add((long) 0); //outB is zero terminated
			for(int j=0; j<flagL.get(i).NeighborList.size(); j++){
				outData.add(flagL.get(i).NeighborList.get(j).neighborID);				
			}
			try {
				byteData = byteEncoder(outData); //byte Encoder
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inputBytes = inputBytes + byteData.size();
			
			try {
				comBytes = comBytes + deflateAlgo(byteData); //deflate Algorithm
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outData.clear(); byteData.clear();
		}
		System.out.println("Input Bytes: "+inputBytes+" Compressed Bytes: "+comBytes);
		System.out.println("Bits Required to store the graph under List Merging scheme: "+comBytes*8);
		
	}
	
	public ArrayList<Byte> byteEncoder(ArrayList<Long> orgData) throws DataFormatException{
		
		ArrayList<Byte> byteData = new ArrayList<Byte>();
		byte[] bytes;
		BigInteger big;
		for(int i=0; i<orgData.size(); i++){
			big   = BigInteger.valueOf(orgData.get(i).longValue());
			bytes = big.toByteArray();	
			for(int j=0; j<bytes.length; j++){
				byteData.add(new Byte(bytes[j]));
			}
		}
		return byteData;
	}
	
	public int deflateAlgo(ArrayList<Byte> bData) throws DataFormatException{
		
		byte[] bytes = new byte[bData.size()];
		for(int i=0; i<bData.size(); i++){
			bytes[i] = bData.get(i).byteValue();
		}
		Deflater d = new Deflater();
        d.setInput(bytes); 
        d.finish(); 
        byte output[] = new byte[bData.size()*4]; 
        int size = d.deflate(output);  // compress the data 
        d.end();
        
        return size;
       
	}
}