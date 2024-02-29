package ch.epfl.chacun;

import java.util.*;

public record Area <Z extends Zone> (Set<Z> zones , List<PlayerColor> occupants, int openConnections) {

    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        zones = Set.copyOf(zones);
        List<PlayerColor> sorted = new ArrayList<>(occupants);// recheck si faisable direct
        Collections.sort(occupants);
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
            if(forestZones.kind().equals(Zone.Forest.Kind.WITH_MUSHROOMS)) {
                mushroomCount = 1;
            }
        }
        return mushroomCount;
    }

    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> remainingAnimals = new HashSet<>();
        for (Zone.Meadow meadowZones : meadow.zones) {
            remainingAnimals.addAll(meadowZones.animals());
            remainingAnimals.forEach( animal -> {
                if (cancelledAnimals.contains(animal)) {
                    remainingAnimals.remove(animal);
                }
            });
        }
        return remainingAnimals;
    }

    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        for (Zone.River riverZone : river.zones) {
            fishCount = riverZone.fishCount();
            if(riverZone.hasLake()) {
                fishCount += riverZone.lake().fishCount();
            }
        }
        return fishCount;

    }

    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        int fishCount = 0;
        for (Zone.Water riverZone : riverSystem.zones) {
            if (riverZone instanceof Zone.River) {
                fishCount += riverZone.fishCount();
            } else if (riverZone instanceof Zone.Lake) {
                fishCount += riverZone.fishCount();
            }
        }
        return fishCount;
    }

    public static int lakeCount(Area<Zone.Water> riverSystem) {
        int lakes = 0;
        for (Zone riverZone : riverSystem.zones) {
            if (riverZone instanceof Zone.Lake) {
                lakes += 1;
            } else {
                return lakes;
            }
        }
        return lakes;
    }

    public boolean isClosed() {
        return openConnections == 0;
    }

    public boolean isOccupied() {
        return !occupants.isEmpty(); //null?
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
}

public Area<Z> connectTo(Area<Z> that) {
}

public Area<Z> withInitialOccupant(PlayerColor occupant) {
}

public Area<Z> withInitialOccupant(PlayerColor occupant) {
}

public Area<Z> withoutOccupants() {
}

public Set<Integer> tileIds() {
}

public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
    for (Z SPzone : zones) {
        if (SPzone.specialPower() == specialPower) {
            return SPzone;
        }
    }
    return null;
}
}

