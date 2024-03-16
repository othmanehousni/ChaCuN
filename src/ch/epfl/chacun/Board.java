package ch.epfl.chacun;

import java.util.HashSet;
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

    public PlacedTile TileAt(Pos position) {
        for (int i = 0; i < placedTiles.length; i++) {
            if (placedTiles[i] != null && placedTiles[i].pos().equals(position)) {
                return placedTiles[i];
            }
        }
        return null;
    }

    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int index : tileIndex) {
            for (Direction direction : Direction.ALL) {
                if (isPositionInBoard(placedTiles[index].pos().neighbor(direction)) && TileAt(placedTiles[index].pos().neighbor(direction)) == null) {
                    insertionPositions.add(placedTiles[index].pos().neighbor(direction));
                }
            }
            //check si position est dans board : pos.x et reach : les deux en methodes privees
            // trouver la tile qui correspond a la position
            // si la tile est null alors on ajoute la position a insertionPositions
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

    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        Set<Area<Zone.Forest>> closedForests = new HashSet<>();
        PlacedTile lastTile = lastPlacedTile();
        if (lastTile != null) {
            for (Zone.Forest forest : lastTile.forestZones()) {
                Area<Zone.Forest> forestArea = forestArea(forest);
                if(forestArea.isClosed()) {
                    closedForests.add(forestArea);
                }
            }
        }
        return closedForests;
    }

    public Set<Area<Zone.River>> riversClosedByLastTile() {
        Set<Area<Zone.River>> closedRiver = new HashSet<>();
        PlacedTile lastTile = lastPlacedTile();
        if (lastTile != null) {
            for (Zone.River river : lastTile.riverZones()) {
                Area<Zone.River> riverArea = riverArea(river);
                if (riverArea.isClosed()) {
                    closedRiver.add(riverArea);
                }
            }
        }
        return closedRiver;
    }

    public boolean canAddTile(PlacedTile tile) {
        if (tile != null && insertionPositions().contains(tile.pos())) {
            return true;
        } else {
            return false;
        } // TODO CHECK LIEN AVEC INDEX LENGTH
    }

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

    public Board withNewTile(PlacedTile tile) {
        if (canAddTile(tile)) {
            PlacedTile[] newPlacedTiles = placedTiles.clone();
            int[] newTileIndex = tileIndex.clone();
            ZonePartitions.Builder newZonePartitions = zonePartitions;
            Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals);
            newPlacedTiles[newTileIndex.length] = tile;
            newTileIndex[newTileIndex.length] = newTileIndex.length;
            newZonePartitions.addTile(tile.tile());
            return new Board(newPlacedTiles, newTileIndex, newZonePartitions.build(), newCancelledAnimals);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Board withOccupant(Occupant occupant) {
        if (occupant != null) {
            PlacedTile[] newPlacedTiles = placedTiles.clone();

            //for(int i = 0; i < tileIndex.length; i++) {
            // if(newPlacedTiles[i] == null) {
            // newPlacedTiles[i] = newPlacedTiles[i].withOccupant(occupant);
            //break;
           // } TODO : wallahi je sais pas c une proposition

            newPlacedTiles[tileIndex.length] = newPlacedTiles[tileIndex.length].withOccupant(occupant);
            return new Board(newPlacedTiles, tileIndex, zonePartitions, cancelledAnimals);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Board withoutOccupant(Occupant occupant) {
        if (occupant != null) {
            PlacedTile[] newPlacedTiles = placedTiles.clone();
            for(int i = 0; i < tileIndex.length; i++) {
                if (newPlacedTiles[i].occupant().equals(occupant)) {
                    newPlacedTiles[i] = null;
                    break;
                }
            }
           // TODO : jsp c une idee qui peut marcher

            return new Board(newPlacedTiles, tileIndex, zonePartitions, cancelledAnimals);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        for (Area<Zone.Forest> forest : forests) {
            for (Area<Zone.River> river : rivers) {
                ZonePartitions.Builder newZonePartitions = new ZonePartitions(forest., river, zonePartitions.meadows(), zonePartitions.riverSystems());

            }
            newZonePartitions.

            return new Board(placedTiles, tileIndex, zonePartitions, );

        }
    }

    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> newCancelledAnimals = new HashSet<>(cancelledAnimals);
        newCancelledAnimals.addAll(newlyCancelledAnimals);
        return new Board(placedTiles, tileIndex, zonePartitions, newCancelledAnimals);
    }

}
