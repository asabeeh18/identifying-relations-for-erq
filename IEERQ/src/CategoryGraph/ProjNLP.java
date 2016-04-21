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
        {
            e.printStackTrace();
        }
    }

/*    
    public static void getData(Scanner scr)
    {
        System.out.print("SUBJECT: ");
        subject = scr.nextLine().toLowerCase();
        
        System.out.print("SENTENCE: ");
        input = scr.nextLine().toLowerCase();
        
        System.out.print("OBJECT: ");
        object = scr.nextLine().toLowerCase();
    }
*/

    public static void getData(Node node)
    {
        subject = node.subject.toLowerCase();
        try{
            input = node.predicate.toLowerCase();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        object = node.object.toLowerCase();
    }
    
    public static String getPredicate(String str)
    {
        predicate = "";
        objTok = tokenizer.tokenize(object);
        tokens = tokenizer.tokenize(str);
        tags = tagger.tag(tokens);
        
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

/*    
    public static void findPredicates()
    {
        sentSet = sentDet.sentDetect(input);
        predSet = new String[sentSet.length];
        
        for(int i = 0; i < sentSet.length; i++)
            predSet[i] = getPredicate(sentSet[i]);
    }
*/

    public static String[] findPredicates(Node node)
    {
       
        getData(node);
        sentSet = sentDet.sentDetect(input);
        predSet = new String[sentSet.length];
        
        for(int i = 0; i < sentSet.length; i++)
            predSet[i] = getPredicate(sentSet[i]);
        
        return predSet;
    }  

    
/*
    public static void main(String args[])
    {
        ProjNLP classObj = new ProjNLP();
        
        getData(new Scanner(System.in));
        findPredicates();
        
        System.out.println("\nFINAL PREDICATE SET:");
        for(int i = 0; i < predSet.length; i++)
            System.out.println(predSet[i]);
    }
*/
}