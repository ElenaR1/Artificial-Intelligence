

#include "stdafx.h"
#include <iostream>
#include <list>
#include <map>
#include <algorithm>
#include <ostream>
#include <assert.h>
#include <cassert>
#include <set>
#include <stack>
#include <string>
#include <queue>

using namespace std;
#define ROW 7
#define COL 7
int N;

struct Point
{
	int x;
	int y;
};


struct queueNode
{
	Point pt;  // The cordinates of a cell
	int dist;  // cell's distance of from the source
};


bool isValid(int row, int col)
{
	return (row >= 0) && (row < ROW) &&
		(col >= 0) && (col < COL);
}

int rowNum[] = { -1, 0, 0, 1 };
int colNum[] = { 0, -1, 1, 0 };



template<typename T>
void findPath(int x, int y, T m[ROW][COL], int dist)
{
	m[x][y] = 3;

	//look for the predecesor of the end cell. Its distance=dist-1.
	for (size_t i = 0; i < v.size(); i++)
	{
		if (v.at(i).dist == dist - 1 && notDiagonal(x, y, v.at(i).pt.x, v.at(i).pt.y))
		{
			//cout << v.at(i).pt.x << " " << v.at(i).pt.y << endl;
			findPath(v.at(i).pt.x, v.at(i).pt.y, m, dist -= 1);
		}
	}

}

bool notDiagonal(int x1, int y1, int x2, int y2)
{
	if (x2 != x1 && y2 != y1)//ne moje da se izmestvat i po dvete osi. Moje samo po ednata
		return false;
	else if ((x2 == x1 || x2 == x1 + 1 || x2 == x1 - 1) && (y2 == y1 || y2 == y1 + 1 || y2 == y1 - 1))
		return true;
	else
		return false;
}

vector<queueNode> v;

int makeInt(char c)
{
	int n = c - '0';
	return n;
}
bool canPass(char a)
{
	if (a == '1')
		return true;
	else
		return false;
}
template<typename T>
void findPath2(int x, int y, T m[ROW][COL], int dist)
{
	m[x][y] = '*';

	//look for the predecesor of end. Its distance=dist-1.
	for (size_t i = 0; i < v.size(); i++)
	{
		if (v.at(i).dist == dist - 1 && notDiagonal(x, y, v.at(i).pt.x, v.at(i).pt.y))
		{
			//cout << v.at(i).pt.x << " " << v.at(i).pt.y << endl;
			findPath(v.at(i).pt.x, v.at(i).pt.y, m, dist -= 1);
		}
	}

}


void BFS2(char mat[ROW][COL], Point start, Point end)
{
	if (!canPass(mat[start.x][start.y]) || !canPass(mat[end.x][end.y]))
	{
		cout << "wrong coordinates of the points " << endl;
	}
	bool visited[ROW][COL];
	memset(visited, false, sizeof visited);//makes every elem of visited false

	visited[start.x][start.y] = true;
	queue<queueNode> q;

	queueNode s = { start, 0 };// distance of the start cell is 0
	q.push(s);
	v.push_back(s);

	int reached = 0;
	// Do a BFS starting from start cell
	while (!q.empty())
	{
		queueNode curr = q.front();
		Point pt = curr.pt;
		//if we reach the end cell  we want to find the path to it
		if (pt.x == end.x && pt.y == end.y)
		{
			findPath2(pt.x, pt.y, mat, curr.dist);
			reached++;
		}

		q.pop();

		for (int i = 0; i < 4; i++)
		{
			int row = pt.x + rowNum[i];
			int col = pt.y + colNum[i];

			if (isValid(row, col) && canPass(mat[row][col]) &&
				!visited[row][col])
			{
				visited[row][col] = true;// mark the cell as visited and add it to the queue
				queueNode Adjcell = { { row, col },
					curr.dist + 1 };
				q.push(Adjcell);
				v.push_back(Adjcell);
			}
		}
	}
	if (reached == 0)
	{
		cout << "the destinationn cannot be reached" << endl;
	}
}



template<typename T>
void display(T m[ROW][COL])
{

	for (size_t i = 0; i < ROW; i++)
	{
		for (size_t j = 0; j < COL; j++)
		{
			cout << m[i][j] << " ";
		}
		cout << endl;
	}
}
void printVector(vector <queueNode> v)
{
	//cout << v.size();
	for (size_t i = 0; i < v.size(); i++)
	{
		cout << v.at(i).pt.x << "," << v.at(i).pt.y << " with distance: " << v.at(i).dist << endl;
	}
}

int main()
{
	


	char m3[ROW][COL] = { { '1','1','0','1','1','1','1' },
	{ '1','0','0','1','1','1','1' },
	{ '1','1','1','1','1','1','1' },
	{ '1','1','1','0','1','1','1' },
	{ '1','1','1','0','1','1','1' },
	{ '1','1','0','0','1','1','1' },
	{ '1','1','1','1','1','1','1' } };
	Point start = { 0, 0 };
	Point end = { 5, 4 };

	cout << "The maze before finding the path: " << endl;
	display(m3);
	BFS2(m3, start, end);

	cout << "The maze after finding the path: " << endl;
	display(m3);
	cout << "---------------------------------------" << endl;
	


	char m4[ROW][COL] = { { '1','1','0','1','1','1','1' },
	{ '1','0','0','1','1','1','1' },
	{ '1','1','1','1','1','1','1' },
	{ '1','1','1','0','0','0','1' },
	{ '1','1','1','0','1','0','1' },
	{ '1','1','0','0','0','1','1' },
	{ '1','1','1','1','1','1','1' } };
	Point start2 = { 0, 0 };
	Point end2 = { 4, 4 };

	cout << "The maze before finding the path: " << endl;
	display(m4);
	BFS2(m4, start2, end2);

	cout << "The maze after finding the path: " << endl;
	display(m4);
	cout << "---------------------------------------" << endl;



	return 0;
}

