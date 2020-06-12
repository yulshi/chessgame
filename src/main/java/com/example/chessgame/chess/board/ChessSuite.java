package com.example.chessgame.chess.board;

import com.example.chessgame.chess.piece.*;

/**
 * A suite of chess pieces which consist of 2 colors (black and red)
 *
 * @author yulshi
 * @create 2020/06/09 10:41
 */
public class ChessSuite {

  private final ChessPiece[] blackSuite;
  private final ChessPiece[] redSuite;


  public ChessSuite() {

    blackSuite = new ChessPiece[16];
    redSuite = new ChessPiece[16];

    blackSuite[0] = new General(ChessPiece.Color.Black);
    blackSuite[1] = new Guard(ChessPiece.Color.Black);
    blackSuite[2] = new Guard(ChessPiece.Color.Black);
    blackSuite[3] = new Premier(ChessPiece.Color.Black);
    blackSuite[4] = new Premier(ChessPiece.Color.Black);
    blackSuite[5] = new Horse(ChessPiece.Color.Black);
    blackSuite[6] = new Horse(ChessPiece.Color.Black);
    blackSuite[7] = new Vehicle(ChessPiece.Color.Black);
    blackSuite[8] = new Vehicle(ChessPiece.Color.Black);
    blackSuite[9] = new Cannon(ChessPiece.Color.Black);
    blackSuite[10] = new Cannon(ChessPiece.Color.Black);
    for (int i = 0; i < 5; i++) {
      blackSuite[11 + i] = new Soldier(ChessPiece.Color.Black);
    }

    redSuite[0] = new General(ChessPiece.Color.Red);
    redSuite[1] = new Guard(ChessPiece.Color.Red);
    redSuite[2] = new Guard(ChessPiece.Color.Red);
    redSuite[3] = new Premier(ChessPiece.Color.Red);
    redSuite[4] = new Premier(ChessPiece.Color.Red);
    redSuite[5] = new Horse(ChessPiece.Color.Red);
    redSuite[6] = new Horse(ChessPiece.Color.Red);
    redSuite[7] = new Vehicle(ChessPiece.Color.Red);
    redSuite[8] = new Vehicle(ChessPiece.Color.Red);
    redSuite[9] = new Cannon(ChessPiece.Color.Red);
    redSuite[10] = new Cannon(ChessPiece.Color.Red);
    for (int i = 0; i < 5; i++) {
      redSuite[11 + i] = new Soldier(ChessPiece.Color.Red);
    }

  }

  public ChessPiece[] getBlackSuite() {
    return blackSuite;
  }

  public ChessPiece[] getRedSuite() {
    return redSuite;
  }
}
