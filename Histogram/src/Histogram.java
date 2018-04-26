import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Histogram {

	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;
	private int[] hist;

	public Histogram(int numRows, int numCols, int minVal, int maxVal) 
	{
		this.numRows = numRows;
		this.numCols = numCols;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.hist = new int[maxVal + 1];
	}

	private void computeHistogram(Scanner input, PrintWriter output)
	{
		
		while (input.hasNext()) {
			hist[input.nextInt()]++;
		}
		for (int i = 0; i < hist.length; i++) 
		{
			output.println(i + " " + hist[i]);
		}

	}

	public static void main(String[] args)
	{
		Scanner 	input  = null; 
		PrintWriter output = null;
		try {
			input = new Scanner(new File(args[0]));
			output = new PrintWriter(args[1]);
			int numRows = input.nextInt();
			int numCols = input.nextInt();
			int minVal = input.nextInt();
			int maxVal = input.nextInt();
			
			// output header
			output.println(numRows + " " + numCols + " " + minVal + " " + maxVal);

			Histogram histogram = new Histogram(numRows, numCols, minVal, maxVal);
			histogram.computeHistogram(input, output);

		} catch (FileNotFoundException e) {
			System.err.println("File Not found");

		} finally {
			input.close();
			output.close();
		}
	}

}
