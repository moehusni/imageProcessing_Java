	#include <fstream>
	#include <iostream>
	#include <stdexcept>

	using namespace std;

	// function declaration:
	void loadNeighbor(int i, int j, int **mirrorFramedAry, int neighborAry[]);

	int sortNeihborAry(int neighborAry[]);

	int main(int argc, char *argv[]) {

		// step 0: inFile <-- argv[1]
		ifstream inFile(argv[1]);
		// step 0: outFile <-- argv[2]
		ofstream outFile(argv[2]);
		try {

			if (!inFile || !outFile.is_open()) { //checks if input file is in working dirctory
				throw runtime_error("File Not found!");
			}

			// step 0: open the output file with the required name.
			int numRows, numCols, minVal, maxVal;
			int newMin = 9999;
			int newMax = -1;

			int neighborAry[9];
			inFile >> numRows;
			inFile >> numCols;
			inFile >> minVal;
			inFile >> maxVal;

			// dynamic allocation for mirrorFramedAry and tempAry
			int** mirrorFramedAry = new int*[numRows + 2];
			int** tempAry = new int*[numRows + 2];

			// creating dynamic allocated
			for (int row = 0; row < numRows + 2; row++) {
				mirrorFramedAry[row] = new int[numCols + 2];
				tempAry[row] = new int[numCols + 2];

			}
			// step 1: read the input file and load onto mirrorFramedAry
			// begin at [i,j] == [1,1] to [i,j] == [numRows][numCols]
			for (int i = 1; i <= numRows; i++) {
				for (int j = 1; j <= numCols; j++) {
					inFile >> mirrorFramedAry[i][j];
				}
			}

			// step 2: mirrorFramed the mirrorFreamedAry.
			for (int row = 1; row <= numRows; row++) { //framing Rows starting from row = 1 to row = numRows
				mirrorFramedAry[row][0] = mirrorFramedAry[row][1]; // p(row,0) <-- p(row,1)
				mirrorFramedAry[row][numCols + 1] = mirrorFramedAry[row][numCols]; //
			}
			for (int col = 0; col <= numCols + 1; col++) { // framing cols starting from col = 0 to col, the last column in mirrorFramedAry is numCols + 1
				mirrorFramedAry[0][col] = mirrorFramedAry[1][col]; //  p(0,col) <-- p(1,col)
				mirrorFramedAry[numRows + 1][col] = mirrorFramedAry[numRows][col]; // the last row in mirrorFramedAry is == numRows + 1

			}

			// step 3: process the MirrorframedAry, from left to right and top to bottom begin at (1,1)
			// step 4: repeat step 3 until all pixels are processed
			int pixel_val;
			for (int i = 1; i <= numRows; i++) {
				for (int j = 1; j <= numCols; j++) {

					loadNeighbor(i, j, mirrorFramedAry, neighborAry);
					pixel_val = sortNeihborAry(neighborAry);
					tempAry[i][j] = pixel_val;

					if (pixel_val > newMax) // tracking newMax
						newMax = pixel_val;
					if (pixel_val < newMin) // tracking newMin
						newMin = pixel_val;
				}
			}
			// step 5: write  numRows, numCols, newMin, newMax to outFile
			outFile << numRows << " " << numCols << " " << newMin << " " << newMax
					<< endl;

			// step 6: output the tempAry, begin at [1,1], to outfile
			for (int i = 1; i <= numRows; i++) {
				for (int j = 1; j <= numCols; j++) {
					outFile << tempAry[i][j] << " ";
				}
				outFile << endl;
			}

			// step 7: close input file and output file
			inFile.close();
			outFile.close();

			// step 8: de-allocate mirroframedAry and tempAry
			for (int i = 0; i < numRows + 2; i++) {
				delete[] mirrorFramedAry[i];
				delete[] tempAry[i];
			}
			delete[] mirrorFramedAry;
			delete[] tempAry;

		} catch (exception &ex) {
			cout << ex.what() << endl;

		}

		return 0;

	}

	void loadNeighbor(int i, int j, int **mirrorFramedAry, int neighborAry[]) {
		int counter = 0;
		for (int k = 0; k <= 2; k++) {
			for (int m = 0; m <= 2; m++) {
				neighborAry[counter] = mirrorFramedAry[i + k - 1][j + m - 1];
				counter++;
			}
		}

	}
	void swap(int *xp, int *yp) {
		int temp = *xp;
		*xp = *yp;
		*yp = temp;
	}
	int sortNeihborAry(int arr[]) { // insertion sort
		int i, j, min_idx;

		// One by one move boundary of unsorted subarray
		for (i = 0; i < 8; i++) {
			// Find the minimum element in unsorted array
			min_idx = i;
			for (j = i + 1; j < 9; j++)
				if (arr[j] < arr[min_idx])
					min_idx = j;

			// Swap the found minimum element with the first element
			swap(&arr[min_idx], &arr[i]);
		}

		return arr[4];
	}

