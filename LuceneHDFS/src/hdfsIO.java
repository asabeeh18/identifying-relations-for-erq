// cc MaxTemperature Application to find the maximum temperature in the weather dataset
// vv MaxTemperature

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class hdfsIO {
    public static final String INDEX_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Index";
    public static final String FILES_DIRECTORY = "hdfs://master:9000/hadoop/";
    public static final String FIELD_CONTENT = "Content";
    public static final String FIELD_PATH = "Path";
    
    public static void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException
    {

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        Directory indexDir = FSDirectory.open(new File(INDEX_DIRECTORY).toPath());
        try (IndexWriter indexWriter = new IndexWriter(indexDir, config))
        {
            File dir = new File(FILES_DIRECTORY);
            File[] files = dir.listFiles();
            System.out.println("Total Count: " + files.length);
            int i = 0;
            for (File file : files)
            {
                Document document = new Document();
                String path = file.getCanonicalPath();
                document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

                Reader reader = new FileReader(file);
                document.add(new TextField(FIELD_CONTENT, reader));

                indexWriter.addDocument(document);
                i++;
                System.out.println("Current count: " + i + " File " + file.getName());
            }
        }
    }

    public static void main(String[] args) throws Exception
    {

        System.out.println("16");

        Job job = Job.getInstance();
        job.setJarByClass(hdfsIO.class);
        job.setJobName("hdfsIO");

        URI uri = URI.create("hdfs://master:9000/hadoop/core-site.xml");
        Path path = new Path(uri);
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://master:9000");
        FileSystem file = FileSystem.get(uri, conf);
        FSDataInputStream in = file.open(new Path(uri));
        byte[] btbuffer = new byte[5];
        in.seek(5); // sent to 5th position

        in.read(btbuffer, 0, 5);//read 5 byte in byte array from offset 0
        System.out.println("New String: " + new String(btbuffer));// &amp;amp;amp;quot; print 5 character from 5th position
        
        createIndex();
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
        System.out.println("Exit JOB");
    }
}
