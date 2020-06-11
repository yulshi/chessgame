package com.example.chessgame.chess;

import com.example.chessgame.entity.ChessPiece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class General extends ChessPiece {
  public General(Color color) {
    super(color == Color.Black ? "将" : "帅", color, 6, AttackType.Adjacent);
  }
}
