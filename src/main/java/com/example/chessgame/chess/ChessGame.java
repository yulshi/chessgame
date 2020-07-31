package com.example.chessgame.chess;

import com.example.chessgame.chess.board.Chessboard;
import com.example.chessgame.chess.board.Position;
import com.example.chessgame.chess.piece.ChessPiece;
import com.example.chessgame.chess.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author yulshi
 * @create 2020/06/09 22:20
 */
@Slf4j
public class ChessGame {

  public static ConcurrentHashMap<String, ChessGame> allGames = new ConcurrentHashMap<>();

  private final Chessboard chessboard;

  private Player activePlayer;
  private final List<Player> players;
  private final int countOfPlayers = 2;
  private int activePlayerIndex = -1;

  private final String id;
  private State state;

  public ChessGame() {
    this.chessboard = new Chessboard();
    this.id = UUID.randomUUID().toString();
    allGames.put(id, this);
    this.state = State.NEW;
    this.activePlayer = null;
    this.players = new ArrayList<>();
  }

  public synchronized void addPlayer(Player player) {

    // 两个玩家都加入了游戏，如果此时还有加入游戏的请求，直接忽略
    if (players.size() == countOfPlayers) {
      return;
    }

    if (players.size() < countOfPlayers) {
      players.add(player);
    }

    if (players.size() == countOfPlayers) {
      // 所有棋手都到达了，可以开始游戏了
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
    //Player opponent = opponentPlayer(player); // 当前玩家的对手

    ChessPiece piece = chessboard.locate(position);
    if (player.getColor() == null) {
      // 说明还没有选定颜色
      piece.setHidden(false);
      player.setColor(piece.getColor());
      updateOpponets(player, opponent -> {
        opponent.setColor(piece.getColor() == ChessPiece.Color.Black ?
                ChessPiece.Color.Red : ChessPiece.Color.Black);
      });

      activateNext();
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
        activateNext(); // 翻子以后，就轮到对手下棋了
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
      if (selectedPiece.getAttackType() == ChessPiece.AttackType.Adjacent
              && !selectedPiece.getPosition().adjacentOf(position)) {
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
          activateNext();
        } else { // 尝试兑子或吃子
          if (chessboard.attack(piece)) {
            activateNext();
          } else {
            invalidOperation("不能攻击对方");
          }
        }
        return;
      }

      // 如果选定的棋子是炮，则攻击的目标必须是隔着一个子的对方的棋子
      if (selectedPiece.getAttackType() == ChessPiece.AttackType.Segregative) {

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
          activateNext();
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
          activateNext();
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
    activePlayerIndex = new Random().nextInt(countOfPlayers);
    activateNext();
    state = State.STARTED;
  }

  /**
   * 轮到下一个棋手下棋
   */
  private void activateNext() {

    activePlayerIndex = (activePlayerIndex + 1) % countOfPlayers;
    activePlayer = players.get(activePlayerIndex);
    activePlayer.setActive(true);
    updateOpponets(activePlayer, opponent -> {
      opponent.setActive(false);
    });

    // 检查对手的棋子是否已经全部被吃掉或兑掉
    ChessPiece.Color color = activePlayer.getColor();
    if (color == null) {
      // 说明棋手还没有选定颜色
      return;
    }

    ChessPiece[][] grid = chessboard.getGrid();
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
      // TODO: 需要一个daemon来监测什么时候，有一个Player的子全部被吃掉。
      endGame(activePlayer, activePlayer.getName() + "的棋子被吃光");
    }

    // TODO: 判断是否应该和棋（即双方都不能吃光对方的子）

  }

  /**
   * 以loser输掉比赛结束
   *
   * @param loser
   */
  public void endGame(Player loser, String reason) {
    log.info("本局游戏结束：" + reason);
    updateOpponets(loser, opponent -> {
      opponent.winOneGame(this.id);
    });
    this.state = State.END;
  }

  private void updateOpponets(Player player, Consumer<Player> consumer) {
    for (Player opponet : players) {
      if (!opponet.equals(player)) {
        consumer.accept(opponet);
      }
    }
  }

  public State getState() {
    return state;
  }

  /**
   * @author yulshi
   * @create 2020/06/10 17:00
   */
  public enum State {
    NEW, STARTED, END
  }
}
