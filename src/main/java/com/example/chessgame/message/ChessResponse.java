package com.example.chessgame.message;

import com.example.chessgame.chess.ChessGame;

/**
 * @author yulshi
 * @create 2020/06/10 19:39
 */
public class ChessResponse<T> {

  private boolean success;
  private String message;
  private T payload;

  public ChessResponse(String message) {
    this(message, null);
  }

  public ChessResponse(T payload) {
    this(null, payload);
    if(payload instanceof ChessGame) {
      ChessGame chessGame = (ChessGame)payload;
      chessGame.getChessboard().display();
      System.out.println("--------------");
    }
  }

  public ChessResponse(String message, T payload) {
    this.message = message;
    this.payload = payload;
    if(payload == null) {
      this.success = false;
    } else {
      this.success = true;
    }
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
