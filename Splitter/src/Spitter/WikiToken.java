package Spitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.wikipedia.WikipediaTokenizer;

class WikiToken {

    public static void main(String ar[]) throws IOException
    {
        String test = "the modern sense of the word ''autism'' in 1938.]]	Autism is a [[developmental disorder]] of the [[human brain]] that first gives signs during infancy or childhood and follows a steady course without [[Remission (medicine)|remission]] or [[relapse]].<ref name=ICD-10-<B>F84.0</B>/> Impairments result from maturation-related changes in various systems of the brain.<ref name=Penn/> Autism is one of the five [[pervasive developmental disorder]]s (PDD), which are characterized by widespread abnormalities of social";
        
        InputStream modelIn = new FileInputStream("en-sent.bin");
        SentenceModel model = new SentenceModel(modelIn);
        if (modelIn != null)
            modelIn.close();
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
        String sentences[] = sentenceDetector.sentDetect(test);
        for(String se:sentences)
        {
            System.out.println(se);
        }

        WikipediaTokenizer tf = new WikipediaTokenizer();
        
        tf.setReader(new StringReader(test));
        OffsetAttribute offsetAttribute = tf.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = tf.addAttribute(CharTermAttribute.class);
        tf.reset();
        while (tf.incrementToken())
        {
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = charTermAttribute.toString();
            System.out.print(term+" ");
        }
    }
}
