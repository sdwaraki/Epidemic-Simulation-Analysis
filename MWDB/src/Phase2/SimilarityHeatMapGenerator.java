package Phase2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Phase1.MatlabJavaConnection;
import matlabcontrol.MatlabInvocationException;

/*
 * Core class for Task2. It would have following duties:
 * 1. Have the function to read all the simulation files in the directory location specified for query file.
 * 2. Compute similarity of each file with the query file and store the results in an efficient data structure
 * 3. Function to get top k similar files and call the matlab proxy to generate heat map for these files
 */
public class SimilarityHeatMapGenerator {
	// Instance field to store query file obj
	private EpidemicFile queryFileObj;
	// Instance field to store the value of k
	private int valueOfK;
	// Instance field to store the data structure for maintaining sorted order of files with most similarity
	// Idea : Use a tree map that keeps elements in reverse sorted order
	// Key(Similarity) --> Value ("File Name")
	private Map<Double, String> similarityMap;
	//Instance variable to hold the directoryLocation name
	private String directoryLocation;
	//The similarityFileName list would store the name of k most similar files
	private List<String> similarityFileList;

	// NEED TO CONFIRM : We want to generate heatmap of simulation files or not?
	public static String simulationFileLocation = "C:/Users/Sumanth/workspace/Phase 1 Programs/InputData/";
	public static String queryFileLocation = "C:/Users/Sumanth/workspace/Phase 1 Programs/InputQuery/";
	
	// Constructor for the class
	public SimilarityHeatMapGenerator(EpidemicFile queryFileObj, int valueOfK) {
		this.queryFileObj = queryFileObj;
		this.valueOfK = valueOfK;
		setDirectoryLocation(this.queryFileObj.getDirectoryLocation());
		// Instantiate the tree map with instruction to keep map in reverse sorted order
		// Pass a comparator basically to the map
		this.similarityMap = new TreeMap<Double, String>(Collections.reverseOrder());
		// instantiate the similarity file list
		this.similarityFileList = new ArrayList<String>();
	}
	
	// Constructor for the class
		public SimilarityHeatMapGenerator(EpidemicFile queryFileObj, int valueOfK, String directoryLocation) {
			this.queryFileObj = queryFileObj;
			this.valueOfK = valueOfK;
			setDirectoryLocation(directoryLocation);
			// Instantiate the tree map with instruction to keep map in reverse sorted order
			// Pass a comparator basically to the map
			this.similarityMap = new TreeMap<Double, String>(Collections.reverseOrder());
			// instantiate the similarity file list
			this.similarityFileList = new ArrayList<String>();
		}

	// Constructor overloading, to solve simulation similarity problem
	public SimilarityHeatMapGenerator(EpidemicFile queryFileObj) {
		this.queryFileObj = queryFileObj;
		setDirectoryLocation(this.queryFileObj.getDirectoryLocation());
		// Instantiate the tree map with instruction to keep map in reverse sorted order
		// Pass a comparator basically to the map
		this.similarityMap = new TreeMap<Double, String>(Collections.reverseOrder());
	}

	// Getter for query file Obj
	public EpidemicFile getQueryFileObj() {
		return queryFileObj;
	}

	// Getter for value of K
	public int getValueOfK() {
		return valueOfK;
	}

	

	// getter for directory location to be read
	public String getDirectoryLocation() {
		return directoryLocation;
	}

