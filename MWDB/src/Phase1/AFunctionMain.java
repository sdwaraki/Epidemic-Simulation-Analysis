package Phase1;

import java.io.IOException;
import java.util.Scanner;

import jxl.read.biff.BiffException;

public class AFunctionMain {

	public void computeAFunction() throws BiffException, IOException{
		Scanner sc = new Scanner(System.in);
		AMatrixClass a = new AMatrixClass();
		
		System.out.println("Please enter type of file");
		String fileType = sc.next();
		//System.out.println("File Name Format : epidemic_word_file_(avg/diff/basic)_<filenumber>");
		System.out.println("Please enter the epidemic file name 1");
		String epidemicFileName1 = sc.next();
		System.out.println("Please enter the epidemic file name 2");
		String epidemicFileName2 = sc.next();
		a.performFileOperations(fileType, epidemicFileName1,epidemicFileName2);
		
		
		
		
	}
}
