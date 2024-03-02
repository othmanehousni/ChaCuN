package ch.epfl.chacun;

import java.util.List;


/**
 * Represents a side of a tile in the game, which can be a forest, meadow, or river.
 * This interface is sealed to restrict its implementation to specific kinds of tile sides.
 *
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public sealed interface TileSide {

    /**
     * Returns the zones touching this side of the tile.
     * (the methods are overwritten later)
     *
     * @return A list of zones adjacent to this tile side.
     */
    public abstract List<Zone> zones();

    /**
     * Determines if another tile side is of the same kind as this one.
     * (the methods are overwritten later)
     *
     * @param that The other tile side to compare with.
     * @return true if the other tile side is of the same kind, false otherwise.
     */
    public abstract boolean isSameKindAs(TileSide that);

    /**
     * Represents a forest tile side with a single forest zone.
     */
    public record Forest(Zone.Forest forest) implements TileSide {
        public List<Zone> zones() {
            return List.of(forest);
        }

        public boolean isSameKindAs(TileSide that) {
            return that instanceof Forest;
        }
    }

    /**
     * Represents a meadow tile side with a single meadow zone.
     */
    public record Meadow(Zone.Meadow meadow) implements TileSide {
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }

    /**
     * Represents a river tile side, surrounded by two meadow zones and containing one river zone.
     * The order of the meadow zones is significant and follows the clockwise traversal of the tile.
     */
    public record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof River;
        }
    }
}
