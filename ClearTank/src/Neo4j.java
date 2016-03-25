
import java.util.HashMap;
import java.util.Map;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


/*
Efficiency is what you will love
create index on:
CREATE INDEX ON :User(username)
CREATE INDEX ON :Role(name)

To create relationships you might use:
MATCH (u:User {username:'admin'}), (r:Role {name:'ROLE_WEB_USER'})
CREATE (u)-[:HAS_ROLE]->(r)
*/
class Neo4j {

    GraphDatabaseService graphDb;
    final String DB_PATH = "";

    private static void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        });
    }

    public Node addNode(String name)
    {
        Node result = null;
        ResourceIterator<Node> resultIterator = null;
        try (Transaction tx = graphDb.beginTx())
        {
            String queryString = "MATCH (u:User {username:'admin'}), (r:Role {name:'ROLE_WEB_USER'})\n" + "CREATE (u)-[:HAS_ROLE]->(r)";
            Map<String, Object> parameters = new HashMap<>();
            
            resultIterator = graphDb.execute(queryString, parameters).columnAs("n");
            result = resultIterator.next();
            tx.success();
            return result;
        }
    }

    public Node addEdge(String subject,String predicate,String object)
    {
        Node result = null;
        ResourceIterator<Node> resultIterator = null;
        try (Transaction tx = graphDb.beginTx())
        {
            String queryString = "MERGE (n:"+name+") RETURN n";
            Map<String, Object> parameters = new HashMap<>();
            
            resultIterator = graphDb.execute(queryString, parameters).columnAs("n");
            result = resultIterator.next();
            tx.success();
            return result;
        }
    }
    
    public void addConstraint(String label)
    {
        try (Transaction tx = graphDb.beginTx())
        {
            graphDb.schema()
                    .constraintFor(DynamicLabel.label(label))
                    .assertPropertyIsUnique("rank")
                    .create();
            tx.success();
        }
    }

    public Neo4j()
    {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }

}
