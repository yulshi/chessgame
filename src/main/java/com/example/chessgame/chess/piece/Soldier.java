package com.example.chessgame.chess.piece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class Soldier extends ChessPiece {
  public Soldier(Color color) {
    super(color == Color.Black ? "卒" : "兵", color, 0, AttackType.Adjacent);
  }
}
