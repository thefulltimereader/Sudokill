package sudoku.com;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sudoku.gameState.Board;
import sudoku.util.Pair;
import sudoku.util.Point;

public class Player {
  final String name;
  private int id, totalPlayers;
  private Board board;
  private int numOfPlays = 0;
  public int DEPTH=3;
  public Player(String name) {
    this.name = name;
  }
  
  /**
   * input looks like:
   * START|1|2|0 0 0 1 3 4 0 8 9|3 0 0 0 0 5 0 0 0| ... |0 2 0 0 1 0 0 6 0
   * @param in
   */
  public void initializeBoard(String in) {
    String[] data = in.split("\\|");
    id = Integer.parseInt(data[1]);
    totalPlayers = Integer.parseInt(data[2]);
    this.board = new Board(Arrays.copyOfRange(data, 3, data.length));
  }
  /**
   * input looks like:
   * ADD|0 0 0 1 3 4 0 8 9|3 0 0 0 0 5 0 0 0| ... |0 2 0 0 1 0 0 6 0
   * @param in
   */
  public void initializeBoardWithAdd(String in) {
    String[] data = in.split("\\|");
    this.board = new Board(Arrays.copyOfRange(data, 1, data.length));
  }
  /**
   * Input looks like
   * 0 1 8 2 => indicates player 2 added the value 8 at row 0, col 1
   * @param in
   */
  public void setOpponentMove(String in) {
    String[] strs = in.split(" ");
    int x = Integer.parseInt(strs[0]);
    int y = Integer.parseInt(strs[1]);
    int val = Integer.parseInt(strs[2]);
    //int playerId = Integer.parseInt(strs[3]);    
    Point opponentPt = new Point(x, y, val);
    board.addPositionByOpponent(opponentPt);
  }
  /**
   * Build mini-max tree and do alpha beta prune
   * Pair carries the possible point canditate and its score
   */
  private Pair<Point, Double> alphaBeta(Point candidate, double alpha, 
      double beta, int depth, Board state){
    if(state.win() || state.complete()){
      System.out.println("WIN! at "+ depth);
      return new Pair<Point, Double>(candidate,depth+0.0);
    }
    if(depth<=0){
      return new Pair<Point, Double>(candidate, evaluateState(state));
    }
    else{
      List<Point> possibilities = state.getPossibleMoves();
      Point winner = candidate;
      for(Point p: possibilities){
        Board newBoard = new Board(state);//copy

        newBoard.addPositionByOpponent(p);
        Pair<Point, Double> res = alphaBeta(p, -beta, -alpha, depth-1, newBoard);
        Double score = -1*res.getSnd(); //negate to use symmetry. Now -b == alpha
        if(alpha<score){
          alpha = score;
          winner = p;
        }
        if(alpha>=beta)break;

      }
      return new Pair<Point, Double>(winner, alpha);
    }
  }
  /**
   * Core part of the game.. which moves are the best?? 
   * So.. if it got here,, means that the deapth was not enough move to kill
   * the opponent. Now the board is at the point where your opponent will place
   * something (you just placed on the board). So count the number of nodes
   * that will kill you on the possible points.
   * 
   * If your last move has taken all the possible positions and now your opp.
   * has a free move, that's BAD so return bad
   * 
   * total number of moves smaller the better
   * @return score, higher the better
   */
  public double evaluateState(Board b){
    if(b.crossSectionIsFull()) return -DEPTH/2;
    List<Point> poss = b.getPossibleMoves();
    int winCount=0;
    double totalOptions=0.0;
    for(Point p: poss){
      Pair<Integer, Integer> pair =optionsAfterThisPoint(p, b); 
      int optionsLeft = pair.getFst();
      totalOptions+= pair.getSnd();
      if(optionsLeft == -1) winCount--;
      else if(optionsLeft == 1) winCount++;
    }
    //System.out.println("with " + b.toString()+" score: " + (winCount-((totalOptions/poss.size()))));
    return winCount-((totalOptions/poss.size()));
  }
  /**
   * Returns what kind of choices i have after an opponent places the point p
   * returns 1 if I win, -1 if I lose, 0 if neighter
   * I lose = after the opponent places that point p with with that val, I have
   * no legit moves
   * I win = after the opponent makes the move I have free choice (not win but
   * good..)
   * Neighter = 0
   * @param p, b
   * @return win/lose/neither , # of optionsn
   */
  public Pair<Integer, Integer> optionsAfterThisPoint(Point p, Board b) {
    Board newBoard = new Board(b);
    newBoard.addPositionByOpponent(p);
    int size = newBoard.getPossibleMoves().size();
    //check if this point kills my options.
    if(newBoard.win()) return new Pair<Integer, Integer>(-1, size);
    else if(newBoard.crossSectionIsFull()) return new Pair<Integer, Integer>(1, size);
    return new Pair<Integer, Integer>(0, size);
  }

  public String getMove() {
    if(numOfPlays>5) DEPTH++;
    Pair<Point, Double> result = alphaBeta(null, Double.NEGATIVE_INFINITY, 
        Double.POSITIVE_INFINITY, DEPTH, this.board);
    Point res = result.getFst();
    if(res == null){
      System.out.println("LOST!!");
      res = new Point(0,0,1);
    }
    this.board.addPositionByMe(res);
    numOfPlays++;
    return formReply(res);
  }
  /**
   * reply looks like: 0 0 9
   * // Places value 9 in row 0, column 0 (top left) 
   * @param p
   * @return
   */

  public String formReply(Point p){
    return new StringBuilder().append(p.getX()).append(" ").append(p.getY())
    .append(" ").append(p.getVal()).toString();
  }


}
