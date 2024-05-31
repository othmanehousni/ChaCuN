package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ch.epfl.chacun.ActionEncoder.StateAction;

//todo handler? consumer??

/**
 * The Main class is the entry point of the ChaCuN application. It extends the JavaFX Application class
 * and sets up the graphical user interface (GUI) for the game.
 */
public final class Main extends Application {

    /**
     * Sets the property value by applying a unary operator to the current property value.
     *
     * @param operator the unary operator to apply
     * @param property the property to set
     * @param <T> the type of the property value
     */
    private static <T> void setProperty(UnaryOperator<T> operator, ObjectProperty<T> property) {
        property.setValue(operator.apply(property.getValue()));
    }

    /**
     * Updates the actions list and sets the new game state.
     *
     * @param actionsListP the property containing the list of actions
     * @param stateAction the new state-action pair
     * @param gameStateP the property containing the game state
     */
    private void updateActionsAndSetState(SimpleObjectProperty<List<String>> actionsListP,
                                          StateAction stateAction, SimpleObjectProperty<GameState> gameStateP) {

        setProperty(list-> {
            List<String> newStrList = new ArrayList<>(list);
            newStrList.add(stateAction.action());
           return newStrList;
        }, actionsListP);
        gameStateP.setValue(stateAction.state());
    }

