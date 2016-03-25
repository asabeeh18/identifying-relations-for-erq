package Spitter;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


/**
 * Fast DBpedia INFOBOX version
 * @author Ahmed
 */
public class KeyGenerator extends Mapper<LongWritable, Text, Node, Text> {

	DataReader dataReader=new DataReader();
    static ProjNLP predicateExtractor=new ProjNLP();
    static SearcherL sL=new SearcherL();

    @Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
    {
        Node formal=Cleaner.clean(value.toString());
        String sentences[]=sL.getHighlightedResult(formal.subject+" "+formal.object);
        String predicates[]=new String[sentences.length];
        int i=0;
        for(String sentence:sentences)
        {
            predicates[i]=predicateExtractor.getPred(new Node(formal.subject,sentence,formal.object));
            context.write(new Node(formal.subject,null,formal.object), new Text(predicates[i]));
            i++;
            
        }
        
        
	}
}
// ^^ MaxTemperatureMapper