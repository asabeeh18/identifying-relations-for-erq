/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package solar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Ahmed
 */
public class Solar {

    /**
     * @param args the command line arguments
     */
    static final String url="http://localhost:8983",
            PATH="path",
            CONTENT="content";
    static String FILES_DIRECTORY;
    SolrClient solr;
    void server()
    {
        solr=new HttpSolrClient(url); //change to CloudSolrClient
        
    }   
    void query()
    {
        SolrQuery query=new SolrQuery();
        query.setQuery("df");
    }
    void addDoc(String path,String content) throws SolrServerException, IOException
    {
        SolrInputDocument doc=new SolrInputDocument();
        doc.addField("Path", path);
        doc.addField("Content", content);
        UpdateResponse res=solr.add(doc);
    }
    void indexFolder() throws IOException, SolrServerException
    {
        File dir = new File(FILES_DIRECTORY);
        File[] files = dir.listFiles();
        System.out.println("Total Count: " + files.length);
        int i = 0;
        for (File file : files)
        {
            String path = file.getCanonicalPath();

                BufferedReader line = new BufferedReader(new FileReader(file));
                String content;
                while ((content = line.readLine()) != null)
                {
                    addDoc(path,content);
                    System.out.println("Current count: " + i + " File " + file.getName());
                    i++;                  
                }
        }
        solr.commit();
    }
    public static void main(String[] args) throws IOException, SolrServerException
    {
        // TODO code application logic here
        Scanner r=new Scanner(System.in);
        FILES_DIRECTORY= r.nextLine();
        Solar sr=new Solar();
        sr.server();
        sr.indexFolder();
        System.out.println("Completed");
    }
    
}
