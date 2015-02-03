/**
 * @author Sumanth
 *
 */

/** Normalize Data Files in the CSV files **/

package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class normalizeData 
{
	public static void normalize() throws IOException 
	{
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/Input Simulation Files";
		int fileCount=new File(dirPath).listFiles().length;
		int index =1;
		File [] files =  new File(dirPath).listFiles();
		
		for(File file : files)
		{
			String filePath = file.getAbsolutePath();
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String delim =",";
			String [][] content = new String[214][53];
			String line="";
			int rowcount=0;
			int i,j;
			double max = 0;
			double value;
			//Read all the lines into the array "content"
			while((line=br.readLine())!=null)
			{	
				content[rowcount]=line.split(delim);
				rowcount++;
			}
			br.close();
			max=Double.parseDouble(content[2][2]);
			
			//Find out the maximum of the array.
			for(i=1;i<214;i++)
			{
				for(j=2;j<53;j++)
				{
					if(Double.parseDouble(content[i][j])>=max)
					{
						max=Double.parseDouble(content[i][j]);
					}
				}
			}
			
			//Compute the Normalized value for a particular file
			
			for(i=1;i<214;i++)
			{
				for(j=2;j<53;j++)
				{
					value=Double.parseDouble(content[i][j]);
					value=value/max;
					content[i][j]=String.valueOf(value);
				}
			}
			
			//Write the content array back into the CSV file
			//Format of Normalized CSV file Norm<Index>.csv
			//String outputFile="C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/NormalizedCSV/Norm"+fileCount+".csv";
			String outputFile="C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/NormalizedCSV/"+file.getName();
			FileWriter writer = new FileWriter(outputFile);
			for(i=0;i<214;i++)
			{
				for(j=0;j<53;j++)
				{
					writer.append(content[i][j]);
					writer.append(",");
					
				}
				writer.append("\n");
			}
				
			writer.close();
			index++;
		}
	
	}
}
