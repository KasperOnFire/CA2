package chatServer;

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
    private boolean loggedIn;

    public ClientHandler(ChatServer server, Socket socket) throws IOException {
        this.master = server;
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
        loggedIn = false;
    }

    @Override
    public void run() {
        this.master.addClient(this);

        initHelp();
        userLogin();
        while (loggedIn) {
            parseCommand();
        }

        try {
            this.master.removeClient(this);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initHelp() {
        out.println("Usage:");
        out.println("COMMAND:message");
        out.println("Please login. use LOGIN:username");
    }

    private void userLogin() {
        String s = in.nextLine();
        String[] split = s.split(":");
        String cmd = split[0];
        String uname = split[1];

        if (cmd != "LOGIN") {
            userLogin();
        } else {
            username = uname;
            loggedIn = true;
        }
    }

    private void parseCommand() {
        String[] split = in.nextLine().split(":");
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

        switch (var) {
            case val:

                break;
            default:
                throw new AssertionError();
        }

    }

    public void sendMessage(String in) {
        out.println(in);
    }

    public String getUsername() {
        return this.username;
    }
}
