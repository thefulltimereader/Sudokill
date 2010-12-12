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
  public final static int DEPTH=4;
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
    if(depth==0 || state.complete()){
      return new Pair<Point, Double>(candidate, evaluateState(state));
    }
    else{
      List<Point> possibilities = state.getPossibleMoves();
      Point winner = candidate;
      for(Point p: possibilities){
        Board newBoard = new Board(state);//copy
        newBoard.addPositionByOpponent(p);
        Pair<Point, Double> res = alphaBeta(p, -beta, -alpha, --depth, newBoard);
        Double score = -1*res.getSnd(); //negate to use symmetry. Now -b == alpha
        if(alpha<score){
          alpha = score;
          winner = p;
        }
        if(alpha>=beta) break;
      }
      return new Pair<Point, Double>(winner, alpha);
    }
  }
  /**
   * Core part of the game.. which moves are the best?? 
   * 1st solve the board, if the board is unsolvable that's terrible
   * 2nd if the solve
   * @return score, higher the better
   */
  public double evaluateState(Board b){
    return new Random().nextDouble();
  }
  public String getMove() {
    Pair<Point, Double> result = alphaBeta(null, Double.NEGATIVE_INFINITY, 
        Double.POSITIVE_INFINITY, DEPTH, board);
    return formReply(result.getFst());
  }
  
  public String formReply(Point p){
    return new StringBuilder(p.getX()).append(" ").append(p.getY())
    .append(" ").append(p.getVal()).toString();
  }

}
