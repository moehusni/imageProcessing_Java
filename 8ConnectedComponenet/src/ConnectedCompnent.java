package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

	private class Property {
		int label, numbPixels, minRow, minCol, maxRow, maxCol;

		void setLabel(int l) {
			label = l;
		}

		void incrementPixels() {
			numbPixels++;
		}

		void checkBounds(int i, int j) {
			if (i < minRow) {
				minRow = i;
			}
			if (i > maxRow) {
				maxRow = i;
			}

			if (j < minCol) {
				minCol = j;
			}
			if (j > maxCol) {
				maxCol = j;
			}

		}

		Property() {
			numbPixels = 0;
			maxRow = -1;
			maxCol = -1;
			minRow = 2147483647;
			minCol = 2147483647;
		}

		int getMaxRows() {
			return maxRow;
		}

		int getMinCols() {
			return minCol;
		}

		int getMaxCols() {
			return maxCol;
		}

		int getMinRows() {
			return minRow;
		}

		int getNumbPixels() {
			return numbPixels;
		}
	}

	int numRows, numCols, minVal, maxVal, newMin, newMax, newLabel;
	int[][] zeroFramedAry;
	int[] neighborAry;
	int[] eqAry;
	private boolean pass2 = false;

	Scanner infile;
	PrintWriter outfile, outfile2, outfile3;
	Property[] components;

	Main(String infilename, String outfilename, String outfilename2, String outfilename3) throws IOException {
		infile = new Scanner(new File(infilename));
		outfile = new PrintWriter(new File(outfilename));
		outfile2 = new PrintWriter(new File(outfilename2));
		outfile3 = new PrintWriter(new File(outfilename3));

		int data = 0;
		for (int i = 0; i < 4; i++) {
			data = infile.nextInt();
			switch (i) {
			case 0:
				numRows = data;
				break;
			case 1:
				numCols = data;
				break;
			case 2:
				minVal = data;
				break;
			case 3:
				maxVal = data;
				break;
			}
		}
		newLabel = 0;
		zeroFramedAry = new int[numRows + 2][numCols + 2];

		eqAry = new int[(numRows * numCols) / 4];
		for (int i = 0; i < (numRows * numCols) / 4; i++) {
			eqAry[i] = i;
		}

		neighborAry = new int[9];
	}

	void zeroFrame() {
		for (int i = 0; i < numCols + 2; i++) {
			zeroFramedAry[0][i] = 0;
			zeroFramedAry[numRows + 1][i] = 0;

		}

		for (int i = 0; i < numRows + 2; i++) {
			zeroFramedAry[i][0] = 0;
			zeroFramedAry[i][numCols + 1] = 0;

		}
	}

	void loadImage() {
		int data = 0;

		for (int i = 1; i < numRows + 1; i++) {
			for (int x = 1; x < numCols + 1; x++) {
				data = infile.nextInt();
				zeroFramedAry[i][x] = data;
			}
		}
	}

	void loadNeighbors(int i, int j) {
		int leftMostJ = j - 1;
		int currentIndex = 0;
		
		if (!pass2) {
			int x = i - 1;
			for (int y = leftMostJ; y < leftMostJ + 3; y++) {
				neighborAry[currentIndex++] = zeroFramedAry[x][y];

			}
			neighborAry[currentIndex] = zeroFramedAry[i][j - 1];
		} else {
			int x = i + 1;
			neighborAry[currentIndex++] = zeroFramedAry[i][j]; // self
			neighborAry[currentIndex++] = zeroFramedAry[i][j + 1];
			for (int y = leftMostJ; y < leftMostJ + 3; y++) {
				neighborAry[currentIndex++] = zeroFramedAry[x][y];

			}

		}
	}

	void connectCC_pass1() {
		for (int i = 1; i < numRows + 1; i++) {
			for (int x = 1; x < numCols + 1; x++) {
				if (zeroFramedAry[i][x] > 0) {

					loadNeighbors(i, x);
					if (findMin() == 0) {
						newLabel++;
						zeroFramedAry[i][x] = newLabel;
					} else {
						// case 2 and 3
						int min = findMin();
						zeroFramedAry[i][x] = min;
						updateEQAry(min);
					} // else
					}
				}
			}
		}
	private void updateEQAry(int min) {
		for (int i = 0; i < 4; i++) {
			if (neighborAry[i] != 0 && neighborAry[i] > min) {
				eqAry[neighborAry[i]] = min;
			}
		}
	
	}

	private int findMin() {
		int min = 99;
		for (int i = 0; i < 5; i++) {
			if (neighborAry[i] != 0 && min > neighborAry[i])
				min = neighborAry[i];
		}
		if (min == 99) {
			return 0;
		} else
			return min;

	}


	void connectCC_pass2() {
		pass2 = true;
		for (int i = numRows; i > 0; i--) {
			for (int j = numCols; j > 0; j--)
				if (zeroFramedAry[i][j] > 0) {
					 loadNeighbors(i, j);
					if (findMin() != 0) {
						int min = findMin();
						zeroFramedAry[i][j] = min;
						updateEQAry(min);
					}
				}
		}
	
	}

	void connectCC_pass3() {
		components = new Property[newLabel + 1];
		for (int i = 1; i <= newLabel; i++) {
			components[i] = new Property();
			components[i].setLabel(i);
		}
		for (int i = 1; i < numRows + 1; i++) {
			for (int x = 1; x < numCols + 1; x++) {
				if (zeroFramedAry[i][x] > 0) {
					zeroFramedAry[i][x] = eqAry[zeroFramedAry[i][x]];
					components[zeroFramedAry[i][x]].incrementPixels();
					components[zeroFramedAry[i][x]].checkBounds(i, x);

				}
				// track newMax
				if (zeroFramedAry[i][x] > newMax)
					newMax = zeroFramedAry[i][x];
				// track newMin
				if (zeroFramedAry[i][x] < newMin)
					newMin = zeroFramedAry[i][x];
			}
		}
	}

	void manageEqAry() {
		int trueLabel = 0;
		int index = 1;
		while (index <= newLabel) {
			if (eqAry[index] == index) {
				trueLabel++;
				eqAry[index] = trueLabel;
			} else {
				eqAry[index] = eqAry[eqAry[index]];
			}

			index++;
		}

		newLabel = trueLabel;
	}

	void printCcProperty() {
		outfile3.printf("%d %d %d %d", numRows, numCols, minVal, newLabel);
		outfile3.println();
		for (int i = 1; i <= newLabel; i++) {
			outfile3.println(i);
			outfile3.println(components[i].getNumbPixels());
			outfile3.printf("%d %d", components[i].getMinRows(), components[i].getMinCols());
			outfile3.println();
			outfile3.printf("%d %d", components[i].getMaxRows(), components[i].getMaxCols());
			outfile3.println();
			outfile3.println();
		}

	}

	void prettyPrint(int pass) {
		switch (pass) {
		case 1:
			outfile.println("Pass 1:");
			break;
		case 2:
			outfile.println("Pass 2:");
			break;
		case 3:
			outfile.println("Pass 3:");
			break;
		}

		for (int i = 0; i < numRows + 2; i++) {
			for (int x = 0; x < numCols + 2; x++) {

				if (zeroFramedAry[i][x] != 0) {
					outfile.print(zeroFramedAry[i][x]);

				} else {
					outfile.print(" ");
				}

				if (zeroFramedAry[i][x] < 10) {
					outfile.print(" ");
				} else {
					outfile.print(" ");
				}
			}
			outfile.println();
		}

		print("EQAry Pass " + pass + ":");

		outfile.println();
		outfile.println();
		outfile.println();

	}

	void print(String message) {
		outfile.println(message);
		

		for (int i = 1; i <= newLabel; i++) {
			outfile.printf("%3s", i);
		}
		outfile.println();
		for (int i = 1; i <= newLabel; i++) {
			outfile.printf("%3s", eqAry[i]);
		}
		outfile.println();
	}

	void print2() {
		outfile2.printf("%d %d 0 %d ", numRows, numCols, newLabel);
		outfile2.println();
		for (int i = 1; i < numRows + 1; i++) {
			for (int x = 1; x < numCols; x++) {
				outfile2.printf("%d ", zeroFramedAry[i][x]);
			}
			outfile2.println();
		}

		infile.close();
		outfile.close();
		outfile2.close();
		outfile3.close();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main cc;
		try {
			cc = new Main(args[0], args[1], args[2], args[3]);
			cc.loadImage();
			cc.zeroFrame();
			cc.connectCC_pass1();
			cc.prettyPrint(1);
			cc.connectCC_pass2();
			cc.prettyPrint(2);
			cc.manageEqAry();
			cc.print("After managing eqAry");
			cc.connectCC_pass3();
			cc.prettyPrint(3);
			cc.printCcProperty();
			cc.print2();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
