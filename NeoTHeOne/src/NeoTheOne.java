/*
 * To change- this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author Ahmed
 */
public class NeoTheOne {

    GraphDatabaseService db;
    private final String DB_PATH="/home/user/Documents/test1.db";
    
   
    public NeoTheOne()
    {
        if(db==null)
        {
            GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
            db = dbFactory.newEmbeddedDatabase(DB_PATH);
            registerShutdownHook(db);
        }
    }

    /**
     * @param fargs the command line arguments
     */
    Node createNode(String subject)
    {
        Node newNode = null;
        try (Transaction tx = db.beginTx())
        {
            newNode = db.createNode(Entity.CATEGORY);
            try
            {
                newNode.setProperty("Name", subject);
            } 
            catch (org.neo4j.graphdb.ConstraintViolationException e)
            {
                return getNode(subject);
            }

            tx.success();
        }
        return newNode;
    }

    void uniqueNode()
    {
        try (Transaction tx = db.beginTx())
        {
            db.schema()
                    .constraintFor(Entity.CATEGORY)
                    .assertPropertyIsUnique("Name")
                    .create();
            tx.success();
        }
    }

    public Relationship createEdge(Node node1, Node node2, String reln)
    {
        Relationship relationship;
        try (Transaction tx = db.beginTx())
        {
            relationship = node1.createRelationshipTo(node2, Entity.PREDICATE);
            relationship.setProperty("Predicate", reln);
            tx.success();
        }
        return relationship;
    }

    Node getNode(String subject)
    {
        Node node;
        try (Transaction tx = db.beginTx())
        {
            node = db.findNode(Entity.CATEGORY, "Name", subject);
            tx.success();
        }
        return node;
    }

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
    public void shutdown()
    {
        db.shutdown();
    }
    public void testBlock(Node a,Node b)
    {
        try (Transaction tx = db.beginTx())
        {
            Relationship rel=a.getSingleRelationship(Entity.CATEGORY, Direction.OUTGOING);
            System.out.println("testBlock: "+rel.getProperty("Predicate"));
            tx.success();
        }
    }
    public static void main(String[] args)
    {
        NeoTheOne neo = null;
        Node node1,node2 = null;

        try
        {
            neo = new NeoTheOne();
            //neo.uniqueNode();
            node1 = neo.createNode("abc1");
            node2 = neo.createNode("abc");
            System.out.println("n1: " + node1+"n2: "+node2);
            
            Relationship rel=neo.createEdge(node1, node2,"Prop");
            //System.out.println(rel.getProperty("Pred"));
            //fassert rel.getNodes()[0].equals(node1);
            neo.testBlock(node1,node2);
            //System.out.println(rel);
        } catch (Exception e)
        {
            System.out.println("Caught");
            e.printStackTrace();
        }
        neo.shutdown();
        System.out.println("Off");
        
    }

}
