import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Average {

	public static void main(String[] args) {

		try { // step 0: open the input file and output file

			Scanner input = new Scanner(new File(args[0]));
			PrintWriter output = new PrintWriter( new File(args[1]));

			// step 0: read the image header, the four numbers
			int numRows = input.nextInt();
			int numCols = input.nextInt();
			int minVal = input.nextInt();
			int maxVal = input.nextInt();
			int newMin = 9999;
			int newMax = -1;
			int[] neighborAry = new int[9];
			int mirrorFramedAry[][];
			int tempAry[][];

			// constructor : dynamically allocate the two (2-D) arrays and assign values to
			// numRows,.. etc
			mirrorFramedAry = new int[numRows + 2][];
			tempAry = new int[numRows + 2][];

			// creating columns with size numCols + 2
			for (int i = 0; i < numRows + 2; i++) {
				mirrorFramedAry[i] = new int[numCols + 2];
				tempAry[i] = new int[numCols + 2];
			}

			// step 1: read the input file and load onto mirrorframeAry begin at [1,1]
			for (int i = 1; i <= numRows; i++) {
				for (int j = 1; j <= numCols; j++) {

					mirrorFramedAry[i][j] = input.nextInt();

				}
			}

			// step 2: mirrorFramed the mirrorFreamedAry.
			for (int row = 1; row < numRows + 1; row++) { // framing Rows starting from row = 1 to row = numRows
				mirrorFramedAry[row][0] = mirrorFramedAry[row][1]; // p(row,0) <-- p(row,1)
				mirrorFramedAry[row][numCols + 1] = mirrorFramedAry[row][numCols]; //
			}
			for (int col = 0; col < numCols + 2; col++) { // framing cols starting from col = 0 to col, the last column
				// in mirrorFramedAry is numCols + 1
				mirrorFramedAry[0][col] = mirrorFramedAry[1][col]; // p(0,col) <-- p(1,col)
				mirrorFramedAry[numRows + 1][col] = mirrorFramedAry[numRows][col]; // the last row in mirrorFramedAry is
				// == numRows + 1

			}

			// step 3: process the MirrorframedAry, from left to right and top to bottom
			// begin at (1,1)
			// step 4: repeat step 3 until all pixels are processed
			int pixel_val;
			for (int i = 1; i < numRows + 1; i++) {
				for (int j = 1; j < numCols + 1; j++) {

					loadNeighbor(i, j, mirrorFramedAry, neighborAry);
					pixel_val = AvgNeihborAry(neighborAry);
					tempAry[i][j] = pixel_val;

					if (pixel_val > newMax) // tracking newMax
						newMax = pixel_val;
					if (pixel_val < newMin) // tracking newMin
						newMin = pixel_val;
				}
			}

			// step 5: output the Image header (numRows, numCols, newMin, newMax) to
			// AVG3X3Out.txt
			output.println(numRows + " " + numCols + " " + newMin + " " + newMax);

			// step 6: output to AVG3X3Out.txt from tempAry, begin at [1,1], without the
			// pixels on the boarder.
			for (int i = 1; i < numRows + 1; i++) {
				for (int j = 1; j < numCols + 1; j++) {
					output.print(tempAry[i][j] + " ");
				}
				output.println();
			}
			// step 7: close input file and AVG3x3Out.txt
			input.close();
			output.close();

		} catch (NullPointerException ex) {
			System.err.println("Need an output File!");
		} catch (FileNotFoundException e) {
			System.err.println("File Not found!");

		}
	}

	private static int AvgNeihborAry(int[] neighborAry) {
		int sum = 0;
		for (int i = 0; i < neighborAry.length; i++) {
			sum += neighborAry[i];
		}
		sum /= 9;

		return sum;
	}

	private static void loadNeighbor(int i, int j, int[][] mirrorFramedAry, int[] neighborAry) {
		int counter = 0;
		for (int k = 0; k <= 2; k++) {
			for (int m = 0; m <= 2; m++) {
				neighborAry[counter] = mirrorFramedAry[i + k - 1][j + m - 1];
				counter++;
			}
		}

	}
}
