package Spitter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Node implements WritableComparable<Node> {
       // Some data
       public String subject;
       public String object;
       public String predicate;

    public Node(String s,String p,String o)
    {
        subject=s;
        predicate=p;
        object=o;
    }
       
       @Override
       public void write(DataOutput out) throws IOException {
         out.writeUTF(subject);
         out.writeUTF(object);
       }
       
       @Override
       public void readFields(DataInput in) throws IOException {
         subject = in.readUTF();
         object = in.readUTF();
       }
       
       @Override
       public int compareTo(Node o) {
    	   if(subject.compareTo(o.subject)==0)
    	   {
    		   if(object.compareTo(o.object)==0)
    		   {
    			   return 0;
    		   }
    		   return 1;
    	   }
    	   return 1;
       }
     }
 
