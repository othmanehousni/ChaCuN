package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.epfl.chacun.Zone.SpecialPower.*;

/**
 * Represents the complete state of a ChaCuN game at any given time. This record encapsulates all the
 * essential components and information necessary to represent and manage the game's state, including
 * player information, tile management, and the game board.

 * The GameState is immutable, ensuring that once a game state is created, it cannot be altered, which
 * helps in maintaining the consistency and integrity of the game's logic throughout its lifecycle.

 * This class also handles transitions between different game actions like starting the game, placing tiles,
 * retaking pawns, occupying tiles, and ending the game, based on the evolving state of the game.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 *
 * @param players The list of players in the game, in their playing order.
 * @param tileDecks The current state of the tile decks, including normal and special tiles.
 * @param tileToPlace The next tile to be placed on the board, may be null if not applicable.
 * @param board The current state of the game board.
 * @param nextAction The next action to be performed in the game flow.
 * @param messageBoard The board displaying messages and scores throughout the game.
 */

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {

    /**
     * Represents the possible actions in a game of ChaCuN.
     * START_GAME: Indicates the game is starting, and the initial setup is being performed.
     * PLACE_TILE: A player is required to place a tile on the board.
     * RETAKE_PAWN: A player has the option to retake one of their pawns from the board (triggered by placing a Shaman tile).
     * OCCUPY_TILE: A player may place an occupant (pawn or hut) on the newly placed tile.
     * END_GAME: The game has ended, and final scoring is performed.
     */

    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    /**
     * Constructs a new GameState with specified players, tile decks, tile to place, game board, next action, and message board.
     * Validates the input to ensure a valid game state.
     * @param players The list of players in the game, in their playing order.
     * @param tileDecks The current state of the tile decks, including normal and special tiles.
     * @param tileToPlace The next tile to be placed on the board, may be null if not applicable.
     * @param board The current state of the game board.
     * @param nextAction The next action to be performed in the game flow.
     * @param messageBoard The board displaying messages and scores throughout the game.
     */


    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction.equals(Action.PLACE_TILE));
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
        players = List.copyOf(players);

    }

    /**
     * Calculates and returns the number of tigers in a given set of animals.
     * This method filters through the provided set of animals and counts how many
     * of them are of the type TIGER.
     *
     * @param allAnimals A {@link Set} of {@link Animal} from which to count the tigers.
     * @return The count of tigers in the provided set of animals.
     */

    private int tigerCount(Set<Animal> allAnimals) {
        return (int) allAnimals.stream().filter(animal -> animal.kind() == Animal.Kind.TIGER).count();
    }

    /**
     * Initializes a new game state for the start of a game, with the given players, tile decks, and a text maker for messages.
     * The game begins with no tile to place, an empty board, START_GAME action, and an empty message board.
     * @param players The list of players participating in the game.
     * @param tileDecks The initial state of the tile decks.
     * @param textMaker The tool to generate text for messages.
     * @return The initial game state ready for the start of the game.
     */


    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }

    /**
     * Handles the transition from starting the game to placing the first tile.
     * Places the starting tile on the board, draws the first tile for placement, and updates the game state to PLACE_TILE action.
     * @return A new GameState reflecting the placement of the starting tile and preparation for the first player's turn.
     * @throws IllegalArgumentException if the next action is not START_GAME.
     */

    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);

        PlacedTile startingTile = new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, Pos.ORIGIN);
        Board newBoard = board.withNewTile(startingTile);

        TileDecks newTileDeck = tileDecks.withTopTileDrawn(Tile.Kind.START);
        Tile newTileToPlace = newTileDeck.topTile(Tile.Kind.NORMAL);
        newTileDeck = newTileDeck.withTopTileDrawn(Tile.Kind.NORMAL);

        return new GameState(players, newTileDeck, newTileToPlace, newBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Retrieves the current player based on the next action. If the game is at the start or end, there is no current player.
     * @return The current player if applicable, otherwise null.
     */


    public PlayerColor currentPlayer() {
        return nextAction == Action.START_GAME || nextAction == Action.END_GAME ? null : players.getFirst();
    }

    /**
     * Calculates the number of free occupants of a specified kind for a given player.
     * Free occupants are those not currently placed on the game board.
     * @param player The player whose free occupants are counted.
     * @param kind The kind of occupant (PAWN or HUT).
     * @return The count of free occupants for the player.
     */


    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        int placedOccupants = board.occupantCount(player, kind);
        return Occupant.occupantsCount(kind) - placedOccupants;
    }

    /**
     * Determines the set of potential occupants that can be placed on the last placed tile by the current player.
     * Factors in the kinds of occupants the player has available and whether the tile's zones are already occupied.
     * @return A set of potential occupants for the last placed tile.
     * @throws IllegalArgumentException if there is no last placed tile.
     */


    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board() != Board.EMPTY);
        Set<Occupant> potentialOccupants = board.lastPlacedTile().potentialOccupants();

        potentialOccupants = potentialOccupants
                .stream()
                .filter(occupant -> freeOccupantsCount(currentPlayer(), occupant.kind()) > 0)
                .collect(Collectors.toSet());

        potentialOccupants.removeIf(potentialOccupant ->
                switch (board.lastPlacedTile().zoneWithId(potentialOccupant.zoneId())) {
                    case Zone.Forest forest -> board.forestArea(forest).isOccupied();
                    case Zone.Meadow meadow -> board.meadowArea(meadow).isOccupied();
                    case Zone.River river when (potentialOccupant.kind() == Occupant.Kind.PAWN) ->
                            board.riverArea(river).isOccupied();
                    case Zone.Water water -> board.riverSystemArea(water).isOccupied();
                });
        return potentialOccupants;
    }


    /**
     * Processes the placement of a tile by the current player.
     * Updates the board with the new tile, handles special tile effects, and transitions to the next appropriate action.
     * @param tile The tile placed by the current player.
     * @return A new GameState reflecting the game state after the tile placement.
     * @throws IllegalArgumentException if the action is not PLACE_TILE or if the tile is already occupied.
     */


    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE && tile.occupant() == null);
        MessageBoard newMessageBoard = messageBoard;
        int playerPawnCount = board.occupantCount(currentPlayer(), Occupant.Kind.PAWN);
        Board newBoard = board.withNewTile(tile);

        switch (tile.specialPowerZone()) {

            case Zone.Meadow meadow when meadow.specialPower() == HUNTING_TRAP -> {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);

                Set<Animal> cancelledDeer = eachTigerEatsOneDeer(adjacentMeadow, Set.of());

                newBoard = newBoard.withMoreCancelledAnimals(cancelledDeer);

                newMessageBoard = newMessageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);

            }
            case Zone specialZone when specialZone.specialPower() == SHAMAN -> {
                if (playerPawnCount > 0) {
                    return new GameState(players, tileDecks, null, newBoard, Action.RETAKE_PAWN, newMessageBoard);
                }
            }
            case Zone.Lake lake
                    when lake.specialPower() == LOGBOAT ->
                    newMessageBoard = newMessageBoard.withScoredLogboat(currentPlayer(), newBoard.riverSystemArea(lake));

            case null, default -> {}
        }

        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, newMessageBoard).withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Calculates which deer are 'eaten' based on the number of tigers present in the meadow.
     * The method returns a set of deer that are considered eaten, assuming there is one deer
     * available for each tiger to consume.
     *
     * @param meadowArea The {@link Area} of {@link Zone.Meadow} from which animals are considered.
     * @param currentCancelledAnimals A set of {@link Animal} that are already cancelled and should
     *                         not be considered in the calculation.
     * @return A {@link Set} of {@link Animal} representing the deer that are eaten by tigers.
     *         The number of deer in this set is limited by the number of tigers in the meadow,
     *         ensuring that each tiger can only eat one deer.
     */

    private Set<Animal> eachTigerEatsOneDeer (Area<Zone.Meadow> meadowArea, Set<Animal> currentCancelledAnimals) {
        Set<Animal> animals = Area.animals(meadowArea, currentCancelledAnimals);

        Set <Animal> deerSet = animals.stream()
                .filter(animal -> animal.kind() == Animal.Kind.DEER)
                .collect(Collectors.toSet());

        return deerSet.stream()
                .limit(tigerCount(animals))
                .collect(Collectors.toSet());
    }

    /**
     * Checks if occupation of the last placed tile is impossible and finalizes the turn if so.
     * Occupation may be impossible if there are no free occupants or if the last tile does not allow for any potential occupants.
     * @return A new GameState with the turn finished or the current state if occupation is possible.
     */

    private GameState withTurnFinishedIfOccupationImpossible() {
        return lastTilePotentialOccupants().isEmpty() ? withTurnFinished() :
                new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard);
    }

    /**
     * Finalizes the current turn by processing closed areas, scoring, and determining the next action.
     * Handles special tiles' effects, calculates and assigns points for closed forests and rivers, and updates the game state accordingly.
     * @return A new GameState with updates after the turn is concluded.
     */

    private GameState withTurnFinished() {
        MessageBoard newMessageBoard = messageBoard;
        TileDecks newTileDecks = tileDecks;
        ArrayList<PlayerColor> newPlayerList = new ArrayList<>(players);
        Set<Area<Zone.Forest>> closedForestsByLastTile = board.forestsClosedByLastTile();
        Set<Area<Zone.River>> closedRiversByLastTile = board.riversClosedByLastTile();
        Board newBoard = board;

        if (newBoard.lastPlacedTile() != null && !closedForestsByLastTile.isEmpty()) {
            for (Area<Zone.Forest> forestArea : closedForestsByLastTile) {
                newMessageBoard = newMessageBoard.withScoredForest(forestArea);
            }
        }

        if (newBoard.lastPlacedTile() != null && !closedRiversByLastTile.isEmpty()) {
            for (Area<Zone.River> riverArea : closedRiversByLastTile) {
                newMessageBoard = newMessageBoard.withScoredRiver(riverArea);
            }
        }
        newBoard = newBoard.withoutGatherersOrFishersIn(closedForestsByLastTile, closedRiversByLastTile);


        if(newBoard.lastPlacedTile() != null) {
            if (newBoard.lastPlacedTile().kind() == Tile.Kind.NORMAL && closedForestsByLastTile.stream().anyMatch(Area::hasMenhir)) {
                newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, newBoard::couldPlaceTile);

                if (newTileDecks.deckSize(Tile.Kind.MENHIR) > 0) {

                    Optional<Area<Zone.Forest>> menhirAreaContainer = closedForestsByLastTile.stream().filter(Area::hasMenhir).findFirst();
                    Tile newTileToPlace = newTileDecks.topTile(Tile.Kind.MENHIR);
                    newTileDecks = newTileDecks.withTopTileDrawn(Tile.Kind.MENHIR);

                    if (menhirAreaContainer.isPresent()) {
                        newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), menhirAreaContainer.get());
                    }

                    return new GameState(newPlayerList, newTileDecks, newTileToPlace, newBoard, Action.PLACE_TILE, newMessageBoard);
                }
            } else {

                newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, newBoard::couldPlaceTile);

                if (newTileDecks.deckSize(Tile.Kind.NORMAL) > 0) {

                    Collections.rotate(newPlayerList, -1);
                    Tile newTileToPlace = newTileDecks.topTile(Tile.Kind.NORMAL);
                    newTileDecks = newTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
                    return new GameState(newPlayerList, newTileDecks, newTileToPlace, newBoard, Action.PLACE_TILE, newMessageBoard);
                }
            }
        }
        return new GameState(newPlayerList, newTileDecks, null, newBoard, Action.END_GAME, newMessageBoard).withFinalPointsCounted();
    }

    /**
     * Counts the final points at the end of the game, assigning points for meadows, rivers, and special tiles.
     * Calculates points based on the animals present, special powers activated, and updates the message board with the final scores and winners.
     * @return A new GameState reflecting the final points count and the end of the game.
     */

    private GameState withFinalPointsCounted() {
        Set<Animal> cancelledAnimals = new HashSet<>(board.cancelledAnimals());
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board;

        for (Area<Zone.Meadow> meadow : newBoard.meadowAreas()) {

            if (meadow.zoneWithSpecialPower(WILD_FIRE) instanceof Zone.Meadow) { // wildTrap
                newMessageBoard = newMessageBoard.withScoredMeadow(meadow, cancelledAnimals);

                if (meadow.zoneWithSpecialPower(PIT_TRAP) instanceof Zone.Meadow pitTrapZone) { // wildTrap and pitTrap
                    Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(board.tileWithId(pitTrapZone.tileId()).pos(), pitTrapZone);
                    newMessageBoard = newMessageBoard.withScoredPitTrap(adjacentMeadow, cancelledAnimals);
                }
            }
            else if (meadow.zoneWithSpecialPower(PIT_TRAP) instanceof Zone.Meadow pitTrapZone) { // no wildTrap but pitTrap
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(board.tileWithId(pitTrapZone.tileId()).pos(), pitTrapZone);
                Set<Animal> adjacentAnimals = Area.animals(adjacentMeadow, cancelledAnimals);
                Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
                Set<Animal> adjacentDeer = new HashSet<>();
                Set<Animal> priorityDeer = new HashSet<>();

                adjacentAnimals.stream().filter(animal -> animal.kind() == Animal.Kind.DEER).forEach(adjacentDeer::add);
                animals.stream().filter(animal -> animal.kind() == Animal.Kind.DEER && !adjacentDeer.contains(animal)).forEach(priorityDeer::add);

                //here, the animals that are in the MeadowArea but not in the adjacentMeadow are eaten first.
                // if there are still tigers, the animals in the adjacentMeadow are eaten (hence the concatenation of the two)

                Set<Animal> pitTrapCancelledAnimals = Stream.concat(priorityDeer.stream(), adjacentDeer.stream())
                                .distinct()
                                .limit(tigerCount(animals))
                                .collect(Collectors.toSet());



                cancelledAnimals.addAll(pitTrapCancelledAnimals);

                newBoard = newBoard.withMoreCancelledAnimals(cancelledAnimals);

                newMessageBoard = newMessageBoard.withScoredMeadow(meadow, cancelledAnimals);
                newMessageBoard = newMessageBoard.withScoredPitTrap(adjacentMeadow, cancelledAnimals);

            } else {  // no wildTrap or pitTrap

                Set<Animal> cancelledDeer = eachTigerEatsOneDeer(meadow, cancelledAnimals);

                cancelledAnimals.addAll(cancelledDeer);

                newBoard = newBoard.withMoreCancelledAnimals(cancelledAnimals);
                newMessageBoard = newMessageBoard.withScoredMeadow(meadow, cancelledAnimals);
            }
        }

        for (Area<Zone.Water> riverSystem : newBoard.riverSystemAreas()) {
            newMessageBoard = newMessageBoard.withScoredRiverSystem(riverSystem);
            if (riverSystem.zoneWithSpecialPower(RAFT) != null) {
                newMessageBoard = newMessageBoard.withScoredRaft(riverSystem);
            }
        }

        Map<PlayerColor, Integer> points = newMessageBoard.points();
        int maxPoints = points
                .values()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);

        Set<PlayerColor> winners = points
                .entrySet()
                .stream()
                .filter(pointsCounted -> pointsCounted.getValue() == maxPoints)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);

        return new GameState(players, tileDecks, null, newBoard, Action.END_GAME, newMessageBoard);
    }

    /**
     * Handles the action of removing an occupant from the board, such as when using the Shaman tile's special power.
     * Updates the game state to reflect the removal of the occupant, if chosen by the player.
     * @param occupant The occupant to be removed, can be null if the player chooses not to remove any.
     * @return A new GameState reflecting the game state after the occupant removal.
     * @throws IllegalArgumentException if the next action is not RETAKE_PAWN.
     */

    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN );
        Preconditions.checkArgument(occupant == null || occupant.kind() == Occupant.Kind.PAWN);
        return occupant == null ?
                new GameState(players, tileDecks, tileToPlace, board, Action.OCCUPY_TILE, messageBoard).withTurnFinishedIfOccupationImpossible()
                : new GameState(players, tileDecks, tileToPlace, board.withoutOccupant(occupant), Action.OCCUPY_TILE, messageBoard);
    }
    /**
     * Processes the addition of a new occupant to the last placed tile.
     * Updates the board with the new occupant and finalizes the turn, moving to the next action in the game flow.
     * @param occupant The occupant to be placed on the last tile, can be null if the player chooses not to place any.
     * @return A new GameState reflecting the game state after the new occupant placement.
     * @throws IllegalArgumentException if the next action is not OCCUPY_TILE.
     */

    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        Board newBoard = occupant == null ? board : board.withOccupant(occupant);
        return new GameState(players, tileDecks, null, newBoard, nextAction, messageBoard).withTurnFinished();

    }
}

