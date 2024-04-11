package ch.epfl.chacun;

import java.util.*;

/**
 * Represents a generic, immutable area in the game, parameterized by zone type {@code Z} bound by {@link Zone}.
 * Represents an area composed of various zones within the game.
 * An area consists of zones, occupants (players identified by their colors), and open connections.
 * It includes features to manage zones, occupants based on player colors, and open connections for gameplay dynamics.
 *
 * @param <Z> The type of zones constituting the area, extending {@link Zone}.
 * @param zones The set of zones constituting the area.
 * @param occupants The sorted list of player colors occupying the area.
 * @param openConnections The number of open connections in the area, must be non-negative.
 *
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public record Area <Z extends Zone> (Set<Z> zones , List<PlayerColor> occupants, int openConnections) {

    /**
     * Constructs an {@code Area} instance ensuring immutability and validity of the provided parameters.
     * The {@code zones} and {@code occupants} are copied to ensure immutability. The {@code occupants} list is sorted
     * by player color. {@code openConnections} must be non-negative.
     *
     * @throws IllegalArgumentException if {@code openConnections} is negative.
     */
    public Area {
        Preconditions.checkArgument(openConnections >= 0);
        zones = Set.copyOf(zones);
        List <PlayerColor> sorted = occupants.stream().sorted().toList();
        occupants = List.copyOf(sorted);
    }


    /**
     * Checks if the given forest area contains at least one menhir.
     *
     * @param forest The forest area to check.
     * @return true if the forest contains at least one menhir, otherwise false.
     */

    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        for (Zone.Forest forestZones : forest.zones) {
            if (forestZones.kind().equals(Zone.Forest.Kind.WITH_MENHIR)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Calculates the number of mushroom groups in the given forest area.
     *
     * @param forest The forest area to inspect.
     * @return The count of mushroom groups in the forest.
     */
    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        int mushroomCount = 0;
        for (Zone.Forest forestZones : forest.zones) {
            if (forestZones.kind().equals(Zone.Forest.Kind.WITH_MUSHROOMS)) {
                mushroomCount += 1;
            }
        }
        return mushroomCount;
    }

    /**
     * Retrieves the set of animals present in the given meadow area, excluding any cancelled animals.
     *
     * @param meadow The meadow area to inspect.
     * @param cancelledAnimals The set of animals to exclude from the result.
     * @return A set of animals present in the meadow, excluding cancelled ones.
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> remainingAnimals = new HashSet<>();
        for (Zone.Meadow meadowZones : meadow.zones) {
            remainingAnimals.addAll(meadowZones.animals()); }
        if(cancelledAnimals != null) {
            remainingAnimals.removeAll(cancelledAnimals);
        }
        return remainingAnimals;
    }

    /**
     * Calculates the total fish count in the given river area, including any lakes at its ends.
     *
     * @param river The river area to inspect.
     * @return The total fish count in the river and its lakes.
     */

    public static int riverFishCount(Area<Zone.River> river) {
        int fishCount = 0;
        Set<Zone.Lake> lakeFishZone = new HashSet<>();
        for (Zone.River riverZone : river.zones) {
            fishCount += riverZone.fishCount();
            if(riverZone.hasLake() && lakeFishZone.add(riverZone.lake())) {
                fishCount += riverZone.lake().fishCount();
            }
        }
        return fishCount;

    }

    /**
     * Calculates the total fish count in the specified river system, encompassing all water zones.
     *
     * @param riverSystem The river system to inspect.
     * @return The total fish count within the entire river system.
     */

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

    /**
     * Checks whether the area is closed, meaning it has no open connections.
     *
     * @return {@code true} if the area has no open connections and is considered closed, otherwise {@code false}.
     */

    public boolean isClosed() {
        return openConnections == 0;
    }

    /**
     * Determines whether the area is currently occupied by any player(s).
     *
     * @return {@code true} if there is at least one occupant in the area, otherwise {@code false}.
     */
    public boolean isOccupied() {
        return !occupants.isEmpty();
    }

    /**
     * Retrieves the set of colors belonging to the majority occupants in the area.
     * In case of a tie, all tying colors are included.
     *
     * @return A set of {@link PlayerColor} representing the colors of the majority occupants.
     */
    public Set<PlayerColor> majorityOccupants() {
        int[] occupantCount = new int[PlayerColor.values().length];
        Set<PlayerColor> majorityOccupants = new HashSet<>();
        for (PlayerColor occupantColor : occupants) {
            occupantCount[occupantColor.ordinal()]++; }
        if(Arrays.stream((occupantCount)).max().isPresent()) {
            int maxOccupantColor = Arrays.stream((occupantCount)).max().getAsInt();
            if (maxOccupantColor > 0) {
                for (int i = 0; i < occupantCount.length; i++) {
                    if (occupantCount[i] == maxOccupantColor) {
                        majorityOccupants.add(PlayerColor.values()[i]);
                    }
                }
            }
        }
        return majorityOccupants;
    }

    /**
     * Connects the current area with another area, merging their zones and occupants.
     * The open connections of the resulting area are adjusted accordingly.
     *
     * @param that The area to connect with.
     * @return A new {@code Area} instance representing the result of connecting the two areas.
     */

    public Area<Z> connectTo(Area<Z> that){
        if(that.equals(this)){
            if(that.openConnections < 2) {
                return new Area<>(zones, occupants,0);
            } else {
                return new Area<>(zones, occupants,openConnections - 2);
            }
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

    /**
     * Creates a new area identical to the current one but with an initial occupant added.
     * If the area already has occupants, an {@code IllegalArgumentException} is thrown.
     *
     * @param occupant The color of the initial occupant to add.
     * @return A new {@code Area} instance with the initial occupant added.
     * @throws IllegalArgumentException if the area is already occupied.
     */

    public Area<Z> withInitialOccupant(PlayerColor occupant){
        Preconditions.checkArgument(!isOccupied());
        return new Area<>(zones, List.of(occupant), openConnections);
    }

    /**
     * Creates a new area identical to the current one but with one occupant of the specified color removed.
     * If no such occupant exists, an {@code IllegalArgumentException} is thrown.
     *
     * @param occupant The color of the occupant to remove.
     * @return A new {@code Area} instance with the specified occupant removed.
     * @throws IllegalArgumentException if the area doesn't contain an occupant of the specified color.
     */

    public Area<Z> withoutOccupant(PlayerColor occupant){
        Preconditions.checkArgument(occupants.contains(occupant));
        List<PlayerColor> updatedOccupants = new ArrayList<>(occupants);
        updatedOccupants.remove(occupant);
        return new Area<>(zones, updatedOccupants, openConnections);
    }

    /**
     * Creates a new area identical to the current one but without any occupants.
     *
     * @return A new {@code Area} instance devoid of any occupants.
     */

    public Area<Z> withoutOccupants(){
        return new Area<>(zones, new ArrayList<>(),openConnections);
    }

    /**
     * Retrieves the set of tile IDs that contain this area.
     *
     * @return A set of integers representing the IDs of tiles containing this area.
     */

    public Set<Integer> tileIds(){
        Set<Integer> tileIdsSet = new HashSet<>();
        for (Zone zone : zones){
            tileIdsSet.add(zone.tileId());
        }
        return tileIdsSet;
    }

    /**
     * Finds and returns the zone within the area that possesses a specific special power, if any.
     *
     * @param specialPower The special power to search for within the area's zones.
     * @return The zone that has the specified special power, or {@code null} if no such zone exists.
     */

    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Zone specialZone : zones) {
            if (specialPower.equals(specialZone.specialPower())) {
                return specialZone;
            }
        }
        return null;
    }
}

