package sudoku.com;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

public class Client {
  private boolean justPlayed = false;
  Client(String host, int port, String name) throws Exception {
    // connect & send name
    System.out.println("connecting");
    PrintWriter out = null;
    BufferedReader br = null;
    try{
    Socket s = new Socket(host, port);
    out = new PrintWriter(s.getOutputStream(), true);
    br = new BufferedReader(new InputStreamReader(
        s.getInputStream()));
    } catch (UnknownHostException e) {
      System.out.println("Unknown host");
      System.exit(-1);
    } catch (IOException e) {
      System.out.println("No I/O");
      System.exit(-1);
    }
    out.println(name);
    String in;
    Player player = new Player(name);
    while((in = br.readLine()) != null){
      //make player
      System.out.println("Input is:" +in);
      if (in.startsWith("START")){
        player.initializeBoard(in);
        if(in.contains("START|1|")){
        System.out.println("Board Initialized");        
        String reply = player.getMove();
        System.out.println("Make first move: "+reply);
        out.println(reply);
        justPlayed = true;
        
        }
      }
      else if(in.matches("^\\d+\\s+\\d+\\s+\\d+$")){
        System.out.println("Add opponents move!");
        player.setOpponentMove(in);
      }
      else if(in.startsWith("ADD")){
        if(justPlayed){
          justPlayed=false;
        }
        else{
      //  player.initializeBoardWithAdd(in);
        // play
        String reply = player.getMove();
        System.out.println("response: "+reply);
        out.println(reply);
        }
      }
      else if(in.contains("WIN") || in.contains("LOSE")){
        System.exit(0);
        break;
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.out.println("usage:  java Client"
          + " <host> <port> <name>");
      System.exit(1);
    }
    new Client(args[0], Integer.parseInt(args[1]), args[2]);
  }

}
