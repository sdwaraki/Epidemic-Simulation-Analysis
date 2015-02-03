package Phase3;

import java.util.Scanner;

import Phase2.SimulationSimilarityMatrixGenerator;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class Task3aMain {
	static MatlabProxyFactory factory = new MatlabProxyFactory();
	static MatlabProxy proxy;

	public static double[][] getInputMatrix(String choice)
			throws NumberFormatException, MatlabInvocationException {
		// Directory Path where the simulation files are present.

		String dirPath;

		if (choice.equals("C"))
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Basic";
		else if (choice.equals("D"))
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Avg";
		else
			dirPath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Output/Diff";

		SimulationSimilarityMatrixGenerator s = new SimulationSimilarityMatrixGenerator(
				dirPath,choice);
		s.computeSimulationSimilarityMatrix();
		return s.getSimulationSimilarityMatrix();

	}

	public static void main(String[] args) throws NumberFormatException,
			MatlabInvocationException, MatlabConnectionException {
		boolean flag = true;
		// Get the MATLAB Connections required
		double[][] similarityMatrix = null;
		proxy = factory.getProxy();
		String path = "cd(\'E:/Program Files/MATLAB/R2013b/bin\')";

		while (flag) {
			System.out
					.println("Select Similarity Measure to build the simulation similarity graph:");
			System.out.println("A. Eucledian");
			System.out.println("B. DTW");
			System.out
					.println("C. Dot Product similarity of Epidemic Word Files");
			System.out
					.println("D. Dot Product similarity of Epidemic Average Files");
			System.out
					.println("E. Dot Product similarity of Epidemic Difference Files");
			System.out.println("F. Exit");
			Scanner in = new Scanner(System.in);
			String choice = in.next();

			proxy.eval(path);
			if (choice.equals("A")) {
				// Call MATLAB to get the Eucledian Simulation Similarity Matrix
				// and
				// Plot the graph
				proxy.setVariable("choice", 1);
				proxy.eval("Phase3_Task3a(choice)");
			} else if (choice.equals("B")) {
				// Call MATLAB to get the DTW Simulation Similarity Matrix and
				// Plot
				// the graph
				proxy.setVariable("choice", 2);
				proxy.eval("Phase3_Task3a(choice)");
			} else if (choice.equals("C") || choice.equals("D")
					|| choice.equals("E")) {
				// Get the Simulation Similarity Matrix
				similarityMatrix = getInputMatrix(choice);

				// Send the Simulation Similarity Matrix to the the MATLAB
				// function
				// to get the plot.
				proxy.setVariable("choice", choice);
				MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
				processor.setNumericArray("inputMatrix",
						new MatlabNumericArray(similarityMatrix, null));
				proxy.eval("Phase3_Task3a(choice,inputMatrix)");

			} else {
				flag = false;
			}

		}
	}

}
