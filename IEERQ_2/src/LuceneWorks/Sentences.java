package LuceneWorks;

import CategoryGraph.Cleaner;
import CategoryGraph.Node;
import CategoryGraph.ProjNLP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Ahmed
 */
public class Sentences {

    static double TOTAL_TUPLES = 32143159.0;

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Scanner r = new Scanner(System.in);
        System.out.println("Enter Infobox File Path");
        final String File_Path = r.nextLine(), B_OPEN = "<b>", B_END = "</b>", EMPTY_STRING = "";
        BufferedReader br = new BufferedReader(new FileReader(new File(File_Path)));
        SearcherL sL = new SearcherL();
        ProjNLP pN = new ProjNLP();
        int i = 0;
        String line, pred[] = new String[0], sentences[];
        StringBuilder predicates = new StringBuilder();
        Node node = null;
        line = br.readLine();
        while ((line = br.readLine()) != null)
        {
            try
            {
                node = InfoboxMine.perfectCut(line);
            } catch (Exception e)
            {
                e.printStackTrace();
                System.out.println(line);
            }
            if (node == null || node.object.equals(node.subject) || node.object.equals(EMPTY_STRING) || node.subject.equals(EMPTY_STRING) || node.predicate.equals(EMPTY_STRING))
            {
                continue;
            }

            sentences = sL.getHighlightedResult("+" + node.subject + " AND +" + node.object);
            System.out.println(node + ": ");
            for (String sentence : sentences)
            {
//                f

//                sentence = sentence.replace("<B", " <B");
//                sentence = sentence.replace("-", " -");
//                sentence = sentence.replace("_", " _");
//                sentence = sentence.replace("|", " | ");
//                int start = node.object.indexOf(' ');
//                if (start != -1)
//                {
//
//                    StringBuilder s = new StringBuilder(node.object.substring(0, start));
//                    int last = start;
//                    
//                    for (int m = start + 1; m < node.object.length(); m++)
//                    {
//                        if (node.object.charAt(m) == ' ')
//                        {
//                            s.append(node.object.substring(last + 1, m));
//                            last = m;
//                        }
//                    }
//                    s.append(node.object.substring(last + 1, node.object.length()));
                //node.object = s.toString();
                try
                {
                    if (sentence != null && !sentence.equals(EMPTY_STRING))
                    {
                        pred = pN.findPredicates(new Node(node.subject, sentence, node.object));
                        System.out.print(";" + pred);
                        predicates.append(";").append(pred);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            i++;
            System.out.println(i + " tuples Processed, " + (i * 100.0 / TOTAL_TUPLES) + "% done.");
        }
    }

}