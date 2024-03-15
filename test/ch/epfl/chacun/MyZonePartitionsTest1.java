package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyZonePartitionsTest1 {
    private static final TileSide dummyForestSide1 = new TileSide.Forest(new Zone.Forest(765, Zone.Forest.Kind.PLAIN));
    private static final TileSide dummyForestSide2 = new TileSide.Forest(new Zone.Forest(766, Zone.Forest.Kind.PLAIN));
    private static final TileSide dummyForestSide3 = new TileSide.Forest(new Zone.Forest(767, Zone.Forest.Kind.PLAIN));
    private static final TileSide dummyForestSide4 = new TileSide.Forest(new Zone.Forest(768, Zone.Forest.Kind.PLAIN));
    private static final TileSide dummyForestSide5 = new TileSide.Forest(new Zone.Forest(769, Zone.Forest.Kind.PLAIN));
    private static final TileSide dummyForestSide6 = new TileSide.Forest(new Zone.Forest(770, Zone.Forest.Kind.PLAIN));

    private static final Zone.Meadow dummyMeadowZone1 = new Zone.Meadow(771, List.of(), null);
    private static final Zone.Meadow dummyMeadowZone2 = new Zone.Meadow(772, List.of(), null);

    @Test
    void zonePartitionsConstructorWorks() {
        assertDoesNotThrow(() -> new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>(),
                new ZonePartition<>()
        ));
    }

    @Test
    void zonePartitionsBuilderConstructorWorks() {
        assertDoesNotThrow(() -> new ZonePartitions.Builder(ZonePartitions.EMPTY));
    }

    @Test
    void addTileWorks() {
        Zone.Meadow meadow = new Zone.Meadow(123, new ArrayList<>(), null);
        Zone.Forest forest = new Zone.Forest(12, null);
        Zone.River river = new Zone.River(10, 2, null);

        TileSide meadowSide = new TileSide.Meadow(meadow);
        TileSide forestSide = new TileSide.Forest(forest);
        TileSide riverSide = new TileSide.River(meadow, river, meadow);

        Tile tile = new Tile(12, null, meadowSide, forestSide, riverSide, dummyForestSide1);
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);

        ZonePartitions result = builder.build();

        assertTrue(result.meadows().areaContaining(meadow).zones().contains(meadow));
        assertEquals(1, result.meadows().areaContaining(meadow).zones().size());

        assertTrue(result.rivers().areaContaining(river).zones().contains(river));
        assertEquals(1, result.rivers().areaContaining(river).zones().size());

        assertTrue(result.forests().areaContaining(forest).zones().contains(forest));
        assertEquals(1, result.forests().areaContaining(forest).zones().size());

    }

    @Test
    void addTileWorksWithTwoLakes(){
        Zone.Lake lake1 = new Zone.Lake(0, 5, null);
        Zone.Lake lake2 = new Zone.Lake(4, 50000, null);

        Zone.Meadow meadow = new Zone.Meadow(123, new ArrayList<>(), null);
        Zone.Forest forest = new Zone.Forest(12, null);
        Zone.River river1 = new Zone.River(10, 2, lake1 );
        Zone.River river2 = new Zone.River(1, 456, lake2 );

        TileSide meadowSide = new TileSide.Meadow(meadow);
        TileSide forestSide = new TileSide.Forest(forest);
        TileSide riverSide = new TileSide.River(meadow, river1, meadow);
        TileSide riverSide2 = new TileSide.River(meadow, river2, meadow);

        Tile tile = new Tile(12, null, meadowSide, forestSide, riverSide, riverSide2);
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);

        ZonePartitions result = builder.build();

        assertTrue(result.meadows().areaContaining(meadow).zones().contains(meadow));
        assertEquals(1, result.meadows().areaContaining(meadow).zones().size());

        assertTrue(result.forests().areaContaining(forest).zones().contains(forest));
        assertEquals(1, result.forests().areaContaining(forest).zones().size());

        assertTrue(result.riverSystems().areaContaining(river1).zones().contains(river1));
        assertTrue(result.riverSystems().areaContaining(river1).zones().contains(lake1));
        assertEquals(2, result.riverSystems().areaContaining(river1).zones().size());

        assertTrue(result.riverSystems().areaContaining(river2).zones().contains(river2));
        assertTrue(result.riverSystems().areaContaining(river2).zones().contains(lake2));
        assertEquals(2, result.riverSystems().areaContaining(river1).zones().size());
    }

    @Test
    void connectSidesWorksForRiverSides() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        Zone.Meadow meadow3 = new Zone.Meadow(8, List.of(new Animal(190, Animal.Kind.DEER)), null);
        Zone.River river2 = new Zone.River(12, 48, null);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), null);

        TileSide side1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide side2 = new TileSide.River(meadow3, river2, meadow4);

        Tile tile1 = new Tile(12345, Tile.Kind.NORMAL, side1, dummyForestSide1, dummyForestSide2, dummyForestSide3);
        Tile tile2 = new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, dummyForestSide5, side2, dummyForestSide6);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(side1, side2);

        ZonePartitions result = builder.build();

        // check that meadow1 and meadow4 are connected
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow1));
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow4));
        assertEquals(2, result.meadows().areaContaining(meadow1).zones().size());

        // check that meadow2 and meadow3 are connected
        assertTrue(result.meadows().areaContaining(meadow2).zones().contains(meadow2));
        assertTrue(result.meadows().areaContaining(meadow2).zones().contains(meadow3));
        assertEquals(2, result.meadows().areaContaining(meadow2).zones().size());

        // check that river1 and river2 are connected
        assertTrue(result.rivers().areaContaining(river1).zones().contains(river1));
        assertTrue(result.rivers().areaContaining(river1).zones().contains(river2));
        assertEquals(2, result.rivers().areaContaining(river1).zones().size());
    }

    @Test
    void connectSidesWorksForMeadowSides() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        TileSide side1 = new TileSide.Meadow(meadow1);
        TileSide side2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12345, Tile.Kind.NORMAL, side1, dummyForestSide1, dummyForestSide2, dummyForestSide3);
        Tile tile2 = new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, dummyForestSide5, side2, dummyForestSide6);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(side1, side2);

        ZonePartitions result = builder.build();

        // check that meadow1 and meadow2 are connected
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow1));
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow2));
        assertEquals(2, result.meadows().areaContaining(meadow1).zones().size());
    }

    @Test
    void connectSidesWorksForForestSides() {
        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);

        TileSide side1 = new TileSide.Forest(forest1);
        TileSide side2 = new TileSide.Forest(forest2);

        Tile tile1 = new Tile(12345, Tile.Kind.NORMAL, side1, dummyForestSide1, dummyForestSide2, dummyForestSide3);
        Tile tile2 = new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, dummyForestSide5, side2, dummyForestSide6);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(side1, side2);

        ZonePartitions result = builder.build();

        // check that forest1 and forest2 are connected
        assertTrue(result.forests().areaContaining(forest1).zones().contains(forest1));
        assertTrue(result.forests().areaContaining(forest1).zones().contains(forest2));
        assertEquals(2, result.forests().areaContaining(forest1).zones().size());
    }

    @Test
    void connectSidesWorksForEastWest() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        TileSide side1 = new TileSide.Meadow(meadow1);
        TileSide side2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12345, Tile.Kind.NORMAL, dummyForestSide1, side1, dummyForestSide2, dummyForestSide3);
        Tile tile2 = new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, dummyForestSide5, dummyForestSide6, side2);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(side1, side2);

        ZonePartitions result = builder.build();

        // check that meadow1 and meadow2 are connected
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow1));
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow2));
        assertEquals(2, result.meadows().areaContaining(meadow1).zones().size());
    }

    @Test
    void connectSidesWorksForWestEast() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        TileSide side1 = new TileSide.Meadow(meadow1);
        TileSide side2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12345, Tile.Kind.NORMAL, dummyForestSide1, dummyForestSide2, dummyForestSide3, side1);
        Tile tile2 = new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, side2, dummyForestSide5, dummyForestSide6);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(side1, side2);

        ZonePartitions result = builder.build();

        // check that meadow1 and meadow2 are connected
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow1));
        assertTrue(result.meadows().areaContaining(meadow1).zones().contains(meadow2));
        assertEquals(2, result.meadows().areaContaining(meadow1).zones().size());
    }

    @Test
    void connectSidesThrowsIfSidesAreNotOfTheSameKind() {
        Zone.Meadow meadow = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.Forest forest = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone.River river = new Zone.River(1234, 45, null);

        TileSide meadowSide = new TileSide.Meadow(meadow);
        TileSide forestSide = new TileSide.Forest(forest);
        TileSide riverSide = new TileSide.River(dummyMeadowZone1, river, dummyMeadowZone2);

        TileSide[] sides = {meadowSide, forestSide, riverSide};

        for (TileSide side1 : sides) {
            for (TileSide side2 : sides) {
                if (side1 == side2) {
                    continue;
                }

                Tile tile1 =
                        new Tile(12345, Tile.Kind.NORMAL, dummyForestSide1, dummyForestSide2, dummyForestSide3, side1);
                Tile tile2 =
                        new Tile(12346, Tile.Kind.NORMAL, dummyForestSide4, side2, dummyForestSide5, dummyForestSide6);

                ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
                builder.addTile(tile1);
                builder.addTile(tile2);

                assertThrows(IllegalArgumentException.class, () -> builder.connectSides(side1, side2));
            }
        }
    }

    @Test
    void addInitialOccupantWorksForPawns() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        Zone.Meadow meadow3 = new Zone.Meadow(8, List.of(new Animal(190, Animal.Kind.DEER)), null);
        Zone.River river2 = new Zone.River(12, 48, null);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), null);

        Zone.Forest forest = new Zone.Forest(3456, Zone.Forest.Kind.PLAIN);

        TileSide side1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide side2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide side3 = new TileSide.Forest(forest);
        TileSide side4 = new TileSide.Forest(forest);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, side3, side4);

        for (Zone zone : new Zone[]{meadow1, river1, meadow2, meadow3, river2, meadow4, forest}) {
            for (PlayerColor color : PlayerColor.values()) {
                ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
                builder.addTile(tile);

                builder.addInitialOccupant(color, Occupant.Kind.PAWN, zone);

                ZonePartitions result = builder.build();

                Area targetArea = switch (zone) {
                    case Zone.Meadow meadowZone -> result.meadows().areaContaining(meadowZone);
                    case Zone.River riverZone -> result.rivers().areaContaining(riverZone);
                    case Zone.Forest forestZone -> result.forests().areaContaining(forestZone);
                    case Zone.Lake ignored -> null; // never happens
                };

                assertEquals(List.of(color), targetArea.occupants());
            }
        }
    }

    @Test
    void addInitialOccupantWorksForHuts() {
        Zone.Lake lake = new Zone.Lake(1000, 13, null);
        Zone.River river = new Zone.River(1234, 45, lake);

        TileSide side = new TileSide.River(dummyMeadowZone1, river, dummyMeadowZone2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side, dummyForestSide1, dummyForestSide2, dummyForestSide3);

        for (Zone zone : new Zone[]{lake, river}) {
            for (PlayerColor color : PlayerColor.values()) {
                ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
                builder.addTile(tile);

                builder.addInitialOccupant(color, Occupant.Kind.HUT, zone);

                ZonePartitions result = builder.build();

                Area targetArea = switch (zone) {
                    case Zone.River riverZone -> result.riverSystems().areaContaining(riverZone);
                    case Zone.Lake lakeZone -> result.riverSystems().areaContaining(lakeZone);
                    default -> null; // never happens
                };

                assertEquals(List.of(color), targetArea.occupants());
            }
        }
    }

    @Test
    void addInitialOccupantThrowsForHutsNotInLake() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        Zone.Meadow meadow3 = new Zone.Meadow(8, List.of(new Animal(190, Animal.Kind.DEER)), null);
        Zone.River river2 = new Zone.River(12, 48, null);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), null);

        Zone.Forest forest = new Zone.Forest(3456, Zone.Forest.Kind.PLAIN);

        TileSide side1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide side2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide side3 = new TileSide.Forest(forest);
        TileSide side4 = new TileSide.Forest(forest);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, side3, side4);

        for (Zone zone : new Zone[]{meadow1, meadow2, meadow3, meadow4, forest}) {
            for (PlayerColor color : PlayerColor.values()) {
                ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
                builder.addTile(tile);

                assertThrows(IllegalArgumentException.class,
                        () -> builder.addInitialOccupant(color, Occupant.Kind.HUT, zone)
                );
            }
        }
    }

    @Test
    void addInitialOccupantThrowsForPawnsInLake() {
        Zone.Lake lake = new Zone.Lake(1000, 13, null);
        Zone.River river = new Zone.River(1234, 45, lake);

        TileSide side = new TileSide.River(dummyMeadowZone1, river, dummyMeadowZone2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side, dummyForestSide1, dummyForestSide2, dummyForestSide3);

        for (PlayerColor color : PlayerColor.values()) {
            ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
            builder.addTile(tile);

            assertThrows(IllegalArgumentException.class,
                    () -> builder.addInitialOccupant(color, Occupant.Kind.PAWN, lake)
            );
        }
    }

    @Test
    void removePawnWorks() {
        Zone.Meadow meadow1 = new Zone.Meadow(0, List.of(new Animal(100, Animal.Kind.AUROCHS)), null);
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.Meadow meadow2 = new Zone.Meadow(3, List.of(), null);

        Zone.Meadow meadow3 = new Zone.Meadow(8, List.of(new Animal(190, Animal.Kind.DEER)), null);
        Zone.River river2 = new Zone.River(12, 48, null);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), null);

        Zone.Forest forest = new Zone.Forest(3456, Zone.Forest.Kind.PLAIN);

        TileSide side1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide side2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide side3 = new TileSide.Forest(forest);
        TileSide side4 = new TileSide.Forest(forest);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, side3, side4);

        for (Zone zone : new Zone[]{meadow1, river1, meadow2, meadow3, river2, meadow4, forest}) {
            for (PlayerColor color : PlayerColor.values()) {
                ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
                builder.addTile(tile);

                builder.addInitialOccupant(color, Occupant.Kind.PAWN, zone);
                builder.removePawn(color, zone);

                ZonePartitions result = builder.build();

                Area targetArea = switch (zone) {
                    case Zone.Meadow meadowZone -> result.meadows().areaContaining(meadowZone);
                    case Zone.River riverZone -> result.rivers().areaContaining(riverZone);
                    case Zone.Forest forestZone -> result.forests().areaContaining(forestZone);
                    case Zone.Lake ignored -> null; // never happens
                };

                assertTrue(targetArea.occupants().isEmpty());
            }
        }
    }

    @Test
    void removePawnThrowsForLake() {
        Zone.Lake lake = new Zone.Lake(1000, 13, null);
        Zone.River river = new Zone.River(1234, 45, lake);

        TileSide side = new TileSide.River(dummyMeadowZone1, river, dummyMeadowZone2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side, dummyForestSide1, dummyForestSide2, dummyForestSide3);

        for (PlayerColor color : PlayerColor.values()) {
            ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
            builder.addTile(tile);

            builder.addInitialOccupant(color, Occupant.Kind.HUT, lake);
            assertThrows(
                    IllegalArgumentException.class, () -> builder.removePawn(color, lake));
        }
    }

    @Test
    void clearGatherersWorks() {
        Zone.Forest forest1 = new Zone.Forest(3456, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(3457, Zone.Forest.Kind.PLAIN);

        TileSide side1 = new TileSide.Forest(forest1);
        TileSide side2 = new TileSide.Forest(forest2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, dummyForestSide1, dummyForestSide2);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest1);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, forest2);

        // get the forest areas
        Area<Zone.Forest> forestArea1 = builder.build().forests().areaContaining(forest1);

        builder.clearGatherers(forestArea1);

        ZonePartitions result = builder.build();

        assertTrue(result.forests().areaContaining(forest1).occupants().isEmpty());
        assertEquals(List.of(PlayerColor.RED), result.forests().areaContaining(forest2).occupants());
    }

    @Test
    void clearGatherersWorksWithNoGatherers() {
        Zone.Forest forest = new Zone.Forest(3456, Zone.Forest.Kind.PLAIN);

        TileSide side = new TileSide.Forest(forest);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side, dummyForestSide1, dummyForestSide2, dummyForestSide3);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);

        // get the forest area
        Area<Zone.Forest> forestArea = builder.build().forests().areaContaining(forest);

        builder.clearGatherers(forestArea);

        ZonePartitions result = builder.build();

        assertTrue(result.forests().areaContaining(forest).occupants().isEmpty());
    }

    @Test
    void clearFishersWorks() {
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.River river2 = new Zone.River(1234, 1851, null);

        TileSide side1 = new TileSide.River(dummyMeadowZone1, river1, dummyMeadowZone2);
        TileSide side2 = new TileSide.River(dummyMeadowZone1, river2, dummyMeadowZone2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, dummyForestSide1, dummyForestSide2);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);
        builder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, river1);
        builder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, river2);

        // get the river area
        Area<Zone.River> riverArea = builder.build().rivers().areaContaining(river1);

        builder.clearFishers(riverArea);

        ZonePartitions result = builder.build();

        assertTrue(result.rivers().areaContaining(river1).occupants().isEmpty());
        assertEquals(List.of(PlayerColor.BLUE), result.rivers().areaContaining(river2).occupants());
    }

    @Test
    void clearFishersWorksWithNoFishers() {
        Zone.River river1 = new Zone.River(1234, 45, null);
        Zone.River river2 = new Zone.River(1234, 1851, null);

        TileSide side1 = new TileSide.River(dummyMeadowZone1, river1, dummyMeadowZone2);
        TileSide side2 = new TileSide.River(dummyMeadowZone1, river2, dummyMeadowZone2);

        Tile tile = new Tile(12345, Tile.Kind.NORMAL, side1, side2, dummyForestSide1, dummyForestSide2);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile);

        // get the river area
        Area<Zone.River> riverArea = builder.build().rivers().areaContaining(river1);

        builder.clearFishers(riverArea);

        ZonePartitions result = builder.build();

        assertTrue(result.rivers().areaContaining(river1).occupants().isEmpty());
        assertTrue(result.rivers().areaContaining(river2).occupants().isEmpty());
    }

    @Test
    void buildWorksWithNoChanges() {
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        ZonePartitions result = builder.build();

        assertTrue(result.forests().areas().isEmpty());
        assertTrue(result.meadows().areas().isEmpty());
        assertTrue(result.rivers().areas().isEmpty());
        assertTrue(result.riverSystems().areas().isEmpty());
    }
}