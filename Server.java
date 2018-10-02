
package chatGroup;

import java.util.*;
import java.net.*;
import java.io.*;

public class Server extends Thread{

   public DataOutputStream outToServer = null;
   public static Socket socket = null;
   public static Vector<Server> threads = new Vector<Server>();
   public final static String sm = "Number of peoples connected ";
   
   public String name2;
   
   public Server(){
      
   }                

   public static void main(String []args) throws IOException{
       ServerSocket ss = new ServerSocket(4000);
       System.out.println(sm);
       while(true){
            socket = ss.accept();
            Server s =  new Server();
            threads.addElement(s);
            s.start();
       }
   }

   @Override
   public void run(){
     try{
       outToServer = new DataOutputStream(socket.getOutputStream());
       BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  //     String sendingString = "Enter your name ";
       String receivingString,name;
  //     outToServer.writeBytes(sendingString + "\n");
       outToServer.flush();
       name = br.readLine();
       name2 = name;
       System.out.println("   " + name);
       while(true){
          receivingString = br.readLine();
  //        System.out.println(receivingString);
  //         System.out.println("Thread size = " + threads.size());
            for(int i=0;i<threads.size();i++){
                   threads.get(i).outToServer.writeBytes(name + " : " + receivingString + "\n");
                   threads.get(i).outToServer.flush();
            }
       }
    }
    catch(IOException e){
         System.out.println(name2 + " left the group ");
      /*   for(int i=0;i<threads.size();i++){
                   threads.get(i).outToServer.writeBytes(name2 + " left the group " + "\n");
                   threads.get(i).outToServer.flush();
            }
*/
    }
   }
}