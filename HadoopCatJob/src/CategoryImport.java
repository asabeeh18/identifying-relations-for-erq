
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

class CategoryImport {

    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        BufferedReader file = new BufferedReader(new FileReader("H:\\NBProject\\identifying-relations-for-erq\\Splitter\\build\\classes\\keyValue.txt"));
        String[] line = file.readLine().split(",");
        HashMap<String, Node> tuple = new HashMap<>();
        Node node=new Node(line[0],line[1],line[2]);
        tuple.put(line[0],node );
        
    }
}
