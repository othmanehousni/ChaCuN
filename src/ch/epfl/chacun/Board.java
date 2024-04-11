package ch.epfl.chacun;

import java.util.*;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 * Represents the game board for ChaCuN, a square board of 25x25 tiles with the central tile initially placed.
 * This class manages the placement of tiles, tracking of occupants, and game state related to the board layout.
 */


public final class Board {
    private final PlacedTile[] placedTiles; // Array holding the tiles placed on the board, mostly null except for placed tiles.
    private final int[] tileIndex; // Array holding indexes of placed tiles in the order they were placed.
    private final ZonePartitions zonePartitions; // Partitions of the board into different zone types.
    private final Set<Animal> cancelledAnimals; // Set of animals that have been cancelled (removed from play).
    public static final int REACH = 12; // The reach of the board, distance from the center to the edge.
    private static final int BOARD_SIZE = 2 * REACH + 1; // Calculated board size, ensuring it covers the 25x25 tile area.

    public static final Board EMPTY = new Board(new PlacedTile[625], new int[0], ZonePartitions.EMPTY, new HashSet<>()); // Represents an empty board state.


    /**
     * Constructs a new Board instance.
     *
     * @param placedTiles     An array of PlacedTile representing the tiles placed on the board.
     * @param tileIndex       An array of integers representing the indexes of placed tiles.
     * @param zonePartitions  A ZonePartitions instance representing the partitioning of zones on the board.
     * @param cancelledAnimals A Set of Animal representing animals that have been cancelled.
     */

