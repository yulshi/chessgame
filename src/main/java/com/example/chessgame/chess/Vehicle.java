package com.example.chessgame.chess;

import com.example.chessgame.entity.ChessPiece;

/**
 * @author yulshi
 * @create 2020/06/09 10:46
 */
public class Vehicle extends ChessPiece {
  public Vehicle(Color color) {
    super(color == Color.Black ? "车" : "车", color, 2, AttackType.Adjacent);
  }
}
