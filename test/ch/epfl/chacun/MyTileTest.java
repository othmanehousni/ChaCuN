package ch.epfl.chacun;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class MyTileTest {

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
        List<TileSide> sides = tile.sides();
        assertNotNull(sides);
        assertFalse(sides.isEmpty());
    }

    @Test
    void sideZonesNotNullAndNotEmpty() {
        Zone.Meadow meadow1 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(3, Collections.emptyList(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.River river = new Zone.River(4,0,new Zone.Lake(5,1, Zone.SpecialPower.SHAMAN));
        TileSide.River tileRiver = new TileSide.River(meadow1, river, meadow2);
        TileSide.Meadow tileMeadow = new TileSide.Meadow(new Zone.Meadow(5, Collections.emptyList(), Zone.SpecialPower.RAFT));
        TileSide.Forest forest1 = new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest2 = new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.WITH_MENHIR));
        Tile tile = new Tile(1, Tile.Kind.START, forest1, tileMeadow, tileRiver,forest2);
        Set<Zone> sideZones = tile.sideZones();
        assertNotNull(sideZones);
        assertFalse(sideZones.isEmpty());
    }

    @Test
    void zones() {
        Zone.Meadow meadow1 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(3, Collections.emptyList(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.River river = new Zone.River(4,0,new Zone.Lake(5,1, Zone.SpecialPower.SHAMAN));
        TileSide.River tileRiver = new TileSide.River(meadow1, river, meadow2);
        TileSide.Meadow tileMeadow = new TileSide.Meadow(new Zone.Meadow(5, Collections.emptyList(), Zone.SpecialPower.RAFT));
        TileSide.Forest forest1 = new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN));
        TileSide.Forest forest2 = new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.WITH_MENHIR));
        Tile tile = new Tile(1, Tile.Kind.START, forest1, tileMeadow, tileRiver,forest2);

        // TODO : REGLER CE PROBLEME DE MERDE

        assertNotNull(tile.zones());
        assertFalse(tile.zones().isEmpty());
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