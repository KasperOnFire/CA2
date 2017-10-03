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

    public void sendMessage(String input) {
        String[] split = input.split(":");
        String cmd;
        String cmd2;
        String msg;
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
            h.sendMessage(connectedClients);
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
