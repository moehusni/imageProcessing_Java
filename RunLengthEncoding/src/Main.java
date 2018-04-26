import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		// step 0: - open input file and output files
		Scanner inFile = new Scanner(new FileReader(args[0]));
		PrintWriter outFile = new PrintWriter(args[1]);
		try {
			int r, c, min, max;
			r = inFile.nextInt();
			c = inFile.nextInt();
			min = inFile.nextInt();
			max = inFile.nextInt();

			// Conctructors
			Encode encode = new Encode(r, c, min, max);

			// Loading Image
			encode.loadImage(inFile);

			BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
			int method;

			System.out.println("MENU FOR RUN-LENGTH ENCODE");
			System.out.println("1.Encode without zero and no wrap-around");
			System.out.println("2.Encode without zero and wrap-around");
			System.out.println("3.Encode with zero and no wrap-around");
			System.out.println("4.Encode with zero and wrap-around");
			System.out.println("ENTER YOUR CHOICE:");

			method = Integer.parseInt(obj.readLine());

			// Print out image header and the method number.
			outFile.println(r + " " + c + " " + min + " " + max + "\n" + method);

			switch (method) {
			case 1:
				encode.method1(outFile);

				break;

			case 2:
				encode.method2(outFile);

				break;

			case 3:
				encode.method3(outFile);

				break;

			case 4:
				encode.method4(outFile);
				break;
			default:
				System.err.println("You must choose method number within 1-4.");

			}

		}

		catch (Exception e) {
			System.err.println(e);
		}

		inFile.close();
		outFile.close();

	}
}

class Encode {
	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;
	private int[][] p;

	public Encode(int r, int c, int min, int max) {
		numRows = r;
		numCols = c;
		minVal = min;
		maxVal = max;
		p = new int[r][c];
	}

	public void method1(PrintWriter outFile) {
		// Encode without zero and no wrap-around
		int previous = 0;
		// Step 0: r <-- 0
		for (int r = 0; r < numRows; r++) {
			// Step 1: Scan inFile left to right and top to bottom
			int c = 0, count = 0, currVal = 0; // c <-- 0, count <-- 0
			if (p[r][c] > 0) {
				currVal = p[r][c]; // currVal <-- p(r, c)
				// Step 2: output r and c and currVal to outFile
				outFile.print(r + " " + c + " " + currVal + " ");
				count++;
			}
			c++;
			while (c < numCols) {
				// Step 4: nextVal <-- p(r, c) // read next pixel
				if (previous == 0 && p[r][c] > 0) {
					if (count != 0) {
						outFile.println(count);
						outFile.print(r + " " + c + " " + p[r][c] + " ");
						currVal = p[r][c];
						count = 0;
					}
				}
				previous = p[r][c];
				if (p[r][c] > 0) {
					int nextVal = p[r][c];
					// Step 5: if nextVal == currVal
					if (nextVal == currVal) {
						count++;
					} else {
						if (count != 0)
							outFile.println(count);
						currVal = nextVal;
						count = 1;
						outFile.print(r + " " + c + " " + currVal + " ");
					}
				}
				c++; // Step3 : c++;
				if (c == numCols && count != 0) {
					outFile.println(count);
				}
			} // Step 6: repeat Step 3 to Step 5 until end of text Line
		} // Step 7 = r++ && Step 8 repeat Step 1 until the end of file
	}

	public void method2(PrintWriter outFile) {
		// Encode without zero and wrap-around

		int r = 0;
		int c = 0; // c <-- 0
		int count = 0; // count <-- 0
		// if p(r,c) == 0) loop until you find the beginning of the object. - the first
		// Non zero pixel
		if (p[r][c] == 0) {
			while (p[r][c] == 0) {
				c++;
				if (c == numCols) {
					c = 0;
					r++;
					if (r == numRows)
						System.exit(0);
				} // if
			} // while
		} // if
		int currVal = p[r][c]; // currVal <-- p(r, c)
		// Step 2: output r and c and currVal to outFile
		outFile.print(r + " " + c + " " + currVal + " ");
		c--;
		int previous = 0;
		int nextVal = -1;
		while (r < numRows) {
			while (c < numCols) {
				if (previous == 0 && p[r][c] > 0) {
					if (count != 0) {
						outFile.println(count);
						outFile.print(r + " " + c + " " + p[r][c] + " ");
						currVal = p[r][c];
						count = 0;
					}
				}
				previous = p[r][c];
				nextVal = p[r][c];
				if (nextVal == currVal) { // check if p[r][c] == p[r][c+1]
					count++;
				} else { // else p[r][c+1] deffers from previous

					if (nextVal != 0) {
						outFile.println(count);
						currVal = nextVal;
						count = 1;
						outFile.print(r + " " + c + " " + currVal + " ");
					}

				} // else
				c++;
			} // while c< numCols
			c = 0;
			r++;
		} // while r < numCols

		outFile.println(count); // print the final counted pixel.

	} // method2

	public void method3(PrintWriter outFile) {
		// Encode with zero and no wrap-around

		// Step 0: r <-- 0
		for (int r = 0; r < numRows; r++) {
			// Step 1: Scan inFile left to right and top to bottom
			int c = 0; // c <-- 0
			int count = 0; // count <-- 0
			int currVal = p[r][c]; // currVal <-- p(r, c)
			// Step 2: output r and c and currVal to outFile
			outFile.print(r + " " + c + " " + currVal + " ");
			count++;
			c++;
			while (c < numCols) {
				// Step 4: nextVal <-- p(r, c) // read next pixel
				int nextVal = p[r][c];
				// Step 5: if nextVal == currVal
				if (nextVal == currVal) {
					count++;
				} else {
					outFile.println(count);
					currVal = nextVal;
					count = 1;
					outFile.print(r + " " + c + " " + currVal + " ");

				}
				c++; // Step3 : c++;
				if (c == numCols) {
					outFile.println(count);
				}
			} // Step 6: repeat Step 3 to Step 5 until end of text Line

		} // Step 7 = r++ && Step 8 repeat Step 1 until the end of file
	}

	public void method4(PrintWriter outFile) {
		// Encode with zero and wrap-around
		int count = 0;
		int currVal = -1;
		int nextVal = -1;
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				nextVal = p[r][c];

				if (currVal == -1) { // if it the very first pixel.
					currVal = p[r][c];
					count++;
					outFile.print(r + " " + c + " " + currVal + " ");
					c++; // incremant the column number
					nextVal = p[r][c]; // nextVal <-- p[r][c+1]
				} // if
				if (nextVal == currVal) { // check if p[r][c] == p[r][c+1]
					count++;
				} else { // else p[r][c+1] deffers from previous
					outFile.println(count);
					currVal = nextVal;
					count = 1;
					outFile.print(r + " " + c + " " + currVal + " ");

				} // else
			} // for
		} // for

		outFile.println(count); // print the final counted pixel.
	}

	public void loadImage(Scanner input) {
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				p[i][j] = input.nextInt();
	}

}