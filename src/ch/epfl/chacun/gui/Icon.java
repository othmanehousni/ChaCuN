package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Occupant;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

public abstract  class Icon {


    public static Node newFor(PlayerColor playerColor, Occupant occupant) {
        SVGPath svgPath = new SVGPath();
        switch (occupant.kind()) {
            case PAWN ->
                svgPath.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6 -6\n" +
                        "L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
            case HUT ->
                svgPath.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");

        }
        svgPath.setFill(ColorMap.fillColor(playerColor));
        svgPath.setStroke(ColorMap.strokeColor(playerColor));
        return svgPath;
    }
}
