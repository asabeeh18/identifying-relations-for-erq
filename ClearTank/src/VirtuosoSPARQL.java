
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.*;

class VirtuosoSPARQL {

    /**
     * Executes a SPARQL query against a virtuoso url and prints results.
     */
    public static void main(String[] args)
    {

        String url;
        if (args.length == 0)
        {
            url = "jdbc:virtuoso://localhost:1111";
        } else	
        {
            url = args[0];
        }

        /*			STEP 1			*/
        VirtGraph set = new VirtGraph(url, "dba", "dba");

        /*			STEP 2			*/
        System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
        String str = "CLEAR GRAPH <http://test1>";
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();

        System.out.println("\nexecute: INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc'. <aa1> <bb1> 123. }");
        str = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc'.<aa1> <bb1> 123. }";
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();

        /*			STEP 3			*/
        /*		Select all data in virtuoso	*/
        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
        Query sparql = QueryFactory.create("SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");

        /*			STEP 4			*/
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, set);

        ResultSet results = vqe.execSelect();
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            RDFNode j = rs.get("j");
            System.out.println(" { " + s + " " + p + " " + o + " "+j+" . }");
        }

        System.out.println("\nexecute: DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }");
        str = "DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }";
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();

        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
        vqe = VirtuosoQueryExecutionFactory.create(sparql, set);
        results = vqe.execSelect();
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            
            System.out.println(" { " + s + " " + p + " " + o + " . }");
        }
    }
}