package Spitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Fast DBpedia INFOBOX version
 *
 * @author Ahmed
 */
public class MapTester {

    DataReader dataReader = new DataReader();
    static ProjNLP predicateExtractor = new ProjNLP();
    //static PredExt predicateExtractor=new PredExt();
    static SearcherL sL = new SearcherL();
    static BufferedWriter context;

    public void map(int key, String value) throws IOException, Exception
    {
        Node formal = InfoboxMine.perfectCut(value.toString());
        String searchQ = LuceneConstants.FIELD_TITLE + ":" + formal.subject + " AND Content:" + formal.object;
        String sentences[] = sL.getHighlightedResult(searchQ);
        for (String sentence : sentences)
        {
            context.write(formal.subject + LuceneConstants.DELIMITER + formal.predicate + LuceneConstants.DELIMITER + formal.object + LuceneConstants.DELIMITER + sentence+"\r\n");
        }
        /**
         * String predicates[] = new String[sentences.length]; //TODO: Remove
         * the for loop int i = 0; for (String sentence : sentences) {
         * predicates[i] = predicateExtractor.getPred(new Node(formal.subject,
         * sentence, formal.object)); context.write(formal.subject + ", " +
         * formal.predicate + ", " + formal.object + ", " + predicates[i] +
         * "\n"); i++; }
        *
         */
    }

    public static void main(String args[]) throws IOException, Exception
    {
        context = new BufferedWriter(new FileWriter("keyValue", true));
        MapTester mT = new MapTester();

        BufferedReader file = new BufferedReader(new FileReader("H:\\DBpedia DataSet\\mappingbased-properties_en[INFOBOX].ttl\\mappingbased-properties_en[INFOBOX].ttl"));
        String tuple = file.readLine();
        int i = 0;
        tuple = file.readLine();
        while (tuple != null)
        {
            try
            {
                mT.map(i, tuple);
            } catch (Exception e)
            {
                System.out.println(tuple);
                //e.printStackTrace();
            }
            tuple = file.readLine();
            i++;
            context.flush();
        }
        context.close();
    }
}
