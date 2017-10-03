/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Anton
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private Scanner scan;
    private String username;
    private PrintWriter pw;
    private String input;
    private String[] split;
    private String cmd;
    private String msg;

    public ClientHandler(Socket socket) {
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
    
    public void run(){
        
        //The ClientHandlers 
        
        boolean notStopping = true;
        
        while(notStopping){
            
        }
        
    }

    public Socket getSocket() {
        return socket;
    }

    public Scanner getScan() {
        return scan;
    }

    public PrintWriter getPw() {
        return pw;
    }

    
}
