import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class KeyGenerator extends Mapper<LongWritable, Text, Node, Text> {

	DataReader dataReader=new DataReader();
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		
		Node node=dataReader.readInfobox();
		while(node!=null)
		{
			context.write(node, filePath);
			node=dataReader.readInfobox();
		}
		
		
		
	}
}
// ^^ MaxTemperatureMapper