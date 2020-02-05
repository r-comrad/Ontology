int main (  ) 
 { 
	int n ,  m ,  k ,  l ; 
	vector  < int >  a ( n )  ; 
	for  ( int i  =  0 ;  i  <  n ;  i +  +  ) 
	 { cin  >  >  a[i] ;  } 
	int i  =  0 ; 
	int o  =  0 ; 
	while  ( i  <  n  -  1 ) 
	 { 
		l  =  min ( m ,  a[i] )  ; 
		i  +  =  l ; 
		o +  +  ; 
		if  ( o % k  =  =  0 ) 
			m -  -  ; 
		if  ( i  >  =  n ) 
		 { 
			cout  <  <  " - 2" ; 
			return 0 ; 
		 } 
		if  ( l  =  =  0 ) 
		 { 
			cout  <  <  " - 1" ; 
			return 0 ; 
		 } 
	 } 
	cout  <  <  o ; 
	return 0 ; 
 } 