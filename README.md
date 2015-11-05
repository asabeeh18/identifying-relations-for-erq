1.  Stage 1 

  - Linux
  - install postgresql
  - Import all files to db with 2 cols..primary key as file name   
  Filename | Whole Article
  ----------|---------------
  121424232|The whole artice as a single string
  - Create inverted index lucene on the file or db whichever is faster(easy)
   
1. Stage 2

  - Get the SPO db from freebase(or anywhere)
  - start the process of finding new raltions and dumping it with the known predicate  
  
Entity|Predicate|Object|List of Phrases
-----:|--------:|:-----:|:---------------
SRK|cast|My Name is Khan|[Acted in,Shooted for,Worked in]

Right     | Left   | Center 
---------:| :----- |:-----:
Computer  |  $1600 | one
Phone     |    $12 | three
Pipe      |     $1 | eleven