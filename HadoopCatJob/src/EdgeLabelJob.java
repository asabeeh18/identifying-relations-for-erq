
// cc MaxTemperature Application to find the maximum temperature in the weather dataset
// vv MaxTemperature
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class EdgeLabelJob {

    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            System.err.println("Usage: Intersector <input path> <output path>");
            System.exit(-1);
        }
        System.out.println("16");

        Job job = Job.getInstance();
        job.setJarByClass(EdgeLabelJob.class);
        job.setJobName("Relationer");
        //Add to classpath
        //Yes *JUGAAD*
        job.addArchiveToClassPath(new Path("/libs/arq.jar"));
//        job.addArchiveToClassPath(new Path("/libs/commons-lang3-3.3.2.jar"));
        job.addArchiveToClassPath(new Path("/libs/commons-logging.jar"));
//        job.addArchiveToClassPath(new Path("/libs/concurrentlinkedhashmap-lru-1.4.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/CopyLibs/"));
//        job.addArchiveToClassPath(new Path("/libs/hadoop-annotations-2.7.1.jar"));
//        job.addArchiveToClassPath(new Path("/libs/hadoop-common-2.7.1.jar"));
//        job.addArchiveToClassPath(new Path("/libs/hadoop-mapreduce-client-core-2.7.1.jar"));
        job.addArchiveToClassPath(new Path("/libs/icu4j_3_4.jar"));
        job.addArchiveToClassPath(new Path("/libs/iri.jar"));
        job.addArchiveToClassPath(new Path("/libs/jena.jar"));
//        job.addArchiveToClassPath(new Path("/libs/jena-2.6.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/jline-2.12.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-analyzers-common-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-core-3.6.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-core-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-highlighter-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-memory-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-queries-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/lucene-queryparser-5.4.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/nblibraries.properties"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-codegen-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-consistency-check-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-consistency-check-legacy-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-csv-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-1.9_2.11-2.0.5.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-2.2_2.11-2.2.6.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-compiler-2.3-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-cypher-frontend-2.3-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-function-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-graph-algo-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-graph-matching-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-import-tool-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-io-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-jmx-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-kernel-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-logging-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-lucene-index-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-primitive-collections-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-shell-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-udc-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/neo4j-unsafe-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/opencsv-2.3.jar"));
//        job.addArchiveToClassPath(new Path("/libs/opennlp-tools-1.6.0.jar"));
//        job.addArchiveToClassPath(new Path("/libs/parboiled-core-1.1.7.jar"));
//        job.addArchiveToClassPath(new Path("/libs/parboiled-scala_2.11-1.1.7.jar"));
//        job.addArchiveToClassPath(new Path("/libs/scala-library-2.11.7.jar"));
//        job.addArchiveToClassPath(new Path("/libs/scala-parser-combinators_2.11-1.0.4.jar"));
//        job.addArchiveToClassPath(new Path("/libs/scala-reflect-2.11.7.jar"));
//        job.addArchiveToClassPath(new Path("/libs/server-api-2.3.2.jar"));
//        job.addArchiveToClassPath(new Path("/libs/slf4j-api-1.7.18.jar"));
        job.addArchiveToClassPath(new Path("/libs/virt_jena.jar"));
        job.addArchiveToClassPath(new Path("/libs/virtjdbc3.jar"));
//        job.addArchiveToClassPath(new Path("/libs/virtjdbc4.jar"));
        job.addArchiveToClassPath(new Path("/libs/xercesImpl.jar"));

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //NLineInputFormat.setNumLinesPerSplit(job, 2);
        //job.setNumReduceTasks(0);
        job.setMapperClass(EdgeLabelMap.class);
        //job.setReducerClass(Relationships.class);
        //job.setNumReduceTasks(1);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
        System.out.println("Exit JOB");
    }
}
