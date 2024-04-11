package ch.epfl.chacun;

import java.util.List;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * Represents and enumerates the possible rotations that can be applied to an object in a two-dimensional space.
 */
public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    /**
     * A list of all possible rotations.
     */
    public static final List<Rotation> ALL = List.of(Rotation.values());

    /**
     * The count of all possible directions. (equivalent to the max ordinal/index of the Enum)
     */
    public static final int COUNT = ALL.size();


    /**
     * Adds another rotation to this rotation and returns the resulting rotation.
     *
     * @param that The other rotation to add.
     * @return The resulting rotation after addition.
     */
    public Rotation add(Rotation that) {
        return ALL.get((ordinal() + that.ordinal()) % COUNT);

    }

    /**
     * Returns the negated (opposite) rotation of this rotation.
     *
     * @return The negated rotation.
     */
    public Rotation negated() {
        return ALL.get((COUNT - ordinal()) % COUNT);
    }


    /**
     * Returns the number of quarter turns clockwise that this rotation represents.
     *
     * @return The number of quarter turns clockwise.
     */
    public int quarterTurnsCW() {
        return ordinal();
    }

    /**
     * Returns the number of degrees clockwise that this rotation represents.
     *
     * @return The number of degrees clockwise.
     */
    public int degreesCW() {
        return ordinal() * 90;
    }
}
