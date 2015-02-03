package Phase2;

import java.util.Scanner;

/*
 * Main class for executing the task 1c, task 1d, and task 1e
 */
public class RunTask1CDE {

	public double RunCDE(int optionType, String fileOneName, String fileTwoName) {

		// instantiate a similarity finder object
		SimilarityFinder sfObj = new SimilarityFinder();

		// Create two EpidemicFile Objects
		EpidemicFile file1 = null;
		EpidemicFile file2 = null;

		if(optionType == 3) {
			// Create file Objects
			file1 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/", fileOneName);
			file2 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/", fileTwoName);
		}
		else if(optionType == 4) {
			// Create file Objects
			file1 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg/", fileOneName);
			file2 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg/", fileTwoName);
		}
		else {
			// Create file Objects
			file1 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff/", fileOneName);
			file2 = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff/", fileTwoName);
		}

		// read file 1 and generate its unique window set
		file1.readFile();
		// read file 2 and generate its unique window set
		file2.readFile();

		// now calculate similarity

		return sfObj.dotProduct(file1, file2);
	
		


	}

}
