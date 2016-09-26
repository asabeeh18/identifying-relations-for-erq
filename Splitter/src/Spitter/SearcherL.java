package Spitter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.Scanner;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;

class SearcherL {

    LuceneHighlighter luceneHighlighter;
    
    private Searcher searcher;
    //QueryParser queryParser;
    MultiFieldQueryParser queryParser;
    public SearcherL()
    {
        luceneHighlighter = new LuceneHighlighter();
        try
        {
            searcher = new Searcher(LuceneConstants.INDEX_DIRECTORY);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //queryParser = new QueryParser(LuceneConstants.FIELD_CONTENT, new StandardAnalyzer());
         queryParser = new MultiFieldQueryParser(
                                        new String[] {LuceneConstants.FIELD_TITLE},
                                        new StandardAnalyzer());
    }

    public class LuceneHighlighter {

        private static final int MAX_DOC = 10;

        public String[] searchAndHighLightKeywords(String searchQuery) throws Exception
        {

            // STEP A
            Query query = queryParser.parse(searchQuery);
            ScoreDoc scoreDocs[] = searcher.search(query, MAX_DOC).scoreDocs;
            String[] sentence = new String[scoreDocs.length];
            
            QueryScorer queryScorer = new QueryScorer(query, LuceneConstants.FIELD_CONTENT);
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer, 500);
            Highlighter highlighter = new Highlighter(queryScorer); // Set the best scorer fragments
            //highlighter.setMaxDocCharsToAnalyze(100);
            highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight

            // STEP B
            File indexFile = new File(LuceneConstants.INDEX_DIRECTORY);
            Directory directory = FSDirectory.open(indexFile.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);

            // STEP C
            //System.out.println("query: " + query);
            
            int i = 0;
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                //System.out.println("1");
                Document document = searcher.getDocument(scoreDoc.doc);
                String title = document.get(LuceneConstants.FIELD_CONTENT);
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader,
                        scoreDoc.doc, LuceneConstants.FIELD_CONTENT, document, new StandardAnalyzer());
                sentence[i] = highlighter.getBestFragment(tokenStream, title);
                System.out.println(sentence[i] + "-------");
                //=fragment;
                i++;
            }
            return sentence;
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

    public static void main(String[] args) throws Exception
    {
        
        Scanner r = new Scanner(System.in);
        
        SearcherL sl = new SearcherL();

        System.out.println("Search Query: ");
/***
 *  The way it works... no results still , Use luke to look into the index ...luke build connect
             java.net.SocketException: Socket closed deymo..   :'/ They won't come out no matter what
             * Done
    using core to look in lucene index, errors lo and behold!
 */        
        
        String s[] = sl.getHighlightedResult(LuceneConstants.FIELD_TITLE + ":Aristotle" + " AND Content:Golden AND Content:mean");//+LuceneConstants.FIELD_CONTENT+"");
        
        
        for (String s1 : s)
        {
            System.out.println(s1);
        }
    }

}