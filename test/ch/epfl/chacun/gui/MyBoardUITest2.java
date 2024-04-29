package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.epfl.chacun.Tiles.TILES;

public final class MyBoardUITest2 extends Application {
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));
        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var textMaker = new TextMakerFr(playerNames);

        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        gameState = gameState.withStartingTilePlaced();

        gameState = gameState.withPlacedTile(new PlacedTile(TILES.get(57),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0),
                null));
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN,571));

        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.of(new Occupant(Occupant.Kind.PAWN,571)));
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameStateO = new SimpleObjectProperty<>(gameState);
        gameStateO.set(gameState);
        var boardNode = boardclaquejsp
                .create(1,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> System.out.println("Rotate: " + r),
                        t -> System.out.println("Place: " + t),
                        o -> System.out.println("Select: " + o));




        var gamestate2 = new GameState(gameState.players(), tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), TILES.get(56), Board.EMPTY, GameState.Action.PLACE_TILE, new MessageBoard(textMaker, List.of()));

        var gamestate3 = gamestate2.withPlacedTile(new PlacedTile(TILES.get(56),
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0),
                null));

        Button button = new Button("bouton");
        button.setOnMouseClicked(e -> {
            System.out.println(gameStateO.get());
            gameStateO.set(gamestate2);
            System.out.println(gameStateO.get());

        });


        var rootNode = new BorderPane();
        var tileToPlace = new SimpleObjectProperty<>(Tiles.TILES.get(0));
        var remaining = new SimpleObjectProperty<Integer>(5);
        var str = new SimpleObjectProperty<String>("");
        var playersNode = DecksUI.create(tileToPlace, remaining, remaining, str,null);
        var messages = List.of(new MessageBoard.Message("test", 1, Set.of(PlayerColor.RED), Set.of(2)));
        var messagesList = new SimpleObjectProperty<List<MessageBoard.Message>>(messages);
        var setTiles = new SimpleObjectProperty<Set<Integer>>(Set.of(1,2,3));
        var messageBoardNode = MessageBoardUI.create(messagesList, setTiles);
        rootNode.setRight(playersNode);
        rootNode.setLeft(boardNode);
        rootNode.setBottom(messageBoardNode);
        //rootNode.getChildren().addAll(playersNode,boardNode);

        primaryStage.setScene(new Scene(rootNode, 1000, 1000));


        Stage t = new Stage();
        t.setScene(new Scene(button));
        t.show();
        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();


    }
}