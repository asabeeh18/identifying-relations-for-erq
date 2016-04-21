package CategoryGraph;
import java.io.*;
import java.util.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ProjNLP
{
    static SentenceDetectorME sentDet;
    static POSTaggerME tagger;
    static Tokenizer tokenizer;
    
    static String subject, object, predicate, input;
    static String[] sentSet, tokens, tags, objTok, predSet;
    static final String[] predTags = new String[]
    {
        "CC", "RBR", "RBS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "IN"
    };
    
    public ProjNLP()
    {
        InputStream loadedModel;
        
        try
        {
            loadedModel = new FileInputStream("en-sent.bin");
            SentenceModel sentModel = new SentenceModel(loadedModel);
            sentDet = new SentenceDetectorME(sentModel);
            loadedModel = new FileInputStream("en-token.bin");
            TokenizerModel tokModel = new TokenizerModel(loadedModel);
            tokenizer = new TokenizerME(tokModel);

            loadedModel = new FileInputStream("en-pos-maxent.bin");
            POSModel posModel = new POSModel(loadedModel);
            tagger = new POSTaggerME(posModel);
        }
        catch(Exception e)
        {}
    }
    
    public static void getData(Node node)
    {
        subject = node.subject;
        input = node.predicate;
        object = node.object;
    }

    public static String getPredicate(String str)
    {
        predicate = "";
        objTok = tokenizer.tokenize(object);
        tokens = tokenizer.tokenize(str);
        tags = tagger.tag(tokens);
        
        System.out.println("\nTOKEN-TAG SET:");
        for(int i = 0; i < tokens.length; i++)
            System.out.println(tokens[i] + "|" + tags[i]);
        
        int objPos = -1;
        loop1:  for(int i = 0; i < tokens.length; i++)
                    for(int j = 0; j < objTok.length; j++)
                        if((tokens[i].contains(objTok[j]) || objTok[j].contains(tokens[i])))
                        {
                            if(tokens[i].contains("<b>") || tokens[i].contains("</b>"))
                            {
                                if(tokens[i].contains("</b>") && (tokens[i].contains("<b>") == false))
                                {
                                    for(int k = i - 1; k > 0; k--)
                                    {
                                        if(tokens[k].contains("<b>"))
                                        {
                                            objPos = k;
                                            break loop1;
                                        }
                                    }
                                }
                                else
                                {
                                    objPos = i;
                                    break loop1;
                                }
                            }
                        }
        
        for(int i = 0; i < objPos; i++)
        {
            if(tokens[i].contains("<b>") || tokens[i].contains("</b>"))
                continue;
            
            if(tags[i].equals("CD"))
                predicate += "[[CD]] ";
            else if(tags[i] != "PRP" && tags[i] != "PRP$")
            {
                predicate += tokens[i] + " ";
                
                for(int j = 0; j < predTags.length; j++)
                    if(tags[i].equals(predTags[j]))
                    {
                        predicate += "[[" + tags[i] + "]] ";
                        break;
                    }
            }
        }
        
        predicate.trim();
        return predicate;
    }
    
    public static String[] findPredicates(Node node)
    {
        
        
        getData(node);
        sentSet = sentDet.sentDetect(input);
        predSet = new String[sentSet.length];
        
        for(int i = 0; i < sentSet.length; i++)
            predSet[i] = getPredicate(sentSet[i]);
        
        return predSet;
    }
    
    public static void main(String args[])
    {
       // findPredicates();

        //ProjNLP classObj = new ProjNLP();
        
        //getData(new Scanner(System.in));
        //sentSet = sentDet.sentDetect(input);
        //predSet = new String[sentSet.length];
        
        //for(int i = 0; i < sentSet.length; i++)
            //predSet[i] = getPredicate(sentSet[i]);
        
        //System.out.println("\nFINAL PREDICATE SET:");
        //for(int i = 0; i < predSet.length; i++)
            //System.out.println(predSet[i]);
    }
}
//package CategoryGraph;
//
//import java.io.*;
//import java.util.*;
//import opennlp.tools.postag.POSModel;
//import opennlp.tools.postag.POSTaggerME;
//import opennlp.tools.sentdetect.*;
//import opennlp.tools.tokenize.Tokenizer;
//import opennlp.tools.tokenize.TokenizerME;
//import opennlp.tools.tokenize.TokenizerModel;
////====
///* For representing a sentence that is annotated with pos tags and np chunks.*/
//import edu.washington.cs.knowitall.nlp.ChunkedSentence;
//
///* String -> ChunkedSentence */
//import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
//
///* The class that is responsible for extraction. */
//import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
//
///* The class that is responsible for assigning a confidence score to an
// * extraction.
// */
//import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
//import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunctionException;
//import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;
//
///* A class for holding a (arg1, rel, arg2) triple. */
//import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
//
//public class ProjNLP {
//
//    static SentenceDetectorME sentDet;
//    static POSTaggerME tagger;
//    static Tokenizer tokenizer;
//    static String subject, str, object, predicate, cd = "CD";
//    static String[] sentSet, tokens, tags;
//    static final String[] predTags = new String[]
//    {
//        "CC", "RBR", "RBS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "IN"
//    };
//    static int numSent;
//    ReVerbExtractor reverb;
//    ConfidenceFunction confFunc;
//    OpenNlpSentenceChunker chunker;
//
//    public ProjNLP()
//    {
//        InputStream loadedModel;
//        try
//        {
//            loadedModel = new FileInputStream("en-sent.bin");
//            SentenceModel sentModel = new SentenceModel(loadedModel);
//            sentDet = new SentenceDetectorME(sentModel);
//            loadedModel = new FileInputStream("en-token.bin");
//            TokenizerModel tokModel = new TokenizerModel(loadedModel);
//            tokenizer = new TokenizerME(tokModel);
//            loadedModel = new FileInputStream("en-pos-maxent.bin");
//            POSModel posModel = new POSModel(loadedModel);
//            tagger = new POSTaggerME(posModel);
//
//            reverb = new ReVerbExtractor();
//            confFunc = new ReVerbOpenNlpConfFunction();
//            chunker = new OpenNlpSentenceChunker();
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void getData(Node node)
//    {
//        subject = node.subject;
//        str = node.predicate;
//        object = node.object;
//    }
//
//    public String getPred(Node node)
//    {
//
//        getData(node);
//
//        sentSet = sentDet.sentDetect(str);
//        numSent = sentSet.length;
//
//        for (int i = 0; i < numSent; i++)
//        {
//            tokens = tokenizer.tokenize(sentSet[i]);
//            tags = tagger.tag(tokens);
//        }
//
//        int objPos, subPos;
//
//        for (int i = 0; i < tokens.length; i++)
//        {
//            if (tokens[i].equals(subject))
//            {
//                subPos = i;
//            }
//
//            if (tokens[i].equals(object))
//            {
//                objPos = i;
//            }
//        }
//
//        int j;
//        predicate = "";
//
//        for (int i = 0; i < tokens.length; i++)
//        {
//            predicate += tokens[i] + " ";
//
//            for (j = 0; j < predTags.length; j++)
//            {
//                if (predTags[j].equals(tags[i]))
//                {
//                    if (tags[i].equals(cd))
//                    {
//                        predicate = predicate.substring(0, predicate.length() - tokens[i].length());
//                        predicate += "[[CD]] ";
//                    } else
//                    {
//                        predicate += "[[" + predTags[j] + "]] ";
//                    }
//
//                    break;
//                }
//            }
//        }
//
//        predicate.trim();
//        return predicate;
//    }
//
//    public String reverb(String s) throws ConfidenceFunctionException, IOException
//    {
//        ChunkedSentence sent = chunker.chunkSentence(s);
//        String pred;
//        for (ChunkedBinaryExtraction extr : reverb.extract(sent))
//        {
//            double conf = confFunc.getConf(extr);
//            pred = extr.getRelation().toString();
//        }
//        return s;
//    }
//
//    public static void main(String ar[]) throws ConfidenceFunctionException, IOException
//    {
//        ProjNLP pn = new ProjNLP();
//        Scanner r = new Scanner(System.in);
//        String sent;
//        Node node = new Node(r.nextLine(), (sent = r.nextLine()), r.nextLine());
//        String pred = pn.getPred(node);
//        System.out.println("Pred: " + pred);
//        pn.reverb(sent);
//    }
//}
