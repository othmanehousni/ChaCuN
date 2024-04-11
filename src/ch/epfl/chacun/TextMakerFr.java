package ch.epfl.chacun;

import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class TextMakerFr implements TextMaker {

    public final Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = playerNames;
    }

    public BiFunction<Integer, Integer, Integer> blabla = Integer::compare;



    public double dada = 5d;
    public String sortedNames(Map<PlayerColor, String> playerNames) { //TODO : remettre private
        List<Map.Entry<PlayerColor,String >> sortedPlayers = playerNames.entrySet().stream().sorted((Comparator.comparing(player -> player.getKey().ordinal()))).toList();
        String last = sortedPlayers.getLast().getValue();
        sortedPlayers = sortedPlayers.subList(0, sortedPlayers.size()-1);
        return STR."\{sortedPlayers.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(", "))} et \{last}";


    }

    private int countAnimalsByKind(Map<Animal.Kind, Integer> animals, Animal.Kind kind) {
        return (int) animals.entrySet().stream().filter(animal -> animal.getKey().equals(kind)).count();
    }


    @Override
    public String playerName(PlayerColor playerColor) {
        return playerNames.get(playerColor);
    }

        @Override
    public String points(int points) {
        return "";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerNames.get(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {

        List<PlayerColor> playerList = scorers.stream().toList();
        String last = playerList.stream().map(playerNames::get).toList().getLast();
        playerList = playerList.subList(0, playerList.size() - 1);
        String text;
        if (playerList.size() == 1) {
            text = STR."\{playerList.stream().map(playerNames::get).collect(Collectors.joining(", "))} a remporté \{points} points en tant qu'occupant·e majoritaire d'une forêt composée de \{tileCount} tuiles.";
        } else {
            text = STR."\{playerList.stream().map(playerNames::get).collect(Collectors.joining(", "))} et \{last} ont remporté \{points} points en tant qu'occupant·es majoritaire d'une forêt composée de \{tileCount} tuiles.";
        }

        if(mushroomGroupCount > 0) {
            text = text.replace(".", " ");
            text += STR."et de \{mushroomGroupCount} groupes de champignons.";
        }

        return text;
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        List<PlayerColor> playerList = scorers.stream().toList();
        String last = playerList.stream().map(playerNames::get).toList().getLast();
        playerList = playerList.subList(0, playerList.size() - 1);
        String text;
        if (playerList.size() == 1) {
            text = STR."\{playerList
                    .stream()
                    .map(playerNames::get)
                    .collect(Collectors.joining(", "))} a remporté \{points} points en tant qu'occupant·e majoritaire d'une rivière composée de \{tileCount} tuiles.";
        } else {
            text = STR."\{playerList
                    .stream()
                    .map(playerNames::get)
                    .collect(Collectors.joining(", "))} et \{last} ont remporté \{points} points en tant qu'occupant·es majoritaire d'une riviëre composée de \{tileCount} tuiles.";
        }

        if(fishCount > 0) {
            text = text.replace(".", " ");
            text += STR."et contenant \{fishCount} poissons.";
        }

        return text;
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerNames.get(scorer)} a remporté \{points} points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de  \{countAnimalsByKind(animals, Animal.Kind.MAMMOTH) } mammouths, \{countAnimalsByKind(animals, Animal.Kind.AUROCHS)} aurochs, \{countAnimalsByKind(animals, Animal.Kind.DEER)} cerfs.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerNames.get(scorer)} a remporté \{points} points en plaçant la pirogue dans un réseau hydrographique contenant \{lakeCount} lacs.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        List<PlayerColor> playerList = scorers.stream().toList();
        String last = playerList.stream().map(playerNames::get).toList().getLast();
        playerList = playerList.subList(0, playerList.size() - 1);
        String text;
        if (playerList.size() == 1) {
            text = STR."\{playerList
                    .stream()
                    .map(playerNames::get)
                    .collect(Collectors.joining(", "))} a remporté \{points} points en tant qu'occupant·e majoritaire d'une pré contenant \{countAnimalsByKind(animals, Animal.Kind.DEER)} cerfs.";
        } else {
            text = STR."\{playerList
                    .stream()
                    .map(playerNames::get)
                    .collect(Collectors.joining(", "))} et \{last} ont remporté \{points} points en tant qu'occupant·es majoritaire d'une riviëre composée de \{} tuiles.";
        }

//        if(fishCount > 0) {
//            text = text.replace(".", " ");
//            text += STR."et contenant \{fishCount} poissons.";
//        }
//
      return text;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return "";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return "";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return "";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return "";
    }

    @Override
    public String clickToOccupy() {
        return "";
    }

    @Override
    public String clickToUnoccupy() {
        return "";
    }
}
