/**
 * 
 * @author Sumanth
 */

/**
 * 
 * Program to compute the top r latent semantics given a set of Epidemic Simulation files using SVD.
 * 
 */
package Phase2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class Task3a 
{
	
	
	
	@SuppressWarnings("unchecked")
	public static void computeUsingSVD(int r) throws IOException, MatlabConnectionException, MatlabInvocationException 
	{
		// TODO Auto-generated method stub
		//Create the Epidemic Word Files
		
		
		List<List<BigDecimal>> allWindows;
		List<Map<List<BigDecimal>,Integer>> fileVector;
		fileVector = generateFileVector();
		allWindows = getAllWindows(fileVector);
		double matrix[][] = getInputMatrix(fileVector,allWindows);
		
		
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("inputMatrix", new MatlabNumericArray(matrix, null));
		proxy.setVariable("r",r);
		proxy.eval("computeSVD(inputMatrix,r)");
			
	}
	
	
	public static double[][] getInputMatrix(List<Map<List<BigDecimal>,Integer>> fileVector, List<List<BigDecimal>> allWindows)
	{
		

		double [][] matrix = new double[fileVector.size()][allWindows.size()];
		int j=0;
		
		for(List<BigDecimal> win : allWindows)
		{	
			for(int i = 0; i < fileVector.size(); i++)
			{
				matrix[i][allWindows.indexOf(win)] = fileVector.get(i).get(win);
			}
			
		}
		
		return matrix;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<List<BigDecimal>,Integer>> generateFileVector() throws IOException
	{
		
		String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/";
		File files [] = new File(dirPath).listFiles();
		
		Arrays.sort(files, new Comparator()
		{
		    @Override
		    public int compare(Object f1, Object f2) {
		        String fileName1 = ((File) f1).getName();
		        String fileName2 = ((File) f2).getName();

		        String fileId1 = fileName1.replaceAll("[^0-9]", ""); //split("_")[2].substring(4);
		        String fileId2 = fileName2.replaceAll("[^0-9]", ""); //.split("_")[2].substring(4);
		        
//		        String a = fileId1.split("\\.")[0];
//		        String b = fileId2.split("\\.")[0];
//		        int f1number = Integer.parseInt(a);
//		        int f2number = Integer.parseInt(b);	
		        return Integer.parseInt(fileId1) - Integer.parseInt(fileId2);
		    }
		});
		
				
		int fileCount = 1;
		BufferedReader br;
		String filePath, line;
		String lineContent[];
		
		List<Map<List<BigDecimal>,Integer>> fileVector = new ArrayList<Map<List<BigDecimal>,Integer>>();
		LinkedHashMap<List<BigDecimal>,Integer> windowCount;
		
						
		for(int k =0;k<files.length;k++)
		{
			filePath = files[k].getAbsolutePath();
			String fileName = files[k].getName();
			//System.out.println(filePath);
			
				br = new BufferedReader(new FileReader(filePath));
				windowCount = new LinkedHashMap<List<BigDecimal>,Integer>();
				while((line=br.readLine())!=null)
				{
					lineContent = line.split(",");
					List<BigDecimal> l = new ArrayList<BigDecimal>();
					for(int i=3;i<lineContent.length;i++)
					{
						l.add(new BigDecimal(lineContent[i]));
					}
					if(!windowCount.containsKey(l))
					{
						windowCount.put(l, 1);
					}
					else
					{
						int temp = windowCount.get(l);
						temp++;
						windowCount.put(l, temp);
					}		
				}
				fileVector.add(windowCount);
			
		}
		
		return fileVector;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<List<BigDecimal>> getAllWindows(List<Map<List<BigDecimal>,Integer>> fileVector) throws FileNotFoundException
	{		
		List<List<BigDecimal>> allWindows = new ArrayList<List<BigDecimal>>();
		for(Map<List<BigDecimal>, Integer> m : fileVector)
		{
			for(Map.Entry<List<BigDecimal>, Integer> e : m.entrySet())
			{
				if(!allWindows.contains(e.getKey()))
				{
					allWindows.add(e.getKey());
				}
			}
		}
		
		for(List<BigDecimal> win : allWindows)
		{
			for(Map<List<BigDecimal>, Integer> m : fileVector)
			{
				if(!m.containsKey(win))
				{
					m.put(win, 0);
				}
				
			}
		}
		
		return allWindows;
		
	}
	
	
	
	
	
	
}
