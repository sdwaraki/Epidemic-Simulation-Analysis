/**
 * @author Sumanth
 *
 */

// Create Epidemic Word File using the Task-2(Part 1) Formula
package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

public class epidemicAverageFileCreation 
{
	
	/**
	 * Writes the Avg Window content into the file
	 * @param idxArray
	 * @param winArray
	 * @param outputFile
	 * @throws IOException
	 */
	public static void writeIntoOutputFile(idx[] idxArray, window[] winArray, String outputFile) throws IOException
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
	 * multiply (1-alpha) to each window_avg
	 * @param avgWindow
	 * @param alpha
	 * @return
	 */
	public static List<Double> addWeight(List<Double> avgWindow,double alpha)
	{
		double weightValue;
		List<Double> temp = new ArrayList<Double>(avgWindow);
		for(int i=0;i<temp.size();i++)
		{
				weightValue = temp.get(i);
				weightValue = weightValue*(1-alpha);
				temp.set(i, weightValue);
		}
		return temp;
		
	}
	
	
	/**
	 * Compute the alpha*win[i] + (1-alpha)(avg of win[i])
	 * @param resultWindow
	 * @param alpha
	 * @param w
	 * @return
	 */
	public static window fixWeight(window resultWindow, double alpha, window w)
	{
		List<Double> temp = new ArrayList<Double>(w.getList());
		double value;
		for(int i=0;i<temp.size();i++)
		{
			value = alpha * temp.get(i);
			temp.set(i, value);
		}
		
		List<Double> t;
		t = resultWindow.getList();
		for(int j=0;j<t.size();j++)
		{
			value = temp.get(j) + t.get(j);
			t.set(j, value);
		}
		resultWindow.setList(t);
			
		return resultWindow;
	}
	
	
	/**
	 * Initialize an array to zeroes
	 * @param size
	 * @return
	 */
	public static Double[] initializeArray(int size)
	{
		Double newArray[] = new Double[size];
		for( int k=0;k<newArray.length;k++)
			newArray[k]=0.0;
		return newArray;
	}
	
	/**
	 * Compute length of file
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
	
	
	/**
	 * Computes the window average for each window
	 * @param winArray
	 * @param idxArray
	 * @param listOfWindows
	 * @param assocMap
	 * @param alpha
	 * @return
	 */
	public static window[] computeResultWindowArray(window[] winArray, idx[] idxArray, Map<String,List<String>> assocMap, double alpha)
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
				avgWindow = addWeight(avgWindow,alpha);
				
						
				
				window finalAvgWindow = new window();
				for(int z=0;z<avgWindow.size();z++)
					finalAvgWindow.addToList(avgWindow.get(z));
				resultWinArray[i] = finalAvgWindow;
				resultWinArray[i]=fixWeight(resultWinArray[i], alpha,winArray[i]);
				
			}
			else
			{
				window finalAvgWindow = new window();
				for(int p =0;p<winArray[0].getList().size();p++)
					finalAvgWindow.addToList(0.0);
				List<Double> temp = finalAvgWindow.getList();
				temp = addWeight(temp,alpha);
				finalAvgWindow.setList(temp);
				resultWinArray[i] = finalAvgWindow;
				resultWinArray[i]=fixWeight(resultWinArray[i], alpha,winArray[i]);
			
			}
			listOfWindows.clear();
		}
		
		return resultWinArray;
	}
	
	
	public static void createAverageFile() throws IOException, BiffException 
	{
		// TODO Auto-generated method stub
		
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFiles";
		File [] files =  new File(dirPath).listFiles();
		Map<String,List<String>> assocMap = new HashMap<String,List<String>>();
		CreateAssociationList create = new CreateAssociationList();
		assocMap = create.getAssociationMap();
		String epidemicWordFile, outputEpidemicAvgFile; 
		BufferedReader br;
		int fileCount =1;
		int fileLength;
		String line;
		String content, delim =",";	
		double alpha = 0.6;
		
		/**
		 *  Iterate for each file
		 */
		for(File file:files)
		{	
			//System.out.println("file "+fileCount);
			fileLength =0;
			line="";
			content="";
			epidemicWordFile = file.getAbsolutePath();
			outputEpidemicAvgFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFilesAverage/"+file.getName();
			fileLength=computeFileLength(epidemicWordFile);
			String afterSplit[];
			List<String> oneHop = new ArrayList<String>();
			
			idx idxArray[] = new idx[fileLength];
			window winArray[] = new window[fileLength];
			window resultWinArray[];
			br=new BufferedReader(new FileReader(epidemicWordFile));
			
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
			
				
			
			resultWinArray = computeResultWindowArray(winArray,idxArray,assocMap,alpha);
			
			
			//write into file-----------function
			writeIntoOutputFile(idxArray,resultWinArray,outputEpidemicAvgFile);
			
			//empty lists before next iteration
			
			br.close();
			fileCount++;
		}
		
	}
}

