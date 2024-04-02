package ch.epfl.chacun;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

public class MyZonePartitionsTest2 {

    @Test
    void addTileWorksWhenAddOneTile(){
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);

        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56), List.of(), 1);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56), List.of(), 1);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56), List.of(), 1);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);


        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuilt);

    }


    @Test
    void addTileWorksWhenAddTwoTiles(){

        //Tile 56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);


        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL,tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);

        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56), List.of(), 1);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone0_Tile17), List.of(), 4);
        Area<Zone.Meadow> meadowArea4Expected = new Area<>(Set.of(meadowZone2_Tile17), List.of(), 2);
        Area<Zone.Meadow> meadowArea5Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone1_Tile17), List.of(), 2);
        Area<Zone.River> riverArea3Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);

        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56), List.of(), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone1_Tile17), List.of(), 2);
        Area<Zone.Water> riverSystemArea3Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);



        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected, meadowArea4Expected, meadowArea5Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected, riverArea3Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected, riverSystemArea3Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);


        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuilt);

    }


    @Test
    void connectSidesWorks(){


        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL,tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);

        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);


        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuilt);

    }

    void addInitialOccupantWorksOnGoodCase(){


        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL,tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        //Expected


        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(PlayerColor.YELLOW), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(PlayerColor.RED), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(PlayerColor.PURPLE), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);


        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuilt);

    }

    void addInitialOccupantWorksWhenAlreadyOccupied() {


        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, riverZone1_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        //zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);

        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56));

    }



    @Test
    void addInitialOccupantWorksWhenNotGoodKindOfOccupant() {


        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);


        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, forestZone1_Tile56));
        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.HUT, meadowZone2_Tile17));
        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, meadowZone0_Tile17));
        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.HUT, meadowZone4_Tile17));
        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.PAWN, lakeZone8_Tile56));

    }



    //Est ce qu'on a bien compris la consigne?, enft jcois que c bon dcp chill
    void addInitialOccupantWorksForRiverSystemWithLake() {

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);

        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.HUT, riverZone3_Tile56));

    }



    void removePawnWorksOnNormalCase(){

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL,tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        zonePartitionsBuilderAgain.removePawn(PlayerColor.BLUE, forestZone1_Tile56);
        zonePartitionsBuilderAgain.removePawn(PlayerColor.YELLOW, meadowZone0_Tile56);
        zonePartitionsBuilderAgain.removePawn(PlayerColor.RED, meadowZone2_Tile56);
        zonePartitionsBuilderAgain.removePawn(PlayerColor.PURPLE, meadowZone4_Tile17);

        ZonePartitions zonePartitionsBuiltAgain = zonePartitionsBuilderAgain.build();

        //Expected


        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);


        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuiltAgain);

    }


    @Test
    void removePawnWorksWhenLake(){

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL,tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        //Test

        assertThrows(IllegalArgumentException.class, () -> zonePartitionsBuilderAgain.removePawn(PlayerColor.GREEN, lakeZone8_Tile56));


    }


    void clearGatherersWorksWhenOccupied() {

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        Area<Zone.Forest> forestAreaToClear = new Area<>(Set.of(forestZone1_Tile56), List.of(PlayerColor.BLUE), 2);

        zonePartitionsBuilderAgain.clearGatherers(forestAreaToClear);

        ZonePartitions zonePartitionsBuiltAgain = zonePartitionsBuilderAgain.build();


        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(PlayerColor.YELLOW), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(PlayerColor.RED), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(PlayerColor.PURPLE), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);

        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuiltAgain);

    }


    void clearGatherersWorksWhenUnoccupied() {

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        //zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.HUT, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        Area<Zone.Forest> forestAreaToClear = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);

        zonePartitionsBuilderAgain.clearGatherers(forestAreaToClear);

        ZonePartitions zonePartitionsBuiltAgain = zonePartitionsBuilderAgain.build();


        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(PlayerColor.YELLOW), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(PlayerColor.RED), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(PlayerColor.PURPLE), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);

        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuiltAgain);

    }


    void clearFishersWorksWhenOccupied() {

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        Area<Zone.River> riverAreaToClear = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);

        zonePartitionsBuilderAgain.clearFishers(riverAreaToClear);

        ZonePartitions zonePartitionsBuiltAgain = zonePartitionsBuilderAgain.build();


        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(PlayerColor.YELLOW), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(PlayerColor.RED), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(PlayerColor.PURPLE), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(PlayerColor.BLUE), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);

        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuiltAgain);

    }


    @Test
    void clearFishersWorksWhenUnoccupied() {

        //Tile56
        Animal aurochInMeadowZone1_Tile56 = new Animal(5600, Animal.Kind.AUROCHS);

        Zone.Lake lakeZone8_Tile56 = new Zone.Lake(568, 1, null);
        Zone.Meadow meadowZone0_Tile56 = new Zone.Meadow(560, List.of(aurochInMeadowZone1_Tile56), null);
        Zone.Forest forestZone1_Tile56 = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Meadow meadowZone2_Tile56 = new Zone.Meadow(562, List.of(), null);
        Zone.River riverZone3_Tile56 = new Zone.River(563, 0, lakeZone8_Tile56);

        TileSide tileSideN_Tile56 = new TileSide.Meadow(meadowZone0_Tile56);
        TileSide tileSideE_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideS_Tile56 = new TileSide.Forest(forestZone1_Tile56);
        TileSide tileSideW_Tile56 = new TileSide.River(meadowZone2_Tile56, riverZone3_Tile56, meadowZone0_Tile56);


        Tile tile56 = new Tile(56, Tile.Kind.START, tileSideN_Tile56, tileSideE_Tile56, tileSideS_Tile56,
                tileSideW_Tile56);

        //Tile17

        Animal deerInMeadowZone2_Tile17 = new Animal(1700, Animal.Kind.DEER);
        Animal smilodonInMeadowZone4_Tile17 = new Animal(1701, Animal.Kind.TIGER);

        Zone.Meadow meadowZone0_Tile17 = new Zone.Meadow(170, List.of(), null);
        Zone.River riverZone1_Tile17 = new Zone.River(171, 0, null);
        Zone.Meadow meadowZone2_Tile17 = new Zone.Meadow(172, List.of(deerInMeadowZone2_Tile17), null);
        Zone.River riverZone3_Tile17 = new Zone.River(173, 0, null);
        Zone.Meadow meadowZone4_Tile17 = new Zone.Meadow(174, List.of(smilodonInMeadowZone4_Tile17), null);

        TileSide tileSideN_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone1_Tile17, meadowZone2_Tile17);
        TileSide tileSideE_Tile17 = new TileSide.River(meadowZone2_Tile17, riverZone1_Tile17, meadowZone0_Tile17);
        TileSide tileSideS_Tile17 = new TileSide.River(meadowZone0_Tile17, riverZone3_Tile17, meadowZone4_Tile17);
        TileSide tileSideW_Tile17 = new TileSide.River(meadowZone4_Tile17, riverZone3_Tile17, meadowZone0_Tile17);

        Tile tile17 = new Tile(17, Tile.Kind.NORMAL, tileSideN_Tile17, tileSideE_Tile17, tileSideS_Tile17, tileSideW_Tile17);


        //ZonePartitions

        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(ZonePartitions.EMPTY);
        zonePartitionsBuilder.addTile(tile56);
        zonePartitionsBuilder.addTile(tile17);
        zonePartitionsBuilder.connectSides(tileSideE_Tile17, tileSideW_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, forestZone1_Tile56);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.YELLOW, Occupant.Kind.PAWN, meadowZone2_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.RED, Occupant.Kind.PAWN, meadowZone0_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.PURPLE, Occupant.Kind.PAWN, meadowZone4_Tile17);
        //zonePartitionsBuilder.addInitialOccupant(PlayerColor.BLUE, Occupant.Kind.PAWN, riverZone3_Tile17);
        zonePartitionsBuilder.addInitialOccupant(PlayerColor.GREEN, Occupant.Kind.HUT, lakeZone8_Tile56);


        ZonePartitions zonePartitionsBuilt = zonePartitionsBuilder.build();

        ZonePartitions.Builder zonePartitionsBuilderAgain = new ZonePartitions.Builder(zonePartitionsBuilt);

        Area<Zone.River> riverAreaToClear = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);

        zonePartitionsBuilderAgain.clearFishers(riverAreaToClear);

        ZonePartitions zonePartitionsBuiltAgain = zonePartitionsBuilderAgain.build();


        //Expected

        Area<Zone.Forest> forestArea1Expected = new Area<>(Set.of(forestZone1_Tile56), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Meadow> meadowArea1Expected = new Area<>(Set.of(meadowZone0_Tile56, meadowZone2_Tile17), List.of(PlayerColor.YELLOW), 2);
        Area<Zone.Meadow> meadowArea2Expected = new Area<>(Set.of(meadowZone2_Tile56, meadowZone0_Tile17), List.of(PlayerColor.RED), 3);
        Area<Zone.Meadow> meadowArea3Expected = new Area<>(Set.of(meadowZone4_Tile17), List.of(PlayerColor.PURPLE), 2);
        Area<Zone.River> riverArea1Expected = new Area<>(Set.of(riverZone3_Tile56, riverZone1_Tile17), List.of(), 1);
        Area<Zone.River> riverArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);
        Area<Zone.Water> riverSystemArea1Expected = new Area<>(Set.of(riverZone3_Tile56, lakeZone8_Tile56, riverZone1_Tile17), List.of(PlayerColor.GREEN), 1);
        Area<Zone.Water> riverSystemArea2Expected = new Area<>(Set.of(riverZone3_Tile17), List.of(), 2);


        ZonePartition<Zone.Forest> forestZonePartitionExpected = new ZonePartition<>(Set.of(forestArea1Expected));
        ZonePartition<Zone.Meadow> meadowZonePartitionExpected = new ZonePartition<>(Set.of(meadowArea1Expected, meadowArea2Expected, meadowArea3Expected));
        ZonePartition<Zone.River> riverZonePartitionExpected = new ZonePartition<>(Set.of(riverArea1Expected, riverArea2Expected));
        ZonePartition<Zone.Water> riverSystemPartitionExpected = new ZonePartition<>(Set.of(riverSystemArea1Expected, riverSystemArea2Expected));

        ZonePartitions zonePartitionsExpected = new ZonePartitions(forestZonePartitionExpected, meadowZonePartitionExpected, riverZonePartitionExpected, riverSystemPartitionExpected);

        //Test

        assertEquals(zonePartitionsExpected, zonePartitionsBuiltAgain);

    }


}
