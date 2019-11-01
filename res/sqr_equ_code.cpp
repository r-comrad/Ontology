#include <iostream>
#include <math.h>
 
using namespace std;
 
int main ( ) 
 { 
	double a, b, c, D, x1, x2 ;
	cin >> a >> b >> c ;
	D = ( b * b ) - ( 4 * a * c ) ;
	if ( D == 0 )
	{
		x1 = (-b + sqrt(D)) / ( 2 * a ) ;
		cout << "one solution" << endl ;
		cout << fixed << x1 << endl ;
	}
	else if  ( D < 0 ) 
	 { 
		cout << "no solutions" << endl ;
	 } 
	else
	 { 
		x1 = ( -b + sqrt ( D ) ) / ( 2 * a ) ;
		x2 = ( -b - sqrt ( D ) ) / ( 2 * a ) ;
		cout << "two solutions" << endl ;
		cout << fixed << x1 << endl << x2 << endl ;
	 } 
	return 0 ;
 } 