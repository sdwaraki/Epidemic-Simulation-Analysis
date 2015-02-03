package Phase1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import jxl.read.biff.BiffException;

public class AFunctionMainTask2 {

	public static void main(String[] args) throws BiffException, IOException, MatlabConnectionException, MatlabInvocationException
	{
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		// a = new AMatrixClassTask2();
		
		System.out.println("Please enter type of file");
		String fileType = sc.next();
		System.out.println("Enter the value of k");
		int k = sc.nextInt();
		String queryFilePath,dirPath;
		//System.out.println("File Name Format : epidemic_word_file_(avg/diff/basic)_<filenumber>");
		if(fileType.equals("Avg"))
		{
			queryFilePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_avg_query.csv";
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg";
		}
		else if(fileType.equals("Diff"))
		{
			queryFilePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_diff_query.csv";
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff";
		}
		else
		{
			queryFilePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Query/epidemic_word_file_basic_query.csv";
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic";
		}
		
		File [] files =  new File(dirPath).listFiles();
		double sArray[][] = new double[files.length][1];
		
		
		for(int i=0;i<files.length;i++)
		{
			AMatrixClassTask2 a = new AMatrixClassTask2();
			sArray[i][0] = a.performFileOperations(fileType, queryFilePath,i+1);
		}
		
		
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		proxy.setVariable("queryPath", "C:/Users/Sumanth/workspace/Phase 1 Programs/InputQuery/query.csv");
		proxy.setVariable("k",k);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("inputMatrix", new MatlabNumericArray(sArray, null));
		proxy.eval("getHeatMapsForAFunction(inputMatrix,queryPath,k)");
		
		
		System.out.println("Print");
	}

}