    private Board(PlacedTile[] placedTiles, int[] tileIndex, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.tileIndex = tileIndex;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Checks if a given position is within the board's boundaries.
     *
     * @param position The position to check.
     * @return true if the position is within the board, false otherwise.
     */
    private boolean isPositionInBoard(Pos position) {
        return Math.abs(position.x()) <= REACH && Math.abs(position.y()) <= REACH;
        //return position.x() >= REACH && position.x() < REACH + 25 && position.y() >= REACH && position.y() < REACH + 25;
    }

    /**
     * Converts a position to its corresponding index in the board's tile array.
     *
     * @param pos The position to convert.
     * @return The index of the position in the array, or -1 if the position is not on the board.
     */
    private int posToIndex(Pos pos) {
        return isPositionInBoard(pos) ? (pos.x() + REACH) + (pos.y() + REACH) * BOARD_SIZE : -1;
    }

    /**
     * Retrieves a tile placed on the board at a given position.
     *
     * @param position The position of the tile to retrieve.
     * @return The PlacedTile at the specified position, or null if no tile is placed there.
     */

    public PlacedTile tileAt(Pos position) {
        if (isPositionInBoard(position)) {
            return placedTiles[posToIndex(position)];
        }
        return null;
    }

    /**
     * Retrieves a tile placed on the board by its ID.
     *
     * @param tileId The ID of the tile to retrieve.
     * @return The PlacedTile with the specified ID.
     * @throws IllegalArgumentException If no tile with the given ID is found on the board.
     */
    public PlacedTile tileWithId(int tileId) {
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null && placedTile.id() == tileId) {
                return placedTile;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Retrieves the set of cancelled animals on the board.
     *
     * @return An unmodifiable Set of cancelled Animal.
     */
    public Set<Animal> cancelledAnimals() {
        return Collections.unmodifiableSet(cancelledAnimals);
    }

    /**
     * Retrieves all occupants placed on the board.
     *
     * @return A Set of all Occupant on the board.
     */
    public Set<Occupant> occupants() {
        Set<Occupant> allOccupants = new HashSet<>();
        for (int index : tileIndex) {
            if (placedTiles[index].occupant() != null) {
                allOccupants.add(placedTiles[index].occupant());
            }
        }
        return allOccupants;
    }

    /**
     * Retrieves the area of a specified forest on the board.
     *
     * @param forest The forest zone to find the area for.
     * @return The Area containing the specified forest zone.
     * @throws IllegalArgumentException If the specified zone is not part of the board.
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Retrieves the area of a specified meadow on the board.
     *
     * @param meadow The meadow zone to find the area for.
     * @return The Area containing the specified meadow zone.
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Retrieves the area of a specified river on the board.
     *
     * @param river The river zone to find the area for.
     * @return The Area containing the specified river zone.
     */
    public Area<Zone.River> riverArea(Zone.River river) {
        return zonePartitions.rivers().areaContaining(river);
    }

    /**
     * Retrieves the area of a specified water system on the board, including rivers and lakes.
     *
     * @param water The water zone to find the area for.
     * @return The Area containing the specified water system.
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    /**
     * Retrieves all meadow areas on the board.
     *
     * @return A Set of all meadow areas.
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }

    /**
     * Retrieves all water system areas on the board, including rivers and lakes.
     *
     * @return A Set of all water system areas.
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }

    /**
     * Retrieves the adjacent meadow area to a specified position and meadow zone, including all zones of the meadow but only occupants of the complete meadow.
     *
     * @param pos The position near which to find the adjacent meadow.
     * @param meadowZone The meadow zone to find the adjacent area for.
     * @return The Area of the adjacent meadow.
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Set<Zone.Meadow> zoneOfTheAdjacentMeadow = new HashSet<>();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                PlacedTile placedTile = tileAt(pos.translated(i, j));
                if (placedTile != null) {
                    zoneOfTheAdjacentMeadow.addAll(placedTile.meadowZones());
                }

            }
        }
        Area<Zone.Meadow> occupantsMeadowArea = meadowArea(meadowZone);
        zoneOfTheAdjacentMeadow.retainAll(occupantsMeadowArea.zones());

        return new Area<>(zoneOfTheAdjacentMeadow, occupantsMeadowArea.occupants(), 0);
    }

    /**
     * Counts the number of occupants of a certain kind belonging to a specific player on the board.
     *
     * @param player       The player whose occupants to count.
     * @param occupantKind The kind of occupant to count.
     * @return The count of specified occupants belonging to the given player.
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int count = 0;
        for (int index : tileIndex) {
            PlacedTile tile = placedTiles[index];
            if (tile.placer() == player && tile.occupant() != null && occupantKind == tile.occupant().kind()) {
                count++;
            }
        }
        return count;

    }

    /**
     * Determines the positions on the board where a new tile can potentially be placed.
     *
     * @return A Set of possible insertion positions for a new tile.
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int index : tileIndex) {
            for (Direction direction : Direction.ALL) {
                Pos tilePos = placedTiles[index].pos().neighbor(direction);
                if (isPositionInBoard(tilePos) && tileAt(tilePos) == null) {
                    insertionPositions.add(tilePos);
                }
            }
        }
        return insertionPositions;
    }

    /**
     * Retrieves the last tile placed on the board.
     *
     * @return The most recently placed PlacedTile, or null if the board is empty.
     */
    public PlacedTile lastPlacedTile() {
        return tileIndex.length == 0 ? null : placedTiles[tileIndex[tileIndex.length - 1]];
    }


    /**
     * Retrieves a set of all forest areas that were closed as a result of placing the last tile on the board.
     * A closed forest area is one that has no open connections left.
     *
     * @return A Set of Area objects representing closed forests. If no forests were closed, returns an empty set.
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForests = new HashSet<>();
        if (tileIndex.length !=0) {
            lastPlacedTile().forestZones().forEach(forest -> {
                if (forestArea(forest).isClosed()) {
                    closedForests.add(forestArea(forest));
                }
            });
        }
        return closedForests;
    }

    /**
     * Retrieves a set of all river areas that were closed as a result of placing the last tile on the board.
     * A closed river area is one that has no open connections left.
     *
     * @return A Set of Area objects representing closed rivers. If no rivers were closed, returns an empty set.
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRiver = new HashSet<>();
        if (tileIndex.length != 0) {
            lastPlacedTile().riverZones().forEach(river -> {
                if (riverArea(river).isClosed()) {
                    closedRiver.add(riverArea(river));
                }
            });
        }
        return closedRiver;
    }

    /**
     * Determines whether a given PlacedTile can be added to the board based on the current state of the board.
     * The tile can be added if its position matches one of the insertion positions and its sides match the neighboring tiles.
     *
     * @param tile The PlacedTile to be added.
     * @return true if the tile can be added to the board; false otherwise.
     */

    public boolean canAddTile(PlacedTile tile) {
        boolean b = false;
        if (!isPositionInBoard(tile.pos()) || tileAt(tile.pos()) != null) {
            return false;
        }
            for (Direction direction : Direction.ALL) {
                PlacedTile neighbor = tileAt(tile.pos().neighbor(direction));
                TileSide side = tile.side(direction);
                if (neighbor != null) {
                    b = true;
                    if (!side.isSameKindAs(neighbor.side(direction.opposite()))) {
                        return false;
                    }
                }
            }
            return b;



    }

    /**
     * Determines whether it is possible to place a given Tile on the board in any orientation.
     * Checks all insertion positions and rotations to see if the tile could fit anywhere on the board.
     *
     * @param tile The Tile to be placed.
     * @return true if the tile could be placed on the board in any orientation; false otherwise.
     */

    public boolean couldPlaceTile(Tile tile) {
        for (Pos position : insertionPositions()) {
            for (Rotation rotation : Rotation.ALL) {
                PlacedTile placedTile = new PlacedTile(tile, null, rotation, position);
                if (canAddTile(placedTile)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a new Board instance with a new tile added to it. This method updates the board's state accordingly.
     * The method checks if adding the tile is valid before making any changes.
     *
     * @param tile The PlacedTile to add to the board.
     * @return A new Board instance with the tile added.
     * @throws IllegalArgumentException if the tile cannot be added to the board.
     */
    public Board withNewTile(PlacedTile tile) {
        Preconditions.checkArgument(canAddTile(tile) || tileIndex.length == 0);

        PlacedTile[] newPlacedTiles = placedTiles.clone();

        newPlacedTiles[posToIndex(tile.pos())] = tile;

        int[] newTileIndex = Arrays.copyOf(tileIndex, tileIndex.length + 1);
        newTileIndex[tileIndex.length] = posToIndex(tile.pos());

        ZonePartitions.Builder newZonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        newZonePartitionsBuilder.addTile(tile.tile());

        for(Direction direction : Direction.ALL){
            PlacedTile neighborTile= tileAt(tile.pos().neighbor(direction));
            if(neighborTile !=null && isPositionInBoard(neighborTile.pos())){
                newZonePartitionsBuilder.connectSides(tile.side(direction), neighborTile.side(direction.opposite()));
            }
        }

        return new Board(newPlacedTiles, newTileIndex, newZonePartitionsBuilder.build(), cancelledAnimals);

    }

    /**
     * Returns a new Board instance with an occupant added to a specified tile. Updates the board's and zone partitions' state.
     *
     * @param occupant The Occupant to add to the board.
     * @return A new Board instance with the occupant added.
     * @throws IllegalArgumentException if the specified tile is already occupied.
     */
    public Board withOccupant(Occupant occupant) {
        PlacedTile tile = tileWithId(Zone.tileId(occupant.zoneId()));
        PlacedTile[] newPlacedTiles = placedTiles.clone();

        newPlacedTiles[posToIndex(tile.pos())] = tile.withOccupant(occupant);

        ZonePartitions.Builder newZonePartitions = new ZonePartitions.Builder(zonePartitions);
        newZonePartitions.addInitialOccupant(tile.placer(), occupant.kind(), tile.zoneWithId(occupant.zoneId()));

        return new Board(newPlacedTiles, tileIndex, newZonePartitions.build(), cancelledAnimals);

    }


    /**
     * Returns a new Board instance with an occupant removed from a specified tile. This reflects changes in both the board's state and zone partitions.
     *
     * @param occupant The Occupant to remove from the board.
     * @return A new Board instance with the occupant removed.
     * @throws IllegalArgumentException if the specified occupant does not exist on the board.
     */
    public Board withoutOccupant(Occupant occupant) {
            PlacedTile tile = tileWithId(Zone.tileId(occupant.zoneId()));
            ZonePartitions.Builder newZonePartitions = new ZonePartitions.Builder(zonePartitions);
            PlacedTile[] newPlacedTiles = placedTiles.clone();

            newPlacedTiles[posToIndex(tile.pos())] = tile.withNoOccupant();
            newZonePartitions.removePawn(tile.placer(), tile.zoneWithId(occupant.zoneId()));

            return new Board(newPlacedTiles, tileIndex, newZonePartitions.build(), cancelledAnimals);
        }

    /**
     * Returns a new Board instance with all gatherers (pawn occupants) or fishers removed from specified forests and rivers.
     *
     * @param forests A Set of Area<Zone.Forest> from which all gatherers will be removed.
     * @param rivers A Set of Area<Zone.River> from which all fishers will be removed.
     * @return A new Board instance reflecting the removal of specified occupants.
     */

    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        PlacedTile[] newPlacedTiles = placedTiles.clone();
        ZonePartitions.Builder newZonePartitions = new ZonePartitions.Builder(zonePartitions);

        forests.forEach(forest -> {
            newZonePartitions.clearGatherers(forest);
                forest.zones().forEach(zone -> {
                    PlacedTile tile = tileWithId(Zone.tileId(zone.id()));
                    if(tile.occupant() != null && tile.occupant().zoneId() == zone.id()) {
                        newPlacedTiles[posToIndex(tile.pos())] = tileWithId(zone.tileId()).withNoOccupant();
                    }
                });
                });


        rivers.forEach(river -> {
            newZonePartitions.clearFishers(river);
            river.zones().forEach(zone -> {
                PlacedTile tile = tileWithId(Zone.tileId(zone.id()));
                if (tile.occupant() != null && tile.occupant().zoneId() == zone.id() && tile.occupant().kind() == Occupant.Kind.PAWN) {
                    newPlacedTiles[posToIndex(tile.pos())] = tileWithId(zone.tileId()).withNoOccupant();
                }
            });

        });
        return new Board(newPlacedTiles, tileIndex, newZonePartitions.build(), cancelledAnimals);

    }

    /**
     * Returns a new Board instance with additional cancelled animals added to the existing set of cancelled animals.
     * This reflects a change in the game state where certain animals are no longer available for scoring.
     *
     * @param newlyCancelledAnimals A Set of Animals to be added to the cancelled animals set.
     * @return A new Board instance with the updated set of cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals);
        newCancelledAnimals.addAll(newlyCancelledAnimals);
        return new Board(placedTiles, tileIndex, zonePartitions, newCancelledAnimals);
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof Board board &&
                Arrays.equals(this.placedTiles, board.placedTiles)
                && Arrays.equals(this.tileIndex, board.tileIndex)
                && Objects.equals(this.zonePartitions, board.zonePartitions)
                && Objects.equals(this.cancelledAnimals, board.cancelledAnimals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(placedTiles), Arrays.hashCode(tileIndex), zonePartitions, cancelledAnimals);
    }

}

