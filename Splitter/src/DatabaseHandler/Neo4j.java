package DatabaseHandler;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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
public class Neo4j {

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
        try (Transaction tx = graphDb.beginTx())
        {

            Node node = graphDb.createNode(Entities.Entity);
            node.setProperty("Name", name);
            return node;
        }
    }

    public void addEdge(Node node1, Node node2)
    {
        try (Transaction tx = graphDb.beginTx())
        {
            Relationship relationship = node1.createRelationshipTo(node2, Entities.Predicate);
            relationship.setProperty("count", 1);
            
        }
    }
    public void updateEdge(String subj)
    {
        try (Transaction tx = graphDb.beginTx())
        {
            Node node1=graphDb.findNode(Entities.Entity,"Name",subj);
            graphDb.findNodes(Entities.Entity,"Name",subj);
            //get relationship between two nodes
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
