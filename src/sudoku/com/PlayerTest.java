package sudoku.com;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
  Player p;
  @Before
  public void setUp() throws Exception {
    p = new Player("AJ");
    String server="START|2|2" +
    		"|7 0 5 0 0 0 2 9 4|0 0 1 2 0 6 0 0 0" +
    		"|0 0 0 0 0 0 0 0 7|9 0 4 5 0 0 0 2 0" +
    		"|0 0 7 3 6 2 1 0 0|0 2 0 0 0 1 7 0 8" +
    		"|1 0 0 0 9 0 0 0 0|0 0 0 7 0 5 9 0 0" +
    		"|5 3 9 0 0 0 8 0 2";
    p.initializeBoard(server);
  }

  @Test
  public void testMakeMove(){
    p.getMove();
  }

}
