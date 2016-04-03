
import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

class NeoRed extends Reducer<NullWritable,NullWritable,NullWritable,NullWritable>
{
    @Override
    public void reduce(NullWritable key,Iterable<NullWritable> values,Context context) throws IOException, InterruptedException
    {
        context.write(NullWritable.get(),NullWritable.get());
    }
}