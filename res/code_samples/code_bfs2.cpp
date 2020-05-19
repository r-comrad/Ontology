#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <queue>
 
using namespace std;
int main()
{
	FILES(a, bfs);
 
	int n, s, f;
 
	cin >> n >> s >> f;
 
	vector <vector <int>> x(n + 1);
 
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			int a;
			cin >> a;
			if (a)
			{x[i].push_back(j);}
		}
	}
 
 
	vector <int> h (n, 2e9);
	queue <int> q;
	q.push(s - 1);
	h[s - 1] = 0;
 
	while (!q.empty())
	{
		int cur = q.front();
		q.pop();
 
		for (int i = 0; i < x[cur].size(); i++)
		{
			int next = x[cur][i];
			if (h[cur] + 1 < h[next])
			{
				h[next] = h[cur] + 1;
				q.push(next);
			}
		}
	}
 
	if (h[f - 1] == 2e9)
	{cout << 0;}
	else
	{cout << h[f - 1];}
 
	return 0;
}
