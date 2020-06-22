/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package working;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Ahmed
 */
public class Working {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException
    {
        // TODO code application logic here
        /*
         1. Read from infobox
         2. pass data to searcher
         3. write search res to file
         4. pick phrase from res
        
         */
        infoboxSearcher();
        /*
        1-3 done above^
        */
        
        /*
        4 options
           i   extract verb, verb is the predicate
           ii  remove nouns, remaining is the predicate
           iii Reverb
        i,ii : Parse tree extract,high chances of missing predicates.nlp.
        
        iii : Sentence in proper format,punctuations,...may give predicates of other S-O combination.
*/
        
        //Start Reverb@29/9.
        /**
         * new timestamp 23:52 of 20/10/16
         * Input: SPO and a sentence(s)
		 
		 1.  Clean (punctutaions except '.',[]())
		 2.  break sentence OpenNLP
		 3.  give each sentence to reverb 
			i. it gives SPO for each sentence
			ii.  extracet predicate
			iii. match our 'O' with rv's 'o' and 's'
			iv. take the highest confident's P
		3. store to file as quadruple S P O newPhrase
         * 
        **/
        //1.
       //  reVerber();
        
        
    }
    
    public static void infoboxSearcher() throws FileNotFoundException, IOException
    {

        Scanner r=new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader(new File(LuceneConstants.INFOBOX+r.nextLine())));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.SEARCH_OUT),true));
        SearchFile sF = new SearchFile();

        String line = br.readLine();
        
        for (int nulls = 0; line != null; line = br.readLine())
        {
            Node n = null;
            try
            {
                n = InfoboxMine.perfectCut(line);
            } catch (Exception e)
            {
                //System.out.println(line);
                sF.errorPrinter(e);
            }

            if (n == null || n.object == null || n.subject == null)
            {
                nulls++;
                continue;
            }

            String s[] = null;
            String query = null;
            try
            {
                query = LuceneConstants.FIELD_TITLE + ":" + n.subject + spacedContentGen(n.object);//+LuceneConstants.FIELD_CONTENT+""
                s = sF.getHighlightedResult(query);
                
            } catch (Exception e)
            {
                //System.out.println(query);
                sF.errorPrinter(e);
            }
            if (s == null)
            {
                nulls++;
                continue;
            }
            for (String s1 : s)
            {
                bw.write(n.subject + LuceneConstants.DELIMITER + n.predicate + LuceneConstants.DELIMITER + n.object + LuceneConstants.DELIMITER + s1 + "\n");
            }
            //bw.flush();
        }
        bw.close();
    }
    private static String spacedContentGen(String content)
    {
        //we have spaces in content, replace it with "Content:obj1 AND Content:obj2"
        String replaceAll = content.replaceAll("  "," ");
        replaceAll = replaceAll.replaceAll("  "," ");
        replaceAll = replaceAll.replaceAll(" ", " AND Content:");
        replaceAll = " AND Content:" + replaceAll;
        return replaceAll.trim();
    }

   
    private static void reVerber() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(new File(LuceneConstants.SEARCH_OUT)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.SPO_PHRASE)));
        //next 2 lines for input CHANGE THEM
        String line=br.readLine();
        String[] spos=line.split(LuceneConstants.DELIMITER);  //SPO and searchResult
        PredExt pE=new PredExt();
        
        //String phrase=pE.reVExtractor(pE.sentencer(spos[3]),spos[2]); // :) ^.^ :'( ReverbFailed
        String sentences[] = pE.sentencer(spos[3]);
        String phrase;
        for(String sentence: sentences)
        {
            phrase = pE.reVExtractor(sentence, spos[2]);
            //System.out.println(phrase);
            bw.write(spos[0] + LuceneConstants.DELIMITER + spos[1] + LuceneConstants.DELIMITER + spos[2] + LuceneConstants.DELIMITER + phrase + "\n");
        }
        
        
    }
}