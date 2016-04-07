import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

class LuceneMap  extends Mapper<LongWritable, Text, NullWritable, Text> {

    static LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
    public static String INDEX_DIRECTORY = "/home/user/index/";
    public static final String FIELD_CONTENT = "Content";
    public static final String FIELD_PATH = "Path";
    static Searcher searcher= new Searcher();
    static QueryParser queryParser = new QueryParser(FIELD_CONTENT, new StandardAnalyzer());;
    
    public static class LuceneHighlighter {

        private static final int MAX_DOC = 10;

        public String[] searchAndHighLightKeywords(String searchQuery) throws Exception
        {

            // STEP A
            Query query = queryParser.parse(searchQuery);
            QueryScorer queryScorer = new QueryScorer(query, FIELD_CONTENT);
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer, 300);

            Highlighter highlighter = new Highlighter(queryScorer); // Set the best scorer fragments
            //highlighter.setMaxDocCharsToAnalyze(100000);
            highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight

            // STEP B
            File indexFile = new File(INDEX_DIRECTORY);
            Directory directory = FSDirectory.open(indexFile.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);

            // STEP C
            //System.out.println("query: " + query);
            ScoreDoc scoreDocs[] = searcher.search(query, MAX_DOC).scoreDocs;
            String[] sentence = new String[scoreDocs.length];
            int i = 0;
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                //System.out.println("1");
                Document document = searcher.getDocument(scoreDoc.doc);
                String title = document.get(FIELD_CONTENT);
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader,
                        scoreDoc.doc, FIELD_CONTENT, document, new StandardAnalyzer());
                sentence[i] = highlighter.getBestFragment(tokenStream, title);
                //System.out.println(fragment + "-------");
                //=fragment;
                i++;
            }
            return sentence;
        }
    }

    public static class Searcher {

        private IndexSearcher indexSearcher;

        public Searcher()
        {
            File indexFile = new File(INDEX_DIRECTORY);
            try
            {
                Directory directory = FSDirectory.open(indexFile.toPath());
                IndexReader indexReader = DirectoryReader.open(directory);
                indexSearcher = new IndexSearcher(indexReader);
            } catch (IOException ex)
            {
                Logger.getLogger(LuceneMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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

    public String[] getHighlightedResult(String query)
    {
        try{
            return luceneHighlighter.searchAndHighLightKeywords(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
        
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {

        Scanner r = new Scanner(System.in);
        System.out.println("Index Directory path: ");
        String INDEX_DIRECTORY = r.nextLine();
        SearcherL sl = new SearcherL();

        System.out.println("Search Query: ");

        String s[] = sl.getHighlightedResult(r.nextLine());
        for (String s1 : s)
        {
            System.out.println(s1);
        }
    }
}
