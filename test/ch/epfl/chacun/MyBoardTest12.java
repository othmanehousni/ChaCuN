package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyBoardTest12 {
    @Test
    void isClassValid() {
        assertEquals("ch.epfl.chacun", Board.class.getPackage().getName(), "wrong package location");
        assertTrue(Modifier.isFinal(Board.class.getModifiers()), "Class should be final (#661) ");
        assertTrue(Modifier.isPublic(Board.class.getModifiers()), "Class should be public ");
        assertFalse(Board.class.isInterface(), "Board should not be an interface");
        assertFalse(Board.class.isEnum(), "Board should not be an enum");
        assertFalse(Board.class.isAnnotation(), "Board should not be an annotation");

        assertTrue((Board.class.getConstructors().length == 0), "You must have no public constructor");
        final Constructor<?>[] constructor = Board.class.getDeclaredConstructors();
        assertTrue((Board.class.getDeclaredConstructors().length == 1), "You must have only one constructor");
        assertTrue(Modifier.isPrivate(constructor[0].getModifiers()), "This unique constructor is not private");

        assertEquals(25,
                Arrays.stream(Board.class.getDeclaredMethods())
                        .map(Method::getModifiers)
                        .filter(Modifier::isPublic).count(),
                "You must have created/override only 25 publics method >:(");

        List<Field> fields = List.of(Board.class.getFields());
        assertEquals(2, fields.size(), "You must have only two public fields");
        List<String> fieldNames = fields.stream()
                .map(Field::getName).toList();

        assertTrue(fieldNames.contains("REACH"), "Field REACH is missing or incorrectly named");
        assertTrue(fieldNames.contains("EMPTY"), "Field EMPTY is missing or incorrectly named");
        assertTrue(fields.stream().map(Field::getType).anyMatch(Board.class::equals),
                "Field EMPTY is missing or incorrectly assigned");
        assertTrue(fields.stream().map(Field::getType).anyMatch(int.class::equals),
                "Field REACH is missing or incorrectly assigned");
        assertEquals(12, Board.REACH);

        for (Field field : fields) {
            assertTrue(Modifier.isStatic(field.getModifiers()), "All the public fields should be static");
            assertTrue(Modifier.isFinal(field.getModifiers()), "All the public fields should be final");
        }
        List<Type> types = Arrays.stream(Board.class.getDeclaredFields())
                .filter(t -> Modifier.isPrivate(t.getModifiers()) && Modifier.isFinal(t.getModifiers()))
                .map(Field::getGenericType)
                .toList();
        assertTrue(4 <= types.size(),
                "The 4 private attribute that you initialise in the constructor should be final");
        assertTrue(types.contains(int[].class),
                "The index Array should be private and final but i can't find it");
        assertTrue(types.contains(ZonePartitions.class),
                "The Zone partitions should be private and final but i can't find it");
        assertTrue(types.contains(PlacedTile[].class),
                "The PlacedTile Array should be private and final but i can't find it");
        List<Type> AnimalSet = types.stream()
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .filter(a -> a.getRawType().equals(Set.class))
                .filter(t -> t.getActualTypeArguments().length != 0)
                .map(t -> t.getActualTypeArguments()[0])
                .toList();
        assertTrue(AnimalSet.contains(Animal.class),
                "The Cancelled Animal Set should be private and final but i can't find it");
    }

    @Test
    void tileAtWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);

        Tile tile = new Tile(46, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        PlacedTile placedTile1 =
                new PlacedTile(tile, null, Rotation.RIGHT, new Pos(0, 0));
        PlacedTile placedTile11 =
                new PlacedTile(tile, null, Rotation.LEFT, new Pos(1, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile, null, Rotation.NONE, new Pos(12, 12));
        PlacedTile placedTile3 =
                new PlacedTile(tile, null, Rotation.NONE, new Pos(-12, -12));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile11);
        Board board2 = Board.EMPTY.withNewTile(placedTile2);
        Board board3 = Board.EMPTY.withNewTile(placedTile3);
        assertEquals(placedTile1, board1.tileAt(new Pos(0, 0)));
        assertEquals(placedTile11, board1.tileAt(new Pos(1, 0)));
        assertEquals(placedTile2, board2.tileAt(new Pos(12, 12)));
        assertEquals(placedTile3, board3.tileAt(new Pos(-12, -12)));
        assertNotEquals(Board.EMPTY, board3);
        assertNull(board2.tileAt(new Pos(0, 0)));
        assertNull(board1.tileAt(new Pos(12, 12)));
        assertNull(board1.tileAt(new Pos(-12, -12)));
        assertNull(board2.tileAt(new Pos(0, 1)));
        assertNull(board1.tileAt(new Pos(13, 12)));
    }

    @Test
    void tileWithIdWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(46, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(47, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, null, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, null, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        assertEquals(placedTile1, board1.tileWithId(46));
        assertEquals(placedTile2, board1.tileWithId(47));
        assertThrows(IllegalArgumentException.class, () -> board1.tileWithId(21));
    }

    @Test
    void cancelledAnimalsWorking() {
        Board board = Board.EMPTY;
        assertEquals(Set.of(), board.cancelledAnimals());
        Set<Animal> animals = board.cancelledAnimals();
        assertThrows(UnsupportedOperationException.class, animals::clear);
    }

    @Test
    void occupantsWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        assertTrue(board1.occupants().isEmpty() && board2.occupants().isEmpty());
        Board board1After = board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        assertEquals(board1After.occupants(), Set.of(new Occupant(Occupant.Kind.PAWN, 10)));
        assertTrue(board1.occupants().isEmpty() && board2.occupants().isEmpty());
    }

    @Test
    void forestAreaWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Forest> forestArea1 = board1.forestArea(new Zone.Forest(13, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestArea2 = board1.forestArea(new Zone.Forest(23, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestArea3 = board2.forestArea(new Zone.Forest(13, Zone.Forest.Kind.PLAIN));
        assertEquals(forestArea1, forestArea3);
        assertNotEquals(forestArea1, forestArea2);
        assertEquals(Set.of(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)), forestArea1.zones());
        assertEquals(Set.of(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)), forestArea2.zones());
        assertThrows(IllegalArgumentException.class, () ->
                board1.forestArea(new Zone.Forest(3, Zone.Forest.Kind.PLAIN)));
    }

    @Test
    void meadowAreaWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Meadow> meadowArea1 = board1.meadowArea(meadow0);
        Area<Zone.Meadow> meadowArea2 = board1.meadowArea(meadow3);
        Area<Zone.Meadow> meadowArea3 = board2.meadowArea(meadow0);
        assertEquals(meadowArea1, meadowArea2);
        assertNotEquals(meadowArea1, meadowArea3);
        assertEquals(Set.of(meadow0, meadow3), meadowArea1.zones());
        assertThrows(IllegalArgumentException.class, () ->
                board2.meadowArea(meadow3));
    }

    @Test
    void riverAreaWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.River> riverArea1 = board1.riverArea(river1);
        Area<Zone.River> riverArea2 = board1.riverArea(river2);
        Area<Zone.River> riverArea3 = board2.riverArea(river1);
        assertEquals(riverArea1, riverArea2);
        assertNotEquals(riverArea3, riverArea1);
        assertEquals(Set.of(river1, river2), riverArea1.zones());
        assertEquals(Set.of(river1), riverArea3.zones());
        assertThrows(IllegalArgumentException.class, () ->
                board2.riverArea(river2));
    }

    @Test
    void riverSystemAreaWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, new Zone.Lake(19, 1, null));
        Zone.River river3 = new Zone.River(27, 0, new Zone.Lake(19, 1, null));
        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river3, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Water> riverSystemArea1 = board1.riverSystemArea(river1);
        Area<Zone.Water> riverSystemArea2 = board1.riverSystemArea(river2.lake());
        Area<Zone.Water> riverSystemArea3 = board1.riverSystemArea(river3);
        Area<Zone.Water> riverSystemArea4 = board2.riverSystemArea(river1);
        assertEquals(riverSystemArea1, riverSystemArea2);
        assertEquals(riverSystemArea1, riverSystemArea3);
        assertNotEquals(riverSystemArea1, riverSystemArea4);
        assertEquals(Set.of(river1, river2, river3, river2.lake()), riverSystemArea1.zones());
        assertEquals(Set.of(river1), riverSystemArea4.zones());
        assertThrows(IllegalArgumentException.class, () ->
                board2.riverSystemArea(river2.lake()));
        assertThrows(IllegalArgumentException.class, () ->
                board2.riverSystemArea(river2));
    }

    @Test
    void meadowAreasWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Meadow> meadowArea1 = board1.meadowArea(meadow0);
        Area<Zone.Meadow> meadowArea2 = board1.meadowArea(meadow4);
        Area<Zone.Meadow> meadowArea3 = board2.meadowArea(meadow0);
        Area<Zone.Meadow> meadowArea4 = board2.meadowArea(meadow2);
        assertEquals(Set.of(meadowArea1, meadowArea2), board1.meadowAreas());
        assertEquals(Set.of(meadowArea3, meadowArea4), board2.meadowAreas());
        assertEquals(Set.of(), Board.EMPTY.meadowAreas());
    }

    @Test
    void riverSystemAreasWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, new Zone.Lake(19, 1, null));
        Zone.River river3 = new Zone.River(27, 0, new Zone.Lake(19, 1, null));
        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river3, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Water> riverSystemArea1 = board1.riverSystemArea(river1);
        Area<Zone.Water> riverSystemArea2 = board2.riverSystemArea(river1);
        assertEquals(Set.of(riverSystemArea1), board1.riverSystemAreas());
        assertEquals(Set.of(riverSystemArea2), board2.riverSystemAreas());
        assertEquals(Set.of(), Board.EMPTY.riverSystemAreas());
    }

    @Test
    void adjacentMeadowWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(35, List.of(), null);
        Zone.Meadow meadow6 = new Zone.Meadow(36, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);
        Zone.River river3 = new Zone.River(32, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        Tile tile3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow5),
                new TileSide.River(meadow5, river3, meadow6),
                new TileSide.Forest(new Zone.Forest(33, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow6, river3, meadow5));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile3 =
                new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(2, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);
        Board board2 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board3 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Meadow> meadowArea1 = board1.adjacentMeadow(new Pos(0, 0), meadow0);
        Area<Zone.Meadow> meadowArea2 = board2.adjacentMeadow(new Pos(0, 0), meadow0);
        Area<Zone.Meadow> meadowArea3 = board2.meadowArea(meadow0);
        Area<Zone.Meadow> meadowArea4 = board3.adjacentMeadow(new Pos(0, 0), meadow0);
        Area<Zone.Meadow> meadowArea5 = board3.meadowArea(meadow0);
        assertEquals(meadowArea1, meadowArea2);
        assertEquals(meadowArea1.zones(), meadowArea3.zones());
        assertEquals(meadowArea4.zones(), meadowArea5.zones());
        assertThrows(IllegalArgumentException.class, () ->
                Board.EMPTY.adjacentMeadow(new Pos(0, 0), meadow0));
    }

    @Test
    void occupantCountWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board1After = board1
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 10))
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 22));
        assertEquals(1, board1After.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(1, board1After.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
        assertEquals(0, board1After.occupantCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, board1After.occupantCount(PlayerColor.GREEN, Occupant.Kind.PAWN));
        assertEquals(0, board1After.occupantCount(PlayerColor.PURPLE, Occupant.Kind.PAWN));
        assertEquals(0, board1After.occupantCount(PlayerColor.BLUE, Occupant.Kind.HUT));
        assertEquals(0, board1After.occupantCount(PlayerColor.RED, Occupant.Kind.HUT));
    }

    @Test
    void insertionPositionsWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.Meadow meadow5 = new Zone.Meadow(35, List.of(), null);
        Zone.Meadow meadow6 = new Zone.Meadow(36, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);
        Zone.River river3 = new Zone.River(32, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        Tile tile3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow5),
                new TileSide.River(meadow5, river3, meadow6),
                new TileSide.Forest(new Zone.Forest(33, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow6, river3, meadow5));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        PlacedTile placedTile3 =
                new PlacedTile(tile3, PlayerColor.RED, Rotation.NONE, new Pos(2, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2).withNewTile(placedTile3);
        Board board2 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board3 = Board.EMPTY.withNewTile(placedTile1);
        Pos pos1 = new Pos(-1, 0);
        Pos pos2 = new Pos(0, -1);
        Pos pos3 = new Pos(0, 1);
        Pos pos4 = new Pos(1, -1);
        Pos pos5 = new Pos(1, 0);
        Pos pos6 = new Pos(1, 1);
        Pos pos7 = new Pos(2, -1);
        Pos pos8 = new Pos(2, 0);
        Pos pos9 = new Pos(2, 1);
        Pos pos10 = new Pos(3, 0);
        Set<Pos> posSet1 = Set.of(pos1, pos2, pos3, pos4, pos6, pos7, pos9, pos10);
        Set<Pos> posSet2 = Set.of(pos1, pos2, pos3, pos4, pos6, pos8);
        Set<Pos> posSet3 = Set.of(pos1, pos2, pos3, pos5);
        assertEquals(posSet1, board1.insertionPositions());
        assertEquals(posSet2, board2.insertionPositions());
        assertEquals(posSet3, board3.insertionPositions());
    }

    @Test
    void lastPlacedTileWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(46, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(47, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, null, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, null, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY;
        assertNull(board1.lastPlacedTile());
        assertEquals(placedTile1, board1.withNewTile(placedTile1).lastPlacedTile());
        assertEquals(placedTile2, board1.withNewTile(placedTile2).lastPlacedTile());
    }

    @Test
    void forestsClosedByLastTileWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.River(meadow4, river2, meadow3),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.Forest> forestArea1 = board1.forestArea(new Zone.Forest(13, Zone.Forest.Kind.PLAIN));
        assertEquals(Set.of(forestArea1), board1.forestsClosedByLastTile());
        assertEquals(Set.of(), board2.forestsClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Meadow(meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Meadow(meadow3),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.LEFT, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(1, 0));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile2);
        Board board2 = Board.EMPTY.withNewTile(placedTile1);
        Area<Zone.River> riverArea = board1.riverArea(river1);
        assertEquals(Set.of(riverArea), board1.riversClosedByLastTile());
        assertEquals(Set.of(), board2.riversClosedByLastTile());
    }

    @Test
    void canAddTileWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Meadow(meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Meadow(meadow3),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.LEFT, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(1, 0));
        PlacedTile placedTile3 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(11, 0));
        PlacedTile placedTile4 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(1111, 0));
        Board board = Board.EMPTY.withNewTile(placedTile1);

        assertTrue(board.canAddTile(placedTile2));
        assertFalse(board.canAddTile(placedTile3));
        assertFalse(board.canAddTile(placedTile4));
    }

    @Test
    void couldPlaceTileWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Meadow(meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Meadow(meadow3),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)));
        Tile tile3 = new Tile(3, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(31, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(31, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(31, Zone.Forest.Kind.PLAIN)),
                new TileSide.Forest(new Zone.Forest(31, Zone.Forest.Kind.PLAIN)));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.LEFT, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(1, 0));
        Board board = Board.EMPTY.withNewTile(placedTile1);

        assertTrue(board.couldPlaceTile(placedTile2.tile()));
        assertFalse(board.couldPlaceTile(tile3));
        assertTrue(board.withNewTile(placedTile2).couldPlaceTile(tile3));
    }

    @Test
    void withNewTileThrows() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Meadow(meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Meadow(meadow3),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.LEFT, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.HALF_TURN, new Pos(11, 0));
        Board board = Board.EMPTY.withNewTile(placedTile1);

        assertThrows(IllegalArgumentException.class, () -> board.withNewTile(placedTile2));
    }

    @Test
    void withOccupantThrows() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        assertThrows(IllegalArgumentException.class, () ->
                board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 10)));
        assertThrows(IllegalArgumentException.class, () ->
                board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 12)));
        assertThrows(IllegalArgumentException.class, () ->
                board1.withOccupant(new Occupant(Occupant.Kind.PAWN, 11)));
    }

    @Test
    void withoutOccupantWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        assertEquals(Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2), board1.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 10)));
    }

    @Test
    void withoutGatherersOrFishersInWorking() {
        Zone.Meadow meadow0 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(12, List.of(), null);
        Zone.Meadow meadow3 = new Zone.Meadow(25, List.of(), null);
        Zone.Meadow meadow4 = new Zone.Meadow(26, List.of(), null);
        Zone.River river1 = new Zone.River(11, 0, null);
        Zone.River river2 = new Zone.River(22, 0, null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow0),
                new TileSide.River(meadow0, river1, meadow2),
                new TileSide.Forest(new Zone.Forest(13, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow2, river1, meadow0));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow3),
                new TileSide.River(meadow3, river2, meadow4),
                new TileSide.Forest(new Zone.Forest(23, Zone.Forest.Kind.PLAIN)),
                new TileSide.River(meadow4, river2, meadow3));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, PlayerColor.BLUE, Rotation.NONE, new Pos(0, 0));
        PlacedTile placedTile2 =
                new PlacedTile(tile2, PlayerColor.RED, Rotation.NONE, new Pos(1, 0));
        Board board1 = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 13))
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 23));
        Area<Zone.Forest> forestArea = board1.forestArea(new Zone.Forest(13, Zone.Forest.Kind.PLAIN));
        assertEquals(Board.EMPTY
                        .withNewTile(placedTile1)
                        .withNewTile(placedTile2)
                        .withOccupant(new Occupant(Occupant.Kind.PAWN, 23))
                , board1.withoutGatherersOrFishersIn(Set.of(forestArea), Set.of()));
        board1 = Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2)
                .withOccupant(new Occupant(Occupant.Kind.PAWN, 11));
        Area<Zone.River> riverArea = board1.riverArea(river1);
        assertEquals(Board.EMPTY
                .withNewTile(placedTile1)
                .withNewTile(placedTile2), board1.withoutGatherersOrFishersIn(Set.of(), Set.of(riverArea)));

    }

    @Test
    void withMoreCancelledAnimalsWorking() {
        Board board = Board.EMPTY;
        Set<Animal> animals1 = Set.of(new Animal(1, Animal.Kind.TIGER));
        Set<Animal> animals2 = Set.of(new Animal(2, Animal.Kind.DEER));
        Set<Animal> animals3 = Set.of(new Animal(2, Animal.Kind.DEER), new Animal(1, Animal.Kind.TIGER));
        assertEquals(Set.of(), board.withMoreCancelledAnimals(Set.of()).cancelledAnimals());
        assertEquals(animals1, board.withMoreCancelledAnimals(animals1).cancelledAnimals());
        assertEquals(animals2, board.withMoreCancelledAnimals(animals2).cancelledAnimals());
        assertEquals(animals3, board.withMoreCancelledAnimals(animals1)
                .withMoreCancelledAnimals(animals2).cancelledAnimals());
    }

    @Test
    void hashCodeWorking() {
        Zone.Meadow meadow1 = new Zone.Meadow(10, List.of(), null);
        Zone.Meadow meadow2 = new Zone.Meadow(20, List.of(), null);

        Tile tile1 = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow1),
                new TileSide.Meadow(meadow1),
                new TileSide.Meadow(meadow1),
                new TileSide.Meadow(meadow1));
        Tile tile2 = new Tile(2, Tile.Kind.NORMAL,
                new TileSide.Meadow(meadow2),
                new TileSide.Meadow(meadow2),
                new TileSide.Meadow(meadow2),
                new TileSide.Meadow(meadow2));
        PlacedTile placedTile1 =
                new PlacedTile(tile1, null, Rotation.RIGHT, new Pos(-11, -12));
        PlacedTile placedTile11 =
                new PlacedTile(tile2, null, Rotation.LEFT, new Pos(-10, -12));
        Board board1 = Board.EMPTY.withNewTile(placedTile1).withNewTile(placedTile11)
                .withMoreCancelledAnimals(Set.of(new Animal(123, Animal.Kind.TIGER)));
        PlacedTile[] placedTiles = new PlacedTile[625];
        placedTiles[1] = placedTile1;
        placedTiles[2] = placedTile11;
        int[] index = new int[2];
        index[0] = 1;
        index[1] = 2;
        ZonePartitions.Builder builder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        builder.addTile(tile1);
        builder.addTile(tile2);
        builder.connectSides(new TileSide.Meadow(meadow1), new TileSide.Meadow(meadow2));
        builder.build();
        assertEquals(
                Objects.hash(
                        Arrays.hashCode(placedTiles),
                        Arrays.hashCode(index),
                        builder.build(),
                        Set.of(new Animal(123, Animal.Kind.TIGER))), board1.hashCode(),
                "Soit les attributs sont mauvaise soit vous n'avez pas le même ordre dans votre méthode," +
                        "Ce n'est pas un problème en soit et vous pouvez juste changer l'ordre dans l'assertion #847");
    }
}