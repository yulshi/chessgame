package com.example.chessgame.chess;

import com.example.chessgame.entity.ChessPiece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class Premier extends ChessPiece {
  public Premier(Color color) {
    super(color == Color.Black ? "象" : "相", color, 4, AttackType.Adjacent);
  }
}
