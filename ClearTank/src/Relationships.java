
import org.apache.hadoop.io.Text;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.Queue;

import virtuoso.jena.driver.*;

class Relationships {

    private static final int MAX_DEPTH = 7;
    String url = "jdbc:virtuoso://localhost:1111";
    VirtGraph connection;
    VirtuosoUpdateRequest vur;

    Relationships()
    {
        connection = new VirtGraph(url, "dba", "dba");
    }

    void classify(Node start, Iterable<Text> predicates)
    {
        toDbpedia(start);
        ResultSet results = runQuery("SELECT ?o FROM <categories> WHERE { " + start.subject + " ?p ?o }");
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode o = rs.get("o");
            dfs(o, 0);
        }
    }

    void dfs(RDFNode begin, int depth)
    {
        ResultSet results = runQuery("SELECT ?o FROM <category.categories> WHERE { " + begin + " ?p ?o }");
        if(depth==7)
            return;
        
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode o = rs.get("o");
            /*
            create edge between begin and o with predicate
            str = "INSERT INTO GRAPH <category.paths> {" +begin+" "+node.predicate+" "+o+"  }";
            vur = VirtuosoUpdateFactory.create(str, set);
            vur.exec();
            OR
            create edge between end terminals only
            INSERT INTO GRAPH <terminal.paths> 
            */
            dfs(o, depth+1);
        }
    }

    ResultSet runQuery(String query)
    {
        Query sparql = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, connection);

        return vqe.execSelect();
    }

    void toDbpedia(Node convert)
    {
        convert.subject = "http://dbpedia.org/resource/" + convert.subject;
        convert.object = "http://dbpedia.org/resource/" + convert.object;
        //return convert;
    }

    public static void main(String[] args)
    {
        Relationships r=new Relationships();
        Text a=new Text(),b=new Text();
        a.set("A");
        b.set("B");
        Node n=new Node("A","p","C");
        r.classify(n, null);
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
//        }
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
}
