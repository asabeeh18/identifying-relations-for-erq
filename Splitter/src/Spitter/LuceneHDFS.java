/*

 TODO: 1. git. WARNING: DO NOT DO 2 without any VCS.
 2. positions http://www.computergodzilla.blogspot.in/2013/07/how-to-use-lucene-highlighter.html
 */
package Spitter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author Ahmed
 */
public class LuceneHDFS {

    /**
     * @param args the command line arguments
     */
    public static final String INDEX_DIRECTORY = "H:\\DBpedia DataSet\\Stage1_Articles\\0 to 14L\\index_1_1";
    public static final String FILES_DIRECTORY = "H:\\DBpedia DataSet\\Stage1_Articles\\0 to 14L\\content_1";
    public static final String FIELD_CONTENT = "Content";
    public static final String FIELD_PATH = "Path";

    public static void main(String[] args) throws LockObtainFailedException, IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
    {
        //createIndex();
        searchIndex1("+abraham");
        //searchIndex1("+fat");

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
                FieldType ft = new FieldType();
                ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                ft.setStoreTermVectors(true);
                ft.setStoreTermVectorOffsets(true);
                ft.setStoreTermVectorPayloads(true);
                ft.setStoreTermVectorPositions(true);
                ft.setTokenized(true);
                Field ff = new Field("SS", new String(Files.readAllBytes(Paths.get(path))), ft);
                document.add(ff);

                document.add(new StringField(FIELD_PATH, path, Field.Store.YES));

                Reader reader = new FileReader(file);
                document.add(new TextField(FIELD_CONTENT, reader));
                indexWriter.addDocument(document);
                i++;
                System.out.println("Current count: " + i + " File " + file.getName());
            }
        }
    }

    public static void searchIndex1(String searchString) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException
    {
        Directory indexDir = FSDirectory.open(new File(INDEX_DIRECTORY).toPath());
        IndexReader ir = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(ir);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(FIELD_CONTENT, analyzer);
        Query query = queryParser.parse(searchString);
        TopDocs hits = indexSearcher.search(query, 30);

        Fields f = ir.getTermVectors(hits.scoreDocs[0].doc);
        Iterator fieldNames = f.iterator();
        while (fieldNames.hasNext())
        {
            System.out.println(fieldNames.next());
        }
        System.out.println("xx--xx---END IERATOR===");

        Document document = indexSearcher.doc(hits.scoreDocs[0].doc);
        String path = document.get(FIELD_PATH);
        System.out.println("Hit: " + path);
        PostingsEnum p = null;

        for (ScoreDoc scoreDoc : hits.scoreDocs)
        {

            Terms tv = ir.getTermVector(scoreDoc.doc, "SS");
            TermsEnum terms = tv.iterator();

            int id = scoreDoc.doc;
            Document doc = indexSearcher.doc(id);
            //Fields termVectors = ir.getTermVectors(id);
            Terms termVector = ir.getTermVector(id, "SS");
            boolean hasPos = termVector.hasPositions();
            if (hasPos)
            {
                PostingsEnum pos = null;
                TermsEnum iterator1 = termVector.iterator();
                pos = iterator1.postings(p, PostingsEnum.POSITIONS);
                int p2 = pos.nextPosition();
                System.out.println(p2 + "");
            }

//            while (terms.next() != null)
//            {
//                p = terms.postings(p, PostingsEnum.ALL);
//
//                while (p.nextDoc() != PostingsEnum.NO_MORE_DOCS)
//                {
//                    int freq = p.freq();
//                    for (int i = 0; i < freq; i++)
//                    {
//                        int pos = p.nextPosition();   // Always returns -1!!!
//                        System.out.println("Pos: " + pos + "DOC: " + p.docID());
//
//                    //BytesRef data = p.getPayload();
//                        //doStuff(freq, pos, data); // Fails miserably, of course.
//                    }
//                    //System.out.println("====FOR====" + p.attributes());
//                }
//                //System.out.println("x==x==WHILE==x==x");
//            }
//            System.out.println("---");
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
        TopDocs hits = indexSearcher.search(query, 30);

        System.out.println("Number of hits: " + hits.totalHits);
        PostingsEnum p = null;
        Terms tv = indexReader.getTermVector(hits.scoreDocs[0].doc, "SS");

        TermsEnum terms = tv.iterator();
        for (ScoreDoc scoreDoc : hits.scoreDocs)
        {
            while (terms.next() != null)
            {
                p = terms.postings(p, PostingsEnum.ALL);

                while (p.nextDoc() != PostingsEnum.NO_MORE_DOCS)
                {
                    int freq = p.freq();
                    for (int i = 0; i < freq; i++)
                    {
                        int pos = p.nextPosition();   // Always returns -1!!!
                        System.out.println("Pos: " + pos);

                        //BytesRef data = p.getPayload();
                        //doStuff(freq, pos, data); // Fails miserably, of course.
                    }
                    System.out.println("====FOR====" + p.attributes());
                }
                System.out.println("x==x==WHILE==x==x");
            }
//            Terms tv = indexReader.getTermVector(scoreDoc.doc, "SS");
//            TermsEnum terms = tv.iterator();
//            p = terms.postings(p, PostingsEnum.POSITIONS);
//            terms.next();
//            p.nextDoc();
//            int freq = p.freq();
//            for (int i = 0; i < freq; i++)
//            {
//                int pos = p.nextPosition();
//                System.out.print("Pos: " + pos);
//             }
            System.out.println();
            Document document = indexSearcher.doc(scoreDoc.doc);
            Object path = document.get(FIELD_PATH);
//            String pos = document.getFields().toString();
            System.out.println("Hit: " + path + " Score: " + scoreDoc.score);
//            String str[] = document.getValues("SS");
//            for (String s : str)
//            {
//                System.out.println("====STRING: " + s);
//            }
            System.out.println("---");
        }

    }
}
