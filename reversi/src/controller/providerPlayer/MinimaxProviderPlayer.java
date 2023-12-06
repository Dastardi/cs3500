package controller.providerPlayer;

import controller.MoveType;
import controller.Pair;
import controller.Player;
import controller.Translator;
import model.Coordinate;
import provider.model.HexPosn;
import provider.model.HexagonalReversi;
import provider.strategy.AbstractStrategy;
import provider.strategy.AggregateStrategy;
import provider.strategy.MinimaxStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An AI player in a Reversi game that values moves based on how little value they provide
 * for the opponent based on the opponent's strategy and picks the one that leaves the opponent worst off.
 */
public class MinimaxProviderPlayer implements Player {
  //represents the strategy this player uses
  private final AggregateStrategy strategy;
  //represents the game model
  private final HexagonalReversi model;

  /**
   * Constructs the AI and its composite strategy.
   * @param model the model from which the strategy reads.
   */
  public MinimaxProviderPlayer(HexagonalReversi model) {
    this.model = Objects.requireNonNull(model);
    List<AbstractStrategy> strategyList = new ArrayList<>();
    strategyList.add(new MinimaxStrategy());
    this.strategy = new AggregateStrategy(strategyList);
  }

  //if the optional is empty, return NOVALID and null
  //if the optional has a value, return VALID and the translation of the HexPosn into a Coordinate
  @Override
  public Pair<MoveType, Coordinate> move() {
    Optional<HexPosn> move = strategy.bestMove(this.model, this.model.getNextTurn());
    if (move.isPresent()) {
      return new Pair<>(MoveType.VALID, Translator.hexPosnToCoordinate(model.getNumLayers(), move.get()));
    } else {
      return new Pair<>(MoveType.NOVALID, null);
    }
  }
}