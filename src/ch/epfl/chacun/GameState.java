package ch.epfl.chacun;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.Comparator;
public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {

    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME;
    }

    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
        players = List.copyOf(players);
    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        if (nextAction == Action.OCCUPY_TILE
                && freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) == 0
                || freeOccupantsCount(currentPlayer(), Occupant.Kind.HUT) == 0
                || lastTilePotentialOccupants().isEmpty()) {
            return withTurnFinished();
        }
        return new GameState(players, tileDecks, tileDecks.topTile(Tile.Kind.NORMAL), board, nextAction, messageBoard);

    }

    private GameState withTurnFinished () {
        MessageBoard newMessageBoard= messageBoard;
        boolean shouldPlaySecondTurn = false;
        TileDecks newTileDecks = tileDecks;
        List<PlayerColor> playersList = players;
        Action newAction = Action.PLACE_TILE;
        Set<Area<Zone.Forest>> closedForestsByLastTile = board.forestsClosedByLastTile();
        Set<Area<Zone.River>> closedRiversByLastTile = board.riversClosedByLastTile();


        if (board.lastPlacedTile() != null && !closedForestsByLastTile.isEmpty()) {
            for (Area<Zone.Forest> forestArea : closedForestsByLastTile) {
                Tile.Kind lastPlacedTileKind = board.lastPlacedTile().tile().kind();
                newMessageBoard = messageBoard.withScoredForest(forestArea);
                for (Zone.Forest zone : forestArea.zones()) {
                    if (zone.kind().equals(Zone.Forest.Kind.WITH_MENHIR) && lastPlacedTileKind == Tile.Kind.NORMAL) {
                        newMessageBoard = messageBoard.withClosedForestWithMenhir(currentPlayer(), forestArea);
                        shouldPlaySecondTurn = true;
                    }
                }
                ;
            }
        }


        if (board.lastPlacedTile() != null && !closedRiversByLastTile.isEmpty()) {
            for(Area<Zone.River> riverArea : closedRiversByLastTile) {
                newMessageBoard = messageBoard.withScoredRiver(riverArea);
            }
        }

        board.withoutGatherersOrFishersIn(board.forestsClosedByLastTile(), board.riversClosedByLastTile());

        if (shouldPlaySecondTurn) {
            newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile);
        } else {
            newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);
            Collections.rotate(playersList, -1);
        }

        if (tileDecks.deckSize(Tile.Kind.NORMAL) == 0) {
            return withFinalPointsCounted();
        }

