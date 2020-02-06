 
int main (  )  { 
	int n ,  s ,  f ; 
	s  -  =  1 ; 
	f  -  =  1 ; 
	vector  < vector  < int >  >  v ( n ,  vector  < int >  { int ( 10e8 )  }  )  ; 
	v[s][0]  =  0 ; 
	for  ( int i  =  0 ;  i  <  n ;   +  + i )  { 
		for  ( int j  =  0 ;  j  <  n ;   +  + j )  { 
			int mtx ; 
			if  ( mtx  =  =  1 ) 
				v[i].push_back ( j )  ; 
		 } 
	 } 
	queue  < int >  p ; 
	int b ; 
	p.push ( s )  ; 
	while  ( !p.empty (  )  )  { 
		s  =  p.front (  )  ; 
		p.pop (  )  ; 
		for  ( int i  =  1 ;  i  <  v[s].size (  )  ;   +  + i )  { 
			b  =  v[s][i] ; 
			if  ( v[b][0] > v[s][0]  +  1 )  { 
				v[b][0]  =  v[s][0]  +  1 ; 
				p.push ( b )  ; 
			 } 
		 } 
	 } 
	if  ( v[f][0] =  = 10e8 )   { cout  <  <  0 ;  } 
	else  { cout  <  <  v[f][0] ;  } 
 } 
