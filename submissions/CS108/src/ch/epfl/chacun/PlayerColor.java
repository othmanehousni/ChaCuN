package ch.epfl.chacun;

import java.util.List;

/**
 * Enumerates the possible colors that a player can choose in the game.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */

public enum PlayerColor {

    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE;

    /**
     * A list containing all possible player colors.
     */
    public static final List<PlayerColor> ALL = List.of(PlayerColor.values());
}

