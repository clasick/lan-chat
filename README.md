# lan-chat
A Client-Server Chat Room written in basic Java

The application consists of two primary classes, the LanServer class and the LanClient class.
The LanServer class is run when the user wants to create a server to host his chat room.
Another user can then use the LanClient class to connect to the previously created chat room, by knowing the details of the host i.e., IP Address, port number.
Any number of users can join the created LanServer and once connected, they can send messages to the server, which is then relayed to all other LanClient instances that are currently connected to the server. 
The LanServer is created by inputing the desired port to host the chat room. The LanServer has the option to START or STOP the hosting of the chat room. It handles the interception of messages from the clients and relaying of messages. A feature to output the “server log” of the current session to a file is also present.
The LanClient and LanServer both have been enabled to close their connection when  their respective UI has been closed. 

When the LanServer class is run, and the port number is inputed into the interface, a thread called ServerThread is initialized. This ServerThread listens to a ServerSocket connected to the desired port and waits for any connections from a potential client.
Once a request has been made, this ServerSocket accepts this connection and sends it to the LanServerThread to handle output/input to the client Socket.
Therefore, for each Client that is connected to the Server, a LanServerThread exists which will handle the exchange of messages. This is done by prefixing keywords like “USER” and “MESSAGE” to the beginning of the transferred messages. 
The LanClient class waits for input from the user, when it receives this input, it sends it to the LanServer through the LanServerThread by prefixing it with the keyword “MESSAGE”.
In this way, the LanServer and LanClient operate to obtain the chat room. 
