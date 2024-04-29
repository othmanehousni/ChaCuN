package ch.epfl.chacun;


import ch.epfl.chacun.gui.DecksUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class DecksUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
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

        var gameStateO = new SimpleObjectProperty<>(gameState);






        var TilesList = Tiles.TILES;
        var tileToPlace = new SimpleObjectProperty<>(TilesList.get(0));
        var remaining = new SimpleObjectProperty<Integer>(5);
        var str = new SimpleObjectProperty<String>("");


        var playersNode = DecksUI.create(tileToPlace, remaining, remaining, str,null);
        var rootNode = new BorderPane(playersNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
        var message = new MessageBoard.Message("test", 1, Set.of(PlayerColor.RED), Set.of(2));
        var gamestateTest = new GameState(gameStateO.get().players(), gameStateO.get().tileDecks(), gameStateO.get().tileToPlace(),gameStateO.get().board(), GameState.Action.START_GAME,new MessageBoard(textMaker, List.of(message)));
        int te = 99;

        var test =new Stage();

        var testbouton = new Button("si ca update pas je suis une _____");
        testbouton.setOnAction(e -> {
            remaining.set(te);
            gameStateO.set(gamestateTest);
            System.out.println("click");
        });

        test.setScene(new Scene(testbouton));
        test.show();
        ;
    }
}