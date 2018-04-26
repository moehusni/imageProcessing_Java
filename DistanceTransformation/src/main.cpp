#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>
using namespace std;

class ImageProcessing {
public:
	int numRows;
	int numCols;
	int minVal;
	int maxVal;
	int newMinVal;
	int newMaxVal;
	bool flag;
	int** zeroFramedAry;

	// Constructor
	ImageProcessing(int row, int col, int min, int max) {
		numRows = row;
		numCols = col;
		minVal = min;
		maxVal = max;
		newMaxVal = -1;
		newMinVal = 99;
		flag = false;

		zeroFramedAry = new int*[row + 2];

		for (int r = 0; r < row + 2; r++) {
			zeroFramedAry[r] = new int[col + 2];
		}
	}

	// zeroFramed
	void zeroFramed() {
		for (int i = 0; i < numRows + 2; i++)
			for (int j = 0; j < numCols + 2; j++) {
				zeroFramedAry[i][j] = 0;
			}
	}

	// loadImage
	void loadImage(ifstream &input) {
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				input >> zeroFramedAry[i][j];
	}

	// loadNeighbors
	int loadNeighbors(int i, int j) {
		int r = (flag) ? i + 1 : i - 1;
		int min =
				(flag) ? zeroFramedAry[i][j + 1] : min =
						zeroFramedAry[i][j - 1];
		for (int c = -1; c <= 1; c++)
			if (min > zeroFramedAry[r][j + c])
				min = zeroFramedAry[r][j + c];
		return min + 1;
	}

	// firstPassDistance
	void firstPassDistance() {
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				if (zeroFramedAry[i][j] > 0)
					zeroFramedAry[i][j] = loadNeighbors(i, j);
	}

	// secondPassDistance
	void secondPassDisstance() {
		flag = true;
		for (int i = numRows; i > 0; i--)
			for (int j = numCols; j > 0; j--)
				if (zeroFramedAry[i][j] > 0) {
					int min = loadNeighbors(i, j);
					int self = zeroFramedAry[i][j];
					zeroFramedAry[i][j] = self < min ? self : min;
					if (newMinVal > zeroFramedAry[i][j])
						newMinVal = zeroFramedAry[i][j];
					else if (newMaxVal < zeroFramedAry[i][j])
						newMaxVal = zeroFramedAry[i][j];
				}
	}

	// prettyPrint
	void prettyPrint(ofstream& output2) {
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				if (zeroFramedAry[i][j] == 0)
					output2 << "  ";
				else
					output2 << setw(2) << zeroFramedAry[i][j];
			}
			output2 << endl;
		}
	}

	// result of Pass-2
	void createImage(ofstream& output) {

		output << numRows << " " << numCols << " " << newMinVal << " "
				<< newMaxVal << endl;
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				if (j == 1)
					output << zeroFramedAry[i][j];
				else
					output << setw(2) << zeroFramedAry[i][j];
			}
			output << endl;
		}
	}
};
string generateOutFileName(string input) {
	return input.substr(0, input.length() - 4) + "_Process.txt";
}

string genrateOutFileName(string input) {
	return input.substr(0, input.length() - 4) + "_PP.txt";
}
int main(int argc, const char * argv[]) {
	ifstream inFile(argv[1]);

	string outFileProcess = generateOutFileName(argv[1]);
	string outFilePretty = genrateOutFileName(argv[1]);
	ofstream outFile1(outFileProcess);
	ofstream outFile2(outFilePretty);
	try {

		if (!inFile) {
			throw runtime_error("File Not found!");
		}
		int row, col, min, max;
		inFile >> row;
		inFile >> col;
		inFile >> min;
		inFile >> max;
		ImageProcessing ip(row, col, min, max);
		ip.zeroFramed();
		ip.loadImage(inFile);

		ip.firstPassDistance();
		outFile2 << "Pass-1:" << endl;
		ip.prettyPrint(outFile2);

		ip.secondPassDisstance();
		outFile2 << "\nPass-2:" << endl;
		ip.prettyPrint(outFile2);
		ip.createImage(outFile1);
		inFile.close();
		outFile1.close();
		outFile2.close();

	} catch (exception &ex) {
		cout << ex.what() << endl;

	}

	return 0;
}

