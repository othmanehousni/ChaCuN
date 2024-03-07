package ch.epfl.chacun;

import java.util.*;

public record Area <Z extends Zone> (Set<Z> zones , List<PlayerColor> occupants, int openConnections) {

    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        zones = Set.copyOf(zones);
        List <PlayerColor> sorted = occupants.stream().sorted().toList();// sort par rappport au kind
        occupants = List.copyOf(sorted);
    }

    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest forestZones : forest.zones) {
            if (forestZones.kind().equals(Zone.Forest.Kind.WITH_MENHIR)) {
                return true;
            }
        }
        return false;
    }

    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int mushroomCount = 0;
        for (Zone.Forest forestZones : forest.zones) {
            if (forestZones.kind().equals(Zone.Forest.Kind.WITH_MUSHROOMS)) {
                mushroomCount += 1;
            }
        }
        return mushroomCount;
    }

    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> remainingAnimals = new HashSet<>();
        for (Zone.Meadow meadowZones : meadow.zones) {
            remainingAnimals.addAll(meadowZones.animals()); }
        if(cancelledAnimals != null) {
            remainingAnimals.removeAll(cancelledAnimals);
        }
        return remainingAnimals;
    }

    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        Set<Zone.Lake> lakeFishZone = new HashSet<>();
        for (Zone.River riverZone : river.zones) {
            fishCount += riverZone.fishCount();
            if(riverZone.hasLake() && lakeFishZone.add(riverZone.lake())) { //verifier la premirere condition
                fishCount += riverZone.lake().fishCount();
            }
        }
        return fishCount;

    }

    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        int fishCount = 0;
        for (Zone.Water riverZone : riverSystem.zones) {
                fishCount += riverZone.fishCount();
            }
        return fishCount;
    }

    public static int lakeCount(Area<Zone.Water> riverSystem) {
        int lakes = 0;
        for (Zone.Water riverZone : riverSystem.zones) {
            if (riverZone instanceof Zone.Lake) {
                lakes += 1;
            }
        }
        return lakes;
    }

    public boolean isClosed() {
        return openConnections == 0;
    }

    public boolean isOccupied() {
        return !occupants.isEmpty();
    }

    public Set<PlayerColor> majorityOccupants() {
        int[] occupantCount = new int[PlayerColor.values().length];
        Set<PlayerColor> majorityOccupants = new HashSet<>();
        for (PlayerColor occupantColor : occupants) {
            occupantCount[occupantColor.ordinal()]++; }
        int maxOccupantColor = Arrays.stream((occupantCount)).max().getAsInt();
        if (maxOccupantColor > 0) {
            for (int i = 0; i < occupantCount.length; i++) {
                if (occupantCount[i] == maxOccupantColor) {
                    majorityOccupants.add(PlayerColor.values()[i]);
                }
            }
        }
        return majorityOccupants;
    }

    public Area<Z> connectTo(Area<Z> that){
        if(that.equals(this)){
            if(that.openConnections < 2) {
                return new Area<>(zones, occupants,0);
            } return new Area<>(zones, occupants,openConnections -2);
        } else {
            List<PlayerColor> combinedOccupants = new ArrayList<>(occupants);
            combinedOccupants.addAll(that.occupants);
            Set<Z> combinedZones = new HashSet<>(zones);
            combinedZones.addAll(that.zones);
            int combinedOpenConnections = openConnections + that.openConnections - 2;
            if(combinedOpenConnections < 0) {
                combinedOpenConnections = 0;
            }
            return new Area<>(combinedZones,combinedOccupants,combinedOpenConnections);
        }
    }

    public Area<Z> withInitialOccupant(PlayerColor occupant){
        Preconditions.checkArgument(!isOccupied());
        return new Area<>(zones, List.of(occupant), openConnections);
    }

    public Area<Z> withoutOccupant(PlayerColor occupant){
        Preconditions.checkArgument(occupants.contains(occupant));
        List<PlayerColor> updatedOccupants = new ArrayList<>(occupants);
        updatedOccupants.remove(occupant);
        return new Area<>(zones, updatedOccupants, openConnections);
    }

    public Area<Z> withoutOccupants(){
        return new Area<>(zones, new ArrayList<>(),openConnections);
    }

    public Set<Integer> tileIds(){
        Set<Integer> tileIdsSet = new HashSet<>();
        for (Zone zone : zones){
            tileIdsSet.add(zone.tileId());
        }
        return tileIdsSet;
    }

    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Zone specialZone : zones) {
            if (specialPower.equals(specialZone.specialPower())) {
                return specialZone;
            }
        }
        return null;
    }
}

