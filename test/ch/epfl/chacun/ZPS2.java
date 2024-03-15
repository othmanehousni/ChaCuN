package etape4;



import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ZPS2 {
    @Test
    void testEmptyZonePartitions(){
        ZonePartitions actual = new ZonePartitions(new ZonePartition<>(),new ZonePartition<>(),new ZonePartition<>(),new ZonePartition<>());
        assertEquals(ZonePartitions.EMPTY,actual);
    }

    @Test
    void testAddTileOnStartTile(){
        ZonePartitions actual = ZonePartitions.EMPTY;
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        TileSide n = new TileSide.Meadow(m0);
        TileSide e = new TileSide.Forest(f1);
        TileSide s = new TileSide.Forest(f1);
        TileSide w = new TileSide.River(m2,r3,m0);

        Tile t = new Tile(56, Tile.Kind.START,n,e,s,w);

        ZonePartitions.Builder builder = new ZonePartitions.Builder(actual);
        builder.addTile(t);

        Area<Zone.Forest> areaF = new Area<>(Set.of(f1),new ArrayList<>(),2);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2),new ArrayList<>(),1);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1));

        Area<Zone.River> areaR = new Area<>(Set.of(r3),new ArrayList<>(),1);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR));

        Area<Zone.Water> areaRS = new Area<>(Set.of(r3,l8),new ArrayList<>(),1);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS));

        ZonePartitions expected = new ZonePartitions(f, m, r, rs);

        assertEquals(expected,builder.build());
    }

    @Test
    void testAddTileOnNonTrivialTile(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Area<Zone.Forest> areaF = new Area<>(Set.of(f1),new ArrayList<>(),2);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2),new ArrayList<>(),1);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1));

        Area<Zone.River> areaR = new Area<>(Set.of(r3),new ArrayList<>(),1);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR));

        Area<Zone.Water> areaRS = new Area<>(Set.of(r3,l8),new ArrayList<>(),1);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS));

        // Ajout d'un tuile non trivial
        ZonePartitions start = new ZonePartitions(f, m, r, rs);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);

        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);
        ZonePartitions.Builder actual = new ZonePartitions.Builder(start);
        actual.addTile(tile);


        Area<Zone.Meadow> expectedAreaM2 = new Area<>(Set.of(nm0),new ArrayList<>(),4);
        Area<Zone.Meadow> expectedAreaM3 = new Area<>(Set.of(nm2),new ArrayList<>(),2);
        Area<Zone.Meadow> expectedAreaM4 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> expectedM = new ZonePartition(Set.of(areaM0,areaM1,expectedAreaM2,expectedAreaM3,expectedAreaM4));

        Area<Zone.River> expectedAreaR1 = new Area<>(Set.of(nr1),new ArrayList<>(),2);
        Area<Zone.River> expectedAreaR2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> expectedR = new ZonePartition(Set.of(areaR,expectedAreaR1,expectedAreaR2));

        Area<Zone.Water> expectedAreaRS1 = new Area<>(Set.of(nr1),new ArrayList<>(),2);
        Area<Zone.Water> expectedAreaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> expectedRS = new ZonePartition(Set.of(areaRS,expectedAreaRS1,expectedAreaRS2));

        ZonePartitions expected = new ZonePartitions(f, expectedM, expectedR, expectedRS);


        assertEquals(expected,actual.build());
    }

    @Test
    void testConnectSidesOnNonTrivialTileSameKind(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        actual.addTile(tStart);
        actual.addTile(tile);

        actual.connectSides(wStart,e);

        Area<Zone.Forest> areaF = new Area<>(Set.of(f1),new ArrayList<>(),2);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0,nm2),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2,nm0),new ArrayList<>(),3);
        Area<Zone.Meadow> areaM2 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1,areaM2));

        Area<Zone.River> areaR1 = new Area<>(Set.of(r3,nr1),new ArrayList<>(),1);
        Area<Zone.River> areaR2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR1,areaR2));

        Area<Zone.Water> areaRS1 = new Area<>(Set.of(nr1,r3,l8),new ArrayList<>(),1);
        Area<Zone.Water> areaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS1,areaRS2));

        ZonePartitions expected = new ZonePartitions(f,m,r,rs);
        assertEquals(expected,actual.build());
    }

    @Test
    void testConnectSidesOnNonTrivialTileNotSameKind(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        actual.addTile(tStart);
        actual.addTile(tile);

        assertThrows(IllegalArgumentException.class, () -> actual.connectSides(n,eStart));
        assertThrows(IllegalArgumentException.class, () -> actual.connectSides(eStart,nStart));
        assertThrows(IllegalArgumentException.class, () -> actual.connectSides(nStart,s));
    }

    @Test
    void testaddInitialOccupantPawnOnNonTrivialTile(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        actual.addTile(tStart);
        actual.addTile(tile);
        actual.connectSides(wStart,e);
        actual.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN,nm2);

        Area<Zone.Forest> areaF = new Area<>(Set.of(f1),new ArrayList<>(),2);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0,nm2),List.of(PlayerColor.PURPLE),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2,nm0),new ArrayList<>(),3);
        Area<Zone.Meadow> areaM2 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1,areaM2));

        Area<Zone.River> areaR1 = new Area<>(Set.of(r3,nr1),new ArrayList<>(),1);
        Area<Zone.River> areaR2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR1,areaR2));

        Area<Zone.Water> areaRS1 = new Area<>(Set.of(nr1,r3,l8),new ArrayList<>(),1);
        Area<Zone.Water> areaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS1,areaRS2));

        ZonePartitions expected = new ZonePartitions(f,m,r,rs);
        assertEquals(expected,actual.build());
    }

    @Test
    void testaddInitialOccupantPawnOnNonTrivialTileCanNotBeOccupiedByThat(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        actual.addTile(tStart);
        actual.addTile(tile);
        actual.connectSides(wStart,e);
        actual.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN,nm2);


        assertThrows(IllegalArgumentException.class, () -> actual.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN,l8));
        assertThrows(IllegalArgumentException.class, () -> actual.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.HUT,f1));
        assertThrows(IllegalArgumentException.class, () -> actual.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT,nm4));
    }

    @Test
    void testRemovePawnOnNonTrivialTileNotOccupied(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        actual.addTile(tStart);
        actual.addTile(tile);
        actual.connectSides(wStart,e);
        assertThrows(IllegalArgumentException.class, () -> actual.removePawn(PlayerColor.PURPLE,nr1));
    }

    @Test
    void testRemovePawnOnNonTrivialTile(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        actual.addTile(tStart);
        actual.addTile(tile);
        actual.connectSides(wStart,e);
        actual.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.HUT,l8);
        actual.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN,nm0);
        actual.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN,f1);
        actual.removePawn(PlayerColor.BLUE,f1);

        Area<Zone.Forest> areaF = new Area<>(Set.of(f1),new ArrayList<>(),2);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0,nm2),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2,nm0),List.of(PlayerColor.RED),3);
        Area<Zone.Meadow> areaM2 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1,areaM2));

        Area<Zone.River> areaR1 = new Area<>(Set.of(r3,nr1),new ArrayList<>(),1);
        Area<Zone.River> areaR2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR1,areaR2));

        Area<Zone.Water> areaRS1 = new Area<>(Set.of(nr1,r3,l8),List.of(PlayerColor.YELLOW),1);
        Area<Zone.Water> areaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS1,areaRS2));

        ZonePartitions expected = new ZonePartitions(f,m,r,rs);
        assertEquals(expected,actual.build());
    }

    @Test
    void testRemovePawnOnNonTrivialTileOnLac(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        TileSide n = new TileSide.River(nm0,nr1,nm2);
        TileSide e = new TileSide.River(nm2,nr1,nm0);
        TileSide s = new TileSide.River(nm0,nr3,nm4);
        TileSide w = new TileSide.River(nm4,nr3,nm0);
        Tile tile = new Tile(17, Tile.Kind.NORMAL,n,e,s,w);

        TileSide nStart = new TileSide.Meadow(m0);
        TileSide eStart = new TileSide.Forest(f1);
        TileSide sStart = new TileSide.Forest(f1);
        TileSide wStart = new TileSide.River(m2,r3,m0);

        Tile tStart = new Tile(56, Tile.Kind.START,nStart,eStart,sStart,wStart);

        ZonePartitions.Builder actual = new ZonePartitions.Builder(ZonePartitions.EMPTY);

        actual.addTile(tStart);
        actual.addTile(tile);
        actual.connectSides(wStart,e);


        assertThrows(IllegalArgumentException.class, () -> actual.removePawn(PlayerColor.PURPLE,l8));
    }


    @Test
    void testclearGatherersOnNonTrivialTile(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);

        Zone.Forest nf1 = new Zone.Forest(750, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest nf2 = new Zone.Forest(751, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest nf3 = new Zone.Forest(752, Zone.Forest.Kind.PLAIN);

        Area<Zone.Forest> areaF1 = new Area<>(Set.of(f1),List.of(PlayerColor.PURPLE),2);
        Area<Zone.Forest> areaF2 = new Area<>(Set.of(nf1,nf2),new ArrayList<>(),2);
        Area<Zone.Forest> areaF3 = new Area<>(Set.of(nf3),List.of(PlayerColor.RED,PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.GREEN),4);
        Area<Zone.Forest> areaF3Wanted = new Area<>(Set.of(nf3),new ArrayList<>(),4);
        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF1,areaF2,areaF3));
        ZonePartition<Zone.Forest> fWanted = new ZonePartition(Set.of(areaF1,areaF2,areaF3Wanted));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0,nm2),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2,nm0),List.of(PlayerColor.RED),3);
        Area<Zone.Meadow> areaM2 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1,areaM2));

        Area<Zone.River> areaR1 = new Area<>(Set.of(r3,nr1),new ArrayList<>(),1);
        Area<Zone.River> areaR2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR1,areaR2));

        Area<Zone.Water> areaRS1 = new Area<>(Set.of(nr1,r3,l8),List.of(PlayerColor.YELLOW),1);
        Area<Zone.Water> areaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS1,areaRS2));

        ZonePartitions.Builder actual = new ZonePartitions.Builder(new ZonePartitions(f,m,r,rs));
        actual.clearGatherers(areaF3);
        ZonePartitions expected = new ZonePartitions(fWanted,m,r,rs);
        assertEquals(expected,actual.build());
    }

    @Test
    void testclearFishersOnNonTrivialTile(){
        // Partition avec la tuile de départ placé
        Zone.Meadow m0 = new Zone.Meadow(560, List.of(new Animal(5600, Animal.Kind.AUROCHS)),null);
        Zone.Forest f1 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow m2 = new Zone.Meadow(562,new ArrayList<>(),null);
        Zone.Lake l8 = new Zone.Lake(568,1,null);
        Zone.River r3 = new Zone.River(563,0,l8);

        Zone.Meadow nm0 = new Zone.Meadow(170,new ArrayList<>(),null);
        Zone.River nr1 = new Zone.River(171, 0,null);
        Zone.Meadow nm2 = new Zone.Meadow(172, List.of(new Animal(1700, Animal.Kind.DEER)), null);
        Zone.River nr3 = new Zone.River(173,0,null);
        Zone.Meadow nm4 = new Zone.Meadow(174, List.of(new Animal(1701, Animal.Kind.AUROCHS)),null);


        Area<Zone.Forest> areaF1 = new Area<>(Set.of(f1),List.of(PlayerColor.PURPLE),2);

        ZonePartition<Zone.Forest> f = new ZonePartition(Set.of(areaF1));

        Area<Zone.Meadow> areaM0 = new Area<>(Set.of(m0,nm2),new ArrayList<>(),2);
        Area<Zone.Meadow> areaM1 = new Area<>(Set.of(m2,nm0),List.of(PlayerColor.RED),3);
        Area<Zone.Meadow> areaM2 = new Area<>(Set.of(nm4),new ArrayList<>(),2);
        ZonePartition<Zone.Meadow> m = new ZonePartition(Set.of(areaM0,areaM1,areaM2));

        Area<Zone.River> areaR1 = new Area<>(Set.of(r3,nr1),List.of(PlayerColor.RED),1);
        Area<Zone.River> areaR2 = new Area<>(Set.of(nr3),List.of(PlayerColor.YELLOW,PlayerColor.YELLOW,PlayerColor.YELLOW),2);
        Area<Zone.River> areaRWanted = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.River> r = new ZonePartition(Set.of(areaR1,areaR2));
        ZonePartition<Zone.River> rWanted = new ZonePartition(Set.of(areaR1,areaRWanted));

        Area<Zone.Water> areaRS1 = new Area<>(Set.of(nr1,r3,l8),List.of(PlayerColor.YELLOW),1);
        Area<Zone.Water> areaRS2 = new Area<>(Set.of(nr3),new ArrayList<>(),2);
        ZonePartition<Zone.Water> rs = new ZonePartition(Set.of(areaRS1,areaRS2));

        ZonePartitions.Builder actual = new ZonePartitions.Builder(new ZonePartitions(f,m,r,rs));
        actual.clearFishers(areaR2);
        ZonePartitions expected = new ZonePartitions(f,m,rWanted,rs);
        assertEquals(expected,actual.build());
    }
}
