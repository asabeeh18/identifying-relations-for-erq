/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Spitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Ahmed
 */
public class Splitter {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    //run with start  end
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception
    {
        final String REDIRECT = "#REDIRECT",
                INFOBOX = "infobox\\",
                ARTICLES = "Stage1_Articles\\",
                CONTENT = "content\\",
                REDIRECTS = "redirect\\",
                SEPERATOR = " ::: ",
                INFOBOX_OPEN = "{{Infobox"; //Except this the rest are Path to files
        final int MAX_CHAR_PER_FILE = 67108000; //actually +800
        int start, end;

        Scanner r = new Scanner(System.in);
        PrintWriter out = new PrintWriter(new File("OutputLogger.log"));
        //BufferedWriter output;

        System.out.println("Enter Start file name: ");
        start = r.nextInt();
        System.out.println("Enter End file name: ");
        end = r.nextInt();

        BufferedReader file;
        String fileName = "";
        int infoboxChars = 0,
                redirectChars = 0,
                contentChars = 0,
                count = 0;
        BufferedWriter redirectWriter = new BufferedWriter(new FileWriter(REDIRECTS + start, true)),
                infoboxWriter = new BufferedWriter(new FileWriter(INFOBOX + start)),
                 contentWriter = new BufferedWriter(new FileWriter(CONTENT + start));
        
        for (int currentFile = start; currentFile <= end; currentFile++)
        {
            try
            {
                fileName = ARTICLES + currentFile + "";
                file = new BufferedReader(new FileReader(fileName));
            } catch (Exception e)
            {
                //out.println("NOT FOUND: " + fileName);
               // e.printStackTrace();
                continue;
            }

            String idLine = file.readLine();
            //number, NAME->So length of ID+1(comma)+1(space) the rest is file name
             /*
             Redirects will be stored seperately filename as the Target name content are 'ID Name' on each line
             Or maybe a triple ?.. Space wasteage :/
             */
            String contentLine = file.readLine();

            if (contentLine.substring(0, 9).equals(REDIRECT))
            {
                //It is a redirect File
                //public FileWriter(File file,boolean append)throws IOException
                String targetName = contentLine.substring(
                        contentLine.indexOf("[[") + 2,
                        contentLine.lastIndexOf("]]"));

                if (redirectChars + idLine.length() >= MAX_CHAR_PER_FILE)
                {
                    redirectWriter.close();
                    redirectWriter = new BufferedWriter(new FileWriter(REDIRECTS + currentFile, true));
                    redirectChars=0;
                }

                try
                {
                    redirectWriter.write(idLine+SEPERATOR+targetName + '\n');
                    redirectChars+=idLine.length()+2;
                } catch (Exception e)
                {
                    e.printStackTrace();
                    out.println("ERROR!! Writing REDIRECT Skipping File: " + currentFile);
                    continue;
                }
            } else
            {
                // good Content
                int infoboxStart = contentLine.indexOf(INFOBOX_OPEN);
                int lastInfoboxStart = 0;
                int lastInfoboxEnd = 0;
                if (infoboxStart >= 0)
                {
                    lastInfoboxStart = contentLine.lastIndexOf(INFOBOX_OPEN);
                    lastInfoboxEnd = infoboxEndFinder(lastInfoboxStart, contentLine);
                    if (lastInfoboxEnd == -1)
                    {
                        out.println("CRITICAL ERROR!! Malformed Infobox Brackets\nArticle: " + currentFile + " Infobox Start: " + infoboxStart);
                       // out.println("---Skipped Article---");
                        continue;
                    }
                    //Store infobox in seperate folder
                    if (infoboxChars + (lastInfoboxEnd-infoboxStart+idLine.length()) >= MAX_CHAR_PER_FILE)
                    {
                        infoboxWriter.close();
                        infoboxWriter = new BufferedWriter(new FileWriter(INFOBOX + currentFile, true));
                        infoboxChars=0;
                    }
                    try
                    {
                        infoboxWriter.write(idLine + SEPERATOR);
                        infoboxWriter.write(contentLine.substring(infoboxStart, lastInfoboxEnd) + '\n');
                        infoboxChars+=lastInfoboxEnd-infoboxStart+idLine.length();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        out.println("ERROR!! Writing INFOBOX. Skipping File: " + currentFile);
                        continue;
                    }
                }

                //Store Content
                if (contentChars + (contentLine.length()-(lastInfoboxEnd-infoboxStart)+idLine.length()) >= MAX_CHAR_PER_FILE)
                {
                        contentWriter.close();
                        contentWriter = new BufferedWriter(new FileWriter(CONTENT + currentFile, true));
                        contentChars=0;
                }
                try
                {
                    contentWriter.write(idLine + SEPERATOR);
                    /*
                     output.write(contentLine.substring(0, infoboxStart<0?0:infoboxStart) + contentLine.substring(lastInfoboxEnd, contentLine.length()));
                     Omit the 1st substring part cause it mostly contains Disambiguations
                     */
                    contentWriter.write(contentLine.substring(lastInfoboxEnd, contentLine.length()) + '\n');
                    contentChars+=(contentLine.length()-(lastInfoboxEnd-infoboxStart)+idLine.length());
                } catch (Exception e)
                {
                    e.printStackTrace();
                    out.println("ERROR!! Writing CONTENT. Skipping File: " + currentFile);
                    continue;
                }
            }
            //out.println("OK  " + currentFile);
            count++;
            if(count%5==0)
            {
                System.out.println(count+" Files Done. Now at: "+currentFile);
            }
        }
    }

    private static int infoboxEndFinder(int lastInfoboxStart, String contentLine)
    {
        Stack<Character> s = new Stack<>();
        s.push('{');
        s.push('{');
        char[] line = contentLine.substring(lastInfoboxStart + 2, contentLine.length()).toCharArray();
        for (int i = 0; i < line.length; i++)
        {
            if (line[i] == '{')
            {
                s.push('{');
            } else if (line[i] == '}')
            {
                s.pop();
                if (s.isEmpty())
                {
                    return i + 3 + lastInfoboxStart;
                }
            }
        }
        return -1;
    }

}
