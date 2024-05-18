package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;

import java.util.*;

public final class ActionEncoder {

    private static final class InvalidException extends Exception {}


    private ActionEncoder() {}

    public record StateAction(GameState state, String action) { }


    public static StateAction withPlacedTile(GameState state, PlacedTile placedTile) {
        List<Pos> liste = state.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparing(Pos::x)
                        .thenComparing(Pos::y)).toList();

        Optional<Pos> position = liste.stream().filter(p -> p.equals(placedTile.pos())).findFirst();
        int positionIndex = position.map(liste::indexOf).orElse(-1);
        int rotationIndex = placedTile.rotation().ordinal();

        return new StateAction(state.withPlacedTile(placedTile),
                Base32.encodeBits10(positionIndex | rotationIndex)); //+ ?
    }

    public static StateAction withNewOccupant(GameState state, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(state.withNewOccupant(null), Base32.encodeBits5(Base32.BINARY3));
        } else {
            int zoneIndex = Zone.localId(occupant.zoneId());
            int occupantType = occupant.kind().ordinal();
            return new StateAction(state.withNewOccupant(occupant),
                    Base32.encodeBits5(occupantType << 4 | zoneIndex));
        }
    }

    public static StateAction withOccupantRemoved(GameState state, Occupant occupant) {
        List<Occupant> occupants = state.board().occupants()
                .stream()
                .sorted(Comparator.comparing(Occupant::zoneId))
                .toList();

        int zoneIndex = occupants.indexOf(occupant);

        return new StateAction(state.withOccupantRemoved(occupant),
                Base32.encodeBits5(zoneIndex));


    }

    private static StateAction decodeAndApplyLogic(GameState state, String actionString) throws InvalidException {
        if (Base32.isValid(actionString)) {
            switch (state.nextAction()) {
                case PLACE_TILE: {
                    int actionValue = Base32.decode(actionString);
                    List<Pos> fringePositions = state.board().insertionPositions()
                            .stream()
                            .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y))
                            .toList();

                    int fringeIndex = actionValue >> 2;
                    int rotation = actionValue & Base32.BINARY3;
                    if (fringeIndex < 0 || fringeIndex >= fringePositions.size()) { throw new InvalidException();
                    }
                    else {
                        PlacedTile placedTile = new PlacedTile(state.tileToPlace(), state.currentPlayer(), Rotation.values()[rotation], fringePositions.get(fringeIndex));
                        if (!state.board().canAddTile(placedTile) || actionString.length() != 2) {
                            throw new InvalidException();
                        } else {
                            return withPlacedTile(state, placedTile);

                        }
                    }
                }

                case OCCUPY_TILE: {
                    int actionValue = Base32.decode(actionString);
                    int zoneIndex = actionValue & 0xF;
                    int occupantType = (actionValue >> 4) & 1;
                    Occupant occupant = new Occupant(occupantType == 0 ? Occupant.Kind.PAWN : Occupant.Kind.HUT, zoneIndex);

                    if (state.freeOccupantsCount(state.currentPlayer(), occupant.kind()) == 0 || actionString.length() != 1) {
                        throw new InvalidException();
                    }

                    return withNewOccupant(state, occupant);
                }

                case RETAKE_PAWN: {
                    int zoneIndex = Base32.decode(actionString);
                    List<Occupant> occupants = state.board().occupants().stream().sorted(Comparator.comparing(Occupant::zoneId)).toList();
                    if (zoneIndex < 0 || zoneIndex >= occupants.size()) {
                        throw new InvalidException();
                    }
                    Occupant occupant = occupants.get(zoneIndex);
                    if (!(state.board().occupantCount(state.currentPlayer(), occupant.kind()) > 0) || actionString.length() != 1) {
                        //verifier que c'est le joueur qui a place qui doit prendre
                        throw new InvalidException();
                    }

                    return withOccupantRemoved(state, occupant);
                }

                // }
            }
        }
        throw new InvalidException();
    }

    public static StateAction decodeAndApply(GameState state, String actionString) {
        try {
            return decodeAndApplyLogic(state, actionString);
        } catch (InvalidException e) {
            return null;
        }
    }
}

