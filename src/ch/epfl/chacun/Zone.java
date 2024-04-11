package ch.epfl.chacun;

import java.util.List;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * Represents a zone in the game world, with various implementations for different types of zones.
 */
public sealed interface Zone {

    /**
     * Calculates the tile ID based on the zone's unique identifier.
     *
     * @param zoneId the unique identifier of the zone.
     * @return the tile ID derived from the zone's identifier.
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * Calculates the local ID within a tile based on the zone's unique identifier.
     *
     * @param zoneId the unique identifier of the zone.
     * @return the local ID within a tile.
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    /**
     * Gets the unique identifier for the zone.
     *
     * @return the unique identifier of the zone.
     */
     int id();

    /**
     * Default method to calculate the tile ID of this zone.
     *
     * @return the tile ID of this zone.
     */
    default int tileId() {
        return tileId(id());
    }

    /**
     * Default method to calculate the local ID of this zone.
     *
     * @return the local ID of this zone.
     */
    default int localId() {
        return localId(id());
    }

    /**
     * Returns the special power associated with the zone, if any.
     *
     * @return the special power of the zone, or null if none.
     */
    default SpecialPower specialPower() {
        return null;
    }


    /**
     * Enumerates the special powers that can be associated with zones.
     */
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    /**
     * Represents a water zone, either a lake or a river, containing fish.
     */
    sealed interface Water extends Zone {

         int fishCount();
    }

    /**
     * Represents a forest zone with a specific kind and ID.
     */
    record Forest(int id, Kind kind) implements Zone {

        /**
         * Enumerates the kinds of forests.
         */
        public enum Kind {
            PLAIN,

            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    /**
     * Represents a meadow zone containing animals and possibly a special power.
     */

    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    /**
     * Represents a lake, a type of water zone, with a specific fish count and possibly a special power.
     */
    record Lake(int id, int fishCount, SpecialPower specialPower) implements Water {

    }

    /**
     * Represents a river, a type of water zone, potentially connected to a lake, with a specific fish count.
     */
    record River(int id, int fishCount, Lake lake) implements Water {

        /**
         * Determines if the river is connected to a lake.
         *
         * @return true if the river has a lake, false otherwise.
         */
        public boolean hasLake() {
            return lake != null;
        }
    }
}
