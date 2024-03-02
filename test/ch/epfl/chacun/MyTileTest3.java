package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyTileTest3 {
        @Test
        void mySidesTrivialTest(){
            Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest = new TileSide.Forest(forestZone);

            Zone.Forest forestZone2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest2 = new TileSide.Forest(forestZone);

            List<Animal> animals = new ArrayList<>();
            Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

            List<Animal> animals11 = new ArrayList<>();
            Zone.Meadow meadow12 = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow11 = new TileSide.Meadow(meadow);

            List<Animal> animals1 = new ArrayList<>();
            Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);


            Zone.River riverZone = new Zone.River(1, 2, null);
            TileSide.River river = new TileSide.River(meadow, riverZone, meadow2);

            Tile tile = new Tile(2, Tile.Kind.NORMAL, forest, meadow1, river, forest2);


            List<TileSide> sides = List.of(forest, meadow1, river, forest2);
            assertEquals(sides, tile.sides());
        }

        @Test
        void mySidesNonTrivialTest(){
            Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest = new TileSide.Forest(forestZone);

            Zone.Forest forestZone2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest2 = new TileSide.Forest(forestZone);

            List<Animal> animals = new ArrayList<>();
            Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

            List<Animal> animals11 = new ArrayList<>();
            Zone.Meadow meadow12 = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow11 = new TileSide.Meadow(meadow);

            List<Animal> animals1 = new ArrayList<>();
            Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
            TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);


            Zone.River riverZone = new Zone.River(1, 2, null);
            TileSide.River river = new TileSide.River(meadow, riverZone, meadow2);

            Tile tile = new Tile(2, Tile.Kind.NORMAL, river, meadow1, forest, forest2);


            List<TileSide> sides = List.of(forest, meadow1, river, forest2);
            assertNotEquals(sides, tile.sides());
        }

        @Test
        void mySidesZonesTest(){
            Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest = new TileSide.Forest(forestZone);

            Zone.Forest forestZone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest2 = new TileSide.Forest(forestZone2);

            List<Animal> animals1 = new ArrayList<>();
            Zone.Meadow meadowZone1 = new Zone.Meadow(3, animals1, null);
            TileSide.Meadow meadow1 = new TileSide.Meadow(meadowZone1);

            List<Animal> animals2 = new ArrayList<>();
            Zone.Meadow meadowZone2 = new Zone.Meadow(4, animals2, null);
            TileSide.Meadow meadow2 = new TileSide.Meadow(meadowZone2);

            List<Animal> animals3 = new ArrayList<>();
            Zone.Meadow meadowZone3 = new Zone.Meadow(6, animals3, null);
            TileSide.Meadow meadow3 = new TileSide.Meadow(meadowZone3);


            Zone.River riverZone = new Zone.River(5, 2, null);
            TileSide.River river = new TileSide.River(meadowZone2, riverZone, meadowZone3);

            Tile tile = new Tile(2, Tile.Kind.NORMAL, forest, forest2, meadow1, river);

            Set<Zone> sides = new HashSet<>();
            sides.add(forestZone);
            sides.add(forestZone2);
            sides.add(meadowZone1);
            sides.add(meadowZone2);
            sides.add(riverZone);
            sides.add(meadowZone3);

            assertEquals(sides, tile.sideZones());
        }

        @Test
        void myZonesTest(){
            Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest = new TileSide.Forest(forestZone);

            Zone.Forest forestZone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
            TileSide.Forest forest2 = new TileSide.Forest(forestZone2);

            List<Animal> animals1 = new ArrayList<>();
            Zone.Meadow meadowZone1 = new Zone.Meadow(3, animals1, null);
            TileSide.Meadow meadow1 = new TileSide.Meadow(meadowZone1);

            List<Animal> animals2 = new ArrayList<>();
            Zone.Meadow meadowZone2 = new Zone.Meadow(4, animals2, null);
            TileSide.Meadow meadow2 = new TileSide.Meadow(meadowZone2);

            List<Animal> animals3 = new ArrayList<>();
            Zone.Meadow meadowZone3 = new Zone.Meadow(6, animals3, null);
            TileSide.Meadow meadow3 = new TileSide.Meadow(meadowZone3);

            Zone.Lake lake = new Zone.Lake(8, 3, null);
            Zone.River riverZone = new Zone.River(5, 2, lake);
            TileSide.River river = new TileSide.River(meadowZone2, riverZone, meadowZone3);

            Tile tile = new Tile(2, Tile.Kind.NORMAL, forest, forest2, meadow1, river);

            Set<Zone> sides = new HashSet<>();
            sides.add(forestZone);
            sides.add(forestZone2);
            sides.add(meadowZone1);
            sides.add(meadowZone2);
            sides.add(riverZone);
            sides.add(meadowZone3);
            sides.add(lake);

            assertEquals(sides, tile.zones());
        }
}