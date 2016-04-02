import DatabaseHandler.Virtuoso;
import Spitter.Cleaner;
import Spitter.Node;
import Spitter.ProjNLP;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Fast DBpedia INFOBOX version
 *
 * @author Ahmed
 */
public class EdgeLabel {

    private static final int MAX_DEPTH=15;
    
  //  static ProjNLP predicateExtractor = new ProjNLP();
    //static PredExt predicateExtractor=new PredExt();
//    static SearcherL sL = new SearcherL();
    static BufferedWriter context;
    Virtuoso virt=new Virtuoso();
    Set<String> objC;
    static Set<String> duplicateCheck = new HashSet<>();
    
    Node node;
    
    public void map(int key, String value) throws Exception
    {
        node=Cleaner.seperate(value);
        Set<String> subC=virt.getSet(virt.runQuery(problemGenerator(node.subject)));
        objC=virt.getSet(virt.runQuery(problemGenerator(node.object)));
        if(objC.size()>0)
            classify(subC);
        context.flush();
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
    public static void main(String args[]) throws IOException, Exception
    {
        context = new BufferedWriter(new FileWriter("keyValue.txt", true));
        EdgeLabel mT = new EdgeLabel();

        BufferedReader file = new BufferedReader(new FileReader("H:\\DBpedia DataSet\\mappingbased-properties_en[INFOBOX].ttl\\mappingbased-properties_en[INFOBOX].ttl"));
        String tuple = file.readLine();
        int i = 0;
        tuple = file.readLine();
        while (tuple != null)
        {
            System.out.println(i+" : "+tuple);
            try
            {
                mT.map(i, tuple);
            } catch (Exception e)
            {
                System.out.println(tuple);
                e.printStackTrace();
            }
            tuple = file.readLine();
            i++;
            context.flush();
        }
        context.close();
    }
}
