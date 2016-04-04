
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;
/**
 * ***
 * The custom category graph has tuples in form of
 * <subject> <rank>^^<predicate> <object>
 * Ex: Will need to use regex when querying.... think of a better option :(
 *
 */
class EdgeLabelReducer extends Reducer<NullWritable, Text, Text, Text> {

    @Override
    public void reduce(NullWritable val, Iterable<Text> values, Context context) throws IOException, InterruptedException
    {

        for (Text value : values)
        {
            System.out.println("4: "+ value);
            context.write(new Text("4"), value);
        }

    }
}
