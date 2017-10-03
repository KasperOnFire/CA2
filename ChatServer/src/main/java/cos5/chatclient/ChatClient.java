/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cos5.chatclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient extends Thread {

  Socket socket;
  private Scanner input;
  private PrintWriter output;
  private IDataReady observer;
  private boolean keepRunning = true;

  public void addObserver(IDataReady observer) {
    this.observer = observer;
  }

  public void closeConnection() {
    send("stop");
    keepRunning = false;
  }

  //This connects to the server, and start listening for incomming messages
  public void connect(String address, int port) throws IOException {
    socket = new Socket(address, port);
    input = new Scanner(socket.getInputStream());
    output = new PrintWriter(socket.getOutputStream(), true);
    this.start();
  }

  public void send(String msg) {
    output.println(msg);
  }

  
  @Override
    public void run()  { 
    while (keepRunning) {
      String msg = input.nextLine();
      observer.messageReady(msg);
    }
    try {
      socket.close();
    } catch (IOException ex) {
      Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
