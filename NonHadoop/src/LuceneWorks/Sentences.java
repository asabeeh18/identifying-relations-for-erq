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
        final String File_Path = r.nextLine(),B_OPEN="<b>",B_END="</b>";
        BufferedReader br = new BufferedReader(new FileReader(new File(File_Path)));
        SearcherL sL = new SearcherL();
        ProjNLP pN = new ProjNLP();
        int i = 0;
        String line, pred[], sentences[];
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
            if (node == null || node.object.equals(node.subject))
            {
                continue;
            }

            sentences = sL.getHighlightedResult("+" + node.subject + " AND +" + node.object);
            System.out.println(node + ": ");
            for (String sentence : sentences)
            {
//                f

                sentence = sentence.replace("<B", " <B");
                sentence = sentence.replace("-", " -");
                sentence = sentence.replace("_", " _");
                sentence = sentence.replace("|", " | ");

                int start = node.object.indexOf(' ');
                if (start != -1)
                {
                    
                    StringBuilder s = new StringBuilder(node.object.substring(0, start));
                    int last = start;
                    s.append(B_OPEN+" "+B_END);
                    for (int m = start + 1; m < node.object.length(); m++)
                    {
                        if (node.object.charAt(m) == ' ')
                        {
                            s.append(node.object.substring(last+1, m)).append(B_OPEN+" "+B_END);
                            last = m;
                        }
                    }
                    s.append(node.object.substring(last+1, node.object.length()));
                    node.object = s.toString();
                }
                pred = pN.findPredicates(new Node("<b>" + node.subject + "</b>", sentence, "<b>" + node.object + "</b>"));
                System.out.print(";" + pred);
                predicates.append(";").append(pred);
            }
            i++;
            System.out.println(i + " tuples Processed, " + (i * 100.0 / TOTAL_TUPLES) + "% done.");
        }
    }

}
