package chatServer;

import chatServer.ChatServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    
    private ChatServer master;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    
    private String username;
    private String input;
    private String[] split;
    private String cmd;
    private String rec;
    private String msg;
    
    public ClientHandler(ChatServer server, Socket socket) throws IOException {
        this.master = server;
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
    }
    
    @Override
    public void run() {
        this.master.addClient(this);
        boolean notStopping = true;
        userLogin();
        while (notStopping) {
            parseCommand();
        }
        
    }
    
    private void userLogin() {
        out.println("Usage:");
        out.println("COMMAND:message");
        out.println("Please login. use LOGIN#username");
        
    }
    
    private void parseCommand() {
        
    }
    
    public void sendMessage(String in) {
        out.println(in);
    }
    
    public String getUsername() {
        return this.username;
    }
}
