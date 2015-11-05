#TODO LIST

####Dhruv

1. Install Linux Ubuntu based (preferably Lubuntu)  
1. Install and read about PostgreSQL  
1. Unzip the Wikipedia file. _LOTS OF TIME_
1. Code and put all the Wikipedia files on the database, call it WikiDB . _LOTS OF TIME_

####Tembe

1. Read about [Lucene](https://lucene.apache.org/core/) index and write the code to create inverted index on our files/db call it inverted index for now :/ .       
2. Get the EPO Databse.

#PROCEDURE      


####1. Stage 1
  * Linux  
  * Install postgresql  
  * Export all Wikipedia files to db with 2 cols,primary key as file name   
  
Filename | Whole Article
:----------|:--------------
2142423|The whole artice as a single string
		  
  * Create inverted index [`Lucene`](https://lucene.apache.org/core/) on the file or db whichever is faster(easy)  
   
####1. Stage 2  

  * Get the `EPO` db from freebase(or anywhere) __READYMADE__
  * Create the `EPO` db
     * EPO is the primary key

Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|null
		
  * Start the process of finding new relations and dumping it with the known predicate  
        * `Complex Long process,Leave this for vacation`
        * List of Phrases come from `WikiDB` by querying the `E` in `Inverted Index`
  
Entity|Predicate|Object|List of Phrases
:-----|:--------|:-----|:---------------
SRK|cast|My Name is Khan|[Acted in,Shooted for,Worked in]  

####1. Stage 3

  1. #####Cleanup
  1. Get the category Graph.Hierarchy from somewhere(FB,dbpedia...)
  1. Mark the corrsponding edges with`P` with the list of phrases
  1. Assign Weights