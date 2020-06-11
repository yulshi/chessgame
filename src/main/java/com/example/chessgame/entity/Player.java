package com.example.chessgame.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yulshi
 * @create 2020/06/09 20:56
 */
public class Player {

  private String name;

  private ChessPiece.Color color = null;
  // private Chessboard chessboard;

  private boolean active;
  private Map<String, Integer> scores = new HashMap<>();

  public Player() {
  }

  public Player(String name) {
    this();
    this.name = name;
  }

//  public synchronized void click(Position position) {
//    ChessPiece piece = chessboard.locate(position);
//    if (color == null) {
//      // 说明还没有选定颜色
//      piece.setHidden(false);
//      this.color = piece.getColor();
//      for (Player player : chessboard.getPlayers()) {
//        if (player != this) {
//          player.color = this.color == ChessPiece.Color.Black ? ChessPiece.Color.Red : ChessPiece.Color.Black;
//        }
//      }
//      this.setActive(false);
//      return;
//    }
//
//    if (piece != null && piece.isHidden()) {
//      // 仅仅是翻子操作
//      chessboard.flip(position);
//      this.opponent.setActive(true);
//      return;
//    }
//
//    if (piece != null && piece.getColor() == this.color) {
//      chessboard.setSelectedPiece(piece);
//      return;
//    }
//
//    if (chessboard.getSelectedPiece() != null) {
//      chessboard.move(position);
//      this.setActive(false);
//    }
//
//  }

//  public Chessboard getChessboard() {
//    return chessboard;
//  }
//
//  public void setChessboard(Chessboard chessboard) {
//    this.chessboard = chessboard;
//  }

  public void winOneGame(String gameId) {
    Integer score = scores.get(gameId);
    if (score != null) {
      score = 1;
    } else {
      score += 1;
    }
    scores.put(gameId, score);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public ChessPiece.Color getColor() {
    return color;
  }

  public void setColor(ChessPiece.Color color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Player{" +
            "name='" + name + '\'' +
            ", color=" + color +
            ", active=" + active +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
