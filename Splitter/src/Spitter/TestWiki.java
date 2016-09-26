
import java.io.StringReader;
import org.apache.lucene.analysis.wikipedia.WikipediaTokenizer;


class TestWiki {

    public WikipediaTokenizer testSimple() throws Exception
    {
        String text = "This is a [[Category:foo]]";
        return new WikipediaTokenizer(new StringReader(text));
    }

    public static void main(String[] args)
    {
        WikipediaTokenizerTest wtt = new WikipediaTokenizerTest();

        try
        {
            WikipediaTokenizer x = wtt.testSimple();

            logger.info(x.hasAttributes());

            Token token = new Token();
            int count = 0;
            int numItalics = 0;
            int numBoldItalics = 0;
            int numCategory = 0;
            int numCitation = 0;

            while (x.incrementToken() == true)
            {
                System.out.println("");("seen something");
            }

        } catch (Exception e)
        {
            System.out.println("Exception while tokenizing Wiki Text: " + e.getMessage());
        }

    
}
