package Phase2;

import java.io.IOException;
import java.util.Scanner;

import jxl.read.biff.BiffException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import Phase1.AFunctionMain;

public class Task1Main {

	public static void main(String[] args) throws MatlabInvocationException, MatlabConnectionException, BiffException, IOException 
	{
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		double sim;
		int choice;
		while(true)
		{
				
			System.out.println("Enter choice for task 1");
			System.out.println("1. Task 1a");
			System.out.println("2. Task 1b");
			System.out.println("3. Task 1c");
			System.out.println("4. Task 1d");
			System.out.println("5. Task 1e");
			System.out.println("6. Task 1f");
			System.out.println("7. Task 1g");
			System.out.println("8. Task 1h");
			System.out.println("9. Exit");
			if(in.hasNextInt())
			{
				choice = in.nextInt();
				
				
				
				if(choice==1)
				{
					Task1a t = new Task1a();
					sim = t.getEuclideanDistance();
					System.out.println("Similarity = "+sim);
					
				}
				else if(choice ==2)
				{
					Task1b t = new Task1b();
					sim = t.getDTWSimilarity();
					System.out.println("Similarity = "+sim);
					
				}
				else if(choice ==3 ||choice==4||choice==5)
				{
					System.out.println("Enter the name of file 1");
					String fileOneName = in.next();
					System.out.println("Enter the name of file 2");
					String fileTwoName = in.next();
					RunTask1CDE r = new RunTask1CDE();
					sim = r.RunCDE(choice,fileOneName,fileTwoName);
					System.out.println("Similarity = "+sim);
				}
				else if(choice==6||choice==7||choice==8)
				{
						 AFunctionMain obj = new AFunctionMain();	
						 obj.computeAFunction();
				}
				else if(choice==9)
				{
					break;
				}
				
			}
		}
		in.close();
	}

}
