
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

class Virtuoso
{
    private static final int MAX_DEPTH = 7;
	String url = "jdbc:virtuoso://localhost:1111";
	VirtGraph connection;
	VirtuosoUpdateRequest vur;
    VirtuosoQueryExecution vqe;
    
	Virtuoso()
	{
		connection = new VirtGraph(url, "dba", "dba");
	}
	ResultSet runQuery(String query)
	{
        Query sparql = QueryFactory.create(query);
        vqe = VirtuosoQueryExecutionFactory.create(sparql, connection);
        return vqe.execSelect();
	}
    Node getResult(ResultSet results)
    {
        if (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            RDFNode j = rs.get("j");
            
            System.out.println(" { " + s + " " + p + " " + o + " "+j+" . }");
            return new Node(s.toString(),p.toString(),o.toString());
        }
        return null;
    }
}