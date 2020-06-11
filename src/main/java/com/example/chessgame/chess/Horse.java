package com.example.chessgame.chess;

import com.example.chessgame.entity.ChessPiece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class Horse extends ChessPiece {
  public Horse(Color color) {
    super(color == Color.Black ? "马" : "马", color, 3, AttackType.Adjacent);
  }
}