    /**
     * The main entry point for all JavaFX applications. The start method is called to launch the whole game
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Get application parameters
        Parameters parameters = getParameters();
        List<String> unNamed = parameters.getUnnamed();
        Map<String, String> named = parameters.getNamed();

        // Initialize player colors using the arguments given to the program.
        Map<PlayerColor, String> playerColors = new TreeMap<>();
        for(int i = 0; i < unNamed.size(); i++) {
            playerColors.put(PlayerColor.values()[i], unNamed.get(i));
        }
        List<PlayerColor> playerColorsList = new ArrayList<>(playerColors.keySet());

        // Initialize random generator and shuffle each tile based on the seed.
        long seed = named.containsKey("seed") ? Long.parseUnsignedLong(named.get("seed")) : new Random().nextLong();
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create(seed);
        List<Tile> tilesToShuffle = new ArrayList<>(Tiles.TILES);
        //shuffling based on the seed that was given (the seed could be empty, thus the program wil shuffle it randomly).
        Collections.shuffle(tilesToShuffle, generator);

        // Group tiles by kind in order to extract each kind later on.
        Map<Tile.Kind, List<Tile>> tilesByKind = tilesToShuffle.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        // Create tile decks based on the map of tile kinds
        TileDecks tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        TextMakerFr textMaker = new TextMakerFr(playerColors);

        // Initialize game state
        GameState gameState =
                GameState.initial(playerColorsList,
                        tileDecks,
                        textMaker);

        // Create observable properties of the Game state.
        SimpleObjectProperty<GameState> gameStateP = new SimpleObjectProperty<>(gameState);

        // CREATION OF THE PLAYER LIST
        Node playersNode = PlayersUI.create(gameStateP, textMaker);


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create the observable value for all occupants. There are two possibilities :
        // - the only occupants that must be shown are the ones that were placed earlier, they appear most of the time.
        // - when placing the tile, the player must see his potential occupants but also the occupants that were placed
        // earlier.
        ObservableValue<Set<Occupant>> allOccupantsO = gameStateP.map(g-> {
            Set<Occupant> visibleOccupants = new HashSet<>();
            if (g.board().lastPlacedTile() != null && g.nextAction() == GameState.Action.OCCUPY_TILE) {
                //concatenating the potential occupants and also the already placed occupants.
                visibleOccupants.addAll(Stream.concat(g.board().occupants().stream(),
                        g.lastTilePotentialOccupants().stream()).toList());
            } else {
                visibleOccupants.addAll(g.board().occupants());
            }
            return visibleOccupants;
        });

        // Create the list of observable messages that have to be shown on the message board
        ObservableValue<List<MessageBoard.Message>> messagesListO = gameStateP
                .map(GameState::messageBoard)
                .map(MessageBoard::messages);

        // Create a set of highlighted tiles that is empty at the beginning of the game, this set will update based
        // on its implementation in MessageBoardUI
        SimpleObjectProperty<Set<Integer>> highlightedTilesP = new SimpleObjectProperty<>(Set.of());

        //CREATION OF THE MESSAGE BOARD
        Node messageBoardNode = MessageBoardUI.create(messagesListO, highlightedTilesP);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Create the observable encoded list of actions that will be updated accordingly to the players' actions.
        List<String> actionsList = new ArrayList<>();
        SimpleObjectProperty<List<String>> actionsListP = new SimpleObjectProperty<>(actionsList);

        // The base rotation is created and set to none.
        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);

        // An event handler that handles changes related to the rotation property
        // by adding to the current rotation a left turn of a right turn :
        // alt + right click : turn right.
        // right click : turn left.
        Consumer <Rotation> onRotateTile = rotation -> setProperty(rotation::add,tileToPlaceRotationP);

        // An event handler that places the tileToPlace, encodes to the actionList and then updates the state
        // once done, it returns to the base rotation : none.
        Consumer <Pos> onPlaceTile = tile -> {
            GameState currentGameState = gameStateP.getValue();
            PlacedTile placedTile = new PlacedTile(currentGameState.tileToPlace(),
                    currentGameState.currentPlayer(),
                    tileToPlaceRotationP.getValue(), tile);
            StateAction stateAction = ActionEncoder.withPlacedTile(currentGameState, placedTile);
            updateActionsAndSetState(actionsListP, stateAction, gameStateP);
            tileToPlaceRotationP.set(Rotation.NONE);
        };

        // when selecting and doing something with an occupant, the player must either be in PLACE_TILE or RETAKE_PAWN
        // note that in a case of a null occupant, each of the branches will be given a null occupant, the condition
        // that takes in an id directly goes through if the occupant is null, thus never giving a NullPointerException.

        Consumer <Occupant> onSelectOccupant = occupant -> {
            int id = occupant == null ? -1 : Zone.tileId(occupant.zoneId());
            switch (gameStateP.getValue().nextAction()) {
                // the player wants to place/or not an occupant.
                // in both cases, the list of actions is updated and the state is set
                case OCCUPY_TILE -> {
                    // this is the exception that we were talking about earlier, it never happens.
                    if (occupant == null || gameStateP.getValue().board().lastPlacedTile().id() == id) {
                        StateAction stateAction = ActionEncoder.withNewOccupant(gameStateP.getValue(), occupant);
                        updateActionsAndSetState(actionsListP, stateAction, gameStateP);
                    }
                }
                // the player wants to take/or not one of his pawns.
                // in both cases, the list of actions is updated and the state is set
                case RETAKE_PAWN -> {
                    if (occupant == null || occupant.kind() == Occupant.Kind.PAWN
                            && gameStateP.getValue().board().tileWithId(id).placer() == gameStateP.get().currentPlayer()) {
                        StateAction stateAction = ActionEncoder.withOccupantRemoved(gameStateP.getValue(), occupant);
                        updateActionsAndSetState(actionsListP, stateAction, gameStateP);
                    }
                }
            }
        };

        // CREATION OF THE BOARD
        Node boardNode = BoardUI
                .create(Board.REACH,
                        gameStateP, tileToPlaceRotationP,
                        allOccupantsO, highlightedTilesP,
                        onRotateTile, onPlaceTile, onSelectOccupant);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Consumer<String> onAction = textEntry -> {
            ActionEncoder.StateAction action = ActionEncoder.decodeAndApply(gameStateP.getValue(), textEntry);
            if(action != null) {
                updateActionsAndSetState(actionsListP, action, gameStateP);
                tileToPlaceRotationP.set(Rotation.NONE);
            }
        };

        Node actionsNode = ActionUI.create(actionsListP, onAction);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // observing the next tile to place in order to update the shown tile on the deck
        ObservableValue<Tile> nextTileO = gameStateP.map(GameState::tileToPlace);
        // the remaining number of tiles in each kind.
        ObservableValue<Integer> remainingNormalO = gameStateP.map(g-> g.tileDecks().normalTiles().size());
        ObservableValue<Integer> remainingMenhirO = gameStateP.map(g-> g.tileDecks().menhirTiles().size());
        // which text must be shown based on the current state of the game. The base text is an empty string.
        ObservableValue<String> TileTextO = gameStateP.map(g -> {
            if (g.nextAction() == GameState.Action.OCCUPY_TILE) {
                return textMaker.clickToOccupy();
            } else if (g.nextAction() == GameState.Action.RETAKE_PAWN) {
                return textMaker.clickToUnoccupy();
            } else {
                return "";
            }
        });

        //CREATION OF THE DECK AND THE TILE TO PLACE
        Node deckNode = DecksUI.create(nextTileO, remainingNormalO, remainingMenhirO, TileTextO, onSelectOccupant);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // putting the deck and the deck and the text field/shown encoded actions in a single vbox in order to put it in
        // the pane that regroups the player list, the actions, the message board and the decks/tile to place
        VBox deckActionVbox = new VBox();
        deckActionVbox.getChildren().addAll(actionsNode, deckNode);

        // creating the message board as a scroll pane which the player can scroll through.
        ScrollPane messageBoardPane = new ScrollPane(messageBoardNode);

        //fitting the whole node with the right dimensions.
        messageBoardPane.setFitToHeight(true);
        messageBoardPane.setFitToWidth(true);

        // the pane regrouping the message board, the deck, the tile to place, the actions and the player list.
        BorderPane paneRight = new BorderPane();
        paneRight.setBottom(deckActionVbox);
        paneRight.setTop(playersNode);
        paneRight.setCenter(messageBoardPane);

        // creating the whole border pane which will show the board, as a main component, thus making it in the center
        // and big. and also putting the pane with the rest of the components on the right.
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(boardNode);
        mainPane.setRight(paneRight);

        // at the end of all of this, finally setting the game state with the tile 56 (start tile) placed
        setProperty(GameState::withStartingTilePlaced,gameStateP);

        // showing the stage with as its scene the main regrouping everything.
        primaryStage.setScene(new Scene(mainPane, 1440, 1000));
        primaryStage.setTitle("ChaCuN");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
