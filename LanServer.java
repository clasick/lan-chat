import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;  
import java.io.*;  
import java.util.Date;

public class LanServer {

    public static JFrame frame = new JFrame("LanChat Server");
    public static JTextArea messageArea = new JTextArea(15, 40); 
    public static JButton startServer = new JButton("Start SERVER");
    public static JButton stopServer = new JButton("Stop SERVER");
    public static ServerSocket server;
    public static JFrame settingsPage = new JFrame("Server Settings");
    public static JTextField portField = new JTextField(10);
    public static JButton setPort = new JButton("Set Port No");
    public static JButton saveLog = new JButton("Save Server Log");
    public static int port;
    public static Vector<String> users = new Vector<String>(); 
    public static Vector<PrintWriter> sockets = new Vector<PrintWriter>();
    public static ServerThread s1;

    public static void serverPage() {
        frame.setSize(800,335);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.add(new JLabel("Server Log"));
        panel1.add(messageArea);
        panel2.add(new JLabel("Server Controls"));
        panel2.add(startServer);
        panel2.add(stopServer);
        panel2.add(saveLog);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel1,panel2);
        frame.getContentPane().add(splitPane1);
        frame.pack();

       startServer.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                try {
                        s1 = new ServerThread();
                        s1.start();
                     }

                 catch(Exception e1){
                     messageArea.append("Server startup failed.\n");

                 }
            }
        });

         stopServer.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                try {
                    s1.interrupt();
                    messageArea.append("Server was stopped.\n");
                     }
                 catch(Exception e1){
                     messageArea.append("Server shutdown failed.\n");
                 }
            }
        });

           saveLog.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                try {
                      createLog();
                      messageArea.append("Log Creation success.\n");

                     }
                 catch(Exception e1){
                     messageArea.append("Log Creation failed.\n");
                 }
            }
        });




    }

    public static void createLog() {
       try{
      File file =new File("server_log.txt");

      if(!file.exists()){
         file.createNewFile();
      }

      FileWriter fw = new FileWriter(file,true);
      BufferedWriter bw = new BufferedWriter(fw);
      Date date = new Date();

      bw.write("#####################################SERVER LOG AT " + date.toString() + " #####################################\n");
      bw.write(messageArea.getText());
      bw.close();

      }catch(IOException ioe){
         System.out.println("Exception occurred:");
       ioe.printStackTrace();
       }


    }


    public static void showSettings()  {
        settingsPage.setVisible(true);
        settingsPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel3 = new Panel();
        panel3.add(portField);
        panel3.add(setPort);
        settingsPage.getContentPane().add(panel3);
        settingsPage.pack();


           setPort.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                   if(portField.getText()==null) 
                          return;
            else {
                port = Integer.parseInt(portField.getText());
                  settingsPage.setVisible(false);
                  settingsPage.dispose();
                  serverPage();
            }
            
          }
        });
    }
    


    public static void main(String[] args) throws Exception {
      LanServer.showSettings();
      settingsPage.setSize(210,100);

    }
}

class ServerThread extends Thread {  
  public void run() {
    try {
         LanServer.server = new ServerSocket(LanServer.port);
       }
       catch(Exception e) {

       }
        LanServer.messageArea.append("Server has been started!\n");
        
        try {
            while (true) {
                new LanServerThread(LanServer.server.accept()).start();
            }
        } 
        catch(IOException e1) {
            //messageArea.append("There was an error in creating the server..." + e1.getMessage());
        }
        finally {
          try{
            LanServer.server.close();

          }
          catch(Exception e) {

          }
        }

    }
  }