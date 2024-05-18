package ch.epfl.chacun;

import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class TextMakerFr implements TextMaker {

    public final Map<PlayerColor, String> playerNames;

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
        String text = STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} point en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'une forêt composée de \{tileCount} tuile\{pluralOrNot(tileCount)}.";

        if(mushroomGroupCount > 0) {
            text = text.replace(".", " ");
            text += STR."et de \{mushroomGroupCount} groupe \{pluralOrNot(mushroomGroupCount)} de champignons.";
        }
        return text;
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String text = STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'une rivière composée de \{tileCount} tuile\{pluralOrNot(tileCount)}.";
        if(fishCount > 0) {
            text = text.replace(".", " ");
            text += STR."et contenant \{fishCount} poisson\{pluralOrNot(fishCount)}.";
        }

        return text;
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerNames.get(scorer)} a remporté \{points(points)} point en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{pluralMultipleAnimals(animals)}.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerNames.get(scorer)} a remporté \{points(points)} point en plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lacs.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
       return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points} point \{pluralOrNot(points)} en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'un pré contenant \{pluralMultipleAnimals(animals)}.";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'une rivière composée de \{fishCount} tuile\{pluralOrNot(fishCount)}.";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'un pré contenant la grande fosse à pieux entourée de \{pluralMultipleAnimals(animals)}.";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{playerPluralCalculator(scorers)} \{verbConjugaison(scorers)} remporté \{points(points)} en tant qu'occupant·e\{pluralOrNot(scorers.size())} majoritaire d'un réseau hydrographique contenant le radeau et \{lakeCount} lac\{pluralOrNot(lakeCount)}.";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{playerPluralCalculator(winners)} \{verbConjugaison(winners)} remporté la partie avec \{points(points)} point\{pluralOrNot(points)}!";
    }

    @Override
    public String clickToOccupy() {
        return STR."Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    @Override
    public String clickToUnoccupy() {
        return STR."Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }


    private String pluralMultipleAnimals(Map<Animal.Kind, Integer> animals) {
        StringBuilder sb = new StringBuilder();
        List<Animal.Kind> animalList = animals.keySet().stream().toList();
        Integer lastNumber = animals.values().stream().toList().getLast();
        String last = animalList.getLast().toString();

        //todo annuler tigers??

        if (animals.size() == 1) {
            return animals.keySet().iterator().next().toString();
        } else {
            animals.forEach((kind, count) -> {
                if(kind != animalList.getLast() && count > 0) {
                    sb.append(count).append(" ").append(kind.toString()).append(count > 1 ? "s" : "").append(", ");
                }
                });
        }

        //todo creer map <kind, string> pour en francais

        sb.append("et ").append(lastNumber).append(" ").append(last).append(lastNumber > 1 ? "s" : "");
        return sb.toString();
    }

    private String playerPluralCalculator(Set<PlayerColor> scorers) {
        List<PlayerColor> playerList = scorers.stream().sorted((Comparator.comparing(Enum::ordinal))).toList();
        if(playerList.size() == 1) {
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
        return scorers.size() > 1 ? "ont" : "a";
    }


}
