package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class PlayersUI {

    private PlayersUI() {}

    private static TextFlow createPlayerInfo(ObservableValue<GameState> gameStateObservable, TextMaker textMaker, PlayerColor playerColor) {

        TextFlow playerInfo = new TextFlow();
        playerInfo.getStyleClass().add("player");

        Circle playerCircle = new Circle(5);
        playerCircle.setFill(ColorMap.fillColor(playerColor));

        ObservableValue<Map<PlayerColor, Integer>> map = gameStateObservable.map(gameState -> gameState.messageBoard().points());
        Text playerText = new Text();
        playerText.textProperty().bind(map.map(pointsPlayer -> STR."\{textMaker.playerName(playerColor)} : \{textMaker.points(pointsPlayer.getOrDefault(playerColor, 0))}\n"));

        Text spaceText3 = new Text("   ");

        TreeSet<Occupant.Kind> kindTreeSet = new TreeSet<>(List.of(Occupant.Kind.values()));
        playerInfo.getChildren().addAll(spaceText3,playerCircle);
        playerInfo.getChildren().addAll(playerText);

        for (Occupant.Kind kind : kindTreeSet.reversed()) {
            for (int i = 0; i < Occupant.occupantsCount(kind); i++) {
                int finalI = i;
                ObservableValue<Integer> freeOccupantCount = gameStateObservable.map(gameState -> gameState.freeOccupantsCount(playerColor,kind));
                Node icon = Icon.newFor(playerColor, kind);
                icon.opacityProperty().bind(freeOccupantCount.map(integer -> integer > finalI ? 1.0 : 0.1));
                playerInfo.getChildren().add(icon);

            }
        }

        gameStateObservable.map(GameState::currentPlayer).addListener((_, oldPlayer, newPlayer) -> {
            if (newPlayer == playerColor) {
                playerInfo.getStyleClass().add("current");
            } else {
                playerInfo.getStyleClass().remove("current");
            }
        });

        return playerInfo;
    }


    public static Node create(ObservableValue<GameState> gameStateObservable, TextMaker textMaker) {
        VBox root = new VBox();
        root.getStylesheets().add("players.css");
        root.setId("players");
        List<PlayerColor> playerColors = gameStateObservable.getValue().players();
        for (PlayerColor color : playerColors) {
            String playerName = textMaker.playerName(color);
            TextFlow playerInfo = createPlayerInfo(gameStateObservable, textMaker, color);
            root.getChildren().add(playerInfo);
            root.setId(STR."\{color.name()}");
        }
        return root;
    }



}
