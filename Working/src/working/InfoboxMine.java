package working;
public class InfoboxMine {

    private static final String DATA = "http://www.w3.org/",
            PROPER = "http://dbpedia.org/resource/",
            ENGLISH = "@en";

    public static Node perfectCut(String tuple)
    {
        /*
         Assumes the tuple format of SPO (space seperated and each entity surrounded by <>) and S & P are always in PROPER format
         */
        int end = tuple.indexOf('>');
        int start = tuple.lastIndexOf("/", end);
        String subject = tuple.substring(start + 1, end);

        String po = tuple.substring(end + 1, tuple.length());

        end = po.indexOf('>');
        start = po.lastIndexOf("/", end);
        String predicate = po.substring(start + 1, end);

        String o = po.substring(end + 1, po.length());
        String object;
        if (o.indexOf(DATA) != -1 || o.indexOf(ENGLISH) != -1)
        {
            start = o.indexOf('"');
            end = o.indexOf('"', start+1);
            object = o.substring(start, end + 1);
        } else if (o.indexOf(PROPER) != -1)
        {

            end = o.indexOf('>');
            start = o.lastIndexOf("/", end);
            object = o.substring(start + 1, end);
        } else
        {
            //System.out.println("Critical Error!!!\n tuple: " + tuple);
            return null;
        }
        //subject=spacer(subject);
        object=spacer(object);
        
        return new Node(subject, predicate, object);
    }
    static String spacer(String s)
    {
        return s.replace('_', ' ');
    }
    public static void main(String ar[])
    {
        System.out.println(perfectCut("<http://dbpedia.org/resource/American_National_Standards_Institute> <http://xmlns.com/foaf/0.1/homepage> <http://www.ansi.org/> ."));
    }
}
