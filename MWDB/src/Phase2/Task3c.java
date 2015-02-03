package Phase2;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class Task3c 
{
	/**
	 * 
	 * @return inputMatrix
	 * @throws MatlabInvocationException 
	 * @throws NumberFormatException 
	 */
	public static double[][] getInputMatrix(String choice) throws NumberFormatException, MatlabInvocationException
	{
		
		String dirPath = "C:/Users/Sumanth/workspace/Phase 1 Programs/InputData/";
		SimulationSimilarityMatrixGenerator s = new SimulationSimilarityMatrixGenerator(dirPath,choice);
		s.computeSimulationSimilarityMatrix();
		return s.getSimulationSimilarityMatrix();
		
	}
	
	
	public static void computeTopSemantics(String ch,int r) throws MatlabConnectionException, MatlabInvocationException
	{
			
		MatlabProxyFactory factory = new MatlabProxyFactory();//Get Matlab connection
		
		MatlabProxy proxy = factory.getProxy();
		String path="cd(\'E:/Program Files/MATLAB/R2013b/bin\')";
		proxy.eval(path);
		proxy.setVariable("r",r);
		int choice;
		
		if(ch.equals("A"))
			choice=1;
		else if(ch.equals("B"))
			choice=2;
		else
			choice =3;
		
		if(choice==1 || choice==2)
		{
			proxy.setVariable("choice", choice);
			proxy.eval("computeSimulationSimilarity(choice,r)");
		}
		else
		{
			double [][] matrix = getInputMatrix(ch);
			MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
			processor.setNumericArray("inputMatrix", new MatlabNumericArray(matrix,null));
			proxy.eval("computeSVD(inputMatrix,r)");
		}
			
	}
	
		
}
