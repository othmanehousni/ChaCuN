package ch.epfl.chacun;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TextMakerFr implements TextMaker {

    public final Map<PlayerColor, String> playerNames;

    private final Map<Animal.Kind, String> frenchAnimalNames = Map.of(
            Animal.Kind.AUROCHS, "auroch",
            Animal.Kind.DEER, "cerf",
            Animal.Kind.MAMMOTH, "mammouth",
            Animal.Kind.TIGER, "smilodon"
    );

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = Map.copyOf(playerNames);
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerNames.get(playerColor) == null ? null : playerNames.get(playerColor);
    }

    @Override
    public String points(int points) {
        return STR."\{points} point\{pluralOrNot(points)}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerNames.get(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String text = STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())} d'une forêt composée de \{tileCount} tuile\{pluralOrNot(tileCount)}.";

        if (mushroomGroupCount > 0) {
            text = text.replace(".", " ");
            text += STR."et de \{mushroomGroupCount} groupe \{pluralOrNot(mushroomGroupCount)}de champignons.";
        }
        return text;
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String text = STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())} d'une rivière composée de \{tileCount} tuile\{pluralOrNot(tileCount)}.";
        if (fishCount > 0) {
            text = text.replace(".", " ");
            text += STR."et contenant \{fishCount} poisson\{pluralOrNot(fishCount)}.";
        }
        return text;
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerNames.get(scorer)} a remporté \{points(points)} en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{animalsMap(animals)}.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerNames.get(scorer)} a remporté \{points(points)} en plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lac\{pluralOrNot(lakeCount)}.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} " +
                STR."en tant qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())} "
                + STR."d'un pré contenant \{animalsMap(animals)}.";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant" +
                STR."qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())}" +
                STR."d'un réseau hydrographique contenant \{fishCount} poisson\{pluralOrNot(fishCount)}.";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())} d'un pré contenant la grande fosse à pieux entourée de \{animalsMap(animals)}.";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} majoritaire\{pluralOrNot(scorers.size())} d'un réseau hydrographique contenant le radeau et \{lakeCount} lac\{pluralOrNot(lakeCount)}.";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{playerPluralCalculator(winners)} \{verbConjugaison(winners)} remporté la partie avec \{points(points)} !";
    }

    @Override
    public String clickToOccupy() {
        return STR."Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    @Override
    public String clickToUnoccupy() {
        return STR."Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    private String animalsMap(Map<Animal.Kind, Integer> animal) {
        return joiner(animal.entrySet().stream().filter(map -> map.getKey() != Animal.Kind.TIGER && map.getValue() > 0)
                .sorted(Map.Entry.comparingByKey()).map(Map.Entry::getKey)
                .map(kind -> kindCounter(animal.get(kind), frenchAnimalNames.get(kind))));
    }

    private String kindCounter(int count, String kind) {
        return STR."\{count} \{kind}\{count > 1 ? "s" : ""}";
    }

    private String joiner(Stream<String> stringStream) {
        List<String> stringList = stringStream.toList();
        List<String> subbedStringList = stringList.subList(0, stringList.size() - 1);
        String last = stringList.getLast();
        if (stringList.size() > 1) return STR."\{String.join(", ", subbedStringList)} et \{last}";
        return last;
    }

    private String playerPluralCalculator(Set<PlayerColor> scorers) {
        List<PlayerColor> playerList = scorers.stream().sorted((Comparator.comparing(Enum::ordinal))).toList();
        if (playerList.size() == 1) {
            return playerNames.get(playerList.getFirst());
        } else {
            String last = playerList.stream().map(playerNames::get).toList().getLast();
            playerList = playerList.subList(0, playerList.size() - 1);
            return STR."\{playerList.stream().map(playerNames::get).collect(Collectors.joining(", "))} et \{last}";
        }
    }

    private String pluralOrNot(int thing) {
        return thing > 1 ? "s" : "";
    }

    private String verbConjugaison(Set<PlayerColor> scorers) {
        return scorerSize(scorers) > 1 ? "ont" : "a";
    }

    private int scorerSize(Set<PlayerColor> scorers) {
        return scorers.size();
    }

    private String occupantDotOrNot(int occupantCount) {
        return occupantCount > 1 ? "·s" : "";
    }

}