// todo : regler problemes tile to place

        return new GameState(playersList, newTileDecks, null, board, newAction, newMessageBoard);

    }



    private GameState withFinalPointsCounted() {
        Set<Animal> cancelledAnimals = new HashSet<>(board.cancelledAnimals());
        MessageBoard newMessageBoard = messageBoard;


        for (Area<Zone.Meadow> meadow : board.meadowAreas()) {
            Zone pitTrapZone = meadow.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
            Zone wildFireZone = meadow.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE);

            if (wildFireZone != null ) {
                cancelledAnimals.addAll(Area.animals(meadow, cancelledAnimals));

            } else if (pitTrapZone != null) { //TODO : trouver acces a la position de la fosse
                Set<Animal> adjacentAnimals = Area.animals(board.adjacentMeadow(board.tileWithId(pitTrapZone.tileId()).pos(),(Zone.Meadow) pitTrapZone), cancelledAnimals);
                Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
                Set<Animal> adjacentDeers = new HashSet<>();
                Set<Animal> priorityDeers = new HashSet<>();
                
                int tigerCount = (int) animals
                        .stream()
                        .filter(animal -> animal.kind() == Animal.Kind.TIGER)
                        .count();


                adjacentAnimals.forEach(animal -> {
                    if (animal.kind() == Animal.Kind.DEER) {
                        adjacentDeers.add(animal);
                    }
                });

                animals.forEach(animal -> {
                    if (animal.kind() == Animal.Kind.DEER && !adjacentDeers.contains(animal)) {
                        priorityDeers.add(animal);
                    }
                });


                Iterator<Animal> priorityIterator = priorityDeers.iterator();
                for (int i = 0; i <= tigerCount; i++) {
                    if (priorityIterator.hasNext()) {
                        cancelledAnimals.add(priorityIterator.next());
                        tigerCount--;
                    }
                }

                Iterator<Animal> iterator2 = adjacentAnimals.iterator();
                    for (int i = 0; i <= tigerCount; i++) {
                        if(tigerCount > 0) {
                            if (iterator2.hasNext() && !cancelledAnimals.contains(iterator2.next())) {
                                cancelledAnimals.add(iterator2.next());
                                tigerCount--;
                        }
                    }

                }

                //pour un pré normal chaque smilodon mange un cerf.

                //on annule en priorité les cerfs qui ne sont pas dans le pré adjacent,
                // pour maximiser les points rapportés par la grande fosse à pieux

                        // Pour exécuter cet algorithme, vous avez besoin de connaître la position de la fosse,
                // donc vous devez d'abord vérifier si elle est présente dans l'aire
                newMessageBoard = newMessageBoard.withScoredPitTrap(meadow, cancelledAnimals);
        }
            newMessageBoard = newMessageBoard.withScoredMeadow(meadow, cancelledAnimals);
            board.withMoreCancelledAnimals(cancelledAnimals);

        }

        for (Area<Zone.Water> riverSystem : board.riverSystemAreas()) {
            if(riverSystem.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null) {
                newMessageBoard = newMessageBoard.withScoredRaft(riverSystem);
            }
            newMessageBoard = newMessageBoard.withScoredRiverSystem(riverSystem);
        }

        Map<PlayerColor, Integer> points = newMessageBoard.points();
        int maxPoints = Collections.max(points.values());
        Set<PlayerColor> winners = points
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == maxPoints)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);

        return new GameState(players, tileDecks,null, board, Action.END_GAME, newMessageBoard);
    }






    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }

    public PlayerColor currentPlayer() {
        if (nextAction == Action.START_GAME || nextAction == Action.END_GAME) {
            return null;
        } else {
            return players.getFirst();
        }
    }

    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        int placedOccupants = board().occupantCount(player,kind);
        return Occupant.occupantsCount(kind)-placedOccupants;
    }



    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board != Board.EMPTY);
        Set<Occupant> potentialOccupants = board.lastPlacedTile().potentialOccupants();
        if (board.lastPlacedTile() != null && board.lastPlacedTile().potentialOccupants() != null) {
            potentialOccupants.removeIf(potentialOccupant ->
                    freeOccupantsCount(currentPlayer(), potentialOccupant.kind()) == 0
                            ||
                    switch (board.lastPlacedTile().zoneWithId(potentialOccupant.zoneId())) {
                        case Zone.Forest forest -> board.forestArea(forest).isOccupied();
                        case Zone.Meadow meadow -> board.meadowArea(meadow).isOccupied();
                        case Zone.River river -> potentialOccupant.kind() == Occupant.Kind.PAWN && board.riverArea(river).isOccupied();
                        case Zone.Water water -> board.riverSystemArea(water).isOccupied();
                    });
        }
        return potentialOccupants;
    }

    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE && tile.occupant() == null);
        Action newAction = Action.OCCUPY_TILE;
        Tile newTileToPlace = null;
        MessageBoard newMessageBoard = messageBoard;
        TileDecks topTileDrawn = tileDecks.withTopTileDrawn(Tile.Kind.MENHIR);
        int playerPawnCount = board.occupantCount(players.getFirst(), Occupant.Kind.PAWN);
        int playerHutCount = board.occupantCount(players.getFirst(), Occupant.Kind.HUT);


        Board newBoard = board.withNewTile(tile);
        switch (tile.specialPowerZone()) {

            case Zone.Meadow meadow1
                    when meadow1.specialPower() == Zone.SpecialPower.HUNTING_TRAP -> {
                if(playerPawnCount > 0 || playerHutCount > 0) {
                    Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow1);

                    Set<Animal> animals = Area.animals(adjacentMeadow, Set.of());
                    newBoard.withMoreCancelledAnimals(animals);
                    Set<Animal> deerSet = new HashSet<>();
                    Set<Animal> tigerSet = new HashSet<>();

                    animals.stream()
                            .filter(animal -> animal.kind() == Animal.Kind.DEER).forEach(deerSet::add);
                    animals.stream()
                            .filter(animal -> animal.kind() == Animal.Kind.TIGER).forEach(tigerSet::add);

                    newMessageBoard.withScoredHuntingTrap(players.getFirst(), adjacentMeadow);
                    newTileToPlace = tileDecks.topTile(Tile.Kind.NORMAL);


                }

            }
            case Zone specialZone
                    when specialZone.specialPower() == Zone.SpecialPower.SHAMAN -> {
                if(playerPawnCount > 0) {
                    return new GameState(players, topTileDrawn, null, newBoard, Action.RETAKE_PAWN, newMessageBoard);

                }


            }
            case Zone.Lake lake
                    when lake.specialPower() == Zone.SpecialPower.LOGBOAT -> {
                newMessageBoard.withScoredLogboat(players.getFirst(), board.riverSystemArea(lake));
                newTileToPlace = tileDecks.topTile(Tile.Kind.NORMAL);

            }

            case null, default -> withTurnFinishedIfOccupationImpossible();
        }

        return new GameState(players, tileDecks, newTileToPlace, newBoard, newAction, newMessageBoard);

    }



    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN || (occupant == null || occupant.kind() == Occupant.Kind.PAWN));
        if (occupant == null) {
            return withTurnFinishedIfOccupationImpossible();
        } else {
            return new GameState(players, tileDecks, tileToPlace, board.withoutOccupant(occupant), Action.OCCUPY_TILE, messageBoard);
        }


    }


                }