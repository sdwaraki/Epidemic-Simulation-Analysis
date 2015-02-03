package Phase2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Phase1.MatlabJavaConnection;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class Task3df 
{

	public static void main(String[] args) throws IOException, MatlabConnectionException, MatlabInvocationException 
	{
		// TODO Auto-generated method stub
		int choice;
		int r;
		System.out.println("1. Task 3a - Top r semantics using SVD");
		System.out.println("2. Task 3b - Top r semantics using LDA");
		System.out.println("3. Task 3c - Top r semantics using Simulation-Simulation Similarity Matrix");
		System.out.println("4. Task 3d - Query using SVD");
		System.out.println("5. Task 3e - Query using LDA");
		System.out.println("6. Task 3f - Query using SVD on Simulation-Simulation Similarity Matrix");
		
		System.out.println("Enter choice");
		Scanner in = new Scanner(System.in);
		
		choice = in.nextInt();
		System.out.println("Enter the value of r");
		in = new Scanner(System.in);
		r = in.nextInt();
		
		
		if(choice ==1)
		{
			Task3a t = new Task3a();
			t.computeUsingSVD(r);	//Computes the top r semantics
		}
		else if(choice == 4)
		{
			System.out.println("Query using SVD..");
			System.out.println("Enter the value of k");
			int k = in.nextInt();
			Task3a obj = new Task3a();
			List<Map<List<BigDecimal>,Integer>> fileVector = obj.generateFileVector();  //Generates the object - feature matrix
			List<List<BigDecimal>> allWindows = obj.getAllWindows(fileVector);  // List of all unique windows
			double matrix[][] = obj.getInputMatrix(fileVector,allWindows);		//Gets the input matrix
			//System.out.println("Enter the file number for the query");
			//int fileNumber= in.nextInt();
			String filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_query.csv";
			double queryMatrix[][] = convertQueryToVector(allWindows,filePath);			//Converts the query in the space of unique windows
			
			MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
			MatlabProxy proxy = factory.getProxy();
			String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
			proxy.eval(path);
			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("inputMatrix", new MatlabNumericArray(matrix, null));
			processor.setNumericArray("p", new MatlabNumericArray(queryMatrix,null));
			proxy.setVariable("r",r);
			proxy.setVariable("k", k);
			//MATLAB function to call the query on the latent semantics
			proxy.eval("queryTopSemantic(inputMatrix,p,k,r)");
			
		}
		else if(choice ==3)
		{
			Task3c t = new Task3c();
			System.out.println("Enter your choice (A,B,C) similarity measure from Task 1 you want to consider:");
			System.out.println("A. Similarity using Eucledian Distance");
			System.out.println("B. Similarity using DTW");
			System.out.println("C. Similarity using Dot Product");
			String ch = in.next();
			//Compute the Top R latent semantics using the simulation simulation similarity Matrix
			t.computeTopSemantics(ch,r);
			
		}
		else if(choice == 6)
		{
			System.out.println("Enter your choice (A,B,C) similarity measure from Task 1 you want to consider:");
			System.out.println("A. Similarity using Eucledian Distance");
			System.out.println("B. Similarity using DTW");
			System.out.println("C. Similarity using Dot Product");
			String ch = in.next();
			System.out.println("Enter the value of k");
			int k = in.nextInt();
			//System.out.println("Enter the fileNumber for the query");
			//int fileNumber = in.nextInt();
			String dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/";
			String filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_query.csv";
			double simulationSimilarityMatrix [][]=null;
			if(ch.equals("C"))
			{
				SimulationSimilarityMatrixGenerator s = new SimulationSimilarityMatrixGenerator(dirPath,ch);
				s.computeSimulationSimilarityMatrix();
				simulationSimilarityMatrix = s.getSimulationSimilarityMatrix();
			}
			else if(ch.equals("A")||(ch.equals("B")))
			{
				int c =0;
				MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
				MatlabProxy proxy = factory.getProxy();
				String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
				proxy.eval(path);
				if(ch.equals("A"))
					c=1;
				else
					c=2;
				proxy.setVariable("choice", c);
				proxy.setVariable("similarityMatrix",0);
				proxy.eval("similarityMatrix=Task3f(choice);");
				MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
				MatlabNumericArray array = processor.getNumericArray("similarityMatrix");
				simulationSimilarityMatrix = array.getRealArray2D();
	
			}
			Task3a t = new Task3a();
			List<Map<List<BigDecimal>,Integer>> fileVector = t.generateFileVector();
			List<List<BigDecimal>> allWindows = t.getAllWindows(fileVector);
			double inputMatrix [][] = t.getInputMatrix(fileVector, allWindows);  
			double queryMatrix [][] = convertQueryToVector(allWindows,filePath);
			double queryVectorInTermsOfSimulations[][];		
			MatlabJavaConnection obj = new MatlabJavaConnection();
			obj.queryOnLatentSemantics(queryMatrix, inputMatrix, simulationSimilarityMatrix, r, k);
		
		}
		
		
	}
	
	
	/**
	 * 
	 * 
	 * @param allWindows - List of all unique windows
	 * @param queryFilePath
	 * @return - matrix that represents the query vector
	 * @throws IOException
	 */
	public static double[][] convertQueryToVector(List<List<BigDecimal>> allWindows, String queryFilePath) throws IOException
	{
		List<Map<List<BigDecimal>,Integer>> queryFileVector = new ArrayList<Map<List<BigDecimal>,Integer>>();
		LinkedHashMap<List<BigDecimal>,Integer> windowCount;
		BufferedReader br = new BufferedReader(new FileReader(queryFilePath));
		windowCount = new LinkedHashMap<List<BigDecimal>,Integer>();
		String line;
		String[]lineContent;
		
		//Read all the lines  in the query and find its unique windows
		//windowCount - Maps Window to its count
		//queryFileVector - The whole object feature matrix
		//allWindows - List of unique windows
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
		queryFileVector.add(windowCount);
		
	/**	for(Map<List<BigDecimal>, Integer> m : queryFileVector)
		{
			for(Map.Entry<List<BigDecimal>, Integer> e : m.entrySet())
			{
				if(!allWindows.contains(e.getKey()))
				{
					allWindows.add(e.getKey());
				}
			}
		}
		**/
		
		for(List<BigDecimal> win : allWindows)
		{
			for(Map<List<BigDecimal>, Integer> m : queryFileVector)
			{
				if(!m.containsKey(win))
				{
					m.put(win, 0);
				}
				
			}
		}
		
		double queryMatrix[][] = new double[queryFileVector.size()][allWindows.size()];
		
		for(List<BigDecimal> win : allWindows)
		{	
			for(int i = 0; i < queryFileVector.size(); i++)
			{
				queryMatrix[i][allWindows.indexOf(win)] = queryFileVector.get(i).get(win);
			}
			
		}
		
		return queryMatrix;
		
	}
	
	

}
