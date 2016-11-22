import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Vector;

public  class LanServerThread extends Thread {
    
        private String user;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;



        public LanServerThread(Socket s) {
            socket = s;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            
                    out.println("ASK");
                    user = in.readLine();
                    LanServer.users.add(user);
                    LanServer.messageArea.append(user + " has joined the chat!\n");
                 
                    LanServer.messageArea.append("There are now " + LanServer.users.size() + " users in the chat.\n");
                   

       
                out.println("WELCOME");

                for(String n: LanServer.users) {
                                out.println("PREVIOUS" + n);
                }


                for (PrintWriter socket : LanServer.sockets) {
                        socket.println("NEWUSER" + user);
                }

                LanServer.sockets.add(out);


                while (true) {
                    String msg = in.readLine();
                    if (msg == null) {
                        return;
                    }
                    for (PrintWriter socket : LanServer.sockets) {
                        socket.println("MSG <" + user + "> " + msg);
                    }

                }
            } 

              catch (IOException e1) {
                LanServer.messageArea.append("There was an error in creating a server thread...." + e1.getMessage());
            } finally {
                LanServer.messageArea.append(user + " left the chat!\n");
                
              



                for (PrintWriter socket : LanServer.sockets) {
                        socket.println("REMOVE" + user);
                 }
                    LanServer.users.remove(user);
                    LanServer.sockets.remove(out);
                    if(LanServer.users.size()!=0) {
                       LanServer.messageArea.append("There are now " + LanServer.users.size() + " users in the chat.\n");
                     


                   }

                   else {
                        LanServer.messageArea.append("Chat is now empty.\n");
                       

                    }


            } 
        }
    }