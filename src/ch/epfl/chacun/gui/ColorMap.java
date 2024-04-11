package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;

public abstract class ColorMap {

    public static javafx.scene.paint.Color fillColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> javafx.scene.paint.Color.RED;
            case BLUE -> javafx.scene.paint.Color.BLUE;
            case GREEN -> javafx.scene.paint.Color.LIME;
            case YELLOW -> javafx.scene.paint.Color.YELLOW;
            case PURPLE -> javafx.scene.paint.Color.PURPLE;
        };
    }

    public static javafx.scene.paint.Color strokeColor(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED, BLUE, PURPLE ->  javafx.scene.paint.Color.WHITE;
            case GREEN, YELLOW -> fillColor(playerColor).deriveColor(0, 1, 0.6, 1);
        };
    }
}
