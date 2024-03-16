package ch.epfl.chacun;

import java.util.Map;
import java.util.Set;

public class TextMakerImpl implements TextMaker {

    @Override
    public String playerName(PlayerColor playerColor) {
        return playerColor.toString();
    }

    @Override
    public String points(int points) {
        return points + " points";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return playerName(player) + " closed a forest with a menhir and earns the right to take a second turn.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return "Players " + scorers + " scored " + points + " points for closing a forest with " +
                mushroomGroupCount + " mushroom groups and " + tileCount + " tiles.";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return playerName(scorer) + " scored " + points + " points for placing a hunting trap in a meadow with animals: " + animals;
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        return "Players " + scorers + " scored " + points + " points for closing a river with " +
                fishCount + " fish and " + tileCount + " tiles.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return playerName(scorer) + " scored " + points + " points for placing a logboat in a river system with " +
                lakeCount + " lakes.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return "Players " + scorers + " scored " + points + " points for closing a meadow with animals: " + animals;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return "Players " + scorers + " scored " + points + " points for closing a river system with " +
                fishCount + " fish.";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return "Players " + scorers + " scored " + points + " points for closing a meadow with a pit trap and animals: " + animals;
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return "Players " + scorers + " scored " + points + " points for closing a river system with a raft and " +
                lakeCount + " lakes.";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return "Players " + winners + " won the game with " + points + " points!";
    }

    @Override
    public String clickToOccupy() {
        return "Click to occupy";
    }

    @Override
    public String clickToUnoccupy() {
        return "Click to unoccupy";
    }
}