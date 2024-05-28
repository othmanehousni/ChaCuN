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


public final class Main extends Application {

    //todo handler? consumer??
    private static <T> void setProperty(UnaryOperator<T> operator, ObjectProperty<T> property) {
        property.setValue(operator.apply(property.getValue()));
    }

    private void updateActionsAndSetState(SimpleObjectProperty<List<String>> actionsListP,
                                          StateAction stateAction, SimpleObjectProperty<GameState> gameStateP) {

        setProperty(list-> {
            List<String> newStrList = new ArrayList<>(list);
            newStrList.add(stateAction.action());
           return newStrList;
        }, actionsListP);
        gameStateP.setValue(stateAction.state());
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parameters parameters = getParameters();
        List<String> unNamed = parameters.getUnnamed();
        Map<String, String> named = parameters.getNamed();

        Map<PlayerColor, String> playerColors = new TreeMap<>();
        for(int i = 0; i < unNamed.size(); i++) {
            playerColors.put(PlayerColor.values()[i], unNamed.get(i));
        }
        List<PlayerColor> playerColorsList = new ArrayList<>(playerColors.keySet());

        long seed = named.containsKey("seed") ? Long.parseUnsignedLong(named.get("seed")) : new Random().nextLong();
        RandomGenerator generator = RandomGeneratorFactory.getDefault().create(seed);
        List<Tile> tilesToShuffle = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tilesToShuffle, generator);

        Map<Tile.Kind, List<Tile>> tilesByKind = tilesToShuffle.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.MENHIR),
                        tilesByKind.get(Tile.Kind.MENHIR));

        TextMakerFr textMaker = new TextMakerFr(playerColors);

        GameState gameState =
                GameState.initial(playerColorsList,
                        tileDecks,
                        textMaker);


        SimpleObjectProperty<GameState> gameStateP = new SimpleObjectProperty<>(gameState);
        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);

        Node playersNode = PlayersUI.create(gameStateP, textMaker);

        ObservableValue<Set<Occupant>> allOccupantsO = gameStateP.map(g-> {
            Set<Occupant> visibleOccupants = new HashSet<>();
            if (g.board().lastPlacedTile() != null && g.nextAction() == GameState.Action.OCCUPY_TILE) {
                visibleOccupants.addAll(Stream.concat(g.board().occupants().stream(),
                        g.lastTilePotentialOccupants().stream()).toList());
            } else {
                visibleOccupants.addAll(g.board().occupants());
            }
            return visibleOccupants;
        });

        ObservableValue<Tile> nextTileO = gameStateP.map(GameState::tileToPlace);
        ObservableValue<Integer> remainingNormalO = gameStateP.map(g-> g.tileDecks().normalTiles().size());
        ObservableValue<Integer> remainingMenhirO = gameStateP.map(g-> g.tileDecks().menhirTiles().size());

        ObservableValue<String> TileTextO = gameStateP.map(g -> {
            if (g.nextAction() == GameState.Action.OCCUPY_TILE) {
                return textMaker.clickToOccupy();
            } else if (g.nextAction() == GameState.Action.RETAKE_PAWN) {
                return textMaker.clickToUnoccupy();
            } else {
                return "";
            }
        });

        ObservableValue<List<MessageBoard.Message>> messagesListO = gameStateP
                .map(GameState::messageBoard)
                .map(MessageBoard::messages);

        SimpleObjectProperty<Set<Integer>> highlightedTilesP = new SimpleObjectProperty<>(Set.of());
        Node messageBoardNode = MessageBoardUI.create(messagesListO, highlightedTilesP);

        Consumer <Rotation> onRotateTile = rotation -> setProperty(rotation::add,tileToPlaceRotationP);

        List<String> actionsList = new ArrayList<>();
        SimpleObjectProperty<List<String>> actionsListP = new SimpleObjectProperty<>(actionsList);

        Consumer <Pos> onPlaceTile = tile -> {
            GameState currentGameState = gameStateP.getValue();
            PlacedTile placedTile = new PlacedTile(currentGameState.tileToPlace(),
                    currentGameState.currentPlayer(),
                    tileToPlaceRotationP.getValue(), tile);
            StateAction stateAction = ActionEncoder.withPlacedTile(currentGameState, placedTile);
            updateActionsAndSetState(actionsListP, stateAction, gameStateP);
            tileToPlaceRotationP.set(Rotation.NONE);
        };

        Consumer <Occupant> onSelectOccupant = occupant -> {
            int id = occupant == null ? -1 : Zone.tileId(occupant.zoneId());
            switch (gameStateP.getValue().nextAction()) {
                case OCCUPY_TILE -> {
                    if (occupant == null || gameStateP.getValue().board().lastPlacedTile().id() == id) {
                        StateAction stateAction = ActionEncoder.withNewOccupant(gameStateP.getValue(), occupant);
                        updateActionsAndSetState(actionsListP, stateAction, gameStateP);
                    }
                }
                case RETAKE_PAWN -> {
                    if (occupant == null || occupant.kind() == Occupant.Kind.PAWN
                            && gameStateP.getValue().board().tileWithId(id).placer() == gameStateP.get().currentPlayer()) {
                        StateAction stateAction = ActionEncoder.withOccupantRemoved(gameStateP.getValue(), occupant);
                        updateActionsAndSetState(actionsListP, stateAction, gameStateP);
                    }
                }
            }
        };

        Node boardNode = BoardUI
                .create(Board.REACH,
                        gameStateP,
                        tileToPlaceRotationP,
                        allOccupantsO,
                        highlightedTilesP,
                        onRotateTile,
                        onPlaceTile,
                        onSelectOccupant);

        ScrollPane messageBoardPane = new ScrollPane(messageBoardNode);
        messageBoardPane.setFitToHeight(true);
        messageBoardPane.setFitToWidth(true);

        Consumer<String> onAction = textEntry -> {
            ActionEncoder.StateAction action = ActionEncoder.decodeAndApply(gameStateP.getValue(), textEntry);
            if(action != null) {
                updateActionsAndSetState(actionsListP, action, gameStateP);
                tileToPlaceRotationP.set(Rotation.NONE);
            }
        };

        Node deckNode = DecksUI.create(nextTileO, remainingNormalO, remainingMenhirO, TileTextO, onSelectOccupant);
        Node actions = ActionUI.create(actionsListP, onAction);

        VBox deckActionVbox = new VBox();
        deckActionVbox.getChildren().addAll(actions, deckNode) ;
        BorderPane paneRight = new BorderPane();

        paneRight.setBottom(deckActionVbox);
        paneRight.setTop(playersNode);
        paneRight.setCenter(messageBoardPane);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(boardNode);
        mainPane.setRight(paneRight);

        setProperty(GameState::withStartingTilePlaced,gameStateP);

        primaryStage.setScene(new Scene(mainPane, 1440, 1000));
        primaryStage.setTitle("ChaCuN");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
