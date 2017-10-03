package clientHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private Socket socket;
    private Scanner in;
    private String username;
    private PrintWriter out;
    private String input;
    private String[] split;
    private String cmd;
    private String msg;

    public ClientThread(Socket socket) {
        this.socket = socket;
        System.out.println("Attempt to create Scanner/PrintWriter for ClientHandler");

        try {

            scan = new Scanner(socket.getInputStream());
            pw = new PrintWriter(socket.getOutputStream(), true); //Always remember true to enable autoflush
            username = (String) scan.nextLine();
            System.out.println(username + " connected to the server");

        } catch (IOException e) {
            System.out.println("Error creating Scanner or PrintWriter");
        }

    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            //The ClientThreads
            boolean notStopping = true;

            while (notStopping) {
                userLogin();

            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



}
