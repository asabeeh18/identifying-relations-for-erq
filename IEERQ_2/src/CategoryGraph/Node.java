package CategoryGraph;

public class Node {

    // Some data
    public String subject;
    public String object;
    public String predicate;

    public Node(String s, String p, String o)
    {
        subject = s;
        predicate = p;
        object = o;
    }

    @Override
    public String toString()
    {
        return subject+"--"+predicate+"--"+object;
    }
}
