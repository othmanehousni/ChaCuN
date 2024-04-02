package ch.epfl.chacun;


import org.junit.jupiter.api.Test;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class Yannis {
    private Zone.Forest forest1 = new Zone.Forest(100 , Zone.Forest.Kind.WITH_MENHIR);
    private Zone.Forest forest2 = new Zone.Forest(101 , Zone.Forest.Kind.PLAIN);
    private Zone.Forest forest3 = new Zone.Forest(102 , Zone.Forest.Kind.WITH_MUSHROOMS);
    private Zone.Forest forest4 = new Zone.Forest(103 , Zone.Forest.Kind.WITH_MUSHROOMS);
    private Zone.Forest forest5 = new Zone.Forest(200 , Zone.Forest.Kind.WITH_MUSHROOMS);
    private Zone.Meadow meadow1 = new Zone.Meadow(201 , List.of() , null);
    private Zone.Meadow meadow2 = new Zone.Meadow(203 , List.of() , Zone.SpecialPower.PIT_TRAP);
    private Zone.Lake lake = new Zone.Lake(208,0,null);
    private Zone.River river1 = new Zone.River(202 , 0 , lake);
    private Zone.River river2 = new Zone.River(204 , 0 , lake);

    private TileSide.Forest sideForest= new TileSide.Forest(forest1);
    private TileSide.Forest sideForest2= new TileSide.Forest(forest2);
    private TileSide.Forest sideForest3= new TileSide.Forest(forest3);
    private TileSide.Forest sideForest4= new TileSide.Forest(forest4);
    private TileSide.Forest sideForest5= new TileSide.Forest(forest5);
    private TileSide.Meadow sideMeadow1 = new TileSide.Meadow(meadow1);
    private TileSide.Meadow sideMeadow2 = new TileSide.Meadow(meadow2);
    private TileSide.River sideRiver = new TileSide.River(meadow1 , river1 , meadow2);
    private TileSide.River sideRiver2 = new TileSide.River(meadow2 , river2 , meadow1);

    private Tile t1 = new Tile(10 , Tile.Kind.NORMAL , sideForest,sideForest2,sideForest3,sideForest4 );
    private Tile t2 = new Tile(20 , Tile.Kind.NORMAL , sideForest5 , sideRiver , sideMeadow2 , sideRiver2);

    @Test
    void testPersoStaticArgsWork(){
        assertEquals(12 , Board.REACH);
    }
    @Test
    void testPersoTileAtReturnNull(){
        Pos p1 = new Pos(-13 , 7);
        Pos p2 = new Pos(7,-13);
        Pos p3 = new Pos(13,7);
        Pos p4 = new Pos(7,13);
        Pos p5 = new Pos(0,0);

        Board b = Board.EMPTY;
        assertEquals(null , b.tileAt(p1));
        assertEquals(null , b.tileAt(p2));
        assertEquals(null , b.tileAt(p3));
        assertEquals(null , b.tileAt(p4));
        assertEquals(null , b.tileAt(p5));
    }
    @Test
    void testPersoTileAtWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);

        assertEquals(pt1 , b.tileAt(new Pos(-12,-11)));
        assertEquals(b.tileAt(new Pos(-12,-12)) , pt2);
    }
    @Test
    void testPersoTileWithIdThrows(){
        Board b = Board.EMPTY;
        assertThrows(IllegalArgumentException.class , ()->{b.tileWithId(10);});
        assertThrows(IllegalArgumentException.class , ()->{b.tileWithId(20);});

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , null );
        b.withNewTile(pt1);
        assertThrows(IllegalArgumentException.class , ()->{b.tileWithId(20);});
    }
    @Test
    void testPersoTileWithIdWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);

        assertEquals(pt1 , b.tileWithId(10));
        assertEquals(b.tileWithId(20) , pt2);
    }
    @Test
    void testPersoOccupantsWorks(){
        Board b = Board.EMPTY;

        Occupant o1 = new Occupant(Occupant.Kind.PAWN, 100);
        Occupant o2 = new Occupant(Occupant.Kind.HUT, 208);

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , o1 );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) ,o2);

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);

        assertEquals(Set.of(o1,o2) , b.occupants());
    }
    @Test
    void testPersoXAreaThrows(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        final Board b2=b.withNewTile(pt1);



        assertThrows(IllegalArgumentException.class , ()->{b2.forestArea(new Zone.Forest(222 , Zone.Forest.Kind.PLAIN));});
        assertThrows(IllegalArgumentException.class , ()->{b2.meadowArea(new Zone.Meadow(200 , List.of() , null));});
        assertThrows(IllegalArgumentException.class , ()->{b2.riverArea(new Zone.River(201 , 0 , lake));});
        assertThrows(IllegalArgumentException.class , ()->{b2.riverSystemArea(new Zone.Lake(5000,0,null));});

    }
    @Test
    void testPersoXAreaWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);
        b=b.withOccupant(new Occupant(Occupant.Kind.PAWN , 101));

        Area<Zone.Forest> area1= new Area(Set.of(forest1,forest5) , List.of() , 0);
        Area<Zone.Forest> area2= new Area(Set.of(forest2) , List.of(PlayerColor.BLUE) , 1);
        Area<Zone.Forest> area3= new Area(Set.of(forest3) , List.of() , 1);
        Area<Zone.Forest> area4= new Area(Set.of(forest4) , List.of() , 1);
        Area<Zone.Meadow> area5= new Area(Set.of(meadow1) , List.of() , 2);
        Area<Zone.Meadow> area6= new Area(Set.of(meadow2) , List.of() , 3);
        Area<Zone.River> area7= new Area(Set.of(river1) , List.of() , 1);
        Area<Zone.River> area8= new Area(Set.of(river2) , List.of() , 1);
        Area<Zone.River> area9= new Area(Set.of(river1 , lake , river2) , List.of() , 2);


        assertEquals(area1 , b.forestArea(forest1));
        assertEquals(area2 , b.forestArea(forest2));
        assertEquals(area3 , b.forestArea(forest3));
        assertEquals(area4 , b.forestArea(forest4));
        assertEquals(area5 , b.meadowArea(meadow1));
        assertEquals(area6 , b.meadowArea(meadow2));
        assertEquals(area7 , b.riverArea(river1));
        assertEquals(area8 , b.riverArea(river2));
        assertEquals(area9 , b.riverSystemArea(lake));
        assertEquals(area9 , b.riverSystemArea(river1));
        assertEquals(area9 , b.riverSystemArea(river2));
    }
    @Test
    void testPersoXAreasWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);

        Area<Zone.Meadow> area5= new Area(Set.of(meadow1) , List.of() , 2);
        Area<Zone.Meadow> area6= new Area(Set.of(meadow2) , List.of() , 3);
        Area<Zone.River> area9= new Area(Set.of(river1 , lake , river2) , List.of() , 2);

        assertEquals(Set.of(area5 , area6) , b.meadowAreas());
        assertEquals(Set.of(area9) , b.riverSystemAreas());
    }
    @Test
    void testPersoAdjacentMeadow(){
        Board b = Board.EMPTY;

        Zone.Meadow meadow01 = new Zone.Meadow(001 , List.of() , null);
        TileSide.Meadow sideMeadow01 = new TileSide.Meadow(meadow01);
        TileSide.Meadow sideMeadow02 = new TileSide.Meadow(meadow01);
        TileSide.Meadow sideMeadow03 = new TileSide.Meadow(meadow01);
        TileSide.Meadow sideMeadow04 = new TileSide.Meadow(meadow01);
        Tile t0 = new Tile(00 , Tile.Kind.NORMAL , sideMeadow01 , sideMeadow02 , sideMeadow03 , sideMeadow04 );

        Zone.Meadow meadow11 = new Zone.Meadow(101 , List.of() , null);
        TileSide.Meadow sideMeadow11 = new TileSide.Meadow(meadow11);
        TileSide.Meadow sideMeadow12 = new TileSide.Meadow(meadow11);
        TileSide.Meadow sideMeadow13 = new TileSide.Meadow(meadow11);
        TileSide.Meadow sideMeadow14 = new TileSide.Meadow(meadow11);
        Tile t1 = new Tile(10 , Tile.Kind.NORMAL , sideMeadow11 , sideMeadow12 , sideMeadow13 , sideMeadow14 );

        Zone.Meadow meadow21 = new Zone.Meadow(201 , List.of() , null);
        TileSide.Meadow sideMeadow21 = new TileSide.Meadow(meadow21);
        TileSide.Meadow sideMeadow22 = new TileSide.Meadow(meadow21);
        TileSide.Meadow sideMeadow23 = new TileSide.Meadow(meadow21);
        TileSide.Meadow sideMeadow24 = new TileSide.Meadow(meadow21);
        Tile t2 = new Tile(20 , Tile.Kind.NORMAL , sideMeadow21 , sideMeadow22 , sideMeadow23 , sideMeadow24 );

        Zone.Meadow meadow31 = new Zone.Meadow(301 , List.of() , null);
        TileSide.Meadow sideMeadow31 = new TileSide.Meadow(meadow31);
        TileSide.Meadow sideMeadow32 = new TileSide.Meadow(meadow31);
        TileSide.Meadow sideMeadow33 = new TileSide.Meadow(meadow31);
        TileSide.Meadow sideMeadow34 = new TileSide.Meadow(meadow31);
        Tile t3 = new Tile(30 , Tile.Kind.NORMAL , sideMeadow31 , sideMeadow32 , sideMeadow33 , sideMeadow34 );

        Zone.Meadow meadow41 = new Zone.Meadow(401 , List.of() , null);
        TileSide.Meadow sideMeadow41 = new TileSide.Meadow(meadow41);
        TileSide.Meadow sideMeadow42 = new TileSide.Meadow(meadow41);
        TileSide.Meadow sideMeadow43 = new TileSide.Meadow(meadow41);
        TileSide.Meadow sideMeadow44 = new TileSide.Meadow(meadow41);
        Tile t4 = new Tile(40 , Tile.Kind.NORMAL , sideMeadow41 , sideMeadow42 , sideMeadow43 , sideMeadow44 );

        Zone.Meadow meadow51 = new Zone.Meadow(501 , List.of() , null);
        TileSide.Meadow sideMeadow51 = new TileSide.Meadow(meadow51);
        TileSide.Meadow sideMeadow52 = new TileSide.Meadow(meadow51);
        TileSide.Meadow sideMeadow53 = new TileSide.Meadow(meadow51);
        TileSide.Meadow sideMeadow54 = new TileSide.Meadow(meadow51);
        Tile t5 = new Tile(50 , Tile.Kind.NORMAL , sideMeadow51 , sideMeadow52 , sideMeadow53 , sideMeadow54 );

        Zone.Meadow meadow61 = new Zone.Meadow(601 , List.of() , null);
        TileSide.Meadow sideMeadow61 = new TileSide.Meadow(meadow61);
        TileSide.Meadow sideMeadow62 = new TileSide.Meadow(meadow61);
        TileSide.Meadow sideMeadow63 = new TileSide.Meadow(meadow61);
        TileSide.Meadow sideMeadow64 = new TileSide.Meadow(meadow61);
        Tile t6 = new Tile(60 , Tile.Kind.NORMAL , sideMeadow61 , sideMeadow62 , sideMeadow63 , sideMeadow64 );

        Zone.Meadow meadow71 = new Zone.Meadow(701 , List.of() , null);
        TileSide.Meadow sideMeadow71 = new TileSide.Meadow(meadow71);
        TileSide.Meadow sideMeadow72 = new TileSide.Meadow(meadow71);
        TileSide.Meadow sideMeadow73 = new TileSide.Meadow(meadow71);
        TileSide.Meadow sideMeadow74 = new TileSide.Meadow(meadow71);
        Tile t7 = new Tile(70 , Tile.Kind.NORMAL , sideMeadow71 , sideMeadow72 , sideMeadow73 , sideMeadow74 );

        Zone.Meadow meadow81 = new Zone.Meadow(801 , List.of() , null);
        TileSide.Meadow sideMeadow81 = new TileSide.Meadow(meadow81);
        TileSide.Meadow sideMeadow82 = new TileSide.Meadow(meadow81);
        TileSide.Meadow sideMeadow83 = new TileSide.Meadow(meadow81);
        TileSide.Meadow sideMeadow84 = new TileSide.Meadow(meadow81);
        Tile t8 = new Tile(80 , Tile.Kind.NORMAL , sideMeadow81 , sideMeadow82 , sideMeadow83 , sideMeadow84 );

        Zone.Meadow meadow91 = new Zone.Meadow(901 , List.of() , null);
        TileSide.Meadow sideMeadow91 = new TileSide.Meadow(meadow91);
        TileSide.Meadow sideMeadow92 = new TileSide.Meadow(meadow91);
        TileSide.Meadow sideMeadow93 = new TileSide.Meadow(meadow91);
        TileSide.Meadow sideMeadow94 = new TileSide.Meadow(meadow91);
        Tile t9 = new Tile(90 , Tile.Kind.NORMAL , sideMeadow91 , sideMeadow92 , sideMeadow93 , sideMeadow94 );



        PlacedTile pt0 = new PlacedTile(t0 , null , Rotation.NONE , new Pos(0 , 0) , null );
        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(0 , -1) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.NONE , new Pos(-1 , -1) , null );
        PlacedTile pt3 = new PlacedTile(t3 , null , Rotation.NONE , new Pos(1 , -1) , null );
        PlacedTile pt4 = new PlacedTile(t4 , null , Rotation.NONE , new Pos(-1 , 0) , null );
        PlacedTile pt5 = new PlacedTile(t5 , null , Rotation.NONE , new Pos(1 , 0) , null );
        PlacedTile pt6 = new PlacedTile(t6 , null , Rotation.NONE , new Pos(0 , 1) , null );
        PlacedTile pt7 = new PlacedTile(t7 , null , Rotation.NONE , new Pos(-1 , 1) , null );
        PlacedTile pt8 = new PlacedTile(t8 , null , Rotation.NONE , new Pos(1 , 1) , null );
        PlacedTile pt9 = new PlacedTile(t9 , PlayerColor.BLUE , Rotation.NONE , new Pos(2 , 0) , null );


        b=b.withNewTile(pt0);
        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);
        b=b.withNewTile(pt3);
        b=b.withNewTile(pt4);
        b=b.withNewTile(pt5);
        b=b.withNewTile(pt6);
        b=b.withNewTile(pt7);
        b=b.withNewTile(pt8);
        b=b.withNewTile(pt9);


        Set zones = Set.of(meadow01, meadow11, meadow21, meadow31, meadow41, meadow51, meadow61, meadow71, meadow81);
        Area<Zone.Meadow> expected = new Area(zones , List.of(),0);
        Area<Zone.Meadow> a= b.adjacentMeadow(new Pos(0,0) , meadow01);
        assertEquals(expected , b.adjacentMeadow(new Pos(0,0) , meadow01));
    }
    @Test
    void testPersoOccupantCountWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , new Occupant(Occupant.Kind.PAWN , 100) );
        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.BLUE , Rotation.HALF_TURN , new Pos(-12,-12) , new Occupant(Occupant.Kind.PAWN, 201));

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);

        assertEquals(2 , b.occupantCount(PlayerColor.BLUE , Occupant.Kind.PAWN));
        assertEquals(0 , b.occupantCount(PlayerColor.YELLOW , Occupant.Kind.PAWN));
        assertEquals(0 , b.occupantCount(PlayerColor.BLUE , Occupant.Kind.HUT));

        PlacedTile pt3 = new PlacedTile(t2 , PlayerColor.YELLOW, Rotation.NONE , new Pos(-12,-10) , new Occupant(Occupant.Kind.HUT, 208));

        b=b.withNewTile(pt3);

        assertEquals(1 , b.occupantCount(PlayerColor.YELLOW , Occupant.Kind.HUT));
    }
    @Test
    void testPersoInsertionPositionsWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , new Occupant(Occupant.Kind.PAWN , 100) );
        b=b.withNewTile(pt1);
        assertEquals(Set.of(new Pos(-12,-12) , new Pos(-11,-11) , new Pos(-12,-10)) , b.insertionPositions());

        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.BLUE , Rotation.HALF_TURN , new Pos(-12,-12) , new Occupant(Occupant.Kind.PAWN, 201));
        b=b.withNewTile(pt2);
        assertEquals(Set.of(new Pos(-11,-12) , new Pos(-11,-11) , new Pos(-12,-10)) , b.insertionPositions());

        Board b2 = Board.EMPTY;
        PlacedTile pt3 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(0 , 0) , new Occupant(Occupant.Kind.PAWN , 100) );
        b2=b2.withNewTile(pt3);
        assertEquals(Set.of(new Pos(-1,0) , new Pos(1,0) , new Pos(0,-1) , new Pos(0,1)) , b2.insertionPositions());
    }

    @Test
    void testPersoLastPlacedTile(){
        Board b = Board.EMPTY;

        assertEquals(null , b.lastPlacedTile());

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , new Occupant(Occupant.Kind.PAWN , 100) );
        b=b.withNewTile(pt1);
        assertEquals (pt1 , b.lastPlacedTile());

        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.BLUE , Rotation.HALF_TURN , new Pos(-12,-12) , new Occupant(Occupant.Kind.PAWN, 201));
        b=b.withNewTile(pt2);
        assertEquals (pt2 , b.lastPlacedTile());
    }
    @Test
    void testPersoForestsClosedByLastPlacedTileWorks(){
        Board b = Board.EMPTY;
        assertEquals(Set.of() , b.forestsClosedByLastTile());

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        b=b.withNewTile(pt1);
        assertEquals(Set.of() , b.forestsClosedByLastTile());

        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);
        b=b.withNewTile(pt2);
        Area<Zone.Forest> area = new Area(Set.of(forest1 , forest5) , List.of() , 0);
        assertEquals(Set.of(area) , b.forestsClosedByLastTile());


        PlacedTile pt3 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(0 , 0) , null );
        Zone.Forest forest10 = new Zone.Forest(500 , Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest11 = new Zone.Forest(501 , Zone.Forest.Kind.PLAIN);
        Zone.Forest forest12 = new Zone.Forest(502 , Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest13 = new Zone.Forest(503 , Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide.Forest sideForest10 = new TileSide.Forest(forest10);
        TileSide.Forest sideForest11= new TileSide.Forest(forest11);
        TileSide.Forest sideForest12= new TileSide.Forest(forest12);
        TileSide.Forest sideForest13= new TileSide.Forest(forest13);
        Tile t555 = new Tile(50 , Tile.Kind.NORMAL , sideForest10 , sideForest11 , sideForest12 , sideForest13);
        PlacedTile pt6 = new PlacedTile(t555 , PlayerColor.BLUE , Rotation.NONE , new Pos(1 , 1) , null );

        Zone.Forest forest5 = new Zone.Forest(300 , Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest6 = new Zone.Forest(301 , Zone.Forest.Kind.PLAIN);
        Zone.Forest forest7 = new Zone.Forest(302 , Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest8 = new Zone.Forest(303 , Zone.Forest.Kind.WITH_MUSHROOMS);


        TileSide.Forest sideForest = new TileSide.Forest(forest5);
        TileSide.Forest sideForest2= new TileSide.Forest(forest6);
        TileSide.Forest sideForest3= new TileSide.Forest(forest7);
        TileSide.Forest sideForest4= new TileSide.Forest(forest8);

        Tile t3 = new Tile(30 , Tile.Kind.NORMAL , sideForest , sideForest2 , sideForest3 , sideForest4);

        PlacedTile pt4 = new PlacedTile(t3 , PlayerColor.BLUE , Rotation.NONE , new Pos(1 , 0) , null );
        PlacedTile pt5 = new PlacedTile(t3 , PlayerColor.BLUE , Rotation.NONE , new Pos(0 , 1) , null );
        Board b2 = Board.EMPTY;

        b2=b2.withNewTile(pt3);
        b2=b2.withNewTile(pt4);
        b2= b2.withNewTile(pt5);
        b2=b2.withNewTile(pt6);


        Area<Zone.Forest> area34 = new Area(Set.of(forest6, forest13) , List.of() , 0);
        Area<Zone.Forest> area2 = new Area(Set.of(forest7, forest10) , List.of() , 0);
        assertEquals(Set.of(area34, area2) , b2.forestsClosedByLastTile());
    }
    @Test
    void testPersoRiversClosedByLastPlacedTileWorks(){
        Board b2 = Board.EMPTY;
        assertEquals(Set.of() , b2.forestsClosedByLastTile());

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(0 , 0) , null );
        b2=b2.withNewTile(pt1);
        assertEquals(Set.of() , b2.forestsClosedByLastTile());

        Board b = Board.EMPTY;
        b=b.withNewTile(pt1);

        Zone.Forest forest1 = new Zone.Forest(300 , Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadow11 = new Zone.Meadow(301 , List.of() , null);
        Zone.Meadow meadow12 = new Zone.Meadow(301 , List.of() , null);
        Zone.Lake lake = new Zone.Lake(308,0,null);
        Zone.River river1 = new Zone.River(302 , 0 , lake);
        TileSide.Forest ts1 = new TileSide.Forest(forest1);
        TileSide.River ts2 = new TileSide.River(meadow11,river1,meadow12);
        Tile tile1 = new Tile(30 , Tile.Kind.NORMAL , ts1,ts1,ts2,ts1);
        PlacedTile pt3 = new PlacedTile(tile1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-1 , 0) , null );

        Zone.Forest forest2 = new Zone.Forest(400 , Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow21 = new Zone.Meadow(401 , List.of() , null);
        Zone.Meadow meadow22 = new Zone.Meadow(403 , List.of() , null);
        Zone.Lake lake2 = new Zone.Lake(408,0,null);
        Zone.River river2 = new Zone.River(402 , 0 , lake2);
        TileSide.Forest ts3 = new TileSide.Forest(forest1);
        TileSide.River ts4 = new TileSide.River(meadow21,river2,meadow22);
        Tile tile2 = new Tile(40 , Tile.Kind.NORMAL , ts3,ts3,ts4,ts3);
        PlacedTile pt4 = new PlacedTile(tile2 , PlayerColor.BLUE , Rotation.RIGHT , new Pos(0 , 1) , null );

        b=b.withNewTile(pt3);
        b=b.withNewTile(pt4);

        Zone.Meadow meadow31 = new Zone.Meadow(500 , List.of() , null);
        Zone.Lake lake32 = new Zone.Lake(508,0,null);
        Zone.River river32 = new Zone.River(501 , 0 , lake32);
        Zone.Lake lake33 = new Zone.Lake(509,0,null);
        Zone.River river33 = new Zone.River(502 , 0 , lake33);
        TileSide.River ts5 = new TileSide.River(meadow31,river32,meadow31);
        TileSide.River ts6 = new TileSide.River(meadow31,river33,meadow31);
        TileSide.Meadow ts7 = new TileSide.Meadow(meadow31);
        Tile tile3 = new Tile(50 , Tile.Kind.NORMAL , ts5,ts6,ts7,ts7);
        PlacedTile pt5 = new PlacedTile(tile3 , PlayerColor.BLUE , Rotation.NONE , new Pos(-1 , 1) , null );

        b=b.withNewTile(pt5);

        Area<Zone.River> area1 = new Area(Set.of(river32 , river1) , List.of() , 0);
        Area<Zone.River> area2 = new Area(Set.of(river33 , river2) , List.of() , 0);
        assertEquals(Set.of(area1,area2) , b.riversClosedByLastTile());
    }
    @Test
    void testPersoCanAddTileWorks(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        b=b.withNewTile(pt1);

        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);
        PlacedTile pt3 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-10) , null);
        PlacedTile pt4 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(8,7) , null);
        assertTrue(b.canAddTile(pt2));
        assertFalse(b.canAddTile(pt3));
        assertFalse(b.canAddTile(pt4));
    }
    @Test
    void testPersoCouldAddTileWorks(){
        Board b = Board.EMPTY;
        //assertTrue(b.couldPlaceTile(t1));

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(0, 0) , null );
        b=b.withNewTile(pt1);

        assertTrue(b.couldPlaceTile(t2));

        Tile tile = new Tile(50 , Tile.Kind.NORMAL , sideMeadow1,sideRiver,sideMeadow2,sideRiver2);
        assertFalse(b.couldPlaceTile(tile));
    }
    @Test
    void testPersoWithNewTileThrows(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(0, 0) , null );
        b=b.withNewTile(pt1);
        Tile tile = new Tile(50 , Tile.Kind.NORMAL , sideMeadow1,sideRiver,sideMeadow2,sideRiver2);
        PlacedTile pt2 = new PlacedTile(tile , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);
        PlacedTile pt3 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-10) , null);
        PlacedTile pt4 = new PlacedTile(t1 , null , Rotation.HALF_TURN , new Pos(-12,-11) , null);
        final Board b2 =b;
        assertThrows(IllegalArgumentException.class , ()->{b2.withNewTile(pt2);});
        assertThrows(IllegalArgumentException.class , ()->{b2.withNewTile(pt3);});
        assertThrows(IllegalArgumentException.class , ()->{b2.withNewTile(pt4);});
    }
    @Test
    void testPersoWithNewTileWorks(){
        Board b = Board.EMPTY;
        Board b2 = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        assertEquals(b.withNewTile(pt1) , b2.withNewTile(pt1));
        assertEquals(b.withNewTile(pt2) , b2.withNewTile(pt2));
        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);
        b2=b2.withNewTile(pt1);
        b2=b2.withNewTile(pt2);
        assertEquals(b,b2);
    }

    @Test
    void testPersoWithOccupantThrows(){
        Board b = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , null , Rotation.NONE , new Pos(-12 , -11) , new Occupant(Occupant.Kind.PAWN , 100) );
        PlacedTile pt2 = new PlacedTile(t2 , null , Rotation.HALF_TURN , new Pos(-12,-12) , new Occupant(Occupant.Kind.HUT , 208));

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);
        final Board b2 = b;
        assertThrows(IllegalArgumentException.class , ()-> {b2.withOccupant(new Occupant(Occupant.Kind.PAWN , 101));});
        assertThrows(IllegalArgumentException.class , ()-> {b2.withOccupant(new Occupant(Occupant.Kind.HUT , 208));});
    }

    @Test
    void testPersoWithoutOccupant(){
        Board b = Board.EMPTY;
        Board b2 = Board.EMPTY;


        Occupant df = new Occupant(Occupant.Kind.PAWN, 100);
        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.YELLOW , Rotation.HALF_TURN , new Pos(-12,-12) ,null );

        Zone.Forest forest1 = new Zone.Forest(100 , Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest2 = new Zone.Forest(101 , Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(102 , Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest4 = new Zone.Forest(103 , Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest5 = new Zone.Forest(200 , Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Meadow meadow1 = new Zone.Meadow(201 , List.of() , null);
        Zone.Meadow meadow2 = new Zone.Meadow(203 , List.of() , Zone.SpecialPower.PIT_TRAP);
        Zone.Lake lake = new Zone.Lake(208,0,null);
        Zone.River river1 = new Zone.River(202 , 0 , lake);
        Zone.River river2 = new Zone.River(204 , 0 , lake);

        TileSide.Forest sideForest= new TileSide.Forest(forest1);
        TileSide.Forest sideForest2= new TileSide.Forest(forest2);
        TileSide.Forest sideForest3= new TileSide.Forest(forest3);
        TileSide.Meadow sideMeadow1 = new TileSide.Meadow(meadow1);
        TileSide.Meadow sideMeadow2 = new TileSide.Meadow(meadow2);
        TileSide.River sideRiver = new TileSide.River(meadow1 , river1 , meadow2);
        TileSide.River sideRiver2 = new TileSide.River(meadow2 , river2 , meadow1);
        TileSide.Forest sideForest4= new TileSide.Forest(forest4);
        TileSide.Forest sideForest5= new TileSide.Forest(forest5);
        Tile t3 = new Tile(10 , Tile.Kind.NORMAL , sideForest,sideForest2,sideForest3,sideForest4 );
        Tile t4 = new Tile(20 , Tile.Kind.NORMAL , sideForest5 , sideRiver , sideMeadow2 , sideRiver2);


        PlacedTile pt3 = new PlacedTile(t3 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt4 = new PlacedTile(t4 , PlayerColor.YELLOW , Rotation.HALF_TURN , new Pos(-12,-12) ,null);

        b=b.withNewTile(pt1);
        b = b.withNewTile(pt2);
        b= b.withOccupant(df);
        b=b.withOccupant(new Occupant(Occupant.Kind.HUT , 208));
        b2=b2.withNewTile(pt3);
        b2=b2.withNewTile(pt4);
        b2=b2.withOccupant(new Occupant(Occupant.Kind.HUT , 208));
        b = b.withoutOccupant(df);

        assertEquals(b,b2);
        //assertThrows(NullPointerException.class, () -> {b = b.withoutOccupant(new Occupant(Occupant.Kind.HUT,201));});

    }

    @Test
    void testPersoWithOccupant(){
        Board b = Board.EMPTY;
        Board b2 = Board.EMPTY;

        Occupant df = new Occupant(Occupant.Kind.PAWN, 100);
        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.YELLOW , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        PlacedTile pt3 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );

        b = b.withNewTile(pt1);
        b = b.withNewTile(pt2);

        b2 = b2.withNewTile(pt3);
        b2 = b2.withNewTile(pt2);

        b = b.withOccupant(df);
        b2 = b2.withOccupant(df);

        assertTrue(b.equals(b2));

    }
    @Test
    void testPersoClearGatherersOrFishersInWorks(){

        Board b = Board.EMPTY;
        Board b2 = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.YELLOW , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        Tile t3 = new Tile(10 , Tile.Kind.NORMAL , sideForest,sideForest2,sideForest3,sideForest4 );
        Tile t4 = new Tile(20 , Tile.Kind.NORMAL , sideForest5 , sideRiver , sideMeadow2 , sideRiver2);
        PlacedTile pt3 = new PlacedTile(t3 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , null );
        PlacedTile pt4 = new PlacedTile(t4 , PlayerColor.YELLOW , Rotation.HALF_TURN , new Pos(-12,-12) , null);

        b = b.withNewTile(pt1);
        b = b.withNewTile(pt2);
        b2 = b2.withNewTile(pt3);
        b2 = b2.withNewTile(pt4);
        b=b.withOccupant(new Occupant(Occupant.Kind.PAWN , 100));
        b=b.withOccupant(new Occupant(Occupant.Kind.PAWN , 203));
        b2=b2.withOccupant(new Occupant(Occupant.Kind.PAWN , 203));


        Area<Zone.Forest> area1 = new Area<>(Set.of(forest2) , List.of() , 1);
        Area<Zone.Forest> area2 = new Area<>(Set.of(forest1 , forest5) , List.of(PlayerColor.BLUE) , 0);
        Area<Zone.River> area3 = new Area<>(Set.of(river1) , List.of() , 1);
        b=b.withoutGatherersOrFishersIn(Set.of(area1, area2) , Set.of(area3));

        assertTrue(b.equals(b2));

        Area<Zone.River> area4 = new Area<>(Set.of(river1) , List.of(PlayerColor.YELLOW) , 1);

        b=b.withoutOccupant(new Occupant(Occupant.Kind.PAWN , 203));
        b2=b2.withoutOccupant(new Occupant(Occupant.Kind.PAWN, 203));
        b=b.withOccupant(new Occupant(Occupant.Kind.PAWN , 202));
        b=b.withoutGatherersOrFishersIn(Set.of(area1) , Set.of(area4));
        assertTrue(b.equals(b2));
    }
    @Test
    void testPersoWithMoreCancelledAnimals(){
        Board b = Board.EMPTY;
        Board b2 = Board.EMPTY;
        Board b3 = Board.EMPTY;

        PlacedTile pt1 = new PlacedTile(t1 , PlayerColor.BLUE , Rotation.NONE , new Pos(-12 , -11) , new Occupant(Occupant.Kind.PAWN , 100) );
        PlacedTile pt2 = new PlacedTile(t2 , PlayerColor.BLUE , Rotation.HALF_TURN , new Pos(-12,-12) , new Occupant(Occupant.Kind.PAWN, 201));

        b=b.withNewTile(pt1);
        b=b.withNewTile(pt2);
        b2=b2.withNewTile(pt1);
        b2=b2.withNewTile(pt2);
        b3=b3.withNewTile(pt1);
        b3=b3.withNewTile(pt2);
        Set<Animal> newCanc = Set.of( new Animal(2030 , Animal.Kind.MAMMOTH) , new Animal(2031 , Animal.Kind.DEER) , new Animal(2032 , Animal.Kind.TIGER) , new Animal(2010 , Animal.Kind.AUROCHS));
        Set<Animal> newCanc2 = Set.of( new Animal(2030 , Animal.Kind.MAMMOTH) , new Animal(2031 , Animal.Kind.DEER) , new Animal(2032 , Animal.Kind.TIGER) , new Animal(2033 , Animal.Kind.AUROCHS));
        b=b.withMoreCancelledAnimals(newCanc);
        b2=b2.withMoreCancelledAnimals(newCanc2);
        b3=b3.withMoreCancelledAnimals(newCanc);
        assertFalse(b.equals(b2));
        assertTrue(b.equals(b3));
    }

}
