/*

TODO: 1. git. WARNING: DO NOT DO 2 without any VCS.
      2. positions http://www.computergodzilla.blogspot.in/2013/07/how-to-use-lucene-highlighter.html
 */
package Spitter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Ahmed
 */
public class LuceneHDFS {

    /**
     * @param args the command line arguments
     */
    public static final String INDEX_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Index";
    public static final String FILES_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Stage1_Articles";
    public static final String FIELD_CONTENT = "Content";
    public static final String FIELD_PATH = "Path";

    public static void main(String[] args) throws LockObtainFailedException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
    {
        //createIndex();
        searchIndex("islamic AND revolution AND 1979");
    }

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

    public static void searchIndex(String searchString) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
    {
        System.out.println("Searching for '" + searchString + "'");
        Directory indexDir = FSDirectory.open(new File(INDEX_DIRECTORY).toPath());
        
        //Directory directory = FSDirectory.getDirectory(INDEX_DIRECTORY);
        IndexReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(FIELD_CONTENT, analyzer);
        Query query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(query,30);
        System.out.println("Number of hits: " + hits.totalHits);

        for ( ScoreDoc scoreDoc : hits.scoreDocs )
        {
            Document document = indexSearcher.doc(scoreDoc.doc);
            String path = document.get(FIELD_PATH);
            System.out.println("Hit: " + path+" Score: "+scoreDoc.score);
        }

    }

}
