package com.example.chessgame;

import com.example.chessgame.chess.ChessGame;
import com.example.chessgame.chess.player.Player;
import com.example.chessgame.chess.board.Position;
import org.junit.jupiter.api.Test;

/**
 * @author yulshi
 * @create 2020/06/09 22:50
 */
public class ChessGameTest {

  @Test
  public void testInit() {
    ChessGame chessGame = new ChessGame();
    chessGame.addPlayer(new Player("Jimmy"));
    chessGame.addPlayer(new Player("Trudy"));

    chessGame.click(new Position(1, 1));

    chessGame.getChessboard().display();
    System.out.println(chessGame.getActivePlayer());

    chessGame.click(new Position(2,2 ));
    chessGame.getChessboard().display();
    System.out.println(chessGame.getActivePlayer());

  }

}
