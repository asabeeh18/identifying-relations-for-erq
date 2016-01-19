/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Spitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
                INFOBOX = "infobox/",
                ARTICLES = "Stage1_Articles/",
                CONTENT = "content/",
                REDIRECTS = "redirect/",
                INFOBOX_OPEN = "{{Infobox";
        //BufferedWriter output;
        // TODO code application logic here
        int start = 104851;//Integer.parseInt(args[0]);
        int end = Integer.MAX_VALUE;
        if (args.length > 1)
        {
            end = Integer.parseInt(args[0]);
        }
        BufferedReader file;
        for (int currentFile = start; currentFile <= end; currentFile++)
        {
            try
            {
                file = new BufferedReader(new FileReader(ARTICLES + currentFile + ""));
            } catch (Exception e)
            {
                System.out.println("NOT FOUND");
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
                //OOPS! It was a redirect File
                //public FileWriter(File file,boolean append)throws IOException
                String targetName = contentLine.substring(
                        contentLine.indexOf("[[") + 2,
                        contentLine.length() - 2);
                try (BufferedWriter output = new BufferedWriter(new FileWriter(REDIRECTS + targetName, true)))
                {
                    output.write(idLine);
                    output.close();
                }
            } else
            {
                //YIPEE good Content
                int infoboxStart = contentLine.indexOf(INFOBOX_OPEN);
                int lastInfoboxStart = 0;
                int lastInfoboxEnd = 0;
                if (infoboxStart >= 0)
                {
                    lastInfoboxStart = contentLine.lastIndexOf(INFOBOX_OPEN);
                    lastInfoboxEnd = infoboxEndFinder(lastInfoboxStart, contentLine);
                    if (lastInfoboxEnd == -1)
                    {
                        System.out.println("Critical Error!! Malformed Infobox Brackets\nArticle: " + currentFile + " Infobox Start: " + infoboxStart);
                        System.out.println("---Skipped Article---");
                        continue;
                    }
                    //Store infobox in seperate folder
                    try (BufferedWriter output = new BufferedWriter(new FileWriter(INFOBOX + currentFile)))
                    {
                        output.write(idLine + "\n");
                        output.write(contentLine.substring(infoboxStart, lastInfoboxEnd));
                        output.close();
                    }
                }
                
                //Store Content
                try (BufferedWriter output = new BufferedWriter(new FileWriter(CONTENT + currentFile)))
                {
                    output.write(idLine + "\n");
                    /*
                            output.write(contentLine.substring(0, infoboxStart<0?0:infoboxStart) + contentLine.substring(lastInfoboxEnd, contentLine.length()));
                            Omit the 1st substring part cause it mostly contains Disambiguations
                    */
                    output.write(contentLine.substring(lastInfoboxEnd, contentLine.length()));
                    output.close();
                }
            }
            System.out.println(currentFile + ": ALL OK");
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
                    return i + 3+lastInfoboxStart;
                }
            }
        }
        return -1;
    }

}
