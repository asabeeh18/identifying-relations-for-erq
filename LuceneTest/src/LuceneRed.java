
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.Reducer;

class LuceneRed  extends Reducer<NullWritable, Text, NullWritable, Text> {
     @Override
    public void reduce(NullWritable val, Iterable<Text> values, Context context) throws IOException, InterruptedException
    {

        for (Text value : values)
        {
            context.write(NullWritable.get(), value);
        }

    }
}