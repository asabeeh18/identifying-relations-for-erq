
package LuceneWorks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Ahmed
 */
public class Sentences {
    
    static double TOTAL_TUPLES=32143159.0;
    
    private String 
            
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Scanner r=new Scanner(System.in);
        System.out.println("Enter Infobox File Path");
        final String File_Path=r.nextLine();
        BufferedReader br=new BufferedReader(new FileReader(new File(File_Path)));
        
        int i=0;
        String line;
        while((line=br.readLine())!=null)
        {
            System.out.println(i+" tuples Processed, "+(i*100.0/TOTAL_TUPLES)+"% done.");
            
        }
    }
    
}
