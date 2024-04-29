package ch.epfl.chacun;

import ch.epfl.chacun.*;

import java.util.List;
import java.util.Set;

public class ChaCuNUtils2 {
    private ChaCuNUtils2() {
    }

    @SafeVarargs
    public static <Z extends Zone> Area<Z> createClosedAreaWithNoOccupants(Z... zones) {
        return new Area<>(Set.of(zones), List.of(), 0);
    }

    @SafeVarargs
    public static <Z extends Zone> Area<Z> createClosedAreaWithOccupants(List<PlayerColor> occupants, Z... zones) {
        return new Area<>(Set.of(zones), occupants, 0);
    }

    @SafeVarargs
    public static <Z extends Zone> Area<Z> createAreaWithNoOccupant(int openConnections, Z... zones) {
        return new Area<>(Set.of(zones), List.of(), openConnections);
    }

    @SafeVarargs
    public static <Z extends Zone> Area<Z> createAreaWithOccupant(int openConnections, PlayerColor occupant, Z... zones) {
        return new Area<>(Set.of(zones), List.of(occupant), openConnections);
    }

    @SafeVarargs
    public static <Z extends Zone> Area<Z> createAreaWithOccupant(int openConnections, List<PlayerColor> occupant, Z... zones) {
        return new Area<>(Set.of(zones), occupant, openConnections);
    }


    /**
     * this zone will not have any special power.
     */
    public static Zone.Meadow createMeadowZoneWithNoSpecialPower(int id) {
        return new Zone.Meadow(id, List.of(), null);
    }

    /**
     * this zone will not have any special power.
     */
    public static Zone.Meadow createMeadowZoneWithNoSpecialPower(int id, Animal... animals) {
        return new Zone.Meadow(id, List.of(animals), null);
    }

    /**
     * this zone will not have any special power.
     */
    public static Zone.Meadow createMeadowZone(int id, Zone.SpecialPower specialPower, Animal... animals) {
        return new Zone.Meadow(id, List.of(animals), specialPower);
    }

    public static Zone.Meadow createMeadowZone(int id, Animal... animals) {
        return new Zone.Meadow(id, List.of(animals), null);
    }

    public static Zone.River createRiverZone(int id) {
        return new Zone.River(id, 0, null);
    }

    public static Zone.River createRiverZone(int id, int fishCount) {
        return new Zone.River(id, fishCount, null);
    }

    public static Zone.River createRiverZone(int id, int fishCount, Zone.Lake lake) {
        return new Zone.River(id, fishCount, lake);
    }

    public static Zone.River createRiverZone(int id, int fishCount, int lakeId, int lakeFishCount) {
        return createRiverZone(id, fishCount, new Zone.Lake(lakeId, lakeFishCount, null));
    }

    public static Zone.Lake createLake(int id, int fishCount) {
        return new Zone.Lake(id, fishCount, null);
    }

    public static Zone.Lake createLake(int id, int fishCount, Zone.SpecialPower specialPower) {
        return new Zone.Lake(id, fishCount, specialPower);
    }

    /**
     * Default kind is Plain
     */
    public static Zone.Forest createForest(int id) {
        return new Zone.Forest(id, Zone.Forest.Kind.PLAIN);
    }

    public static Zone.Forest createForest(int id, Zone.Forest.Kind kind) {
        return new Zone.Forest(id, kind);
    }

    public static TileSide createMeadowTileSide(int id, Animal... animals) {
        return new TileSide.Meadow(createMeadowZone(id, animals));
    }

    public static TileSide createMeadowTileSide(int id, Zone.SpecialPower specialPower, Animal... animals) {
        return new TileSide.Meadow(createMeadowZone(id, specialPower, animals));
    }

    public static TileSide createForestTileSide(int id) {
        return new TileSide.Forest(createForest(id));
    }

    public static TileSide createForestTileSide(int id, Zone.Forest.Kind kind) {
        return new TileSide.Forest(createForest(id, kind));
    }

    public static TileSide createRiverTileSide(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) {
        return new TileSide.River(meadow1, river, meadow2);
    }

    public static Tile createTile(
            int id,
            Tile.Kind kind,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west
    ) {
        return new Tile(id, kind, north, east, south, west);
    }

    public static PlacedTile createPlacedTile(
            int id,
            PlayerColor placer,
            Pos pos,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west
    ) {
        return new PlacedTile(createTile(id, Tile.Kind.NORMAL, north, east, south, west), placer, Rotation.NONE, pos);
    }

    public static PlacedTile createPlacedTile(
            int id,
            Tile.Kind kind,
            PlayerColor placer,
            Pos pos,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west
    ) {
        return new PlacedTile(createTile(id, kind, north, east, south, west), placer, Rotation.NONE, pos);
    }

    public static PlacedTile createPlacedTile(
            int id,
            Tile.Kind kind,
            PlayerColor placer,
            Pos pos,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west,
            Occupant occupant
    ) {
        return new PlacedTile(createTile(id, kind, north, east, south, west), placer, Rotation.NONE, pos, occupant);
    }

    public static PlacedTile createPlacedTile(
            int id,
            Tile.Kind kind,
            PlayerColor placer,
            Pos pos,
            Rotation rotation,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west
    ) {
        return new PlacedTile(createTile(id, kind, north, east, south, west), placer, rotation, pos);
    }

    public static PlacedTile createPlacedTile(
            int id,
            Tile.Kind kind,
            PlayerColor placer,
            Pos pos,
            Rotation rotation,
            TileSide north,
            TileSide east,
            TileSide south,
            TileSide west,
            Occupant occupant
    ) {
        return new PlacedTile(createTile(id, kind, north, east, south, west), placer, rotation, pos, occupant);
    }
}