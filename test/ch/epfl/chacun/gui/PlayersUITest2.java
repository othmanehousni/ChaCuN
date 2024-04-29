package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Map;
import java.util.stream.Collectors;

public final class PlayersUITest2 extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard", PlayerColor.YELLOW, "Alice",
                PlayerColor.PURPLE, "Bob");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream().collect(Collectors.groupingBy(Tile::kind));
        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var textMaker = new TextMakerFr(playerNames);

        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        var gameStateO = new SimpleObjectProperty<>(gameState);



        Tile tile = Tiles.TILES.get(14);

        var pos = new Pos(1, 0);

        gameStateO.set(gameStateO.get().withStartingTilePlaced());
        var placedTile = new PlacedTile(tile,gameStateO.get().currentPlayer(), Rotation.NONE,pos);

        gameStateO.set(gameStateO.get().withPlacedTile(placedTile));

        gameStateO.set(gameStateO.get().withNewOccupant(new Occupant(Occupant.Kind.PAWN, 140)));


        var playersNode = PlayersUI.create(gameStateO, textMaker);
        var rootNode = new BorderPane(playersNode);
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.setTitle("ChaCuN test 2");
        primaryStage.show();
    }
}