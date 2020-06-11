package com.example.chessgame.entity;

/**
 * @author yulshi
 * @create 2020/06/09 10:30
 */
public class ChessPiece {

  // All immutable properties
  private final String name;
  private final Color color;
  private final int weight;
  private final AttackType attackType;

  // All mutable properties
  private Position position;
  private boolean hidden;

  public ChessPiece(String name, Color color, int weight, AttackType attackType) {
    this.name = name;
    this.color = color;
    this.weight = weight;
    this.attackType = attackType;
    this.position = new Position(-1, -1);
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public int getWeight() {
    return weight;
  }

  public AttackType getAttackType() {
    return attackType;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  @Override
  public String toString() {
    return "ChessPiece{" +
            "name='" + name + '\'' +
            ", color=" + color +
            ", weight=" + weight +
            ", position=" + position +
            ", hidden=" + hidden +
            '}';
  }

  /**
   * @author yulshi
   * @create 2020/06/09 10:33
   */
  public static enum Color {
    Black, Red;
  }

  /**
   * @author yulshi
   * @create 2020/06/09 10:35
   */
  public static enum AttackType {
    Adjacent, Segregative;
  }

}
