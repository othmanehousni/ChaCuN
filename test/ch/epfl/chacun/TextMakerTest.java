package ch.epfl.chacun;

import ch.epfl.chacun.*;

import java.util.*;
import java.util.function.Function;


/**
 * Simple test class that implements TextMaker useful when testing {@link MessageBoard}
 */
public class TextMakerTest implements TextMaker {

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerColor.toString();
    }

    @Override
    public String points(int points) {
        return String.valueOf(points);
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return player.toString();
    }

    // we need these sorting functions because the messages strings are compared, and it's not
    // guaranteed for sets and maps to have the same order every time.
    private <T, K extends Comparable<K>> List<T> sortBy(Set<T> set, Function<T, K> key) {
        var arrayList = new ArrayList<>(set);
        Comparator<K> comparator = Comparator.naturalOrder();

        arrayList.sort((a, b) -> {
            var keyA = key.apply(a);
            var keyB = key.apply(b);

            return comparator.compare(keyA, keyB);
        });

        return List.copyOf(arrayList);
    }

    private <T extends Comparable<T>> List<T> sort(Set<T> set) {
        return sortBy(set, t -> t);
    }

    private <K, V, C extends Comparable<C>> List<Map.Entry<K, V>> sortBy(Map<K, V> set, Function<Map.Entry<K, V>, C> key) {
        var arrayList = new ArrayList<>(set.entrySet());
        Comparator<C> comparator = Comparator.naturalOrder();

        arrayList.sort((a, b) -> {
            var keyA = key.apply(a);
            var keyB = key.apply(b);

            return comparator.compare(keyA, keyB);
        });

        return List.copyOf(arrayList);
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        var sortedScorers = sort(scorers);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(mushroomGroupCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        var sortedScorers = sort(scorers);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        var sortedAnimal = sortBy(animals, Map.Entry::getKey);

        return new StringJoiner(" ")
                .add(scorer.toString())
                .add(String.valueOf(points))
                .add(sortedAnimal.toString())
                .toString();
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add(scorer.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        var sortedAnimal = sortBy(animals, Map.Entry::getKey);
        var sortedScorers = sort(scorers);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(sortedAnimal.toString())
                .toString();
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        var sortedScorers = sort(scorers);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .toString();
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        var sortedScorers = sort(scorers);
        var sortedAnimal = sortBy(animals, Map.Entry::getKey);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(sortedAnimal.toString())
                .toString();
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        var sortedScorers = sort(scorers);

        return new StringJoiner(" ")
                .add(sortedScorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        var sortedWinner = sort(winners);

        return new StringJoiner(" ")
                .add(sortedWinner.toString())
                .add(String.valueOf(points))
                .toString();
    }

    @Override
    public String clickToOccupy() {
        return null;
    }

    @Override
    public String clickToUnoccupy() {
        return null;
    }
}
