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

public final class PlayersUI {

    private static final int CIRCLE_RADIUS = 5;

    private PlayersUI() {}

    private static TextFlow createPlayerInfo(ObservableValue<GameState> gameStateObservable, TextMaker textMaker, PlayerColor playerColor) {

        TextFlow playerInfo = new TextFlow();
        playerInfo.getStyleClass().add("player");
        Circle playerCircle = new Circle(CIRCLE_RADIUS, ColorMap.fillColor(playerColor));
        Text spaceText3 = new Text("   ");
        Text spaceText1 = new Text(" ");
        playerInfo.getChildren().add(playerCircle);
        ObservableValue<Map<PlayerColor, Integer>> pointsMapO =
                gameStateObservable.map(gameState -> gameState.messageBoard().points());
        ObservableValue<PlayerColor> currentPlayerO = gameStateObservable.map(GameState::currentPlayer);

        Text playerText = new Text();
        playerText.textProperty().bind(pointsMapO.map(pointsPlayer ->
                STR."\{textMaker.playerName(playerColor)} : \{
                        textMaker.points(pointsPlayer.getOrDefault(playerColor, 0))}\n"));

        playerInfo.getChildren().addAll(playerText, spaceText1);

        List<Occupant.Kind> kindList = List.of(Occupant.Kind.values());
        for (Occupant.Kind kind : kindList.reversed()) {
            for (int i = 0; i < Occupant.occupantsCount(kind); i++) {
                int finalI = i;
                ObservableValue<Integer> freeOccupantCount = gameStateObservable.map(
                        gameState -> gameState.freeOccupantsCount(playerColor, kind));
                Node occupantIcon = Icon.newFor(playerColor, kind);
                ObservableValue<Boolean> isOccupantOpaque = freeOccupantCount.map(occupantCount -> occupantCount > finalI);
                occupantIcon.opacityProperty().bind(isOccupantOpaque.map(visible -> visible ? 1.0 : 0.1));
                playerInfo.getChildren().add(occupantIcon);

                if (kind == Occupant.Kind.HUT && i == 2) {
                    playerInfo.getChildren().add(spaceText3);
                }
            }
        }

        updatePlayerStyleClass(currentPlayerO, playerInfo, playerColor);
        return playerInfo;
    }

    private static void updatePlayerStyleClass (ObservableValue<PlayerColor> currentPlayerO, TextFlow playerInfo, PlayerColor playerColor) {
        currentPlayerO.addListener((_, _, newPlayer) -> {
            if (newPlayer == playerColor) {
                playerInfo.getStyleClass().add("current");
            } else {
                playerInfo.getStyleClass().remove("current");
            }
        });
    }

    public static Node create(ObservableValue<GameState> gameStateObservable, TextMaker textMaker) {
        VBox root = new VBox();
        root.getStylesheets().add("players.css");
        root.setId("players");
        List<PlayerColor> playerColors = gameStateObservable.getValue().players();
        for (PlayerColor color : playerColors) {
            TextFlow playerInfo = createPlayerInfo(gameStateObservable, textMaker, color);
            root.getChildren().add(playerInfo);
        }
        return root;
    }



}
