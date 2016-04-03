/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Ahmed
 */
public class WastedCollector {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    //run with start  end
    static PrintWriter out;
    static int start,
            end,
            currentFile,
            count,
            goodInfoboxCount,
            badInfoboxCount,
            contentCount,
            redirectCount,
            exceptionCount,
            id;

    static String PATH = "H:\\DBpedia DataSet\\Stage1_Articles\\",
            REDIRECT = "#REDIRECT",
            INFOBOX,
            ARTICLES,
            CONTENT,
            REDIRECTS,
            WASTED,
            SEPERATOR = " ::: ",
            INFOBOX_OPEN = "{{Infobox"; //Except this the rest are Path to files

    static BufferedWriter wastedWriter;

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception
    {
        Scanner r = new Scanner(System.in);
        System.out.println("Enter Process Id: ");
        id = r.nextInt();
        System.out.println("Enter Start file name: ");
        start = r.nextInt();
        System.out.println("Enter End file name: ");
        end = r.nextInt();
        out = new PrintWriter(new File(PATH + "OutputLogger_" + id + ".log"));
        out.println("Job No: "+id+" Start: "+start+" End: "+end+" TIME: "+LocalDateTime.now());
        INFOBOX = PATH + "infobox_" + id + "\\";
        ARTICLES = PATH + "Stage1_Articles\\";
        CONTENT = PATH + "content_" + id + "\\";
        WASTED= PATH + "wasted_" + id + "\\";
        REDIRECTS = PATH + "redirect_" + id + "\\";
        currentFile = start;

        File dir = new File(PATH + "content_" + id);
        dir.mkdir();
        dir = new File(PATH + "infobox_" + id);
        dir.mkdir();
        dir = new File(PATH + "redirect_" + id);
        dir.mkdir();
        dir = new File(PATH + "wasted_" + id);
        dir.mkdir();

        wastedWriter = new BufferedWriter(new FileWriter(WASTED + currentFile));
        while (currentFile <= end)
        {
            try
            {
                split();
            } catch (Exception e)
            {
                exceptionCount++;
                currentFile++;
                out.println("Unknown Exception on file: " + currentFile + " " + e.getStackTrace());
                try{
                    wastedWriter.write(new String(Files.readAllBytes(Paths.get(ARTICLES+currentFile))));
                }
                catch(Exception e1)
                {
                    System.out.println("Stupid Wasted error");
                    e1.printStackTrace();
                }
                e.printStackTrace();
                
                out.println("============**IGNORED**============");
                out.flush();
                wastedWriter.flush();
            }
        }
        out.println("=========/n=========/n  REPORT/n=========/n=========/n "
                + "Total Okays: " + count
                + " Excpetions: " + exceptionCount
                + " Good Infobox: " + goodInfoboxCount
                + " Bad Infobox: " + badInfoboxCount
                + " Content: " + contentCount
                + " Redirects: " + redirectCount
        );
        out.close();
    }

    public static void split() throws FileNotFoundException, IOException
    {

        final int MAX_CHAR_PER_FILE = 67100000; //actually +800

        BufferedReader file;
        String fileName = "";
        int infoboxChars = 0,
                redirectChars = 0,
                contentChars = 0;

        for (; currentFile <= end; currentFile++)
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
                redirectCount++;
                //It is a redirect File
                //public FileWriter(File file,boolean append)throws IOException
                String targetName = contentLine.substring(
                        contentLine.indexOf("[[") + 2,
                        contentLine.lastIndexOf("]]"));

                if (redirectChars + idLine.length() >= MAX_CHAR_PER_FILE)
                {
                    //redirectWriter.close();
                    //redirectWriter = new BufferedWriter(new FileWriter(REDIRECTS + currentFile, true));
                    redirectChars = 0;
                }

                try
                {
                    //redirectWriter.write(idLine + SEPERATOR + targetName + '\n');
                    //redirectChars += idLine.length() + 2;
                    //redirectWriter.flush();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    wastedWriter.write(new String(Files.readAllBytes(Paths.get(ARTICLES+currentFile))));
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
                        badInfoboxCount++;
                        out.println("CRITICAL ERROR!! Malformed Infobox Brackets\nArticle: " + currentFile + " Infobox Start: " + infoboxStart);
                        wastedWriter.write(new String(Files.readAllBytes(Paths.get(ARTICLES+currentFile))));
                        System.out.println("---Skipped Article---"+currentFile);
                        continue;
                    }
                    //Store infobox in seperate folder
                    if (infoboxChars + (lastInfoboxEnd - infoboxStart + idLine.length()) >= MAX_CHAR_PER_FILE)
                    {
                        infoboxChars = 0;

                        contentChars = 0;
                    }
                    try
                    {
                        goodInfoboxCount++;
                        infoboxChars += lastInfoboxEnd - infoboxStart + idLine.length();
        
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        out.println("ERROR!! Writing INFOBOX. Skipping File: " + currentFile);
                        wastedWriter.write(new String(Files.readAllBytes(Paths.get(ARTICLES+currentFile))));
                        continue;
                    }
                }

                //Store Content
                if (contentChars + (contentLine.length() - (lastInfoboxEnd - infoboxStart) + idLine.length()) >= MAX_CHAR_PER_FILE)
                {
                    contentChars = 0;

                    infoboxChars = 0;
                }
                try
                {
                    contentCount++;
        //            contentWriter.write(idLine + SEPERATOR);
                    /*
                     output.write(contentLine.substring(0, infoboxStart<0?0:infoboxStart) + contentLine.substring(lastInfoboxEnd, contentLine.length()));
                     Omit the 1st substring part cause it mostly contains Disambiguations
                     */
          //          contentWriter.write(contentLine.substring(lastInfoboxEnd, contentLine.length()) + '\n');
                    contentChars += (contentLine.length() - (lastInfoboxEnd - infoboxStart) + idLine.length());
            //        contentWriter.flush();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    out.println("ERROR!! Writing CONTENT. Skipping File: " + currentFile);
                    wastedWriter.write(Files.readAllBytes(Paths.get(ARTICLES+currentFile)).toString());
                    continue;
                }
            }
            //out.println("OK  " + currentFile);
            count++;
            if (count % 1000 == 0)
            {
                System.out.println(count + " Files Done. Now at: " + currentFile);
            }

        }
        wastedWriter.close();
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