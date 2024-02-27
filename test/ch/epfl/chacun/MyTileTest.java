package ch.epfl.chacun;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class MyTileTest {

    @Test
    void sides() {
        Tile tile = new Tile(1, Tile.Kind.START, );
        List<TileSide> sides = tile.sides();
        assertNotNull(sides);
        assertFalse(sides.isEmpty());
    }

    @Test
    void sideZones() {
    }

    @Test
    void zones() {
    }

    @Test
    void id() {
    }

    @Test
    void kind() {
    }

    @Test
    void n() {
    }

    @Test
    void e() {
    }

    @Test
    void s() {
    }

    @Test
    void w() {
    }
}