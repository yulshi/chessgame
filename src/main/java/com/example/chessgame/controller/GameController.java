package com.example.chessgame.controller;

import com.example.chessgame.message.ActionRequest;
import com.example.chessgame.chess.player.Player;
import com.example.chessgame.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author yulshi
 * @create 2020/06/10 12:30
 */
@Controller
@Slf4j
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @MessageMapping("/game/init")
  public void initGame(Player player) throws Exception {
    log.info("A new player is in..." + player);
    gameService.initGame(player);

  }

  @MessageMapping("/game/join/{id}")
  public void joinGame(Player player, @DestinationVariable("id") String id) throws Exception {
    log.info("A new player joined the game " + id + "..." + player);
    gameService.joinGame(id, player);
  }

  /**
   * 下棋走子
   *
   * @param action
   * @param id
   */
  @MessageMapping("/game/action/{id}")
  public void action(ActionRequest action, @DestinationVariable("id") String id) {
    log.info("gameId: " + id + ", 操作：" + action);
    gameService.play(id, action.getPlayer(), action.getPosition());
  }

  /**
   * 一个玩家主动认输，放弃该局比赛，对手得分
   *
   * @param action
   * @param id
   */
  @MessageMapping("/game/giveup/{id}")
  public void giveUp(ActionRequest action, @DestinationVariable("id") String id) {
    log.info("gameId: " + id + ", player：" + action.getPlayer().getName() + " 放弃了比赛");
    gameService.giveUp(id, action.getPlayer());
  }

}
