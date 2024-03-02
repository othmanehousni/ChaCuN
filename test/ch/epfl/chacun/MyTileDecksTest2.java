package ch.epfl.chacun;


import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static ch.epfl.chacun.Tile.Kind.NORMAL;
import static ch.epfl.chacun.Tile.Kind.START;
import static ch.epfl.chacun.Tile.Kind.MENHIR;

import static ch.epfl.chacun.Zone.Forest.Kind.WITH_MENHIR;
import static ch.epfl.chacun.Zone.Forest.Kind.WITH_MUSHROOMS;

import static java.util.List.copyOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyTileDecksTest2 {

    @Test
    public void myDeckSizeTest(){
        var meadowAnimals = List.of(new Animal(0, Animal.Kind.DEER));
        var meadow = new Zone.Meadow(0, meadowAnimals, null);
        var meadow2 = new Zone.Meadow(1, meadowAnimals, null);

        var forest = new Zone.Forest(1, WITH_MENHIR);
        var forest2 = new Zone.Forest(3, WITH_MUSHROOMS);

        var lake = new Zone.Lake(5, 2, null);

        var river = new Zone.River(2, 3, lake);

        var northMeadow = new TileSide.Meadow(meadow);
        var eastForest = new TileSide.Forest(forest);
        var southRiver = new TileSide.River(meadow, river, meadow2);
        var westForest = new TileSide.Forest(forest2);

        Tile testTile1 = new Tile(1, NORMAL, northMeadow, eastForest, southRiver, westForest);
        Tile testTile2 = new Tile(2, START, northMeadow, eastForest, southRiver, westForest);
        Tile testTile3 = new Tile(3, MENHIR, northMeadow, eastForest, southRiver, westForest);
        Tile testTile4 = new Tile(4, NORMAL, northMeadow, eastForest, southRiver, westForest);
        Tile testTile5 = new Tile(5, MENHIR, northMeadow, eastForest, southRiver, westForest);
        Tile testTile6 = new Tile(6, NORMAL, northMeadow, eastForest, southRiver, westForest);

        var startTiles = new ArrayList<Tile>();
        var normalTiles = new ArrayList<Tile>();
        var menhirTiles = new ArrayList<Tile>();

        startTiles.add(testTile2);
        normalTiles.add(testTile1);
        normalTiles.add(testTile4);
        normalTiles.add(testTile6);
        menhirTiles.add(testTile3);
        menhirTiles.add(testTile5);

        var testTileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertEquals(3, testTileDeck.deckSize(NORMAL));
        assertEquals(1, testTileDeck.deckSize(START));
        assertEquals(2, testTileDeck.deckSize(MENHIR));
    }

    @Test
    public void myTopTileTest(){
        var meadowAnimals = List.of(new Animal(0, Animal.Kind.DEER));
        var meadow = new Zone.Meadow(0, meadowAnimals, null);
        var meadow2 = new Zone.Meadow(1, meadowAnimals, null);

        var forest = new Zone.Forest(1, WITH_MENHIR);
        var forest2 = new Zone.Forest(3, WITH_MUSHROOMS);

        var lake = new Zone.Lake(5, 2, null);

        var river = new Zone.River(2, 3, lake);

        var northMeadow = new TileSide.Meadow(meadow);
        var eastForest = new TileSide.Forest(forest);
        var southRiver = new TileSide.River(meadow, river, meadow2);
        var westForest = new TileSide.Forest(forest2);

        Tile testTile1 = new Tile(1, NORMAL, northMeadow, eastForest, southRiver, westForest);
        Tile testTile2 = new Tile(2, START, northMeadow, eastForest, southRiver, westForest);
        Tile testTile3 = new Tile(3, MENHIR, northMeadow, eastForest, southRiver, westForest);
        Tile testTile4 = new Tile(4, NORMAL, northMeadow, eastForest, southRiver, westForest);
        Tile testTile5 = new Tile(5, MENHIR, northMeadow, eastForest, southRiver, westForest);
        Tile testTile6 = new Tile(6, NORMAL, northMeadow, eastForest, southRiver, westForest);

        var startTiles = new ArrayList<Tile>();
        var normalTiles = new ArrayList<Tile>();
        var menhirTiles = new ArrayList<Tile>();

        startTiles.add(testTile2);
        normalTiles.add(testTile1);
        normalTiles.add(testTile4);
        normalTiles.add(testTile6);
        menhirTiles.add(testTile3);
        menhirTiles.add(testTile5);

        var testTileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);

        var normalTilesDrawn = new ArrayList<Tile>(normalTiles);
        normalTilesDrawn.remove(0);

        var startTilesDrawn = new ArrayList<Tile>(startTiles);
        startTilesDrawn.remove(0);

        var menhirTilesDrawn = new ArrayList<Tile>(menhirTiles);
        menhirTilesDrawn.remove(0);

        assertEquals(new TileDecks(startTiles, normalTilesDrawn, menhirTiles), testTileDeck.withTopTileDrawn(Tile.Kind.NORMAL));
        assertEquals(new TileDecks(startTilesDrawn, normalTiles, menhirTiles), testTileDeck.withTopTileDrawn(Tile.Kind.START));
        assertEquals(new TileDecks(startTiles, normalTiles, menhirTilesDrawn), testTileDeck.withTopTileDrawn(Tile.Kind.MENHIR));
    }

    @Test
    public void myNullTopTileTest(){
        var startTiles = new ArrayList<Tile>();
        var normalTiles = new ArrayList<Tile>();
        var menhirTiles = new ArrayList<Tile>();

        var testTileDeck = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertThrows(IllegalArgumentException.class, () -> testTileDeck.withTopTileDrawn(NORMAL));
        assertThrows(IllegalArgumentException.class, () -> testTileDeck.withTopTileDrawn(START));
        assertThrows(IllegalArgumentException.class, () -> testTileDeck.withTopTileDrawn(MENHIR));
    }

}