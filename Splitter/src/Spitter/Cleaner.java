//Eat everything together - Gluttony
//Flurionses
package Spitter;

class Cleaner {

    public static Node clean(String tuple)
    {
        String token[] = makeSplit(tuple);
        Node node = new Node(clear(token[0]), clear(token[1]), clear(token[2]));
        return node;
    }

    public static String[] makeSplit(String tuple)
    {
        int start, end;
        if ((start = tuple.indexOf('\"')) != -1)
        {
            String a, leftS, rightS, midS = "";//,b,c;
            end = tuple.indexOf('\"', start + 1);
            a = tuple.substring(start + 1, end);
            int left = countSpaces((leftS = tuple.substring(0, start)));
            int right = countSpaces(rightS = tuple.substring(end, tuple.length())) - 1;

            if (left == 1)
            {
                leftS = (leftS.split(" "))[0];
                rightS = (tuple.substring(end + 1, tuple.length()).split(" "))[1];
                midS = a;
            } else if (left == 2)
            {
                String arr[] = (leftS.split(" "));
                leftS = arr[0];
                midS = arr[1];
                rightS = a;
            } else
            {
                System.out.println("Critical Error: " + tuple);
            }
            return new String[]
            {
                leftS, midS, rightS
            };
        } else
        {
            return tuple.split(" ");
        }

    }
    static String clear(String url)
    {
        int start=url.lastIndexOf('/');
        if(start==-1)
            return url;
        return url.substring(start+1, url.length()-1);
    }
    private static String clearIfValue(String string)
    {
        int end = string.length() - 1;
        int start = string.indexOf("\"") + 1;
        if (start == 0)
        {
            start = string.lastIndexOf('/') + 1;

        } else
        {
            end = string.lastIndexOf("\"");
        }
        return string.substring(start, end);
    }

    private static int countSpaces(String substring)
    {
        int count = 0;
        for (int i = 0; i < substring.length(); i++)
        {
            if (substring.charAt(i) == ' ')
            {
                count++;
            }
        }
        return count;
    }
}
