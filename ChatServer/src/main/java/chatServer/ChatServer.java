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
    private final List<ClientHandler> clientList = Collections.synchronizedList(new ArrayList());

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
        System.out.println("Waiting for clients!");
        while (true) {
            Socket socket = serverSocket.accept(); //BLOCK
            new ClientHandler(this, socket).start();
        }
    }

    public void addClient(ClientHandler ch) {
        clientList.add(ch);
    }

    public void removeClient(ClientHandler ch) {
        clientList.remove(ch);
        sendClientList();
    }

    public void sendToOne(ClientHandler sender, String person, String msg) {
        ClientHandler reciever = null;
        for (ClientHandler ch : clientList) {
            if (ch.getUsername().equals(person)) {
                reciever = ch;
                reciever.printMsg("MSGRES:" + sender.getUsername() + ":" + msg);
                break;
            }
        }
    }

    public void sendToAll(ClientHandler sender, String msg) {
        String username = sender.getUsername();
        clientList.forEach((ch) -> {
            ch.printMsg("MSGRES:" + username + ":" + msg);
        });
    }

    public void sendToMany(ClientHandler sender, String persons, String msg) {
        String[] personList;
        List<ClientHandler> recievers = new ArrayList();
        String username = sender.getUsername();
        //SEND TO SPECIFIC
        personList = persons.split(",");
        clientList.forEach((client) -> {
            if (Arrays.asList(personList).contains(client.getUsername())) {
                recievers.add(client);
            }
        });
        recievers.forEach((reciever) -> {
            reciever.printMsg("MSGRES:" + username + ":" + msg);
        });

    }

    public void sendClientList() {
        String connectedClients;
        StringBuilder sb = new StringBuilder("CLIENTLIST:");
        clientList.forEach((h) -> {
            sb.append(h.getUsername() + ",");
        });
        connectedClients = sb.toString();
        if (connectedClients.endsWith(",")) {
            connectedClients = connectedClients.substring(0, (connectedClients.length() - 1));
        }
        for (ClientHandler clientHandler : clientList) {
            clientHandler.printMsg(connectedClients);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error while stopping server");
        }
    }

}
