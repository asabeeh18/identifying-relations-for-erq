package Spitter;

/* For representing a sentence that is annotated with pos tags and np chunks.*/


/* String -> ChunkedSentence */


/* The class that is responsible for extraction. */


/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */


/* A class for holding a (arg1, rel, arg2) triple. */
/**
 * Discard plan Reverb most of the sentences in wiki are compound sentences.
 * ReVerb "could" "possibly" be used incase the other plan fails TERRIBLY because 
 * reVerb would probably in my opinion give 10% of the real result at best.
 *  :'(
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class PredExt {

    //OpenNlpSentenceChunker chunker;
    SentenceDetectorME sentenceDetector;
    ReVerbExtractor reverb;
    private void loadSentencer()    //for OpenNLP sentence detection
    {
        SentenceModel model = null ;
        InputStream modelIn = null ;
        

        try
        {
            modelIn = new FileInputStream("en-sent.bin");
            model= new SentenceModel(modelIn);
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (modelIn != null)
            {
                try
                {
                    modelIn.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        sentenceDetector = new SentenceDetectorME(model);
    }
    public String[] sentencer(String string) throws FileNotFoundException
    {
        return sentenceDetector.sentDetect(string);
    }
    

    public PredExt()
    {
//        try
//        {
//            chunker = new OpenNlpSentenceChunker();
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
        loadSentencer();
        reverb = new ReVerbExtractor();
    }

//    public String getPred(Node node) throws IOException
//    {
//        String sentStr = node.predicate;
//        String preds = "";
//        ChunkedSentence sent = chunker.chunkSentence(sentStr);
//        ReVerbExtractor reverb = new ReVerbExtractor();
//        
//        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
//        for (ChunkedBinaryExtraction extr : reverb.extract(sent))
//        {
//            preds += extr.getRelation();
//
//        }
//        return preds;
//    }

    public static void main(String[] args) throws Exception
    {
        PredExt pE = new PredExt();
        String input="to renewing American appreciation of Lincoln’s legacy, the 15-member commission is made up of lawmakers and scholars and also features an adivsory board of over 130 various Lincoln historians and enthusiasts. Located at [[Library of Congress]] in [[Washington, D.C.]], the [[ALBC]] is the organizing force behind numerous tributes, programs and cultural events highlighting a two-year celebration scheduled to begin in February 2008 at Lincoln’s birthplace:  [[<B>Hodgenville</B>]], [[<B>Kentucky</B>]].		Lincoln's";
        input=pE.clean(input);
        String sentences[] = pE.sentencer(input);
        String phrase;
        for(String sentence: sentences)
        {
            phrase = pE.reVExtractor(sentence, "Hodgenville, Kentucky");
            System.out.println(phrase);
        } // :) ^.^
        //(new PredExt()).reverbRunner("Mutualism began in 18th century English and French labor movements before taking an anarchist form associated with [[Pierre-Joseph Proudhon]] in France");
    }

//    void reverbRunner(String sentStr) throws ConfidenceFunctionException, IOException
//    {
//        // Looks on the classpath for the default model files.
//        //OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
//        ChunkedSentence sent = chunker.chunkSentence(sentStr);
//
//        // Prints out the (token, tag, chunk-tag) for the sentence
//        System.out.println(sentStr);
//        for (int i = 0; i < sent.getLength(); i++)
//        {
//            String token = sent.getToken(i);
//            String posTag = sent.getPosTag(i);
//            String chunkTag = sent.getChunkTag(i);
//            System.out.println(token + " " + posTag + " " + chunkTag);
//        }
//
//        // Prints out extractions from the sentence.
//        ReVerbExtractor reverb = new ReVerbExtractor();
//        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
//        for (ChunkedBinaryExtraction extr : reverb.extract(sent))
//        {
//            double conf = confFunc.getConf(extr);
//            System.out.println("Arg1=" + extr.getArgument1());
//            System.out.println("Rel=" + extr.getRelation());
//            System.out.println("Arg2=" + extr.getArgument2());
//
//            System.out.println("Conf=" + conf);
//
//        }
//    }

    String reVExtractor(String sentStr, String obj) throws IOException
    {
        // Looks on the classpath for the default model files.
        //OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
        //ChunkedSentence sent = chunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        //System.out.println(sentStr);
//        String token,posTag,chunkTag;
//        for (int i = 0; i < sent.getLength(); i++)
//        {
//            token = sent.getToken(i);
//            posTag = sent.getPosTag(i);
//            chunkTag = sent.getChunkTag(i);
//            //System.out.println(token + " " + posTag + " " + chunkTag);
//        }

        // Prints out extractions from the sentence.
         
        //ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();

        double maxConf = Integer.MIN_VALUE;
        String maxConfRel = null;
        // !Important rev.extreactFromString takes a very long time when done 1st time,
        //same for the reverb OBj in constructor keep that in mind when doing further development
        for (ChunkedBinaryExtraction extr : reverb.extractFromString(sentStr))
        {
           // double conf = confFunc.getConf(extr);
            String subject = extr.getArgument1().toString();
            String rel = extr.getRelation().toString();
            String object = extr.getArgument2().toString();

            double c1 = checkConf(subject, object);
            double c2 = checkConf(subject, object);
            if (c1 > maxConf)
            {
                maxConf = c1;
                maxConfRel = rel;
            }
            if (c2 > maxConf)
            {
                maxConf = c2;
                maxConfRel = rel;
            }
            //System.out.println("Conf=" + conf); NOT THIS CONF

        }
        return maxConfRel;
    }

    //=======SIMILARITY ==========
    public static double similarity(String s1, String s2)
    {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length())
        { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0)
        {
            return 1.0; /* both strings are zero length */ }
        /* // If you have StringUtils, you can use it to calculate the edit distance:
         return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
         (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2)
    {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++)
            {
                if (i == 0)
                {
                    costs[j] = j;
                } else
                {
                    if (j > 0)
                    {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                        {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
            {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    private double checkConf(String subject, String object)
    {
        return similarity(subject, object);
    }

    private String clean(String input)
    {

        //char[] replaceMe= {!:=()|{}\\"#,};
        char replace[]=input.toCharArray();
        input=input.replaceAll("[!:=()|#,'\\{\\}\\\\\"\\[\\]\\[\\/]","");
        
        return input;
    }
}
