package ch.epfl.chacun;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Zone;

import org.junit.jupiter.api.Test;

import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;

import static ch.epfl.chacun.Animal.Kind.MAMMOTH;
import static ch.epfl.chacun.Zone.Forest.Kind.PLAIN;
import static ch.epfl.chacun.Zone.SpecialPower.SHAMAN;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTileSideTest2 {

    //Je crerai une instance de la claxse tilesideforest
    //si on peut creer une instance de tilesideforest

    // un test par methode

    // assertequals(Foret.zone,


    //asserthrows pour les exceptions


    @Test
    void TestInstanceTileside() {
        Zone.Forest forestTest = new Zone.Forest(05, PLAIN);
        List<Zone> expectedList = new ArrayList<>();
        expectedList.add(forestTest);
        TileSide Tiletest = new TileSide.Forest(forestTest);
        assertEquals(Tiletest.zones(), expectedList);
    }

    @Test
    void ForestZonesAndIsSameKindAsWorks() {
        Zone.Forest test1 = new Zone.Forest(5, PLAIN);
        TileSide.Forest test2 = new TileSide.Forest(test1);
        TileSide.Forest test3 = new TileSide.Forest(test1);
        TileSide.Forest test4 = new TileSide.Forest(new Zone.Forest(4, PLAIN));
        List<Animal>test6 = new ArrayList<>();
        test6.add(new Animal(7, MAMMOTH));
        TileSide.Meadow test5 = new TileSide.Meadow(new Zone.Meadow(4,test6,SHAMAN ));
        List<Zone> list = new ArrayList<>();
        list.add(test1);
        assertEquals(list, test2.zones());
        assertEquals(true, test2.isSameKindAs(test3));
        assertEquals(true, test2.isSameKindAs(test4));
        assertEquals(false, test2.isSameKindAs(test5));
    }

}