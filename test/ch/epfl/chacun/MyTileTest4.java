package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe de Test de l'enregistrement Tile.
 *
 * @author Salah Farhat (372635)
 * @author Hedi kallala (381750)
 */
class MyTileTest4 {

    @Test
    void tillKindAllIsCorrectlyDefined() {
        assertEquals(0, Tile.Kind.START.ordinal());
        assertEquals(1, Tile.Kind.NORMAL.ordinal());
        assertEquals(2, Tile.Kind.MENHIR.ordinal());
    }

    @Test
    void testTileSides() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(0, Animal.Kind.TIGER));

        TileSide.Forest forestSide = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide.Meadow meadowSide = new TileSide.Meadow(new Zone.Meadow(1, animals, Zone.SpecialPower.PIT_TRAP));
        TileSide.River riverSide = new TileSide.River(new Zone.Meadow(2, animals, Zone.SpecialPower.RAFT), new Zone.River(3, 0, null), new Zone.Meadow(4, animals, Zone.SpecialPower.LOGBOAT));

        Tile tile = new Tile(0, Tile.Kind.NORMAL, forestSide, meadowSide, riverSide, forestSide);
        List<TileSide> sides = tile.sides();
        assertEquals(forestSide, sides.get(0));
        assertEquals(meadowSide, sides.get(1));
        assertEquals(riverSide, sides.get(2));
        assertEquals(forestSide, sides.get(3));
    }

    @Test
    void testTileSideZones() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(0, Animal.Kind.TIGER));

        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow1 = new Zone.Meadow(1, animals, Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, Zone.SpecialPower.RAFT);
        Zone.Lake lake = new Zone.Lake(3,5, Zone.SpecialPower.PIT_TRAP);
        Zone.River river1 = new Zone.River(4, 0, lake);
        Zone.Meadow meadow3 = new Zone.Meadow(5, animals, Zone.SpecialPower.PIT_TRAP);
        Zone.River river2 = new Zone.River(4, 0, null);

        TileSide.Forest forestSide = new TileSide.Forest(forest1);
        TileSide.Meadow meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide.Meadow meadowSide2 = new TileSide.Meadow(meadow2);
        TileSide.River riverSideWithLake = new TileSide.River(meadow2, river1, meadow3);
        TileSide.River riverSideWithOutLake = new TileSide.River(meadow2, river2, meadow3);

        Tile tileWithLake = new Tile(0, Tile.Kind.NORMAL, forestSide, meadowSide1, riverSideWithLake, meadowSide2);
        Tile tileWithOutLake = new Tile(1, Tile.Kind.NORMAL, forestSide, meadowSide1, riverSideWithOutLake, meadowSide2);
        Tile tileWithRedundantSide = new Tile(2, Tile.Kind.NORMAL, forestSide, meadowSide2, forestSide, forestSide);

        assertEquals(5, tileWithLake.sideZones().size());
        assertEquals(5, tileWithOutLake.sideZones().size());
        assertEquals(2, tileWithRedundantSide.sideZones().size());
    }

    @Test
    void testTileZones() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(0, Animal.Kind.TIGER));

        Zone.Forest forest1 = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow1 = new Zone.Meadow(1, animals, Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, Zone.SpecialPower.RAFT);
        Zone.Lake lake = new Zone.Lake(3,5, Zone.SpecialPower.PIT_TRAP);
        Zone.River river1 = new Zone.River(4, 0, lake);
        Zone.Meadow meadow3 = new Zone.Meadow(5, animals, Zone.SpecialPower.PIT_TRAP);
        Zone.River river2 = new Zone.River(4, 0, null);

        TileSide.Forest forestSide = new TileSide.Forest(forest1);
        TileSide.Meadow meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide.Meadow meadowSide2 = new TileSide.Meadow(meadow2);
        TileSide.River riverSideWithLake = new TileSide.River(meadow2, river1, meadow3);
        TileSide.River riverSideWithOutLake = new TileSide.River(meadow2, river2, meadow3);

        Tile tileWithLake = new Tile(0, Tile.Kind.NORMAL, forestSide, meadowSide1, riverSideWithLake, meadowSide2);
        Tile tileWithOutLake = new Tile(1, Tile.Kind.NORMAL, forestSide, meadowSide1, riverSideWithOutLake, meadowSide2);
        Tile tileWithRedundantSide = new Tile(2, Tile.Kind.NORMAL, forestSide, meadowSide2, forestSide, forestSide);

        assertEquals(6, tileWithLake.zones().size());
        assertEquals(5, tileWithOutLake.zones().size());
        assertEquals(2, tileWithRedundantSide.zones().size());
    }
    @Test
    void sidesNotNullAndNotEmpty() {
        Zone.Meadow meadow1 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(3, Collections.emptyList(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.River river = new Zone.River(4,0,new Zone.Lake(5,1, Zone.SpecialPower.SHAMAN));
        TileSide.River tileRiver = new TileSide.River(meadow1, river, meadow2);
        TileSide.Meadow tileMeadow = new TileSide.Meadow(new Zone.Meadow(5, Collections.emptyList(), Zone.SpecialPower.RAFT));
        TileSide.Forest forest1 = new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest2 = new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.WITH_MENHIR));
        Tile tile = new Tile(1, Tile.Kind.START, forest1, tileMeadow, tileRiver,forest2);
        List<TileSide> expectedSides = List.of(forest1,tileMeadow,tileRiver,forest2);
        assertNotNull(tile.sides());
        assertFalse(tile.sides().isEmpty());
        assertEquals(expectedSides,tile.sides());
    }

    @Test
    void sideZonesNotNullAndNotEmpty() {
        Zone.Meadow meadow = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.River river = new Zone.River(4,0,new Zone.Lake(5,1, Zone.SpecialPower.SHAMAN));
        Zone.Forest forests = new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide.River tileRiver = new TileSide.River(meadow, river, meadow);
        TileSide.Meadow tileMeadow = new TileSide.Meadow(meadow);
        TileSide.Forest forest1 = new TileSide.Forest(forests);
        TileSide.Forest forest2 = new TileSide.Forest(forests);
        Tile tile = new Tile(1, Tile.Kind.START, forest1, tileMeadow, tileRiver,forest2);
        Set<Zone> expectedSideZones = Set.of(forests,meadow,river);
        assertNotNull(tile.sideZones());
        assertFalse(tile.sideZones().isEmpty());
        assertEquals(expectedSideZones,tile.sideZones());
    }

    @Test
    void zones() {
        Zone.Meadow meadow = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Lake lake = new Zone.Lake(5,1, Zone.SpecialPower.SHAMAN);
        Zone.River river = new Zone.River(4,0,lake);
        TileSide.River tileRiver = new TileSide.River(meadow, river, meadow);
        TileSide.Meadow tileMeadow = new TileSide.Meadow(meadow);
        Tile tile = new Tile(1, Tile.Kind.START, tileMeadow, tileMeadow, tileRiver,tileMeadow);
        Set<Zone> expectedZones = Set.of(river,lake,meadow);
        assertEquals(expectedZones,tile.zones());
    }

    @Test
    void id() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertEquals(1, tile.id());
        assertTrue(tile.id() > 0);

    }

    @Test
    void kind() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertEquals(Tile.Kind.START, tile.kind());
        assertNotNull(tile.kind());
    }

    @Test
    void n() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertNotNull(tile.n());
        assertEquals(new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)), tile.n());

    }

    @Test
    void e() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertNotNull(tile.e());
        assertEquals(new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),tile.e());
    }

    @Test
    void s() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertNotNull(tile.s());
        assertEquals(new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),tile.s());
    }

    @Test
    void w() {
        Tile tile = new Tile(1, Tile.Kind.START,
                new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN)),
                new TileSide.Meadow(new Zone.Meadow(1,Collections.emptyList(), Zone.SpecialPower.SHAMAN)),
                new TileSide.Forest(new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS)),
                new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)));
        assertNotNull(tile.w());
        assertEquals(new TileSide.Forest(new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS)),tile.w());

    }
}

