import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
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
			DeCode deCode = new DeCode(r, c, min, max);
			int method;
			method = inFile.nextInt();
			// Print out image header and the method number.
			outFile.println(r + " " + c + " " + min + " " + max);
			switch (method) {
			case 1:
				deCode.deCodeMethod1(inFile, outFile);
				break;
			case 2:
				deCode.deCodeMethod2(inFile, outFile);
				break;
			case 3:
				deCode.deCodeMethod3(inFile, outFile);
				break;
			case 4:
				deCode.deCodeMethod4(inFile, outFile);
				break;
			default:
				System.err.println("You must choose method number within 1-4.");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		inFile.close();
		outFile.close();
	}
}

class DeCode {
	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;
	private int[][] p;

	public DeCode(int r, int c, int min, int max) {
		numRows = r;
		numCols = c;
		minVal = min;
		maxVal = max;
		p = new int[r][c];
	}

	class Pixel {
		public Pixel(int r, int c, int val, int count) {
			// TODO Auto-generated constructor stub
			sRow = r;
			sCol = c;
			pVal = val;
			this.count = count;
		}

		int sRow;
		int sCol;
		int pVal;
		int count;
	}

	public void deCodeMethod1(Scanner inFile, PrintWriter outFile) {
		 // DenCode without zero and no wrap-around
		LinkedList<Pixel>[] vertex = loadList(inFile);
		
		for (int r = 0; r < numRows; r++) {
			int c = 0;
			while (!vertex[r].isEmpty()) {

				Pixel temp = vertex[r].removeFirst();
				while (c < temp.sCol) {
					outFile.print(0 + " ");
					c++;
					if(c == 20) {
						System.exit(0);
					}
				}
				while (temp.count > 0) {
					temp.count--;
					outFile.print(temp.pVal + " ");
					c++;
					
				}
			}
			while (c < numCols) {
				outFile.print(0 + " ");
				c++;
			}
			if(c==numCols) {
				outFile.println("");
			}
			c = 0;

		}
	

	}

	private LinkedList<Pixel>[] loadList(Scanner inFile) {
		LinkedList<Pixel>[] vertex = new LinkedList[numRows];
		for (int i = 0; i < numRows; i++) {
			vertex[i] = new LinkedList<Pixel>();

		}
		while (inFile.hasNext()) {
			int r = inFile.nextInt();
			int c = inFile.nextInt();
			int val = inFile.nextInt();
			int count = inFile.nextInt();
			Pixel temp = new Pixel(r, c, val, count);
			if (vertex[r] == null) {
				vertex[r] = new LinkedList<Pixel>();
			}

			vertex[r].add(temp);
		}
		return vertex;
	}

	public void deCodeMethod2(Scanner inFile, PrintWriter outFile) {
		// DeCode without zero and wrap-around
		int[][] img = new int[numRows][];
		for (int i = 0 ; i< numRows ; i++) {
		img[i] = new int[numCols];
		}
		
		while(inFile.hasNext()) {
			// reading the lines
			int row = inFile.nextInt();
			int col = inFile.nextInt();
			int val = inFile.nextInt();
			int count = inFile.nextInt();
			while(count > 0) {
				img[row][col] = val;
				count--;
				col++;
				if(col == numCols) {
					col= 0;
					row++;
					
				}

			}
			
		}
		for(int r = 0; r<numRows; r++) {
			for(int c = 0; c< numCols; c++) {
				outFile.print(img[r][c] + " ");
				
			}
			outFile.println();
		}
		
	}

	public void deCodeMethod3(Scanner inFile, PrintWriter outFile) {
		// DeCode with zero and no wrap-around
		int newLine = 0;
		while (inFile.hasNext()) {
			// skip 2 Integers
			inFile.nextInt();
			inFile.nextInt();
			int pixel = inFile.nextInt();
			int count = inFile.nextInt();
			while (count > 0) {
				count--;
				outFile.print(pixel + " ");
				newLine++;

				if (newLine == numCols) {
					outFile.println();
					newLine = 0;
				}
			}
		}
	}

	public void deCodeMethod4(Scanner inFile, PrintWriter outFile) {
		// DeCode with zero and wrap-around

		int newLine = 0;
		while (inFile.hasNext()) {
			// skip 2 Integers
			inFile.nextInt();
			inFile.nextInt();
			int pixel = inFile.nextInt();
			int count = inFile.nextInt();
			while (count > 0) {
				count--;
				outFile.print(pixel + " ");
				newLine++;

				if (newLine == numCols) {
					outFile.println();
					newLine = 0;
				}
			}
		}
	}
}
