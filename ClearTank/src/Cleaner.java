class Cleaner {
    public  static Node clean(String tuple)
    {
        String token[]=tuple.split(" ");
        Node node=new Node(clear(token[0]),clear(token[1]),clearIfValue(token[2]));
        return node;
    }
    static String clear(String url)
    {
        int start=url.lastIndexOf('/');
        return url.substring(start, url.length());
    }

    private static String clearIfValue(String string)
    {
        int end=string.length();
        int start=string.indexOf("\"");
        if(start==-1)
        {
            start=string.lastIndexOf('/');
        }
        else
        {
            end=string.lastIndexOf("\"");
        }
        return string.substring(start, end);
    }
}
