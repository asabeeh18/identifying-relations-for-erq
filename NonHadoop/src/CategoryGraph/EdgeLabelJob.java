package CategoryGraph;

import com.sun.prism.shader.FillCircle_ImagePattern_AlphaTest_Loader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class EdgeLabelJob {

    static BufferedWriter context;
    static String File_Path;
    public static void main(String[] args) throws Exception
    {
        double TOTAL_TUPLES=32143159.0;
        EdgeLabelMap mapper = new EdgeLabelMap();
        context = new BufferedWriter(new FileWriter(new File("CategoryEdges.csv")));
        Scanner r=new Scanner(System.in);
        File_Path = r.nextLine();
        BufferedReader br=new BufferedReader(new FileReader(new File(File_Path)));
        String line;
        String out;
        int i=0;
        while((line=br.readLine())!=null)
        {
            
            System.out.println(i+" tuples Processed, "+(i*100.0/TOTAL_TUPLES)+"% done.");
            out=mapper.map(line);
            if(out!=null)
                context.write(out);
            context.flush();
            i++;
        }
        context.close();
    }
}
