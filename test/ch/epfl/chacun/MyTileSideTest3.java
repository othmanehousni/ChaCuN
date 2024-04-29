package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyTileSideTest3 {

    //Test for forest
    @Test
    void myTileSideForesteConatructorTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);
        assertEquals(forestZone, forest.forest());
    }

    @Test
    void myZonesForestTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);
        List<Zone> zones = new ArrayList<>();
        zones.add(forestZone);

        assertEquals(zones, forest.zones());
    }

    @Test
    void myAsSameKindAsForestTrivialTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);
        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest1 = new TileSide.Forest(forestZone1);

        assertEquals(true, forest.isSameKindAs(forest1));
    }
    @Test
    void myAsSameKindAsForestNonTrivialTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);

        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        assertEquals(false, forest.isSameKindAs(meadow1));
    }

    // for meadow
    @Test
    void myZonesMeadowTest(){
        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        List<Zone> zones = new ArrayList<>();
        zones.add(meadow);

        assertEquals(zones, meadow1.zones());
    }

    @Test
    void myAsSameKindAsMeadowTrivialTest(){
        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        List<Animal> animals1 = new ArrayList<>();
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);

        assertEquals(true, meadow1.isSameKindAs(meadow3));
    }
    @Test
    void myAsSameKindAsMeadowNonTrivialTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);

        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        assertEquals(false, meadow1.isSameKindAs(forest));
    }

    // for river
    @Test
    void myZonesRiverTest(){
        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        List<Animal> animals1 = new ArrayList<>();
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);


        Zone.River riverZone = new Zone.River(1, 2, null);
        TileSide.River river = new TileSide.River(meadow, riverZone, meadow2);

        List<Zone> zones = new ArrayList<>();
        zones.add(meadow);
        zones.add(riverZone);
        zones.add(meadow2);

        assertEquals(zones, river.zones());
    }

    @Test
    void myAsSameKindAsRiverTrivialTest(){
        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        List<Animal> animals1 = new ArrayList<>();
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);


        Zone.River riverZone = new Zone.River(1, 2, null);
        TileSide.River river = new TileSide.River(meadow, riverZone, meadow2);

        List<Animal> animals10 = new ArrayList<>();
        Zone.Meadow meadow10 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow11 = new TileSide.Meadow(meadow);

        List<Animal> animals11 = new ArrayList<>();
        Zone.Meadow meadow21 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow31 = new TileSide.Meadow(meadow);


        Zone.River riverZone1 = new Zone.River(1, 2, null);
        TileSide.River river1 = new TileSide.River(meadow, riverZone, meadow2);

        assertEquals(true, river.isSameKindAs(river1));
    }
    @Test
    void myAsSameKindAsRiverNonTrivialTest(){
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forest = new TileSide.Forest(forestZone);

        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadow = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow1 = new TileSide.Meadow(meadow);

        List<Animal> animals1 = new ArrayList<>();
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, null);
        TileSide.Meadow meadow3 = new TileSide.Meadow(meadow);


        Zone.River riverZone = new Zone.River(1, 2, null);
        TileSide.River river = new TileSide.River(meadow, riverZone, meadow2);

        assertEquals(false, river.isSameKindAs(forest));
    }



}