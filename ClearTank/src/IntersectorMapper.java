// cc MaxTemperatureMapper Mapper for maximum temperature example
// vv MaxTemperatureMapper
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IntersectorMapper
  extends Mapper<LongWritable, Text, LongWritable, Text> {
  
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException 
  {
    //FileSplit fileSplit=(FileSplit)context.getInputSplit();
	 // System.err.println("Mapper: "+key);
	  System.out.println("Mapper:1 ");
	  System.out.println("Received: "+value);
      long  i=10;
      
	  while(true)
      {
          i=(long)Math.pow(i, i);
          if(i>Long.MAX_VALUE)
              break;
      }
      System.out.println("Mapper: OUT OF LOOP!!!!");
    Path filePath=((FileSplit)context.getInputSplit()).getPath();
    //System.out.println("Mapper:2 ");
      context.write(new LongWritable(3),new Text() );
      //System.out.println("Mapper:3 ");
  }
}
// ^^ MaxTemperatureMapper