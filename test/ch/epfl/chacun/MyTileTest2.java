package ch.epfl.chacun;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileSide;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import static ch.epfl.chacun.Direction.*;
import static ch.epfl.chacun.Tile.Kind.MENHIR;
import static ch.epfl.chacun.Tile.Kind.NORMAL;
import static ch.epfl.chacun.Zone.Forest.Kind.WITH_MENHIR;
import static ch.epfl.chacun.Zone.Forest.Kind.WITH_MUSHROOMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MyTileTest2 {

    @Test
    public void mySidesTest() {

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

        Tile testTile = new Tile(1, NORMAL, northMeadow, eastForest, southRiver, westForest);
        assertEquals(northMeadow, testTile.sides().get(0));
        assertEquals(eastForest, testTile.sides().get(1));
        assertEquals(southRiver, testTile.sides().get(2));
        assertEquals(westForest, testTile.sides().get(3));
    }

    @Test
    public void mySideZonesTest() {

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

        Tile testTile = new Tile(1, NORMAL, northMeadow, eastForest, southRiver, westForest);
        assertTrue(testTile.sideZones().contains(meadow));
        assertTrue(testTile.sideZones().contains(forest));
        assertTrue(testTile.sideZones().contains(forest2));
        assertTrue(testTile.sideZones().contains(river));
        assertTrue(testTile.sideZones().contains(meadow2));
    }

    @Test
    public void myZonesTest() {
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

        Tile testTile = new Tile(1, NORMAL, northMeadow, eastForest, southRiver, westForest);
        assertTrue(testTile.zones().contains(meadow));
        assertTrue(testTile.zones().contains(forest));
        assertTrue(testTile.zones().contains(forest2));
        assertTrue(testTile.zones().contains(river));
        assertTrue(testTile.zones().contains(meadow2));
        assertTrue(testTile.zones().contains(lake));
    }
}