package com.example.chessgame.chess.board;

/**
 * @author yulshi
 * @create 2020/06/09 11:04
 */
public class Position {

  public static Position OUT = new Position(-1, -1);

  private int row;
  private int col;

  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public static Position of(int row, int col) {
    return new Position(row, col);
  }

  public void moveTo(Position otherPos) {
    this.row = otherPos.row;
    this.col = otherPos.col;
  }

  public boolean adjacentOf(Position otherPos) {
    int rowDiff = Math.abs(row - otherPos.row);
    int colDiff = Math.abs(col - otherPos.col);
    if ((rowDiff == 0 && colDiff == 1) || (rowDiff == 1 && colDiff == 0)) {
      return true;
    }
    return false;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  @Override
  public String toString() {
    return "Position{" +
            "row=" + row +
            ", col=" + col +
            '}';
  }

  public boolean same(Position position) {
    if (this.row == position.getRow() && this.col == position.getCol()) {
      return true;
    }
    return false;
  }
}
