#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
using namespace std;
int main() {
	string s;
	char c;
	cin >> c;
	getline(cin, s);
	getline(cin, s);
	if (s.size() == 2) {
		s.push_back('/');
	}
	if (c == 'W') {
		char s1 = s[0] - ('A' - 'a');
		s[0] = '/';
		s[1] = s1;
		for (int i = 0; i < s.size(); ++i) {
			if (s[i] == '\\') {
				s[i] = '/';
			}
		}
		if (s[s.size() - 1] == '/') {
			s.pop_back();
		}
		cout << s;
	}
	else {
		if (s[0] == '/' && s[2] == '/' && s[1] >= 'a' && s[1] <= 'z') {
			char s1 = s[1] + ('A' - 'a');
			s[0] = s1;
			s[1] = ':';
			for (int i = 0; i < s.size(); ++i) {
				if (s[i] == '/') {
					s[i] = '\\';
				}
			}
			if (s[s.size() - 1] == '\\' && s.size()>3) {
				s.pop_back();
			}
			cout << s;
		}
		else {
			cout << -1;
		}
	}
	return 0;
}