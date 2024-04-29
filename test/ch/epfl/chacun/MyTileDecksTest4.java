package ch.epfl.chacun;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class MyTileDecksTest4 {
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
    void testDeckSize() {
        List<Tile> startTiles = List.of(
                new Tile(0, Tile.Kind.START, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );

        List<Tile> normalTiles = List.of(
                new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2)),
                new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );

        List<Tile> menhirTiles = List.of(
                new Tile(3, Tile.Kind.MENHIR, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertEquals(1, tileDecks.deckSize(Tile.Kind.START));
        assertEquals(2, tileDecks.deckSize(Tile.Kind.NORMAL));
        assertEquals(1, tileDecks.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void testTopTile() {
        List<Tile> startTiles = List.of(
                new Tile(0, Tile.Kind.START, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> normalTiles = List.of(
                new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2)),
                new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> menhirTiles = List.of(
                new Tile(3, Tile.Kind.MENHIR, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> menhirTilesVide = List.of();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        TileDecks tileDecksWithMenhirTilesVide = new TileDecks(startTiles, normalTiles, menhirTilesVide);

        assertEquals(startTiles.getFirst(), tileDecks.topTile(Tile.Kind.START));
        assertEquals(normalTiles.getFirst(), tileDecks.topTile(Tile.Kind.NORMAL));
        assertEquals(menhirTiles.getFirst(), tileDecks.topTile(Tile.Kind.MENHIR));
        assertNull(tileDecksWithMenhirTilesVide.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void testWithTopTileDrawn() {
        List<Tile> startTiles = List.of(
                new Tile(0, Tile.Kind.START, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> normalTiles = List.of(
                new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2)),
                new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> menhirTiles = List.of(
                new Tile(3, Tile.Kind.MENHIR, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        TileDecks updatedDecks1 = tileDecks.withTopTileDrawn(Tile.Kind.START);
        TileDecks updatedDecks2 = tileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
        TileDecks updatedDecks3 = updatedDecks2.withTopTileDrawn(Tile.Kind.NORMAL);


        assertEquals(0, tileDecks.withTopTileDrawn(Tile.Kind.START).deckSize(Tile.Kind.START));
        assertEquals(1, tileDecks.withTopTileDrawn(Tile.Kind.NORMAL).deckSize(Tile.Kind.NORMAL));
        assertEquals(0, updatedDecks2.withTopTileDrawn(Tile.Kind.NORMAL).deckSize(Tile.Kind.NORMAL));
        assertThrows(IllegalArgumentException.class, () -> updatedDecks1.withTopTileDrawn(Tile.Kind.START));
        assertThrows(IllegalArgumentException.class, () -> updatedDecks3.withTopTileDrawn(Tile.Kind.NORMAL));
    }

    @Test
    void testWithTopTileDrawnUntil() {
        List<Tile> startTiles = List.of(
                new Tile(0, Tile.Kind.START, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> normalTiles = List.of(
                new Tile(1, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2)),
                new Tile(2, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2)),
                new Tile(3, Tile.Kind.NORMAL, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );
        List<Tile> menhirTiles = List.of(
                new Tile(4, Tile.Kind.MENHIR, new TileSide.Meadow(meadow1), new TileSide.Forest(forest1), new TileSide.Meadow(meadow2), new TileSide.Forest(forest2))
        );

        List<Tile> menhirTilesVide = List.of();

        TileDecks tileDecks = new TileDecks(startTiles, normalTiles, menhirTiles);
        TileDecks tileDecksWithMenhirTileVide = new TileDecks(startTiles, normalTiles, menhirTilesVide);

        // Define a predicate for testing
        Predicate<Tile> predicate = t -> t.id() == 2;


        //assertThrows(IllegalArgumentException.class, () -> tileDecksWithMenhirTileVide.withTopTileDrawnUntil(Tile.Kind.MENHIR, predicate).deckSize(Tile.Kind.MENHIR));

        assertEquals(2, tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, predicate).deckSize(Tile.Kind.NORMAL));
    }

    @Test
    void testDeckSize2() {
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
    void testTopTile2() {
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
    void testWithTopTileDrawn2() {
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
    void testWithTopTileDrawnUntil2() {
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

    }
}

