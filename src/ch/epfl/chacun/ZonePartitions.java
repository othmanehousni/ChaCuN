package ch.epfl.chacun;

import java.util.HashSet;

public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadow,
                             ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    final public static class Builder {
        //4 attributs privés et sont 4 partitions de batisseur de zone de type ZonePartition.Builder<…> (a l'interieur on met soit forest soit ...)
        private ZonePartition.Builder<Zone.Forest> forestBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowBuilder;
        private ZonePartition.Builder<Zone.River> riverBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;


        public Builder(ZonePartitions initial) {
            this.forestBuilder = new ZonePartition.Builder<>(initial.forests);
            this.meadowBuilder = new ZonePartition.Builder<>(initial.meadow);
            this.riverBuilder = new ZonePartition.Builder<>(initial.rivers);
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems);
        }

        void addTile(Tile tile) {
            //premiere partie
            int[] numberOfConnections = new int[10];

            for (TileSide sides : tile.sides()) {
                for (Zone sideZones : sides.zones()) {
                    numberOfConnections[sideZones.localId()]++;
                    //Ajouet nom rivierre et enlbver  ((Zone.River) sideZones) remplacer par nom de la riviere
                    if (sideZones instanceof Zone.River && ((Zone.River) sideZones).hasLake()) {
                        //numberOfConnections[sideZones.localId()]++ askip IL FAT AAUJOUTER;
                        numberOfConnections[((Zone.River) sideZones).lake().localId()]++;
                    }
                }
            }
            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.Forest) {
                    forestBuilder.addSingleton((Zone.Forest) zone, numberOfConnections[zone.localId()]);
                } else if (zone instanceof Zone.Meadow) {
                    meadowBuilder.addSingleton((Zone.Meadow) zone, numberOfConnections[zone.localId()]);
                } else if (zone instanceof Zone.River && !((Zone.River) zone).hasLake()) {
                    riverBuilder.addSingleton((Zone.River) zone, numberOfConnections[zone.localId()]);
                } else {

                }
            }

        }
    }
}