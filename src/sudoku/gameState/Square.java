package sudoku.gameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.util.Point;


public class Square{
  private final static int SIZE = 3;
  private List<Integer> content;
  private int id;//1D id of the grid
  public Square(int id){
    this.id = id;
    content = new ArrayList<Integer>(SIZE*SIZE);
    for(int i=0; i<SIZE*SIZE; i++){
      content.add(0);
    }
    shiftById = Point.inflate(id, SIZE, SIZE, 0);
    shiftById = shiftById.scale(SIZE);
  }
  private Point shiftById;
  /**
   * Copy
   * @param content
   * @param id
   */
  public Square(Square s){
    this.id = s.id;
    content = new ArrayList<Integer>(s.content);
    this.shiftById = s.shiftById;
  }
  public Square(List<Integer> content, int id){
    this.id = id;
    this.content = new ArrayList<Integer>(content);
  }  
  public Square(String[] s, int id){
    this.id = id;
    this.content = new ArrayList<Integer>(SIZE*SIZE);
    for(int i=0; i<SIZE*SIZE; i++){
      content.add(Integer.parseInt(s[i]));
    }
  }
  /**
   * If the position is 0, for each open position, calculate val
   *  that passes constraints
   * @return
   */
  public List<Point> possiblePositions(){
    List<Point> l = new ArrayList<Point>();
    for(int i=0; i<SIZE*SIZE; i++){
      if(content.get(i)==0){
        for(Integer val=1; val<10; val++){
          if(!content.contains(val)){
            Point p = Point.inflate(i, SIZE, SIZE, val).addWith(shiftById);
            l.add(p);
          }
        }
      }
    }
    return l;
  } 
  
  public List<Point> possiblePositionsAt(Integer ind) {
    List<Point> l = new ArrayList<Point>();
    if(content.get(ind)==0){
      for(Integer val=1; val<10; val++){
          if(!content.contains(val)){
            Point p = Point.inflate(ind, SIZE, SIZE, val).addWith(shiftById);
            l.add(p);
          }
        }
    }
    return l;
  } 
  public List<Integer> getRow(int ind){      
    List<Integer> l = new ArrayList<Integer>();
    ind = ind*SIZE;
    for(int i=0; i<SIZE; i++){
      l.add(content.get(ind));
      ind++;
    }
    return l;
  }
  public List<Point> getRowPossible(int ind) {
    List<Point> l = new ArrayList<Point>();
    ind = ind*SIZE;
    
    for(int i=0; i<SIZE; i++){
      if(content.get(ind)==0)
        for(int v=1; v<10; v++){
          if(!content.contains(v))
          l.add(Point.inflate(ind, SIZE, SIZE, v).addWith(shiftById));
          //to conver to 9*9 array..
        }
      ind++;
    }
    return l;

  }
  public void setRow(int[] ins, int row){
    row*=SIZE;
    for(int i=0; i<SIZE;i++){
      content.set(row+i, ins[i]);
    }
  }
  public List<Integer> getCol(int ind){      
    List<Integer> l = new ArrayList<Integer>();
    for(int i=0; i<SIZE; i++){
      l.add(content.get(ind));
      ind+=SIZE;
    }
    return l;
  }
  public List<Point> getColPossible(int ind){      
    List<Point> l = new ArrayList<Point>();
    for(int i=0; i<SIZE; i++){
      if(content.get(ind)==0)
        for(int v=1; v<10; v++){
          if(!content.contains(v))
            l.add(Point.inflate(ind, SIZE, SIZE, v).addWith(shiftById));
        }
      ind+=SIZE;
    }
    return l;
  }

  public void update(int index, int val){
    content.set(index, val);
  }
  public boolean complete(){
    Set<Integer> set = new HashSet<Integer>(content);
    return set.size()==SIZE*SIZE;
  }
  /**
   * Checks if, the # of element -{0} = set -{0}
   * If not equal, that means some number had a dup
   * @return
   */
  public boolean inconsistentState(){
    return noDuplicates(content);
  }
  /**
   * Returns number of non-repeating non-zero elements in a list
   */
  public static List<Integer> getNonZeroList(List<Integer> l){
    List<Integer> noZero = new ArrayList<Integer>();
    for(Integer i: l){
      if(i!=0) noZero.add(i);
    }
    return noZero;
  }
  /**
   * Returns true is there is no duplicates (non zero numbers only)
   * @param list
   * @return
   */
  public static boolean noDuplicates(List<Integer> list) {
    List<Integer> noZero = Square.getNonZeroList(list);
    Set<Integer> set = new HashSet<Integer>(noZero);
    return noZero.size()==set.size();
  }
  @Override
  public boolean equals(Object o){
    if(o==null || !(o instanceof Square)) return false;
    Square that = (Square) o;
    for(int i=0; i<SIZE; i++)
      if(this.content.get(i)!=that.content.get(i)) return false;
    return true;
  }
  @Override
  public String toString(){
    StringBuilder str = new StringBuilder();
    int c=0;
    for(Integer i: content){
      str.append(i).append(" ");
      c++;
      if(c%SIZE==0) str.append("\n");
    }
    str.deleteCharAt(str.length()-1);//remove the last space
    return str.toString();
  }
  
   
}