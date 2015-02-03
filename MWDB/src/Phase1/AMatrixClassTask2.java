package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

public class AMatrixClassTask2 
{
	int AMatrixRow = -1;
	int AMatrixColumn;

	double[][] AMatrix;
	List<String> file1List = new ArrayList<String>();			
	List<String> file2List = new ArrayList<String>();
	Map<List<Double>,idx> fileMap1 = new LinkedHashMap<List<Double>,idx>();
	Map<List<Double>,idx> fileMap2 = new LinkedHashMap<List<Double>,idx>();
	Map<List<Double>,idx> fileMap1Copy = new LinkedHashMap<List<Double>,idx>();
	Map<List<Double>,idx> fileMap2Copy = new LinkedHashMap<List<Double>,idx>();
	
	
	CreateAssociationList c;
	Map<String,List<String>> associationMap;
	
	AMatrixClassTask2() throws BiffException, IOException
	{
		c = new CreateAssociationList();
		associationMap = c.getAssociationMap();
	}
	
	
	public void calculateRowsInFile(String fileType, String epidemicFileName1,int fNumber){
		FileReader epidemicFile1 = null;
		BufferedReader br1 = null;
		String sCurrentLine1,filePath;
		
		FileReader epidemicFile2 = null;			
		BufferedReader br2 = null;			
		String sCurrentLine2;
		
		if(fileType.equals("Avg"))
		{
			filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg/";
		}
		else if(fileType.equals("Basic"))
		{
			filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic/";
		}
		else
		{
			filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff/";
		}
		
		try 
		{
			epidemicFile1 = new FileReader(epidemicFileName1);
			br1 = new BufferedReader(epidemicFile1);
			double counterForMap1 = 0; 		
			double counterForMap2 = 0; 		
			
			while ((sCurrentLine1 = br1.readLine()) != null)
			{
				
				String line1[] = sCurrentLine1.split(",");
				idx idx1 = new idx();
				List<Double> w = new ArrayList<Double>();
				
				idx1.setState(line1[1]);
				idx1.setIteration(Integer.parseInt(line1[2]));
				
				for(int i = 3; i < line1.length; i++){
					w.add(Double.parseDouble(line1[i]));
				}
				
				ArrayList<Double> windowCopy = new ArrayList<Double>();
				
				if(!fileMap1.containsKey(w))
				{
					fileMap1.put(w, idx1);
					windowCopy.addAll(w);
					windowCopy.add(counterForMap1);
					fileMap1Copy.put(windowCopy, idx1);	
				}
				else
				{
					String state = fileMap1.get(w).getState();
					if(state.equals(idx1.getState())==false)
					{
						fileMap1.put(w, idx1);
						windowCopy.addAll(w);
						windowCopy.add(counterForMap1);			
						fileMap1Copy.put(windowCopy, idx1);			
					}
					
				}
				counterForMap1++;					
			}
			br1.close();
			
			
			
			epidemicFile2 = new FileReader(filePath+fNumber+".csv");
				br2 = new BufferedReader(epidemicFile2);			
				while ((sCurrentLine2 = br2.readLine()) != null)	
				{
					String line2[] = sCurrentLine2.split(",");
					idx idx2 = new idx();
					List<Double> w2 = new ArrayList<Double>();
					idx2.setState(line2[1]);
					idx2.setIteration(Integer.parseInt(line2[2]));
					for(int i=3;i<line2.length;i++)
					{
						w2.add(Double.parseDouble(line2[i]));
					}
					
					ArrayList<Double> windowCopy2 = new ArrayList<Double>();
					
					if(!fileMap2.containsKey(w2))
					{					
						fileMap2.put(w2, idx2);
						windowCopy2.addAll(w2);
						windowCopy2.add(counterForMap2);
						fileMap2Copy.put(windowCopy2, idx2);		
					}
					else
					{
						String state = fileMap2.get(w2).getState();
						if(!state.equals(idx2.getState()))
						{
							fileMap2.put(w2, idx2);
							windowCopy2.addAll(w2);
							windowCopy2.add(counterForMap2);	
							fileMap2Copy.put(windowCopy2, idx2);	
						}
					}
					counterForMap2++;		
				}
				System.out.println("File map 1 sixe" + fileMap1Copy.size());
				System.out.println("File map 2 sixe" + fileMap2Copy.size());
				AMatrix = new double[fileMap1Copy.size()][fileMap2Copy.size()];
				br2.close();
				
				//since AMAtrix is square, number of rows = number of columns
			//AMatrix = new double[numberOfRows][numberOfRows];
			//System.out.println("This is the number of rows " + numberOfRows);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public double performFileOperations(String fileType, String epidemicFileName1,int fNumber) throws IOException{
		
		calculateRowsInFile(fileType, epidemicFileName1,fNumber);
		
		String[] dataOfFile1 = null;
		String[] dataOfFile2 = null;
		boolean isNewLine = false;			//--------------------------------------
		boolean isFirstLine = true;			//--------------------------------------
		
		double finalSimilarity=0;
		idx idx1 = new idx();
		window window1 = new window();
		idx idx2 = new idx();
		window window2 = new window();
		 
		try 
		{
			FileWriter writer = new FileWriter("AMatrixFile.csv");			//--------------------------------------
			for(Map.Entry<List<Double>, idx> f1: fileMap1Copy.entrySet())
			{
				idx1=f1.getValue();
				List<Double> l1=f1.getKey();
				
				isNewLine = true;
				AMatrixRow++;
				AMatrixColumn = 0;
				
				for(Map.Entry<List<Double>, idx> f2:fileMap2Copy.entrySet())
				{
					idx2=f2.getValue();
					List<Double> l2 = f2.getKey();
					int editDistanceSimilarity = editDistance(l1,l2);
					int stateSimilarity = stateSimilarity(idx1.getState(),idx2.getState());
					double iterationSimilarity = iterationCalculation(idx1,idx2,l1.size()-1);
					
					calculateAMatrix(editDistanceSimilarity, iterationSimilarity, stateSimilarity, isNewLine, isFirstLine);
					isNewLine = false;
					isFirstLine = false;
					
				}
			}
			
			//displayMatrix();
			finalSimilarity = getSimilarityFromAMatrix();
			
			
		}		
		catch(Exception e){
			e.printStackTrace();
		}
		return finalSimilarity;
	}
	
	private int stateSimilarity(String state1, String state2) {
		
		// TODO Auto-generated method stub
		System.out.println(state1+"  "+state2);
		
		
		
		if(state1.equals(state2))
			return 6;
		else
		{
			List<String> neighbors = associationMap.get(state1);
			if(neighbors!=null)
			{	
				if(neighbors.contains(state2))
					return 4;
				else
					return 2;
			}
			else
				return 2;
			
		}
	}


	public int editDistance(List<Double> windowList1, List<Double> windowList2)
	{
		int count = 0;
				
		for(int i = 0; i < windowList1.size()-1; i++){			//--------------------------------------
			if(windowList1.get(i).equals(windowList2.get(i))){
				count++;
			}
		}
		return count;
	}
	
	public void calculateAMatrix(int editSimilarity, double timeSimilarity,int stateSimilarity, boolean isNewLine, boolean isFirstLine){
		
		double result = Math.sqrt((editSimilarity*editSimilarity) + (timeSimilarity*timeSimilarity)+Math.pow(stateSimilarity, 2));
		
		/*try 
		{
			if(isNewLine && !isFirstLine)			//--------------------------------------
				writer.append('\n');
			writer.append(result + ",");
				
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
		
		AMatrix[AMatrixRow][AMatrixColumn] = result;
		AMatrixColumn++;
		
	}
	
	public double iterationCalculation(idx idx1, idx idx2, int windowSize){
		double result = 0.0;
		double denominator = 0.0;
		
		if(idx1.getIteration() == idx2.getIteration())
			denominator = 1.0;
		else
			denominator = Math.abs(idx1.getIteration() - idx2.getIteration());
		
		result = windowSize/denominator;
		
		return result;
	}
	
	public void displayMatrix(){
		System.out.println("This is the A Matrix");
		for(int i = 0; i <= AMatrixRow; i++){
			for(int j = 0; j < AMatrixColumn; j++){
				System.out.print(AMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public double getSimilarityFromAMatrix(){
		double sumOfRow = 0.0;
		double finalSimilarity = 0.0;
		
		for(int i = 0; i <= AMatrixRow; i++){
			sumOfRow = 0.0;
			for(int j = 0; j < AMatrixColumn; j++){
				sumOfRow = sumOfRow + AMatrix[i][j];
			}
			finalSimilarity = finalSimilarity + sumOfRow;
		}
		return finalSimilarity;
	}
}
