package Phase1;

import java.io.IOException;
import java.util.Scanner;

import jxl.read.biff.BiffException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class controllerClass {

	public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException
	{
		// TODO Auto-generated method stub
		System.out.println("Entering task 1");
		normalizeData n = new normalizeData();
		quantizeData q = new quantizeData();
		epidemicWordFileCreation eword = new epidemicWordFileCreation();
		epidemicAverageFileCreation eavg = new epidemicAverageFileCreation();
		epidemicDifferenceFileCreation ediff = new epidemicDifferenceFileCreation();
		try {
			n.normalize();
			System.out.println("Enter the number of bands you require");
			Scanner in = new Scanner(System.in);
			int r = in.nextInt();
			q.createQuantizedFile(r);
			System.out.println("Enter the value of window size");
			int windowSize = in.nextInt();
			System.out.println("Enter the shift Length");
			int shiftLength = in.nextInt();
			eword.createEpidemicWordFile(windowSize,shiftLength);
			//eavg.createAverageFile();
			//ediff.createDifferenceFile();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Problem in calling Normalize function");
		}/** catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}**/ //catch (BiffException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		

	}

}
