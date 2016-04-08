package Spitter;

import java.io.BufferedReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

class IndexerL {

    public static String INDEX_DIRECTORY;
    public static String FILES_DIRECTORY;

    public class Indexer {

        private IndexWriter indexWriter;
        public static final String FIELD_CONTENT = "Content",
                FIELD_TITLE = "Title",
                FIELD_ID="Id";

        public Indexer(String indexerDirectoryPath) throws Exception
        {
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(OpenMode.CREATE);

            File indexFile = new File(indexerDirectoryPath);
            Directory directory = FSDirectory.open(indexFile.toPath());
            indexWriter = new IndexWriter(directory, indexWriterConfig);
        }

        public int createIndex() throws Exception
        {

            File dir = new File(FILES_DIRECTORY);
            File[] files = dir.listFiles();
            System.out.println("Total Count: " + files.length);
            int i = 0,start,end;
            for (File file : files)
            {
                
                //String path = file.getCanonicalPath();
                
                BufferedReader line = new BufferedReader(new FileReader(file));
                String content;
                while ((content = line.readLine()) != null)
                {
                    end=content.indexOf(":::");
                    start=content.indexOf(',');
                    String title=content.substring(start+1,end).trim();
                    String id=content.substring(0,start);
                    Document document = new Document();
                    document.add(new TextField(FIELD_CONTENT, content, Field.Store.YES));
                    document.add(new StringField(FIELD_TITLE, title, Field.Store.YES));
                    document.add(new StringField(FIELD_ID, id, Field.Store.YES));
                    indexWriter.addDocument(document);
                    System.out.println("Current count: " + i + " File " + file.getName());
                    i++;                  
                }

                //String content = new String(Files.readAllBytes(Paths.get(path)));
                System.out.println("Current count: " + i + " File " + file.getName());
            }
            indexWriter.commit();
            return indexWriter.numDocs();
        }

        public void close() throws Exception
        {
            indexWriter.close();
        }
    }

    public class LuceneHighlighter {

        //private static final String INDEX_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Index";
//        private static final int MAX_DOC = 10;

        public void createIndex() throws Exception
        {
            Indexer indexer = new Indexer(INDEX_DIRECTORY);
            Integer maxDoc = indexer.createIndex(); // Returns total documents indexed
            System.out.println("Index Created, total documents indexed: " + maxDoc);
            indexer.close(); // Close index writer
        }
    }
    public static void main(String[] args) throws Exception
    {
        IndexerL h = new IndexerL();
        h.runner();
    }

    public void runner() throws Exception
    {
        Scanner r = new Scanner(System.in);
        System.out.println("Index Directory path: ");
        INDEX_DIRECTORY = r.nextLine();
        System.out.println("Files Directory path: ");
        FILES_DIRECTORY = r.nextLine();
        createIndex();
    }
    
    
    void createIndex() throws Exception
    {
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();
    }

}
