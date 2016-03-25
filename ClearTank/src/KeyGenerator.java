import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.lucene.search.SearcherLifetimeManager;

public class KeyGenerator extends Mapper<LongWritable, Text, Node, Text> {

	DataReader dataReader=new DataReader();
    static ProjNLP predicateExtractor=new ProjNLP();
    static SearcherL=new SearcherL();
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
    {
	
		Node[] node=dataReader.readInfobox();
        for(Node n: node)
        {
            LuceneSearch.search(n);
            
            predicateExtractor.getPred(new Node(n.subject,,n.object));
        }
		while(node!=null)
		{
			context.write(node, filePath);
			node=dataReader.readInfobox();
		}
	}
}
// ^^ MaxTemperatureMapper