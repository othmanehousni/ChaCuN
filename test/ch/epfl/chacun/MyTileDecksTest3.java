package ch.epfl.chacun;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static ch.epfl.chacun.Tile.Kind.*;
import static org.junit.jupiter.api.Assertions.*;

class MyTileDecksTest3 {

    @Test
    void testDeckSize() {
        List<Tile> startTiles = Collections.singletonList(new Tile(1, START, null, null, null, null));
        List<Tile> normalTiles = List.of(new Tile(2, NORMAL, null, null, null, null), new Tile(3, NORMAL, null, null, null, null));
        List<Tile> menhirTiles = Collections.emptyList();

        TileDecks decks = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertEquals(1, decks.deckSize(START));
        assertEquals(2, decks.deckSize(NORMAL));
        assertEquals(0, decks.deckSize(MENHIR));
    }

    @Test
    void testTopTile() {
        List<Tile> startTiles = Collections.singletonList(new Tile(1, START, null, null, null, null));
        List<Tile> normalTiles = List.of(new Tile(2, NORMAL, null, null, null, null), new Tile(3, NORMAL, null, null, null, null));
        List<Tile> menhirTiles = Collections.emptyList();

        TileDecks decks = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertEquals(new Tile(1, START, null, null, null, null), decks.topTile(START));
        assertEquals(new Tile(2, NORMAL, null, null, null, null), decks.topTile(NORMAL));
        assertNull(decks.topTile(MENHIR));
    }

    @Test
    void testWithTopTileDrawn() {
        List<Tile> startTiles = List.of(new Tile(1, START, null, null, null, null), new Tile(4, START, null, null, null, null));
        List<Tile> normalTiles = List.of(new Tile(2, NORMAL, null, null, null, null), new Tile(3, NORMAL, null, null, null, null));
        List<Tile> menhirTiles = Collections.emptyList();

        TileDecks decks = new TileDecks(startTiles, normalTiles, menhirTiles);

        TileDecks newDecks = decks.withTopTileDrawn(START);
        assertEquals(1, newDecks.deckSize(START));
        assertEquals(2, decks.deckSize(START));

        newDecks = decks.withTopTileDrawn(NORMAL);
        assertEquals(1, newDecks.deckSize(NORMAL));
        assertEquals(2, decks.deckSize(NORMAL));
    }



}
