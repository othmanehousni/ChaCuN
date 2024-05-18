package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
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
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends Application {
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

        long seed = new Random().nextLong();
        if (named.containsKey("seed")) {
            seed = Long.parseUnsignedLong(named.get("seed"));
        }

        RandomGenerator generator = RandomGeneratorFactory.getDefault().create(seed);
        List<Tile> tilesToShuffle = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tilesToShuffle, generator);

        Map<Tile.Kind, List<Tile>> tilesByKind = tilesToShuffle.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        TextMakerFr textMaker = new TextMakerFr(playerColors);

        GameState gameState =
                GameState.initial(playerColorsList,
                        tileDecks,
                        textMaker);



        List<String> actionsList = new ArrayList<>();
        SimpleObjectProperty<List<String>> actionsListP = new SimpleObjectProperty<>(actionsList);
        SimpleObjectProperty<GameState> gameStateO = new SimpleObjectProperty<>(gameState);
        SimpleObjectProperty<Rotation> tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        SimpleObjectProperty<Set<Integer>> highlightedTilesP = new SimpleObjectProperty<>();


        Node playersNode = PlayersUI.create(gameStateO, textMaker);

        ObservableValue<Set<Occupant>> allOccupantsO = gameStateO.map(g-> {
            Set<Occupant> visibleOccupants = new HashSet<>();
            if (g.board().lastPlacedTile() != null && g.nextAction() == GameState.Action.OCCUPY_TILE) {
                visibleOccupants.addAll(Stream.concat(g.board().occupants().stream(),
                        g.lastTilePotentialOccupants().stream()).toList());
            } else {
                visibleOccupants.addAll(g.board().occupants());
            }
            return visibleOccupants;
        });


        ObservableValue<Tile> nextTileO = gameStateO.map(GameState::tileToPlace);
        ObservableValue<Integer> remainingNormalO = gameStateO.map(g-> g.tileDecks().normalTiles().size());
        ObservableValue<Integer> remainingMenhirO = gameStateO.map(g-> g.tileDecks().menhirTiles().size());
        ObservableValue<String> TileTextO = gameStateO.map(g -> {
            if (g.nextAction() == GameState.Action.OCCUPY_TILE) {
                return textMaker.clickToOccupy();
            } else if (g.nextAction() == GameState.Action.RETAKE_PAWN) {
                return textMaker.clickToUnoccupy();
            } else {
                return "";
            }
        });

        ObservableValue<List<MessageBoard.Message>> messagesListO = gameStateO
                .map(GameState::messageBoard)
                .map(MessageBoard::messages);
        List<MessageBoard.Message> messageList = gameStateO.getValue().messageBoard().messages();
        Set<Integer> tileSet = new HashSet<>();
        messageList.forEach(message -> tileSet.addAll(message.tileIds()));
        highlightedTilesP.set(tileSet);

        Node messageBoardNode = MessageBoardUI.create(messagesListO, highlightedTilesP);


        Consumer <Rotation> onRotateTile = r -> tileToPlaceRotationP.set(tileToPlaceRotationP.getValue().add(r));
        Consumer <Pos> onPlaceTile = t -> {
            PlacedTile placedTile = new PlacedTile(gameStateO.get().tileToPlace(), gameStateO.get().currentPlayer(),
                    tileToPlaceRotationP.getValue(), t);
            ActionEncoder.StateAction stateAction = ActionEncoder.withPlacedTile(gameStateO.getValue(), placedTile);
            List<String> newStrList = new ArrayList<>(actionsList);
            newStrList.add(stateAction.action());
            actionsList.add(stateAction.action());
            actionsListP.set(newStrList);
            gameStateO.setValue(stateAction.state());
        };

        Consumer <Occupant> onSelectOccupant = o -> {
            int id = o == null ? -1 : Zone.tileId(o.zoneId());
            switch (gameStateO.getValue().nextAction()) {
                case OCCUPY_TILE -> {
                    if (o == null || gameStateO.getValue().board().lastPlacedTile().id() == id) {
                        ActionEncoder.StateAction stateAction = ActionEncoder.withNewOccupant(gameStateO.getValue(), o);
                        List<String> newStrList = new ArrayList<>(actionsList);
                        newStrList.add(stateAction.action());
                        actionsList.add(stateAction.action());
                        actionsListP.set(newStrList);
                        gameStateO.setValue(stateAction.state());
                    }
                }
                case RETAKE_PAWN -> {
                    if (o == null || o.kind() == Occupant.Kind.PAWN
                            && gameStateO.getValue().board().tileWithId(id).placer() == gameStateO.get().currentPlayer()) {
                        ActionEncoder.StateAction stateAction = ActionEncoder.withOccupantRemoved(gameStateO.getValue(), o);
                        List<String> newStrList = new ArrayList<>(actionsList);
                        newStrList.add(stateAction.action());
                        actionsList.add(stateAction.action());
                        actionsListP.set(newStrList);
                        gameStateO.setValue(stateAction.state());
                    }
                }

            }

        };

        Node boardNode = BoardUI
                .create(6,
                        gameStateO,
                        tileToPlaceRotationP,
                        allOccupantsO,
                        highlightedTilesP,
                        onRotateTile,
                        onPlaceTile,
                        onSelectOccupant);


        ScrollPane messageBoardPane = new ScrollPane(messageBoardNode);
        messageBoardPane.setFitToHeight(true);
        messageBoardPane.setFitToWidth(true);

        Consumer<String> onAction = s -> {
            ActionEncoder.StateAction action = ActionEncoder.decodeAndApply(gameStateO.getValue(), s);
            if(action != null) {
                gameStateO.set(action.state());
            }
        };

        Node deckNode = DecksUI.create(nextTileO, remainingNormalO, remainingMenhirO, TileTextO, onSelectOccupant);
        Node actions = ActionsUI.create(actionsListP, onAction);

        VBox deckActionVbox = new VBox();
        deckActionVbox.getChildren().addAll(actions, deckNode) ;
        BorderPane paneRight = new BorderPane();

        paneRight.setBottom(deckActionVbox);
        paneRight.setTop(playersNode);
        paneRight.setCenter(messageBoardPane);

        BorderPane mainPaneLeft = new BorderPane();
        mainPaneLeft.setCenter(boardNode);
        mainPaneLeft.setRight(paneRight);

        gameStateO.set(gameStateO.get().withStartingTilePlaced());

        primaryStage.setScene(new Scene(mainPaneLeft, 1440, 1000));
        primaryStage.setTitle("ChaCuN");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
