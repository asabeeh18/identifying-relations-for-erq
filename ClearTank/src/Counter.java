// cc MaxTemperature Application to find the maximum temperature in the weather dataset
// vv MaxTemperature
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Counter {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: Intersector <input path> <output path>");
      System.exit(-1);
    }
   
    Job job = Job.getInstance();
    job.setJarByClass(Counter.class);
    job.setJobName("Counter");

    
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    //NLineInputFormat.setNumLinesPerSplit(job,2);
    //job.setNumReduceTasks(0);
    job.setMapperClass(CountMap.class);
    job.setReducerClass(CountRed.class);
    //job.setNumReduceTasks(1);
     
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    System.out.println("Exit JOB");
  }
  
}
// ^^ MaxTemperature