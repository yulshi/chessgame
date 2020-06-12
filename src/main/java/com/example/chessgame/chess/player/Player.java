package com.example.chessgame.chess.player;

import com.example.chessgame.chess.piece.ChessPiece;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yulshi
 * @create 2020/06/09 20:56
 */
public class Player {

  private String name;
  private ChessPiece.Color color = null;
  private boolean active;
  private Map<String, Integer> scores = new HashMap<>();

  public Player() {
  }

  public Player(String name) {
    this();
    this.name = name;
  }

  public void winOneGame(String gameId) {
    Integer score = scores.get(gameId);
    if (score == null) {
      score = 1;
    } else {
      score += 1;
    }
    scores.put(gameId, score);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public ChessPiece.Color getColor() {
    return color;
  }

  public void setColor(ChessPiece.Color color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Player{" +
            "name='" + name + '\'' +
            ", color=" + color +
            ", active=" + active +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
