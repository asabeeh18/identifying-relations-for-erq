package Spitter;

import org.apache.hadoop.io.Text;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * ***
 * The custom category graph has tuples in form of
 *
 * Ex: Will need to use regex when querying.... think of a better option :(
 *
 */
class Relationships extends Reducer<Node, Text, Text, Text> {

    private static final int MAX_DEPTH = 7;
    static Set<RDFNode> edges = new HashSet<>();
    
    static Set<String> objCategory;
    
    Node key;
    ResultSet objects;
    Virtuoso virt=new Virtuoso();

    @Override
    public void reduce(Node key, Iterable<Text> values, Context context) throws IOException, InterruptedException
    {
//        System.out.println("Reducer: " + key);
        this.key = key;
        objCategory = virt.getSet(virt.runQuery("SELECT ?o FROM <categories> WHERE { " + key.object + " ?p ?o }"));
        classify(values);
        context.write(new Text(key.predicate), predicateLine(key, values));
        
    }
void classify(Iterable<Text> predicates)
    {
        ResultSet results = runQuery("SELECT ?o FROM <categories> WHERE { " + key.subject + " ?p ?o }");
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode o = rs.get("o");
            dfs(o, 0);
        }
    }
    void classify(Iterable<Text> predicates)
    {
        /**
         * Do. 
         * 
         * equal: return and store start and final edge
         * not: store intermediate edge,add next categories in queue
         * 
         */
        Set<String> results = virt.getSet(virt.runQuery("SELECT ?o FROM <categories> WHERE { " + key.subject + " ?p ?o }"));
        Iterator<String> iter=results.iterator();
        while (iter.hasNext())
        {
            String s=iter.next();
            if(objCategory.add(s))
            {
                objCategory.remove(s);
            }
            else
            {
                return ;
            }
            //write to text file
            String edge = key.subject+","+key.predicate+","+"";
        }
    }

    Text predicateLine(Node start, Iterable<Text> predicates)
    {
        String lineOfPredicates = start.predicate + "    ";
        for (Text pred : predicates)
        {
            lineOfPredicates += pred + " ";
        }
        return new Text(lineOfPredicates);
    }

    boolean dfs(RDFNode begin, int depth)
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

        ResultSet results = runQuery("SELECT ?o FROM <category.categories> WHERE { " + begin + " ?p ?o }");
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode o = rs.get("o");
            if (!edges.add(o))
            {
                continue;
            }

            boolean pathExists = dfs(o, depth + 1);
            if (pathExists)
            {
                /*
                 ASK if edge already exists If yes increment the rank.
                    
                 */
                //      ASK {<http://dbpedia.org/resource/Arthur_Laurents> <http://purl.org/dc/terms/subject> <http://dbpedia.org/resource/Categaory:United_States_Army_personnel>};
                boolean res = runAsk("ASK{<> <> <> }");
                if (res)
                {
                    //update rank of predicate
                }
            }
            path = pathExists | path;
        }
        return path;
    }

    
    void toDbpedia(Node convert)
    {
        convert.subject = "http://dbpedia.org/resource/" + convert.subject;
        convert.object = "http://dbpedia.org/resource/" + convert.object;
        convert.predicate = "http://dbpedia.org/property/" + convert.predicate;
        //return convert;
    }

    public static void main(String[] args)
    {
        Relationships r = new Relationships();
        Text a = new Text(), b = new Text();
        a.set("A");
        b.set("B");
        Node n = new Node("A", "p", "C");
        //r.classify(n, null);
    }

//    public static void main1(String[] args)
//    {
//        /*			STEP 2			*/
//        System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
//
//        System.out.println("\nexecute: INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }");
//        str = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }";
//        vur = VirtuosoUpdateFactory.create(str, set);
//        vur.exec();
//
//        /*			STEP 3			*/
//        /*		Select all data in virtuoso	*/
//        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
//        Query sparql = QueryFactory.create("SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
//
//        /*			STEP 4			*/
//        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
//
//        ResultSet results = vqe.execSelect();
//        while (results.hasNext())
//        {
//            QuerySolution rs = results.nextSolution();
//            RDFNode s = rs.get("s");
//            RDFNode p = rs.get("p");
//            RDFNode o = rs.get("o");
//            System.out.println(" { " + s + " " + p + " " + o + " . }");
////        }
//    	sparql = QueryFactory.create("ASK FROM <http://test1> WHERE { <http://aa> <http://bb> ?y }");
//		vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
//
//		boolean res = vqe.execAsk();
//                System.out.println("\nASK results: "+res);
//
//        System.out.println("\nexecute: DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }");
//        str = "DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }";
//        vur = VirtuosoUpdateFactory.create(str, set);
//        vur.exec();
//
//        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
//        vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
//        results = vqe.execSelect();
//        while (results.hasNext())
//        {
//            QuerySolution rs = results.nextSolution();
//            RDFNode s = rs.get("s");
//            RDFNode p = rs.get("p");
//            RDFNode o = rs.get("o");
//            System.out.println(" { " + s + " " + p + " " + o + " . }");
//        }
//
//    }
    private boolean hasPath(RDFNode results)
    {
        //Do any of the o's correspond to category belonging to ENtity-category graph
        while (objects.hasNext())
        {
            QuerySolution rs = objects.nextSolution();
            RDFNode o = rs.get("o");
            if (o.equals(results))
            {
                return true;
            }
        }

        return false;
    }
}
