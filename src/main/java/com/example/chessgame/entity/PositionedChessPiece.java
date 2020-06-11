package com.example.chessgame.entity;

/**
 * A chess piece that is or had been put on a chessboard
 *
 * @author yulshi
 * @create 2020/06/09 14:58
 */
public class PositionedChessPiece {

  private final ChessPiece chessPiece;

  private final Position position;

  public PositionedChessPiece(ChessPiece chessPiece) {
    this.chessPiece = chessPiece;
    this.position = new Position(-1, -1);
  }

  /**
   * Move the piece around by step
   *
   * @param direction
   * @param step
   */
  public void moveBy(Direction direction, int step) {

    Position newPos = null;

    switch (direction) {
      case Left:
        if (position.getCol() - step >= 0) {
          newPos = new Position(position.getRow(), position.getCol() - step);
        }
        break;
      case Right:
        if (position.getCol() + step < 8) {
          newPos = new Position(position.getRow(), position.getCol() + step);
        }
        break;
      case Up:
        if (position.getRow() - step >= 0) {
          newPos = new Position(position.getRow() - step, position.getCol());
        }
        break;
      case Down:
        if (position.getRow() + step < 4) {
          newPos = new Position(position.getRow() + step, position.getCol());
        }
        break;
    }

    if (newPos == null) {
      System.out.println("can not move to the specified position");
      throw new IllegalStateException(
              String.format("The chess piece at[%d,%d] can not be moved %s",
                      position.getRow(), position.getCol(), direction));
    }

    this.moveTo(newPos);

  }

  /**
   * Move this piece to the specified position
   *
   * @param position
   */
  public void moveTo(Position position) {



    this.position.moveTo(position);
  }

//  /**
//   * Attack the specified piece
//   *
//   * @param piece
//   */
//  public void attack(ChessPiece piece) {
//    int diff = this.weight - piece.weight;
//
//    // 两个子的权重相同，视为兑子
//    if (diff == 0) {
//      this.quit();
//      piece.quit();
//    }
//
//    // 如果差小于6并且大于0，则说明A可以吃掉B
//    // 如果差等于-6，说明A也可以吃掉B
//    if (diff < 6 && diff > 0 || diff == -6) {
//      Position pos = piece.position;
//      piece.quit();
//      this.moveTo(pos);
//    }
//
//  }

  /**
   * 从棋盘拿走
   */
  public void quit() {
    this.position.setRow(-1);
    this.position.setCol(-1);
  }


  public static enum Direction {
    Up, Down, Left, Right;
  }
}
