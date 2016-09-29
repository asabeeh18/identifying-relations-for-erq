package Spitter;

/* 
 Last Updated:27/09/16, store all search results in a file
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

class SearchFile {

    LuceneHighlighter luceneHighlighter;

    private Searcher searcher;
    //QueryParser queryParser;
    MultiFieldQueryParser queryParser;

    public SearchFile()
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
                new String[]
                {
                    LuceneConstants.FIELD_TITLE
                },
                new StandardAnalyzer());
    }

    void errorPrinter(Exception e)
    {
        e.printStackTrace();
        System.out.println("=====\n");
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
                //System.out.println(sentence[i] + "-------");
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
        try
        {
            return luceneHighlighter.searchAndHighLightKeywords(query);
        } catch (Exception e)
        {
            System.out.println(query);
            errorPrinter(e);
        }
        return null;

    }

    public static void main(String[] args) throws Exception
    {

        infoboxSearcher();
        Scanner r = new Scanner(System.in);

        SearchFile sl = new SearchFile();

        System.out.println("Search Query: ");

        String s[] = sl.getHighlightedResult(LuceneConstants.FIELD_TITLE + ":Aristotle" + " AND Content:Golden AND Content:mean");//+LuceneConstants.FIELD_CONTENT+"");

        for (String s1 : s)
        {
            System.out.println(s1);
        }
    }

    public static void infoboxSearcher() throws FileNotFoundException, IOException
    {

        BufferedReader br = new BufferedReader(new FileReader(new File(LuceneConstants.INFOBOX)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.SEARCH_OUT)));
        SearchFile sF = new SearchFile();

        String line = br.readLine();
        
        for (int nulls = 0;line != null;line = br.readLine())
        {
            Node n=null;
            try
            {
                n = InfoboxMine.perfectCut(line);
            } catch (Exception e)
            {
                System.out.println(line);
                sF.errorPrinter(e);
            }
            
            if (n == null || n.object == null || n.subject == null)
            {
                nulls++;
                continue;
            }
            
            String s[]=null;
            String query=null;
            try{
                query=LuceneConstants.FIELD_TITLE + ":" + n.subject + spacedContentGen(n.object);//+LuceneConstants.FIELD_CONTENT+""
                s= sF.getHighlightedResult(query);    
            }
            catch(Exception e)
            {
                System.out.println(query);
                sF.errorPrinter(e);
            }
            if (s == null)
            {
                nulls++;
                continue;
            }
            for (String s1 : s)
            {
                bw.write(n.subject+LuceneConstants.DELIMITER+n.predicate+LuceneConstants.DELIMITER+n.object+LuceneConstants.DELIMITER+s1+"\n");
            }
            bw.flush();
            
        }
        bw.close();
    }

    private static String spacedContentGen(String content)
    {
        //we have spaces in content, replace it with "Content:obj1 AND Content:obj2"
        String replaceAll = content.replaceAll("  ", " ");
        replaceAll = replaceAll.replaceAll("  ", " ");
        replaceAll = replaceAll.replaceAll(" ", " AND Content:");
        replaceAll = " AND Content:" + replaceAll;
        return replaceAll;
    }

}
