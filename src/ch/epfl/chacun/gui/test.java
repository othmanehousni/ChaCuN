package ch.epfl.chacun.gui;

import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMakerFr;

import java.util.*;
import java.util.function.BiFunction;

public class test {


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

        String liste = new TextMakerFr(players).sortedNames(players);
        System.out.println(liste);

        String text = new TextMakerFr(players).playersScoredForest(set, 1, 1, 1);
        System.out.println(text);

        String text2 = new TextMakerFr(players).playersScoredForest(set, 1, 0, 1);
        System.out.println(text2);


        List<Integer> numbers = Arrays.asList(3, -1, 4, 1, 5);
        numbers.parallelStream().forEachOrdered(System.out::println);
        numbers.parallelStream().forEach(System.out::println);
    }
}
