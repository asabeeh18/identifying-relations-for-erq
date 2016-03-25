package Spitter;


import java.io.*;
import java.util.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ProjNLP {

    static SentenceDetectorME sentDet;
    static POSTaggerME tagger;
    static Tokenizer tokenizer;
    static String subject, str, object, predicate;
    static String[] sentSet, tokens, tags;
    static final String[] predTags = new String[]
    {
        "CC", "RBR", "RBS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "IN"
    };
    static int numSent;

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
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void getData(Node node)
    {
        subject = node.subject;
        str = node.predicate;
        object = node.object;
    }

    public String getPred(Node node)
    {

        getData(node);

        sentSet = sentDet.sentDetect(str);
        numSent = sentSet.length;

        for (int i = 0; i < numSent; i++)
        {
            tokens = tokenizer.tokenize(sentSet[i]);
            tags = tagger.tag(tokens);
        }

        int objPos, subPos;

        for (int i = 0; i < tokens.length; i++)
        {
            if (tokens[i].equals(subject))
            subPos = i;

            if (tokens[i].equals(object))
            objPos = i;
        }

        int j;
        predicate = "";

        for (int i = 0; i < tokens.length; i++)
        {
            for (j = 0; j < predTags.length; j++)
            {
                if (predTags[j].equals(tags[i]))
                {
                    predicate += tokens[i];
                    break;
                }
            }
        }

        return predicate;
    }
}
