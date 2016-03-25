package Spitter;

/* For representing a sentence that is annotated with pos tags and np chunks.*/
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/* String -> ChunkedSentence */
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;

/* The class that is responsible for extraction. */
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;

/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;

/* A class for holding a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PredExt {
    OpenNlpSentenceChunker chunker;
    public PredExt()
    {
        try{
            chunker = new OpenNlpSentenceChunker();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
    }

    
    public String getPred(Node node) throws IOException
    {
        String sentStr=node.predicate;
        String preds="";
        ChunkedSentence sent = chunker.chunkSentence(sentStr);

        ReVerbExtractor reverb = new ReVerbExtractor();
        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) 
        {  
            preds+=extr.getRelation();
            
        }
        return preds;
    }
    public static void main(String[] args) throws Exception {

        String sentStr = "<http://dbpedia.org/resource/Sociological_and_cultural_aspects_of_autism> <http://dbpedia.org/ontology/abstract> \"Sociological and cultural aspects of <B>autism</B> come into play with recognition of <B>autism</B>, approaches to its support services and therapies, and how <B>autism</B> affects how we define personhood";

        // Looks on the classpath for the default model files.
        OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
        ChunkedSentence sent = chunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        System.out.println(sentStr);
        for (int i = 0; i < sent.getLength(); i++) {
            String token = sent.getToken(i);
            String posTag = sent.getPosTag(i);
            String chunkTag = sent.getChunkTag(i);
            System.out.println(token + " " + posTag + " " + chunkTag);
        }

        // Prints out extractions from the sentence.
        ReVerbExtractor reverb = new ReVerbExtractor();
        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
            double conf = confFunc.getConf(extr);
            System.out.println("Arg1=" + extr.getArgument1());
            System.out.println("Rel=" + extr.getRelation());
            System.out.println("Arg2=" + extr.getArgument2());
            System.out.println("Conf=" + conf);
        }
    }
}