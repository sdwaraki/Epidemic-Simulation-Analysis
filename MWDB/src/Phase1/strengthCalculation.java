/**
 * 
 * @author Sumanth
 */

/**
 * 
 * Compute Strength of windows of a file
 */
package Phase1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import jxl.read.biff.BiffException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class strengthCalculation 
{
	/**
	 * 
	 * Takes input from the user and gets all the data that should be given to processing function plotting.
	 * @param fileNumber
	 * @param type
	 * @throws IOException
	 * @throws BiffException
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public static void getWindows(int fileNumber, String type) throws IOException, BiffException, MatlabConnectionException, MatlabInvocationException
	{
		String file,content,delim=",";
		int fileLength;
		BufferedReader br;
		String [] afterSplit;
		double maxStrength;
		double minStrength;
		int maxStrengthIndex;
		int minStrengthIndex;
		idx maxStrengthIndexObj;
		window maxStrengthWindowObj;
		idx minStrengthIndexObj;
		window minStrengthWindowObj;
		
		
		if(type.equals("word"))
			 file = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFiles/epidemic_word_file"+fileNumber+".csv";
			    	 
		else if(type.equals("avg"))
			file = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesAverage/epidemic_word_file_avg_"+fileNumber+".csv";
			//file = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesAverage/epidemic_word_file_avg_Sample2.csv";
		else
			file = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesDifference/epidemic_word_file_diff"+fileNumber+".csv";
			//file = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesDifference/epidemic_word_file_diffSample2.csv";
		
		
		 
		fileLength = computeFileLength(file);
		
		idx idxArray[] = new idx[fileLength];
		window winArray[] = new window[fileLength];
		double strengthArray [] = new double[fileLength];
		br = new BufferedReader(new FileReader(file));
		
		for(int i=0;i<fileLength;i++)
		{
			//initialize the window and the idx objects
			content = br.readLine();
			afterSplit = content.split(delim);
			idx index = new idx();
			window win = new window();
			
			index.setFile(Integer.parseInt(afterSplit[0]));
			index.setState(afterSplit[1]);
			index.setIteration(Integer.parseInt(afterSplit[2]));
			
			for(int a = 3; a<afterSplit.length;a++)
			{
				win.addToList(Double.parseDouble(afterSplit[a]));
			}
			
			idxArray[i] = index;
			winArray[i] = win;
		}
		
		br.close();
		strengthArray = computeStrength(winArray);
		
		edgeValue obj = new edgeValue();
		
		//compute max strength
		obj = computeMax(strengthArray);
		maxStrength = obj.getValue();
		maxStrengthIndex = obj.getIndex();
		
		//compute min strength
		obj = computeMin(strengthArray);
		minStrength = obj.getValue();
		minStrengthIndex = obj.getIndex();
		
		//index objects of the max and min strength windows 
		maxStrengthIndexObj = idxArray[maxStrengthIndex];
		minStrengthIndexObj = idxArray[minStrengthIndex];
		
		//window objects of max and min strength windows
		maxStrengthWindowObj = winArray[maxStrengthIndex];
		minStrengthWindowObj = winArray[minStrengthIndex];
		
		heatMapDataCalculation h = new heatMapDataCalculation();
		
		h.plotHeatMap(maxStrengthIndexObj, minStrengthIndexObj, maxStrengthIndex, minStrengthIndex, 
				 maxStrengthWindowObj,  minStrengthWindowObj, fileNumber);
				
	}
	
	/**
	 * 
	 * Computes the maximum strength. 
	 * The first maximum is taken into account in case of multiple windows having same strength
	 * @param strengthArray
	 * @return
	 */
	public static edgeValue computeMax(double [] strengthArray)
	{
		edgeValue o = new edgeValue();
		double maxValue = 0.0;
		int maxIndex;
		maxValue=strengthArray[0];
		maxIndex=0;
		int i=1;
		while(i<strengthArray.length)
		{
			if(strengthArray[i]>maxValue)
			{
				maxValue = strengthArray[i];
				maxIndex =i;
			}
			i++;
		}
		
		o.setValue(maxValue);
		o.setIndex(maxIndex);
		
		return o;
	}
	
	
	/**
	 * Computes Minimum Strength
	 * The first minimum value is taken into account in case of multiple minimum strengths
	 * @param strengthArray
	 * @return
	 */
	public static edgeValue computeMin(double [] strengthArray)
	{
		edgeValue o = new edgeValue();
		double minValue = 0.0;
		int minIndex;
		minValue=strengthArray[0];
		minIndex=0;
		int i=1;
		while(i<strengthArray.length)
		{
			if(strengthArray[i]<minValue)
			{
				minValue = strengthArray[i];
				minIndex =i;
			}
			i++;
		}
		
		o.setValue(minValue);
		o.setIndex(minIndex);
		
		return o;
	}
	
	
	/**
	 * 
	 * Compute strength of all windows.
	 * @param winArray
	 * @return
	 */
	public static double [] computeStrength(window[] winArray)
	{
		double strengthArray [] = new double[winArray.length];
		double strength;
		for(int i=0;i<winArray.length;i++)
		{
			strength = 0.0;
			window win = winArray[i];
			List<Double> winList = win.getList();
			for(int j=0;j<winList.size();j++)
			{
				strength = strength + Math.pow(winList.get(j), 2);
			}
			strengthArray[i]=Math.sqrt(strength);
		}
		return strengthArray;
	}
	
	/**
	 * 
	 * Computes length of the file
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public static int computeFileLength(String inputFile) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line ="";
		int count = 0;
		while((line=br.readLine())!=null)
		{
			count++;
		}
		br.close();
		return count;
	}
	
	
	
	public static void main(String[] args) throws IOException, BiffException, MatlabConnectionException, MatlabInvocationException 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			System.out.print("Enter the file number or type 'exit' to exit :");
			String f = br.readLine();
			if(f.equals("exit"))
				break;
			int fileNumber = Integer.parseInt(f);
			System.out.print("Enter Choice ---- 1.Epidemic Word File \n2.Epidemic Average File \n3.Epidemic Difference File\n");
			String choice = br.readLine();
			int ch = Integer.parseInt(choice);
			if(ch==1)
			{
				getWindows(fileNumber, "word");
			}
			else if(ch==2)
			{
				getWindows(fileNumber,"avg");
			}
			else
			{
				getWindows(fileNumber,"diff");
			}
		}
	}

}
