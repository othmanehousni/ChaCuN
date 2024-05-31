package ch.epfl.chacun;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The ActionEncoder class provides methods for encoding and decoding game actions.
 * This class is public and non-instantiable.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
public final class ActionEncoder {

    /**
     * Exception thrown when an invalid action is encountered during decoding.
     */
    private static final class InvalidException extends Exception { }

    /**
     * Private constructor to prevent instantiation.
     */
    private ActionEncoder() {}

    /**
     * Record representing a state-action pair.
     */
    public record StateAction(GameState state, String action) {}

    // maximum value that the fringe index can take
    private final static int MAX_FRINGE_INDEX = 190;

    /**
     * Sorts a collection using the specified comparator.
     *
     * @param collection the collection to sort
     * @param comparator the comparator to use for sorting
     * @param <T> the type of elements in the collection
     * @return a sorted list of the collection's elements
     */
    private static <T> List<T> sortCollection(Collection<T> collection, Comparator<? super T> comparator) {
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Returns the sorted list of fringe positions in the given game state.
     *
     * @param state the game state
     * @return the sorted list of fringe positions
     */
    private static List<Pos> sortedFringePositions(GameState state) {
        return sortCollection(state.board().insertionPositions(),
                Comparator.comparing(Pos::x).thenComparing(Pos::y));
    }

    /**
     * Returns the sorted list of occupants in the given game state.
     *
     * @param state the game state
     * @return the sorted list of occupants
     */
    private static List<Occupant> sortedOccupants(GameState state) {
        List<Occupant> stateOccupants = state.board().occupants()
                .stream()
                .filter(occupant -> occupant.kind() == Occupant.Kind.PAWN).toList();

        return sortCollection(stateOccupants,
                Comparator.comparing(Occupant::zoneId));
    }

    /**
     * Encodes an action of placing a tile in the game state.
     *
     * @param state the game state
     * @param placedTile the placed tile
     * @return the state-action pair after the tile is placed
     */
    public static StateAction withPlacedTile(GameState state, PlacedTile placedTile) {
        List<Pos> sortedFringePositions = sortedFringePositions(state);
        // once the fringe positions are sorted, we can find the index of the placed tile's position
        Optional<Pos> position = sortedFringePositions.stream().filter(pos -> pos.equals(placedTile.pos())).findFirst();
        int positionIndex = position.map(sortedFringePositions::indexOf).orElse(-1);
        int rotationIndex = placedTile.rotation().ordinal();
        // once we have the position index and the rotation index, we can encode the action,
        // and return the new state-action pair. The first 2 bits are used for the rotation index,
        // while the last 8 bits are used for the position index.
        return new StateAction(state.withPlacedTile(placedTile),
                Base32.encodeBits10(positionIndex << 2 | rotationIndex));
    }

    /**
     * Encodes an action of placing a new occupant in the game state.
     *
     * @param state the game state
     * @param occupant the new occupant
     * @return the state-action pair after the occupant is placed
     */
    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(state.withNewOccupant(null), Base32.encodeBits5(Base32.NO_OCCUPANT_BINARY31));
        } else {
            // find the index of the zone using the local ID of the occupant's zone
            int zoneIndex = Zone.localId(occupant.zoneId());
            // its type is either pawn or hut, thus 0 or 1.
            int occupantType = occupant.kind().ordinal();
            // once we have the zone index and the occupant type, we can encode the action,
            // and return the new state-action pair. The last 4 bits are used for the zone index,
            // while the first bit is used for the occupant type.
            return new StateAction(state.withNewOccupant(occupant),
                    Base32.encodeBits5(occupantType << 4 | zoneIndex));
        }
    }

    /**
     * Encodes an action of removing an occupant from the game state.
     *
     * @param state the game state
     * @param occupant the occupant to remove
     * @return the state-action pair after the occupant is removed
     */
    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(state.withOccupantRemoved(null), Base32.encodeBits5(Base32.NO_OCCUPANT_BINARY31));
        }
        // find the index of the occupant in the sorted list of occupants, where there only is pawn occupants.
        // they are sorted by zone ID.
        List<Occupant> occupants = sortedOccupants(state);
        int occupantIndex = occupants.indexOf(occupant);
        // once we have the zone index, we can encode the action, and return the new state-action pair.
        // The 5 bits are used for the occupant index.
        return new StateAction(state.withOccupantRemoved(occupant),
                Base32.encodeBits5(occupantIndex));
    }

    /**
     * Decodes and applies the action string to the game state.
     *
     * @param state the game state
     * @param actionString the action string
     * @return the state-action pair after the action is applied
     * @throws InvalidException if the action string is invalid
     */
    private static StateAction decodeAndApplyLogic(GameState state, String actionString) throws InvalidException {
        if (!Base32.isValid(actionString)) throw new InvalidException();
        GameState finalState = switch (state.nextAction()) {
            case PLACE_TILE: {
                // the action string should be of length 2
                if(actionString.length() != 2) throw new InvalidException();
                int actionValue = Base32.decode(actionString);
                List<Pos> fringePositions = sortedFringePositions(state);
                // we know that the first 8 bits are used for the position index, while the last 2 bits are
                // used for the rotation index.
                // we decode the position index and the rotation index from the action value.
                int fringeIndex = actionValue >> 2;
                int rotation = actionValue & Base32.BINARY3;
                // if the fringe index is out of bounds, we throw an exception.
                if (fringeIndex < 0 || fringeIndex >= fringePositions.size() || fringeIndex >= MAX_FRINGE_INDEX)
                    throw new InvalidException();

                PlacedTile placedTile = new PlacedTile(state.tileToPlace(), state.currentPlayer(),
                        Rotation.values()[rotation], fringePositions.get(fringeIndex));
                // if the tile cannot be added to the board, we throw an exception.
                if (!state.board().canAddTile(placedTile)) throw new InvalidException();
                // if the tile can be added to the board, we return the new state-action pair.
                yield state.withPlacedTile(placedTile);
            }

            case OCCUPY_TILE: {
                // the action string should be of length 1
                if (actionString.length() != 1) throw new InvalidException();
                int actionValue = Base32.decode(actionString);
                // if the action value is the binary representation of no occupant, we return the new state-action pair.
                if (actionValue == Base32.NO_OCCUPANT_BINARY31) yield state.withNewOccupant(null);
                int zoneIndex = actionValue & Base32.MASK4FIRST;
                int occupantType = (actionValue >> 4);
                // we decode the zone index and the occupant type from the action value.
                Occupant.Kind kind = Occupant.Kind.values()[occupantType];
                // we find the occupant in the last tile's potential occupants that has the same zone index and kind.
                Occupant occupant = state.lastTilePotentialOccupants().stream()
                        .filter(p -> Zone.localId(p.zoneId()) == zoneIndex
                                && p.kind() == kind)
                        .findFirst()
                        .orElseThrow(InvalidException::new);

                yield state.withNewOccupant(occupant);
            }

            case RETAKE_PAWN: {
                // the action string should be of length 1
                if(actionString.length() != 1) throw new InvalidException();
                int occupantIndex = Base32.decode(actionString);
                // if the action value is the binary representation of no occupant, we return the new state-action pair.
                if (occupantIndex == Base32.NO_OCCUPANT_BINARY31) yield state.withOccupantRemoved(null);
                List<Occupant> occupants = sortedOccupants(state);
                // if the occupant index is out of bounds, we throw an exception.
                if (occupantIndex < 0 || occupantIndex > occupants.size()) throw new InvalidException();
                // we find the occupant in the sorted list of occupants using the occupant index.
                Occupant occupant = occupants.get(occupantIndex);
                if (state.board().lastPlacedTile() == null
                        || state.board().lastPlacedTile().placer() != state.currentPlayer()) {
                    throw new InvalidException();
                }
                yield state.withOccupantRemoved(occupant);
            }
            // if the next action is not one of the above, we throw an exception.
            case START_GAME, END_GAME:
                throw new InvalidException();
        };

        return new StateAction(finalState, actionString);
    }

    /**
     * Decodes and applies the action string to the game state.
     *
     * @param state the game state
     * @param actionString the action string
     * @return the state-action pair after the action is applied, or null if the action string is invalid
     */
    public static StateAction decodeAndApply(GameState state, String actionString) {
        try {
            return decodeAndApplyLogic(state, actionString);
        } catch (InvalidException e) {
            return null;
        }
    }
}

