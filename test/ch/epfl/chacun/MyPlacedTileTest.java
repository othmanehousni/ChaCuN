package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyPlacedTileTest {

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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        Set<Zone> expectedZones = new HashSet<>(List.of(meadowZone));
        // Verify that meadowZones() method returns the correct meadow zones
        assertEquals(expectedZones, placedTile.meadowsZones());
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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

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
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
        Set<Occupant> expectedOccupants = new HashSet<>(List.of(new Occupant(Occupant.Kind.PAWN, 11),
                new Occupant(Occupant.Kind.PAWN, 12), new Occupant(Occupant.Kind.PAWN, 13), new Occupant(Occupant.Kind.PAWN, 14),
                new Occupant(Occupant.Kind.HUT, 14), new Occupant(Occupant.Kind.PAWN, 15), new Occupant(Occupant.Kind.PAWN, 16)));

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
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        // Create an occupant
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 11);


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
        PlacedTile placedTileWithOccupant = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(Occupant.Kind.PAWN, 11));
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

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
        PlacedTile placedTileWithOccupant1 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(Occupant.Kind.PAWN, 11));
        PlacedTile placedTileWithOccupant2 = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0),
                new Occupant(Occupant.Kind.HUT, 13));
        PlacedTile placedTileWithoutOccupant = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        // Verify that idOfZoneOccupiedBy() returns the correct zone ID for the given occupant kind
        assertEquals(11, placedTileWithOccupant1.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
        assertEquals(13, placedTileWithOccupant2.idOfZoneOccupiedBy(Occupant.Kind.HUT));
        assertEquals(-1, placedTileWithoutOccupant.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
        assertEquals(-1, placedTileWithOccupant1.idOfZoneOccupiedBy(Occupant.Kind.HUT));


    }
}



