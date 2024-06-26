package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the cardinal directions in which an entity can move or face.
 * Enumerates all possible directions.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)

 */
public enum Direction {
    N,
    E,
    S,
    W;

    /**
     * A list of all possible directions using the specific values of the Enum.
     */
    public static final List<Direction> ALL = List.of(Direction.values());


    /**
     * The count of all possible directions. (equivalent to the size of the List of all directions)
     */

    public static final int COUNT = ALL.size();

    /**
     * Rotates this direction according to a given rotation.
     * @param rotation The rotation to apply to this direction.
     * @return The new direction after rotation is applied.
     */

    public Direction rotated(Rotation rotation) {
        return ALL.get((ordinal() + rotation.ordinal()) % COUNT);
    }

    /**
     * Gets the opposite direction of this direction.
     * @return The opposite direction.
     */

    public Direction opposite() {
        return rotated(Rotation.HALF_TURN);
    }
}
