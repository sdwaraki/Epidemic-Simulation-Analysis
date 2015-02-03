/**
 * 
 * @author Sumanth
 */


/**
 * 
 * Functions to act on the simulation file
 */
package Phase1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class heatMapDataCalculation 
{
	/**
	 * creates a number - state association 
	 * @param content
	 * @return
	 */
	public Map<String,Integer> createStateMap(String [][] content)
	{
		Map<String,Integer> stateMap = new LinkedHashMap<String,Integer>();
		for(int i=2;i<53;i++)
		{
			stateMap.put(content[0][i],i-2);
		}
		return stateMap;
	}
	
	/**
	 * 
	 * Takes in the required parameters to plot the heat map in MatLab
	 * @param matrixToPlot
	 * @param maxWindowStart
	 * @param maxStateNumber
	 * @param minWindowStart
	 * @param minStateNumber
	 * @param windowSize
	 * @param oneHopForMaxStateNumbers
	 * @param oneHopForMinStateNumbers
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public void getHeatMap(double[][]matrixToPlot,int maxWindowStart,int maxStateNumber,int minWindowStart,int minStateNumber,
			int windowSize,double[][] oneHopForMaxStateNumbers,double[][]oneHopForMinStateNumbers) throws MatlabConnectionException, MatlabInvocationException
	{
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("inputMatrix", new MatlabNumericArray(matrixToPlot, null));
		processor.setNumericArray("maxOneHopNeighbors",new MatlabNumericArray(oneHopForMaxStateNumbers, null));
		processor.setNumericArray("minOneHopNeighbors", new MatlabNumericArray(oneHopForMinStateNumbers,null));
		proxy.setVariable("maxWindowStart", maxWindowStart);
		proxy.setVariable("minWindowStart", minWindowStart);
		proxy.setVariable("maxStateNumber", maxStateNumber);
		proxy.setVariable("minStateNumber", minStateNumber);
		proxy.setVariable("windowSize",windowSize);
		proxy.eval("makeHeatMap(inputMatrix, maxWindowStart, maxStateNumber, minWindowStart, minStateNumber, windowSize, maxOneHopNeighbors, minOneHopNeighbors)");
	}
	
	
	/**
	 * 
	 * Modeling data to what should go into the heat map.
	 * Creates list of one hop neighbors for maximum window's and minimum window's state.
	 * Reads the simulation file and obtains the matrix
	 * @param maxStrengthIndexObj
	 * @param minStrengthIndexObj
	 * @param maxStrengthIndex
	 * @param minStrengthIndex
	 * @param maxStrengthWindowObj
	 * @param minStrengthWindowObj
	 * @param fileNumber
	 * @throws IOException
	 * @throws BiffException
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public void plotHeatMap(idx maxStrengthIndexObj, idx minStrengthIndexObj, int maxStrengthIndex, int minStrengthIndex, 
			window maxStrengthWindowObj, window minStrengthWindowObj, int fileNumber) throws IOException, BiffException, MatlabConnectionException, MatlabInvocationException
	{
		BufferedReader br;
		//String inputFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/"+fileNumber+".csv";
		String inputFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/Sample2.csv";
		br = new BufferedReader(new FileReader(inputFile));
		String [][] content = new String[214][53];
		String line="";
		int rowcount=0,windowSize,maxStateNumber,maxWindowStart,minStateNumber,minWindowStart;
		String delim = ",";
		Map<String,Integer> stateMap;
		String maxState,minState;
		double matrixToPlot [][] = new double[213][51];
		CreateAssociationList create = new CreateAssociationList();
		List<String> oneHopForMaxState, oneHopForMinState;
		double [][] oneHopForMinStateNumbers, oneHopForMaxStateNumbers;
		
		
		Map<String,List<String>>oneHop = create.getAssociationMap();
		
		while((line=br.readLine())!=null)
		{	
			content[rowcount]=line.split(delim);
			rowcount++;
		}
		
		//create  StateMap
		stateMap = createStateMap(content);
		windowSize = maxStrengthWindowObj.getList().size();
			
		
		//Get the maximumStrength Parameters
		maxState = maxStrengthIndexObj.getState();
		maxStateNumber = stateMap.get("US-"+maxState);
		maxWindowStart = maxStrengthIndexObj.getIteration();
		
		//Computing the input Matrix
		for(int i=1;i<214;i++)
		{
			for(int j=2;j<53;j++)
			{
				matrixToPlot[i-1][j-2] = Double.parseDouble(content[i][j]);
			}
		}
		
		//Get OneHopNeighbors for the maxState
		oneHopForMaxState = oneHop.get(maxState);

		//Handle edge cases
		if(oneHopForMaxState == null)
		{
			oneHopForMaxStateNumbers = new double [1][1];
			oneHopForMaxStateNumbers[0][0] = -1;
		}
		else
		{
			//Make array of oneHopNeighbor's StateNumbers
			oneHopForMaxStateNumbers = new double [1][oneHopForMaxState.size()];
			for(int i=0;i<oneHopForMaxState.size();i++)
			{
				 oneHopForMaxStateNumbers[0][i]= (double)stateMap.get("US-"+oneHopForMaxState.get(i));
			}
		}
		
		//Get MinimumStrength parameters
		minState = minStrengthIndexObj.getState();
		minStateNumber = stateMap.get("US-"+minState);
		minWindowStart = minStrengthIndexObj.getIteration();
		
		//Get OneHopNeighbors for the minState
		oneHopForMinState = oneHop.get(minState);
		if(oneHopForMinState ==null)
		{
			oneHopForMinStateNumbers = new double [1][1];
			oneHopForMinStateNumbers[0][0] = -1;
		}
		else
		{	
			//Make array of oneHopNighbor's StateNumbers
			oneHopForMinStateNumbers = new double[1][oneHopForMinState.size()];
			for(int i=0;i<oneHopForMinState.size();i++)
			{
				oneHopForMinStateNumbers[0][i] = (double)stateMap.get("US-"+oneHopForMinState.get(i));
			}
		}
		
		getHeatMap(matrixToPlot, maxWindowStart,maxStateNumber,minWindowStart,minStateNumber,windowSize,oneHopForMaxStateNumbers,oneHopForMinStateNumbers);
		
	}
}
