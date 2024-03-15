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

    public static Board emptyBoard() {
        return EMPTY;
    }


}
