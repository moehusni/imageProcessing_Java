#include <iostream>
#include <string>
#include <fstream>

using namespace std;

class ThinningSkeleton {
private:
	int numRows, numCols, minVal, maxVal;

public:
	bool changeflag;
	int cycleCount;
	int** firstAry;
	int** secondAry;
	ThinningSkeleton(int numRows, int numCols, int minVal, int maxVal) {
		this->numRows = numRows;
		this->numCols = numCols;
		this->minVal = minVal;
		this->maxVal = maxVal;
		this->cycleCount = 0;
		this->changeflag = true;
		firstAry  = new int*[numRows + 2];
		secondAry = new int*[numRows + 2];
		for (int row = 0; row < this->numRows + 2; row++) {
			firstAry[row]  = new int[numCols + 2];
			secondAry[row] = new int[numCols + 2];
		}
	}
	void zeroFramed(int ** ary) {

		for (int row = 0; row < numRows + 2; row++) {
			for (int col = 0; col < numCols + 2; col++) {
				ary[row][col] = 0;
			}
		}
	}
	void loadImage(ifstream &input) {
		for (int row = 1; row <= numRows; row++) {
			for (int col = 1; col <= numCols; col++) {
				input >> secondAry[row][col];
			}
		}
		copyAry();
	}
	void copyAry() {
		for (int row = 1; row <= numRows; row++) {
			for (int col = 1; col <= numCols; col++) {
				this->firstAry[row][col] = this->secondAry[row][col];
			}
		}
	}

	void DoThinning(int row, int col) {
		int count = -1;
		int NAry = 0;
		int neighbor[9];
		// loop through p(row,col) neighbors
		for (int k = 0; k <= 2; k++) {
			for (int j = 0; j <= 2; j++) {
				neighbor[NAry] = firstAry[row + k - 1][col + j - 1];
				// count how many neighbors are > 0
				if (neighbor[NAry] > 0) {
					count++;
				}
				NAry++;
			}
		}
		// condition 1: if there are 4 or more of firstAry(i,j)'s 8 neighbors > 0
		// condition 2: if flipping firstAry(i,j) from 1 to 0, it would NOT create two
		// or more connected components in firstAry(i,j)'s 3X3
		if (count >= 4 && isConnected(neighbor)) {
			secondAry[row][col] = 0;
			this->changeflag = true;

		} else {
			this->secondAry[row][col] = 1;
		}

	}
	bool isConnected(int ary[]) {
		// [0][N][2]
		// [W][X][E]
		// [6][S][8]
		int north_south = ary[1] + ary[7];
		int west_east   = ary[3] + ary[5];
		int north_west  = ary[1] + ary[3];
		int north_east  = ary[1] + ary[5];
		int south_west  = ary[7] + ary[3];
		int south_east  = ary[7] + ary[5];

		if (north_south == 0 || west_east == 0) {
			return false;

		} else if ((north_west == 0 && ary[0]==1) ||(north_east == 0 && ary[2]==1)) {
			return false;

		} else if ((south_west == 0 && ary[6]==1) ||  (south_east == 0 && ary[8]==1)) {
			return false;

		} else {
			return true;
		}

	}
	void NorthThinning() {
		bool first =true;
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				if(first && firstAry[row][col] == 1){
									first = false;
									continue;
								}
				if (firstAry[row][col] == 1 && firstAry[row - 1][col] == 0) {
					this->DoThinning(row, col);

				}
			}
		}
	}
	void SouthThinning() {
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				if (firstAry[row][col] == 1 && firstAry[row + 1][col] == 0) {
					this->DoThinning(row, col);

				}
			}
		}

	}
	void WestThinning() {
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				if (firstAry[row][col] == 1 && firstAry[row][col - 1] == 0) {
					this->DoThinning(row, col);

				}
			}
		}
	}
	void EastThinning() {
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				if (firstAry[row][col] == 1 && firstAry[row][col + 1] == 0) {
					this->DoThinning(row, col);

				}
			}
		}
	}
	void prettyPrint(ofstream &output) {
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				if (firstAry[row][col] == 0) {
					output << "  ";
				} else {
					output << firstAry[row][col]<< " ";
				}

			}
			output << endl;
		}

	}
	void print(ofstream &output) {
		for (int row = 1; row < numRows + 1; row++) {
			for (int col = 1; col < numCols + 1; col++) {
				output << firstAry[row][col] << " ";
			}
			output << endl;
		}
	}
	void deleteAry(){
		for (int i = 0; i < numRows + 2; i++) {
					delete[] firstAry[i];
					delete[] this->secondAry[i];
				}
				delete[] this->firstAry;
				delete[] this->secondAry;

	}

};
// ThinnningSkeleton Class
int main(int argc, char *argv[]) {
	int numRows, numCols, minVal, maxVal;

	ifstream inFile(argv[1]);
	ofstream skeleton(argv[2]);
	ofstream prettyprint(argv[3]);

	inFile >> numRows;
	inFile >> numCols;
	inFile >> minVal;
	inFile >> maxVal;
	ThinningSkeleton thinSkel(numRows, numCols, minVal, maxVal);
	thinSkel.zeroFramed(thinSkel.firstAry);
	thinSkel.zeroFramed(thinSkel.secondAry);
	thinSkel.loadImage(inFile);
	while (thinSkel.changeflag) {
		thinSkel.changeflag = false;
		thinSkel.cycleCount++;
		thinSkel.NorthThinning();
		thinSkel.copyAry();
		thinSkel.WestThinning();
		thinSkel.copyAry();
		thinSkel.SouthThinning();
		thinSkel.copyAry();
		thinSkel.EastThinning();
		thinSkel.copyAry();

		if (thinSkel.cycleCount % 2 == 0) {
			prettyprint << "Thinning  in " << thinSkel.cycleCount
					<< " Cycle. \n";
			thinSkel.prettyPrint(prettyprint);
		}
		if (thinSkel.cycleCount % 100 == 0)
			break;
	}
	prettyprint << "Skeleton Image: \n";
	thinSkel.prettyPrint(prettyprint);
	skeleton << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
	thinSkel.print(skeleton);
	thinSkel.deleteAry();


inFile.close();
skeleton.close();
prettyprint.close();

return 0;
}
