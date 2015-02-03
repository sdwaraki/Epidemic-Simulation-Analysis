package Phase2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

// File Reader Class for phase2
public class EpidemicFile {
	// file name to be read
	private String fileName;
	// Tree set to maintain the list of unique window read in file
	// TreeSet is important, this would guarantee us the window order for each file
	private TreeSet<String> uniqueWindowSet;
	// File path for the file
	private String filePath;
	// Some time we can get the file object itself
	private File epidemicFileObj;
	// Store the directory location for the files as well
	private String directoryLocation;

	//constructor for the class
	public EpidemicFile(String directoryLocation, String fileName) {
		this.uniqueWindowSet = new TreeSet<String>();
		setFileName(fileName);
		this.filePath = setFilePath(directoryLocation, getFileName());
		this.directoryLocation = directoryLocation;

	}

	// Use polymorphism to create a constructor that can directly accomodate the file object
	public EpidemicFile(File epidemicFileObj) {
		setEpidemicFileObj(epidemicFileObj);
		this.uniqueWindowSet = new TreeSet<String>();
		this.filePath = epidemicFileObj.getAbsolutePath();
		this.directoryLocation = epidemicFileObj.getParent();
		this.fileName = epidemicFileObj.getName();
	}

	// getter for file name
	public String getFileName() {
		return fileName;
	}

	// setter for file name
	public void setFileName(String fileName) {
		this.fileName = fileName + ".csv";
	}

	// getter for window hash set for file
	public TreeSet<String> getUniqueWindowSet() {
		return uniqueWindowSet;
	}

	// function to convert the fileNumber into the the path for fileName
	public String setFilePath(String directoryLocation, String fileName) {
		return directoryLocation + fileName;
	}

	// getter for filePath
	public String getFilePath() {
		return filePath;
	}

	// getter for file obj
	public File getEpidemicFileObj() {
		return epidemicFileObj;
	}

	// setter for file obj
	public void setEpidemicFileObj(File epidemicFileObj) {
		this.epidemicFileObj = epidemicFileObj;
	}

	// getter for the directory location
	public String getDirectoryLocation() {
		return directoryLocation;
	}

	// setter for the directory location
	public void setDirectoryLocation(String directoryLocation) {
		this.directoryLocation = directoryLocation;
	}

	public void readFile() {
		BufferedReader fileReader = null;
		try {

			// if we have intialized the constructor with a file object
			if(getEpidemicFileObj() != null) {
				fileReader = new BufferedReader(new FileReader(getEpidemicFileObj()));
			}
			// initialize a buffered reader object and read the given file
			else {
				fileReader = new BufferedReader(new FileReader(new File(getFilePath())));
			}


			// Line to be read
			String line = null;

			// Until we reach the end of the file
			while( (line = fileReader.readLine()) != null ) {
				// Add windows to window set. Remember that set contains unique values and thus only unique windows would be added
				// Also we are using tree set thus our set would always be sorted. This would help in resolving the common windows.
				// See FileSimilarity for further reference

				// create a string builder to append window values to it
				StringBuilder windowValues = new StringBuilder();
				// Split the read line
				String[] lineArr = line.split(",");

				// Waste initial three index of the split line. Only need windowValues
				for(int j = 3; j < lineArr.length; j++) {
					// dont append "," for last window value
					if(j == lineArr.length-1) {
						windowValues.append(lineArr[j]);
					}
					else {
						windowValues.append(lineArr[j] + ",");
					}
				}

				getUniqueWindowSet().add(windowValues.toString());
			}

			// close the stream
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
