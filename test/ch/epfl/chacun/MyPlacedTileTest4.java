package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.Occupant.Kind.PAWN;
import static ch.epfl.chacun.PlayerColor.RED;
import static ch.epfl.chacun.Pos.ORIGIN;
import static ch.epfl.chacun.Rotation.HALF_TURN;
import static org.junit.jupiter.api.Assertions.*;

public class MyPlacedTileTest4 {
    List<Animal> animals;
    Zone.Lake lake;
    Zone.River river1;
    Zone.River river2;
    Zone.Meadow meadow1;
    Zone.Meadow meadow2;
    Zone.Forest forest1;
    Zone.Forest forest2;

    @BeforeEach
    void setUp() {
        animals = List.of(new Animal(0, Animal.Kind.TIGER));
        meadow1 = new Zone.Meadow(1, animals, Zone.SpecialPower.PIT_TRAP);
        meadow2 = new Zone.Meadow(2, animals, null);
        forest1 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        forest2 = new Zone.Forest(4, Zone.Forest.Kind.PLAIN);
        lake = new Zone.Lake(5,5, Zone.SpecialPower.PIT_TRAP);
        river1 = new Zone.River(6, 0, null);
        river2 = new Zone.River(7, 0, lake);
    }

    @Test
    void testPlacedTileConstructor() {
        // Test constructor with valid arguments
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertNotNull(placedTile);
        assertEquals(tile, placedTile.tile());
        assertEquals(placer, placedTile.placer());
        assertEquals(rotation, placedTile.rotation());
        assertEquals(pos, placedTile.pos());
        assertEquals(occupant, placedTile.occupant());
    }

    @Test
    void testPlacedTileSecondaryConstructor() {
        // Test secondary constructor
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos);

