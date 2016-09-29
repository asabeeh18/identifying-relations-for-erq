package Spitter;

import DatabaseHandler.Virtuoso;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Fast DBpedia INFOBOX version
 *
 * @author Ahmed
 */
public class EdgeLabelMap extends Mapper<LongWritable, Text, Void, Text> {

    private static final int MAX_DEPTH=15;
    
    static ProjNLP predicateExtractor = new ProjNLP();
    //static PredExt predicateExtractor=new PredExt();
    static SearcherL sL = new SearcherL();
    static BufferedWriter context;
    Virtuoso virt=new Virtuoso();
    Set<String> objC;
    static Set<String> duplicateCheck = new HashSet<>();
    
    Node node;
    @Override
    public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException 
    {
        node=Cleaner.seperate(value.toString());
        Set<String> subC=virt.getSet(virt.runQuery(problemGenerator(node.subject)));
        objC=virt.getSet(virt.runQuery(problemGenerator(node.object)));
        if(objC.size()>0)
            classify(subC);
        //context.write("\n");
    }
    
    void classify(Set<String> subC) throws IOException
    {
        Iterator<String> result=subC.iterator();
        while (result.hasNext())
        {
            duplicateCheck.clear();
            String curNode=result.next();
            boolean path=dfs(curNode,0);
            if(path)
            {
                context.write(node.subject+","+node.predicate+","+curNode+"\n");
                
            }
            //context.write("\n");
            //System.out.print("");
            
        }
    }
    
    boolean dfs(String begin, int depth) throws IOException
    {
        boolean path = false;

        if (hasPath(begin))
        {
            path = true;
            System.out.println("Found Path!");
        }

        if (depth == MAX_DEPTH)
        {
            return path;
        }
        if(begin.indexOf("Category:")==-1)
            return path;
        Iterator<String> results = virt.getSet(virt.runQuery(problemGenerator("<"+begin+">"))).iterator();
        while (results.hasNext())
        {
            String rs = results.next();
            if (!duplicateCheck.add(rs))
            {
                continue;
            }

            boolean pathExists = dfs(rs, depth + 1);
            if (pathExists)
            {
                //Create edge
                //Assuming duplicate edges wont exist
                context.write(begin+","+node.predicate+","+rs+"\n");
                System.out.println("Written: "+ begin+","+node.predicate+","+rs);
            }
            path = pathExists | path;
        }
        return path;
    }
    private boolean hasPath(String result) throws IOException
    {
        //Do any of the o's correspond to category belonging to ENtity-category graph
        Iterator<String> objIter = objC.iterator();
        while (objIter.hasNext())
        {
            String curNode=objIter.next();
            if (curNode.equals(result))
            {
                context.write(curNode+","+node.predicate+","+node.object+"\n");
                return true;
            }
        }
        return false;
    }
    private String problemGenerator(String subject)
    {
        String s="SELECT ?o FROM <http://dbpedia.org> WHERE {"+subject+ " ?p ?o}";
        //System.out.println(s);
        return s;
    }
}
