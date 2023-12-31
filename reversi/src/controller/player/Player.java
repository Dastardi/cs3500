package controller.player;

import controller.MoveType;
import controller.Pair;
import model.Coordinate;

/**
 * An interface for a player in a game of Reversi. This interface is very broad
 * and can be implemented by both AI and human players and simply provides the ability
 * to make a move, in the form of the Coordinate for that move and its status (see MoveType enum).
 */
public interface Player {
  /**
   * Returns a Pair with a status and a Coordinate. The Coordinate will be null in two of three
   * cases, and is not accessed in those cases - when the player is a HUMAN or does not find a
   * valid move (and therefore has to pass), the field will not be accessed. When it finds a valid
   * move, it will return VALID and the first coordinate of the list found by the strategy.
   * @return a MoveType (VALID, NOVALID, HUMAN) and a Coordinate (null or the move to make)
   */
  Pair<MoveType, Coordinate> move();
}
