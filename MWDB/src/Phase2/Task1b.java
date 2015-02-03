package Phase2;

import java.util.Scanner;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class Task1b 
{
	/**
	 * 
	 * Takes two file names and computes the DTW distance between the two files
	 * @return
	 * @throws MatlabInvocationException
	 * @throws MatlabConnectionException
	 */
	public double getDTWSimilarity() throws MatlabInvocationException, MatlabConnectionException
	{
		System.out.println("Enter the file number 1:");
		Scanner in = new Scanner(System.in);
		double file1Number = in.nextDouble();
		System.out.println("Enter the file number 2:");
		double file2Number = in.nextDouble();
		MatlabProxyFactory factory = new MatlabProxyFactory(); //Get Matlab connection
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		proxy.setVariable("file1number", file1Number);
		proxy.setVariable("file2number", file2Number);
		proxy.setVariable("similarity", -1);
		//Call the Matlab function for DTW distance computation.
		proxy.eval("similarity=Phase2_Task1b(file1number,file2number)");
		// Store the result --- a similarity score in a variable that can be used later. 
		double result = ((double[])proxy.getVariable("similarity"))[0];
		proxy.disconnect();
		return result;
	}
	
	
	
}
