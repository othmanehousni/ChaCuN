package ch.epfl.chacun;


public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    final public static class Builder {
        private ZonePartition.Builder<Zone.Forest> forestBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private ZonePartition.Builder<Zone.River> riverBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;


        public Builder(ZonePartitions initial) {
            this.forestBuilder = new ZonePartition.Builder<>(initial.forests);
            this.meadowBuilder = new ZonePartition.Builder<>(initial.meadows);
            this.riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        public void addTile(Tile tile) {
            //premiere partie
            int[] numberOfConnections = new int[10];

            for (TileSide sides : tile.sides()) {
                for (Zone sideZones : sides.zones()) {
                    numberOfConnections[sideZones.localId()]++;
                    //Ajouet nom rivierre et enlbver  ((Zone.River) sideZones) remplacer par nom de la riviere
                    if (sideZones instanceof Zone.River && ((Zone.River) sideZones).hasLake()) {
                        //numberOfConnections[sideZones.localId()]++;// askip IL FAT AAUJOUTER, en gros si ona une rivierre on une oppenconnections en pélus emtre la riviere et le lac ;
                        numberOfConnections[((Zone.River) sideZones).lake().localId()]++;
                    }
                }
            }
            for (Zone zone : tile.zones()) {
                switch (zone) {
                    case Zone.Forest forest -> forestBuilder.addSingleton(forest, numberOfConnections[zone.localId()]);
                    case Zone.Meadow meadow -> meadowBuilder.addSingleton(meadow, numberOfConnections[zone.localId()]);
                    case Zone.River river ->{
                        riverBuilder.addSingleton(river, numberOfConnections[zone.localId()]);
                        riverSystemsBuilder.addSingleton((Zone.River) zone, numberOfConnections[zone.localId()] - 1);
                        if(river.hasLake()) {
                            riverSystemsBuilder.addSingleton(river.lake(), numberOfConnections[zone.localId()]);//rajouetr le -1????
                        }}

                    case Zone.Lake lake -> { riverSystemsBuilder.addSingleton(lake, numberOfConnections[zone.localId()]);
                    }
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river && river.hasLake()) {
                    riverSystemsBuilder.union((Zone.River) zone, river.lake());
                }
            }

        }

        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest forest1)
                        when s2 instanceof TileSide.Forest(Zone.Forest forest2) -> forestBuilder.union(forest1, forest2);
                case TileSide.Meadow(Zone.Meadow meadow1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow meadow2) -> meadowBuilder.union(meadow1, meadow2);
                case TileSide.River(Zone.Meadow meadow1, Zone.River river1, Zone.Meadow meadow1_1)
                        when s2 instanceof TileSide.River(
                        Zone.Meadow meadow2, Zone.River river2, Zone.Meadow meadow2_2
                ) -> {//Expliquer pourquoi on a fait la croix entre les meadows
                    meadowBuilder.union(meadow1, meadow2_2);
                    riverBuilder.union(river1, river2);
                    meadowBuilder.union(meadow1_1, meadow2);
                    riverSystemsBuilder.union(river1,river2);
                }
                default -> throw new IllegalArgumentException();
            }
        }

        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {

            //aire contenant la zone donnée
            switch (occupiedZone) {
                case Zone.Forest forest
                        when occupantKind == Occupant.Kind.PAWN -> forestBuilder.addInitialOccupant(forest, player);
                case Zone.Meadow meadow
                        when occupantKind == Occupant.Kind.PAWN -> meadowBuilder.addInitialOccupant(meadow, player);
                case Zone.River river
                        when (occupantKind == Occupant.Kind.PAWN) -> { //|| occupantKind == Occupant.Kind.HUT) peut etre a ajouter
                    //addInitialOccupant(player, occupantKind, occupiedZone);
                    //}JSP si IL FAIT RAJOIUTER LE CAS OU ON A RIVER SYSTEM NIRMALMENT CA SERT A RIEN
                    //case Zone.Water riverSystem
                    // when (occupantKind==Occupant.Kind.PAWN ||occupantKind == Occupant.Kind.HUT)-> {
                    riverBuilder.addInitialOccupant(river, player);
                }
                case Zone.River river
                        when !river.hasLake() && Occupant.Kind.HUT == occupantKind -> { riverSystemsBuilder.addInitialOccupant(river,player);
                }
                case Zone.Lake lake
                        when Occupant.Kind.HUT == occupantKind -> riverSystemsBuilder.addInitialOccupant(lake, player);
                //case Zone.River river
                //  when river.hasLake() && Occupant.Kind.HUT == occupantKind -> riverSystemsBuilder.addInitialOccupant(river.lake(), player);
                //mettre river.lake ou river
                default -> throw new IllegalArgumentException();
            }
        }

        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest ->
                        forestBuilder.removeOccupant(forest, player);

                case Zone.Meadow meadow -> meadowBuilder.removeOccupant(meadow, player);
                case Zone.River river -> riverBuilder.removeOccupant(river, player);

                //addInitialOccupant(player, occupantKind, occupiedZone);
                //}JSP si IL FAIT RAJOIUTER LE CAS OU ON A RIVER SYSTEM NIRMALMENT CA SERT A RIEN
                //case Zone.Water riverSystem
                // when (occupantKind==Occupant.Kind.PAWN ||occupantKind == Occupant.Kind.HUT)-> {
                default -> throw new IllegalArgumentException();//Prend en comople cas ou la riveire possde un lac
            }
        }

        public void clearGatherers(Area<Zone.Forest> forest){
            forestBuilder.removeAllOccupantsOf(forest);
        }
        public void clearFishers(Area<Zone.River> river){
            riverBuilder.removeAllOccupantsOf(river);
        }
        public ZonePartitions build(){
            return new ZonePartitions(forestBuilder.build(), meadowBuilder.build(), riverBuilder.build(), riverSystemsBuilder.build());
        }
    }
}