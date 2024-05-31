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

/**
 *
 * The PlayersUI class is a non-instantiable class that contains the code
 * for creating the part of the graphical user interface that displays information about the players,
 * such as their names, points, and remaining occupants.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public final class PlayersUI {


    private static final int CIRCLE_RADIUS = 5;

    /**
     * Private constructor to prevent instantiation.
     */
    private PlayersUI() {}

    /**
     * Creates a TextFlow node that displays information about a player.
     *
     * @param gameStateObservable the observable version of the current game state
     * @param textMaker the text generator
     * @param playerColor the color of the player
     * @return a TextFlow node containing the player's information
     */
    private static TextFlow createPlayerInfo(ObservableValue<GameState> gameStateObservable,
                                             TextMaker textMaker, PlayerColor playerColor) {

        TextFlow playerInfo = new TextFlow();
        playerInfo.getStyleClass().add("player");
        // small cercle before the name
        Circle playerCircle = new Circle(CIRCLE_RADIUS, ColorMap.fillColor(playerColor));
        // the spaces used to keep the icons nicely spaced
        Text spaceText3 = new Text("   ");
        Text spaceText1 = new Text(" ");
        playerInfo.getChildren().addAll(playerCircle, spaceText1);

        // creating the observable values for the player's points and current player
        ObservableValue<Map<PlayerColor, Integer>> pointsMapO =
                gameStateObservable.map(gameState -> gameState.messageBoard().points());
        ObservableValue<PlayerColor> currentPlayerO = gameStateObservable.map(GameState::currentPlayer);

        Text playerText = new Text();
        // the text property of the playerText is bound to the pointsMap, showing the player and his points
        playerText.textProperty().bind(pointsMapO.map(pointsPlayer ->
                STR."\{textMaker.playerName(playerColor)} : \{
                        textMaker.points(pointsPlayer.getOrDefault(playerColor, 0))}\n"));

        playerInfo.getChildren().addAll(playerText);

        List<Occupant.Kind> kindList = List.of(Occupant.Kind.values());

        // looping through the kinds of occupants in order to create the icons for each kind
        // the list is reversed due to the order of the icons in the game that is reversed in the enum
        for (Occupant.Kind kind : kindList.reversed()) {
            for (int i = 0; i < Occupant.occupantsCount(kind); i++) {
                int currentI = i;
                // creating the observable value for the free occupants count to show the remaining occupants
                ObservableValue<Integer> freeOccupantCount = gameStateObservable.map(
                        gameState -> gameState.freeOccupantsCount(playerColor, kind));
                Node occupantIcon = Icon.newFor(playerColor, kind);
                // binding the opacity of the occupant icon to the freeOccupantCount,
                // making it opaque if there are remaining occupants
                ObservableValue<Boolean> isOccupantOpaque = freeOccupantCount.map(
                        occupantCount -> occupantCount > currentI);
                occupantIcon.opacityProperty().bind(isOccupantOpaque.map(visible -> visible ? 1.0 : 0.1));
                playerInfo.getChildren().add(occupantIcon);

                // adding the space text between the pawns and the huts
                if (kind == Occupant.Kind.HUT && i == 2) {
                    playerInfo.getChildren().add(spaceText3);
                }
            }
        }

        // update the style class of the player information node based on the current player
        updatePlayerStyleClass(currentPlayerO, playerInfo, playerColor);
        return playerInfo;
    }

    /**
     * Updates the style class of the player information node based on the current player.
     *
     * @param currentPlayerO the observable version of the current player
     * @param playerInfo the TextFlow node containing the player's information
     * @param playerColor the color of the player
     */
    private static void updatePlayerStyleClass (ObservableValue<PlayerColor> currentPlayerO,
                                                TextFlow playerInfo, PlayerColor playerColor) {
        currentPlayerO.addListener((_, _, newPlayer) -> {
            if (newPlayer == playerColor) {
                playerInfo.getStyleClass().add("current");
            } else {
                playerInfo.getStyleClass().remove("current");
            }
        });
    }

    /**
     * Creates the root JavaFX node of the scene graph that displays information about the players.
     *
     * @param gameStateObservable the observable version of the current game state
     * @param textMaker the text generator
     * @return the root node of the scene graph
     */
    public static Node create(ObservableValue<GameState> gameStateObservable, TextMaker textMaker) {
        VBox root = new VBox();
        root.getStylesheets().add("players.css");
        root.setId("players");
        List<PlayerColor> playerColors = gameStateObservable.getValue().players();

        //looping through each player of the game in order to create its playerInfo slot
        for (PlayerColor color : playerColors) {
            TextFlow playerInfo = createPlayerInfo(gameStateObservable, textMaker, color);
            root.getChildren().add(playerInfo);
        }
        return root;
    }



}
