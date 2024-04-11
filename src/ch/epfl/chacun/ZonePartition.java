package ch.epfl.chacun;

import java.util.*;

/**
 * Represents a partition of areas of a given zone type in the game.
 * This record is generic, parameterized by type {@code Z} which is bound by {@link Zone}.
 * It consists of a set of areas forming a partition.
 *
 * @param <Z> The type of zones constituting the areas, extending {@link Zone}.
 * @param areas The set of areas forming the partition.
 *
 * @author Othmane HOUSNI
 * @author Hamza ZOUBAYRI
 */

public record ZonePartition <Z extends Zone> (Set <Area<Z>> areas) {

    /**
     * Initializes a {@code ZonePartition} with a set of areas, ensuring immutability.
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    /**
     * Secondary constructor that initializes the partition with an empty set of areas.
     */
    public ZonePartition() {
        this(new HashSet<>());
    }

    /**
     * Finds and returns the area containing the specified zone.
     *
     * @param zone The zone to find within the areas of the partition.
     * @return The area containing the specified zone.
     * @throws IllegalArgumentException if the zone does not belong to any area in the partition.
     */

    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }


    /**
     * A builder class for {@code ZonePartition}, allowing for incremental construction of a zone partition.
     *
     * @param <Z> The type of zones, bound by {@link Zone}.
     */
    public final static class Builder<Z extends Zone> {

        private final Set<Area<Z>> areas;

        /**
         * Constructs a {@code Builder} initialized with areas from an existing {@code ZonePartition}.
         *
         * @param zonePartition The existing zone partition to initialize the builder with.
         */
        public Builder(ZonePartition<Z> zonePartition) {
            this.areas = new HashSet<>(zonePartition.areas());
        }

        private Area<Z> areaContaining(Z zone, Set<Area<Z>> areas) {
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    return area;
                }
            }
            throw new IllegalArgumentException();
        }


        /**
         * Adds a new, unoccupied area consisting solely of the specified zone and given number of open connections.
         *
         * @param zone The zone to create the new area with.
         * @param openConnections The number of open connections for the new area.
         */

        public void addSingleton(Z zone, int openConnections) {
            Area<Z> newArea = new Area<>(Set.of(zone), List.of(), openConnections);
            areas.add(newArea);
        }

        /**
         * Adds an initial occupant of the specified color to the area containing the given zone.
         *
         * @param zone The zone whose area to add an occupant to.
         * @param color The color of the initial occupant to add.
         * @throws IllegalArgumentException if the zone does not belong to any area in the partition, or if the area is already occupied.
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> newArea = areaContaining(zone,areas).withInitialOccupant(color);
            areas.remove(areaContaining(zone,areas));
            areas.add(newArea);
        }

        /**
         * Removes an occupant of the specified color from the area containing the given zone.
         *
         * @param zone The zone whose area to remove an occupant from.
         * @param color The color of the occupant to remove.
         * @throws IllegalArgumentException if the zone does not belong to any area in the partition, or if the area does not contain an occupant of the specified color.
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> newArea = areaContaining(zone,areas).withoutOccupant(color);
            areas.remove(areaContaining(zone,areas));
            areas.add(newArea);
        }

        /**
         * Removes all occupants from the specified area.
         *
         * @param area The area to remove all occupants from.
         * @throws IllegalArgumentException if the area is not part of the partition.
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.contains(area));
            Area <Z> newArea = area.withoutOccupants();
            areas.remove(area);
            areas.add(newArea);
        }

        /**
         * Connects the areas containing the two specified zones, creating a larger area.
         *
         * @param zone1 The first zone whose area to connect.
         * @param zone2 The second zone whose area to connect.
         * @throws IllegalArgumentException if either of the zones does not belong to any area in the partition.
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> Area1 = areaContaining(zone1, areas);
            Area<Z> Area2 = areaContaining(zone2, areas);
            areas.add(Area1.connectTo(Area2));
            if(!Area1.equals(Area2)){
                areas.remove(Area1);
                areas.remove(Area2);
            } else {
                areas.remove(Area1);
            }

        }
        /**
         * Builds and returns the {@code ZonePartition} from the current state of the builder.
         *
         * @return The constructed {@code ZonePartition}.
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}