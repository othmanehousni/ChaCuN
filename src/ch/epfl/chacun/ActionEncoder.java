package ch.epfl.chacun;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ActionEncoder {

    private static final class InvalidException extends Exception { }

    private ActionEncoder() {}

    public record StateAction(GameState state, String action) {}
    private final static int MAX_FRINGE_INDEX = 190;

    private static <T> List<T> sortCollection(Collection<T> collection, Comparator<? super T> comparator) {
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private static List<Pos> sortedFringePositions(GameState state) {
        return sortCollection(state.board().insertionPositions(),
                Comparator.comparing(Pos::x).thenComparing(Pos::y));
    }

    private static List<Occupant> sortedOccupants(GameState state) {
        List<Occupant> stateOccupants = state.board().occupants()
                .stream()
                .filter(occupant -> occupant.kind() == Occupant.Kind.PAWN).toList();

        return sortCollection(stateOccupants,
                Comparator.comparing(Occupant::zoneId));
    }

    public static StateAction withPlacedTile(GameState state, PlacedTile placedTile) {
        List<Pos> sortedFringePositions = sortedFringePositions(state);
        Optional<Pos> position = sortedFringePositions.stream().filter(p -> p.equals(placedTile.pos())).findFirst();
        int positionIndex = position.map(sortedFringePositions::indexOf).orElse(-1);
        int rotationIndex = placedTile.rotation().ordinal();

        return new StateAction(state.withPlacedTile(placedTile),
                Base32.encodeBits10(positionIndex << 2 | rotationIndex));
    }

    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(state.withNewOccupant(null), Base32.encodeBits5(Base32.NO_OCCUPANT_BINARY31));
        } else {
            int zoneIndex = Zone.localId(occupant.zoneId());
            int occupantType = occupant.kind().ordinal();
            return new StateAction(state.withNewOccupant(occupant),
                    Base32.encodeBits5(occupantType << 4 | zoneIndex));
        }
    }

    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(state.withOccupantRemoved(null), Base32.encodeBits5(Base32.NO_OCCUPANT_BINARY31));
        }
        List<Occupant> occupants = sortedOccupants(state);
        int zoneIndex = occupants.indexOf(occupant);
        return new StateAction(state.withOccupantRemoved(occupant),
                Base32.encodeBits5(zoneIndex));
    }

    private static StateAction decodeAndApplyLogic(GameState state, String actionString) throws InvalidException {
        if (!Base32.isValid(actionString)) throw new InvalidException();
        GameState finalState = switch (state.nextAction()) {
            case PLACE_TILE: {
                if(actionString.length() != 2) throw new InvalidException();
                int actionValue = Base32.decode(actionString);
                List<Pos> fringePositions = sortedFringePositions(state);
                int fringeIndex = actionValue >> 2;
                int rotation = actionValue & Base32.BINARY3;
                if (fringeIndex < 0 || fringeIndex >= fringePositions.size() || fringeIndex >= MAX_FRINGE_INDEX) throw new InvalidException();
                PlacedTile placedTile = new PlacedTile(state.tileToPlace(), state.currentPlayer(),
                        Rotation.values()[rotation], fringePositions.get(fringeIndex));
                if (!state.board().canAddTile(placedTile)) throw new InvalidException();
                yield state.withPlacedTile(placedTile);
            }

            case OCCUPY_TILE: {
                if (actionString.length() != 1) throw new InvalidException();
                int actionValue = Base32.decode(actionString);
                if (actionValue == Base32.NO_OCCUPANT_BINARY31) yield state.withNewOccupant(null);
                int zoneIndex = actionValue & Base32.MASK4FIRST;
                int occupantType = (actionValue >> 4);
                Occupant.Kind kind = Occupant.Kind.values()[occupantType];
                Occupant occupant = state.lastTilePotentialOccupants().stream()
                        .filter(p -> Zone.localId(p.zoneId()) == zoneIndex
                                && p.kind() == kind)
                        .findFirst()
                        .orElseThrow(InvalidException::new);

                yield state.withNewOccupant(occupant);
            }

            case RETAKE_PAWN: {
                if(actionString.length() != 1) throw new InvalidException();
                int occupantIndex = Base32.decode(actionString);
                if (occupantIndex == Base32.NO_OCCUPANT_BINARY31) yield state.withOccupantRemoved(null);
                List<Occupant> occupants = sortedOccupants(state);
                if (occupantIndex < 0 || occupantIndex > occupants.size()) throw new InvalidException();
                Occupant occupant = occupants.get(occupantIndex);
                if (state.board().lastPlacedTile() == null
                        || state.board().lastPlacedTile().placer() != state.currentPlayer()) {
                    throw new InvalidException();
                }
                yield state.withOccupantRemoved(occupant);
            }
            case START_GAME, END_GAME:
                throw new InvalidException();
        };

        return new StateAction(finalState, actionString);
    }

    public static StateAction decodeAndApply(GameState state, String actionString) {
        try {
            return decodeAndApplyLogic(state, actionString);
        } catch (InvalidException e) {
            return null;
        }
    }
}

