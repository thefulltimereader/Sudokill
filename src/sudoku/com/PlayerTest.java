package sudoku.com;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sun.security.pkcs11.P11TlsKeyMaterialGenerator;

public class PlayerTest {
  Player p;
  String server;
  @Before
  public void setUp() throws Exception {
    p = new Player("AJ");
    server="START|2|2" +
    		"|7 0 5 0 0 0 2 9 4|0 0 1 2 0 6 0 0 0" +
    		"|0 0 0 0 0 0 0 0 7|9 0 4 5 0 0 0 2 0" +
    		"|0 0 7 3 6 2 1 0 0|0 2 0 0 0 1 7 0 8" +
    		"|1 0 0 0 9 0 0 0 0|0 0 0 7 0 5 9 0 0" +
    		"|5 3 9 0 0 0 8 0 2";
    server = "START|2|2" +
    "|7 0 5 0 0 0 2 9 4|4 0 1 2 0 6 3 0 0|3 0 0 1 4 9 5 8 7" +
    "|9 6 4 5 7 8 0 2 0|0 0 7 3 6 2 1 0 0|0 2 0 0 0 1 7 0 8" +
    "|1 0 0 0 9 0 0 0 0|8 0 0 7 0 5 9 0 0|5 3 9 0 0 0 8 0 2";
   
    p.initializeBoard(server);
  }

  @Test
  public void testMakeMove(){
    String reply = p.getMove();
    System.out.println(p.getMove());
  }
  @Test
  public void testAddOpponent(){
    p.setOpponentMove("4 1 8");
  }
  @Test
  public void simulate(){
    Player p2 = new Player("TEST");
    p2.initializeBoard(server);
    p.setOpponentMove("2 3 1");
    String reply = p.getMove();
    System.out.println(reply);
    
    p2.setOpponentMove(reply);
    reply = p2.getMove();
    System.out.println(reply);
    
    p.setOpponentMove(reply);
    reply = p.getMove();
    System.out.println(reply);
  }
}
