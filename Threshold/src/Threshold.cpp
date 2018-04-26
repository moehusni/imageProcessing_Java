#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>
using namespace std;

string generateOutFileName(string input, int thrValue) {
	string thr = to_string(thrValue);

	return input.substr(0, input.length() - 4) + "_thr_" + thr + ".txt";
}

int main(int argc, char *argv[]) {

	// step 0: inFile <-- argv[1]
	string input = argv[1];
	ifstream inFile(input);
	try {
		if (!inFile) {
			throw runtime_error("File Not found!");
		}

		// step 1: numRows, numCols, minVal, maxVal <-- read from inFile
		int numRows, numCols, minVal, maxVal, thrValue;
		inFile >> numRows;
		inFile >> numCols;
		inFile >> minVal;
		inFile >> maxVal;

		// step 2: thrValue <-- ask user from console
		cout << "What's the Threshold Value? \n";
		cin >> thrValue;

		// threshold value must but within bound
		if (thrValue < minVal || thrValue > maxVal) {
			cout << "The threshold value must be more than " << minVal
					<< " and less than " << maxVal << endl;
			cin >> thrValue;
		}

		// step 3: outFileName <-- generateOutFileName (thrValue)
		string outFileName = generateOutFileName(input, thrValue);

		// step 4: outFile <-- open the output file, named outFileName
		ofstream outFile(outFileName);

		// step 5: write  numRows, numCols, 0, 1 to outFile
		outFile << numRows << " " << numCols << " " << 0 << " " << 1 << endl;

		//step 6: process the inFile from left to right and top to bottom
		// For easier reading use two loops: i for rows and j for column
		int pixel_val;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				inFile >> pixel_val;
				if (pixel_val >= thrValue) {
					outFile << 1 << " ";
				} else {
					outFile << 0 << " ";
				}
			}
			outFile << endl;
		}

		// step 8: close input file and output file
		inFile.close();
		outFile.close();

	} catch (exception &ex) {
		cout << ex.what() << endl;

	}
	return 0;

}
