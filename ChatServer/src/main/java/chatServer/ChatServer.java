package chatServer;

import java.util.*;
import java.net.*;
import java.io.*;

/**
 *
 * @author Kasper
 */
public class ChatServer {
    
    private ServerSocket serverSocket;
    private static String IP = "localhost";
    private static int PORT = 8081;
    private final List<ClientHandler> chlist = Collections.synchronizedList(new ArrayList());
    
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            IP = args[0];
            PORT = Integer.parseInt(args[1]);
        }
        ChatServer server = new ChatServer();
        server.start();
    }
    
    public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(IP, PORT));
        //Waiting for client connections - infinite loop.
        while (true) {
            Socket socket = serverSocket.accept(); //BLOCK
            new ClientHandler(this, socket).start();
        }
    }
    
    public void addClient(ClientHandler ch) {
        chlist.add(ch);
        sendClientList();
    }
    
    public void removeClient(ClientHandler ch) {
        chlist.remove(ch);
        sendClientList();
    }
    
    public void sendToOne(ClientHandler sender, String person, String msg) {
        ClientHandler reciever = null;
        for (ClientHandler ch : chlist) {
            if (ch.getUsername().equals(person)) {
                reciever = ch;
                reciever.printMsg("MSGRES:" + sender.getUsername() + ":" + msg);
            } else {
                reciever = sender;
                //sender.printMsg("User not found");
            }
        }
        
    }
    
    public void sendToMany(ClientHandler sender, String persons, String msg) {
        String[] split;
        String username = sender.getUsername();
        if (persons.equals("*")) {
            for (ClientHandler ch : chlist) {
                ch.printMsg("MSGRES:" + username + ":" + msg);
            }
        } else {
            split = persons.split(",");
        }
        String cmd2;
        if (split.length > 2) {
            cmd = split[0];
            cmd2 = split[1];
            msg = split[2];
        } else {
            cmd = split[0];
            msg = split[1];
        }
    }
    
    public void sendClientList() {
        String connectedClients;
        StringBuilder sb = new StringBuilder("CLIENTLIST:");
        chlist.forEach((h) -> {
            sb.append(h.getUsername() + ",");
        });
        connectedClients = sb.toString();
        connectedClients.substring(0, (sb.length() - 1));
        chlist.forEach((h) -> {
            h.printMsg(connectedClients);
        });
    }
    
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error while stopping server");
        }
    }
    
}
