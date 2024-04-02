package etape5;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class apotest {

    Board boardWithStartTile() {
        // Ajout de la tuile de départ
        // Tile 56
        var l1 = new Zone.Lake(56_8, 1, null);
        var a0_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(56_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z2 = new Zone.Meadow(56_2, List.of(), null);
        var z3 = new Zone.River(56_3, 0, l1);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        Tile startTile =  new Tile(56, Tile.Kind.START, sN, sE, sS, sW);

        PlacedTile startPlacedTile = new PlacedTile(startTile,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);

        return Board.EMPTY.withNewTile(startPlacedTile);
    }

    Board boardWithTilesAround56NoOccupant() {
        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        Board boardWithEast = boardWithStartTile().withNewTile(placedTile15);

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, -1),
                null);
        Board boardWithN_W = boardWithEast.withNewTile(placedTile8);

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                PlayerColor.PURPLE,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWithN_S_W = boardWithN_W.withNewTile(placedTile31_South);

        //Tile added a droite (East)
        // Tile 29
        var z0 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1 = new Zone.Meadow(29_1, List.of(), null);
        var z2 = new Zone.River(29_2, 0, null);
        var z3 = new Zone.Meadow(29_3, List.of(), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.River(z1, z2, z3);
        var sS = new TileSide.River(z3, z2, z1);
        var sW = new TileSide.Forest(z0);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile29_East = new PlacedTile(tile29,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0),
                null);
        return boardWithN_S_W.withNewTile(placedTile29_East);
    }

    Board boardWithTilesAround56Occupants() {
        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(-1, 0));
        Board boardWithEast = boardWithStartTile().withNewTile(placedTile15);
        PlacedTile occupiedPlacedTile15 = placedTile15.withOccupant(new Occupant(Occupant.Kind.HUT, 15_1));
        Board boardWithEastFinal = boardWithEast.withOccupant(occupiedPlacedTile15.occupant());

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, -1));
        Board boardWithN_W = boardWithEastFinal.withNewTile(placedTile8);
        PlacedTile occupiedPlacedTile8 = placedTile8.withOccupant(new Occupant(Occupant.Kind.PAWN, 8_4));
        Board boardWith_N_W_Final = boardWithN_W.withOccupant(occupiedPlacedTile8.occupant());

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                PlayerColor.PURPLE,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWithN_S_W_notOccupied = boardWith_N_W_Final.withNewTile(placedTile31_South);
        PlacedTile occupiedPlacedTile31 = placedTile31_South.withOccupant(new Occupant(Occupant.Kind.PAWN, 31_0));
        Board boardWithN_S_W = boardWithN_S_W_notOccupied.withOccupant(occupiedPlacedTile31.occupant());

        //Tile added a droite (East)
        // Tile 29
        var z0 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1 = new Zone.Meadow(29_1, List.of(), null);
        var z2 = new Zone.River(29_2, 0, null);
        var z3 = new Zone.Meadow(29_3, List.of(), null);
        var sN = new TileSide.Forest(z0);
        var sE = new TileSide.River(z1, z2, z3);
        var sS = new TileSide.River(z3, z2, z1);
        var sW = new TileSide.Forest(z0);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile29_East = new PlacedTile(tile29,
                PlayerColor.RED,
                Rotation.NONE,
                new Pos(1, 0));
        Board boardFinal = boardWithN_S_W.withNewTile(placedTile29_East);
        PlacedTile occupiedPlacedTile29 = placedTile29_East.withOccupant(new Occupant(Occupant.Kind.PAWN, 29_2));
        return boardFinal.withOccupant(occupiedPlacedTile29.occupant());

    }

    PlacedTile startTile() {
        // Tile 56
        var l1 = new Zone.Lake(56_8, 1, null);
        var a0_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(56_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z2 = new Zone.Meadow(56_2, List.of(), null);
        var z3 = new Zone.River(56_3, 0, l1);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Forest(z1);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.River(z2, z3, z0);
        Tile startTile = new Tile(56, Tile.Kind.START, sN, sE, sS, sW);

        return new PlacedTile(startTile,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);
    }

    //                                              **TESTS**
    // ----------------------------------------------------------------------------------------------------------------
    @Test
    void tileAtWorksWhenEmptyBoard() {
        assertNull(Board.EMPTY.tileAt(new Pos(3,4)));
    }

    @Test
    void tileAtWorksWhenPositionOutOfBounds() {
        assertNull(Board.EMPTY.tileAt(new Pos(0,20)));
        assertNull(Board.EMPTY.tileAt(new Pos(20,0)));
        assertNull(Board.EMPTY.tileAt(new Pos(-13,4)));
        assertNull(Board.EMPTY.tileAt(new Pos(3,-14)));
    }

    @Test
    void tileAtWorks() {
        PlacedTile startPlacedTile = startTile();
        PlacedTile actual = boardWithStartTile().tileAt(new Pos(0,0));
        assertEquals(startPlacedTile, actual);


        //Placement de la tuile 15 à gauche de la tuile de départ
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);

        Board boardWith15 = boardWithStartTile().withNewTile(placedTile15);
        PlacedTile actual15 = boardWith15.tileAt(new Pos(-1,0));
        assertEquals(placedTile15, actual15);

        //Placement de la tuile 31 en bas de la tuile de départ (south)
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board boardWith31 = boardWithStartTile().withNewTile(placedTile31_South);
        PlacedTile actualSouth = boardWith31.tileAt(new Pos(0, 1));
        assertEquals(placedTile31_South, actualSouth);

    }

    @Test
    void CanBeAddedWorksWhenCannotBeAdded() {
        Board boardWithStartTile = boardWithStartTile();
        //Cannot be added bc TileSides do not match
        // Tile 74
        var z0 = new Zone.Meadow(74_0, List.of(), null);
        var z1 = new Zone.River(74_1, 1, null);
        var z2 = new Zone.Meadow(74_2, List.of(), null);
        var z3 = new Zone.Forest(74_3, Zone.Forest.Kind.PLAIN);
        var z4 = new Zone.Forest(74_4, Zone.Forest.Kind.PLAIN);
        var sN = new TileSide.River(z0, z1, z2);
        var sE = new TileSide.Forest(z3);
        var sS = new TileSide.River(z2, z1, z0);
        var sW = new TileSide.Forest(z4);
        Tile tile74 = new Tile(74, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile74 = new PlacedTile(tile74,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);

        assertFalse(boardWithStartTile.canAddTile(placedTile74));
        ///Cannot be added bc No corresponding insertion position
        PlacedTile placedTile74_NoInsertionPos = new PlacedTile(tile74,
                null,
                Rotation.NONE,
                new Pos(-10, 0),
                null);
        assertFalse(boardWithStartTile().canAddTile(placedTile74_NoInsertionPos));
    }

    @Test
    void CanBeAddedWorks() {
        // Board with start tile, trying to add a tile next to it
        Board boardWithStartTile = boardWithStartTile();
        //Tile added east
        // Tile 19
        var a0_0 = new Animal(19_0_0, Animal.Kind.DEER);
        var z0 = new Zone.Meadow(19_0, List.of(a0_0), null);
        var z1 = new Zone.River(19_1, 1, null);
        var z2 = new Zone.Meadow(19_2, List.of(), null);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.River(z0, z1, z2);
        var sS = new TileSide.River(z2, z1, z0);
        var sW = new TileSide.Meadow(z0);
        Tile tile19 = new Tile(19, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile19 = new PlacedTile(tile19,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile19));

        //Tile added North
        // Tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var a0_0_8 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z0_8 = new Zone.Meadow(8_0, List.of(a0_0_8), null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var a2_0_8 = new Animal(8_2_0, Animal.Kind.DEER);
        var z2_8 = new Zone.Meadow(8_2, List.of(a2_0_8), null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);
        var z5_8 = new Zone.River(8_5, 0, l1);
        var sn_8 = new TileSide.River(z0_8, z1_8, z2_8);
        var sE_8 = new TileSide.River(z2_8, z3_8, z4_8);
        var sS_8 = new TileSide.Meadow(z4_8);
        var sW_8 = new TileSide.River(z4_8, z5_8, z0_8);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sn_8, sE_8, sS_8, sW_8);
        PlacedTile placedTile8 = new PlacedTile(tile8,
                null,
                Rotation.NONE,
                new Pos(0, -1),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile8));

        //Tile added South
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31_South = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile31_South));
        //Tile added West
        PlacedTile placedTile31_West = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(1, 0),
                null);
        assertTrue(boardWithStartTile.canAddTile(placedTile31_West));

        //Tile added east with rotation
        // Tile 42
        var z0_42 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z1_42 = new Zone.Meadow(42_1, List.of(), null);
        var z2_42 = new Zone.Meadow(42_2, List.of(), null);
        var sN_42 = new TileSide.Forest(z0_42);
        var sE_42 = new TileSide.Forest(z0_42);
        var sS_42 = new TileSide.Meadow(z1_42);
        var sW_42 = new TileSide.Meadow(z2_42);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN_42, sE_42, sS_42, sW_42);
        PlacedTile placedTile42 = new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(1, 0),
                new Occupant(Occupant.Kind.PAWN, 422));

        assertTrue(boardWithStartTile.canAddTile(placedTile42));
    }

    @Test
    void tileWithIdWorksWhenTileNotInBoard() {
        Board boardWithStartTile = boardWithStartTile();

        assertThrows(IllegalArgumentException.class, () -> {
            boardWithStartTile.tileWithId(40);
        });

    }

    @Test
    void tileWithIdWorksOnStartTile() {
        Board boardWithStartTile = boardWithStartTile();
        PlacedTile expected = startTile();
        PlacedTile actual = boardWithStartTile.tileWithId(56);
        assertEquals(expected, actual);
    }

    @Test
    void cancelledAnimalsWorksWhenNoCancelledAnimals() {
        Board boardWithStartTile = boardWithStartTile();
        Set<Animal> expected = new HashSet<>();
        Set<Animal> actual = boardWithStartTile.cancelledAnimals();
        assertEquals(expected, actual);
    }

    //Teste + ou - withMoreCancelledAnimals aussi puisque l'on a besoin de cette méthode pour ajouter des cancelled animals
    @Test
    void cancelledAnimalsWorks() {
        Board boardWithStartTile = boardWithStartTile();
        Set<Animal> newCancelledAnimals = new HashSet<>();
        Collections.addAll(newCancelledAnimals,
                new Animal(4, Animal.Kind.TIGER),
                new Animal(5, Animal.Kind.AUROCHS));
        Board expected = boardWithStartTile.withMoreCancelledAnimals(newCancelledAnimals);
        assertEquals(newCancelledAnimals, expected.cancelledAnimals());
    }


    @Test
    void occupantsWorksWhenNoOccupants() {
        Board boardWithTiles = boardWithTilesAround56NoOccupant();
        Set<Occupant> expected = new HashSet<>();
        assertEquals(expected, boardWithTiles.occupants());
    }

    @Test
    void occupantsWorksWhenOccupants() {
        Board boardWithTiles = boardWithTilesAround56Occupants();
        Set<Occupant> expected = new HashSet<>();
        Collections.addAll(expected, new Occupant(Occupant.Kind.HUT, 15_1), new Occupant(Occupant.Kind.PAWN, 29_2), new Occupant(Occupant.Kind.PAWN, 8_4),
                new Occupant(Occupant.Kind.PAWN, 31_0));
        assertEquals(expected, boardWithTiles.occupants());
    }

    @Test
    void forestAreaThrows() {
        //Placement de la tuile 15 qui n'a pas de foret sur un emptyBoard
        // Tile 15
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z1_15 = new Zone.River(15_1, 0, null);
        var z2_15 = new Zone.Meadow(15_2, List.of(), null);
        var sN_15 = new TileSide.Meadow(z0_15);
        var sE_15 = new TileSide.River(z0_15, z1_15, z2_15);
        var sS_15 = new TileSide.River(z2_15, z1_15, z0_15);
        var sW_15 = new TileSide.Meadow(z0_15);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN_15, sE_15, sS_15, sW_15);
        PlacedTile placedTile15 = new PlacedTile(tile15,
                null,
                Rotation.NONE,
                new Pos(-1, 0),
                null);
        Board boardWith15 = Board.EMPTY.withNewTile(placedTile15);

        Zone.Forest forestZone = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);

        assertThrows(IllegalArgumentException.class, () -> {
            boardWith15.forestArea(forestZone);
            Board.EMPTY.forestArea(forestZone);
        });

        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            board.forestArea(forestZone);
        });
    }

    @Test
    void forestAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tile 29 zone foret
        var z0_29 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z1_56 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);

        Set<Zone.Forest> forestZones = Set.of(z0_29, z0_31, z1_56);

        Area<Zone.Forest> expected = new Area<>(forestZones, List.of(PlayerColor.PURPLE), 2);
        assertEquals(expected, board.forestArea(z0_29));
    }

    @Test
    void meadowAreaThrows() {
        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        Zone.Meadow meadowZone = new Zone.Meadow(3, new ArrayList<>(), null);
        assertThrows(IllegalArgumentException.class, () -> {
            board.meadowArea(meadowZone);
            Board.EMPTY.meadowArea(meadowZone);
        });
    }

    @Test
    void meadowAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tile 15 zone de pré
        var a0_0_15 = new Animal(15_0_0, Animal.Kind.DEER);
        var a0_0_56 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0_15 = new Zone.Meadow(15_0, List.of(a0_0_15), null);
        var z0_56 = new Zone.Meadow(56_0, List.of(a0_0_56), null);
        var z4_8 = new Zone.Meadow(8_4, List.of(), null);

        Set<Zone.Meadow> meadowZones = Set.of(z0_15, z0_56, z4_8);
        Area<Zone.Meadow> expected = new Area<>(meadowZones, List.of(PlayerColor.YELLOW), 5);
        assertEquals(expected, board.meadowArea(z0_15));
    }

    @Test
    void riverAreaThrows() {
        //Placement de la tuile 30 qui n'a pas de foret sur un emptyBoard
        // Tile 30
        var a0_0 = new Animal(30_0_0, Animal.Kind.DEER);
        var z0 = new Zone.Meadow(30_0, List.of(a0_0), null);
        var z1 = new Zone.Forest(30_1, Zone.Forest.Kind.WITH_MENHIR);
        var sN = new TileSide.Meadow(z0);
        var sE = new TileSide.Meadow(z0);
        var sS = new TileSide.Forest(z1);
        var sW = new TileSide.Forest(z1);
        Tile tile30 = new Tile(30, Tile.Kind.NORMAL, sN, sE, sS, sW);
        PlacedTile placedTile30 = new PlacedTile(tile30,
                null,
                Rotation.NONE,
                new Pos(0, 0),
                null);
        Board boardWith30 = Board.EMPTY.withNewTile(placedTile30);
        Zone.River riverZone = new Zone.River(40, 4, null);

        assertThrows(IllegalArgumentException.class, () -> {
            boardWith30.riverArea(riverZone);
            Board.EMPTY.riverArea(riverZone);
        });

        //Cherche une aire qui contient une zone qui n'est pas sur le plateau
        Board board = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            board.riverArea(riverZone);
        });
    }

    @Test
    void riverAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tuile 15 zone de rivière
        var z1_15 = new Zone.River(15_1, 0, null);
        var l1_56 = new Zone.Lake(56_8, 1, null);
        var z3_56 = new Zone.River(56_3, 0, l1_56);
        Set<Zone.River> riverZones15 = Set.of(z3_56, z1_15);
        //TODO bien d'accord que l'Area<Zone.River> contient que des PAWN et pas les HUT ?
        Area<Zone.River> expected15 = new Area<>(riverZones15, new ArrayList<>(), 1);
        //Area<Zone.River> expected = new Area<>(riverZones15, List.of(PlayerColor.RED), 1);
        assertEquals(expected15, board.riverArea(z1_15));


        //Tuile 29 zone de rivière
        var z2 = new Zone.River(29_2, 0, null);
        Set<Zone.River> riverZones29 = Set.of(z2);
        Area<Zone.River> expected29 = new Area<>(riverZones29, List.of(PlayerColor.RED), 2);
        assertEquals(expected29, board.riverArea(z2));


    }

    @Test
    void riverSystemAreaThrows() {
        //Placement tuile 31 qui n'a ni lac ni rivière sur un empty board
        // Tile 31
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a1_0_31 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z1_31 = new Zone.Meadow(31_1, List.of(a1_0_31), null);
        var sN_31 = new TileSide.Forest(z0_31);
        var sE_31 = new TileSide.Meadow(z1_31);
        var sS_31 = new TileSide.Meadow(z1_31);
        var sW_31 = new TileSide.Forest(z0_31);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN_31, sE_31, sS_31, sW_31);
        PlacedTile placedTile31 = new PlacedTile(tile31,
                null,
                Rotation.NONE,
                new Pos(0, 1),
                null);
        Board board = Board.EMPTY.withNewTile(placedTile31);
        Zone.Water riverSystemZone = new Zone.River(3, 4 , null);
        assertThrows(IllegalArgumentException.class, () -> {
            board.riverSystemArea(riverSystemZone);
            Board.EMPTY.riverSystemArea(riverSystemZone);
        });

        //Cherche l'aire d'une zone qui n'est pas sur le plateau
        Board fullBoard = boardWithTilesAround56Occupants();
        assertThrows(IllegalArgumentException.class, () -> {
            fullBoard.riverSystemArea(riverSystemZone);
        });
    }

    @Test
    void riverSystemAreaWorks() {
        Board board = boardWithTilesAround56Occupants();
        //Tuile 15 zone de rivière
        var z1_15 = new Zone.River(15_1, 0, null);
        var l1_56 = new Zone.Lake(56_8, 1, null);
        var z3_56 = new Zone.River(56_3, 0, l1_56);

        Set<Zone.Water> riverSystemZones15_56 = Set.of(z3_56, z1_15, l1_56);
        Area<Zone.Water> expected15 = new Area<>(riverSystemZones15_56, List.of(PlayerColor.RED) , 1);
        assertEquals(expected15, board.riverSystemArea(z1_15));

        //Tuile 8 lac
        var l1 = new Zone.Lake(8_8, 1, null);
        var z1_8 = new Zone.River(8_1, 0, l1);
        var z3_8 = new Zone.River(8_3, 0, l1);
        var z5_8 = new Zone.River(8_5, 0, l1);
        Set<Zone.Water> zones = new HashSet<>();
        Collections.addAll(zones, l1, z1_8, z3_8, z5_8);
        Area<Zone.Water> expected8 = new Area<>(zones, new ArrayList<>(), 3);
        assertEquals(expected8, board.riverSystemArea(l1));

        //Tuile 29 rivière
        var z2 = new Zone.River(29_2, 0, null);
        Area<Zone.Water> expected29 = new Area<>(Set.of(z2), new ArrayList<>(), 2);
    }

    @Test
    void withMoreCancelledAnimalsWorksTrivially() {
        Board board = boardWithTilesAround56Occupants();
        Board actual = board.withMoreCancelledAnimals(Set.of());

        assertEquals(board.cancelledAnimals(), actual.cancelledAnimals());
    }

    @Test
    void withMoreCancelledAnimalsWorks() {
        Board board = boardWithTilesAround56Occupants();

        //Adding a first set of cancelled animals
        Animal deer = new Animal(23, Animal.Kind.DEER);
        Animal auroch = new Animal(140, Animal.Kind.AUROCHS);
        Set<Animal> addedAnimals = Set.of(deer, auroch);
        Board actual = board.withMoreCancelledAnimals(addedAnimals);
        assertEquals(addedAnimals, actual.cancelledAnimals());

        //Adding a second set of cancelled animals
        Animal mammoth = new Animal(30, Animal.Kind.MAMMOTH);
        Animal tiger = new Animal(300, Animal.Kind.TIGER);
        Animal deer2 = new Animal(9, Animal.Kind.DEER);
        Set<Animal> addedAnimals2 = Set.of(mammoth, tiger, deer2);
        Set<Animal> expectedCancelledAnimals = new HashSet<>();
        expectedCancelledAnimals.addAll(addedAnimals);
        expectedCancelledAnimals.addAll(addedAnimals2);
        Board actual2 = actual.withMoreCancelledAnimals(addedAnimals2);
        assertEquals(expectedCancelledAnimals, actual2.cancelledAnimals());
    }

    @Test
    void withoutGatherersOrFisherWorksTrivially() {
        Board boardWithNoOccupants = boardWithTilesAround56NoOccupant();

        //Zone de forets
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        Area<Zone.Forest> forestArea31 = boardWithNoOccupants.forestArea(z0_31);
        //Set d'aires de foret
        Set<Area<Zone.Forest>> forestAreaSet = Set.of(forestArea31);

        //Area de river tile 15
        var z1_15 = new Zone.River(15_1, 0, null);
        Area<Zone.River> associatedArea15 =  boardWithNoOccupants.riverArea(z1_15);
        //Area de river tile 8
        var l1 = new Zone.Lake(8_8, 1, null);
        var z3_8 = new Zone.River(8_3, 0, l1);
        Area<Zone.River> associatedArea8 =  boardWithNoOccupants.riverArea(z3_8);
        //Set d'aires de river
        Set<Area<Zone.River>> riverAreaSet = Set.of(associatedArea15, associatedArea8);

        Board actual = boardWithNoOccupants.withoutGatherersOrFishersIn(forestAreaSet, riverAreaSet);

        assertEquals(boardWithNoOccupants.occupants(), actual.occupants());

    }

    @Test
    void withoutGatherersOrFisherWorksWhenRemoving1ForestOccAnd1RiverOcc() {
        Board boardWithOccupants = boardWithTilesAround56Occupants();

        //Zone de foret
        var z0_31 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        Area<Zone.Forest> forestArea = boardWithOccupants.forestArea(z0_31);
        Set<Area<Zone.Forest>> forestAreaSet = Set.of(forestArea);

        //Aire de river
        var z2 = new Zone.River(29_2, 0, null);
        Area<Zone.River> riverArea = boardWithOccupants.riverArea(z2);
        Set<Area<Zone.River>> riverAreaSet = Set.of(riverArea);

        Board boardwithoutGatherers = boardWithOccupants.withoutGatherersOrFishersIn(forestAreaSet, riverAreaSet);

        Set<Occupant> expected = Set.of(new Occupant(Occupant.Kind.PAWN, 8_4), new Occupant(Occupant.Kind.HUT, 15_1));

        assertEquals(expected, boardwithoutGatherers.occupants());
    }

}