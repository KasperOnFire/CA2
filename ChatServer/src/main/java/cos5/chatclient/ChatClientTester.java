
package cos5.chatclient;

import java.io.IOException;

public class ChatClientTester {
    
  public static void main(String[] args) throws IOException, InterruptedException {
    ChatClient AAA = new ChatClient();
    ChatClient BBB = new ChatClient();
    
    
    AAA.connect("0.0.0.0",8081);
    AAA.addObserver((msg) -> {
      System.out.println("A " + msg);
    });
    
    ChatClient.sleep(1000);
    
    AAA.send("LOGIN:AAA");
    
    ChatClient.sleep(1000);
    
    BBB.connect("0.0.0.0",8081);
    BBB.addObserver((msg) -> {
      System.out.println("B " + msg);
    });
    
    ChatClient.sleep(1000);
    
    BBB.send("LOGIN:BBB");
    
    ChatClient.sleep(1000);
    
    AAA.send("MSG:BBB:Hello");
    
    ChatClient.sleep(1000);
    
    AAA.send("MSG:AAA,BBB:Hello you two");
    
    ChatClient.sleep(1000);
    
    BBB.send("MSG:*:Hello all");
    
    ChatClient.sleep(1000);
    
    
    BBB.send("LOGOUT:");
    
    ChatClient.sleep(1000);
    
    BBB.closeConnection();
    
//    BBB.closeConnection();
    

    
    AAA.closeConnection();
    
   
    
  }

  
}