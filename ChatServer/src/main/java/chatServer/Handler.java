package chatServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
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
    }

    private void userLogin() {
        String s = in.nextLine();
        String[] split = s.split(":");
        String cmd;
        String uname;

        if (split.length > 2) {
            logout();
        }

        cmd = split[0];
        uname = split[1];

        if (!cmd.equals("LOGIN")) {
            logout();
        } else if (uname.contains(":") || uname.contains(",")) {
            logout();
        } else {
            username = uname;
            loggedIn = true;
            master.sendClientList();
        }
    }

    private void parseCommand() {
        try {
            String input = in.nextLine();

            if (input.startsWith("LOGOUT:")) {
                logout();
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
                logout();

            }
        } catch (NoSuchElementException e) {
            logout();
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

    public void logout() {
        loggedIn = false;
        master.removeClient(this);
        master.sendClientList();
    }
}
