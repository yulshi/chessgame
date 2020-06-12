package com.example.chessgame.chess.piece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class Cannon extends ChessPiece {
  public Cannon(Color color) {
    super(color == Color.Black ? "炮" : "炮", color, 1, AttackType.Segregative);
  }
}
