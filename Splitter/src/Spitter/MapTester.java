package Spitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Fast DBpedia INFOBOX version
 * @author Ahmed
 */
public class MapTester{

	DataReader dataReader=new DataReader();
    static ProjNLP predicateExtractor=new ProjNLP();
    //static PredExt predicateExtractor=new PredExt();
    static SearcherL sL=new SearcherL();
    static BufferedWriter context;
    
	public void map(int key,String value) throws IOException, Exception
    {
        Node formal=Cleaner.clean(value.toString());
        String sentences[]=sL.getHighlightedResult("+"+formal.subject+" AND "+"+"+formal.object);
        String predicates[]=new String[sentences.length];
        int i=0;
        for(String sentence:sentences)
        {
            predicates[i]=predicateExtractor.getPred(new Node(formal.subject,sentence,formal.object));
            context.write(formal.subject+"    "+formal.object+"    " +predicates[i]+"..."+sentence+"\n");
            i++;
            
        }
	}
    public static void main(String args[]) throws IOException, Exception
    {
        context = new BufferedWriter(new FileWriter("keyValue", true));
        MapTester mT=new MapTester();
        
        BufferedReader file = new BufferedReader(new FileReader("H:\\DBpedia DataSet\\mappingbased-properties_en[INFOBOX].ttl\\mappingbased-properties_en[INFOBOX].ttl"));
        String tuple=file.readLine();
        int i=0;
        tuple=file.readLine();
        while(tuple!=null && i<100)
        {
            mT.map(i,tuple);
            tuple=file.readLine();
            i++;
            context.flush();
        }
    }
}