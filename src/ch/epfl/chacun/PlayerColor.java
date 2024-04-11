package ch.epfl.chacun;

import java.util.List;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 * Enumerates the possible colors that a player can choose in the game.
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