        assertNotNull(placedTile);
        assertEquals(tile, placedTile.tile());
        assertEquals(placer, placedTile.placer());
        assertEquals(rotation, placedTile.rotation());
        assertEquals(pos, placedTile.pos());
        assertNull(placedTile.occupant());
    }

    @Test
    void testPlacedTileId() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertEquals(tile.id(), placedTile.id());
    }

    @Test
    void testPlacedTileKind() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertEquals(tile.kind(), placedTile.kind());
    }

    @Test
    void testPlacedTileSide() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.Forest(forest2), new TileSide.River(meadow1,river1,meadow2));
        PlayerColor placer = RED;
        Rotation noneRotation = Rotation.NONE;
        Rotation rightRotation = Rotation.RIGHT;
        Rotation halfTurnRotation = HALF_TURN;
        Rotation leftRotation = Rotation.LEFT;

        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        // None rotation
        PlacedTile placedTileWithNoneRotation = new PlacedTile(tile, placer, noneRotation, pos, occupant);

        assertEquals(tile.n(), placedTileWithNoneRotation.side(Direction.N));
        assertEquals(tile.e(), placedTileWithNoneRotation.side(Direction.E));
        assertEquals(tile.s(), placedTileWithNoneRotation.side(Direction.S));
        assertEquals(tile.w(), placedTileWithNoneRotation.side(Direction.W));

        // Right rotation
        /*PlacedTile placedTileWithRightRotation = new PlacedTile(tile, placer, rightRotation, pos, occupant);

        assertEquals(tile.n(), placedTileWithRightRotation.side(Direction.W));
        assertEquals(tile.e(), placedTileWithRightRotation.side(Direction.N));
        assertEquals(tile.s(), placedTileWithRightRotation.side(Direction.E));
        assertEquals(tile.w(), placedTileWithRightRotation.side(Direction.S));

        // HalfTurn rotation
        PlacedTile placedTileWithHalfTurnRotation = new PlacedTile(tile, placer, halfTurnRotation, pos, occupant);

        assertEquals(tile.n(), placedTileWithHalfTurnRotation.side(Direction.S));
        assertEquals(tile.e(), placedTileWithHalfTurnRotation.side(Direction.W));
        assertEquals(tile.s(), placedTileWithHalfTurnRotation.side(Direction.N));
        assertEquals(tile.w(), placedTileWithHalfTurnRotation.side(Direction.E));

        // Left rotation
        PlacedTile placedTileWithLeftRotation = new PlacedTile(tile, placer, leftRotation, pos, occupant);

        assertEquals(tile.n(), placedTileWithLeftRotation.side(Direction.E));
        assertEquals(tile.e(), placedTileWithLeftRotation.side(Direction.S));
        assertEquals(tile.s(), placedTileWithLeftRotation.side(Direction.W));
        assertEquals(tile.w(), placedTileWithLeftRotation.side(Direction.N));*/
    }

    @Test
    void testPlacedTileZoneWithId() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertThrows(IllegalArgumentException.class, () -> placedTile.zoneWithId(10));
        assertEquals(1, placedTile.zoneWithId(1).id());
    }

    @Test
    void testPlacedTileSpecialPowerZone() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow2), new TileSide.Forest(forest1), new TileSide.Forest(forest2), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertNull(placedTile.specialPowerZone());

        // Create a tile with a special power zone
        Tile tileWithSpecialPowerWithRiver = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest2), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.River(meadow1, river2, meadow1));
        Tile tileWithSpecialPowerWithOutRiver = new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2));

        PlacedTile placedTileWithRiverWithSpecialPower = new PlacedTile(tileWithSpecialPowerWithRiver, placer, rotation, pos, occupant);
        PlacedTile placedTileWithOutRiverWithSpecialPower = new PlacedTile(tileWithSpecialPowerWithOutRiver, placer, rotation, pos, occupant);

        assertNotNull(placedTileWithRiverWithSpecialPower.specialPowerZone());
        assertNotNull(placedTileWithOutRiverWithSpecialPower.specialPowerZone());
    }

    @Test
    void testPlacedTileForestZones() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow2), new TileSide.Meadow(meadow1), new TileSide.River(meadow1,river1, meadow2), new TileSide.Meadow(meadow2));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertEquals(0, placedTile.forestZones().size());

        // Create a tile with forest zones
        Tile tileWithForests = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest2), new TileSide.Forest(forest2), new TileSide.River(meadow1, river1, meadow2));
        PlacedTile placedTileWithForests = new PlacedTile(tileWithForests, placer, rotation, pos, occupant);

        assertEquals(2, placedTileWithForests.forestZones().size());
    }

    @Test
    void testPlacedTileMeadowZones() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest2), new TileSide.Forest(forest1), new TileSide.Forest(forest2), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertEquals(0, placedTile.meadowZones().size());

        // Create a tile with meadow zones
        Tile tileWithMeadows = new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Forest(forest2), new TileSide.Meadow(meadow2));
        Tile tileWithMeadowsInRiver = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest2), new TileSide.River(meadow1, river2, meadow2));
        PlacedTile placedTileWithMeadows = new PlacedTile(tileWithMeadows, placer, rotation, pos, occupant);
        PlacedTile placedTileWithMeadowsInRiver = new PlacedTile(tileWithMeadowsInRiver, placer, rotation, pos, occupant);

        assertEquals(2, placedTileWithMeadows.meadowZones().size());
        assertEquals(2, placedTileWithMeadowsInRiver.meadowZones().size());
    }

    @Test
    void testPlacedTileRiverZones() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant = new Occupant(PAWN, 1);

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, occupant);

        assertEquals(0, placedTile.riverZones().size());

        // Create a tile with river zones
        Tile tileWithRiver = new Tile(2, Tile.Kind.NORMAL, new TileSide.River(meadow1, river1, meadow2), new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.River(meadow2,river2,meadow1));
        PlacedTile placedTileWithRiver = new PlacedTile(tileWithRiver, placer, rotation, pos, occupant);

        assertEquals(2, placedTileWithRiver.riverZones().size());
    }

    @Test
    void testPlacedTilePotentialOccupants() {
        Tile tileStart = new Tile(1, Tile.Kind.START, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.River(meadow1,river1,meadow2), new TileSide.Forest(forest2));
        Tile tileWithOutLake = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.River(meadow1,river1,meadow2), new TileSide.Forest(forest2));
        Tile tileWithLake = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.River(meadow1,river2,meadow2), new TileSide.Forest(forest2));

        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);

        PlacedTile placedTileWithOutLake = new PlacedTile(tileWithOutLake, placer, rotation, pos);
        PlacedTile placedTileWithLake = new PlacedTile(tileWithLake, placer, rotation, pos);
        PlacedTile placedTileStart = new PlacedTile(tileStart, null, rotation, pos);

        // tile start n'a pas d'occupants
        assertEquals(0, placedTileStart.potentialOccupants().size());
        // riviere n'a pas de lake
        assertEquals(6, placedTileWithOutLake.potentialOccupants().size());
        // riviere avec un lake
        assertEquals(6, placedTileWithLake.potentialOccupants().size());

        // Create a tile with potential occupants
        Tile tileWithOccupants = new Tile(2, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.River(meadow1, river2, meadow2));
        Occupant occupant = new Occupant(PAWN,1);
        PlacedTile placedTileWithOccupants = new PlacedTile(tileWithOccupants, placer, rotation, pos,occupant);

        assertEquals(5, placedTileWithOccupants.potentialOccupants().size());
    }

    @Test
    void testPlacedTileWithOccupant() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant1 = new Occupant(PAWN, meadow1.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, forest1.id());

        PlacedTile placedTileWithOccupant = new PlacedTile(tile, placer, rotation, pos, occupant1);
        assertThrows(IllegalArgumentException.class, () -> placedTileWithOccupant.withOccupant(occupant2));

        PlacedTile placedTileWithOutOccupant = new PlacedTile(tile, placer, rotation, pos, null);
        PlacedTile updatedTile = placedTileWithOutOccupant.withOccupant(occupant1);

        assertNotNull(updatedTile);
        assertEquals(tile, updatedTile.tile());
        assertEquals(placer, updatedTile.placer());
        assertEquals(rotation, updatedTile.rotation());
        assertEquals(pos, updatedTile.pos());
        assertEquals(occupant1, updatedTile.occupant());
    }

    @Test
    void testPlacedTileWithNoOccupant() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1), new TileSide.Forest(forest1));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant1 = new Occupant(PAWN, 1);

        // placed tuile avec un occupant au depart
        PlacedTile placedTileWithOccupantAtFirst = new PlacedTile(tile, placer, rotation, pos, occupant1);
        PlacedTile updatedTileWithOccupantAtFirst = placedTileWithOccupantAtFirst.withNoOccupant();

        assertNotNull(updatedTileWithOccupantAtFirst);
        assertEquals(tile, updatedTileWithOccupantAtFirst.tile());
        assertEquals(placer, updatedTileWithOccupantAtFirst.placer());
        assertEquals(rotation, updatedTileWithOccupantAtFirst.rotation());
        assertEquals(pos, updatedTileWithOccupantAtFirst.pos());
        assertNull(updatedTileWithOccupantAtFirst.occupant());

        // placed tuile avec un occupant au depart
        PlacedTile placedTileWithOutOccupantAtFirst = new PlacedTile(tile, placer, rotation, pos, occupant1);

        PlacedTile updatedTileWithOutOccupantAtFirst = placedTileWithOutOccupantAtFirst.withNoOccupant();

        assertNotNull(updatedTileWithOutOccupantAtFirst);
        assertEquals(tile, updatedTileWithOutOccupantAtFirst.tile());
        assertEquals(placer, updatedTileWithOutOccupantAtFirst.placer());
        assertEquals(rotation, updatedTileWithOutOccupantAtFirst.rotation());
        assertEquals(pos, updatedTileWithOutOccupantAtFirst.pos());
        assertNull(updatedTileWithOutOccupantAtFirst.occupant());
    }

    @Test
    void testPlacedTileIdOfZoneOccupiedBy() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, new TileSide.Forest(forest1), new TileSide.Meadow(meadow1), new TileSide.Forest(forest2), new TileSide.Meadow(meadow2));
        PlayerColor placer = RED;
        Rotation rotation = Rotation.NONE;
        Pos pos = new Pos(0, 0);
        Occupant occupant1 = new Occupant(PAWN, forest1.id());
        Occupant occupant2 = new Occupant(Occupant.Kind.HUT, meadow1.id());

        PlacedTile placedTile = new PlacedTile(tile, placer, rotation, pos, null);
        PlacedTile placedTile1 = new PlacedTile(tile, placer, rotation, pos, occupant1);
        PlacedTile placedTile2 = new PlacedTile(tile, placer, rotation, pos, occupant2);

        assertEquals(-1, placedTile.idOfZoneOccupiedBy(PAWN));
        assertEquals(forest1.id(), placedTile1.idOfZoneOccupiedBy(PAWN));
        assertEquals(meadow1.id(), placedTile2.idOfZoneOccupiedBy(Occupant.Kind.HUT));

        // Test when tile is not occupied or the occupant kind is different
        PlacedTile unoccupiedTile = new PlacedTile(tile, placer, rotation, pos);
        assertEquals(-1, unoccupiedTile.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }

    @Test
    void testId() {
        // Create a tile
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Verify that id() method returns the correct tile ID
        assertEquals(1, placedTile.id());
    }

    @Test
    void testKind() {
        // Create a tile
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Verify that kind() method returns the correct tile kind
        assertEquals(Tile.Kind.NORMAL, placedTile.kind());
    }


    @Test
    void testForestZones() {
        // Create a tile with forest zones
        Zone.Forest f1 = new Zone.Forest(11, Zone.Forest.Kind.PLAIN);
        Zone.Forest f2 = new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR);

        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(f1),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(f2));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));
        Set<Zone> expectedZones = new HashSet<>(List.of(f1, f2));


        // Verify that forestZones() method returns the correct forest zones
        assertEquals(expectedZones, placedTile.forestZones());
    }

    @Test
    void testMeadowZones() {
        // Create a tile with meadow zones
        Zone.Meadow meadowZone = new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(meadowZone),
                new TileSide.River(meadowZone,
                        new Zone.River(14, 3, null),
                        meadowZone),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));
        Set<Zone> expectedZones = new HashSet<>(List.of(meadowZone));
        // Verify that meadowZones() method returns the correct meadow zones
        assertEquals(expectedZones, placedTile.meadowZones());
    }

    @Test
    void testRiverZones() {
        // Create a tile with river zones
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Verify that riverZones() method returns the correct river zones
        assertEquals(1, placedTile.riverZones().size());
    }

    @Test
    void testPotentialOccupants() {

        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile
        PlacedTile placedTile = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));
        Set<Occupant> expectedOccupants = new HashSet<>(List.of(new Occupant(PAWN, 11),
                new Occupant(PAWN, 12), new Occupant(PAWN, 13), new Occupant(PAWN, 14),
                new Occupant(Occupant.Kind.HUT, 14), new Occupant(PAWN, 15), new Occupant(PAWN, 16)));

        // Verify that potentialOccupants() method returns the correct potential occupants
        assertEquals(expectedOccupants, placedTile.potentialOccupants());
    }

    @Test
    void testWithOccupant() {
        // Create a tile
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile without an occupant
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Create an occupant
        Occupant occupant = new Occupant(PAWN, 11);


        // Add occupant to the placed tile
        PlacedTile placedTileWithOccupant = placedTileWithoutOccupant.withOccupant(occupant);

        // Verify that the placed tile has the correct occupant
        assertEquals(occupant, placedTileWithOccupant.occupant());
        assertThrows(IllegalArgumentException.class, () -> placedTileWithOccupant.withOccupant(occupant));
    }

    @Test
    void testWithNoOccupant() {
        // Create a tile
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)), new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile with an occupant
        PlacedTile placedTileWithOccupant = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(PAWN, 11));
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Remove occupant from the placed tile
        PlacedTile f1 = placedTileWithOccupant.withNoOccupant();
        PlacedTile f2 = placedTileWithoutOccupant.withNoOccupant();


        // Verify that the placed tile has no occupant
        assertNull(f1.occupant());
        assertNull(f2.occupant());
    }

    @Test
    void testIdOfZoneOccupiedBy() {
        // Create a tile
        Tile tile = new Tile(1, Tile.Kind.NORMAL,
                new TileSide.Forest(new Zone.Forest(11, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(12, new ArrayList<>(), Zone.SpecialPower.SHAMAN)),
                new TileSide.River(new Zone.Meadow(13, new ArrayList<>(), null),
                        new Zone.River(14, 3, null),
                        new Zone.Meadow(15, new ArrayList<>(), null)),
                new TileSide.Forest(new Zone.Forest(16, Zone.Forest.Kind.WITH_MENHIR)));

        // Create a placed tile with an occupant
        PlacedTile placedTileWithOccupant1 = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(PAWN, 11));
        PlacedTile placedTileWithOccupant2 = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(Occupant.Kind.HUT, 13));
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, RED, Rotation.NONE, new Pos(0, 0));

        // Verify that idOfZoneOccupiedBy() returns the correct zone ID for the given occupant kind
        assertEquals(11, placedTileWithOccupant1.idOfZoneOccupiedBy(PAWN));
        assertEquals(13, placedTileWithOccupant2.idOfZoneOccupiedBy(Occupant.Kind.HUT));
        assertEquals(-1, placedTileWithoutOccupant.idOfZoneOccupiedBy(PAWN));
        assertEquals(-1, placedTileWithOccupant1.idOfZoneOccupiedBy(Occupant.Kind.HUT));
    }
}



