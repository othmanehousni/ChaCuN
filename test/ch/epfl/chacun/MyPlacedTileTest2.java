package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyPlacedTileTest2 {
    @Test
    public void checkIfTileRotationPosIsNotNull() {
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(null, PlayerColor.RED, Rotation.RIGHT, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 2));
        });
    }

    @Test
    public void checkIfRotationIsNotNull() {
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(new Tile(3, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.PLAIN))), PlayerColor.RED, null, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 2));
        });
    }

    @Test
    public void checkIfPosIsNotNull() {
        assertThrows(NullPointerException.class, () -> {
            new PlacedTile(new Tile(3, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.PLAIN))), PlayerColor.RED, Rotation.RIGHT, null, new Occupant(Occupant.Kind.PAWN, 2));
        });
    }

    @Test
    public void checkIfPlacerOrOccupantCanBenull() {
        new PlacedTile(new Tile(3, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), null, Rotation.LEFT, new Pos(0, 0), null);
    }

    ;

    @Test
    public void secondConstructoWorks() {
        PlacedTile test = new PlacedTile(new Tile(3, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
        assertEquals(null, test.occupant());
    }

    @Test
    public void checkId() {
        for (int i = 0; i < 50; i++) {
            PlacedTile test = new PlacedTile(new Tile(i, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
            assertEquals(i, test.id());
        }
    }

    @Test
    public void checkKind() {
        PlacedTile test = new PlacedTile(new Tile(8, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
        assertEquals(Tile.Kind.START, test.kind());
    }

    @Test
    public void checkSide() {
        TileSide test1 = new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR));
        PlacedTile test = new PlacedTile(new Tile(8, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), test1), PlayerColor.GREEN, Rotation.HALF_TURN, new Pos(0, 0));
        assertEquals(test1, test.side(Direction.E));
    }

    @Test
    public void checkZoneWithId() {
        TileSide.Forest test1 = new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        PlacedTile test = new PlacedTile(new Tile(8, Tile.Kind.START, test1, new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
        assertEquals(test1.zones().get(0), test.zoneWithId(4));
    }

    @Test
    public void checkSpecialPowerZone() {
        PlacedTile test1 = new PlacedTile(new Tile(8, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
        assertEquals(null, test1.specialPowerZone());
        PlacedTile test2 = new PlacedTile(new Tile(8, Tile.Kind.START, new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN)), new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));
        //comment tester avec des zones avec des pouvoirs spéciaux???
    }

    @Test
    public void checkForestZone() {
        TileSide.Forest test1 = new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.PLAIN));
        TileSide.Forest test2 = new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.PLAIN));
        TileSide.Forest test3 = new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN));

        PlacedTile test = new PlacedTile(new Tile(8, Tile.Kind.START, test1, test2, test3, new TileSide.Forest(new Zone.Forest(7, Zone.Forest.Kind.WITH_MENHIR))), PlayerColor.GREEN, Rotation.LEFT, new Pos(0, 0));

    }

    //////////
    @Test
    public void testSpecialPowerZone() {
        TileSide side = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide side2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side, side2, side3, side4), PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0));
        assertNull(placedTile.specialPowerZone());
    }

    @Test
    public void testForestZones() {        // Étant donné une tuile placée avec des zones de forê
        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);        Zone.Forest forest2 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest3 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS);        TileSide side = new TileSide.Forest(forest1);
        TileSide side2 = new TileSide.Forest(forest2);        TileSide side3 = new TileSide.Forest(forest3);
        TileSide side4 = new TileSide.Forest(forest1);        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side, side2, side3, side4), PlayerColor.RED, Rotation.NONE, new Pos(0, 0), new Occupant(Occupant.Kind.PAWN, 0));

        Set<Zone.Forest> forests = new HashSet<>();        forests.add(forest1);
        forests.add(forest2);        forests.add(forest3);

        assertEquals(forests,placedTile.forestZones());
    }
    @Test
    void specialPowerZoneWorksOnTrivialPlacedTile() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide northSide = new TileSide.Forest(forest1);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide eastSide = new TileSide.Forest(forest2);
        Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide southSide = new TileSide.Forest(forest3);
        Zone.Forest forest4 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide westSide = new TileSide.Forest(forest4);

        Tile tile = new Tile(40, Tile.Kind.NORMAL, northSide, eastSide, southSide, westSide);

        Rotation rotation = Rotation.HALF_TURN;

        Pos pos = new Pos(1, 1);

        PlayerColor playerColor = PlayerColor.RED;

        PlacedTile placedTile = new PlacedTile(tile, playerColor, rotation, pos,null);

        assertNull(placedTile.specialPowerZone());
    }

    @Test
    void specialPowerZoneWorksOnSpecialPoweredPlacedTile() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide northSide = new TileSide.Forest(forest1);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide eastSide = new TileSide.Forest(forest2);
        Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide southSide = new TileSide.Forest(forest3);
        Zone.Meadow meadow = new Zone.Meadow(4, new ArrayList<Animal>(), Zone.SpecialPower.HUNTING_TRAP);
        TileSide westSide = new TileSide.Meadow(meadow);

        Tile tile = new Tile(40, Tile.Kind.NORMAL, northSide, eastSide, southSide, westSide);

        Rotation rotation = Rotation.HALF_TURN;

        Pos pos = new Pos(1, 1);

        PlayerColor playerColor = PlayerColor.RED;

        PlacedTile placedTile = new PlacedTile(tile, playerColor, rotation, pos,null);

        assertEquals(meadow, placedTile.specialPowerZone());
    }
    @Test
    void forestZonesWorks(){
        Zone.Forest test2 = new Zone.Forest(4, Zone.Forest.Kind.PLAIN);
        Zone.Forest test3 = new Zone.Forest(5, Zone.Forest.Kind.PLAIN);
        Zone.Forest test4 = new Zone.Forest(6, Zone.Forest.Kind.PLAIN);
        Zone.Forest test5 = new Zone.Forest(7, Zone.Forest.Kind.PLAIN);
        Set test0 = new HashSet<Zone.Forest>();
        test0.add(test2);
        test0.add(test3);
        test0.add(test4);
        test0.add(test5);

        PlacedTile test1 = new PlacedTile(new Tile(3, Tile.Kind.START, new TileSide.Forest(test2), new TileSide.Forest(test3), new TileSide.Forest(test4), new TileSide.Forest(test5)), PlayerColor.RED, Rotation.LEFT, new Pos(0, 0), null);
        assertEquals(test0,test1.forestZones());
    }


}