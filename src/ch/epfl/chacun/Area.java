package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Area <Z extends Zone> (Set<Z> zones , List<PlayerColor> occupants, int openConnections) {

    public Area {
        if (openConnections < 0) {
            throw new IllegalArgumentException();
        }
        zones = Set.copyOf(zones);
        Collections.sort(occupants);
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
        Set<Animal> remainingAnimals = new HashSet<>(meadow.zones);


    }

    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        for (Zone riverZone : river.zones) {
            if (riverZone instanceof Zone.River) {
                fishCount = ((Zone.River) riverZone).fishCount();
            }
        }
        return fishCount;
        
    }

    public static int riverSystemFishCount(Area<Zone.Water> riverSystem) {
        int fishCount = 0;
        for (Zone riverZone : riverSystem.zones) {
            if (riverZone instanceof Zone.River) {
                fishCount = ((Zone.River) riverZone).fishCount();
            } else if (riverZone instanceof Zone.Lake) {
                fishCount = ((Zone.Lake) riverZone).fishCount();
            } else {
                return 0;
            }
        }
        return fishCount;
    }

    public static int lakeCount(Area<Zone.Water> riverSystem) {
        int lakes = 0;
        for (Zone riverZone : riverSystem.zones) {
            if (riverZone instanceof Zone.Lake) {
                lakes += lakes;
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
        return !occupants.isEmpty();
    }

    public Set<PlayerColor> majorityOccupants() {
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

