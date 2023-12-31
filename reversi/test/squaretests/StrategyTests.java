package squaretests;

import model.SquareBasicReversi;
import model.Coordinate;
import model.PlayerColor;
import model.ReversiModel;
import org.junit.Before;
import org.junit.Test;
import strategy.ReversiStrategy;
import strategy.SquareCompositeStrategy;
import strategy.SquareMaxCaptureStrategy;
import strategy.SquareCornerStrategy;
import strategy.SquareAvoidCornerAdjacentStrategy;
import strategy.UpperLeftMostStrategy;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests the AI strategies in this Reversi game.
 */
public class StrategyTests {
  StringBuilder log;
  ReversiModel basicStartModel;
  ReversiModel cornerAndUpperLeftOpenModel;
  ReversiModel cornerAdjacentCornerOpenModel;
  MockReversiModel basicMock;
  MockReversiModel liarMock;

  //three strategies that use the basic model, which has only the starting tiles
  ReversiStrategy basicMaxCapture;
  ReversiStrategy basicAvoidCornerAdjacent;
  ReversiStrategy basicCornerCapture;

  //three strategies that use a model set up with a corner available to capture in
  //the bottom right
  ReversiStrategy cornerMaxCapture;
  ReversiStrategy cornerAvoidCornerAdjacent;
  ReversiStrategy cornerCornerCapture;

  //three strategies that use a model with a corner open in the bottom right, a corner-adjacent
  //tile open in the top left, and a non-corner adjacent tile open in the middle
  ReversiStrategy adjacentMaxCapture;
  ReversiStrategy adjacentAvoidCornerAdjacent;
  ReversiStrategy adjacentCornerCapture;

  //max capture strategies to test using the mocks
  ReversiStrategy mockMaxCapture;
  ReversiStrategy liarMockMaxCapture;

