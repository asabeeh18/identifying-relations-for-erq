package CategoryGraph;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EdgeLabelMap {

    private static final int MAX_DEPTH = 15;
    private static final char DELIMITER=';';
    Virtuoso virt = new Virtuoso();
    Set<String> objC;
    static Set<String> duplicateCheck = new HashSet<>();
    Node node;
    StringBuilder out=new StringBuilder();
    
    public String map(String value) throws IOException, InterruptedException
    {
        System.out.println("Map Entered");
        node = Cleaner.seperate(value.toString());
        
        if (node != null)
        {
            Set<String> subC = virt.getSet(virt.runQuery(articleProblemGenerator(node.subject)));
            objC = virt.getSet(virt.runQuery(articleProblemGenerator(node.object)));

            if (objC != null && subC != null && objC.size() > 0)
            {
                System.out.print(" . ");
                classify(subC);
                if (out != null)
                {
                    System.out.print(".");
                    return new String(out);
                }
            }
        }
        System.out.println("Mapper Done");
        return null;
    }

    void classify(Set<String> subC) throws IOException, InterruptedException
    {
        Iterator<String> result = subC.iterator();
        while (result.hasNext())
        {
            duplicateCheck.clear();
            String curNode = result.next();
            boolean path = dfs(curNode, 0);
            if (path)
            {
                String output = node.subject + DELIMITER + node.predicate + DELIMITER + curNode + "\n";
                out.append(output);
                System.out.print(".");
            }
        }
    }

    boolean dfs(String begin, int depth) throws IOException, InterruptedException
    {

        boolean path = false;

        if (hasPath(begin))
        {
            path = true;
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
                String output = begin + DELIMITER + node.predicate + DELIMITER + rs + "\n";
                out.append(output);
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
                String output = curNode + DELIMITER + node.predicate + DELIMITER + node.object + "\n";
                out.append(output);
                return true;
            }
        }
        return false;
    }

    private String articleProblemGenerator(String subject)
    {
        //Watch out for the typo it's intentional
        String s = "SELECT ?o FROM <http://articles.cat> WHERE {" + subject + " ?p ?o}";
        //System.out.println(s);
        return s;
    }

    private String categoriesProblemGenerator(String subject)
    {
        String s = "SELECT ?o FROM <http://category.cat> WHERE {" + subject + " ?p ?o}";
        //System.out.println(s);
        return s;
    }
}
