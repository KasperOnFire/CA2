
package cos5.chatclient;

import java.io.IOException;

public class ChatClientTester {
    
  public static void main(String[] args) throws IOException, InterruptedException {
    ChatClient client = new ChatClient();
    
    client.addObserver((msg) -> {
      System.out.println("Received a message: "+msg);
    });
    client.connect("localhost",1234);
    
    client.send("Hello");
    client.send("Hello World");
    client.send("Hello Wonderfull World");
    Thread.sleep(100);
    client.closeConnection();
    
    System.out.println("DONE");
   
    
  }

  
}