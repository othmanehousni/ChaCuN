package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] tileIndex;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;
    public static final int REACH = 12;
    public static final Board EMPTY;

    static {
        PlacedTile[] emptyPlacedTiles = new PlacedTile[625];
        int[] emptyTileIndices = new int[625];
        ZonePartitions emptyZonePartitions = ZonePartitions.EMPTY;
        Set<Animal> emptyCancelledAnimals = new HashSet<>();
        EMPTY = new Board(emptyPlacedTiles, emptyTileIndices, emptyZonePartitions, emptyCancelledAnimals);
    }

    private Board(PlacedTile[] placedTiles, int[] tileIndex, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
        this.tileIndex = tileIndex;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    private static Board emptyBoard() {
        return EMPTY;
    }

    private boolean isPositionInBoard(Pos position) {
        return position.x() >= REACH && position.x() < REACH + 25 && position.y() >= REACH && position.y() < REACH + 25;
    }

    private int tileIndexWithId(int id) {
        for (int i = 0; i < tileIndex.length; i++) {
            if (placedTiles[i] != null && placedTiles[i].tile().id() == id) {
                return i;
            }
        }
        return -1;
    }

    public PlacedTile TileAt(Pos position) {
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null && placedTile.pos().equals(position)) {
                return placedTile;
            }
        }
        return null;
    }

    public PlacedTile tileWithId(int id) {
        for (PlacedTile placedTile : placedTiles) {
            if (placedTile != null && placedTile.tile().id() == id) {
                return placedTile;
            }
        }
        return null;
    }

    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int index : tileIndex) {
            for (Direction direction : Direction.ALL) {
                if (isPositionInBoard(placedTiles[index].pos().neighbor(direction))
                        &&
                        TileAt(placedTiles[index].pos().neighbor(direction)) == null) {
                    insertionPositions.add(placedTiles[index].pos().neighbor(direction));
                }
            }
            }
        return insertionPositions;
    }

    public PlacedTile lastPlacedTile() {
        return tileIndex.length == 0 ? null : placedTiles[tileIndex[tileIndex.length - 1]];
    }

    public Area<Zone.Forest> forestArea (Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    public Area<Zone.River> riverArea (Zone.River river) {
        return zonePartitions.rivers().areaContaining(river);
    }

   /* public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    public Set<Area<Zone.Water>> riverSystemArea() {
        return zonePartitions.riverSystems().areas();

    public Set<Area<Zone.Meadow>> meadowArea() {
        return zonePartitions.meadows().areas();
    }*/

    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForests = new HashSet<>();
        if (lastPlacedTile() != null) {
            lastPlacedTile().forestZones().forEach(forest -> {
                if (forestArea(forest).isClosed()) {
                    closedForests.add(forestArea(forest));
                }
            });
        }
        return closedForests;
    }

    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRiver = new HashSet<>();
        if (lastPlacedTile() != null) {
            lastPlacedTile().riverZones().forEach(river -> {
                if (riverArea(river).isClosed()) {
                    closedRiver.add(riverArea(river));
                }
            });
        }
        return closedRiver;
    }

    public boolean canAddTile(PlacedTile tile) {
        return tile != null && insertionPositions().contains(tile.pos());

        // TODO CHECK LIEN AVEC INDEX LENGTH ?

    }

    public boolean couldPlaceTile(Tile tile) {
        for (Pos position : insertionPositions()) {
            for (Rotation rotation : Rotation.ALL) {
                PlacedTile placedTile = new PlacedTile(tile, null, rotation, position);
                return canAddTile(placedTile);
                }
            }
        return false;
        }

    public Board withOccupant(Occupant occupant) {
        if (occupant != null && tileWithId(Zone.tileId(occupant.zoneId())) != null) {
            PlacedTile[] newPlacedTiles = placedTiles.clone();
            newPlacedTiles[tileIndexWithId(Zone.tileId(occupant.zoneId()))]
                    = newPlacedTiles[tileIndexWithId(Zone.tileId(occupant.zoneId()))].withOccupant(occupant);
            return new Board(newPlacedTiles, tileIndex, zonePartitions, cancelledAnimals);
        } else {
            throw new IllegalArgumentException();
                }

    }


    public Board withoutOccupant(Occupant occupant) {
        if (occupant != null && tileWithId(Zone.tileId(occupant.zoneId())) != null) {
            PlacedTile[] newPlacedTiles = placedTiles.clone();
            newPlacedTiles[tileIndexWithId(Zone.tileId(occupant.zoneId()))].withNoOccupant();
            return new Board(newPlacedTiles, tileIndex, zonePartitions, cancelledAnimals);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        Set<Area<Zone.Forest>> newForests = new HashSet<>();
        Set<Area<Zone.River>> newRivers = new HashSet<>();

        forests.forEach(forest -> newForests.add(forest.withoutOccupants()));
        rivers.forEach(river -> newRivers.add(river.withoutOccupants()));

        ZonePartitions newZonePartitions = new ZonePartitions(new ZonePartition<>(newForests), zonePartitions.meadows(), new ZonePartition<>(newRivers), zonePartitions.riverSystems());

        return new Board(placedTiles, tileIndex, newZonePartitions, cancelledAnimals );

        }

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set <Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals);
        newlyCancelledAnimals.addAll(newCancelledAnimals);
        return new Board(placedTiles, tileIndex, zonePartitions, newCancelledAnimals);
    }

}
