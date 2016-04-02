/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Spitter;

import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Ahmed
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Scanner r =new Scanner(System.in);
                String s=r.nextLine();
                System.out.println(infoboxEndFinder(s.lastIndexOf("{{Infobox"), s)+"");
        // TODO code application logic here
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
