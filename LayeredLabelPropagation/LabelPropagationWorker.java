package LayeredLabelPropagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;

public class LabelPropagationWorker implements Callable<Boolean>{//, Future<Boolean> {

	private ArrayList<Integer> dominantLabels;
	private ArrayList<Integer> labelCounts;
	private Random randGen;
	ArrayList<Integer> kFreq             = new ArrayList<Integer>(); //Frequency of labels of neighborhood
	ArrayList<Integer> vFreq             = new ArrayList<Integer>(); //Frequency of labels in the whole graph
	ArrayList<Integer> finalLabels        = new ArrayList<Integer>();
	private int nodeId;
    double gamma;
	// Shared
	private ArrayList<Node> nodeList;

	public LabelPropagationWorker(ArrayList<Node> nodeList) {
		dominantLabels = new ArrayList<Integer>();
		labelCounts = new ArrayList<Integer>(nodeList.size());
		
		for (int i=0; i<nodeList.size(); i++) {
			labelCounts.add(Integer.valueOf(0));
			kFreq.add(Integer.valueOf(0));
			vFreq.add(Integer.valueOf(0));
		}
		randGen = new Random();
		
		this.nodeList = nodeList;//Shared reference
		
		//Node to process
		//this.nodeId=nodeId;
		
	//	System.out.println("Worker created.");
		
	}
	
	public void setNodeToProcess(int nodeId) {
		this.nodeId=nodeId;
	}

	@Override
	public Boolean call() {
		
		if (nodeId==-1) {
			return Boolean.FALSE;
		}

		boolean continueRunning = false;

		Collections.fill(labelCounts, Integer.valueOf(0));
		dominantLabels.clear();

		Node currentNode = nodeList.get(nodeId);
		int maxCount = 0;
		// Neighbors
		for (Integer neighborId : currentNode.getNeighbors()) {
			// for (int j=0; j<currentNode.getNeighbors().size(); j++) {
			int nLabel = nodeList.get(neighborId).getLabel();
			if (nLabel == 0)
				continue; // No label yet (only if initial labels are given?)

			int nLabelCount = labelCounts.get(nLabel) + 1;
			labelCounts.set(nLabel, nLabelCount);// Careful of wrapping
													// un-wrapping here!

			if (maxCount < nLabelCount) {
				maxCount = nLabelCount;
				dominantLabels.clear();
				dominantLabels.add(nLabel);
			} else if (maxCount == nLabelCount) {
				dominantLabels.add(nLabel);
			}
		}

		if (dominantLabels.size() > 0) {
			// Randomly select from dominant labels
			int rand = randGen.nextInt(dominantLabels.size());
			rand = dominantLabels.get(rand);

			// Check if *current* label of node is also dominant
			if (labelCounts.get(currentNode.getLabel()) != maxCount) {
				// it's not. continue
				continueRunning = true;
			}
			currentNode.setLabel(rand);
		}
		return Boolean.valueOf(continueRunning);
	}
}
		
