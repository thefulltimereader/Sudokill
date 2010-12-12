package sudoku.com;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

public class Client {
  Client(String host, int port, String name) throws Exception {
    // connect & send name
    System.out.println("connecting");
    BufferedWriter bw= null;
    BufferedReader br = null;
    try{
    Socket s = new Socket(host, port);
    bw = new BufferedWriter(new OutputStreamWriter(
        s.getOutputStream()));
    br = new BufferedReader(new InputStreamReader(
        s.getInputStream()));
    } catch (UnknownHostException e) {
      System.out.println("Unknown host");
      System.exit(-1);
    } catch (IOException e) {
      System.out.println("No I/O");
      System.exit(-1);
    }
    bw.write(name + "\n");
    bw.flush();
    Player player = new Player(name);
    while(br.readLine()!=null){
      //make player
      
      String in = br.readLine();
      if (in.startsWith("START")){
        System.out.println(in);
        player.initializeBoard(in);
      }
      else if(in.matches("\\d+\\s")){
        player.setOpponentMove(in);
      }
      else if(in.startsWith("ADD")){
        // play
        String out = player.getMove();
        bw.write(out + "\n");
        bw.flush();
      }
      else if(in.contains("WIN") || in.contains("LOSE"))
        break;
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
