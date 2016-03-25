package Spitter;

class Cleaner {
    public  static Node clean(String tuple)
    {
        String token[]=tuple.split(" ");
        Node node=new Node(clearIfValue(token[0]),clearIfValue(token[1]),clearIfValue(token[2]));
        return node;
    }
    

    private static String clearIfValue(String string)
    {
        int end=string.length()-1;
        int start=string.indexOf("\"")+1;
        if(start==0)
        {
            start=string.lastIndexOf('/')+1;
            
        }
        else
        {
            end=string.lastIndexOf("\"");
        }
        return string.substring(start, end);
    }
}
