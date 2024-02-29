package ch.epfl.chacun;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class MyTileDecksTest {

    @Test
    void testDeckSize() {
        // Create a TileDecks instance
        TileDecks tileDecks = new TileDecks(
                List.of(new Tile(1, Tile.Kind.START, null, null, null, null)),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                new ArrayList<>());

        // Verify deck size for each kind of tile
        assertEquals(1, tileDecks.deckSize(Tile.Kind.START));
        assertEquals(2, tileDecks.deckSize(Tile.Kind.NORMAL));
        assertEquals(0, tileDecks.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void testTopTile() {
        // Create a TileDecks instance
        TileDecks tileDecks = new TileDecks(
                List.of(new Tile(1, Tile.Kind.START, null, null, null, null)),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));
        TileDecks tileDecks2 = new TileDecks(
                new ArrayList<>(),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));

        // Verify top tile for each kind of tile
        assertEquals(1, tileDecks.topTile(Tile.Kind.START).id());
        assertEquals(2, tileDecks.topTile(Tile.Kind.NORMAL).id());
        assertEquals(4, tileDecks.topTile(Tile.Kind.MENHIR).id());
        assertNull(tileDecks2.topTile(Tile.Kind.START));
    }

    @Test
    void testWithTopTileDrawn() {
        // Create a TileDecks instance
        TileDecks tileDecks = new TileDecks(
                List.of(new Tile(1, Tile.Kind.START, null, null, null, null)),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));
        TileDecks tileDecks2 = new TileDecks(
                new ArrayList<>(),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));

        // Draw top tile for each kind of tile
        TileDecks updatedDecks1 = tileDecks.withTopTileDrawn(Tile.Kind.START);
        assertEquals(0, updatedDecks1.deckSize(Tile.Kind.START));

        TileDecks updatedDecks2 = tileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
        assertEquals(1, updatedDecks2.deckSize(Tile.Kind.NORMAL));

        TileDecks updatedDecks3 = tileDecks.withTopTileDrawn(Tile.Kind.MENHIR);
        assertEquals(0, updatedDecks3.deckSize(Tile.Kind.MENHIR));

        assertThrows(IllegalArgumentException.class, () -> tileDecks2.withTopTileDrawn(Tile.Kind.START));

    }

    @Test
    void testWithTopTileDrawnUntil() {
        // Create a TileDecks instance
        TileDecks tileDecks = new TileDecks(
                List.of(new Tile(1, Tile.Kind.START, null, null, null, null)),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));
        TileDecks tileDecks2 = new TileDecks(
                new ArrayList<>(),
                List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                        new Tile(3, Tile.Kind.NORMAL, null, null, null, null)),
                List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null)));


        // Draw top tiles until a certain condition is met for each kind of tile
        Predicate<Tile> condition = tile -> tile.id() == 3;
        Predicate<Tile> condition2 = tile -> true;


        TileDecks updatedDecks1 = tileDecks.withTopTileDrawnUntil(Tile.Kind.START, condition);
        assertEquals(0, updatedDecks1.deckSize(Tile.Kind.START));

        TileDecks updatedDecks12 = tileDecks.withTopTileDrawnUntil(Tile.Kind.START, condition2);
        assertEquals(1, updatedDecks12.deckSize(Tile.Kind.START));

        TileDecks updatedDecks2 = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, condition);
        assertEquals(1, updatedDecks2.deckSize(Tile.Kind.NORMAL));

        TileDecks updatedDecks3 = tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, condition);
        assertEquals(0, updatedDecks3.deckSize(Tile.Kind.MENHIR));

        assertThrows(IllegalArgumentException.class, () -> tileDecks2.withTopTileDrawnUntil(Tile.Kind.START, condition));
    }
}

