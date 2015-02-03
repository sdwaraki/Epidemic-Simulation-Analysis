/**
 * @author Sumanth
 *
 */

/* Quantize Data into their respective band lengths **/
package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class quantizeData 
{
	/**
	 * Calculates the quantized values
	 * @param resolutionBands
	 * @param bandCenters
	 * @throws IOException
	 */
	public static void computeQuantizedValues(double resolutionBands[],double bandCenters[]) throws IOException
	{
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/NormalizedCSV";
		File [] files =  new File(dirPath).listFiles();
		String filePath;
		BufferedReader br;
		String delim =",";
		String[][] content = new String[214][53];
		String line;
		int rowcount;
		boolean flag;
		double value;
		String outputFile;
		for(File file: files)
		{
			filePath = file.getAbsolutePath();
			br = new BufferedReader(new FileReader(filePath));
			line="";
			rowcount=0;
			while((line=br.readLine())!=null)
			{
				content[rowcount] =line.split(delim);
				rowcount++;
			}
			br.close();
			
			//Compute the range in which each value falls and assign the corresponding bandCenter value
			for(int i=1;i<214;i++)
			{	
				for(int j=2;j<53;j++)
				{
					flag=false;
					for(int k=0;k<resolutionBands.length;k++)
					{
						if(Double.parseDouble(content[i][j])<=resolutionBands[k])
						{
							value = bandCenters[k];
							content[i][j]=String.valueOf(value);
							flag=true;
							break;
						}
						
					}
					if(flag==false)
					{
						value=bandCenters[resolutionBands.length-1];
						content[i][j]=String.valueOf(value);
					}
				}
			}
			
			//Write the quantized Content into a seperate file
			//Format of Quantized file Quant<fileCount>.csv
			outputFile = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Epidemic Simulation Datasets/QuantizedCSV/"+file.getName();
			FileWriter writer = new FileWriter(outputFile);
			for(int i=0;i<214;i++)
			{
				for(int j=0;j<53;j++)
				{
					writer.append(content[i][j]);
					writer.append(",");
				}
				writer.append("\n");
			}
			writer.close();
			
		}
	}
		
	public static void createQuantizedFile(int r) throws MatlabConnectionException, MatlabInvocationException, IOException 
	{
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		String command = "res=Gaussian("+r+")";
		proxy.eval(command); //Computes Gaussian.m which calculates the Gaussian lengths
		//resolutionBandLengths contains the r Gaussian values
		double resolutionBandLengths [] =(double[]) proxy.getVariable("res"); //Converts Matlab result to Java array.
		double bandCenters[] = new double[resolutionBandLengths.length];//bandCenters contains the bandRepresentative values
		
		double resolutionBands[]= new double[resolutionBandLengths.length];
		//Calculate resolutionBands
		
		int a =0;
		double sum=0;
		for(int i=0;i<resolutionBandLengths.length;i++)
		{
			sum = sum+resolutionBandLengths[i];
			resolutionBands[a]=sum;
			a++;
		}
		
		
		
		
		//Calculating bandCenters or bandRepresentatives
		for(int i=0;i<resolutionBands.length;i++)
		{
			if(i==0)
			{
				bandCenters[i]=resolutionBands[i]/2;
				continue;
			}
			bandCenters[i] = resolutionBands[i-1]+(resolutionBands[i]-resolutionBands[i-1])/2;
		}
		
		
			
		proxy.disconnect();
		
		//Find the quantized values based on the bandCenters and the resolutionBands
		computeQuantizedValues(resolutionBands,bandCenters);
	}

}
