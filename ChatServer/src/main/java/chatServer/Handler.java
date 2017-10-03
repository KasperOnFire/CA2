package chatServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends Thread {

    private ChatServer master;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private String username;
    private boolean loggedIn;

    public Handler(ChatServer server, Socket socket) throws IOException {
        this.master = server;
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
        loggedIn = false;
    }

    @Override
    public void run() {
        this.master.addClient(this);
        userLogin();
        while (loggedIn) {
            parseCommand();
        }
        try {
            master.removeClient(this);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void userLogin() {
        String s = in.nextLine();
        String[] split = s.split(":");
        String cmd;
        String uname;

        if (split.length > 2) {
            loggedIn = false;
        }

        cmd = split[0];
        uname = split[1];

        if (!cmd.equals("LOGIN")) {
            loggedIn = false;
        } else if (uname.contains(":") || uname.contains(",")) {
            loggedIn = false;
        } else {
            username = uname;
            loggedIn = true;
            master.sendClientList();
        }
    }

    private void parseCommand() {
        String input = in.nextLine();
        if (input.startsWith("LOGOUT")) {
            loggedIn = false;
            return;
        }
        String[] split = input.split(":");
        String cmd;
        String persons;
        String msg;
        if (split.length > 2) {
            cmd = split[0];
            persons = split[1];
            msg = split[2];
            sendMsg(persons, msg);
        } else {
            printMsg("No idea what you mean please try again");
        }
    }

    private void sendMsg(String persons, String msg) {
        if (persons.contains(",")) {
            master.sendToMany(this, persons, msg);
        } else if (persons.equals("*")) {
            master.sendToAll(this, msg);
        } else {
            master.sendToOne(this, persons, msg);
        }
    }

    public void printMsg(String in) {
        out.println(in);
    }

    public String getUsername() {
        return this.username;
    }
}