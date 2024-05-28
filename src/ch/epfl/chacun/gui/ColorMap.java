package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;

/**
 * The ColorMap class is a public, non-instantiable class that contains methods
 * for determining the JavaFX colors to use for representing the five player colors in ChaCuN on screen.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
 public final class ColorMap {

    /**
     * Private constructor to prevent instantiation.
     */
    private ColorMap() {
    }

    /**
     * Returns the JavaFX fill color corresponding to the given player color.
     *
     * @param playerColor the color of the player
     * @return the JavaFX color to use for filling the player's occupants
     */
    public static javafx.scene.paint.Color fillColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> javafx.scene.paint.Color.RED;
            case BLUE -> javafx.scene.paint.Color.BLUE;
            case GREEN -> javafx.scene.paint.Color.LIME;
            case YELLOW -> javafx.scene.paint.Color.YELLOW;
            case PURPLE -> javafx.scene.paint.Color.PURPLE;
        };
    }

    /**
     * Returns the JavaFX stroke color corresponding to the given player color.
     *
     * @param playerColor the color of the player
     * @return the JavaFX color to use for drawing the contour of the player's occupants
     */
    public static javafx.scene.paint.Color strokeColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED, BLUE, PURPLE ->  javafx.scene.paint.Color.WHITE;
            case GREEN, YELLOW -> fillColor(playerColor).deriveColor(0, 1, 0.6, 1);
        };
    }
}
