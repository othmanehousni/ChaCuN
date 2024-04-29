package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class apotest1 {
    @Test
    void adjacentMeadowAndWithOccupantWorks() {
        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);


        // Tile 60
        var z060 = new Zone.Meadow(60_0, List.of(), null);
        var z160 = new Zone.Forest(60_1, Zone.Forest.Kind.WITH_MENHIR);
        var a2_060 = new Animal(60_2_0, Animal.Kind.DEER);
        var z260 = new Zone.Meadow(60_2, List.of(a2_060), null);
        var sN60 = new TileSide.Meadow(z060);
        var sE60 = new TileSide.Forest(z160);
        var sS60 = new TileSide.Meadow(z260);
        var sW60 = new TileSide.Forest(z160);
        Tile tile60 = new Tile(60, Tile.Kind.NORMAL, sN60, sE60, sS60, sW60);

        // Tile 61
        var a0_061 = new Animal(61_0_0, Animal.Kind.MAMMOTH);
        var z061 = new Zone.Meadow(61_0, List.of(a0_061), null);
        var sN61 = new TileSide.Meadow(z061);
        var sE61 = new TileSide.Meadow(z061);
        var sS61 = new TileSide.Meadow(z061);
        var sW61 = new TileSide.Meadow(z061);
        Tile tile61 = new Tile(61, Tile.Kind.NORMAL, sN61, sE61, sS61, sW61);


        // Tile 62
        var a0_062 = new Animal(62_0_0, Animal.Kind.DEER);
        var z062 = new Zone.Meadow(62_0, List.of(a0_062), null);
        var sN62 = new TileSide.Meadow(z062);
        var sE62 = new TileSide.Meadow(z062);
        var sS62 = new TileSide.Meadow(z062);
        var sW62 = new TileSide.Meadow(z062);
        Tile tile62 = new Tile(62, Tile.Kind.NORMAL, sN62, sE62, sS62, sW62);


        // Tile 64
        var z064 = new Zone.Forest(64_0, Zone.Forest.Kind.WITH_MENHIR);
        var z164 = new Zone.Meadow(64_1, List.of(), null);
        var z264 = new Zone.Forest(64_2, Zone.Forest.Kind.PLAIN);
        var sN64 = new TileSide.Forest(z064);
        var sE64 = new TileSide.Forest(z064);
        var sS64 = new TileSide.Meadow(z164);
        var sW64 = new TileSide.Forest(z264);
        Tile tile64 = new Tile(64, Tile.Kind.NORMAL, sN64, sE64, sS64, sW64);


        // Tile 94
        var z094 = new Zone.Forest(94_0, Zone.Forest.Kind.PLAIN);
        var z194 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var sN94 = new TileSide.Forest(z094);
        var sE94 = new TileSide.Meadow(z194);
        var sS94 = new TileSide.Meadow(z194);
        var sW94 = new TileSide.Meadow(z194);
        Tile tile94 = new Tile(94, Tile.Kind.MENHIR, sN94, sE94, sS94, sW94);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 272));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.GREEN,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));


        Board board3 = board2occupant.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 3)));

        Board board4 = board3.withNewTile(new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(6, 3)));

        Board board4occupant = board4.withOccupant(new Occupant(Occupant.Kind.PAWN, 421));


        Board board5 = board4occupant.withNewTile(new PlacedTile(tile60,
                PlayerColor.YELLOW,
                Rotation.RIGHT,
                new Pos(6, 2)));

        Board board6 = board5.withNewTile(new PlacedTile(tile61,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(4, 2)));


        Board finalBoard = board6.withNewTile(new PlacedTile(tile94,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 2)));


        Set<Zone.Meadow> meadowSet = new HashSet<>();
        Collections.addAll(meadowSet, z061, z049, z056, z194, z260);
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PlayerColor.GREEN);

        Area<Zone.Meadow> adjacentMeadow = new Area<>(meadowSet, occupants, 0);

        assertEquals(adjacentMeadow, finalBoard.adjacentMeadow(new Pos(5, 2), z194));
    }

    @Test
    void withOccupantFails() {
        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 272));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.GREEN,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));

        assertThrows(IllegalArgumentException.class, () -> board2occupant.withOccupant(new Occupant(Occupant.Kind.PAWN, 491)));
    }


    @Test
    void withOccupantWorks() {
        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);


        // Tile 60
        var z060 = new Zone.Meadow(60_0, List.of(), null);
        var z160 = new Zone.Forest(60_1, Zone.Forest.Kind.WITH_MENHIR);
        var a2_060 = new Animal(60_2_0, Animal.Kind.DEER);
        var z260 = new Zone.Meadow(60_2, List.of(a2_060), null);
        var sN60 = new TileSide.Meadow(z060);
        var sE60 = new TileSide.Forest(z160);
        var sS60 = new TileSide.Meadow(z260);
        var sW60 = new TileSide.Forest(z160);
        Tile tile60 = new Tile(60, Tile.Kind.NORMAL, sN60, sE60, sS60, sW60);

        // Tile 61
        var a0_061 = new Animal(61_0_0, Animal.Kind.MAMMOTH);
        var z061 = new Zone.Meadow(61_0, List.of(a0_061), null);
        var sN61 = new TileSide.Meadow(z061);
        var sE61 = new TileSide.Meadow(z061);
        var sS61 = new TileSide.Meadow(z061);
        var sW61 = new TileSide.Meadow(z061);
        Tile tile61 = new Tile(61, Tile.Kind.NORMAL, sN61, sE61, sS61, sW61);

        // Tile 94
        var z094 = new Zone.Forest(94_0, Zone.Forest.Kind.PLAIN);
        var z194 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var sN94 = new TileSide.Forest(z094);
        var sE94 = new TileSide.Meadow(z194);
        var sS94 = new TileSide.Meadow(z194);
        var sW94 = new TileSide.Meadow(z194);
        Tile tile94 = new Tile(94, Tile.Kind.MENHIR, sN94, sE94, sS94, sW94);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 272));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.GREEN,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));


        Board board3 = board2occupant.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 3)));

        Board board4 = board3.withNewTile(new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(6, 3)));

        Board finalBoard = board4.withOccupant(new Occupant(Occupant.Kind.PAWN, 421));


        assertEquals(board4, finalBoard.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 421)));
    }

    @Test
    void occupantCountWorksPawn() {

        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);


        // Tile 60
        var z060 = new Zone.Meadow(60_0, List.of(), null);
        var z160 = new Zone.Forest(60_1, Zone.Forest.Kind.WITH_MENHIR);
        var a2_060 = new Animal(60_2_0, Animal.Kind.DEER);
        var z260 = new Zone.Meadow(60_2, List.of(a2_060), null);
        var sN60 = new TileSide.Meadow(z060);
        var sE60 = new TileSide.Forest(z160);
        var sS60 = new TileSide.Meadow(z260);
        var sW60 = new TileSide.Forest(z160);
        Tile tile60 = new Tile(60, Tile.Kind.NORMAL, sN60, sE60, sS60, sW60);

        // Tile 61
        var a0_061 = new Animal(61_0_0, Animal.Kind.MAMMOTH);
        var z061 = new Zone.Meadow(61_0, List.of(a0_061), null);
        var sN61 = new TileSide.Meadow(z061);
        var sE61 = new TileSide.Meadow(z061);
        var sS61 = new TileSide.Meadow(z061);
        var sW61 = new TileSide.Meadow(z061);
        Tile tile61 = new Tile(61, Tile.Kind.NORMAL, sN61, sE61, sS61, sW61);


        // Tile 62
        var a0_062 = new Animal(62_0_0, Animal.Kind.DEER);
        var z062 = new Zone.Meadow(62_0, List.of(a0_062), null);
        var sN62 = new TileSide.Meadow(z062);
        var sE62 = new TileSide.Meadow(z062);
        var sS62 = new TileSide.Meadow(z062);
        var sW62 = new TileSide.Meadow(z062);
        Tile tile62 = new Tile(62, Tile.Kind.NORMAL, sN62, sE62, sS62, sW62);


        // Tile 64
        var z064 = new Zone.Forest(64_0, Zone.Forest.Kind.WITH_MENHIR);
        var z164 = new Zone.Meadow(64_1, List.of(), null);
        var z264 = new Zone.Forest(64_2, Zone.Forest.Kind.PLAIN);
        var sN64 = new TileSide.Forest(z064);
        var sE64 = new TileSide.Forest(z064);
        var sS64 = new TileSide.Meadow(z164);
        var sW64 = new TileSide.Forest(z264);
        Tile tile64 = new Tile(64, Tile.Kind.NORMAL, sN64, sE64, sS64, sW64);


        // Tile 94
        var z094 = new Zone.Forest(94_0, Zone.Forest.Kind.PLAIN);
        var z194 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var sN94 = new TileSide.Forest(z094);
        var sE94 = new TileSide.Meadow(z194);
        var sS94 = new TileSide.Meadow(z194);
        var sW94 = new TileSide.Meadow(z194);
        Tile tile94 = new Tile(94, Tile.Kind.MENHIR, sN94, sE94, sS94, sW94);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 272));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.GREEN,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));


        Board board3 = board2occupant.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 3)));

        Board board4 = board3.withNewTile(new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(6, 3)));

        Board board4occupant = board4.withOccupant(new Occupant(Occupant.Kind.PAWN, 421));


        Board board5 = board4occupant.withNewTile(new PlacedTile(tile60,
                PlayerColor.YELLOW,
                Rotation.RIGHT,
                new Pos(6, 2)));

        Board board6 = board5.withNewTile(new PlacedTile(tile61,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(4, 2)));


        Board finalBoard = board6.withNewTile(new PlacedTile(tile94,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 2)));

        int occupantCountYellow = 1;
        int occupantCountBlue = 1;
        int occupantCountRed = 0;
        int occupantCountPurple = 0;
        int occupantCountGreen = 1;

        assertEquals(occupantCountYellow, finalBoard.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(occupantCountBlue, finalBoard.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(occupantCountRed, finalBoard.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(occupantCountPurple, finalBoard.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
        assertEquals(occupantCountGreen, finalBoard.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountWorksHut() {

        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);


        // Tile 60
        var z060 = new Zone.Meadow(60_0, List.of(), null);
        var z160 = new Zone.Forest(60_1, Zone.Forest.Kind.WITH_MENHIR);
        var a2_060 = new Animal(60_2_0, Animal.Kind.DEER);
        var z260 = new Zone.Meadow(60_2, List.of(a2_060), null);
        var sN60 = new TileSide.Meadow(z060);
        var sE60 = new TileSide.Forest(z160);
        var sS60 = new TileSide.Meadow(z260);
        var sW60 = new TileSide.Forest(z160);
        Tile tile60 = new Tile(60, Tile.Kind.NORMAL, sN60, sE60, sS60, sW60);

        // Tile 61
        var a0_061 = new Animal(61_0_0, Animal.Kind.MAMMOTH);
        var z061 = new Zone.Meadow(61_0, List.of(a0_061), null);
        var sN61 = new TileSide.Meadow(z061);
        var sE61 = new TileSide.Meadow(z061);
        var sS61 = new TileSide.Meadow(z061);
        var sW61 = new TileSide.Meadow(z061);
        Tile tile61 = new Tile(61, Tile.Kind.NORMAL, sN61, sE61, sS61, sW61);


        // Tile 62
        var a0_062 = new Animal(62_0_0, Animal.Kind.DEER);
        var z062 = new Zone.Meadow(62_0, List.of(a0_062), null);
        var sN62 = new TileSide.Meadow(z062);
        var sE62 = new TileSide.Meadow(z062);
        var sS62 = new TileSide.Meadow(z062);
        var sW62 = new TileSide.Meadow(z062);
        Tile tile62 = new Tile(62, Tile.Kind.NORMAL, sN62, sE62, sS62, sW62);


        // Tile 64
        var z064 = new Zone.Forest(64_0, Zone.Forest.Kind.WITH_MENHIR);
        var z164 = new Zone.Meadow(64_1, List.of(), null);
        var z264 = new Zone.Forest(64_2, Zone.Forest.Kind.PLAIN);
        var sN64 = new TileSide.Forest(z064);
        var sE64 = new TileSide.Forest(z064);
        var sS64 = new TileSide.Meadow(z164);
        var sW64 = new TileSide.Forest(z264);
        Tile tile64 = new Tile(64, Tile.Kind.NORMAL, sN64, sE64, sS64, sW64);


        // Tile 94
        var z094 = new Zone.Forest(94_0, Zone.Forest.Kind.PLAIN);
        var z194 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var sN94 = new TileSide.Forest(z094);
        var sE94 = new TileSide.Meadow(z194);
        var sS94 = new TileSide.Meadow(z194);
        var sW94 = new TileSide.Meadow(z194);
        Tile tile94 = new Tile(94, Tile.Kind.MENHIR, sN94, sE94, sS94, sW94);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.HUT, 271));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));


        Board board3 = board2occupant.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 3)));

        Board board4 = board3.withNewTile(new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(6, 3)));

        Board board4occupant = board4.withOccupant(new Occupant(Occupant.Kind.PAWN, 421));


        Board board5 = board4occupant.withNewTile(new PlacedTile(tile60,
                PlayerColor.YELLOW,
                Rotation.RIGHT,
                new Pos(6, 2)));

        Board board6 = board5.withNewTile(new PlacedTile(tile61,
                PlayerColor.PURPLE,
                Rotation.NONE,
                new Pos(4, 2)));


        Board finalBoard = board6.withNewTile(new PlacedTile(tile94,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 2)));

        int occupantCountYellowPawn = 1;
        int occupantCountYellowHut = 1;
        int occupantCountBlue = 1;
        int occupantCountRed = 0;
        int occupantCountPurple = 0;
        int occupantCountGreen = 0;

        assertEquals(occupantCountYellowPawn, finalBoard.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(occupantCountBlue, finalBoard.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(occupantCountRed, finalBoard.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(occupantCountPurple, finalBoard.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
        assertEquals(occupantCountGreen, finalBoard.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(occupantCountYellowHut, finalBoard.occupantCount(PlayerColor.YELLOW, Occupant.Kind.HUT));
    }

    @Test
    void insertionPositionsWorksSingleTile() {
        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);

        Board boardEmpty = Board.EMPTY;

        Board board = boardEmpty.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(0, 0)));

        Set<Pos> insertionPositions = new HashSet<>();
        Collections.addAll(insertionPositions,
                new Pos(0, 1),
                new Pos(1, 0),
                new Pos(-1, 0),
                new Pos(0, -1));

        assertEquals(insertionPositions, board.insertionPositions());
    }

    @Test
    void insertionPositionsWorksMultipleTiles() {
        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(0, 0)));

        Board board = board1.withNewTile(new PlacedTile(tile42,
                PlayerColor.YELLOW,
                Rotation.LEFT,
                new Pos(1, 0)));

        Set<Pos> insertionPositions = new HashSet<>();
        Collections.addAll(insertionPositions,
                new Pos(0, 1),
                new Pos(-1, 0),
                new Pos(0, -1),
                new Pos(1, -1),
                new Pos(1, 1),
                new Pos(2, 0));

        assertEquals(insertionPositions, board.insertionPositions());
    }

    @Test
    void lastPlacedTileWorks() {
        // Tile 27
        var z027 = new Zone.Meadow(27_0, List.of(), null);
        var z127 = new Zone.River(27_1, 0, null);
        var a2_027 = new Animal(27_2_0, Animal.Kind.DEER);
        var z227 = new Zone.Meadow(27_2, List.of(a2_027), null);
        var z327 = new Zone.Forest(27_3, Zone.Forest.Kind.PLAIN);
        var sN27 = new TileSide.Meadow(z027);
        var sE27 = new TileSide.River(z027, z127, z227);
        var sS27 = new TileSide.River(z227, z127, z027);
        var sW27 = new TileSide.Forest(z327);
        Tile tile27 = new Tile(27, Tile.Kind.NORMAL, sN27, sE27, sS27, sW27);

        // Tile 42
        var z042 = new Zone.Forest(42_0, Zone.Forest.Kind.WITH_MENHIR);
        var z142 = new Zone.Meadow(42_1, List.of(), null);
        var z242 = new Zone.Meadow(42_2, List.of(), null);
        var sN42 = new TileSide.Forest(z042);
        var sE42 = new TileSide.Forest(z042);
        var sS42 = new TileSide.Meadow(z142);
        var sW42 = new TileSide.Meadow(z242);
        Tile tile42 = new Tile(42, Tile.Kind.NORMAL, sN42, sE42, sS42, sW42);

        // Tile 49
        var a0_049 = new Animal(49_0_0, Animal.Kind.DEER);
        var z049 = new Zone.Meadow(49_0, List.of(a0_049), null);
        var z149 = new Zone.River(49_1, 0, null);
        var a2_049 = new Animal(49_2_0, Animal.Kind.TIGER);
        var z249 = new Zone.Meadow(49_2, List.of(a2_049), null);
        var sN49 = new TileSide.Meadow(z049);
        var sE49 = new TileSide.River(z049, z149, z249);
        var sS49 = new TileSide.Meadow(z249);
        var sW49 = new TileSide.River(z249, z149, z049);
        Tile tile49 = new Tile(49, Tile.Kind.NORMAL, sN49, sE49, sS49, sW49);


        // Tile 56
        var l156 = new Zone.Lake(56_8, 1, null);
        var a0_056 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z056 = new Zone.Meadow(56_0, List.of(a0_056), null);
        var z156 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z256 = new Zone.Meadow(56_2, List.of(), null);
        var z356 = new Zone.River(56_3, 0, l156);
        var sN56 = new TileSide.Meadow(z056);
        var sE56 = new TileSide.Forest(z156);
        var sS56 = new TileSide.Forest(z156);
        var sW56 = new TileSide.River(z256, z356, z056);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);


        // Tile 60
        var z060 = new Zone.Meadow(60_0, List.of(), null);
        var z160 = new Zone.Forest(60_1, Zone.Forest.Kind.WITH_MENHIR);
        var a2_060 = new Animal(60_2_0, Animal.Kind.DEER);
        var z260 = new Zone.Meadow(60_2, List.of(a2_060), null);
        var sN60 = new TileSide.Meadow(z060);
        var sE60 = new TileSide.Forest(z160);
        var sS60 = new TileSide.Meadow(z260);
        var sW60 = new TileSide.Forest(z160);
        Tile tile60 = new Tile(60, Tile.Kind.NORMAL, sN60, sE60, sS60, sW60);

        // Tile 61
        var a0_061 = new Animal(61_0_0, Animal.Kind.MAMMOTH);
        var z061 = new Zone.Meadow(61_0, List.of(a0_061), null);
        var sN61 = new TileSide.Meadow(z061);
        var sE61 = new TileSide.Meadow(z061);
        var sS61 = new TileSide.Meadow(z061);
        var sW61 = new TileSide.Meadow(z061);
        Tile tile61 = new Tile(61, Tile.Kind.NORMAL, sN61, sE61, sS61, sW61);

        // Tile 94
        var z094 = new Zone.Forest(94_0, Zone.Forest.Kind.PLAIN);
        var z194 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var sN94 = new TileSide.Forest(z094);
        var sE94 = new TileSide.Meadow(z194);
        var sS94 = new TileSide.Meadow(z194);
        var sW94 = new TileSide.Meadow(z194);
        Tile tile94 = new Tile(94, Tile.Kind.MENHIR, sN94, sE94, sS94, sW94);


        Board boardEmpty = Board.EMPTY;

        Board board1 = boardEmpty.withNewTile(new PlacedTile(tile27,
                PlayerColor.YELLOW,
                Rotation.NONE, new Pos(3, 3)));

        Board board1occupant = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 272));

        Board board2 = board1occupant.withNewTile(new PlacedTile(tile49,
                PlayerColor.GREEN,
                Rotation.NONE,
                new Pos(4, 3)));

        Board board2occupant = board2.withOccupant(new Occupant(Occupant.Kind.PAWN, 490));


        Board board3 = board2occupant.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 3)));

        Board board4 = board3.withNewTile(new PlacedTile(tile42,
                PlayerColor.BLUE,
                Rotation.LEFT,
                new Pos(6, 3)));

        Board board4occupant = board4.withOccupant(new Occupant(Occupant.Kind.PAWN, 421));


        Board board5 = board4occupant.withNewTile(new PlacedTile(tile60,
                PlayerColor.YELLOW,
                Rotation.RIGHT,
                new Pos(6, 2)));

        Board board6 = board5.withNewTile(new PlacedTile(tile61,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(4, 2)));


        PlacedTile lastTile = new PlacedTile(tile94,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(5, 2));

        Board finalBoard = board6.withNewTile(lastTile);

        assertEquals(lastTile, finalBoard.lastPlacedTile());
    }

    @Test
    void lastPlacedTileWorksOnEmptyBoard() {
        Board board1 = Board.EMPTY;
        assertNull(board1.lastPlacedTile());
    }

    @Test
    void forestsClosedByLastTileWorksOnEmptyBoard() {
        Board board1 = Board.EMPTY;
        assertEquals(new HashSet<>(), board1.forestsClosedByLastTile());
    }

    @Test
    void forestsClosedByLastTileWorksOnNonEmptyBoard() {
        // Tile 23
        var a230_0 = new Animal(23_0_0, Animal.Kind.AUROCHS);
        var z230 = new Zone.Meadow(23_0, List.of(a230_0), null);
        var z231 = new Zone.River(23_1, 0, null);
        var z232 = new Zone.Meadow(23_2, List.of(), null);
        var z233 = new Zone.Forest(23_3, Zone.Forest.Kind.WITH_MENHIR);
        var sN23 = new TileSide.River(z230, z231, z232);
        var sE23 = new TileSide.River(z232, z231, z230);
        var sS23 = new TileSide.Forest(z233);
        var sW23 = new TileSide.Forest(z233);
        Tile tile23 = new Tile(23, Tile.Kind.NORMAL, sN23, sE23, sS23, sW23);

        // Tile 29
        var z290 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z291 = new Zone.Meadow(29_1, List.of(), null);
        var z292 = new Zone.River(29_2, 0, null);
        var z293 = new Zone.Meadow(29_3, List.of(), null);
        var sN29 = new TileSide.Forest(z290);
        var sE29 = new TileSide.River(z291, z292, z293);
        var sS29 = new TileSide.River(z293, z292, z291);
        var sW29 = new TileSide.Forest(z290);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN29, sE29, sS29, sW29);

        // Tile 33
        var z330 = new Zone.Forest(33_0, Zone.Forest.Kind.WITH_MENHIR);
        var z331 = new Zone.Meadow(33_1, List.of(), null);
        var sN33 = new TileSide.Forest(z330);
        var sE33 = new TileSide.Meadow(z331);
        var sS33 = new TileSide.Meadow(z331);
        var sW33 = new TileSide.Forest(z330);
        Tile tile33 = new Tile(33, Tile.Kind.NORMAL, sN33, sE33, sS33, sW33);

        // Tile 43
        var z430 = new Zone.Forest(43_0, Zone.Forest.Kind.WITH_MENHIR);
        var z431 = new Zone.Forest(43_1, Zone.Forest.Kind.PLAIN);
        var z432 = new Zone.Meadow(43_2, List.of(), null);
        var sN43 = new TileSide.Forest(z430);
        var sE43 = new TileSide.Forest(z430);
        var sS43 = new TileSide.Forest(z431);
        var sW43 = new TileSide.Meadow(z432);
        Tile tile43 = new Tile(43, Tile.Kind.NORMAL, sN43, sE43, sS43, sW43);

        Board board1 = Board.EMPTY;
        Board board2 = board1.withNewTile(new PlacedTile(tile43,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, 0)));

        Board board3 = board2.withNewTile(new PlacedTile(tile33,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1, 0)));

        Board board4 = board3.withNewTile(new PlacedTile(tile23,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1, -1)));

        Board board = board4.withNewTile(new PlacedTile(tile29,
                PlayerColor.YELLOW,
                Rotation.HALF_TURN,
                new Pos(0, -1)));

        Set<Zone.Forest> forestSet = new HashSet<>();
        Collections.addAll(forestSet, z233, z330, z290, z430);
        Set<Area<Zone.Forest>> forestAreaSet = new HashSet<>();
        forestAreaSet.add(new Area<>(forestSet, new ArrayList<>(), 0));

        assertEquals(forestAreaSet, board.forestsClosedByLastTile());
    }


    @Test
    void riversClosedByLastTileWorksOnEmptyBoard() {
        Board board1 = Board.EMPTY;
        assertEquals(new HashSet<>(), board1.riversClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileWorksOnNonEmptyBoard() {
        // Tile 16
        var z160 = new Zone.Meadow(16_0, List.of(), null);
        var z161 = new Zone.River(16_1, 0, null);
        var a162_0 = new Animal(16_2_0, Animal.Kind.DEER);
        var z162 = new Zone.Meadow(16_2, List.of(a162_0), null);
        var sN16 = new TileSide.Meadow(z160);
        var sE16 = new TileSide.River(z160, z161, z162);
        var sS16 = new TileSide.River(z162, z161, z160);
        var sW16 = new TileSide.Meadow(z160);
        Tile tile16 = new Tile(16, Tile.Kind.NORMAL, sN16, sE16, sS16, sW16);

        // Tile 17
        var z170 = new Zone.Meadow(17_0, List.of(), null);
        var z171 = new Zone.River(17_1, 0, null);
        var a172_0 = new Animal(17_2_0, Animal.Kind.DEER);
        var z172 = new Zone.Meadow(17_2, List.of(a172_0), null);
        var z173 = new Zone.River(17_3, 0, null);
        var a174_0 = new Animal(17_4_0, Animal.Kind.TIGER);
        var z174 = new Zone.Meadow(17_4, List.of(a174_0), null);
        var sN17 = new TileSide.River(z170, z171, z172);
        var sE17 = new TileSide.River(z172, z171, z170);
        var sS17 = new TileSide.River(z170, z173, z174);
        var sW17 = new TileSide.River(z174, z173, z170);
        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, sN17, sE17, sS17, sW17);

        // Tile 25
        var z250 = new Zone.Meadow(25_0, List.of(), null);
        var z251 = new Zone.River(25_1, 0, null);
        var a252_0 = new Animal(25_2_0, Animal.Kind.DEER);
        var z252 = new Zone.Meadow(25_2, List.of(a252_0), null);
        var z253 = new Zone.Forest(25_3, Zone.Forest.Kind.PLAIN);
        var sN25 = new TileSide.River(z250, z251, z252);
        var sE25 = new TileSide.River(z252, z251, z250);
        var sS25 = new TileSide.Forest(z253);
        var sW25 = new TileSide.Meadow(z250);
        Tile tile25 = new Tile(25, Tile.Kind.NORMAL, sN25, sE25, sS25, sW25);

        // Tile 28
        var z280 = new Zone.Meadow(28_0, List.of(), null);
        var z281 = new Zone.River(28_1, 1, null);
        var z282 = new Zone.Meadow(28_2, List.of(), null);
        var z283 = new Zone.Forest(28_3, Zone.Forest.Kind.PLAIN);
        var sN28 = new TileSide.River(z280, z281, z282);
        var sE28 = new TileSide.Meadow(z282);
        var sS28 = new TileSide.Forest(z283);
        var sW28 = new TileSide.River(z282, z281, z280);
        Tile tile28 = new Tile(28, Tile.Kind.NORMAL, sN28, sE28, sS28, sW28);

        Board board1 = Board.EMPTY;
        Board board2 = board1.withNewTile(new PlacedTile(tile25,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, 0)));

        Board board3 = board2.withNewTile(new PlacedTile(tile28,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1, 0)));

        Board board4 = board3.withNewTile(new PlacedTile(tile17,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1, -1)));

        Board board = board4.withNewTile(new PlacedTile(tile16,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0, -1)));

        Set<Zone.River> riverSet = new HashSet<>();
        Collections.addAll(riverSet, z281, z173, z161, z251);
        Set<Area<Zone.River>> riverAreaSet = new HashSet<>();
        riverAreaSet.add(new Area<>(riverSet, new ArrayList<>(), 0));

        assertEquals(riverAreaSet, board.riversClosedByLastTile());
    }

    @Test
    void meadowAreasWorks() {
        // Tile 8
        var l81 = new Zone.Lake(8_8, 1, null);
        var a80_0 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z80 = new Zone.Meadow(8_0, List.of(a80_0), null);
        var z81 = new Zone.River(8_1, 0, l81);
        var a82_0 = new Animal(8_2_0, Animal.Kind.DEER);
        var z82 = new Zone.Meadow(8_2, List.of(a82_0), null);
        var z83 = new Zone.River(8_3, 0, l81);
        var z84 = new Zone.Meadow(8_4, List.of(), null);
        var z85 = new Zone.River(8_5, 0, l81);
        var sN8 = new TileSide.River(z80, z81, z82);
        var sE8 = new TileSide.River(z82, z83, z84);
        var sS8 = new TileSide.Meadow(z84);
        var sW8 = new TileSide.River(z84, z85, z80);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sN8, sE8, sS8, sW8);

        // Tile 15
        var a150_0 = new Animal(15_0_0, Animal.Kind.DEER);
        var z150 = new Zone.Meadow(15_0, List.of(a150_0), null);
        var z151 = new Zone.River(15_1, 0, null);
        var z152 = new Zone.Meadow(15_2, List.of(), null);
        var sN15 = new TileSide.Meadow(z150);
        var sE15 = new TileSide.River(z150, z151, z152);
        var sS15 = new TileSide.River(z152, z151, z150);
        var sW15 = new TileSide.Meadow(z150);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN15, sE15, sS15, sW15);

        // Tile 29
        var z290 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z291 = new Zone.Meadow(29_1, List.of(), null);
        var z292 = new Zone.River(29_2, 0, null);
        var z293 = new Zone.Meadow(29_3, List.of(), null);
        var sN29 = new TileSide.Forest(z290);
        var sE29 = new TileSide.River(z291, z292, z293);
        var sS29 = new TileSide.River(z293, z292, z291);
        var sW29 = new TileSide.Forest(z290);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN29, sE29, sS29, sW29);

        // Tile 31
        var z310 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a311_0 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z311 = new Zone.Meadow(31_1, List.of(a311_0), null);
        var sN31 = new TileSide.Forest(z310);
        var sE31 = new TileSide.Meadow(z311);
        var sS31 = new TileSide.Meadow(z311);
        var sW31 = new TileSide.Forest(z310);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN31, sE31, sS31, sW31);

        // Tile 56
        var l561 = new Zone.Lake(56_8, 1, null);
        var a560_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z560 = new Zone.Meadow(56_0, List.of(a560_0), null);
        var z561 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z562 = new Zone.Meadow(56_2, List.of(), null);
        var z563 = new Zone.River(56_3, 0, l561);
        var sN56 = new TileSide.Meadow(z560);
        var sE56 = new TileSide.Forest(z561);
        var sS56 = new TileSide.Forest(z561);
        var sW56 = new TileSide.River(z562, z563, z560);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);

        Board board = Board.EMPTY;

        Board board1 = board.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,0)));

        Board board2 = board1.withNewTile(new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,-1)));

        Board board3 = board2.withNewTile(new PlacedTile(tile29,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1,0)));

        Board board4 = board3.withNewTile(new PlacedTile(tile15,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(-1,0)));

        Board finalBoard = board4.withNewTile(new PlacedTile(tile31,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,1)));

        Set<Zone.Meadow> meadowSet1 = new HashSet<>();
        Collections.addAll(meadowSet1, z80);
        Area<Zone.Meadow> meadowArea1 = new Area<>(meadowSet1, new ArrayList<>(), 2);

        Set<Zone.Meadow> meadowSet2 = new HashSet<>();
        Collections.addAll(meadowSet2, z82);
        Area<Zone.Meadow> meadowArea2 = new Area<>(meadowSet2, new ArrayList<>(), 2);

        Set<Zone.Meadow> meadowSet3 = new HashSet<>();
        Collections.addAll(meadowSet3, z84, z560, z150);
        Area<Zone.Meadow> meadowArea3 = new Area<>(meadowSet3, new ArrayList<>(), 5);

        Set<Zone.Meadow> meadowSet4 = new HashSet<>();
        Collections.addAll(meadowSet4, z152, z562);
        Area<Zone.Meadow> meadowArea4 = new Area<>(meadowSet4, new ArrayList<>(), 1);

        Set<Zone.Meadow> meadowSet5 = new HashSet<>();
        Collections.addAll(meadowSet5, z291);
        Area<Zone.Meadow> meadowArea5 = new Area<>(meadowSet5, new ArrayList<>(), 2);

        Set<Zone.Meadow> meadowSet6 = new HashSet<>();
        Collections.addAll(meadowSet6, z293);
        Area<Zone.Meadow> meadowArea6 = new Area<>(meadowSet6, new ArrayList<>(), 2);

        Set<Zone.Meadow> meadowSet7 = new HashSet<>();
        Collections.addAll(meadowSet7, z311);
        Area<Zone.Meadow> meadowArea7 = new Area<>(meadowSet7, new ArrayList<>(), 2);

        Set<Area<Zone.Meadow>> meadowAreas = new HashSet<>();
        Collections.addAll(meadowAreas, meadowArea1, meadowArea2, meadowArea3, meadowArea4, meadowArea5, meadowArea6, meadowArea7);

        assertEquals(meadowAreas, finalBoard.meadowAreas());
    }

    @Test
    void riverSystemsAreasWorks() {
        // Tile 8
        var l81 = new Zone.Lake(8_8, 1, null);
        var a80_0 = new Animal(8_0_0, Animal.Kind.MAMMOTH);
        var z80 = new Zone.Meadow(8_0, List.of(a80_0), null);
        var z81 = new Zone.River(8_1, 0, l81);
        var a82_0 = new Animal(8_2_0, Animal.Kind.DEER);
        var z82 = new Zone.Meadow(8_2, List.of(a82_0), null);
        var z83 = new Zone.River(8_3, 0, l81);
        var z84 = new Zone.Meadow(8_4, List.of(), null);
        var z85 = new Zone.River(8_5, 0, l81);
        var sN8 = new TileSide.River(z80, z81, z82);
        var sE8 = new TileSide.River(z82, z83, z84);
        var sS8 = new TileSide.Meadow(z84);
        var sW8 = new TileSide.River(z84, z85, z80);
        Tile tile8 = new Tile(8, Tile.Kind.NORMAL, sN8, sE8, sS8, sW8);

        // Tile 15
        var a150_0 = new Animal(15_0_0, Animal.Kind.DEER);
        var z150 = new Zone.Meadow(15_0, List.of(a150_0), null);
        var z151 = new Zone.River(15_1, 0, null);
        var z152 = new Zone.Meadow(15_2, List.of(), null);
        var sN15 = new TileSide.Meadow(z150);
        var sE15 = new TileSide.River(z150, z151, z152);
        var sS15 = new TileSide.River(z152, z151, z150);
        var sW15 = new TileSide.Meadow(z150);
        Tile tile15 = new Tile(15, Tile.Kind.NORMAL, sN15, sE15, sS15, sW15);

        // Tile 29
        var z290 = new Zone.Forest(29_0, Zone.Forest.Kind.PLAIN);
        var z291 = new Zone.Meadow(29_1, List.of(), null);
        var z292 = new Zone.River(29_2, 0, null);
        var z293 = new Zone.Meadow(29_3, List.of(), null);
        var sN29 = new TileSide.Forest(z290);
        var sE29 = new TileSide.River(z291, z292, z293);
        var sS29 = new TileSide.River(z293, z292, z291);
        var sW29 = new TileSide.Forest(z290);
        Tile tile29 = new Tile(29, Tile.Kind.NORMAL, sN29, sE29, sS29, sW29);

        // Tile 31
        var z310 = new Zone.Forest(31_0, Zone.Forest.Kind.WITH_MENHIR);
        var a311_0 = new Animal(31_1_0, Animal.Kind.TIGER);
        var z311 = new Zone.Meadow(31_1, List.of(a311_0), null);
        var sN31 = new TileSide.Forest(z310);
        var sE31 = new TileSide.Meadow(z311);
        var sS31 = new TileSide.Meadow(z311);
        var sW31 = new TileSide.Forest(z310);
        Tile tile31 = new Tile(31, Tile.Kind.NORMAL, sN31, sE31, sS31, sW31);

        // Tile 56
        var l561 = new Zone.Lake(56_8, 1, null);
        var a560_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z560 = new Zone.Meadow(56_0, List.of(a560_0), null);
        var z561 = new Zone.Forest(56_1, Zone.Forest.Kind.WITH_MENHIR);
        var z562 = new Zone.Meadow(56_2, List.of(), null);
        var z563 = new Zone.River(56_3, 0, l561);
        var sN56 = new TileSide.Meadow(z560);
        var sE56 = new TileSide.Forest(z561);
        var sS56 = new TileSide.Forest(z561);
        var sW56 = new TileSide.River(z562, z563, z560);
        Tile tile56 = new Tile(56, Tile.Kind.START, sN56, sE56, sS56, sW56);

        Board board = Board.EMPTY;

        Board board1 = board.withNewTile(new PlacedTile(tile56,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,0)));

        Board board2 = board1.withNewTile(new PlacedTile(tile8,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,-1)));

        Board board3 = board2.withNewTile(new PlacedTile(tile29,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(1,0)));

        Board board4 = board3.withNewTile(new PlacedTile(tile15,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(-1,0)));

        Board finalBoard = board4.withNewTile(new PlacedTile(tile31,
                PlayerColor.YELLOW,
                Rotation.NONE,
                new Pos(0,1)));

        Set<Zone.Water> waterSet1 = new HashSet<>();
        Collections.addAll(waterSet1, z81, z83, z85, l81);
        Area<Zone.Water> waterArea1 = new Area<>(waterSet1, new ArrayList<>(), 3);

        Set<Zone.Water> waterSet2 = new HashSet<>();
        Collections.addAll(waterSet2, l561, z563, z151);
        Area<Zone.Water> waterArea2 = new Area<>(waterSet2, new ArrayList<>(), 1);

        Set<Zone.Water> waterSet3 = new HashSet<>();
        Collections.addAll(waterSet3, z292);
        Area<Zone.Water> waterArea3 = new Area<>(waterSet3, new ArrayList<>(), 2);

        Set<Area<Zone.Water>> waterAreas = new HashSet<>();
        Collections.addAll(waterAreas, waterArea1, waterArea2, waterArea3);

        assertEquals(waterAreas, finalBoard.riverSystemAreas());
    }



}