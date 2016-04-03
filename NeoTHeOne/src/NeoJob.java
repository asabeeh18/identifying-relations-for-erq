
// cc MaxTemperature Application to find the maximum temperature in the weather dataset
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// vv MaxTemperature
public class NeoJob {

    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            System.err.println("Usage: Intersector <input path> <output path>");
            System.exit(-1);
        }

        System.err.println("16");
        System.out.println("16");

        Job job = Job.getInstance();
        job.setJarByClass(NeoJob.class);
        job.setJobName("NeoTheOne");
        //job.addArchiveToClassPath(new Path("/libs/lib.zip"));
        //addAllToClassPath(job);
        job.addArchiveToClassPath(new Path("/libs/commons-lang3-3.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/concurrentlinkedhashmap-lru-1.4.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/jline-2.12.jar"));
        job.addArchiveToClassPath(new Path("/libs/lucene-core-3.6.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-codegen-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-consistency-check-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-consistency-check-legacy-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-csv-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-1.9_2.11-2.0.5.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-2.2_2.11-2.2.6.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-2.3-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-frontend-2.3-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-function-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-graph-algo-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-graph-matching-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-import-tool-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-io-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-jmx-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-kernel-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-logging-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-lucene-index-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-primitive-collections-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-shell-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-udc-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/neo4j-unsafe-2.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/opencsv-2.3.jar"));
        job.addArchiveToClassPath(new Path("/libs/parboiled-core-1.1.7.jar"));
        job.addArchiveToClassPath(new Path("/libs/parboiled-scala_2.11-1.1.7.jar"));
        job.addArchiveToClassPath(new Path("/libs/README.txt"));
        job.addArchiveToClassPath(new Path("/libs/scala-library-2.11.7.jar"));
        job.addArchiveToClassPath(new Path("/libs/scala-parser-combinators_2.11-1.0.4.jar"));
        job.addArchiveToClassPath(new Path("/libs/scala-reflect-2.11.7.jar"));
        job.addArchiveToClassPath(new Path("/libs/server-api-2.3.2.jar"));

        NeoTheOne neo = new NeoTheOne();
        ///neo.uniqueNode();
        neo.shutdown();

        System.err.println("25");
        System.out.println("25");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        NLineInputFormat.setNumLinesPerSplit(job, 2);
        job.setNumReduceTasks(0);
        job.setMapperClass(NeoMap.class);
        //job.setReducerClass(IntersectorReducer.class);
        //job.setNumReduceTasks(1);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
        System.out.println("Exit JOB");
    }

    //Yes *JUGAAD*
    private static void addAllToClassPath(Job job) throws IOException
    {

    }

}
