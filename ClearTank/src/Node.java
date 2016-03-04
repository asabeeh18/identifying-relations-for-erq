import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Node implements WritableComparable<Node> {
       // Some data
       String subject;
       String object;
       String predicate;
       Node(String subject,String predicate,String object)
       {
           subject=this.subject;
           object=this.object;
           predicate=this.predicate;
       }
       public void write(DataOutput out) throws IOException {
         out.writeUTF(subject);
         out.writeUTF(object);
       }
       
       public void readFields(DataInput in) throws IOException {
         subject = in.readUTF();
         object = in.readUTF();
       }
       
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
 
