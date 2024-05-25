package ch.epfl.chacun.gui;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMakerFr;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;
import java.util.function.BiFunction;

import static javafx.collections.FXCollections.observableArrayList;

public final class test {

        public static String posIntToStr(int i ){
        if (i < 0) {
            throw new IllegalArgumentException();
        } else {
            StringBuilder newBuilder = new StringBuilder();
            while (i > 0) {
                newBuilder.append((char)('0' + (i % 10)));
                i = i / 10;
            }

            return newBuilder.reverse().toString();
        }
    }

    String aa = "aa" + "bb";

    public static String intToStr(int i) {
        return i < 0 ? "-" + posIntToStr(-i) : posIntToStr(i);
    }

    public static int strToPosInt(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            result = (int) (Math.pow (10, s.length() - i ) *( s.charAt(i) - '0'));
        }
        return result;
    }




    public static void main(String[] args) {
        Map<PlayerColor, String> players = Map.of(
                PlayerColor.RED, "Dalia",
                PlayerColor.BLUE, "Claude",
                PlayerColor.YELLOW, "Alice",
                PlayerColor.GREEN, "Bachir");

        Map<PlayerColor, String> playerUn = Map.of(PlayerColor.PURPLE, "Dalia");

        Set<PlayerColor> setFull = players.keySet();


        Map<Animal.Kind, Integer> animalsTres = Map.of(Animal.Kind.MAMMOTH, 1, Animal.Kind.AUROCHS, 2, Animal.Kind.DEER, 3, Animal.Kind.TIGER, 2);
        Map<Animal.Kind, Integer> animalsSolo = Map.of(Animal.Kind.MAMMOTH, 1, Animal.Kind.AUROCHS, 2, Animal.Kind.DEER, 3);



        //TODO : tests

        String textMenhir = new TextMakerFr(players).playerClosedForestWithMenhir(PlayerColor.RED);
        System.out.println(textMenhir);

        String textSingulierForest = new TextMakerFr(playerUn).playersScoredForest(playerUn.keySet(), 6, 0, 3);
        System.out.println(textSingulierForest);

        String textPlurielForest = new TextMakerFr(players).playersScoredForest(setFull, 9, 1, 3);
        System.out.println(textPlurielForest);

        String textRiver = new TextMakerFr(playerUn).playersScoredRiver(playerUn.keySet(), 8, 5, 3);
        System.out.println(textRiver);

        String text3 = new TextMakerFr(players).playerScoredHuntingTrap(PlayerColor.RED, 10, animalsTres);
        System.out.println(text3);

        List<List<Integer>> aa = new ArrayList<>();


        BiFunction<Integer, Integer, Integer> blabla = Integer::compare;

        List<Integer> unmodif = new ArrayList<>();
        unmodif.add(4);
        Iterator<Integer> it = unmodif.listIterator();
        it.next();
        it.remove();



        Map<String, Set<Integer>> pageRefs = Map.of("ensemble", Set.of(1, 2, 3), "table associative", Set.of(4, 5, 6), "liste", Set.of(7, 8, 9));
        Map<String, String> crossRefs = Map.of("table dynamique", "liste");


        System.out.println();

        StringBuilder builder = new StringBuilder();
        List<String> elements = List.of("a", "b", "c", "d", "e", "f");
        builder.append("[");
        for (int y = 0; y < 2; y++) {
            builder.append("[");
            for (int x = 0; x < 3; x++) {
                builder.append(elements.get(y * 2 + x));
                if (x < 2) {
                    builder.append(", ");
                }
            }
            builder.append("]]").append(",");
        }
        Map<String, Set<Integer>> set1 = new HashMap<>();
        set1.put("a", Set.of(-1, 2, 3));
        set1.put("b", Set.of(4, 5, 6));
        boolean a = set1.entrySet().stream().anyMatch(e -> e.getValue().contains(2));

        for (Set<Integer> inter : set1.values()) {
            for (Integer i : inter) {
                System.out.println(i);
            }
        }

        System.out.println(builder.toString());

        List<String> liste12 = new ArrayList<>();
        liste12.add("baba");

        StringBuilder builder1 = new StringBuilder();
        builder1.append(liste12.get(0));
        builder1.replace(0, 4, liste12.get(0).toUpperCase());
        System.out.println(builder1.toString());

        TreeSet<Integer> set2 = new TreeSet<>();
        set2.add(1);
        set2.add(4);
        set2.add(2);
        set2.add(5);
        set2.add(6);
        set2.add(7);
        set2.add(3);
        set2.add(8);
        set2.add(9);
        set2.add(13);
        set2.add(11);
        System.out.println(set2);

        HashSet<Integer> set3 = new HashSet<>();
        set3.add(1);
        set3.add(4);
        set3.add(2);
        set3.add(5);
        set3.add(6);
        set3.add(7);
        set3.add(3);
        set3.add(8);
        set3.add(9);
        set3.add(13);

        System.out.println(set3);

        StringBuilder builder12 = new StringBuilder().append(2018);
        String date = "2018";
        int dateInt = 2018;
        //System.out.println(posIntToStr(dateInt));
        //System.out.println(intToStr(-2147483648));


        System.out.println(strToPosInt("2018"));
        System.out.println((date.charAt(0) - '0') * 1000 + (date.charAt(1) - '0') * 100 + (date.charAt(2) - '0') * 10 + (date.charAt(3) - '0'));

        System.out.println(String.valueOf((char) 1));

        Map<String,String> set = new HashMap<>();
        System.out.println(Integer.parseInt("1"));
        System.out.println(String.format("x= %s", 10));
        List<String> list = new ArrayList<>();
        list.add("a");
        ObservableList<String> list2 = observableArrayList();
        list2.add("a");
        ObservableMap<String, String> map = new SimpleMapProperty<>();
        System.out.println(map.get(Bindings.valueAt(list2,0).get()));
     }
    }
