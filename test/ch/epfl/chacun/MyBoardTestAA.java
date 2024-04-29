package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyBoardTestAA {
    private TileSide.Forest dummyForestSide(int tileId, int localId) {
        return new TileSide.Forest(new Zone.Forest(tileId * 10 + localId, Zone.Forest.Kind.PLAIN));
    }

    private TileSide.Meadow dummyMeadowSide(int tileId, int localId) {
        return new TileSide.Meadow(new Zone.Meadow(tileId * 10 + localId, List.of(), null));
    }

    private TileSide.River dummyRiverSide(int tileId, int leftMeadowLocalId, int riverLocalId, int rightMeadowLocalId) {
        return new TileSide.River(new Zone.Meadow(tileId * 10 + leftMeadowLocalId, List.of(), null), new Zone.River(tileId * 10 + riverLocalId, 0, null), new Zone.Meadow(tileId * 10 + rightMeadowLocalId, List.of(), null));
    }

    @Test
    void tileAtWorksWithTwoTiles() {
        Pos pos1 = new Pos(1, 2);
        Tile tile1 =
                new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos1, null);

        Pos pos2 = new Pos(2, 2);
        Tile tile2 =
                new Tile(11, Tile.Kind.MENHIR, dummyForestSide(11, 0), dummyForestSide(11, 1), dummyForestSide(11, 2),
                         dummyForestSide(11, 3)
                );
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.LEFT, pos2, null);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        assertEquals(placedTile1, board.tileAt(pos1));
    }

    @Test
    void tileAtWorksIfTileOutside() {
        Pos pos = new Pos(-12, 12);
        Tile tile1 =
                new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos, null);

        Board board = Board.EMPTY.withNewTile(placedTile1);

        assertNull(board.tileAt(new Pos(13, 1)));
    }

    @Test
    void tileAtWorksIfNoTileAtPosition() {
        Pos pos = new Pos(-12, 12);
        Tile tile1 =
                new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos, null);

        Board board = Board.EMPTY.withNewTile(placedTile1);

        assertNull(board.tileAt(new Pos(0, 1)));
    }

    @Test
    void tileAtWorksForEveryPosition() {
        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                assertNull(Board.EMPTY.tileAt(new Pos(x, y)));
            }
        }
    }

    @Test
    void tileWithIdWorks() {
        Pos pos1 = new Pos(1, 2);
        Tile tile1 =
                new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos1, null);

        Pos pos2 = new Pos(2, 2);
        Tile tile2 =
                new Tile(11, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.LEFT, pos2, null);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        assertEquals(placedTile1, board.tileWithId(12));
    }

    @Test
    void tileWithIdThrowsIfNoTileWithGivenIdIsOnBoard() {
        Pos pos1 = new Pos(1, 2);
        Tile tile1 =
                new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), dummyForestSide(12, 2),
                         dummyForestSide(12, 3)
                );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos1, null);

        Pos pos2 = new Pos(2, 2);
        Tile tile2 =
                new Tile(11, Tile.Kind.NORMAL, dummyForestSide(11, 0), dummyForestSide(11, 1), dummyForestSide(11, 2),
                         dummyForestSide(11, 3)
                );
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.LEFT, pos2, null);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        assertThrows(IllegalArgumentException.class, () -> board.tileWithId(56546));
    }

    @Test
    void cancelledAnimalsWorks() {
        Animal deer1 = new Animal(1, Animal.Kind.DEER);
        Animal deer2 = new Animal(2, Animal.Kind.DEER);
        Animal aurochs1 = new Animal(5, Animal.Kind.AUROCHS);
        Animal aurochs2 = new Animal(6, Animal.Kind.AUROCHS);
        Animal mammoth1 = new Animal(9, Animal.Kind.MAMMOTH);
        Animal mammoth2 = new Animal(10, Animal.Kind.MAMMOTH);
        Animal tiger1 = new Animal(13, Animal.Kind.TIGER);
        Animal tiger2 = new Animal(14, Animal.Kind.TIGER);

        Set<Animal> animals = Set.of(deer1, deer2, aurochs1, aurochs2, mammoth1, mammoth2, tiger1, tiger2);

        Board board = Board.EMPTY.withMoreCancelledAnimals(animals);
        assertEquals(animals, board.cancelledAnimals());
    }

    @Test
    void cancelledAnimalsIsImmutableBeforeAddingAnimals() {
        assertThrows(UnsupportedOperationException.class, () -> Board.EMPTY.cancelledAnimals().add(new Animal(88, Animal.Kind.DEER)));
    }

    @Test
    void cancelledAnimalsIsImmutableAfterAddingAnimals() {
        Animal deer1 = new Animal(1, Animal.Kind.DEER);
        Animal deer2 = new Animal(2, Animal.Kind.DEER);
        Animal aurochs1 = new Animal(5, Animal.Kind.AUROCHS);
        Animal aurochs2 = new Animal(6, Animal.Kind.AUROCHS);
        Animal mammoth1 = new Animal(9, Animal.Kind.MAMMOTH);
        Animal mammoth2 = new Animal(10, Animal.Kind.MAMMOTH);
        Animal tiger1 = new Animal(13, Animal.Kind.TIGER);
        Animal tiger2 = new Animal(14, Animal.Kind.TIGER);

        Set<Animal> animals = Set.of(deer1, deer2, aurochs1, aurochs2, mammoth1, mammoth2, tiger1, tiger2);

        Board board = Board.EMPTY.withMoreCancelledAnimals(animals);
        assertThrows(UnsupportedOperationException.class, () -> board.cancelledAnimals().add(new Animal(88, Animal.Kind.DEER)));
    }

    @Test
    void occupantsWorks() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);
        TileSide riverSide1 = new TileSide.River(meadow1, new Zone.River(122, 322632, null), meadow2);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide1, meadowSide2);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Meadow meadow3 = new Zone.Meadow(133, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(131, List.of(), null);

        TileSide meadowSide3 = new TileSide.Meadow(meadow3);
        TileSide meadowSide4 = new TileSide.Meadow(meadow4);
        TileSide riverSide2 = new TileSide.River(meadow1, new Zone.River(130, 3232, null), meadow2);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, riverSide2, meadowSide4, dummyForestSide(13, 2), meadowSide3);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 1), null);

        // creation of the board
        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 133);
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, 122);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withOccupant(occupant1).withOccupant(occupant2);

        assertEquals(Set.of(occupant1, occupant2), board.occupants());
    }

    @Test
    void forestAreaWorksForOneTileWithOneForestZone() {
        // creation of the tile
        Zone.Forest forest = new Zone.Forest(122, Zone.Forest.Kind.PLAIN);

        TileSide forestSide = new TileSide.Forest(forest);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 1), forestSide,
                             dummyMeadowSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertEquals(new Area<>(Set.of(forest), List.of(), 1), board.forestArea(forest));
    }

    @Test
    void forestAreaWorksWithForestsOnThreeTiles() {
        // creation of first tile
        Zone.Forest forest1 = new Zone.Forest(122, Zone.Forest.Kind.PLAIN);

        TileSide forestSide1 = new TileSide.Forest(forest1);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 1), forestSide1,
                              dummyMeadowSide(12, 3)
        );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Forest forest2 = new Zone.Forest(130, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(130, Zone.Forest.Kind.PLAIN);

        TileSide forestSide2 = new TileSide.Forest(forest2);
        TileSide forestSide3 = new TileSide.Forest(forest3);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, forestSide2, forestSide3, dummyMeadowSide(13, 2),
                              dummyMeadowSide(13, 3)
        );
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        // creation of third tile
        Zone.Forest forest4 = new Zone.Forest(140, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest5 = new Zone.Forest(140, Zone.Forest.Kind.PLAIN);

        TileSide forestSide4 = new TileSide.Forest(forest4);
        TileSide forestSide5 = new TileSide.Forest(forest5);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, forestSide4, dummyMeadowSide(14, 1), dummyMeadowSide(14, 2),
                              forestSide5
        );
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 3), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);

        Area<Zone.Forest> expectedArea = new Area<>(Set.of(forest1, forest2, forest4), List.of(), 1);

        assertEquals(expectedArea, board.forestArea(forest1));
        assertEquals(expectedArea, board.forestArea(forest2));
        assertEquals(expectedArea, board.forestArea(forest3));
        assertEquals(expectedArea, board.forestArea(forest4));
        assertEquals(expectedArea, board.forestArea(forest5));
    }

    @Test
    void forestAreaThrowsIfZoneIsNotOnTheBoard() {
        // creation of first tile
        Zone.Forest forest = new Zone.Forest(122, Zone.Forest.Kind.PLAIN);

        TileSide forestSide = new TileSide.Forest(forest);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 1), forestSide,
                             dummyMeadowSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertThrows(IllegalArgumentException.class, () -> board.forestArea(new Zone.Forest(121, Zone.Forest.Kind.PLAIN)));
    }

    @Test
    void meadowAreaWorksForOneTileWithOneMeadowZone() {
        // creation of the tile
        Zone.Meadow meadow = new Zone.Meadow(122, List.of(), null);

        TileSide meadowSide = new TileSide.Meadow(meadow);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), meadowSide,
                             dummyForestSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertEquals(new Area<>(Set.of(meadow), List.of(), 1), board.meadowArea(meadow));
    }

    @Test
    void meadowAreaWorksWithMeadowsOnThreeTiles() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(122, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), meadowSide1,
                              dummyMeadowSide(12, 3)
        );
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Meadow meadow2 = new Zone.Meadow(130, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(130, List.of(), null);

        TileSide meadowSide2 = new TileSide.Meadow(meadow2);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, meadowSide2, meadowSide3, dummyForestSide(13, 2),
                              dummyMeadowSide(13, 3)
        );
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        // creation of third tile
        Zone.Meadow meadow4 = new Zone.Meadow(140, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(140, List.of(), null);

        TileSide meadowSide4 = new TileSide.Meadow(meadow4);
        TileSide meadowSide5 = new TileSide.Meadow(meadow5);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, meadowSide4, dummyForestSide(14, 1), dummyMeadowSide(14, 2),
                              meadowSide5
        );
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 3), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);

        Area<Zone.Meadow> expectedArea = new Area<>(Set.of(meadow1, meadow2, meadow4), List.of(), 1);

        assertEquals(expectedArea, board.meadowArea(meadow1));
        assertEquals(expectedArea, board.meadowArea(meadow2));
        assertEquals(expectedArea, board.meadowArea(meadow3));
        assertEquals(expectedArea, board.meadowArea(meadow4));
        assertEquals(expectedArea, board.meadowArea(meadow5));
    }

    @Test
    void meadowAreaThrowsIfZoneIsNotOnTheBoard() {
        // creation of first tile
        Zone.Meadow meadow = new Zone.Meadow(122, List.of(), null);

        TileSide meadowSide = new TileSide.Meadow(meadow);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), meadowSide,
                             dummyForestSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertThrows(IllegalArgumentException.class, () -> board.meadowArea(new Zone.Meadow(121, List.of(), null)));
    }

    @Test
    void riverAreaWorksForOneTileWithOneRiverZone() {
        // creation of the tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.River river = new Zone.River(122, 87, null);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide riverSide = new TileSide.River(meadow1, river, meadow2);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide, meadowSide2);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertEquals(new Area<>(Set.of(river), List.of(), 1), board.riverArea(river));
    }

    @Test
    void riverAreaWorksWithRiversOnThreeTiles() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.River river1 = new Zone.River(122, 3, null);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide riverSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide1, meadowSide2);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Meadow meadow3 = new Zone.Meadow(130, List.of(), null);
        Zone.River river2 = new Zone.River(131, 4, null);
        Zone.Meadow meadow4 = new Zone.Meadow(132, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(133, List.of(), null);

        TileSide riverSide2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide riverSide3 = new TileSide.River(meadow4, river2, meadow5);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, riverSide2, riverSide3, dummyForestSide(13, 5), meadowSide3);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        // creation of third tile
        Zone.Meadow meadow6 = new Zone.Meadow(140, List.of(), null);
        Zone.Lake lake = new Zone.Lake(148, 8, null);
        Zone.River river3 = new Zone.River(141, 1, lake);
        Zone.Meadow meadow7 = new Zone.Meadow(142, List.of(), null);
        Zone.Meadow meadow8 = new Zone.Meadow(144, List.of(), null);
        Zone.River river4 = new Zone.River(145, 2, lake);

        TileSide riverSide4 = new TileSide.River(meadow6, river3, meadow7);
        TileSide meadowSide4 = new TileSide.Meadow(meadow7);
        TileSide riverSide5 = new TileSide.River(meadow8, river4, meadow6);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, riverSide4, meadowSide4, dummyMeadowSide(14, 3), riverSide5);
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 3), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);

        Area<Zone.River> expectedArea1 = new Area<>(Set.of(river1, river2, river4), List.of(), 0);
        Area<Zone.River> expectedArea2 = new Area<>(Set.of(river3), List.of(), 1);

        assertEquals(expectedArea1, board.riverArea(river1));
        assertEquals(expectedArea1, board.riverArea(river2));
        assertEquals(expectedArea1, board.riverArea(river4));
        assertEquals(expectedArea2, board.riverArea(river3));
    }

    @Test
    void riverAreaThrowsIfZoneIsNotOnTheBoard() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(120, List.of(), null);
        Zone.River river = new Zone.River(121, 2, null);
        Zone.Meadow meadow2 = new Zone.Meadow(122, List.of(), null);

        TileSide riverSide = new TileSide.River(meadow1, river, meadow2);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), riverSide,
                             dummyForestSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertThrows(IllegalArgumentException.class, () -> board.riverArea(new Zone.River(121, 8, null)));
    }

    @Test
    void riverSystemAreaWorksForOneTileWithOneRiverSystem() {
        // creation of the tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.Lake lake = new Zone.Lake(128, 3, null);
        Zone.River river = new Zone.River(122, 87, lake);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide riverSide = new TileSide.River(meadow1, river, meadow2);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide, meadowSide2);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        Area<Zone.Water> expectedArea = new Area<>(Set.of(river, lake), List.of(), 1);

        assertEquals(expectedArea, board.riverSystemArea(river));
        assertEquals(expectedArea, board.riverSystemArea(lake));
    }

    @Test
    void riverSystemAreaWorksWithRiverSystemsOnThreeTiles() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.River river1 = new Zone.River(122, 3, null);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide riverSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide1, meadowSide2);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Meadow meadow3 = new Zone.Meadow(130, List.of(), null);
        Zone.River river2 = new Zone.River(131, 4, null);
        Zone.Meadow meadow4 = new Zone.Meadow(132, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(133, List.of(), null);

        TileSide riverSide2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide riverSide3 = new TileSide.River(meadow4, river2, meadow5);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, riverSide2, riverSide3, dummyForestSide(13, 5), meadowSide3);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        // creation of third tile
        Zone.Meadow meadow6 = new Zone.Meadow(140, List.of(), null);
        Zone.Lake lake = new Zone.Lake(148, 8, null);
        Zone.River river3 = new Zone.River(141, 1, lake);
        Zone.Meadow meadow7 = new Zone.Meadow(142, List.of(), null);
        Zone.Meadow meadow8 = new Zone.Meadow(144, List.of(), null);
        Zone.River river4 = new Zone.River(145, 2, lake);

        TileSide riverSide4 = new TileSide.River(meadow6, river3, meadow7);
        TileSide meadowSide4 = new TileSide.Meadow(meadow7);
        TileSide riverSide5 = new TileSide.River(meadow8, river4, meadow6);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, riverSide4, meadowSide4, dummyMeadowSide(14, 3), riverSide5);
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 3), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);

        Area<Zone.Water> expectedArea = new Area<>(Set.of(river1, river2, river3, river4, lake), List.of(), 1);

        assertEquals(expectedArea, board.riverSystemArea(river1));
        assertEquals(expectedArea, board.riverSystemArea(river2));
        assertEquals(expectedArea, board.riverSystemArea(river3));
        assertEquals(expectedArea, board.riverSystemArea(river4));
        assertEquals(expectedArea, board.riverSystemArea(lake));
    }

    @Test
    void riverSystemAreaThrowsIfZoneIsNotOnTheBoard() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(120, List.of(), null);
        Zone.Lake lake = new Zone.Lake(128, 3, null);
        Zone.River river = new Zone.River(121, 2, lake);
        Zone.Meadow meadow2 = new Zone.Meadow(122, List.of(), null);

        TileSide riverSide = new TileSide.River(meadow1, river, meadow2);

        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 1), riverSide,
                             dummyForestSide(12, 3)
        );
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile);

        assertThrows(IllegalArgumentException.class, () -> board.riverSystemArea(new Zone.River(121, 8, null)));
        assertThrows(IllegalArgumentException.class, () -> board.riverSystemArea(new Zone.Lake(129, 8, null)));
    }

    @Test
    void meadowAreasWorks() {
        Zone.Meadow meadow1 = new Zone.Meadow(120, List.of(), null);
        TileSide meadowSide1 = new TileSide.Meadow(meadow1);

        Zone.Meadow meadow2 = new Zone.Meadow(122, List.of(), null);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Zone.Meadow meadow3 = new Zone.Meadow(130, List.of(), null);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);

        Zone.River river1 = new Zone.River(121, 21, null);
        TileSide riverSide1 = new TileSide.River(meadow1, river1, meadow2);

        Zone.Forest forest1 = new Zone.Forest(131, Zone.Forest.Kind.PLAIN);
        TileSide forestSide1 = new TileSide.Forest(forest1);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, meadowSide1, riverSide1, meadowSide2, riverSide1);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, meadowSide3, meadowSide3, forestSide1, forestSide1);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        Area<Zone.Meadow> area1 = new Area<>(Set.of(meadow1), List.of(), 3);
        Area<Zone.Meadow> area2 = new Area<>(Set.of(meadow2, meadow3), List.of(), 3);

        assertEquals(Set.of(area1, area2), board.meadowAreas());
    }

    @Test
    void riverSystemAreasWorks() {
        // creation of first tile
        Zone.Meadow meadow1 = new Zone.Meadow(121, List.of(), null);
        Zone.River river1 = new Zone.River(122, 3, null);
        Zone.Meadow meadow2 = new Zone.Meadow(123, List.of(), null);

        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide riverSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), meadowSide1, riverSide1, meadowSide2);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2), null);

        // creation of second tile
        Zone.Meadow meadow3 = new Zone.Meadow(130, List.of(), null);
        Zone.River river2 = new Zone.River(131, 4, null);
        Zone.Meadow meadow4 = new Zone.Meadow(132, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(133, List.of(), null);

        TileSide riverSide2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide riverSide3 = new TileSide.River(meadow4, river2, meadow5);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, riverSide2, riverSide3, dummyForestSide(13, 5), meadowSide3);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 3), null);

        // creation of third tile
        Zone.Meadow meadow6 = new Zone.Meadow(140, List.of(), null);
        Zone.Lake lake = new Zone.Lake(148, 8, null);
        Zone.River river3 = new Zone.River(141, 1, lake);
        Zone.Meadow meadow7 = new Zone.Meadow(142, List.of(), null);
        Zone.Meadow meadow8 = new Zone.Meadow(144, List.of(), null);
        Zone.River river4 = new Zone.River(145, 2, lake);

        TileSide riverSide4 = new TileSide.River(meadow6, river3, meadow7);
        TileSide meadowSide4 = new TileSide.Meadow(meadow7);
        TileSide riverSide5 = new TileSide.River(meadow8, river4, meadow6);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, riverSide4, meadowSide4, dummyMeadowSide(14, 3), riverSide5);
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 3), null);

        // creation of fourth tile
        Zone.Meadow meadow9 = new Zone.Meadow(150, List.of(), null);
        Zone.River river5 = new Zone.River(151, 1, null);
        Zone.Meadow meadow10 = new Zone.Meadow(152, List.of(), null);
        Zone.Meadow meadow11 = new Zone.Meadow(153, List.of(), null);
        Zone.River river6 = new Zone.River(154, 1, null);

        TileSide riverSide6 = new TileSide.River(meadow9, river5, meadow10);
        TileSide riverSide7 = new TileSide.River(meadow10, river5, meadow11);
        TileSide riverSide8 = new TileSide.River(meadow11, river6, meadow9);
        TileSide meadowSide5 = new TileSide.Meadow(meadow9);

        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, riverSide6, riverSide7, riverSide8, meadowSide5);
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.BLUE, Rotation.NONE, new Pos(2, 2));

        // creation of the board
        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3).withNewTile(placedTile4);

        Area<Zone.Water> expectedArea1 = new Area<>(Set.of(river1, river2, river3, river4, river6, lake), List.of(), 0);
        Area<Zone.Water> expectedArea2 = new Area<>(Set.of(river5), List.of(), 2);

        assertEquals(Set.of(expectedArea1, expectedArea2), board.riverSystemAreas());
    }

    @Test
    void adjacentMeadowGivesCorrectArea() {
        // Tiles: 12 13 14 15
        //        16 17 19 10

        // animals
        Animal deer1200 = new Animal(1200, Animal.Kind.DEER);
        Animal mammoth1300 = new Animal(1300, Animal.Kind.MAMMOTH);
        Animal deer1520 = new Animal(1520, Animal.Kind.DEER);
        Animal deer1620 = new Animal(1620, Animal.Kind.DEER);
        Animal deer1700 = new Animal(1700, Animal.Kind.DEER);
        Animal tiger1720 = new Animal(1720, Animal.Kind.TIGER);
        Animal aurochs1800 = new Animal(1800, Animal.Kind.AUROCHS);

        // zones
        Zone.Meadow meadow120 = new Zone.Meadow(120, List.of(deer1200), null);

        Zone.Meadow meadow130 = new Zone.Meadow(130, List.of(mammoth1300), null);

        Zone.Forest forest140 = new Zone.Forest(140, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow141 = new Zone.Meadow(141, List.of(), Zone.SpecialPower.PIT_TRAP);

        Zone.Meadow meadow150 = new Zone.Meadow(150, List.of(), null);
        Zone.Forest forest151 = new Zone.Forest(151, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow152 = new Zone.Meadow(152, List.of(deer1520), null);

        Zone.Meadow meadow160 = new Zone.Meadow(160, List.of(), null);
        Zone.River river161 = new Zone.River(161, 4, null);
        Zone.Meadow meadow162 = new Zone.Meadow(162, List.of(deer1620), null);
        Zone.Forest forest163 = new Zone.Forest(163, Zone.Forest.Kind.PLAIN);

        Zone.Meadow meadow170 = new Zone.Meadow(170, List.of(deer1700), null);
        Zone.River river171 = new Zone.River(171, 3, null);
        Zone.Meadow meadow172 = new Zone.Meadow(172, List.of(tiger1720), null);

        Zone.Meadow meadow180 = new Zone.Meadow(180, List.of(aurochs1800), null);
        Zone.Forest forest181 = new Zone.Forest(181, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow182 = new Zone.Meadow(182, List.of(), null);
        Zone.Lake lake188 = new Zone.Lake(188, 1, null);
        Zone.River river183 = new Zone.River(183, 2, lake188);

        Zone.Forest forest190 = new Zone.Forest(190, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow191 = new Zone.Meadow(191, List.of(), null);
        Zone.Meadow meadow192 = new Zone.Meadow(192, List.of(), null);

        // tile sides
        TileSide northSide12 = new TileSide.Meadow(meadow120);
        TileSide eastSide12 = new TileSide.Meadow(meadow120);
        TileSide southSide12 = new TileSide.Meadow(meadow120);
        TileSide westSide12 = new TileSide.Meadow(meadow120);

        TileSide northSide13 = new TileSide.Meadow(meadow130);
        TileSide eastSide13 = new TileSide.Meadow(meadow130);
        TileSide southSide13 = new TileSide.Meadow(meadow130);
        TileSide westSide13 = new TileSide.Meadow(meadow130);

        TileSide northSide14 = new TileSide.Forest(forest140);
        TileSide eastSide14 = new TileSide.Meadow(meadow141);
        TileSide southSide14 = new TileSide.Meadow(meadow141);
        TileSide westSide14 = new TileSide.Meadow(meadow141);

        TileSide northSide15 = new TileSide.Meadow(meadow150);
        TileSide eastSide15 = new TileSide.Forest(forest151);
        TileSide southSide15 = new TileSide.Meadow(meadow152);
        TileSide westSide15 = new TileSide.Forest(forest151);

        TileSide northSide16 = new TileSide.Meadow(meadow160);
        TileSide eastSide16 = new TileSide.River(meadow160, river161, meadow162);
        TileSide southSide16 = new TileSide.River(meadow162, river161, meadow160);
        TileSide westSide16 = new TileSide.Forest(forest163);

        TileSide northSide17 = new TileSide.Meadow(meadow170);
        TileSide eastSide17 = new TileSide.River(meadow170, river171, meadow172);
        TileSide southSide17 = new TileSide.Meadow(meadow172);
        TileSide westSide17 = new TileSide.River(meadow172, river171, meadow170);

        TileSide northSide18 = new TileSide.Meadow(meadow180);
        TileSide eastSide18 = new TileSide.Forest(forest181);
        TileSide southSide18 = new TileSide.Forest(forest181);
        TileSide westSide18 = new TileSide.River(meadow182, river183, meadow180);

        TileSide northSide19 = new TileSide.Forest(forest190);
        TileSide eastSide19 = new TileSide.Forest(forest190);
        TileSide southSide19 = new TileSide.Meadow(meadow191);
        TileSide westSide19 = new TileSide.Meadow(meadow192);

        // tiles (not rotated)
        Tile tile12 = new Tile(12, Tile.Kind.NORMAL, northSide12, eastSide12, southSide12, westSide12);
        Tile tile13 = new Tile(13, Tile.Kind.NORMAL, northSide13, eastSide13, southSide13, westSide13);
        Tile tile14 = new Tile(14, Tile.Kind.NORMAL, northSide14, eastSide14, southSide14, westSide14);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, northSide15, eastSide15, southSide15, westSide15);
        Tile tile16 = new Tile(16, Tile.Kind.NORMAL, northSide16, eastSide16, southSide16, westSide16);
        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, northSide17, eastSide17, southSide17, westSide17);
        Tile tile18 = new Tile(18, Tile.Kind.NORMAL, northSide18, eastSide18, southSide18, westSide18);
        Tile tile19 = new Tile(19, Tile.Kind.NORMAL, northSide19, eastSide19, southSide19, westSide19);

        // placed tiles (rotated)
        PlacedTile placedTile12 = new PlacedTile(tile12, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile13 = new PlacedTile(tile13, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile14 = new PlacedTile(tile14, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile15 = new PlacedTile(tile15, PlayerColor.GREEN, Rotation.RIGHT, new Pos(3, 0));
        PlacedTile placedTile16 = new PlacedTile(tile16, PlayerColor.YELLOW, Rotation.NONE, new Pos(0, 1));
        PlacedTile placedTile17 = new PlacedTile(tile17, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 1));
        PlacedTile placedTile18 = new PlacedTile(tile18, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 1));
        PlacedTile placedTile19 = new PlacedTile(tile19, PlayerColor.BLUE, Rotation.LEFT, new Pos(3, 1));

        // occupants
        Occupant pawn120 = new Occupant(Occupant.Kind.PAWN, 120);
        Occupant pawn152 = new Occupant(Occupant.Kind.PAWN, 152);
        Occupant pawn162 = new Occupant(Occupant.Kind.PAWN, 162);
        Occupant pawn191 = new Occupant(Occupant.Kind.PAWN, 191);

        // creation of the board
        Board board = Board.EMPTY
                .withNewTile(placedTile15)
                .withOccupant(pawn152)
                .withNewTile(placedTile19)
                .withOccupant(pawn191)
                .withNewTile(placedTile18)
                .withNewTile(placedTile17)
                .withNewTile(placedTile16)
                .withOccupant(pawn162)
                .withNewTile(placedTile12)
                .withOccupant(pawn120)
                .withNewTile(placedTile13)
                .withNewTile(placedTile14);

        Area<Zone.Meadow> expectedAdjacentMeadow = new Area<>(
                Set.of(meadow130, meadow141, meadow152, meadow170, meadow180),
                List.of(PlayerColor.RED, PlayerColor.GREEN),
                0
        );

        assertEquals(expectedAdjacentMeadow, board.adjacentMeadow(new Pos(2, 0), meadow141));
    }

    @Test
    void adjacentMeadowWorksInEveryPosition() {
        Zone.Meadow meadow = new Zone.Meadow(230, List.of(), null);
        Tile tile = new Tile(23, Tile.Kind.NORMAL, dummyMeadowSide(23, 0), dummyMeadowSide(23, 0), dummyMeadowSide(23, 0), dummyMeadowSide(23, 0));

        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                Pos pos = new Pos(x, y);
                PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, pos);
                Board board = Board.EMPTY.withNewTile(placedTile);
                assertDoesNotThrow(() -> board.adjacentMeadow(pos, meadow));
            }
        }
    }

    @Test
    void occupantCountWorks() {
        // Tiles: 12 13 14 15
        //        16 17 19 10

        // zones
        Zone.Meadow meadow120 = new Zone.Meadow(120, List.of(), null);

        Zone.Meadow meadow130 = new Zone.Meadow(130, List.of(), null);

        Zone.Forest forest140 = new Zone.Forest(140, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow141 = new Zone.Meadow(141, List.of(), Zone.SpecialPower.PIT_TRAP);

        Zone.Meadow meadow150 = new Zone.Meadow(150, List.of(), null);
        Zone.Forest forest151 = new Zone.Forest(151, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow152 = new Zone.Meadow(152, List.of(), null);

        Zone.Meadow meadow160 = new Zone.Meadow(160, List.of(), null);
        Zone.River river161 = new Zone.River(161, 4, null);
        Zone.Meadow meadow162 = new Zone.Meadow(162, List.of(), null);
        Zone.Forest forest163 = new Zone.Forest(163, Zone.Forest.Kind.PLAIN);

        Zone.Meadow meadow170 = new Zone.Meadow(170, List.of(), null);
        Zone.River river171 = new Zone.River(171, 3, null);
        Zone.Meadow meadow172 = new Zone.Meadow(172, List.of(), null);

        Zone.Meadow meadow180 = new Zone.Meadow(180, List.of(), null);
        Zone.Forest forest181 = new Zone.Forest(181, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow182 = new Zone.Meadow(182, List.of(), null);
        Zone.Lake lake188 = new Zone.Lake(188, 1, null);
        Zone.River river183 = new Zone.River(183, 2, lake188);

        Zone.Forest forest190 = new Zone.Forest(190, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow191 = new Zone.Meadow(191, List.of(), null);
        Zone.Meadow meadow192 = new Zone.Meadow(192, List.of(), null);

        // tile sides
        TileSide northSide12 = new TileSide.Meadow(meadow120);
        TileSide eastSide12 = new TileSide.Meadow(meadow120);
        TileSide southSide12 = new TileSide.Meadow(meadow120);
        TileSide westSide12 = new TileSide.Meadow(meadow120);

        TileSide northSide13 = new TileSide.Meadow(meadow130);
        TileSide eastSide13 = new TileSide.Meadow(meadow130);
        TileSide southSide13 = new TileSide.Meadow(meadow130);
        TileSide westSide13 = new TileSide.Meadow(meadow130);

        TileSide northSide14 = new TileSide.Forest(forest140);
        TileSide eastSide14 = new TileSide.Meadow(meadow141);
        TileSide southSide14 = new TileSide.Meadow(meadow141);
        TileSide westSide14 = new TileSide.Meadow(meadow141);

        TileSide northSide15 = new TileSide.Meadow(meadow150);
        TileSide eastSide15 = new TileSide.Forest(forest151);
        TileSide southSide15 = new TileSide.Meadow(meadow152);
        TileSide westSide15 = new TileSide.Forest(forest151);

        TileSide northSide16 = new TileSide.Meadow(meadow160);
        TileSide eastSide16 = new TileSide.River(meadow160, river161, meadow162);
        TileSide southSide16 = new TileSide.River(meadow162, river161, meadow160);
        TileSide westSide16 = new TileSide.Forest(forest163);

        TileSide northSide17 = new TileSide.Meadow(meadow170);
        TileSide eastSide17 = new TileSide.River(meadow170, river171, meadow172);
        TileSide southSide17 = new TileSide.Meadow(meadow172);
        TileSide westSide17 = new TileSide.River(meadow172, river171, meadow170);

        TileSide northSide18 = new TileSide.Meadow(meadow180);
        TileSide eastSide18 = new TileSide.Forest(forest181);
        TileSide southSide18 = new TileSide.Forest(forest181);
        TileSide westSide18 = new TileSide.River(meadow182, river183, meadow180);

        TileSide northSide19 = new TileSide.Forest(forest190);
        TileSide eastSide19 = new TileSide.Forest(forest190);
        TileSide southSide19 = new TileSide.Meadow(meadow191);
        TileSide westSide19 = new TileSide.Meadow(meadow192);

        // tiles (not rotated)
        Tile tile12 = new Tile(12, Tile.Kind.NORMAL, northSide12, eastSide12, southSide12, westSide12);
        Tile tile13 = new Tile(13, Tile.Kind.NORMAL, northSide13, eastSide13, southSide13, westSide13);
        Tile tile14 = new Tile(14, Tile.Kind.NORMAL, northSide14, eastSide14, southSide14, westSide14);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, northSide15, eastSide15, southSide15, westSide15);
        Tile tile16 = new Tile(16, Tile.Kind.NORMAL, northSide16, eastSide16, southSide16, westSide16);
        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, northSide17, eastSide17, southSide17, westSide17);
        Tile tile18 = new Tile(18, Tile.Kind.NORMAL, northSide18, eastSide18, southSide18, westSide18);
        Tile tile19 = new Tile(19, Tile.Kind.NORMAL, northSide19, eastSide19, southSide19, westSide19);

        // placed tiles (rotated)
        PlacedTile placedTile12 = new PlacedTile(tile12, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile13 = new PlacedTile(tile13, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile14 = new PlacedTile(tile14, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile15 = new PlacedTile(tile15, PlayerColor.GREEN, Rotation.RIGHT, new Pos(3, 0));
        PlacedTile placedTile16 = new PlacedTile(tile16, PlayerColor.YELLOW, Rotation.NONE, new Pos(0, 1));
        PlacedTile placedTile17 = new PlacedTile(tile17, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 1));
        PlacedTile placedTile18 = new PlacedTile(tile18, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 1));
        PlacedTile placedTile19 = new PlacedTile(tile19, PlayerColor.GREEN, Rotation.LEFT, new Pos(3, 1));

        // occupants
        Occupant pawn120 = new Occupant(Occupant.Kind.PAWN, 120);
        Occupant pawn152 = new Occupant(Occupant.Kind.PAWN, 152);
        Occupant pawn162 = new Occupant(Occupant.Kind.PAWN, 162);
        Occupant pawn191 = new Occupant(Occupant.Kind.PAWN, 191);
        Occupant hut188 = new Occupant(Occupant.Kind.HUT, 188);

        // creation of the board
        Board board = Board.EMPTY
                .withNewTile(placedTile15)
                .withOccupant(pawn152)
                .withNewTile(placedTile19)
                .withOccupant(pawn191)
                .withNewTile(placedTile18)
                .withOccupant(hut188)
                .withNewTile(placedTile17)
                .withNewTile(placedTile16)
                .withOccupant(pawn162)
                .withNewTile(placedTile12)
                .withOccupant(pawn120)
                .withNewTile(placedTile13)
                .withNewTile(placedTile14);

        // pawns
        assertEquals(2, board.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(1, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(1, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));

        // huts
        assertEquals(1, board.occupantCount(PlayerColor.PURPLE, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
    }

    @Test
    void occupantCountWorksOnEmptyBoard() {
        // pawns
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));

        // huts
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.PURPLE, Occupant.Kind.HUT));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.YELLOW, Occupant.Kind.HUT));
        assertEquals(0, Board.EMPTY.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
    }

    @Test
    void insertionPositionsWorks() {
        // Tiles: 12 13 14 15
        //        16 17 19 10

        // tiles
        Tile tile12 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile13 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile14 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile16 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0));
        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), dummyMeadowSide(17, 0), dummyMeadowSide(17, 0), dummyMeadowSide(17, 0));
        Tile tile18 = new Tile(18, Tile.Kind.NORMAL, dummyMeadowSide(18, 0), dummyMeadowSide(18, 0), dummyMeadowSide(18, 0), dummyMeadowSide(18, 0));
        Tile tile19 = new Tile(19, Tile.Kind.NORMAL, dummyMeadowSide(19, 0), dummyMeadowSide(19, 0), dummyMeadowSide(19, 0), dummyMeadowSide(19, 0));

        // placed tiles
        PlacedTile placedTile12 = new PlacedTile(tile12, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile13 = new PlacedTile(tile13, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile14 = new PlacedTile(tile14, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile15 = new PlacedTile(tile15, PlayerColor.GREEN, Rotation.RIGHT, new Pos(3, 0));
        PlacedTile placedTile16 = new PlacedTile(tile16, PlayerColor.YELLOW, Rotation.NONE, new Pos(0, 1));
        PlacedTile placedTile17 = new PlacedTile(tile17, PlayerColor.PURPLE, Rotation.NONE, new Pos(1, 1));
        PlacedTile placedTile18 = new PlacedTile(tile18, PlayerColor.PURPLE, Rotation.NONE, new Pos(2, 1));
        PlacedTile placedTile19 = new PlacedTile(tile19, PlayerColor.BLUE, Rotation.LEFT, new Pos(3, 1));

        // creation of the board
        Board board = Board.EMPTY
                .withNewTile(placedTile12)
                .withNewTile(placedTile13)
                .withNewTile(placedTile14)
                .withNewTile(placedTile15)
                .withNewTile(placedTile16)
                .withNewTile(placedTile17)
                .withNewTile(placedTile18)
                .withNewTile(placedTile19);

        assertEquals(Set.of(
            new Pos(0, -1),
            new Pos(1, -1),
            new Pos(2, -1),
            new Pos(3, -1),
            new Pos(-1, 0),
            new Pos(4, 0),
            new Pos(-1, 1),
            new Pos(4, 1),
            new Pos(0, 2),
            new Pos(1, 2),
            new Pos(2, 2),
            new Pos(3, 2)
        ), board.insertionPositions());
    }

    @Test
    void insertionPositionsWorksOnCornersOfBoard() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        PlacedTile placedTile1 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-12, -12));
        PlacedTile placedTile2 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(12, -12));
        PlacedTile placedTile3 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-12, 12));
        PlacedTile placedTile4 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(12, 12));

        assertEquals(Set.of(
                new Pos(-11, -12),
                new Pos(-12, -11)
        ), Board.EMPTY.withNewTile(placedTile1).insertionPositions());

        assertEquals(Set.of(
                new Pos(11, -12),
                new Pos(12, -11)
        ), Board.EMPTY.withNewTile(placedTile2).insertionPositions());

        assertEquals(Set.of(
                new Pos(-11, 12),
                new Pos(-12, 11)
        ), Board.EMPTY.withNewTile(placedTile3).insertionPositions());

        assertEquals(Set.of(
                new Pos(11, 12),
                new Pos(12, 11)
        ), Board.EMPTY.withNewTile(placedTile4).insertionPositions());
    }

    @Test
    void insertionPositionsIsEmptyOnEmptyBoard() {
        assertTrue(Board.EMPTY.insertionPositions().isEmpty());
    }

    @Test
    void lastPlacedTileWorksForOneTilePlaced() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                Pos pos = new Pos(x, y);
                PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, pos);
                Board board = Board.EMPTY.withNewTile(placedTile);

                assertEquals(placedTile, board.lastPlacedTile());
            }
        }
    }

    @Test
    void lastPlacedTileWorksForManyTilesPlaced() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile5 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0));

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-5, -7));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.BLUE, Rotation.HALF_TURN, new Pos(-4, -7));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.YELLOW, Rotation.NONE, new Pos(-3, -7));
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.GREEN, Rotation.NONE, new Pos(-4, -6));
        PlacedTile placedTile5 = new PlacedTile(tile5, PlayerColor.RED, Rotation.NONE, new Pos(-3, -6));

        Board board = Board.EMPTY;

        board = board.withNewTile(placedTile1);
        assertEquals(placedTile1, board.lastPlacedTile());

        board = board.withNewTile(placedTile2);
        assertEquals(placedTile2, board.lastPlacedTile());

        board = board.withNewTile(placedTile3);
        assertEquals(placedTile3, board.lastPlacedTile());

        board = board.withNewTile(placedTile4);
        assertEquals(placedTile4, board.lastPlacedTile());

        board = board.withNewTile(placedTile5);
        assertEquals(placedTile5, board.lastPlacedTile());
    }

    @Test
    void lastPlacedTileReturnsNullIfBoardIsEmpty() {
        assertNull(Board.EMPTY.lastPlacedTile());
    }

    @Test
    void forestsClosedByLastTileIsEmptyIfBoardIsEmpty() {
        assertTrue(Board.EMPTY.forestsClosedByLastTile().isEmpty());
    }

    @Test
    void forestsClosedByLastTileIsEmptyIfNoForestsWereClosedByLastTile() {
        Tile tile1 = new Tile(1, Tile.Kind.NORMAL, dummyMeadowSide(1, 0), dummyForestSide(1, 1), dummyMeadowSide(1, 0), dummyMeadowSide(1, 0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL, dummyMeadowSide(2, 0), dummyMeadowSide(2, 0), dummyForestSide(2, 2), dummyForestSide(2, 1));
        Tile tile3 = new Tile(3, Tile.Kind.NORMAL, dummyForestSide(3, 0), dummyForestSide(3, 0), dummyMeadowSide(3, 1), dummyMeadowSide(3, 1));

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(1, 1));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3);

        assertTrue(board.forestsClosedByLastTile().isEmpty());
    }

    @Test
    void forestsClosedByLastTileWorksIfForestWasClosed() {
        Zone.Forest forest1 = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(21, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(22, Zone.Forest.Kind.PLAIN);

        TileSide.Forest forestSide1 = new TileSide.Forest(forest1);
        TileSide.Forest forestSide2 = new TileSide.Forest(forest2);
        TileSide.Forest forestSide3 = new TileSide.Forest(forest3);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL, dummyMeadowSide(1, 0), forestSide1, dummyMeadowSide(1, 0), dummyMeadowSide(1, 0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL, dummyMeadowSide(2, 0), dummyMeadowSide(2, 0), forestSide3, forestSide2);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2);

        Area<Zone.Forest> expectedArea = new Area<>(Set.of(forest1, forest2), List.of(), 0);

        assertEquals(Set.of(expectedArea), board.forestsClosedByLastTile());
    }

    @Test
    void forestsClosedByLastTileWorksIfTwoForestsWereCombinedByLastTileAndClosed() {
        Zone.Forest forest1 = new Zone.Forest(121, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(161, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(171, Zone.Forest.Kind.PLAIN);

        TileSide.Forest forestSide1 = new TileSide.Forest(forest1);
        TileSide.Forest forestSide2 = new TileSide.Forest(forest2);
        TileSide.Forest forestSide3 = new TileSide.Forest(forest3);
        TileSide.Forest forestSide4 = new TileSide.Forest(forest3);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), forestSide1, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile5 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), forestSide2);
        Tile tile6 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), forestSide3, dummyMeadowSide(17, 2), forestSide4);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, new Pos(0, -1));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.BLUE, Rotation.NONE, new Pos(1, -1));
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.RED, Rotation.NONE, new Pos(2, -1));
        PlacedTile placedTile5 = new PlacedTile(tile5, PlayerColor.GREEN, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile6 = new PlacedTile(tile6, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3)
                .withNewTile(placedTile4)
                .withNewTile(placedTile5)
                .withNewTile(placedTile6);

        Area<Zone.Forest> expectedArea = new Area<>(Set.of(forest1, forest2, forest3), List.of(), 0);

        assertEquals(Set.of(expectedArea), board.forestsClosedByLastTile());
    }
    @Test
    void forestsClosedByLastTileWorksIfTwoForestAreasWereClosed() {
        Zone.Forest forest1 = new Zone.Forest(121, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(161, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(171, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest4 = new Zone.Forest(173, Zone.Forest.Kind.PLAIN);

        TileSide.Forest forestSide1 = new TileSide.Forest(forest1);
        TileSide.Forest forestSide2 = new TileSide.Forest(forest2);
        TileSide.Forest forestSide3 = new TileSide.Forest(forest3);
        TileSide.Forest forestSide4 = new TileSide.Forest(forest4);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), forestSide1, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile5 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), forestSide2);
        Tile tile6 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), forestSide3, dummyMeadowSide(17, 2), forestSide4);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, new Pos(0, -1));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.BLUE, Rotation.NONE, new Pos(1, -1));
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.RED, Rotation.NONE, new Pos(2, -1));
        PlacedTile placedTile5 = new PlacedTile(tile5, PlayerColor.GREEN, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile6 = new PlacedTile(tile6, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3)
                .withNewTile(placedTile4)
                .withNewTile(placedTile5)
                .withNewTile(placedTile6);

        Area<Zone.Forest> expectedArea1 = new Area<>(Set.of(forest1, forest4), List.of(), 0);
        Area<Zone.Forest> expectedArea2 = new Area<>(Set.of(forest2, forest3), List.of(), 0);

        assertEquals(Set.of(expectedArea1, expectedArea2), board.forestsClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileIsEmptyIfBoardIsEmpty() {
        assertTrue(Board.EMPTY.riversClosedByLastTile().isEmpty());
    }

    @Test
    void riversClosedByLastTileIsEmptyIfNoRiversWereClosedByLastTile() {
        Tile tile1 = new Tile(1, Tile.Kind.NORMAL, dummyMeadowSide(1, 0), dummyRiverSide(1, 0, 1, 0), dummyMeadowSide(1, 0), dummyMeadowSide(1, 0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL, dummyMeadowSide(2, 0), dummyMeadowSide(2, 0), dummyRiverSide(2, 0, 2, 0), dummyRiverSide(2, 0, 1, 0));
        Tile tile3 = new Tile(3, Tile.Kind.NORMAL, dummyRiverSide(3, 1, 0, 1), dummyRiverSide(3, 1, 0, 1), dummyMeadowSide(3, 1), dummyMeadowSide(3, 1));

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(1, 1));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3);

        assertTrue(board.riversClosedByLastTile().isEmpty());
    }

    @Test
    void riversClosedByLastTileWorksIfRiverWasClosed() {
        Zone.Meadow meadow1 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(20, List.of(), null);

        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(21, 0, null);
        Zone.River river3 = new Zone.River(22, 0, null);

        TileSide.River riverSide1 = new TileSide.River(meadow1, river1, meadow1);
        TileSide.River riverSide2 = new TileSide.River(meadow2, river2, meadow2);
        TileSide.River riverSide3 = new TileSide.River(meadow2, river3, meadow2);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL, dummyMeadowSide(1, 0), riverSide1, dummyMeadowSide(1, 0), dummyMeadowSide(1, 0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL, dummyMeadowSide(2, 0), dummyMeadowSide(2, 0), riverSide3, riverSide2);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2);

        Area<Zone.River> expectedArea = new Area<>(Set.of(river1, river2), List.of(), 0);

        assertEquals(Set.of(expectedArea), board.riversClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileWorksIfTwoRiversWereCombinedByLastTileAndClosed() {
        Zone.Meadow meadow1 = new Zone.Meadow(120, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(160, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(170, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(172, List.of(), null);

        Zone.River river1 = new Zone.River(121, 0, null);
        Zone.River river2 = new Zone.River(161, 0, null);
        Zone.River river3 = new Zone.River(171, 0, null);

        TileSide.River riverSide1 = new TileSide.River(meadow1, river1, meadow1);
        TileSide.River riverSide2 = new TileSide.River(meadow2, river2, meadow2);
        TileSide.River riverSide3 = new TileSide.River(meadow3, river3, meadow4);
        TileSide.River riverSide4 = new TileSide.River(meadow4, river3, meadow3);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), riverSide1, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile5 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), riverSide2);
        Tile tile6 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), riverSide3, dummyMeadowSide(17, 2), riverSide4);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, new Pos(0, -1));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.BLUE, Rotation.NONE, new Pos(1, -1));
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.RED, Rotation.NONE, new Pos(2, -1));
        PlacedTile placedTile5 = new PlacedTile(tile5, PlayerColor.GREEN, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile6 = new PlacedTile(tile6, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3)
                .withNewTile(placedTile4)
                .withNewTile(placedTile5)
                .withNewTile(placedTile6);

        Area<Zone.River> expectedArea = new Area<>(Set.of(river1, river2, river3), List.of(), 0);

        assertEquals(Set.of(expectedArea), board.riversClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileWorksIfTwoRiverAreasWereClosed() {
        Zone.Meadow meadow1 = new Zone.Meadow(120, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(160, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(170, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(172, List.of(), null);

        Zone.River river1 = new Zone.River(121, 0, null);
        Zone.River river2 = new Zone.River(161, 0, null);
        Zone.River river3 = new Zone.River(171, 0, null);
        Zone.River river4 = new Zone.River(173, 0, null);

        TileSide.River riverSide1 = new TileSide.River(meadow1, river1, meadow1);
        TileSide.River riverSide2 = new TileSide.River(meadow2, river2, meadow2);
        TileSide.River riverSide3 = new TileSide.River(meadow3, river3, meadow4);
        TileSide.River riverSide4 = new TileSide.River(meadow4, river4, meadow3);

        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), riverSide1, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        Tile tile4 = new Tile(15, Tile.Kind.NORMAL, dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0), dummyMeadowSide(15, 0));
        Tile tile5 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), riverSide2);
        Tile tile6 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), riverSide3, dummyMeadowSide(17, 2), riverSide4);

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, new Pos(0, -1));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.BLUE, Rotation.NONE, new Pos(1, -1));
        PlacedTile placedTile4 = new PlacedTile(tile4, PlayerColor.RED, Rotation.NONE, new Pos(2, -1));
        PlacedTile placedTile5 = new PlacedTile(tile5, PlayerColor.GREEN, Rotation.NONE, new Pos(2, 0));
        PlacedTile placedTile6 = new PlacedTile(tile6, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withNewTile(placedTile3)
                .withNewTile(placedTile4)
                .withNewTile(placedTile5)
                .withNewTile(placedTile6);

        Area<Zone.River> expectedArea1 = new Area<>(Set.of(river1, river4), List.of(), 0);
        Area<Zone.River> expectedArea2 = new Area<>(Set.of(river2, river3), List.of(), 0);

        assertEquals(Set.of(expectedArea1, expectedArea2), board.riversClosedByLastTile());
    }

    @Test
     void canAddTileIsFalseIfBoardIsEmpty() {
        for (TileSide tileSide : new TileSide[] {dummyMeadowSide(12, 0), dummyForestSide(12, 0), dummyRiverSide(12, 1, 0, 1)}) {
            for (Tile.Kind tileKind : Tile.Kind.values()) {
                Tile tile = new Tile(12, tileKind, tileSide, tileSide, tileSide, tileSide);

                for (int y = -Board.REACH; y <= Board.REACH; ++y) {
                    for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                        for (Rotation rotation : Rotation.values()) {
                            PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, rotation, new Pos(x, y));

                            assertFalse(Board.EMPTY.canAddTile(placedTile));
                        }
                    }
                }
            }
        }
    }

    @Test
    void canAddTileIsFalseIfPositionIsNotAnInsertionPosition() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                // if the position is an insertion position, skip
                if (Math.abs(x + y) <= 1 && (x == 0 || y == 0)) {
                    continue;
                }

                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(x, y));
                assertFalse(board.canAddTile(placedTile2));
            }
        }
    }

    @Test
    void canAddTileIsFalseIfPositionOutsideOfTheBoard() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (int y = -Board.REACH - 2; y <= Board.REACH + 2; ++y) {
            for (int x = -Board.REACH - 2; x <= Board.REACH + 2; ++x) {
                // if the position is inside the board, skip
                if (y >= -Board.REACH && y <= Board.REACH && x >= -Board.REACH && x <= Board.REACH) {
                    continue;
                }

                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(x, y));
                assertFalse(board.canAddTile(placedTile2));
            }
        }
    }

    @Test
    void canAddTileIsFalseIfTileSidesMatchButTileIsAlreadyFilled() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        assertFalse(board.canAddTile(placedTile3));
    }

    @Test
    void canAddTileIsFalseIfPositionIsAnInsertionPositionButTileSidesDoNotMatch() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyForestSide(13, 1), dummyRiverSide(13, 0, 2, 0), dummyForestSide(13, 3));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (Direction direction : Direction.values()) {
            Pos pos = placedTile1.pos().neighbor(direction);

            for (Rotation rotation : Rotation.values()) {
                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.YELLOW, rotation, pos);

                // if tile sides match, skip
                if (placedTile2.side(direction.opposite()).isSameKindAs(placedTile1.side(direction))) {
                    continue;
                }

                assertFalse(board.canAddTile(placedTile2));
            }
        }
    }

    @Test
    void canAddTileIsTrueIfPositionIsAnInsertionPositionAndTileSidesMatch() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyForestSide(12, 1), dummyRiverSide(12, 0, 2, 1), dummyMeadowSide(12, 1));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyForestSide(13, 1), dummyRiverSide(13, 0, 2, 0), dummyForestSide(13, 3));

        for (Rotation rotation1 : Rotation.values()) {
            PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, rotation1, new Pos(-10, 10));
            Board board = Board.EMPTY.withNewTile(placedTile1);

            for (Direction direction : Direction.values()) {
                Pos pos = placedTile1.pos().neighbor(direction);

                for (Rotation rotation2 : Rotation.values()) {
                    PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.YELLOW, rotation2, pos);

                    // if tile sides don't match, skip
                    if (!placedTile2.side(direction.opposite()).isSameKindAs(placedTile1.side(direction))) {
                        continue;
                    }

                    assertTrue(board.canAddTile(placedTile2));
                }
            }
        }
    }

    @Test
    void couldPlaceTileIsFalseIfBoardIsEmpty() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 1), dummyMeadowSide(12, 0));
        assertFalse(Board.EMPTY.couldPlaceTile(tile));
    }

    @Test
    void couldPlaceTileIsFalseIfTileSidesDoNotMatch() {
        for (TileSide side1 : new TileSide[] {dummyMeadowSide(12, 0), dummyForestSide(12, 0), dummyRiverSide(12, 0, 1, 0)}) {
            Tile tile1 = new Tile(12, Tile.Kind.NORMAL, side1, side1, side1, side1);
            PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));
            Board board = Board.EMPTY.withNewTile(placedTile1);

            for (TileSide side2 : new TileSide[] {dummyMeadowSide(13, 0), dummyForestSide(13, 0), dummyRiverSide(13, 0, 1, 0)}) {
                // if the sides are the same kind, skip
                if (side1.isSameKindAs(side2)) {
                    continue;
                }

                Tile tile2 = new Tile(13, Tile.Kind.MENHIR, side2, side2, side2, side2);

                assertFalse(board.couldPlaceTile(tile2));
            }
        }
    }

    @Test
    void couldPlaceTileIsFalseIfTileSidesMatchButSideIsAlreadyConnected() {
        Tile tile1 = new Tile(15, Tile.Kind.NORMAL, dummyForestSide(15, 0), dummyMeadowSide(15, 1), dummyForestSide(15, 0), dummyForestSide(15, 0));
        Tile tile2 = new Tile(16, Tile.Kind.NORMAL, dummyForestSide(16, 0), dummyForestSide(16, 0), dummyForestSide(16, 0), dummyMeadowSide(16, 1));

        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(-9, 10));

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        Tile tile3 = new Tile(17, Tile.Kind.NORMAL, dummyMeadowSide(17, 0), dummyMeadowSide(17, 0), dummyMeadowSide(17, 0), dummyMeadowSide(17, 0));

        assertFalse(board.couldPlaceTile(tile3));
    }

    @Test
    void couldPlaceTileIsFalseIfTileSidesMatchButSideIsOnEdgeOfBoard() {
        Tile tile1 = new Tile(15, Tile.Kind.NORMAL, dummyForestSide(15, 0), dummyMeadowSide(15, 1), dummyForestSide(15, 0), dummyForestSide(15, 0));
        PlacedTile placedTile = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(12, 5));

        Board board = Board.EMPTY.withNewTile(placedTile);

        Tile tile2 = new Tile(16, Tile.Kind.NORMAL, dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0), dummyMeadowSide(16, 0));

        assertFalse(board.couldPlaceTile(tile2));
    }

    @Test
    void couldPlaceTileIsTrueIfAllTileSidesMatch() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 1), dummyMeadowSide(12, 0));
        PlacedTile placedTile = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));

        Board board = Board.EMPTY.withNewTile(placedTile);

        for (TileSide tileSide : new TileSide[] {dummyMeadowSide(13, 0), dummyForestSide(13, 0), dummyRiverSide(13, 0, 1, 0)}) {
            Tile tile2 = new Tile(13, Tile.Kind.NORMAL, tileSide, tileSide, tileSide, tileSide);

            assertTrue(board.couldPlaceTile(tile2));
        }
    }

    @Test
    void couldPlaceTileIsTrueIfAnyTileSidesMatch() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyForestSide(12, 0), dummyForestSide(12, 0), dummyForestSide(12, 0));

        for (Rotation rotation : Rotation.values()) {
            PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, rotation, new Pos(6, 10));
            Board board = Board.EMPTY.withNewTile(placedTile1);

            assertTrue(board.couldPlaceTile(new Tile(13, Tile.Kind.NORMAL, dummyForestSide(13, 0), dummyMeadowSide(13, 1), dummyMeadowSide(13, 1), dummyMeadowSide(13, 1))));
            assertTrue(board.couldPlaceTile(new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyForestSide(13, 1), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0))));
            assertTrue(board.couldPlaceTile(new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyForestSide(13, 1), dummyMeadowSide(13, 0))));
            assertTrue(board.couldPlaceTile(new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyForestSide(13, 1))));
        }
    }

    @Test
    void withNewTileDoesNotThrowIfBoardIsEmpty() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 1), dummyMeadowSide(12, 0));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));

        assertDoesNotThrow(() -> Board.EMPTY.withNewTile(placedTile));
    }

    @Test
    void withNewTileWorks() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 1), dummyMeadowSide(12, 0));
        Pos pos1 = new Pos(-10, 10);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, pos1);

        Tile tile2 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 1), dummyMeadowSide(12, 0));
        Pos pos2 = new Pos(-9, 10);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, pos2);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        assertEquals(placedTile1, board.tileAt(pos1));
        assertEquals(placedTile2, board.tileAt(pos2));

        // check that there are no tiles anywhere else
        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                Pos pos = new Pos(x, y);

                if (pos.equals(pos1) || pos.equals(pos2)) {
                    continue;
                }

                assertNull(board.tileAt(pos));
            }
        }
    }

    @Test
    void withNewTileThrowsIfBoardIsNotEmptyAndPositionIsNotAnInsertionPosition() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (int y = -Board.REACH; y <= Board.REACH; ++y) {
            for (int x = -Board.REACH; x <= Board.REACH; ++x) {
                // if the position is an insertion position, skip
                if (Math.abs(x + y) <= 1 && (x == 0 || y == 0)) {
                    continue;
                }

                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(x, y));
                assertThrows(IllegalArgumentException.class, () -> board.withNewTile(placedTile2));
            }
        }
    }

    @Test
    void withNewTileThrowsIfBoardIsNotEmptyAndPositionOutsideOfTheBoard() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (int y = -Board.REACH - 2; y <= Board.REACH + 2; ++y) {
            for (int x = -Board.REACH - 2; x <= Board.REACH + 2; ++x) {
                // if the position is inside the board, skip
                if (y >= -Board.REACH && y <= Board.REACH && x >= -Board.REACH && x <= Board.REACH) {
                    continue;
                }

                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(x, y));
                assertThrows(IllegalArgumentException.class, () -> board.withNewTile(placedTile2));
            }
        }
    }

    @Test
    void withNewTileThrowsIfTileSidesMatchButTileIsAlreadyFilled() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0), dummyMeadowSide(13, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);

        Tile tile3 = new Tile(14, Tile.Kind.NORMAL, dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0), dummyMeadowSide(14, 0));
        PlacedTile placedTile3 = new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));

        assertThrows(IllegalArgumentException.class, () -> board.withNewTile(placedTile3));
    }

    @Test
    void withNewTileThrowsIfPositionIsAnInsertionPositionButTileSidesDoNotMatch() {
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0), dummyMeadowSide(12, 0));
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.RED, Rotation.NONE, new Pos(-10, 10));

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyMeadowSide(13, 0), dummyForestSide(13, 1), dummyRiverSide(13, 0, 2, 0), dummyForestSide(13, 3));

        Board board = Board.EMPTY.withNewTile(placedTile1);

        for (Direction direction : Direction.values()) {
            Pos pos = placedTile1.pos().neighbor(direction);

            for (Rotation rotation : Rotation.values()) {
                PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.YELLOW, rotation, pos);

                // if tile sides match, skip
                if (placedTile2.side(direction.opposite()).isSameKindAs(placedTile1.side(direction))) {
                    continue;
                }

                assertThrows(IllegalArgumentException.class, () -> board.withNewTile(placedTile2));
            }
        }
    }

    @Test
    void withOccupantWorks(){
        Tile tile1 = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 3), dummyMeadowSide(12, 3));
        Pos pos1 = new Pos(1, 2);
        PlacedTile placedTile1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos1);

        Tile tile2 = new Tile(13, Tile.Kind.NORMAL, dummyRiverSide(13, 0, 1, 2), dummyMeadowSide(13, 2), dummyForestSide(13, 3), dummyRiverSide(13, 3, 0, 1));
        Pos pos2 = new Pos(1, 1);
        PlacedTile placedTile2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, pos2);

        Occupant occupant1 = new Occupant(Occupant.Kind.HUT, 122);
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 132);

        Board board = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withOccupant(occupant1).withOccupant(occupant2);

        PlacedTile placedTile1WithOccupant1 = new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, pos1, occupant1);
        PlacedTile placedTile2WithOccupant2 = new PlacedTile(tile2, PlayerColor.GREEN, Rotation.NONE, pos2, occupant2);

        assertEquals(placedTile1WithOccupant1, board.tileAt(pos1));
        assertEquals(placedTile2WithOccupant2, board.tileAt(pos2));
        assertEquals(Set.of(occupant1, occupant2), board.occupants());

        assertEquals(1, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.GREEN, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.HUT));
        assertEquals(0, board.occupantCount(PlayerColor.PURPLE, Occupant.Kind.HUT));

        assertEquals(1, board.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, board.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
    }

    @Test
    void withOccupantThrowsIfTileIsAlreadyOccupiedByHut() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 3), dummyMeadowSide(12, 3));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2));

        Occupant occupant1 = new Occupant(Occupant.Kind.HUT, 122);

        Board board = Board.EMPTY.withNewTile(placedTile).withOccupant(occupant1);

        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 121);

        assertThrows(IllegalArgumentException.class, () -> board.withOccupant(occupant2));
    }

    @Test
    void withOccupantThrowsIfTileIsAlreadyOccupiedByPawn() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 3), dummyMeadowSide(12, 3));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2));

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 121);

        Board board = Board.EMPTY.withNewTile(placedTile).withOccupant(occupant1);

        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, 122);

        assertThrows(IllegalArgumentException.class, () -> board.withOccupant(occupant2));
    }

    @Test
    void hashCodeIsEqualForEqualBoards() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 3), dummyMeadowSide(12, 3));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2));

        Animal deer = new Animal(1, Animal.Kind.DEER);

        Board board1 = Board.EMPTY.withNewTile(placedTile).withMoreCancelledAnimals(Set.of(deer));

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 120);

        Board board2 = Board.EMPTY.withNewTile(placedTile).withMoreCancelledAnimals(Set.of(deer))
                                  .withOccupant(occupant)
                                  .withoutOccupant(occupant);

        boolean sameHashCode = board1.hashCode() == board2.hashCode();

        assertTrue(sameHashCode);
    }

    @Test
    void hashCodeIsDifferentForDifferentBoards() {
        Tile tile = new Tile(12, Tile.Kind.NORMAL, dummyForestSide(12, 0), dummyMeadowSide(12, 1), dummyRiverSide(12, 1, 2, 3), dummyMeadowSide(12, 3));
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.BLUE, Rotation.NONE, new Pos(1, 2));

        Animal deer = new Animal(1, Animal.Kind.DEER);

        Board board1 = Board.EMPTY.withNewTile(placedTile).withMoreCancelledAnimals(Set.of(deer));

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 120);

        Board board2 = Board.EMPTY.withNewTile(placedTile).withMoreCancelledAnimals(Set.of(deer))
                                  .withOccupant(occupant);

        boolean sameHashCode = board1.hashCode() == board2.hashCode();

        assertFalse(sameHashCode);
    }
}