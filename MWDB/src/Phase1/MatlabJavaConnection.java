package Phase1;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class MatlabJavaConnection {

	// initialize the factory and proxy objects for the Matlab
	private MatlabProxyFactory factory = null;
	private MatlabProxy proxy = null;

	// Set the formula for gaussianDistribution
	static String gaussianDistributionFormula = "exp(-(x.^2)/(2*0.25*0.25))./sqrt(2*pi*0.25*0.25)";

	/*
	 * 
	 * 
	 * 
	 * 
	 * GIVE BIN FOLDER LOCATION TO BIN
	 * 
	 * 
	 * 
	 */
	
	// set the path to the matlab bin folder
	static String matlabBinFolderLocation = "cd(\'E:/Program Files/MATLAB/R2013b/bin')";

	// set the path to the matlab bin folder
//	static String heatMapGeneratorFileLocation = "'" + new File("generateHeatMap.m").getAbsolutePath() + "'";
//	static String matlabBinFolderLocation = "cd(" + heatMapGeneratorFileLocation + ")";


	// getter for Matlab proxy
	public MatlabProxy getProxy() {
		return proxy;
	}

	// Setter for the Matlab proxy
	public void setProxy(MatlabProxy proxy) {
		this.proxy = proxy;
	}

	// Create the connection to the matlab. This would launch the matlab window on your machine
	public MatlabJavaConnection() throws MatlabInvocationException {
		try {
			factory = new MatlabProxyFactory();
			setProxy(factory.getProxy());
		}
		catch(MatlabConnectionException e) {
			System.out.println("Problems in connecting to matlab!");
			e.printStackTrace();
		}
		catch(Exception e) {
			System.out.println("Problems in running matlab from java");
			e.printStackTrace();
		}
	}

	// function for create the integral string based on the limit
	public String formIntegralExpression(float limit1, float limit2) {
		String expression = "value=integral(@(x) " + gaussianDistributionFormula + "," + limit1 + "," + limit2 + ")";
		return expression;
	}

	// This function would calculate the lengths using the gaussian distribution and value of r given by the user
	public double[] calculateLengths(int r) {
		double[] lengthsArray = new double[r];

		try {
			if(proxy != null) {
				// This would calculate the gaussian integral for the limit of 0 to 1
				proxy.eval(formIntegralExpression(0,1));
				// This would give the total area under consideration
				double totalAreaUnderConsideration = ((double[]) proxy.getVariable("value"))[0];

				//Based on the problem statement we will run the following loop
				for(int i = 1; i <= r; i++) {
					//Calculate the upper and lower limits
					float limit2 = ((float) i)/r;
					float limit1 = ((float) (i-1)/r);
					// Compute the area for each limit integral
					proxy.eval(formIntegralExpression(limit1,limit2));
					System.out.println(formIntegralExpression(limit1,limit2));
					double areaOfIthBand = ((double[]) proxy.getVariable("value"))[0];
					// finally compute the ratio of the area of the ith band to the total area to get the length for ith band
					lengthsArray[i-1] = (areaOfIthBand/totalAreaUnderConsideration);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Problems in calculating the integral for gaussian function");
		}
		finally {
			closeProxy();
		}

		// returns the lengths array from the matlab
		return lengthsArray;
	}
	
	
	/**
	 * 
	 * Function to find top k simulations given a query using Eucledian or DTW distance measures
	 * @param dirPath - Takes in the directory path of the epidemic simulation files.
	 * @param queryFilePath - Takes in the filePath of the query file.
	 * @throws MatlabInvocationException 
	 * 
	 */
	public void strengthGenerator(String dirPath, String queryFileName, int choice, int k) throws MatlabInvocationException
	{
		proxy.setVariable("dirPath", dirPath);
		proxy.setVariable("queryFilePath", queryFileName);
		proxy.setVariable("choice", choice);
		proxy.setVariable("k", k);
		proxy.eval("computeStrength(dirPath,queryFilePath,choice,k)");
		return;
		
	}
	
	/**
	 * 
	 * @param choice - which takes in the choice of the user to get the type of the similarity.
	 * @return
	 * @throws MatlabInvocationException
	 */
	/*public double[][] computeSimulationSimilarityMatrix(int choice) throws MatlabInvocationException
	{
		proxy.setVariable("choice", choice);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		proxy.setVariable("s", 0);
		proxy.eval("s = computeSimulationSimilarity(choice);");
		MatlabNumericArray array = processor.getNumericArray("s");
		double[][] simulationSimilarityMatrix = array.getRealArray2D();
		return simulationSimilarityMatrix;		
	}*/
	
	
	/**
	 * 
	 * @param queryVector - This is the query Matrix in the original space where query is a vector in the space of the unique windows.
	 * @param objectFeatureMatrix - This is the list of files expressed as vectors in the space of the unique windows
	 * @param simulationSimilarityMatrix - This is the simulation simulation similarity Matrix
	 * @param r - The number of latent semantics that you want to retain
	 * @param k - The number of top results that you want to display
	 * @throws MatlabInvocationException 
	 */
	public void queryOnLatentSemantics(double [][] queryVector, double [][]objectFeatureMatrix, double[][] simulationSimilarityMatrix ,int r,int k) throws MatlabInvocationException
	{
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("objectFeatureMatrix", new MatlabNumericArray(objectFeatureMatrix,null));
		processor.setNumericArray("queryVector", new MatlabNumericArray(queryVector,null));
		processor.setNumericArray("simulationSimilarityMatrix", new MatlabNumericArray(simulationSimilarityMatrix,null));
		proxy.setVariable("r",(double) r);
		proxy.setVariable("k",(double) k);
		proxy.eval("querySimulationSimilarityMatrix(objectFeatureMatrix, queryVector, simulationSimilarityMatrix,r,k)");
	}
	
	
	
	


	// This function would build the heat map for the given file
		public void heatMapGenerator(List<String> fileList) {
			try {
				if(proxy != null) {
					// Cd to the path where the heat map script is located
					proxy.eval(matlabBinFolderLocation);

					for( String fileName : fileList) {
						// Pass out the simulation file number to be drawn
						proxy.setVariable("fileName", new String(fileName));
						// Finally call the function to  draw the heat based on these values
						proxy.eval("generateFileHeatMap(fileName)");
					}

				}
			}
			catch(Exception e) {
				System.out.println("Problems in creating the heat map for the given file");
				e.printStackTrace();
			}
		}

	// Close the matlab proxy
	public void closeProxy() {
		try {
			if (proxy != null)
				proxy.disconnect();
		}
		catch(Exception e) {
			System.out.println("Problems in disconnecting with Matlab");
		}

	}

	/*public void computeFastMap(double[][] inputMatrix,int k) throws MatlabInvocationException 
	{
		// TODO Auto-generated method stub
		
		double[][] pivot = new double[2][k];
		Arrays.fill(pivot, 0.0);
		int n = inputMatrix[0].length;
		double [][] x = new double [n][k];
		Arrays.fill(x, 0.0);
		double col =0;
		proxy.setVariable("k", k);
		proxy.setVariable("n", n);
		proxy.setVariable("col", col);
		proxy.setVariable("mappingError", 0);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("pivot",new MatlabNumericArray(pivot,null));
		processor.setNumericArray("x", new MatlabNumericArray(x,null));
		processor.setNumericArray("dist", new MatlabNumericArray(inputMatrix,null));
		proxy.eval("mappingError = calculateMappingError(k,dist,pivot,x,col,n);");
		double mappingError = ((double[]) proxy.getVariable("mappingError"))[0];
		System.out.println("Mapping Error = "+mappingError);
		
		
		
		
		
		
	}*/
}
