package com.example.chessgame.service.impl;

import com.example.chessgame.chess.ChessGame;
import com.example.chessgame.chess.player.Player;
import com.example.chessgame.chess.board.Position;
import com.example.chessgame.message.ChessResponse;
import com.example.chessgame.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yulshi
 * @create 2020/06/09 21:19
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {

  private final SimpMessagingTemplate messagingTemplate;

  public GameServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  /**
   * Initiate a new game
   *
   * @param player
   */
  @Override
  public void initGame(Player player) {
    ChessGame chessGame = new ChessGame();
    chessGame.addPlayer(player);

    messagingTemplate.convertAndSend("/game/init", new ChessResponse<>(chessGame));

  }

  /**
   * A new player joined in
   *
   * @param gameId
   * @param player
   */
  @Override
  public void joinGame(String gameId, Player player) {

    // 发送信息给客户端的目标路径
    String destination = "/game/" + gameId;

    ChessGame chessGame = ChessGame.allGames.get(gameId);
    if (chessGame == null) {
      log.warn("No game found for " + gameId);
      messagingTemplate.convertAndSend(destination, new ChessResponse("没有找到游戏：" + gameId));
      return;
    }
    if (chessGame.getState() != ChessGame.State.NEW) {
      messagingTemplate.convertAndSend(destination, new ChessResponse("已经有两个玩家在下棋了，不能加入了"));
      return;
    }
    chessGame.addPlayer(player);
    messagingTemplate.convertAndSend(destination, new ChessResponse<>(chessGame));
  }

  /**
   * Play the game
   *
   * @param gameId
   * @param player
   * @param position
   */
  @Override
  public void play(String gameId, Player player, Position position) {

    // 发送信息给客户端的目标路径
    String destination = "/game/" + gameId;

    ChessGame chessGame = ChessGame.allGames.get(gameId);
    if (chessGame == null) {
      log.error("No game found for " + gameId);
      messagingTemplate.convertAndSend(destination, new ChessResponse("没有找到游戏：" + gameId));
      return;
    }

    if (!player.equals(chessGame.getActivePlayer())) {
      // 如果不是该Player走棋，则do nothing
      log.warn("不是该玩家走棋，忽略此操作");
      messagingTemplate.convertAndSend(destination, new ChessResponse("不是该玩家走棋，忽略此操作"));
      return;
    }
    chessGame.click(position);

    messagingTemplate.convertAndSend(destination, new ChessResponse<>(chessGame));

  }

  /**
   * 放弃比赛
   * @param gameId
   * @param player
   */
  @Override
  public void giveUp(String gameId, Player player) {

    String destination = "/game/" + gameId;
    ChessGame chessGame = ChessGame.allGames.get(gameId);
    if (chessGame == null) {
      log.error("No game found for " + gameId);
      messagingTemplate.convertAndSend(destination, new ChessResponse("没有找到游戏：" + gameId));
      return;
    }

    chessGame.endGame(player, player.getName() + "主动认输");
    messagingTemplate.convertAndSend(destination, new ChessResponse<>(chessGame));

  }

}
