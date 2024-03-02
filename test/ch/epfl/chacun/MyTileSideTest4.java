package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyTileSideTest4 {
    @Test
    void testForestTileSide() {
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        TileSide.Forest forestSide = new TileSide.Forest(forest);

        assertEquals(forest, forestSide.zones().getFirst());
        assertTrue(forestSide.isSameKindAs(forestSide));
    }

    @Test
    void testMeadowTileSide() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(0, Animal.Kind.TIGER));

        Zone.Meadow meadow = new Zone.Meadow(0,animals, Zone.SpecialPower.PIT_TRAP);
        TileSide.Meadow meadowSide = new TileSide.Meadow(meadow);

        assertEquals(meadow, meadowSide.zones().getFirst());
        assertTrue(meadowSide.isSameKindAs(meadowSide));
    }

    @Test
    void testRiverTileSide() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(0, Animal.Kind.TIGER));

        Zone.Meadow meadow1 = new Zone.Meadow(1, animals, Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(2, animals, Zone.SpecialPower.RAFT);
        Zone.River river = new Zone.River(3, 0, null);

        TileSide.River riverSide = new TileSide.River(meadow1, river, meadow2);

        assertEquals(meadow1, riverSide.zones().get(0));
        assertEquals(river, riverSide.zones().get(1));
        assertEquals(meadow2, riverSide.zones().get(2));

        assertTrue(riverSide.isSameKindAs(riverSide));
    }
    @Test
    public void forestTileSideZonesWorks () {
        Zone.Forest forest = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide.Forest sideForest = new TileSide.Forest(forest);
        List<Zone.Forest> forestList = List.of(forest);
        assertEquals(sideForest.zones(),forestList);
    }
    @Test
    public void forestTileSideIsSameKindAsForestWorks() {
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        TileSide sideForest1 = new TileSide.Forest(forest1);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);
        TileSide sideForest2 = new TileSide.Forest(forest2);
        assertTrue(sideForest1.isSameKindAs(sideForest2));
    }

    @Test
    public void meadowTileSideZonesWorks () {
        Zone.Meadow meadow = new Zone.Meadow (1, Collections.emptyList(), Zone.SpecialPower.SHAMAN);
        TileSide.Meadow sideMeadow = new TileSide.Meadow(meadow);
        List<Zone.Meadow> meadowList = List.of(meadow);
        assertEquals(sideMeadow.zones(),meadowList);
    }
    @Test
    public void meadowTileSideIsSameKindAsMeadowWorks() {
        Zone.Meadow meadow1 = new Zone.Meadow(1, Collections.emptyList(), Zone.SpecialPower.HUNTING_TRAP);
        TileSide sideMeadow1 = new TileSide.Meadow(meadow1);
        Zone.Meadow meadow2 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.LOGBOAT);
        TileSide sideMeadow2 = new TileSide.Meadow(meadow2);
        assertTrue(sideMeadow1.isSameKindAs(sideMeadow2));
    }

    @Test
    public void riverTileSideZonesWorks () {
        Zone.River river = new Zone.River(1, 2, new Zone.Lake(1,2, Zone.SpecialPower.RAFT));
        Zone.Meadow meadow1 = new Zone.Meadow(1, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.WILD_FIRE);
        TileSide.River sideRiver = new TileSide.River(meadow1,river,meadow2);
        List<Zone> riverList = List.of(meadow1,river,meadow2);
        assertEquals(sideRiver.zones(),riverList);
    }
    @Test
    public void riverTileSideIsSameKindAsLakeWorks() {
        Zone.River river = new Zone.River(1, 2, new Zone.Lake(1,2, Zone.SpecialPower.RAFT));
        Zone.Meadow meadow1 = new Zone.Meadow(1, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.WILD_FIRE);
        TileSide.River sideRiver1 = new TileSide.River(meadow1,river,meadow2);
        List<Zone> riverList1 = List.of(meadow1,river,meadow2);
        Zone.River river2 = new Zone.River(1, 2, new Zone.Lake(1,2, Zone.SpecialPower.RAFT));
        Zone.Meadow meadow3 = new Zone.Meadow(1, Collections.emptyList(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow4 = new Zone.Meadow(2, Collections.emptyList(), Zone.SpecialPower.WILD_FIRE);
        TileSide.River sideRiver2 = new TileSide.River(meadow1,river,meadow2);
        List<Zone> riverList2 = List.of(meadow3,river2,meadow4);
        assertTrue(sideRiver1.isSameKindAs(sideRiver2));
    }


}