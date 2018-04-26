import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// read the Input: A binary image.
		try (   Scanner     input   = new Scanner(new File(args[0]));
				
				// II. outFiles:
				// (a) outFile1: in args[1] for :
				// 1) Pretty print the result of Pass-1 and the EQAry with proper captions.
				// 2) Pretty print the result of Pass-2 *and* the EQAry with proper captions.
				// 3) print EQARY after manage the EQARY
				// 4) Pretty print the result of Pass-3 and the EQAry with the proper caption
				PrintWriter outFile1 = new PrintWriter(new File(args[1]));
				
				// (b) outFile2: in args[2] for:
				// 5) create image file from result of Pass-3 with header future processing.
				PrintWriter outFile2 = new PrintWriter(new File(args[2]));
				
				// (c) outFile3 : in agrs[3] for:
				// (6) Output the connected component property using the format given below
				
				// print the header of CC image, followsby the
				// the true numbers of the c.c. follows by the properties of
				// each c.c. component : For an example below in method
				PrintWriter outFile3 = new PrintWriter(new File(args[3]))
			) {
			
			int	numRows = input.nextInt();
			int numCols = input.nextInt();
			int minVal = input.nextInt();
			int maxVal = input.nextInt();
			
			int newMin, newMax;
			ConnectedCompnent cc = new ConnectedCompnent(numRows,numCols, minVal, maxVal);
			cc.loadImage(input);
			// Pass1
			cc.connectCC_Pass1();
			cc.prettyPrint(outFile1,"\nthe Result of Pass1:  \n");
			cc.printEQAry(outFile1,"\nEQAry1:  \n");
			
			// Pass2
			cc.connectCC_Pass2();
			cc.prettyPrint(outFile1,"\n the result of Pass2:  \n");
			cc.printEQAry(outFile1,"\n Updated EQAry\n");
			cc.manageAry();
			cc.printEQAry(outFile1,"\n After Managing EQAry:\n");
			
			// Pass3
			cc.connectCC_Pass3();
			cc.prettyPrint(outFile1,"\n the Reslut of Pass3:  \n");
			cc.printResult(outFile2,"\n the result of Pass3:  \n");
			cc.printProperty(outFile3);
			input.close();
			
			
			outFile1.close();
			outFile2.close();
			outFile3.close();

			
			
		} catch (FileNotFoundException e) {
			System.err.println(args[0] + ": is not in dirctory.");
			e.printStackTrace();
		}
		
		
	}

}
