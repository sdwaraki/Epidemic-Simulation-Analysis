/**
 * 
 * @author Sumanth
 */

// Create epidemic_file_diff w.r.t the formula in  Task-2 Part 2

package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

public class epidemicDifferenceFileCreation 
{
	/**
	 * Writes data into the file
	 * @param idxArray
	 * @param winArray
	 * @param outputFile
	 * @throws IOException
	 */
	private static void writeIntoOutputFile(idx[] idxArray,	window[] winArray, String outputFile) throws IOException 
	{
		FileWriter writer;
		boolean alreadyExists = new File(outputFile).exists();
		if(!alreadyExists)
			writer = new FileWriter(outputFile);
		else
			writer = new FileWriter(outputFile,true);
		
		for(int i=0;i<idxArray.length;i++)
		{
			writer.append(String.valueOf(idxArray[i].getFile()));
			writer.append(",");
			writer.append(idxArray[i].getState());
			writer.append(",");
			writer.append(String.valueOf(idxArray[i].getIteration()));
			writer.append(",");
			window w = winArray[i];
			if (w == null) {
				System.out.println("Index:" + i +"state:" +idxArray[i].getState());
			}
			List<Double> y = w.getList();
			if(y!=null)
			{
				for(int v=0;v<y.size();v++)
				{
					writer.append(String.valueOf(y.get(v)));
					writer.append(",");
				}
				writer.append("\n");
			}
		}
		writer.close();
		
	}

	/**
	 * 
	 * Computes the length of the inputFile
	 * @param inputFile
	 * @return length
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
	
	/**
	 * 
	 * computes the Avg window of each window
	 * @param winArray
	 * @param idxArray
	 * @param listOfWindows
	 * @param assocMap
	 * @return window array
	 */
	 
	private static window[] computeResultWindowArray(window[] winArray,	idx[] idxArray,	Map<String, List<String>> assocMap) 
	{
		window[] resultWinArray = new window[winArray.length]; 
		List<String> oneHop;
		List<window> listOfWindows = new ArrayList<window>();
		for(int i=0;i<winArray.length;i++)
		{
			String state = String.valueOf(idxArray[i].getState());
			oneHop = assocMap.get(state);
			if(oneHop!=null)
			{	
				for(int j=0;j<winArray.length;j++)
				{
					
					if(i==j)
						continue;
					
					String difstate = idxArray[j].getState();
					
					if(idxArray[i].getIteration()!=idxArray[j].getIteration())
						continue;
					if(oneHop.contains(difstate))
						listOfWindows.add(winArray[j]);
				}
				
				Double [] x = initializeArray(winArray[0].getList().size());
				
				
				List<Double> l;
				for(int k=0;k<listOfWindows.size();k++)
				{
					l = listOfWindows.get(k).getList();
					for(int z = 0; z<l.size();z++)
					{
						x[z]=x[z]+l.get(z);
					}
					
				}
				
				
				for(int k=0;k<x.length;k++)
				{
					x[k]=x[k]/listOfWindows.size();
				}
				
				List<Double> avgWindow = Arrays.asList(x);
				window finalAvgWindow = new window();
				for(int z=0;z<avgWindow.size();z++)
					finalAvgWindow.addToList(avgWindow.get(z));
				resultWinArray[i] = finalAvgWindow;
				resultWinArray[i]=fixDifference(resultWinArray[i], winArray[i]);
			}
			else
			{
				window finalAvgWindow = new window();
				for(int p =0;p<winArray[0].getList().size();p++)
					finalAvgWindow.addToList(0.0);
				List<Double> temp = finalAvgWindow.getList();
				finalAvgWindow.setList(temp);
				resultWinArray[i] = finalAvgWindow;
				resultWinArray[i]=fixDifference(resultWinArray[i],winArray[i]);
			}
			listOfWindows.clear();
		}
		return resultWinArray;
	}
	
	/**
	 * 
	 * initializes array to zero	
	 * @param size
	 * @return
	 */
	private static Double[] initializeArray(int size) 
	{
		Double newArray[] = new Double[size];
		for( int k=0;k<newArray.length;k++)
			newArray[k]=0.0;
		return newArray;
	}
	
	/**
	 * Performs subtraction and division of each element in the average window list
	 * @param resultWindow
	 * @param w
	 * @return
	 */
	private static window fixDifference(window resultWindow, window w) 
	{
		List<Double> temp = new ArrayList<Double>(w.getList());
		List<Double> temp_result = new ArrayList<Double>(resultWindow.getList());
		double value;
		for(int i=0;i<temp.size();i++)
		{
			value = temp_result.get(i);
			value = temp.get(i)-value;
			temp_result.set(i, value);
		}
		//element wise division operation
		List<Double> another_result = new ArrayList<Double>(temp_result);
		for(int j=0;j<temp.size();j++)
		{
			value=another_result.get(j);
			value=value/temp.get(j);
			another_result.set(j, value);
		}
		window z = new window();
		z.setList(another_result);
		return z;
	}
	
	/**
	 * 
	 * @param args
	 * @throws BiffException
	 * @throws IOException
	 */
	public static void createDifferenceFile() throws BiffException, IOException 
	{
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFiles";
		File [] files =  new File(dirPath).listFiles();
		String epidemicWordFile;
		String epidemicDifferenceFile;
		Map<String,List<String>> assocMap = new HashMap<String,List<String>>();
		CreateAssociationList create = new CreateAssociationList();
		assocMap = create.getAssociationMap();
		BufferedReader br;
		int fileCount =1;
		String line,content,delim=",";
		int fileLength;
		
		for(File file:files)
		{
			fileLength=0;
			line="";
			content="";
			//epidemicWordFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFiles/epidemic_word_file"+fileCount+".csv";
			epidemicWordFile = file.getAbsolutePath();
			epidemicDifferenceFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesDifference/"+file.getName();
			fileLength = computeFileLength(epidemicWordFile);
			List<String> oneHop = new ArrayList<String>();
			idx idxArray[] = new idx[fileLength];
			window winArray[] = new window[fileLength];
			window resultWinArray[];
			br=new BufferedReader(new FileReader(epidemicWordFile));
			String [] afterSplit;
			for(int i=0;i<fileLength;i++)
			{
				//initialize the window and the idx objects
				content = br.readLine();
				afterSplit = content.split(delim);
				idx index = new idx();
				window win = new window();
				
				index.setFile(Integer.parseInt(afterSplit[0]));
				//String st[] = afterSplit[1].split("\\-");
				index.setState(afterSplit[1]);
				index.setIteration(Integer.parseInt(afterSplit[2]));
				
				for(int a = 3; a<afterSplit.length;a++)
				{
					win.addToList(Double.parseDouble(afterSplit[a]));
				}
				
				idxArray[i] = index;
				winArray[i] = win;
			}
			
			
			
			//compute the resultant average window array
			resultWinArray = computeResultWindowArray(winArray,idxArray,assocMap);
			
			//write the result into the File
			writeIntoOutputFile(idxArray,resultWinArray,epidemicDifferenceFile);
			
					
			fileCount++;
		}

	}
}
