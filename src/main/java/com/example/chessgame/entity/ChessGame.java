package com.example.chessgame.entity;

import com.example.chessgame.chess.Cannon;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yulshi
 * @create 2020/06/09 22:20
 */
@Slf4j
public class ChessGame {

  public static ConcurrentHashMap<String, ChessGame> allGames = new ConcurrentHashMap<>();

  private final Chessboard chessboard;
  private Player player1;
  private Player player2;
  private Player activePlayer;
  private List<Player> players;

  private final String id;
  private boolean endGameFlag = false;
  private State state;

  public ChessGame() {
    this.chessboard = new Chessboard();
    this.id = UUID.randomUUID().toString();
    allGames.put(id, this);
    this.state = State.NEW;
    this.activePlayer = null;
  }

  public void addPlayer(Player player) {

    // 两个玩家都加入了游戏，如果此时还有加入游戏的请求，直接忽略
    if (player1 != null && player2 != null) {
      return;
    }

    if (player1 == null) {
      player1 = player;
    } else {
      player2 = player;
      startGame();
    }
  }

  /**
   * 玩家点击目标位置，该位置可能是任意的位置，上面可能有子，也可能没有子
   *
   * @param position
   */
  public synchronized void click(Position position) {

    Player player = this.getActivePlayer();   // 当前玩家
    Player opponent = opponentPlayer(player); // 当前玩家的对手

    ChessPiece piece = chessboard.locate(position);
    if (player.getColor() == null) {
      // 说明还没有选定颜色
      piece.setHidden(false);
      player.setColor(piece.getColor());
      opponent.setColor(piece.getColor() == ChessPiece.Color.Black ?
              ChessPiece.Color.Red : ChessPiece.Color.Black);
      activate(opponent);
      return;
    }

    // 如果之前没有选定的棋子，则可能进行的操作，要么是翻子，要么是选定棋子
    if (chessboard.getSelectedPiece() == null) {

      // 如果该位置没有棋子，则什么都不做
      if (piece == null) {
        return;
      }

      // 如果棋子是背面朝上，则进行翻子操作
      if (piece.isHidden()) {
        chessboard.flip(position);
        activate(opponent); // 翻子以后，就轮到对手下棋了
        return;
      }

      // 如果棋子的颜色与当前Player的颜色不一致，则什么都不做
      if (player.getColor() != piece.getColor()) {
        return;
      }

      // 如果棋子正面朝上，则选定棋子
      chessboard.setSelectedPiece(piece);
      log.info("选定棋子：" + piece);
      return;

    } else { // 如果之前已经有选定的棋子，则可能的操作是移动或吃子

      ChessPiece selectedPiece = chessboard.getSelectedPiece();

      // 如果选定的棋子与目标位置一样，则取消选定的棋子
      if (selectedPiece.getPosition().same(position)) {
        chessboard.setSelectedPiece(null);
        log.info("取消选定的棋子");
        return;
      }

      // 如果选定的棋子不是炮，则只能移动一个格子，否则视为无效
      if (!(selectedPiece instanceof Cannon) && !selectedPiece.getPosition().adjacentOf(position)) {
        if (log.isDebugEnabled()) {
          invalidOperation("棋子的移动范围不合法：from " + selectedPiece.getPosition() + " to " + position);
        }
        return;
      }

      // 如果选定的棋子与目标位置是相邻的，则尝试进行吃子或兑子操作
      if (selectedPiece.getPosition().adjacentOf(position)) {
        // 选定的棋子与目标位置的棋子是相邻的位置
        if (piece == null) { // 移动
          chessboard.moveTo(position);
          activate(opponent);
        } else { // 尝试兑子或吃子
          if (chessboard.attack(piece)) {
            activate(opponent);
          } else {
            invalidOperation("不能攻击对方");
          }
        }
        return;
      }

      // 如果选定的棋子是炮，则攻击的目标必须是隔着一个子的对方的棋子
      if (selectedPiece instanceof Cannon) {

        // 如果目标位置上没有子，则为无效操作
        if (piece == null || piece.getColor() == selectedPiece.getColor()) {
          invalidOperation("无效操作，被炮攻击的位置上必须是对方的棋子");
          return;
        }

        int sRow = selectedPiece.getPosition().getRow();
        int sCol = selectedPiece.getPosition().getCol();
        int tRow = position.getRow();
        int tCol = position.getCol();

        if (sRow == tRow) { // 两个子是否在同一行
          if (sCol > tCol) { // 保证sCol小于tCol，以便查看是否中间隔着一个子
            int temp = sCol;
            sCol = tCol;
            tCol = temp;
          }
          int countOfPieceBetween = 0;
          for (int i = sCol + 1; i < tCol; i++) {
            if (chessboard.locate(new Position(sRow, i)) != null) {
              countOfPieceBetween++;
            }
          }
          if (countOfPieceBetween != 1) {
            invalidOperation("无效操作，炮只能攻击同一行上隔开一个子的对方棋子");
            return;
          }
          chessboard.moveTo(position);
          activate(opponent);
          return;
        }

        if (sCol == tCol) { // 两个子是否在同一列
          if (sRow > tRow) {
            int temp = sRow;
            sRow = tRow;
            tRow = temp;
          }
          int countOfPieceBetween = 0;
          for (int i = sRow + 1; i < tRow; i++) {
            if (chessboard.locate(new Position(i, sCol)) != null) {
              countOfPieceBetween++;
            }
          }
          if (countOfPieceBetween != 1) {
            invalidOperation("无效操作，炮只能攻击同一列上隔开一个子的对方棋子");
            return;
          }
          chessboard.moveTo(position);
          activate(opponent);
          return;
        }
        invalidOperation("无效操作，炮只能攻击同一行或同一列上的棋子");
        return;
      }

    }

  }

  /**
   * 表明当前操作无效，记录日志，取消选定等
   *
   * @param message
   */
  private void invalidOperation(String message) {
    chessboard.setSelectedPiece(null);
    log.info(message);
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public Chessboard getChessboard() {
    return chessboard;
  }

  public String getId() {
    return id;
  }

  private void startGame() {
    // Randomly choose a player
    Player[] players = new Player[]{player1, player2};
    Player player = players[new Random().nextInt(players.length)];
    activate(player);
    state = State.STARTED;
  }

  private Player opponentPlayer(Player player) {
    return player.equals(player1) ? player2 : player1;
  }

  private void activate(Player player) {
    activePlayer = player;
    player.setActive(true);
    opponentPlayer(player).setActive(false);

    // 检查对手的棋子是否已经全部被吃掉或兑掉
    ChessPiece[][] grid = chessboard.getGrid();
    ChessPiece.Color color = player.getColor();
    boolean hasPiece = false;
    outer:
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] != null && grid[i][j].getColor() == color) {
          hasPiece = true;
          break outer;
        }
      }
    }
    if (!hasPiece) {
      // 对方已经没有棋子了，赢得比赛
      endGame(player, player.getName() + "的棋子被吃光");
    }

    // TODO: 判断是否应该和棋（即双方都不能吃光对方的子）

  }

  public State getState() {
    return state;
  }

  /**
   * 以loser输掉比赛结束
   *
   * @param loser
   */
  public void endGame(Player loser, String reason) {
    log.info("本局游戏结束：" + reason);
    Player player = opponentPlayer(loser);
    player.winOneGame(this.id);
    this.state = State.END;
  }

  /**
   * @author yulshi
   * @create 2020/06/10 17:00
   */
  public enum State {
    NEW, STARTED, END
  }
}
