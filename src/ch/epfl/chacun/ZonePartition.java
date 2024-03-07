package ch.epfl.chacun;

import java.util.*;

public record ZonePartition <Z extends Zone> (Set <Area<Z>> areas) {

    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    public ZonePartition() {
        this(new HashSet<>());
    }

    public Area<Z> areaContaining(Z zone) {
        for (Area<Z> area : areas) {
            if (area.zones().contains(zone)) {
                return area;
            }
        }
        throw new IllegalArgumentException();
    }

    public final static class Builder<Z extends Zone> {

        private Set<Area<Z>> areas;

        public Builder(ZonePartition<Z> zonePartition) {
            this.areas = new HashSet<>(zonePartition.areas());
        }

        private Area<Z> areaContaining(Z zone, Set<Area<Z>> areas) {//est ce que ca doit etre static?
            for (Area<Z> area : areas) {
                if (area.zones().contains(zone)) {
                    return area;
                }
            }
            throw new IllegalArgumentException();
        }

        void addSingleton(Z zone, int openConnections) {
            Area<Z> newArea = new Area<>(Set.of(zone), List.of(), openConnections);
            areas.add(newArea);
        }

        void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> newArea = areaContaining(zone,areas).withInitialOccupant(color);
            areas.remove(areaContaining(zone,areas));
            areas.add(newArea);
        }

        void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> newArea = areaContaining(zone,areas).withoutOccupant(color);
            areas.remove(areaContaining(zone,areas));
            areas.add(newArea);
        }

        void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.contains(area));
            area.withoutOccupants();
            ;
        }

        void union(Z zone1, Z zone2) {
            Area<Z> Area1 = areaContaining(zone1, areas);
            Area<Z> Area2 = areaContaining(zone2, areas);
            if(!Area1.equals(Area2)){
                areas.add(Area1.connectTo(Area2));
                areas.remove(Area1);
                areas.remove(Area2);
            }

        }
        ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}