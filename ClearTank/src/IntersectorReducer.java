// cc MaxTemperatureReducer Reducer for maximum temperature example
// vv MaxTemperatureReducer
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IntersectorReducer
  extends Reducer<Object,Object, Object,Object> {
  
  @Override
  public void reduce(Object key, Iterable<Object> values,Context context) throws IOException, InterruptedException {
    
	  System.out.println("Reducer: "+key);
    int i=0;
    
    if(i==2)
    	context.write(new Object(), new Object());
  }
}
// ^^ MaxTemperatureReducer