package chatServer;

import clientHandler.ClientHandler;
import clientHandler.ClientThread;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 *
 * @author Kasper
 */
public class ChatServer {

    private ServerSocket serverSocket;
    private static int uniqueId; //could use id to keep track of users and delete with id when user logout.
    private static String IP = "localhost";
    private static int PORT = 8081;
    private ArrayList<ClientThread> ch;

    public ChatServer() {
        ch = new ArrayList<ClientThread>();

    }

    public void start() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(IP, PORT));
            //Waiting for client connections - infinite loop.
            while (true) {
                System.out.println("Waiting for clients to connect on port: " + PORT);
                Socket socket = serverSocket.accept();

                if (!true) {
                    break; //Midlertidig
                }

                ClientThread c = new ClientThread(socket);
                ch.add(c);
                c.start();

            }
            // When attempting to close the server. First close serverSocket. 
            // Then loop through clientHandlers in arraylist and close those threads.
            try {
                serverSocket.close();
                for (ClientThread c : ch) {
                    try {
                        c.getScan().close();
                        c.getPw().close();
                        c.getSocket().close();
                    } catch (Exception e) {
                        System.out.println("Problems closing the client sockets or threads");
                    }
                }
            } catch (Exception e) {
                System.out.println("Problems closing the server socket");
            }

        } catch (Exception e) {
            System.out.println("Problems initializing server socket");
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error while stopping server");
        }
    }

    public static void main(String[] args) {

        if (args.length == 2) {
            IP = args[0];
            PORT = Integer.parseInt(args[1]);
        }

        ChatServer server = new ChatServer();
        server.start();
    }

}
