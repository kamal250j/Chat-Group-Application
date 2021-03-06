
package chatGroup;

import com.mongodb.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client implements Runnable,ActionListener{

   public JLabel lbl,lbl2;
   public JTextField tbx,tbx2,tbx3; 
   public JTextArea ta;
   public JPanel pnl1,pnl2,pnl3;
   public JButton btn,btn2;
   public JFrame f;
   public String s;
   public int i = 0;
   public String name = "";
   public JScrollPane pane = null;

       public static BufferedReader br = null;
       public static Socket socket = null;
       public static DataOutputStream out = null;
       public static Boolean isConnected = false;
       
    public static MongoClient mongoClient = null;
    public static DBCollection coll = null;

   public void getInterface(){

       f = new JFrame("Client"); 
     //  f.setLayout(new BoxLayout(f,BoxLayout.Y_AXIS));
     f.setLayout(new FlowLayout());
       f.setResizable(false);
       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       lbl = new JLabel("Enter IP Address ");
       tbx = new JTextField("localHost",12);     // for ip address
       lbl2 = new JLabel("Enter your name ");
       tbx2 = new JTextField("Enter Name",12);   // for writing message
       btn = new JButton("connect");
       btn.addActionListener(this);
       ta = new JTextArea(" ",12,30);    // for reading incoming messages
       ta.setEditable(false);
       tbx3 = new JTextField("Enter Text",24);
       btn2 = new JButton("Send");
       btn2.addActionListener(this);
       
       pnl1 = new JPanel();
       pnl2 = new JPanel();
       pnl3 = new JPanel();
       pnl1.setLayout(new FlowLayout(FlowLayout.CENTER,40,5));
       pnl2.setLayout(new FlowLayout(FlowLayout.CENTER,40,5));
     //  pnl3.setLayout(new FlowLayout(FlowLayout.CENTER,40,5));
       pane = new JScrollPane(ta);
       
 
        pnl1.add(lbl);
        pnl1.add(tbx);
        pnl2.add(lbl2);
        pnl2.add(tbx2);
        pnl3.add(tbx3);
        pnl3.add(btn2);
        f.add(pnl1);
        f.add(pnl2);
        f.add(btn);
        f.add(pane);
        f.add(pnl3);

        f.setSize(400,400);
        f.setVisible(true);

   }

   @Override
   public void actionPerformed(ActionEvent e){
       if(e.getSource() == btn){
          s = tbx.getText();
          name = tbx2.getText();
          createConnection(s);
          sendString(name);
       }
       if(e.getSource() == btn2){
          if(isConnected){
              s = tbx3.getText();
              tbx3.setText("");
              sendString(s);
          }
       }
   }
   
   static void createConnection(String s){
        try{
            if(s != null){
               socket = new Socket(s,4000);  
               isConnected = true;
               System.out.println("Connection has been established "); 
            }
        }catch(IOException o){
               System.out.println("Input Output Error " + o);
          } 
   }

   public void sendString(String s){
       try{
          if(name != ""){
            mongoClient = new MongoClient("localHost",27017);
            DB db = mongoClient.getDB("networking");
            coll = db.getCollection("GuiChat");
            BasicDBObject document = new BasicDBObject();
            document.put(name,s);
            coll.insert(document);
            mongoClient.close();
          }
       }catch(IOException f){
           System.out.println("mongoDB connection error");
       }
       
       try{
            out = new DataOutputStream(socket.getOutputStream());
           if(s != null){
               out.writeBytes(s + "\n");
               out.flush();
           }
       }catch(IOException g){
           System.out.println("Error " + g);
       }
   }



   public static void main(String []args) throws IOException{
       Client obj = new Client();
       obj.getInterface();
   //    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       
       while(isConnected == false);
       Thread t = new Thread(obj);
       t.start();
   /*    String sendingMessage,receivingMessage;
       while(true){
          sendingMessage = brSys.readLine();
          if(sendingMessage != null){
             out.writeBytes(sendingMessage + "\n");
             out.flush();
          }
       }
   */
  }

  @Override
  public void run(){
    try{
         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      while(true){
         String receivingMessage = br.readLine();
      //   System.out.println(receivingMessage );
          
          // ta.setText(receivingMessage);
          if(receivingMessage != null)
             ta.append(receivingMessage + "\n");
          
      }
    }
    catch(IOException e){
          System.out.println("Exception " + e);
    }
  }
}