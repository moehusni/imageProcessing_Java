import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Mom
 *
 */
public class ConnectedCompnent {

	class Property {
		int label;
		int numbpixels;
		int minRow, minCol, maxRow, maxCol;

		public Property(int label) {
			this.label = label;
			minRow = numRows;
			minCol = numCols;
		}
	}

	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;
	private int newMin;
	private int newMax;
	private int newLabel = 0;
	private int[][] zeroFramedAry;
	private int[] NeighborAry = new int[5];
	private int[] EQAry;
	private boolean pass2 = false;
	private Property[] property;

	public ConnectedCompnent(int row, int col, int min, int max) {

		numRows = row;
		numCols = col;
		minVal = min;
		maxVal = max;
		newMax = -1;
		newMin = 99;
		pass2 = false;

		// contruct EQAry
		setEQAry();
		// Construct zeroFramedAry
		zeroFramedAry();
	}

	public void connectCC_Pass1() {
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				if (zeroFramedAry[i][j] > 0) {
					// case 1 : all zero
					loadneighbor(i, j);
					if (findMin() == 0) {
						newLabel++;
						zeroFramedAry[i][j] = newLabel;
					} else {
						// case 2 and 3
						int min = findMin();
						zeroFramedAry[i][j] = min;
						updateEQAry(min);
					} // else
				}
	}

	private void loadneighbor(int row, int col) {
		if(!pass2){
			NeighborAry[0] = zeroFramedAry[row-1][col-1];
			NeighborAry[1] = zeroFramedAry[row-1][col];
			NeighborAry[2] = zeroFramedAry[row-1][col+1];
			NeighborAry[3] = zeroFramedAry[row][col-1];
		}else{
			NeighborAry[0] = zeroFramedAry[row][col];
			NeighborAry[1] = zeroFramedAry[row][col+1];
			NeighborAry[2] = zeroFramedAry[row+1][col-1];
			NeighborAry[3] = zeroFramedAry[row+1][col];
			NeighborAry[4] = zeroFramedAry[row+1][col+1];
		}
	}
	

	public void connectCC_Pass2() {
		pass2 = true;
		for (int i = numRows; i > 0; i--) {
			for (int j = numCols; j > 0; j--)
				if (zeroFramedAry[i][j] > 0) {
					this.loadneighbor(i, j);
					if (findMin() != 0) {
						int min = findMin();
						zeroFramedAry[i][j] = min;
						updateEQAry(min);
					}
				}
		}
	}

	public void connectCC_Pass3() {
		
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {

				zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];
				// track newMax
				if (zeroFramedAry[i][j] > newMax)
					newMax = zeroFramedAry[i][j];
				// track newMin
				if (zeroFramedAry[i][j] < newMin)
					newMin = zeroFramedAry[i][j];
			}
		}
	}

	public int findMin() {

		int min = 99;
		for (int i = 0; i < 5; i++) {
			if (NeighborAry[i] != 0 && min > NeighborAry[i])
				min = NeighborAry[i];
		}
		if (min == 99) {
			return 0;
		} else
			return min;

	}

	public void loadImage(Scanner input) {
		// TODO Auto-generated method stus
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				zeroFramedAry[i][j] = input.nextInt();
	}

	public void manageAry() {
		int trueLabel = 0;
		int index = 1;
		while (index <= newLabel) {
			if (EQAry[index] == index) {
				trueLabel++;
				EQAry[index] = trueLabel;
			} else
				EQAry[index] = EQAry[EQAry[index]];
			index++;
		}
		property = new Property[trueLabel];
	}

	public void prettyPrint(PrintWriter output, String string) {

		output.println(string);
		int pixel_val;
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++) {
				pixel_val = zeroFramedAry[i][j];
				if (pixel_val > 0) {
					output.printf("%3s", pixel_val);
				} else {
					output.printf("%3s", " ");
				} // else
			} // j for.
			output.println();
		} // i for
	}

	public void printEQAry(PrintWriter output, String string) {
		output.println(string);
		for (int i = 1; i <= newLabel; i++) {
			output.printf("%3s", i);
		}
		output.println();
		for (int i = 1; i <= newLabel; i++) {
			output.printf("%3s", EQAry[i]);
		}
		output.println();
	}

	public void printProperty(PrintWriter output) {
		output.println(numRows + " " + numCols + " " + newMin + " " + newMax);
		output.println(property.length);
		for (int l = 0; l < property.length; l++) {
			property[l] = new Property(l + 1);
			for (int i = 1; i < numRows + 1; i++)
				for (int j = 1; j < numCols + 1; j++)
					if (zeroFramedAry[i][j] == property[l].label) {
						property[l].numbpixels++;
						if (property[l].minRow > i)
							property[l].minRow = i;
						if (property[l].minCol > j)
							property[l].minCol = j;
						if (property[l].maxRow < i)
							property[l].maxRow = i;
						if (property[l].maxCol < j)
							property[l].maxCol = j;
					}
		}
		for (int i = 0; i < property.length; i++) {
			output.println(property[i].label);
			output.println(property[i].numbpixels);
			output.println(property[i].minRow + " " + property[i].minCol);
			output.println(property[i].maxRow + " " + property[i].maxCol);
		}

	}

	public void printResult(PrintWriter output, String string) {
		output.println(numRows + " " + numCols + " " + newMin + " " + newMax);
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				output.printf("%3s",zeroFramedAry[i][j] );
			
			}
			output.println();
		}
	}

	public void setEQAry() {
		int size = ((numCols * numRows) / 4) + 1;
		EQAry = new int[size];
		for (int i = 0; i < size; i++)
			EQAry[i] = i;

	}

	public void updateEQAry(int min) {
		for (int i = 0; i < 4; i++) {
			if (NeighborAry[i] != 0 && NeighborAry[i] > min) {
				EQAry[NeighborAry[i]] = min;
			}
		}
	}

	public void zeroFramedAry() {
		zeroFramedAry = new int[numRows + 2][numCols + 2];

		for (int i = 0; i < numRows + 2; i++)
			for (int j = 0; j < numCols + 2; j++)
				zeroFramedAry[i][j] = 0;
	}
}
