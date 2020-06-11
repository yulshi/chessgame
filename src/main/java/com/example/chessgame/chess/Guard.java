package com.example.chessgame.chess;

import com.example.chessgame.entity.ChessPiece;

/**
 * @author yulshi
 * @create 2020/06/09 10:49
 */
public class Guard extends ChessPiece {
  public Guard(Color color) {
    super(color == Color.Black ? "士" : "仕", color, 5, AttackType.Adjacent);
  }
}
