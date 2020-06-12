package com.example.chessgame;

import com.example.chessgame.chess.piece.ChessPiece;
import com.example.chessgame.chess.board.Chessboard;
import com.example.chessgame.chess.board.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * @author yulshi
 * @create 2020/06/09 17:23
 */
public class ChessboardTest {

  @Test
  public void testShuffle() {

    Chessboard chessboard = new Chessboard();
    for (ChessPiece[] pieces : chessboard.getGrid()) {
      for (ChessPiece piece : pieces) {
        Assertions.assertNotNull(piece);
        Assertions.assertTrue(piece.isHidden());
      }
    }

  }

  @Test
  public void testFlip() {

    Chessboard chessboard = new Chessboard();

    Position pos = new Position(3, 2);
    chessboard.flip(pos);

    Assertions.assertNull(chessboard.getSelectedPiece());
    Assertions.assertFalse(chessboard.locate(pos).isHidden());

    chessboard.display();

  }

  @Test
  public void testMove() {

    Chessboard chessboard = new Chessboard();
    ChessPiece[][] grid = chessboard.getGrid();

    Position targetPos = new Position(0, 2);

    grid[targetPos.getRow()][targetPos.getCol()] = null;

    Position sourcePos = new Position(0, 1);
    // 翻子
    chessboard.flip(sourcePos);
    // 选定
    chessboard.setSelectedPiece(chessboard.locate(sourcePos));

    chessboard.moveTo(targetPos);

    Assertions.assertNull(chessboard.locate(sourcePos));
    Assertions.assertNotNull(chessboard.locate(targetPos));

    System.out.println("-----------");
    chessboard.display();

  }

  @Test
  public void test() {

    for (int i = 0; i < 10; i++) {
      System.out.println(new Random().nextInt(2));
    }

  }


}
