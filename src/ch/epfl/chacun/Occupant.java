package ch.epfl.chacun;

import java.util.Objects;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 * <p>
 * Represents an occupant within a specific zone, characterized by its kind and the zone's ID.
 */
public record Occupant(Kind kind, int zoneId) {

    /**
     * Initializes a new Occupant instance.
     * Ensures that the kind is not null and the zone ID is non-negative.
     *
     * @param kind   The kind of the occupant.
     * @param zoneId The ID of the zone where the occupant is located.
     * @throws NullPointerException     if the kind is null.
     * @throws IllegalArgumentException if the zoneId is negative.
     */
    public Occupant {
        Objects.requireNonNull(kind);
        if (zoneId < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Determines the count of occupants based on their kind.
     *
     * @param kind The kind of the occupant.
     * @return The fixed number of occupants of the specified kind.
     */
    public static int occupantsCount(Kind kind) {
        return kind.ordinal() == 0 ? 5 : 3;
    }

    /**
     * Enumerates the different kinds of occupants.
     */
    public enum Kind {
        PAWN,
        HUT
    }


}
