package ch.epfl.chacun;

import java.util.*;

/**
 *
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */

public record MessageBoard (TextMaker textMaker, List<Message> messages) {

    /**
     * Represents a message displayed on the message board.
     *
     * @param text The text of the message.
     * @param points The points associated with the message, which can be 0.
     * @param scorers The set of players who scored the points, which can be empty.
     * @param tileIds The identifiers of the tiles related to the message, or an empty set.
     */

    public record Message (String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        /**
         * Validates the message properties, ensuring points are non-negative and text is not null.
         * Copies the scorers and tileIds sets to ensure immutability.
         */

        public Message {
            Preconditions.checkArgument(points >= 0);
            Objects.requireNonNull(text);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }

    /*
     * Represents the content of a message board in the game. It is immutable and public.
     * The message board is a collection of messages displayed in chronological order.
     *
     * @param textMaker The object responsible for generating the text of various messages.
     * @param messages The list of messages displayed on the board, from the oldest to the most recent.
     */

    /**
     * Compact constructor.
     * Ensures immutability by copying the list of messages and checks that textMaker is not null.
     */
    public MessageBoard {
        messages = List.copyOf(messages);
        }

    //
    private Map<Animal.Kind, Integer> getAnimalMap(Set<Animal> animals) {
        Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
        for (Animal animal : animals) {
            animalCounts.put(animal.kind(),animalCounts.containsKey(animal.kind()) ? animalCounts.get(animal.kind())+1 : 1);
        }
        return animalCounts;
    }

    private int getAnimalPoint(Map<Animal.Kind, Integer> animals) {
        return Points.forMeadow(animals.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animals.getOrDefault(Animal.Kind.AUROCHS, 0),
                animals.getOrDefault(Animal.Kind.DEER, 0));
    }


    /**
     * Calculates and returns a map of total points associated with each player based on the messages.
     *
     * @return A map associating each player (scorer) with their total points.
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> pointsMap = new HashMap<>();
        for (Message message : messages) {
            for (PlayerColor scorer : message.scorers()) {
                pointsMap.put(scorer, pointsMap.getOrDefault(scorer, 0) + message.points);

            }
        }
        return pointsMap;
    }


    /**
     * Adds a new message to the board related to scoring in a forest area.
     *
     * @param forest The forest area being scored.
     * @return A new {@code MessageBoard} instance including the new message, or the original instance if the forest isn't occupied.
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest){
        if(forest.majorityOccupants().isEmpty()) {
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredForest(
                    forest.majorityOccupants(),
                    Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest)),
                    Area.mushroomGroupCount(forest),
                    forest.tileIds().size());

            Message scoredMessage = new Message (scoredForestText,
                    Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest)),
                    forest.majorityOccupants(),
                    forest.tileIds());


            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard (textMaker, newMessages);
        }
    }

    /**
     * Adds a new message to the board for a player closing a forest with a menhir, granting them a second turn.
     *
     * @param player The player who closed the forest.
     * @param forest The forest that was closed.
     * @return A new {@code MessageBoard} instance including the new message.
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest){
            String scoredForestText = textMaker.playerClosedForestWithMenhir(player);
            Message scoredMessage = new Message (scoredForestText,
                    0,
                    Set.of(),
                    forest.tileIds());


        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(scoredMessage);


        return new MessageBoard (textMaker, newMessages);
    }

    /**
     * Adds a message to the board related to scoring in a river area.
     *
     * @param river The river area being scored.
     * @return A new {@code MessageBoard} instance including the new message, or the original instance if the river isn't occupied.
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river){
        if(river.majorityOccupants().isEmpty()) {
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredRiver(
                    river.majorityOccupants(),
                    Points.forClosedRiver(river.tileIds().size(), Area.riverFishCount(river)),
                    Area.riverFishCount(river),
                    river.tileIds().size());

            Message scoredMessage = new Message (
                    scoredForestText,
                    Points.forClosedRiver(river.tileIds().size(), Area.riverFishCount(river)),
                    river.majorityOccupants(),
                    river.tileIds());


            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard (textMaker, newMessages);
        }
    }

    /**
     * Adds a message to the board for scoring points from placing a hunting trap adjacent to a meadow.
     *
     * @param scorer The player who placed the hunting trap.
     * @param adjacentMeadow The meadow adjacent to the hunting trap.
     * @return A new {@code MessageBoard} instance including the new message if points were scored; otherwise, the original instance.
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Set<Animal> animalSet = Area.animals(adjacentMeadow, Set.of());
        Map<Animal.Kind, Integer> animalMap = getAnimalMap(animalSet);
        int points = getAnimalPoint(animalMap);
        if (points <= 0) {
            return this;
        } else {
            String scoredForestText = textMaker.playerScoredHuntingTrap(
                    scorer,
                    points,
                    animalMap);

            Message scoredMessage = new Message(
                    scoredForestText,
                    points,
                    Set.of(scorer),
                    adjacentMeadow.tileIds());


            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard (textMaker, newMessages);
        }
    }

    /**
     * Adds a message to the board for scoring points from placing a logboat in a river system.
     *
     * @param scorer The player who placed the logboat.
     * @param riverSystem The river system where the logboat was placed.
     * @return A new {@code MessageBoard} instance including the new message.
     */

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem){
        String scoredForestText = textMaker.playerScoredLogboat(
                scorer,
                Points.forLogboat(Area.lakeCount(riverSystem)),
                Area.lakeCount(riverSystem));

        Message scoredMessage = new Message (
                scoredForestText,
                Points.forLogboat(Area.lakeCount(riverSystem)),
                Set.of(scorer),
                riverSystem.tileIds());

        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(scoredMessage);


        return new MessageBoard (textMaker, newMessages);
    }