  @Before
  public void init() {
    //model with just the starting tiles in place
    this.basicStartModel = new SquareBasicReversi(8);

    //model with an opening to take two tiles in the upper left (upper left is preferred by
    // the strategies as a tiebreaker) or two tiles in the bottom right corner.
    this.cornerAndUpperLeftOpenModel = new SquareBasicReversi(8);
    cornerAndUpperLeftOpenModel.getTileAt(new Coordinate(6,7)).placeDisc(PlayerColor.WHITE);
    cornerAndUpperLeftOpenModel.getTileAt(new Coordinate(5,7)).placeDisc(PlayerColor.WHITE);
    cornerAndUpperLeftOpenModel.getTileAt(new Coordinate(4,7)).placeDisc(PlayerColor.BLACK);
    cornerAndUpperLeftOpenModel.getTileAt(new Coordinate(3,5)).placeDisc(PlayerColor.WHITE);

    //model with an opening to take two tiles by moving next to a corner in the upper left,
    //take two tiles by moving further away from a corner in the bottom right,
    //or take two tiles by moving to the bottom right corner.
    this.cornerAdjacentCornerOpenModel = new SquareBasicReversi(8);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(6,7)).placeDisc(PlayerColor.WHITE);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(5,7)).placeDisc(PlayerColor.WHITE);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(4,7)).placeDisc(PlayerColor.BLACK);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(3,5)).placeDisc(PlayerColor.WHITE);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(2,0)).placeDisc(PlayerColor.WHITE);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(3,0)).placeDisc(PlayerColor.WHITE);
    cornerAdjacentCornerOpenModel.getTileAt(new Coordinate(4,0)).placeDisc(PlayerColor.BLACK);

    this.log = new StringBuilder();

    this.basicMock = new MockReversiModel(log);
    this.liarMock = new LiarMockReversiModel(log);

    this.basicMaxCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(basicStartModel),
        new UpperLeftMostStrategy());
    this.basicAvoidCornerAdjacent =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(basicStartModel),
        new SquareCompositeStrategy(new SquareAvoidCornerAdjacentStrategy(basicStartModel),
            new UpperLeftMostStrategy()));
    this.basicCornerCapture =
        new SquareCompositeStrategy(new SquareCornerStrategy(basicStartModel),
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(basicStartModel),
            new UpperLeftMostStrategy()));


    this.cornerMaxCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAndUpperLeftOpenModel),
        new UpperLeftMostStrategy());
    this.cornerAvoidCornerAdjacent =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAndUpperLeftOpenModel),
        new SquareCompositeStrategy(new
            SquareAvoidCornerAdjacentStrategy(cornerAndUpperLeftOpenModel),
            new UpperLeftMostStrategy()));
    this.cornerCornerCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAndUpperLeftOpenModel),
        new SquareCompositeStrategy(new SquareCornerStrategy(cornerAndUpperLeftOpenModel),
            new UpperLeftMostStrategy()));

    this.adjacentMaxCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAdjacentCornerOpenModel),
            new UpperLeftMostStrategy());
    this.adjacentAvoidCornerAdjacent =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAdjacentCornerOpenModel),
            new SquareCompositeStrategy(new
                SquareAvoidCornerAdjacentStrategy(cornerAdjacentCornerOpenModel),
                new UpperLeftMostStrategy()));
    this.adjacentCornerCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(cornerAdjacentCornerOpenModel),
            new SquareCompositeStrategy(new SquareCornerStrategy(cornerAdjacentCornerOpenModel),
                new UpperLeftMostStrategy()));

    this.mockMaxCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(basicMock),
            new UpperLeftMostStrategy());
    this.liarMockMaxCapture =
        new SquareCompositeStrategy(new SquareMaxCaptureStrategy(liarMock),
            new UpperLeftMostStrategy());
  }

  //tests that all strategies break ties correctly by moving in the top left.
  @Test
  public void testAllStrategiesHaveSameStart() {
    //check that all three strategies have the same starting move
    assertEquals(new Coordinate(4,2),
        basicMaxCapture.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(4,2),
        basicAvoidCornerAdjacent.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(4,2),
        basicCornerCapture.chooseMove(new ArrayList<>()).get(0));
  }

  //tests that all three strategies will prioritize the higher-value move
  @Test
  public void testStrategiesTakeHigherValueMove() {
    basicStartModel.move(new Coordinate(4, 2));
    basicStartModel.move(new Coordinate(3, 2));
    basicStartModel.move(basicMaxCapture.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(3,1),
        basicMaxCapture.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(3,1),
        basicAvoidCornerAdjacent.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(3,1),
        basicCornerCapture.chooseMove(new ArrayList<>()).get(0));
  }

  //tests that corner strategy prefers corners despite the move losing the top left tiebreaker,
  //that avoid corner strategy doesn't move next to a corner despite it being in the top left,
  //and that max capture moves in the top left regardless of strategic implications
  @Test
  public void testDifferencesBetweenAllThree() {
    assertEquals(new Coordinate(1,0),
        adjacentMaxCapture.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(3,6),
        adjacentAvoidCornerAdjacent.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(7,7),
        adjacentCornerCapture.chooseMove(new ArrayList<>()).get(0));
  }

  //tests that corner goes to the corner and that when it's not worrying about being next to
  //a corner, avoid corner capture strategy has the same behavior as max capture strategy
  @Test
  public void testCornerStrategyFocusesOnCorner() {
    assertEquals(new Coordinate(3,6),
        cornerMaxCapture.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(3,6),
        cornerAvoidCornerAdjacent.chooseMove(new ArrayList<>()).get(0));
    assertEquals(new Coordinate(7,7),
        cornerCornerCapture.chooseMove(new ArrayList<>()).get(0));
  }

  //the mock model appends messages to the log. We want to ensure that all eight potential
  //moves are checked, as well as that the strategy checks every tile in the board.
  //in order to see the entire message, check strategy-transcript.txt.
  @Test
  public void testMaxCaptureMock() {
    mockMaxCapture.chooseMove(new ArrayList<>());
    assertTrue(log.toString().contains("Getting score at 4, 2"));
    assertTrue(log.toString().contains("Getting score at 5, 3"));
    assertTrue(log.toString().contains("Getting score at 2, 4"));
    assertTrue(log.toString().contains("Getting score at 3, 5"));
    assertEquals(new Coordinate(4,2), mockMaxCapture.chooseMove(new ArrayList<>()).get(0));
  }

  //liar mock alters the validity check to ensure that the strategy follows the model in
  //determining its choices for best moves.
  @Test
  public void testLiarMock() {
    assertEquals(new Coordinate(0,0), liarMockMaxCapture.chooseMove(new ArrayList<>()).get(0));
  }
}
