package Spitter;


import static Spitter.SampleIndex.FIELD_CONTENT;
import static Spitter.SampleIndex.FIELD_PATH;
import static Spitter.SampleIndex.FILES_DIRECTORY;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PackedTokenAttributeImpl;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

class HighlightedIndex {

    LuceneHighlighter luceneHighlighter;
    
    public class Indexer {

        private IndexWriter indexWriter;
        public static final String INDEX_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Index";
        public static final String FILES_DIRECTORY = "H:\\Stage1_Subset\\Partaa\\Stage1_Articles";
        public static final String FIELD_CONTENT = "Content";
        public static final String FIELD_PATH = "Path";
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
            int i = 0;
            for (File file : files)
            {
                Document document = new Document();
                String path = file.getCanonicalPath();
                document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

                String content = new Scanner(file).useDelimiter("\\Z").next();
                document.add(new TextField(FIELD_CONTENT, content,Field.Store.YES));

                indexWriter.addDocument(document);
                i++;
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

        private static final String INDEX_DIRECTORY_PATH = "H:\\Stage1_Subset\\Partaa\\Index";
        private static final int MAX_DOC = 10;

        public void createIndex() throws Exception
        {
            Indexer indexer = new Indexer(INDEX_DIRECTORY_PATH);
            Integer maxDoc = indexer.createIndex(); // Returns total documents indexed
            System.out.println("Index Created, total documents indexed: " + maxDoc);
            indexer.close(); // Close index writer
        }
        private Searcher searcher;

        public void searchIndex(String searchQuery) throws Exception
        {

            searcher = new Searcher(INDEX_DIRECTORY_PATH);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser(FIELD_CONTENT, analyzer);
            Query query = queryParser.parse(searchQuery);

            TopDocs topDocs = searcher.search(query, MAX_DOC);
            ScoreDoc scoreDocs[] = topDocs.scoreDocs;
            
            PackedTokenAttributeImpl po=new PackedTokenAttributeImpl();
            int off=po.startOffset();
            System.out.println(off+"");
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                Document document = searcher.getDocument(scoreDoc.doc);
                String title = document.get(FIELD_PATH);
                System.out.println(title);
            }
        }

        public void searchAndHighLightKeywords(String searchQuery) throws Exception
        {
            searcher = new Searcher(INDEX_DIRECTORY_PATH);
            // STEP A
            QueryParser queryParser = new QueryParser(FIELD_CONTENT, new StandardAnalyzer());
            Query query = queryParser.parse(searchQuery);
            QueryScorer queryScorer = new QueryScorer(query, FIELD_CONTENT);
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
            
            Highlighter highlighter = new Highlighter(queryScorer); // Set the best scorer fragments
            highlighter.setMaxDocCharsToAnalyze(1000*Highlighter.DEFAULT_MAX_CHARS_TO_ANALYZE);
            highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight

            // STEP B
            File indexFile = new File(INDEX_DIRECTORY_PATH);
            Directory directory = FSDirectory.open(indexFile.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);

            // STEP C
            System.out.println("query: "+ query);
            ScoreDoc scoreDocs[] = searcher.search(query, MAX_DOC).scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                System.out.println("1");
                Document document = searcher.getDocument(scoreDoc.doc);
                String title = document.get(FIELD_CONTENT);
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader,
                        scoreDoc.doc, FIELD_CONTENT, document, new StandardAnalyzer());
                String fragment = highlighter.getBestFragment(tokenStream, title);
                System.out.println(fragment+"/n-------"+title);
            }
        }
    }

    public class Searcher {

        private IndexSearcher indexSearcher;

        public Searcher(String indexerDirectoryPath) throws Exception
        {

            File indexFile = new File(indexerDirectoryPath);
            Directory directory = FSDirectory.open(indexFile.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
        }

        public TopDocs search(Query query, int n) throws Exception
        {
            return indexSearcher.search(query, n);
        }

        public Document getDocument(int docID) throws Exception
        {
            return indexSearcher.doc(docID); // Returns a document at the nth ID
        }
    }

    public static void main(String[] args) throws Exception
    {

        HighlightedIndex h = new HighlightedIndex();
        h.runner();
    }

    public void runner() throws Exception
    {
        String searchQuery = "Islamic AND revolution";
        searchIndex(searchQuery);
       // 
        //luceneHighlighter.searchIndex(searchQuery); // without highlight functionality
        
    }

    public HighlightedIndex()
    {
        luceneHighlighter = new LuceneHighlighter();
    }
    void createIndex() throws Exception
    {
        luceneHighlighter.createIndex();
    }
    void searchIndex(String searchQuery) throws Exception
    {
        luceneHighlighter.searchIndex(searchQuery);
    }
}
