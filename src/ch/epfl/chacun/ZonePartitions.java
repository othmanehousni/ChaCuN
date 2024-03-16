package ch.epfl.chacun;

/**
 * Represents the group of four zone partitions in the game, encompassing forests, meadows, rivers, and water systems.
 * This record holds the partitions for different types of zones, allowing for operations across the game's landscape.
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {

    /**
     * An empty group of zone partitions, useful as a default or starting state before any zones are added.
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    /**
     * A builder for creating or modifying a {@code ZonePartitions} instance. This allows for incremental construction
     * or modification of a zone partitions group.
     */

    final public static class Builder {
        private ZonePartition.Builder<Zone.Forest> forestBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private ZonePartition.Builder<Zone.River> riverBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;

        /**
         * Constructs a new builder initialized with the partitions from an existing {@code ZonePartitions} instance.
         *
         * @param initial The initial zone partitions to start with.
         */

        public Builder(ZonePartitions initial) {
            this.forestBuilder = new ZonePartition.Builder<>(initial.forests);
            this.meadowBuilder = new ZonePartition.Builder<>(initial.meadows);
            this.riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        /**
         * Adds a tile's zones to the appropriate partitions based on the zones' types.
         * This includes determining open connections and forming new areas as needed.
         *
         * @param tile The tile to add to the partitions.
         */
        public void addTile(Tile tile) {
            int[] numberOfConnections = new int[10];

            for (TileSide sides : tile.sides()) {
                for (Zone sideZones : sides.zones()) {
                    numberOfConnections[sideZones.localId()]++;
                    if (sideZones instanceof Zone.River river && ((Zone.River) sideZones).hasLake()) {
                        numberOfConnections[river.localId()]++;
                        numberOfConnections[river.lake().localId()]++;
                    }
                }
            }
            for (Zone zone : tile.zones()) {
                int nbrOfConnections = numberOfConnections[zone.localId()];
                switch (zone) {
                    case Zone.Forest forest -> forestBuilder.addSingleton(forest, nbrOfConnections);
                    case Zone.Meadow meadow -> meadowBuilder.addSingleton(meadow, nbrOfConnections);
                    case Zone.River river ->{
                        riverSystemsBuilder.addSingleton((Zone.River) zone, nbrOfConnections);
                        if(river.hasLake()) {
                            riverBuilder.addSingleton(river, nbrOfConnections-1);
                        }else {
                            riverBuilder.addSingleton(river, nbrOfConnections);
                        }
                    }
                    case Zone.Lake lake -> riverSystemsBuilder.addSingleton(lake,nbrOfConnections);
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river && river.hasLake()) {
                    riverSystemsBuilder.union(river, river.lake());
                }
            }

        }

        /**
         * Connects two tile sides, merging their corresponding areas in the relevant partition if necessary.
         * Throws an IllegalArgumentException if the tile sides are not of the same kind.
         *
         * @param s1 The first tile side to connect.
         * @param s2 The second tile side to connect.
         */
        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest forest1)
                        when s2 instanceof TileSide.Forest(Zone.Forest forest2) -> forestBuilder.union(forest1, forest2);
                case TileSide.Meadow(Zone.Meadow meadow1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow meadow2) -> meadowBuilder.union(meadow1, meadow2);
                case TileSide.River(Zone.Meadow meadow1, Zone.River river1, Zone.Meadow meadow1_1)
                        when s2 instanceof TileSide.River(
                        Zone.Meadow meadow2, Zone.River river2, Zone.Meadow meadow2_2
                ) -> {
                    meadowBuilder.union(meadow1, meadow2_2);
                    riverBuilder.union(river1, river2);
                    meadowBuilder.union(meadow1_1, meadow2);
                    riverSystemsBuilder.union(river1,river2);
                }
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Adds an initial occupant of a given kind belonging to a specified player to the area containing a specific zone.
         * Throws an IllegalArgumentException if the occupant kind cannot occupy the zone type.
         *
         * @param player       The player to whom the occupant belongs.
         * @param occupantKind The kind of the occupant.
         * @param occupiedZone The zone to be occupied.
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {

            switch (occupiedZone) {
                case Zone.Forest forest
                        when occupantKind == Occupant.Kind.PAWN -> forestBuilder.addInitialOccupant(forest, player);
                case Zone.Meadow meadow
                        when occupantKind == Occupant.Kind.PAWN -> meadowBuilder.addInitialOccupant(meadow, player);
                case Zone.River river
                        when (occupantKind == Occupant.Kind.PAWN) -> riverBuilder.addInitialOccupant(river, player);
                case Zone.Water water
                        when  Occupant.Kind.HUT == occupantKind -> riverSystemsBuilder.addInitialOccupant(water, player);
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Removes a pawn belonging to a specified player from the area containing a given zone.
         * Throws an IllegalArgumentException if the zone is a lake, as lakes cannot have pawns.
         *
         * @param player       The player whose pawn is to be removed.
         * @param occupiedZone The zone from which to remove the pawn.
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest -> forestBuilder.removeOccupant(forest, player);

                case Zone.Meadow meadow -> meadowBuilder.removeOccupant(meadow, player);
                case Zone.River river -> riverBuilder.removeOccupant(river, player);
                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Clears all gatherers (pawns acting as gatherers) from a specified forest area.
         *
         * @param forest The forest area from which to clear gatherers.
         */
        public void clearGatherers(Area<Zone.Forest> forest){ forestBuilder.removeAllOccupantsOf(forest);}

        /**
         * Clears all fishers (pawns acting as fishers) from a specified river area.
         *
         * @param river The river area from which to clear fishers.
         */
        public void clearFishers(Area<Zone.River> river){riverBuilder.removeAllOccupantsOf(river);}

        /**
         * Builds and returns the current state of the zone partitions being constructed or modified.
         *
         * @return A {@code ZonePartitions} instance representing the constructed zone partitions.
         */
        public ZonePartitions build(){
            return new ZonePartitions(forestBuilder.build(), meadowBuilder.build(), riverBuilder.build(), riverSystemsBuilder.build());
        }
    }
}