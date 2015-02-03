package Phase2;

import java.io.IOException;
import java.util.Scanner;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class Task4 {

	
	/**
	 * Function takes in the number of latent dimensions and project the imput simulation files to latent dimensions and
	 * then computes the mapping error.
	 * @throws IOException
	 * @throws MatlabInvocationException
	 * @throws MatlabConnectionException
	 */
	public static void Task4a() throws IOException, MatlabInvocationException, MatlabConnectionException 
	{
		
		System.out.println("Enter the number of latent dimensions");
		Scanner in = new Scanner(System.in);
		int k = in.nextInt();
		double[][] pivot = new double[2][k];
		double col =0;
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);		
		proxy.setVariable("k", k);
		//col is the pointer to the dimension being updated
		proxy.setVariable("col", col);
		proxy.setVariable("mappingError", 0);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		processor.setNumericArray("pivot",new MatlabNumericArray(pivot,null));
		proxy.eval("mappingError = calculateMappingError(k,pivot,col);");
		double mappingError = ((double[]) proxy.getVariable("mappingError"))[0];
		System.out.println("Mapping Error = "+mappingError);
				
	}
	
	/**
	 * Function takes in a query and computes the top k simulations that are most similar to the query.
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 */
	public static void Task4b() throws MatlabConnectionException, MatlabInvocationException
	{
		//
		String queryFilePath = "C:/Users/Sumanth/workspace/Phase 1 Programs/InputQuery/";
		System.out.println("Enter the name of the query file");
		Scanner in = new Scanner(System.in);
		String inputFileName = in.next();
		queryFilePath = queryFilePath+inputFileName+".csv";
		System.out.println("Enter the number of latent dimensions");
		double r = in.nextDouble();
		System.out.println("How many similar simulation files do you want?");
		double k = in.nextDouble();
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);		
		proxy.setVariable("queryFilePath", queryFilePath);
		proxy.setVariable("r", r);
		proxy.setVariable("k", k);
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		proxy.eval("findSimilarSimulations(k,r,queryFilePath)");
		
		
	}
	
	
	public static void main(String args[]) throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		System.out.println("Enter choice : 1)4a	2)4b");
		Scanner in = new Scanner(System.in);
		int ch = in.nextInt();
		if(ch==1)
			Task4a();
		else
			Task4b();
	
	}

}
