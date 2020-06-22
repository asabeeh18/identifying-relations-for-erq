package Spitter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class Broker
{
    public static void main(String ar[]) throws FileNotFoundException, IOException
    {
        
        BufferedReader br = new BufferedReader(new FileReader(new File(LuceneConstants.INFOBOX)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.INFOBOX_SPLIT+"0")));
        String s=br.readLine();
        int part=17857310;
        int i=1;
        while(s!=null)
        {
            if(i%part==0)
            {
                bw.close();
                bw = new BufferedWriter(new FileWriter(new File(LuceneConstants.INFOBOX_SPLIT+(i/part))));
            }
            bw.write(s);
            i++;
            s=br.readLine();
        }
        bw.close();
    }
}