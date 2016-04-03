

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.neo4j.graphdb.Node;


public class NeoMap extends Mapper<LongWritable, Text, NullWritable, NullWritable> {

    
    public static NeoTheOne neo=new NeoTheOne();
    
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException
    {
        
        String[] nodes=value.toString().split(" ");
        Node a=neo.createNode(nodes[0]);
        Node b=neo.createNode(nodes[1]);
        neo.createEdge(a, b, nodes[0]+nodes[1]);
        context.write(NullWritable.get(),NullWritable.get());
    }

}
