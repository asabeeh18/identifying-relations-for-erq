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
    }

    public static void infoboxSearcher() throws FileNotFoundException, IOException
    {

        BufferedReader br = new BufferedReader(new FileReader(new File(LuceneConstants.INFOBOX)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.SEARCH_OUT)));
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
                System.out.println(line);
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
                System.out.println(query);
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
            bw.flush();

        }
        bw.close();
    }
    private static String spacedContentGen(String content)
    {
        //we have spaces in content, replace it with "Content:obj1 AND Content:obj2"
        String replaceAll = content.replaceAll("  ", " ");
        replaceAll = replaceAll.replaceAll("  ", " ");
        replaceAll = replaceAll.replaceAll(" ", " AND Content:");
        replaceAll = " AND Content:" + replaceAll;
        return replaceAll;
    }
}