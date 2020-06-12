package com.example.chessgame.chess.board;

import com.example.chessgame.chess.piece.ChessPiece;
import com.example.chessgame.chess.piece.Cannon;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yulshi
 * @create 2020/06/09 10:30
 */
@Slf4j
public class Chessboard {

  private final int row;
  private final int col;

  private ChessPiece[][] grid;

  // 当前选定的子，可以为null
  private ChessPiece selectedPiece = null;

  private Status status;

  public Chessboard() {
    this(4, 8);
  }

  public Chessboard(int row, int col) {
    this.row = row;
    this.col = col;
    this.grid = new ChessPiece[row][col];
    this.status = Status.Standby;
    shuffle();
  }

  /**
   * 混洗
   */
  public void shuffle() {
    ChessSuite chessSuite = new ChessSuite();
    ChessPiece[] blackSuite = chessSuite.getBlackSuite();
    ChessPiece[] redSuite = chessSuite.getRedSuite();
    List<ChessPiece> suite = new ArrayList<>(blackSuite.length + redSuite.length);
    for (int i = 0; i < blackSuite.length; i++) {
      suite.add(blackSuite[i]);
      suite.add(redSuite[i]);
    }

    Collections.shuffle(suite);

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        ChessPiece piece = suite.get(i * col + j);
        piece.setHidden(true);
        piece.setPosition(new Position(i, j));
        grid[i][j] = piece;
      }
    }

    if (log.isDebugEnabled()) {
      display();
    }

  }

  /**
   * 获取位于row和col位置的棋子
   *
   * @return
   */
  public ChessPiece locate(Position position) {
    return grid[position.getRow()][position.getCol()];
  }

  /**
   * 移动棋子到指定位置，此处假设： 1）已经有选定的棋子，2）目标位置没有子，
   *
   * @param target 目标位置
   */
  public void moveTo(Position target) {
    ChessPiece targetPiece = locate(target);
    grid[selectedPiece.getPosition().getRow()][selectedPiece.getPosition().getCol()] = null;
    selectedPiece.setPosition(target);
    grid[target.getRow()][target.getCol()] = selectedPiece;
    if (targetPiece != null) {
      targetPiece.setPosition(Position.OUT);
    }
    selectedPiece = null;
  }

  /**
   * 攻击对方的棋子，可能的结果：吃子、兑子、失败
   *
   * @param targetPosPiece
   * @boolean 是否攻击成功
   */
  public boolean attack(ChessPiece targetPosPiece) {

    boolean attacked = false;

    Position target = targetPosPiece.getPosition();

    // 检查target上的棋子是否为对方的棋子
    if (!targetPosPiece.isHidden() && selectedPiece.getColor() != targetPosPiece.getColor()) {
      // 检查是否可以可以吃掉或兑掉target位置上的棋子
      int weightDiff = selectedPiece.getWeight() - targetPosPiece.getWeight();
      if (!(selectedPiece instanceof Cannon) && (weightDiff > 0 && weightDiff < 6 || weightDiff == -6)) {
        // 吃掉对方
        moveTo(target);
        attacked = true;
      } else if (weightDiff == 0) {
        // 兑子
        grid[selectedPiece.getPosition().getRow()][selectedPiece.getPosition().getCol()] = null;
        grid[target.getRow()][target.getCol()] = null;
        selectedPiece.setPosition(Position.OUT);
        targetPosPiece.setPosition(Position.OUT);
        selectedPiece = null;
        attacked = true;
      }
    }

    return attacked;

  }

  /**
   * 翻子
   *
   * @param position
   */
  public void flip(Position position) {
    ChessPiece piece = locate(position);
    piece.setHidden(false);
    this.selectedPiece = null;
  }

  public void display() {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        ChessPiece chessPiece = grid[i][j];
        if (chessPiece == null) {
          System.out.print("null\t");
        } else {
          System.out.printf("%s-%s-%s\t",
                  chessPiece.getColor() == ChessPiece.Color.Black ? "B" : "R",
                  chessPiece.getName(),
                  chessPiece.isHidden() ? "H" : "D");
        }
      }
      System.out.println();
    }
  }

  public ChessPiece[][] getGrid() {
    return grid;
  }

  public ChessPiece getSelectedPiece() {
    return selectedPiece;
  }

  public void setSelectedPiece(ChessPiece selectedPiece) {
    this.selectedPiece = selectedPiece;
  }


  public enum Status {
    Standby, Starting, Started, Ended;
  }

}
