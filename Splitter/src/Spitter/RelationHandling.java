package Spitter;

// cc MaxTemperature Application to find the maximum temperature in the weather dataset
// vv MaxTemperature
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RelationHandling {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: Intersector <input path> <output path>");
      System.exit(-1);
    }
    
	System.out.println("16");
    
    Job job = Job.getInstance();
    job.setJarByClass(RelationHandling.class);
    job.setJobName("Relaitoner");

    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    NLineInputFormat.setNumLinesPerSplit(job,2);
    //job.setNumReduceTasks(0);
    job.setMapperClass(KeyGenerator.class);
    job.setReducerClass(Relationships.class);
    //job.setNumReduceTasks(1);
	System.out.println("25");
    
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    System.out.println("Exit JOB");
  }
}
// ^^ MaxTemperature