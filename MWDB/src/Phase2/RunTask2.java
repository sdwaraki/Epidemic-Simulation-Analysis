package Phase2;

import java.util.Scanner;

import matlabcontrol.MatlabInvocationException;
import Phase1.AFunctionMain;
import Phase1.MatlabJavaConnection;

/*
 * This class is responsible for running the task2
 */
public class RunTask2 {

	public static void main(String[] args) throws MatlabInvocationException {
		//Create a stream object to take console line inputs.
				Scanner scObj = new Scanner(System.in);
				SimilarityHeatMapGenerator shObj = null;
				
				while(true) {
								
					System.out.println();
					System.out.println("Please select the similarity measure of Task 1");
					System.out.println("1.Similarity using Eucledian Distance");
					System.out.println("2.Similarity using DTW");
					System.out.println("3.Similarity using Epidemic Word Files and Binary Vectors");
					System.out.println("4.Similarity using Epidemic Average Files and Binary Vectors ");
					System.out.println("5.Similarity using Epidemic Difference Files and Binary Vectors");
					System.out.println("6. Exit");

					try {
						// Take the user input
						int optionType = Integer.parseInt(scObj.nextLine());

						if(optionType > 6)
							throw new NumberFormatException("Invalid option selected");

						// Close the program
						if(optionType == 6)
							break;

						// Get file number for the query file
						//System.out.println("Please enter the name of query files (file number only) : ");
						//String queryFileName = scObj.nextLine();

						/*if(Integer.parseInt(queryFileName) <= 0)
							throw new NumberFormatException("Please enter a valid value for file name");*/

						// Get the value of k
						System.out.println("Please enter the value of k (We will compute k most similar simulations eg: 1 or 2.. etc) : ");
						Integer valueOfK = Integer.parseInt(scObj.nextLine());

						if(valueOfK <= 0)
							throw new NumberFormatException("Please enter a value of K greater than 1 and less than number of total simulation files");

						// Create a file object for the query file
						EpidemicFile queryFileObj = null;

						if(optionType ==1 || optionType == 2)
						{
							queryFileObj = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/","epidemic_word_file_query");
							String dirPath = queryFileObj.getDirectoryLocation();
							MatlabJavaConnection m = new MatlabJavaConnection();
							m.strengthGenerator(dirPath,"C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_query", optionType,valueOfK);							
							continue;
						}
						else if(optionType == 3) {
							// Create file Object
							queryFileObj = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/","epidemic_word_file_query");
							shObj = new SimilarityHeatMapGenerator(queryFileObj, valueOfK, "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/");
						}
						else if(optionType == 4) {
							// Create file Objects
							queryFileObj = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/","epidemic_word_file_avg_query");
							shObj = new SimilarityHeatMapGenerator(queryFileObj, valueOfK, "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg/");
						}
						else {
							// Create file Objects
							queryFileObj = new EpidemicFile("C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/","epidemic_word_file_diff_query");
							shObj = new SimilarityHeatMapGenerator(queryFileObj, valueOfK, "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff/");
						}
						
						// read query file and generate its unique window set
						queryFileObj.readFile();

						// Now call the class to compute similarity of this file with all other files
						// Pick K most similar files and generate heatmaps from it
						shObj.readAndComputeSimilarityForAllFiles();
						shObj.generateHeatMaps();
					}
					catch(NumberFormatException e) {
						System.out.println();
						System.out.println(e.getMessage());
						continue;
					}


				}

				// Close the input stream.
				scObj.close();
	}

}
