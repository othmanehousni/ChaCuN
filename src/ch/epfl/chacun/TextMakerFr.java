package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TextMakerFr implements TextMaker {

    /** Map associating player names with their colors */
    public final Map<PlayerColor, String> playerNames;

    /** Map of animal kinds to their French names */
    private final Map<Animal.Kind, String> frenchAnimalNames = Map.of(
            Animal.Kind.AUROCHS, "auroch",
            Animal.Kind.DEER, "cerf",
            Animal.Kind.MAMMOTH, "mammouth",
            Animal.Kind.TIGER, "smilodon"
    );

    /**
     * Constructor for TextMakerFr.
     *
     * @param playerNames A map associating player colors with their names.
     */
    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = Map.copyOf(playerNames);
    }

    /**
     * Returns the name of the player associated with the specified color.
     *
     * @param playerColor The color of the player whose name is to be retrieved.
     * @return The name of the player associated with the specified color, or {@code null} if no name is associated with this color.
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        return playerNames.get(playerColor) == null ? null : playerNames.get(playerColor);
    }

    /**
     * Generates a string representing the number of points.
     *
     * @param points The number of points to represent.
     * @return A string representing the number of points.
     */
    @Override
    public String points(int points) {
        return STR."\{points} \{pluralOrNot("point", points)}";
    }

    /**
     * Generates a message indicating that a player has closed a forest containing a menhir.
     *
     * @param player The color of the player who closed the forest.
     * @return The message indicating the forest closure.
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerNames.get(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    /**
     * Generates a message indicating that players scored points for a forest.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param mushroomGroupCount The number of mushroom groups in the forest.
     * @param tileCount The number of tiles in the forest.
     * @return The message indicating the points scored for the forest.
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String text = STR."\{MajorityOccupantsTemplate(scorers, points)} d'une forêt composée de \{tileCount} "
                + STR."\{pluralOrNot("tuile", tileCount)}.";
        text = addPropsCountOrNot(text, mushroomGroupCount, "groupe");
        return text;
    }

    /**
     * Generates a message indicating that players scored points for a river.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param fishCount The number of fish in the river.
     * @param tileCount The number of tiles in the river.
     * @return The message indicating the points scored for the river.
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String text = STR."\{MajorityOccupantsTemplate(scorers, points)} d'une rivière composée de " +
                STR."\{tileCount} \{pluralOrNot("tuile", tileCount)}.";
        text = addPropsCountOrNot(text, fishCount, "poisson");
        return text;
    }

    /**
     * Generates a message indicating that a player scored points for placing a hunting trap.
     *
     * @param scorer The color of the player who scored points.
     * @param points The number of points scored.
     * @param animals The kinds and counts of animals in the surrounding meadow.
     * @return The message indicating the points scored for placing the hunting trap.
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerOrPlayersScoredTemplate(null, points, scorer)} en plaçant "
                + STR."la fosse à pieux dans un pré dans lequel elle est entourée de \{animalsMap(animals)}.";
    }

    /**
     * Generates a message indicating that a player scored points for placing a logboat.
     *
     * @param scorer The color of the player who scored points.
     * @param points The number of points scored.
     * @param lakeCount The number of lakes in the river network.
     * @return The message indicating the points scored for placing the logboat.
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerOrPlayersScoredTemplate(null, points, scorer)} en plaçant"
                + STR." la pirogue dans un réseau hydrographique contenant "
                + STR."\{lakeCount} \{pluralOrNot("lac", lakeCount)}.";
    }

    /**
     * Generates a message indicating that players scored points for a meadow.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param animals The kinds and counts of animals in the meadow.
     * @return The message indicating the points scored for the meadow.
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{MajorityOccupantsTemplate(scorers, points)} d'un pré contenant \{animalsMap(animals)}.";
    }

    /**
     * Generates a message indicating that players scored points for a river system.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param fishCount The total number of fish in the river system.
     * @return The message indicating the points scored for the river system.
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{MajorityOccupantsTemplate(scorers, points)} d'un réseau hydrographique contenant "
                + STR."\{fishCount} \{pluralOrNot("poisson", fishCount)}.";
    }

    /**
     * Generates a message indicating that players scored points for scoring a pit trap.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param animals The map containing the count of each animal type surrounding the pit trap.
     * @return The message indicating the points scored for controlling the pit trap.
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{MajorityOccupantsTemplate(scorers, points)} d'un pré contenant la grande fosse à pieux"
                + STR." entourée de \{animalsMap(animals)}.";
    }

    /**
     * Generates a message indicating that players scored points for scoring a raft.
     *
     * @param scorers The colors of the players who scored points.
     * @param points The number of points scored.
     * @param lakeCount The total number of lakes in the river system.
     * @return The message indicating the points scored for controlling the raft.
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{MajorityOccupantsTemplate(scorers, points)} d'un réseau hydrographique contenant"
                + STR." le radeau et \{lakeCount} \{pluralOrNot("lac", lakeCount)}.";
    }

    /**
     * Generates a message indicating which players won the game.
     *
     * @param winners The colors of the players who won the game.
     * @param points The total points scored by the winners.
     * @return The message indicating the winners of the game.
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{playerPluralCalculator(winners)} \{verbConjugaison(winners)} remporté la partie avec \{points(points)} !";
    }

    /**
     * Generates a message instructing the player to click to occupy a tile.
     *
     * @return The message instructing the player to click to occupy a tile.
     */
    @Override
    public String clickToOccupy() {
        return STR."Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    /**
     * Generates a message instructing the player to click to unoccupy a tile.
     *
     * @return The message instructing the player to click to unoccupy a tile.
     */
    @Override
    public String clickToUnoccupy() {
        return STR."Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    /**
     * Constructs a string representation of the animals that can be scored and sorts them by kind.
     * Thus, not showing the tigers.
     *
     * @param animal The map containing the count of each animal type.
     * @return The string representation of the animals that can be scored.
     */
    private String animalsMap(Map<Animal.Kind, Integer> animal) {
        return joiner(animal.entrySet().stream().filter(map -> map.getKey() != Animal.Kind.TIGER && map.getValue() > 0)
                .sorted(Map.Entry.comparingByKey()).map(Map.Entry::getKey)
                .map(kind -> kindCounter(animal.get(kind), frenchAnimalNames.get(kind))));
    }

    /**
     * Constructs a string representation of the count of a specific animal type.
     *
     * @param count The count of the animal type.
     * @param kind The name of the animal type.
     * @return The string representation of the count of the animal type.
     */
    private String kindCounter(int count, String kind) {
        return STR."\{count} \{kind}\{count > 1 ? "s" : ""}";
    }

    /**
     * Constructs a string by joining elements of a stream, separating them with commas and "and" for the last element.
     *
     * @param stringStream The stream of strings to be joined.
     * @return The joined string.
     */
    private String joiner(Stream<String> stringStream) {
        List<String> stringList = stringStream.toList();
        List<String> allButLast = stringList.subList(0, stringList.size() - 1);
        String last = stringList.getLast();
        if (stringList.size() > 1) return STR."\{String.join(", ", allButLast)} et \{last}";
        return last;
    }

    /**
     * Constructs a string representation of the players who scored, handling singular and plural cases.
     *
     * @param scorers The set of players who scored.
     * @return The string representation of the players who scored.
     */
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

    /**
     * Determines whether a given thing should be pluralized based on the count of that thing.
     *
     * @param writtenThing The thing to be potentially pluralized.
     * @param thing The count of the thing.
     * @return The pluralized form of the thing if the count is greater than 1, otherwise the original form.
     */
    private String pluralOrNot(String writtenThing, int thing) {
        return thing > 1 ? STR."\{writtenThing}s" : writtenThing;
    }

    /**
     * Calculates the correct conjugation for the verb "to win" based on the number of winners.
     *
     * @param scorers The colors of the players who won the game.
     * @return The conjugated verb "to win".
     */
    private String verbConjugaison(Set<PlayerColor> scorers) {
        return scorerSize(scorers) > 1 ? "ont" : "a";
    }

    /**
     * Calculates the number of winners.
     *
     * @param scorers The colors of the players who won the game.
     * @return The number of winners.
     */
    private int scorerSize(Set<PlayerColor> scorers) {
        return scorers.size();
    }

    /**
     * Determines whether a dot should be added to the word "occupant" based on the count of occupants.
     *
     * @param occupantCount The count of occupants.
     * @return A dot if the count is greater than 1, otherwise an empty string.
     */
    private String occupantDotOrNot(int occupantCount) {
        return occupantCount > 1 ? "·s" : "";
    }

    /**
     * Constructs a string template indicating the majority occupants of a specific location.
     *
     * @param scorers The colors of the players who control the location.
     * @param points The number of points scored.
     * @return The string indicating the majority occupants of the location.
     */
    private String MajorityOccupantsTemplate(Set<PlayerColor> scorers, int points) {
        return STR."\{playerOrPlayersScoredTemplate(scorers, points, null)} en tant " +
                STR."qu'occupant·e\{occupantDotOrNot(scorerSize(scorers))} \{pluralOrNot("majoritaire", scorers.size())}";
    }

    /**
     * Constructs a string template indicating which player or players scored points for scoring a specific location.
     *
     * @param scorers The colors of the players who control the location.
     * @param points The number of points scored.
     * @param oneScorer The color of the single player who scored points.
     * @return The string indicating which player or players scored points.
     */
    private String playerOrPlayersScoredTemplate(Set<PlayerColor> scorers, int points, PlayerColor oneScorer) {
        if (scorers == null) {
            return STR."\{playerNames.get(oneScorer)} a remporté \{points(points)}";
        }
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)}";
    }

    /**
     * Adds the count of a specific property to a string if the count is greater than zero.
     *
     * @param text The base string to which the count will be added.
     * @param propCount The count of the property.
     * @param str The name of the property.
     * @return The modified string with the property count added.
     */
    private String addPropsCountOrNot(String text, int propCount, String str) {
        if (propCount > 0) {
            text = text.replace(".", " ");
            if (Objects.equals(str, "groupe")) {
                text += STR."et de \{propCount} \{pluralOrNot(str, propCount)} de champignons.";
            } else {
                text += STR."et contenant \{propCount} \{pluralOrNot(str, propCount)}.";
            }
        }
        return text;
    }

}
