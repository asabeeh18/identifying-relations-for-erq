1.  Stage 1 

  a Linux
  a Install postgresql
  a Import all files to db with 2 cols..primary key as file name   
  
Filename | Whole Article
:----------|:--------------
2142423|The whole artice as a single string
		  
  a Create inverted index lucene on the file or db whichever is faster(easy)
   
1. Stage 2

  a Get the EPO db from freebase(or anywhere) __READYMADE__
  a Create the EPO db
     a EPO is the primary key

Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|null
		
  a Start the process of finding new relations and dumping it with the known predicate  
        a `Complex Long process,Leave this for vacation`
  
Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|[Acted in,Shooted for,Worked in]