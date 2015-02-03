/**
 * @author Sumanth
 *
 */

/* Creating an Epidemic Word file with window length 3 and shift length 2 */

package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class epidemicWordFileCreation 
{

	public static void createEpidemicWordFile(int windowSize,int shiftLength) throws IOException 
	{
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/QuantizedCSV";
		File [] files =  new File(dirPath).listFiles();
		int fileCount=1;
		BufferedReader br;
		String[][] content = new String[214][53];
		String line ="";
		int rowcount;
		String filePath;
		String delim = ",", delim2 = "-";
		List<String> stateList = new ArrayList<String>(); 
		Map<String, List<Double>> stateValues = new HashMap<String,List<Double>>();
		String state;
		String [] afterSplit;
		
		//Set windowSize and ShiftLength
			
		Map<idx,window> epidemicData = new LinkedHashMap<idx,window>();
		
		for(File file:files)
		{
			
			filePath = file.getAbsolutePath();
			String outputFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/EpidemicWordFiles/"+file.getName();
			br = new BufferedReader(new FileReader(filePath));
			rowcount=0;
			
			//Getting all the data into content array
			while((line=br.readLine())!=null)
			{
				content[rowcount]=line.split(delim);
				rowcount++;
			}
			br.close();
			
			//Getting a list of states
			for(int i=2;i<53;i++)
			{
				stateList.add(content[0][i]);
			}
			
			
			//Populating statewise entries. Each mapping in stateValues becomes <State> ---> <List of values>
			for(int i=1;i<214;i++)
			{
				for(int j=2;j<53;j++)
				{
					if(!stateValues.containsKey(stateList.get(j-2)))
					{
						state = stateList.get(j-2);
						List<Double> entries = new ArrayList<Double>();
						entries.add(Double.parseDouble(content[i][j]));
						stateValues.put(state, entries);
					}
					else
					{
						state = stateList.get(j-2);
						List<Double> temp = new ArrayList<Double>(); 
					    temp =stateValues.get(state);
					    temp.add(Double.parseDouble(content[i][j]));
					    stateValues.put(state,temp);
					}
						
				}
			}
			List<Double> entries = new ArrayList<Double>();
			
			//Getting the Data into idx and window object and then writing it into epidemic data hashmap. 
			//Epidemic data hashmap mapping : <idx>-----><window>
			for(Map.Entry<String, List<Double>> entry: stateValues.entrySet())
			{
				state = entry.getKey();
				entries=entry.getValue();
				
				for(int k=0;k<entries.size();k=k+shiftLength) //entries contains the List of values from stateList entry
				{
					idx obj = new idx();	//create new index object
					obj.setFile(fileCount);
					afterSplit = state.split(delim2);
					obj.setState(afterSplit[1]);
					obj.setIteration(k+1);
					window w = new window();		//create new window object 
					w.setShiftLength(shiftLength);
					w.setSize(windowSize);
					for(int x=k;x<k+windowSize;x++)		//Loop to create windows
					{
						if(x<entries.size())
						{
							w.addToList(entries.get(x));
						}
						else							//To handle the last window case
						{
							double temp = entries.get(entries.size()-1);
							while(x<k+windowSize)
							{
								w.addToList(temp);
								x++;
							}
						}
					}
					epidemicData.put(obj, w);
				}
				
				
				//Writing data into epidemicWordFile
				FileWriter writer;
				boolean alreadyExists = new File(outputFile).exists();
				if(!alreadyExists)
					writer = new FileWriter(outputFile);
				else
					writer = new FileWriter(outputFile,true);
				
				for(Map.Entry<idx, window> e: epidemicData.entrySet())
				{
					idx index = e.getKey();
					window wind = e.getValue();
					writer.append(String.valueOf(index.getFile()));
					writer.append(",");
					writer.append(index.getState());
					writer.append(",");
					writer.append(String.valueOf(index.getIteration()));
					writer.append(",");
					List<Double> y = wind.getList();
					for(int v=0;v<y.size();v++)
					{
						writer.append(String.valueOf(wind.win.get(v)));
						writer.append(",");
					}
					writer.append("\n");					
				}
				writer.close();
				epidemicData.clear();
				entries.clear();
				
			}
			stateList.clear();
			stateValues.clear();
			fileCount++;
		}
	}

}