	// setter for directory location to read
	public void setDirectoryLocation(String directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	// getter for similarity file list
	public List<String> getSimilarityFileList() {
		return similarityFileList;
	}

	/*
	 * Method to read all files present in the directory and compute similarity for each file with the query file
	 * We also add similarity values to the tree map in this method
	 */
	public void readAndComputeSimilarityForAllFiles() {
		// Remember! we have read the query file in the main() itself thus, we have its unique window set already in the memory

		// Create directory location File Object
		File fileDirectory = new File(getDirectoryLocation());

		// Check if the file object read is itself a directory
		if (fileDirectory != null && fileDirectory.isDirectory()) {

			// get an array of the files in the directory
			File[] directoryFiles = fileDirectory.listFiles();
			// Create a epidemic file obj
			EpidemicFile currentFile = null;
			// Similarity finder obj
			SimilarityFinder sfObj = null;

			for(int i=0; i< directoryFiles.length; i++) {


				// We would not compare the similarity with the file itself.
				// perform following computation only if the file name for read file is different from the queried file
				if( !directoryFiles[i].getName().equals(getQueryFileObj().getFileName()) ) {
					currentFile = new EpidemicFile(directoryFiles[i]);

					//Compute the unique window set
					currentFile.readFile();

					// compute similarity between files
					sfObj = new SimilarityFinder();
					//double similarity = sfObj.dotProduct(getQueryFileObj(), currentFile);
					double similarity = sfObj.getPercentageSimilarity(getQueryFileObj(), currentFile);
					
					// put similarity for each pair (queryfileObj, currentfile) into the hash map
					// Remember more than file can have same similarity value thus we need to check if the key
					// already exists in the map than append current file name to existing string
					if (similarityMap.containsKey(similarity)) {
						String previousValue = similarityMap.get(similarity);
						//Eg : (Similarity) 4 -> "1,14,32" (File Name separated by comma)
						similarityMap.put(similarity, previousValue + "," + currentFile.getFileName());
					}
					else {
						// First time this level of similarity was encountered
						similarityMap.put(similarity, currentFile.getFileName());
					}
				}

			}
			printSimilarityMap();

		}
		// We are not able to reach the desired directory
		else {
			System.out.println("Looks like the directory for the file is missing");
		}
	}

	/*
	 *  This method will actually populate the similarity file list that would be passed on to matlab for heat map generation
	 */
	public void populateSimilarityList() {
		// Now each key in the similarity map may contain more than one file names separated by comma.
		// Thus we will keep a counter to keep track of the number of files in the list

		// Create a file object, because the file list would contain location to epidemic simulation data set files
		File tempFile = null;

		// Add query file to the list, so that we can have its plotted also
		if (queryFileObj.getFileName().contains("query"))
		{
			tempFile = new File(queryFileLocation + "query.csv");
			System.out.println(tempFile.getAbsolutePath());
		}
		else {
			tempFile = new File(simulationFileLocation + queryFileObj.getFileName());
		}
		
		similarityFileList.add(tempFile.getAbsolutePath());

		// Counter
		int filesInList = 0;

		// Iterate the map
		for( Map.Entry<Double, String> entry : similarityMap.entrySet() ) {
			String[] currentFiles = entry.getValue().split(",");

			// copy each file name into the map and increase the counter
			for( int i = 0 ; i < currentFiles.length; i++) {
				// read the file here in java and then pass its absolute path to help matlab
				tempFile = new File(simulationFileLocation + currentFiles[i]);
				// add file to the list
				similarityFileList.add(tempFile.getAbsolutePath());
				// increase the counter
				filesInList += 1;
				// check if counter is equal to valueOfK
				if (filesInList == getValueOfK()) {
					// return from the function
					return;
				}
			}

		}
	}

	/*
	 * This function would generate the heat map for the k files and the query file
	 */
	public void generateHeatMaps() {
		populateSimilarityList();
		try {
			MatlabJavaConnection mjcObj = new MatlabJavaConnection();
			mjcObj.heatMapGenerator(similarityFileList);

		} catch (MatlabInvocationException e) {
			System.out.println("Problems in  invoking the matlab proxy");
		}

	}

	/*
	 * Helper method to see the similarity map generated for the file
	 */
	public void printSimilarityMap() {

		for(Map.Entry<Double, String> entry : similarityMap.entrySet()) {
			System.out.println("Similarity : " + entry.getKey() + " ----- File Name: " + entry.getValue());
		}

	}

	/*
	 * Helper method to see the similary list values
	 */
	public void printSimilarityList() {

		for(String s : similarityFileList) {
			System.out.println(s);
		}

	}

	public Map<Double, String> getSimilarityMap() {
		// TODO Auto-generated method stub
		return similarityMap;
	}

	

}

