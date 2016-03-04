import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SOReducer extends Reducer<Node, Text, Text,Text> {
  
  @Override
  public void reduce(Node key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
    
	//System.out.println("Reducer: "+key);
    
    for(Text predicate:values)
    {
    	
    }
    context.write();
  }
}
// ^^ MaxTemperatureReducer