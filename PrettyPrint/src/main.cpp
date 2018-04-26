#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>
using namespace std;

string generateOutFileName(string input) {
	return input.substr(0, input.length() - 4) + "_PP.txt";
}

int main(int argc, char *argv[]) {

	// step 0: inFile <-- argv[1]
	string input = argv[1];
	ifstream inFile(input);
	try {

		if (!inFile) {
			throw runtime_error("File Not found!");
		}

		// step 0: open the output file with the required name.
		string outFileName = generateOutFileName(input);
		ofstream outFile(outFileName);

		// step 1: - read the image header from input file, the four numbers.
		// numRows, numCols, minVal, maxVal <-- read from inFile
		int numRows, numCols, minVal, maxVal;
		inFile >> numRows;
		inFile >> numCols;
		inFile >> minVal;
		inFile >> maxVal;

		// write  numRows, numCols, 0, 1 to outFile
		outFile << numRows << " " << numCols << " " << 0 << " " << 1 << endl;

		//step 2 & 3: process the inFile from left to right and top to bottom
		// For easier reading use two loops: i for rows and j for column
		int pixel_val;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				inFile >> pixel_val;
				if (pixel_val > 0) {
					outFile << pixel_val << " ";
				} else {
					outFile << "  ";
				}
			}
			outFile << endl;
		}

		// step 4: close input file and output file
		inFile.close();
		outFile.close();

	} catch (exception &ex) {
		cout << ex.what() << endl;

	}

	return 0;

}