    /**
     * Adds a message to the board for scoring points in a meadow, considering certain animals as cancelled.
     *
     * @param meadow The meadow being scored.
     * @param cancelledAnimals The set of animals to ignore in scoring.
     * @return A new {@code MessageBoard} instance including the new message if points were scored; otherwise, the original instance.
     */

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        Set <Animal> animalSet = Area.animals(meadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalMap = getAnimalMap(animalSet);
        int points = getAnimalPoint(animalMap);
        if (meadow.majorityOccupants().isEmpty() || points <= 0) {
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredMeadow(
                    meadow.majorityOccupants(),
                    points,
                    animalMap);

            Message scoredMessage = new Message(
                    scoredForestText,
                    points,
                    meadow.majorityOccupants(),
                    meadow.tileIds());

            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard(textMaker, newMessages);
        }
    }

    /**
     * Adds a message to the board related to scoring in a river system.
     *
     * @param riverSystem The river system being scored.
     * @return A new {@code MessageBoard} instance including the new message, or the original instance if no points were scored.
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem){
        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));
        if(riverSystem.majorityOccupants().isEmpty() || points <= 0) {
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredRiverSystem(
                    riverSystem.majorityOccupants(),
                    points,
                    Area.riverSystemFishCount(riverSystem));

            Message scoredMessage = new Message(
                    scoredForestText,
                    points,
                    riverSystem.majorityOccupants(),
                    riverSystem.tileIds());


            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);

            return new MessageBoard(textMaker, newMessages);
        }
    }

    /**
     * Adds a message to the board for scoring points from a pit trap placed in a meadow, considering certain animals as cancelled.
     *
     * @param adjacentMeadow The meadow where the pit trap affects.
     * @param cancelledAnimals The set of animals to ignore in scoring.
     * @return A new {@code MessageBoard} instance including the new message if points were scored; otherwise, the original instance.
     */

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals){
        Set <Animal> animalSet = Area.animals(adjacentMeadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalMap = getAnimalMap(animalSet);
        int points = getAnimalPoint(animalMap);
        if(adjacentMeadow.majorityOccupants().isEmpty() || points <= 0) {
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredPitTrap(
                    adjacentMeadow.majorityOccupants(),
                    points,
                    animalMap);

            Message scoredMessage = new Message(
                    scoredForestText,
                    points,
                    adjacentMeadow.majorityOccupants(),
                    adjacentMeadow.tileIds());

            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard (textMaker, newMessages);
        }
    }

    /**
     * Adds a message to the board for scoring points from placing a raft in a river system.
     *
     * @param riverSystem The river system where the raft was placed.
     * @return A new {@code MessageBoard} instance including the new message, or the original instance if no points were scored.
     */

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem){
        if(riverSystem.majorityOccupants().isEmpty()){
            return this;
        } else {
            String scoredForestText = textMaker.playersScoredRaft(
                    riverSystem.majorityOccupants(),
                    Points.forRaft(Area.lakeCount(riverSystem)),
                    Area.lakeCount(riverSystem));

            Message scoredMessage = new Message(
                    scoredForestText,
                    Points.forRaft(Area.lakeCount(riverSystem)),
                    riverSystem.majorityOccupants(),
                    riverSystem.tileIds());

            List<Message> newMessages = new ArrayList<>(messages);
            newMessages.add(scoredMessage);


            return new MessageBoard (textMaker, newMessages);

        }
    }

    /**
     * Adds a message to the board announcing the winners of the game and their scores.
     *
     * @param winners The set of players who have won the game.
     * @param points The number of points with which the game was won.
     * @return A new {@code MessageBoard} instance including the winners' announcement message.
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points){
        //Preconditions.checkArgument(!winners.isEmpty());
        String scoredText = textMaker.playersWon(
                winners,
                points);

        Message scoredMessage = new Message (
                scoredText,
                0,
                Set.of(),
                Set.of());


        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(scoredMessage);


        return new MessageBoard (textMaker, newMessages);


    }


}
