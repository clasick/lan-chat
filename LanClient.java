import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.applet.*;
import javax.swing.*;
import javax.swing.AbstractListModel;
import java.util.Vector;

public class LanClient {

    static String ipAddress = null;
    static int port;
    static String userName = null;
    static  BufferedReader in;
    static PrintWriter out;
    static JFrame frame = new JFrame("LanChat Client");
    static JTextField textField = new JTextField(65);
    static JTextArea messageArea = new JTextArea(15, 40); 
    static DefaultListModel<String> listModel = new DefaultListModel<>();
    static JList<String> userList = new JList<>(listModel);
    static JFrame settingsPage = new JFrame("Client Settings");
    static JTextField hostName = new JTextField(30);
    static JTextField portNo = new JTextField(30);
    static JTextField myUserName = new JTextField(30);
    static JButton sendInfo = new JButton("JOIN");

    public static void clientPage() {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        Panel panel3 = new Panel();     

        panel1.add(textField, "South");
        messageArea.setEditable(false);
        textField.setEditable(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(600,250));
        panel2.add(messageScroll, "Center");
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150,250));
        panel3.add(userScroll);

        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel2, panel3);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane1, panel1);
        frame.getContentPane().add(splitPane2);
        frame.pack();

        textField.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }


 public static void showSettings()  {
        settingsPage.setVisible(true);
        settingsPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel3 = new Panel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.add(new JLabel("Host IP"));
        panel3.add(hostName);
        panel3.add(new JLabel("Port Number"));
        panel3.add(portNo);
        panel3.add(new JLabel("User Name"));
        panel3.add(myUserName);
        panel3.add(sendInfo,"CENTER");
        settingsPage.getContentPane().add(panel3);
        settingsPage.pack();


           sendInfo.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                   if(hostName.getText()==null || portNo.getText() == null || myUserName.getText() == null) 
                          return;
            else {
                   LanClient.ipAddress = hostName.getText();
                   LanClient.port = Integer.parseInt(portNo.getText());
                   LanClient.userName = myUserName.getText();
                  settingsPage.setVisible(false);
                  settingsPage.dispose();
                  clientPage();
                   frame.setSize(800,335);
                 ClientListener listen = new ClientListener();
                   listen.start();


            }
            
          }
        });
    }
    

    public static void main(String[] args) throws Exception {
        showSettings();
        settingsPage.setSize(200,200);

    }
}

class ClientListener extends Thread {

    public void run() {
        try {
          Socket socket = new Socket(LanClient.ipAddress, LanClient.port);

        LanClient.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        LanClient.out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = LanClient.in.readLine();
            if (line.startsWith("ASK")) {
                LanClient.out.println(LanClient.userName);
            } else if (line.startsWith("WELCOME")) {
                LanClient.messageArea.append("Welcome to the chat, " + LanClient.userName +"!\n");
                LanClient.textField.setEditable(true);
            } 
            else if(line.startsWith("NEWUSER")) {
                    LanClient.messageArea.append("<" + line.substring(7) + ">" + " has joined the chat! Say hi to them.\n");
                        LanClient.messageArea.setCaretPosition(LanClient.messageArea.getDocument().getLength());

                    LanClient.listModel.addElement(line.substring(7));
            }
             else if(line.startsWith("PREVIOUS")) {
                    LanClient.listModel.addElement(line.substring(8));
            }


            else if(line.startsWith("REMOVE")) {
                    LanClient.listModel.removeElement(line.substring(6));
                    LanClient.messageArea.append("<" + line.substring(6) + ">" + " has left the chat!\n");
                    LanClient.messageArea.setCaretPosition(LanClient.messageArea.getDocument().getLength());

            }
            else if (line.startsWith("MSG")) {
                LanClient.messageArea.append(line.substring(3) + "\n");
                LanClient.messageArea.setCaretPosition(LanClient.messageArea.getDocument().getLength());
            }
        }
         }
      catch(Exception e1) {
        
      }
    }

}