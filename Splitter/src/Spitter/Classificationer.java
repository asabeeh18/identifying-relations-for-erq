package Spitter;


import org.apache.hadoop.io.Text;


import com.hp.hpl.jena.rdf.model.RDFNode;



class Classificationer {

	private static final int MAX_DEPTH = 7;
	
	void classify(Node start,Iterable<Text> predicates)
	{
		toDbpedia(start);
		Query sparql = QueryFactory.create("SELECT ?o FROM <categories> WHERE { "+start.subject+" ?p ?o }");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, connection);

        ResultSet results = vqe.execSelect();
        while (results.hasNext())
        {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            System.out.println(" { " + s + " " + p + " " + o + " . }");
            for(int i=0;i<MAX_DEPTH;i++)
            {
            	sparql = QueryFactory.create("SELECT ?o FROM <categories> WHERE { "+o+" ?p ?o }");
        		vqe = VirtuosoQueryExecutionFactory.create(sparql, connection);
        		results = vqe.execSelect();
                while (results.hasNext())
                {
                	
                }

            }
        }
	}
	void toDbpedia(Node convert)
	{
		convert.subject="http://dbpedia.org/resource/"+convert.subject;
		convert.object="http://dbpedia.org/resource/"+convert.object;
		//return convert;
	}
	public static void main(String[] args)
	{
		
	}
    public static void main1(String[] args)
    {
        /*			STEP 2			*/
        System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
        
        System.out.println("\nexecute: INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }");
        str = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }";
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
            System.out.println(" { " + s + " " + p + " " + o + " . }");
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