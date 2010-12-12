package sudoku.gameState;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sudoku.util.Pair;
import sudoku.util.Point;

public class Board {
  final static int SIZE = 3;
  private List<Square> squares;
  private Point opponentPt;
  public Board(){
    squares = new ArrayList<Square>(SIZE*SIZE);
    for(int i=0; i<SIZE; i++){
      for(int j=0; j<SIZE; j++){
      Square s = new Square(flatten(i, j));
      squares.add(s);
      }
    }
  }
  public List<Square> getSquares(){
    return new ArrayList<Square>(squares);
  }
  /**
   * Copy Constructor
   * @param o
   */
  public Board(Board o){
    List<Square> originalSqs = o.getSquares();
    squares = new ArrayList<Square>(SIZE*SIZE);
    for(int i=0; i<SIZE*SIZE;i++){
      squares.add(new Square(originalSqs.get(i)));
    }
  }
  /**
   * Server Constructor input looks like:
   * {0 0 0 1 3 4 0 8 9, 3 0 0 0 0 5 0 0 0, ... ,0 2 0 0 1 0 0 6 0}
   */
  public Board(String[] ins){
    squares = new ArrayList<Square>(SIZE*SIZE);
    for(int i=0; i<SIZE*SIZE; i++) squares.add(new Square(i));
    int[] rowInputs;
    String[] wholeRow;
    for(int i=0; i<ins.length; i++){
      wholeRow = ins[i].split(" ");
      rowInputs = new int[SIZE];
      int count = 0;
      for(int j=0; j<SIZE*SIZE;j++){
        rowInputs[j%SIZE] = Integer.parseInt(wholeRow[j]);
        count++;
        if(count%SIZE==0 && j!=0){
//          System.out.println((i/SIZE)*SIZE+((count/SIZE)-1));
          squares.get((i/SIZE)*SIZE+((count/SIZE)-1)).setRow(rowInputs, i%SIZE);
        }
      }
    }
  }
  /**
   * If opponentPt is null or no option at that cross-section, get
   * list from all possible moves
   * @return
   */
  public List<Point> getPossibleMoves(){
    if(opponentPt!=null){
      List<Point> l = getPossibleMovesAt(opponentPt);
      if(!l.isEmpty()) return l;
    }
    //means there was no more move at last opponent move
    return getAllPossibleMoves();
  }
  /**
   * Returns all possible position (x, y)
   */
  private List<Point> getAllPossibleMoves(){
    List<Point> poss = new ArrayList<Point>();
    for(Square s: squares){
      List<Point> possB4RowAndCol = s.possiblePositions();      
      //remove vals that conflicts with row and col
      //TODO: core reference de iiin yona?!Q
      checkContraints(possB4RowAndCol);
      poss.addAll(possB4RowAndCol);
    }
    return poss;
  }
  /**
   * Only in the diagonal/vertical of the point
   */
  public List<Point> getPossibleMovesAt(Point pt){
    Pair<Integer, Integer> sqIDandPos = getSquareIDandSquarePos(pt);
    //List<Point> poss =  squares.get(sqIDandPos.getFst())
    //.possiblePositionsAt(sqIDandPos.getSnd());
    List<Point> poss = getRowAndColPossibilities(pt);
    return poss;
  }
  private List<Point> getRowAndColPossibilities(Point p) {
    List<Point> poss = getRowPoints(p.getX()%SIZE);
    poss.addAll(getColPoints(p.getY()%SIZE));
    return null;
  }
  /**
   * Refines the list by adding constraints of row and col
   * @param list
   * @return
   */
  private void checkContraints(List<Point> list){
    List<Integer> rowConstraints, colConstraints; 
    Iterator<Point> itr = list.iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      rowConstraints= getRow(p.getX()%SIZE);
      colConstraints = getCol(p.getY()%SIZE);
      if(rowConstraints.contains(p.getVal()) || 
          colConstraints.contains(p.getVal()))
        itr.remove();
    }
  }    
  /**
   * Gets the row with 0<=index<9
   * @param id
   * @return
   */
  private List<Integer> getRow(int id){
    List<Integer> row = new ArrayList<Integer>(SIZE*SIZE);
    int squareID = id%SIZE;
    for(int i=0; i<SIZE; i++){
      row.addAll(squares.get(squareID+i).getRow(id));
    }
    return row;
  }
  private List<Point> getRowPoints(int id){
    List<Point> row = new ArrayList<Point>();
    int squareID = id%SIZE;
    for(int i=0; i<SIZE; i++){
      row.addAll(squares.get(squareID+i).getRowPossible(id));
    }
    return row;
  }
  /**
   * Gets the col with 0<=index<9
   * @param id
   * @return
   */
  private List<Integer> getCol(int id){
    List<Integer> col = new ArrayList<Integer>(SIZE*SIZE);
    int squareID = id / SIZE;//mod it to 3x3 board
    for(int i=0; i<SIZE;i++){
      col.addAll(squares.get(squareID+i*SIZE).getCol(id));
    }
    return col;
  }
  /**
   * Returns the possible points in this col
   * @param id
   * @return
   */
  private List<Point> getColPoints(int id){
    List<Point> col = new ArrayList<Point>(SIZE*SIZE);
    int squareID = id / SIZE;//mod it to 3x3 board
    for(int i=0; i<SIZE;i++){
      col.addAll(squares.get(squareID+i*SIZE).getColPossible(id));
    }
    return col;
  }

  public void addPositionByOpponent(Point p) {
    opponentPt = p;
    Pair<Integer, Integer> sqIDandPos = getSquareIDandSquarePos(p);
    squares.get(sqIDandPos.getFst()).update(sqIDandPos.getSnd(), p.getVal());
  }
  public void addPosition(int index, int val, int squareId){
    squares.get(squareId).update(index, val);
  }
  
  public Pair<Integer, Integer> getSquareIDandSquarePos(Point pt){
    int squareID = pt.getX()/SIZE;
    int squarePos = pt.getX()*SIZE + pt.getY()%SIZE;
    return new Pair<Integer, Integer>(squareID, squarePos);
  }
  
  //Utils
  /**
   * Flattens a 2D m by n matrix into 1D
   * This is the index for squares, not positions inside squares
   * idx = (n*x) + y
   * @param pt, n is the # of columns of the flattening matrix
   * @return
   */
  public static int flatten(int x, int y){    
    return x*SIZE + y;
  }
  public boolean isConsistent() {
    //check each square
    for(Square s: squares){
      if(s.inconsistentState()) return false;
    }
    //check row and col
    List<Integer> row, col;
    for(int i=0; i<SIZE*SIZE; i++){
      row = getRow(i);
      if(!Square.noDuplicates(row)) return false;
      col = getCol(i);
      if(!Square.noDuplicates(col)) return false;
    }
    return true;
  }
  
  public boolean complete() {
    for(Square s:squares){
      if(!s.complete()) return false;
    }
    return true;
  }
  /**
   * Looks like:
   * 1 2 3 4 5 6 7 8 9
   * 0 0 0 0 1 2 0 0 0
   * ...
   * 1 2 3 4 5 6 7 8 9
   */
  @Override
  public String toString(){
    StringBuilder str = new StringBuilder();
    int id=0;
    for(Square s: squares){
      str.append("box ").append(id).append("\n").append(s.toString()).append("\n");
      id++;
    }
    str.deleteCharAt(str.length()-1);
    return str.toString();
  }
  
  
}