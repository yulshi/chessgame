package com.example.chessgame.message;

import com.example.chessgame.chess.player.Player;
import com.example.chessgame.chess.board.Position;

/**
 * @author yulshi
 * @create 2020/06/10 16:53
 */
public class ActionRequest {

  private Player player;
  private Position position;

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "Action{" +
            "player=" + player +
            ", position=" + position +
            '}';
  }
}
