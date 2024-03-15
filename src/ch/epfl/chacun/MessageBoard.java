package ch.epfl.chacun;

import java.util.*;

public record MessageBoard (TextMaker textMaker, List<Message> messages) {

    public MessageBoard {
        Preconditions.checkArgument(textMaker != null);
        messages = List.copyOf(messages);
        }

    // methodes privees a expliquer
    private Map<Animal.Kind, Integer> getAnimalMap(Set<Animal> animals) {
        Map<Animal.Kind, Integer> animalCounts = new HashMap<>();
        for (Animal animal : animals) {
            animalCounts.put(animal.kind(),animalCounts.containsKey(animal.kind()) ? animalCounts.get(animal.kind())+1 : 1);
        }
        return animalCounts;
    }

    private int getAnimalPoint(Map<Animal.Kind, Integer> animals) {
        return Points.forMeadow(animals.getOrDefault(Animal.Kind.MAMMOTH, 0), animals.getOrDefault(Animal.Kind.AUROCHS, 0), animals.getOrDefault(Animal.Kind.DEER, 0));
    }


    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> pointsMap = new HashMap<>();
        for (Message message : messages) {
            for (PlayerColor scorer : message.scorers()) {
                pointsMap.put(scorer, pointsMap.getOrDefault(scorer, 0) + message.points);

            }
        }
        return pointsMap;
    }


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

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Set<Animal> animalSet = Area.animals(adjacentMeadow, Set.of()); //l'empty set a ete donne par un gars sur ed
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


    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals){
        Set <Animal> animalSet = Area.animals(adjacentMeadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalMap = getAnimalMap(animalSet);
        int points = getAnimalPoint(animalMap);
        if(adjacentMeadow.majorityOccupants().isEmpty() && points <= 0) {
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

    public MessageBoard withWinners(Set<PlayerColor> winners, int points){
        Preconditions.checkArgument(!winners.isEmpty());
        // TODO : CHECK SI LES WINNER C IMPORTANT
        String scoredForestText = textMaker.playersWon(
                winners,
                points);

        Message scoredMessage = new Message (
                scoredForestText,
                points,
                winners,
                Set.of());


        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(scoredMessage);


        return new MessageBoard (textMaker, newMessages);


    }


    public record Message (String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        public Message {
            Preconditions.checkArgument(points >= 0);
            Preconditions.checkArgument(text != null);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
