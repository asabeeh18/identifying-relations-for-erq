
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Fast DBpedia INFOBOX version
 *
 * @author Ahmed
 */
public class EdgeLabelMap extends Mapper<LongWritable, Text, Text, Text> {

    private static final int MAX_DEPTH = 15;

    Virtuoso virt = new Virtuoso();
    Set<String> objC;
    static Set<String> duplicateCheck = new HashSet<>();
    Context context;
    Node node;
    String output="";
    String out="";

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {

        try
        {
            System.out.println("Map Entered");
            //output = new Text();
            this.context = context;
            node = Cleaner.seperate(value.toString());
            if (node != null)
            {
                
                Set<String> subC = virt.getSet(virt.runQuery(articleProblemGenerator(node.subject)));
                objC = virt.getSet(virt.runQuery(articleProblemGenerator(node.object)));
                System.out.println("Query Ran:");
                
                if (objC != null && subC != null && objC.size() > 0)
                {
                    System.out.println("Calling classify: "+output);
                    classify(subC);
                }
                else
                {
                 System.out.println("Samples:"+output+","+objC+","+subC);
                }
                if (output != null)
                {
                    System.out.println("non null op: "+output);
                    System.out.println("====line 52: "+output);
                    context.write(new Text("4"), new Text(output));
                }
            } else
            {
                System.out.println("Trashed");

            }
            System.out.println("Mapper Done");
        } catch (Exception e)
        {
            System.out.println("MAPPER GENERATED EXCEPTION: " + value);
        }
    }

    void classify(Set<String> subC) throws IOException, InterruptedException
    {
        System.out.println("In classify: "+output);
        Iterator<String> result = subC.iterator();
        while (result.hasNext())
        {
            duplicateCheck.clear();
            String curNode = result.next();
            boolean path = dfs(curNode, 0);
            if (path)
            {
                //context.write(null, new Text(node.subject + "," + node.predicate + "," + curNode + "\n"));
                out = node.subject + "," + node.predicate + "," + curNode + "\n";
                output+=out;
            }

            System.out.println("line 82: "+output);
        }
    }

    boolean dfs(String begin, int depth) throws IOException, InterruptedException
    {
        output+="DFS: "+begin+"\n";
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
        if (begin.indexOf("Category:") == -1)
        {
            return path;
        }
        Iterator<String> results = virt.getSet(virt.runQuery(categoriesProblemGenerator("<" + begin + ">"))).iterator();
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
                //context.write(null, new Text(begin + "," + node.predicate + "," + rs + "\n"));
                out = begin + "," + node.predicate + "," + rs + "\n";
                output+=out;
                System.out.println("Written: " + begin + "," + node.predicate + "," + rs);
            }
            path = pathExists | path;
        }
        return path;
    }

    private boolean hasPath(String result) throws IOException, InterruptedException
    {
        //Do any of the o's correspond to category belonging to ENtity-category graph
        Iterator<String> objIter = objC.iterator();
        while (objIter.hasNext())
        {
            String curNode = objIter.next();
            if (curNode.equals(result))
            {
                //context.write(null, new Text(curNode + "," + node.predicate + "," + node.object + "\n"));
                out = curNode + "," + node.predicate + "," + node.object + "\n";
                //output.append(out.getBytes(StandardCharsets.UTF_8), 0, out.length());
                
                return true;
            }
        }
        return false;
    }

    private String articleProblemGenerator(String subject)
    {
        //Watch out for the typo it's intentional
        String s = "SELECT ?o FROM <article.catategories> WHERE {" + subject + " ?p ?o}";
        //System.out.println(s);
        return s;
    }

    private String categoriesProblemGenerator(String subject)
    {
        String s = "SELECT ?o FROM <categories.categories> WHERE {" + subject + " ?p ?o}";
        //System.out.println(s);
        return s;
    }
}
