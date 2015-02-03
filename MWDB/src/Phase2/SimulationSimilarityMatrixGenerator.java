package Phase2;

import java.io.File;
import java.util.Map;

import Phase1.MatlabJavaConnection;
import matlabcontrol.MatlabInvocationException;

/*
 * class responsible for generating simulation simulation similarity generator
 */
public class SimulationSimilarityMatrixGenerator {

	// directory location to be read, must be provided by the user
	// Can be "Output/EpidemicAverageFiles" || "Output/EpidemicDifferenceFiles" || "Output/EpidemicWordFiles"
	private String directoryLocation;
	// matrix for the simulation similarity
	double[][] simulationSimilarityMatrix;
	// similarity measure choice
	private String similarityMeasureType;


	// constructor for the class
	public SimulationSimilarityMatrixGenerator(String directoryLocation, String similarityMeasureType) {
		setDirectoryLocation(directoryLocation);
		setSimilarityMeasureType(similarityMeasureType);

	}

	public SimulationSimilarityMatrixGenerator(String dirPath) {
		// TODO Auto-generated constructor stub
		setDirectoryLocation(dirPath);
		setSimilarityMeasureType(null);

	}

	//getter for directory location
	public String getDirectoryLocation() {
		return directoryLocation;
	}

	// setter for directory location
	public void setDirectoryLocation(String directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	// getter for similarity measure type
	public String getSimilarityMeasureType() {
		return similarityMeasureType;
	}

	// setter for similarity measure type
	public void setSimilarityMeasureType(String similarityMeasureType) {
		this.similarityMeasureType = similarityMeasureType;
	}

	// Important : Need to attach code for task 1a, task 1b and other tasks
	public void computeSimulationSimilarityMatrix() throws MatlabInvocationException {
		File fileDirectory = new File(directoryLocation);

		// Check if the file object read is itself a directory
		if (fileDirectory != null && fileDirectory.isDirectory()) {

			// if similarity measure chose is among 1C,1D and 1E
			// compute similarity simulation matrix in terms of those
			if("CDE".contains(getSimilarityMeasureType())) {
				similaritySimulationMatrixByDotProduct(fileDirectory);
			}
					
			

		}
		// We are not able to reach the desired directory
		else {
			System.out.println("Looks like the directory for the file is missing");
		}

	}
	
	
	/**
	 * Function to generate the Simulation Similarity Matrix by Eucledian and DTW distance measures. 
	 * @param choice : 1 for Eucledian Similarity, 2 for DTW Similarity
	 * @throws MatlabInvocationException ----
	 */
	/*public void generateSimilaritySimulationMatrix(int choice) throws MatlabInvocationException
	{
		MatlabJavaConnection m = new MatlabJavaConnection();
		simulationSimilarityMatrix = m.computeSimulationSimilarityMatrix(choice);
	}*/
		

	/*
	 * This method will only work to create simulation similarity matrix for task 1c,1d, and 1e
	 */
	public void similaritySimulationMatrixByDotProduct(File fileDirectory) {

		// get an array of the files in the directory
		File[] directoryFiles = fileDirectory.listFiles();
		// Create a epidemic file obj
		EpidemicFile currentFile = null;
		// Similarity finder obj
		SimilarityFinder sfObj = null;
		// Create a similarity heat map generator obj
		SimilarityHeatMapGenerator shmgObj = null;

		// no of files in the directory
		int numberOfFiles = directoryFiles.length;

		// initialize the simulation similarity matrix
		simulationSimilarityMatrix = new double[numberOfFiles][numberOfFiles];

		for(int i=0; i< directoryFiles.length; i++) {

			// Create a epidemic file obj for current file
			currentFile = new EpidemicFile(directoryFiles[i]);
			currentFile.readFile();

			// For each file create a SimilarityHeatMapGenerator obj and call its compute method
			// now call the readAndComputeSimilarityForAllFiles() method to create the treeMap of similarity for current file
			shmgObj = new SimilarityHeatMapGenerator(currentFile);
			shmgObj.readAndComputeSimilarityForAllFiles();

			// Now, we have the similarityMap for current file in memory but this map does not contains the value of similarity for (currentFile, currentFile)
			sfObj = new SimilarityFinder();
			double similarity = sfObj.getPercentageSimilarity(currentFile, currentFile);
				
			// check if this similarity value already exists in the similarityMap for file and add accordingly
			// put similarity for each pair (queryfileObj, currentfile) into the hash map
			// Remember more than file can have same similarity value thus we need to check if the key
			// already exists in the map than append current file name to existing string
			if (shmgObj.getSimilarityMap().containsKey(similarity)) {
				String previousValue = shmgObj.getSimilarityMap().get(similarity);
				//Eg : (Similarity) 4 -> "1,14,32" (File Name separated by comma)
				shmgObj.getSimilarityMap().put(similarity, previousValue + "," + currentFile.getFileName());
			}
			else {
				// First time this level of similarity was encountered
				shmgObj.getSimilarityMap().put(similarity, currentFile.getFileName());
			}

			// compute similarityMatrix corresponding to current file and put it into the final 2-D double matrix
			simulationSimilarityMatrix[Integer.parseInt(currentFile.getFileName().replace(".csv", ""))-1] = similarityMatrixProcessor(shmgObj,numberOfFiles);
		}

	}

	/*
	 * Getter for simulation similarity matrix for task 1C, 1D, 1E
	 * similaritySimulationMatrixByDotProduct() - Call this method to compute the matrix first
	 * Then call this getter
	 */
	public double[][] getSimulationSimilarityMatrix() {
		return simulationSimilarityMatrix;
	}

	public void setSimulationSimilarityMatrix(double[][] simulationSimilarityMatrix) {
		this.simulationSimilarityMatrix = simulationSimilarityMatrix;
	}

	/*
	 * This method would process the similarity map generated for each file which represents similarity
	 * The returned double would be fed into the simulation matrix
	 */
	public double[] similarityMatrixProcessor(SimilarityHeatMapGenerator shmgObj, int arrLength) {

		// instantiate the array of similarity for a file
		double[] resultArr = new double[arrLength];

		/*
		 * The map representation is like below :
		 *
		 *  (similarity) 4 -> "1,2,3" (file number)
		 *
		 * 		 |   T    T     T    T    T
		 *   	 |___|____|_____|____|____|
		 * Index  0    1    2     3    4
		 *
		 * File	  1    2    3     4    5
		 * Number
		 */
		for(Map.Entry<Double, String> entry : shmgObj.getSimilarityMap().entrySet()) {
			String[] fileList = entry.getValue().split(",");

			for(String fileNumber : fileList) {
				int indexNumber = Integer.parseInt(fileNumber.replace(".csv", "")) - 1;
				// convert similarity value to double for matlab and put corresponding to file's indexNumber
				resultArr[indexNumber] = (double)(entry.getKey());
			}
		}

		return resultArr;
	}

	/*
	 * Helper method to print the simulation similarity matrix
	 */
	public void printSimulationMatrix() {

		for(int i =0; i < simulationSimilarityMatrix.length; i++) {
			System.out.print("      " + i);
		}

		System.out.println();

		for(int i = 0; i< simulationSimilarityMatrix.length; i++) {

			System.out.print(i + "    ");
			for(int j = 0; j < simulationSimilarityMatrix.length; j++) {
				System.out.print(simulationSimilarityMatrix[i][j] + "    ");
			}
			System.out.println();
		}

	}


}
