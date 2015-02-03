package Phase2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/*
 * This class would be the core class that would contain our business logic for calculating dot product for given files.
 */
public class SimilarityFinder {

	// Tree Set generation to maintain unique dimension
	private TreeSet<String> totalUniqueDimensionSet;

	// Constructor for the class
	public SimilarityFinder() {
		this.totalUniqueDimensionSet = new TreeSet<String>();
	}

	// The getter would return the value of each unique window encountered for the program run
	public TreeSet<String> getTotalUniqueDimensionSet() {
		return totalUniqueDimensionSet;
	}



	// Merge the two tree set receieved from the two file objects to populate the SimilartiyFinder TreeSet
	public void populateTotalUniqueSet(TreeSet<String> fileOneUniqueSet, TreeSet<String> fileTwoUniqueSet) {
		getTotalUniqueDimensionSet().addAll(fileOneUniqueSet);
		getTotalUniqueDimensionSet().addAll(fileTwoUniqueSet);

	}

	public int[][] dotProductPreProcessor(EpidemicFile file1, EpidemicFile file2) {

		int[][] binaryVectorArray = new int[2][2];

		// Need to merge the tree set for each file to generate one unique map
		populateTotalUniqueSet(file1.getUniqueWindowSet(), file2.getUniqueWindowSet());

		// Convert the tree map into an array list for querying operations, this will maintain the sorted order
		ArrayList<String> uniqueDimensionList = new ArrayList<String>(getTotalUniqueDimensionSet());
		// Get the size of Tree set to find the number of unique dimension found after reading both the files
		int numberOfUniqueDimensions = uniqueDimensionList.size();

		// Now initialize two integer array equal to the size of tree set.
		// This would help us represent each file in binary for the given number of unique
		int[] fileOneUniqueDimensionArr  = new int[numberOfUniqueDimensions];
		int[] fileTwoUniqueDimensionArr  = new int[numberOfUniqueDimensions];

		// create binary vectors for both the files
		// For file 1
		Iterator<String> itr= file1.getUniqueWindowSet().iterator();
		while(itr.hasNext()) {
			String windowValue = itr.next();
			if(getTotalUniqueDimensionSet().contains(windowValue)) {
				fileOneUniqueDimensionArr[uniqueDimensionList.indexOf(windowValue)] = 1;
			}

		}
		// add file array to vector array
		binaryVectorArray[0] = fileOneUniqueDimensionArr;

		// For file 2
		itr= file2.getUniqueWindowSet().iterator();
		while(itr.hasNext()) {
			String windowValue = itr.next();
			if(getTotalUniqueDimensionSet().contains(windowValue)) {
				fileTwoUniqueDimensionArr[uniqueDimensionList.indexOf(windowValue)] = 1;
			}
		}
		// add file array to vector array
		binaryVectorArray[1] = fileTwoUniqueDimensionArr;

		return binaryVectorArray;
	}

	public int dotProduct(EpidemicFile file1, EpidemicFile file2) {

		// get the binary vectors
		int[][] binaryVectorsArray = dotProductPreProcessor(file1, file2);

		// dot product result
		int productResult = 0;
		// Loop over both the files array and compute dot product
		for(int i = 0; i < binaryVectorsArray[0].length; i ++) {
			productResult += binaryVectorsArray[0][i] * binaryVectorsArray[1][i];
		}

		// return dot product
		return productResult;
		
	}
	
	public double getPercentageSimilarity(EpidemicFile file1, EpidemicFile file2)
	{
		int[][] binaryVectorsArray = dotProductPreProcessor(file1, file2);

		// dot product result
		double productResult = 0;
		// Loop over both the files array and compute dot product
		for(int i = 0; i < binaryVectorsArray[0].length; i ++) {
			productResult += binaryVectorsArray[0][i] * binaryVectorsArray[1][i];
		}

		// return dot product
		return productResult/binaryVectorsArray[0].length;

	}
	

	
}
