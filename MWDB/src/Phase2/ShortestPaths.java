package Phase2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Sumanth 
 * 
 */

public class ShortestPaths 
{

	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		String filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Graphs/LocationMatrix-1.csv";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		String[][] content = new String[52][52];
		Double[][] adj = new Double[51][51];
		String delim = ",", delim2 = "-";
		int rowcount =0;
		int lineNumber =1;
		String states[] = new String[51];
		while((line=br.readLine())!=null)
		{
			if(lineNumber==1)
			{	
				lineNumber++;
				states=line.split(delim);
				continue;
			}
			content[rowcount]=line.split(delim);
			for(int i=0;i<51;i++)
			{
				//System.out.println(content[rowcount][i+1]);
				adj[rowcount][i]=Double.parseDouble(content[rowcount][i+1]);
			}
			rowcount++;
			lineNumber++;
		}
		
		/*for(int i=0;i<adj[0].length;i++)
		{
			for(int j=0;j<51;j++)
			{
				System.out.print(adj[i][j] +" ");
			}
			System.out.println("\n");
		}
		
		for(int i=0;i<51;i++)
			states[i]=states[i+1];
		
		
		for(int i=0;i<51;i++)
			System.out.println(states[i]);
		
		
		for(int i=0;i<51;i++)
		{
			for(int j=0;j<51;j++)
			{
				if(i==j)
					adj[i][j]=0.0;
				
				if(adj[i][j]==0)
					adj[i][j]=99.0;
			
			}
		}
		
		
		Double[][] pathMatrix = adj;
		
		
		Double [][] distanceMatrix = computeShortestPaths(pathMatrix);
		
		for(int i=0;i<51;i++)
		{
			for(int j=0;j<51;j++)
			{
				if(distanceMatrix[i][j]==99.0)
					distanceMatrix[i][j]=0.0;
			}
		}
		
		
		
		
		System.out.println("Print");*/

	}
	
	public static Double[][] computeShortestPaths(Double[][] adj)
	{
		for(int k=0;k<adj.length;k++)
		{
			for(int i=0;i<adj.length;i++)
			{
				for(int j=0;j<adj.length;j++)
				{
					if(adj[i][k]==99.0 || adj[k][j]==99.0)
						continue;
					
					if(adj[i][j]<adj[i][k]+adj[k][j])
					{
						adj[i][j] = adj[i][k]+adj[k][j];
					}
					
					
				}
			}
		}
		return adj;
	}
	
	
	
	
	

}
