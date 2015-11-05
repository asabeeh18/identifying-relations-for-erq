1.  Stage 1 

  - Linux
  - install postgresql
  - Import all files to db with 2 cols..primary key as file name   
  
Filename | Whole Article
:----------|:--------------
2142423|The whole artice as a single string
		  
  - Create inverted index lucene on the file or db whichever is faster(easy)
   
1. Stage 2

  - Get the EPO db from freebase(or anywhere) __READYMADE__
  - Create the EPO db
     - EPO is the primary key

Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|null
		
  - Start the process of finding new relations and dumping it with the known predicate  
        - `Complex Long process,Leave this for vacation`
  
Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|[Acted in,Shooted for,Worked in]