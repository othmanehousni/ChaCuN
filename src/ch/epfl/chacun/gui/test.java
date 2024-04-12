package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMakerFr;

import java.util.*;
import java.util.function.BiFunction;

public final class test {

    public static void main(String[] args) {
        Map<PlayerColor, String> players = Map.of(
                PlayerColor.RED, "Alice",
                PlayerColor.BLUE, "Bob",
                PlayerColor.YELLOW, "Charlie",
                PlayerColor.GREEN, "David");

        BiFunction<Integer, Integer, Integer> blabla = Integer::compare;

        List<Integer> unmodif = new ArrayList<>();
        unmodif.add(4);
        Iterator<Integer> it = unmodif.listIterator();
        it.next();
        it.remove();

        Set<PlayerColor> set = players.keySet();

       //String liste = new TextMakerFr(players).sortedNames(players);
       // System.out.println(liste);

        String text = new TextMakerFr(players).playersScoredForest(set, 1, 1, 1);
        System.out.println(text);

        String text2 = new TextMakerFr(players).playersScoredForest(set, 1, 0, 1);
        System.out.println(text2);

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
            builder.append("]]").append("beda");
        }
        Map<String, Set<Integer>> set1 = new HashMap<>();
        set1.put("a", Set.of(-1, 2, 3));
        //System.out.println(set1.get());

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


    }
}