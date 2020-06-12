package com.example.chessgame.service;

import com.example.chessgame.chess.ChessGame;
import com.example.chessgame.chess.player.Player;
import com.example.chessgame.chess.board.Position;

import java.util.List;

/**
 * @author yulshi
 * @create 2020/06/11 23:19
 */
public interface GameService {

  /**
   * Initiate a new game
   */
  void initGame(Player player);

  /**
   * A new player joined in
   */
  void joinGame(String gameId, Player player);

  /**
   * Play the game
   */
  void play(String gameId, Player player, Position position);

  /**
   * 放弃比赛
   */
  void giveUp(String gameId, Player player);

  /**
   * List all the the game ids that are in the specified state
   */
  List<String> listGames(ChessGame.State state);

}
