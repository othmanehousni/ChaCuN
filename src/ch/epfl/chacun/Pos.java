package ch.epfl.chacun;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * Represents a position in a two-dimensional space with x and y coordinates.
 */
public record Pos(int x, int y) {

    /**
     * A constant holding the origin position (0, 0).
     */
    public static final Pos ORIGIN = new Pos(0, 0);


    /**
     * Translates this position by the given deltas in the x and y directions.
     *
     * @param dX The delta to add to the x-coordinate.
     * @param dY The delta to add to the y-coordinate.
     * @return A new {@code Pos} instance representing the translated position.
     */
    public Pos translated(int dX, int dY) {
        return new Pos(x + dX, y + dY);
    }


    /**
     * Computes the neighboring position in the given direction.
     *
     * @param direction The direction to find the neighbor in.
     * @return A new {@code Pos} instance representing the neighboring position.
     */
    public Pos neighbor(Direction direction) {
        return switch (direction) {
            case N -> translated(0, -1);
            case E -> translated(1, 0);
            case S -> translated(0, 1);
            case W -> translated(-1, 0);

        };


    }
}
