package ch.epfl.chacun;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 * <p>
 * Represents a tile that has been placed on the board.
 * It encapsulates information about the tile, its placement, rotation, and any occupant it might have.
 */
public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {


    /**
     * Validates the non-null arguments and initializes a new {@code PlacedTile}.
     *
     * @throws NullPointerException if {@code tile}, {@code rotation}, or {@code pos} is {@code null}.
     */
    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    /**
     * Secondary constructor to create a placed tile without an occupant.
     * This is a convenience constructor for when a tile is placed without any occupant.
     *
     * @param tile     The tile being placed.
     * @param placer   The player placing the tile, or {@code null} for the starting tile.
     * @param rotation The rotation applied to the tile upon placement.
     * @param pos      The position where the tile is placed.
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Returns the identifier of the placed tile.
     *
     * @return the ID of the tile.
     */
    public int id() {
        return tile.id();
    }

    /**
     * Returns the kind of the placed tile.
     *
     * @return the kind of the tile.
     */
    public Tile.Kind kind() {
        return tile.kind();
    }


    /**
     * Returns the side of the tile in the given direction, considering the applied rotation.
     *
     * @param direction The direction in which to find the tile side.
     * @return the tile side in the specified direction.
     */
    public TileSide side(Direction direction) {
        return tile.sides().get((direction.rotated(rotation.negated())).ordinal());
    }

    /**
     * Finds and returns the zone with the specified ID on the tile.
     *
     * @param id The ID of the zone to find.
     * @return the zone with the given ID.
     * @throws IllegalArgumentException if there is no zone with the specified ID on the tile.
     */
    public Zone zoneWithId(int id) {
        for (Zone zones : tile.zones()) {
            if (zones.id() == id) {
                return zones;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the zone with a special power on the tile, if any.
     *
     * @return the zone with a special power, or {@code null} if there isn't one.
     */
    public Zone specialPowerZone() {
        for (Zone zones : tile.zones()) {
            if (zones.specialPower() != null) {
                return zones;
            }
        }
        return null;
    }

    /**
     * Returns the set of forest zones on the tile.
     *
     * @return a set of forest zones.
     */
    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> forests = new HashSet<>();
        for (Zone zones : tile.sideZones()) {
            if (zones instanceof Zone.Forest forest) {
                forests.add(forest);
            }
        }
        return forests;
    }

    /**
     * Returns the set of meadow zones on the tile.
     *
     * @return a set of meadow zones.
     */
    public Set<Zone.Meadow> meadowZones() {
        Set<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zones : tile.sideZones()) {
            if (zones instanceof Zone.Meadow meadow) {
                meadows.add(meadow);
            }
        }
        return meadows;
    }

    /**
     * Returns the set of river zones on the tile.
     *
     * @return a set of river zones.
     */

    public Set<Zone.River> riverZones() {
        Set<Zone.River> rivers = new HashSet<>();
        for (Zone zones : tile.sideZones()) {
            if (zones instanceof Zone.River river) {
                rivers.add(river);
            }
        }
        return rivers;
    }

    /**
     * Returns the set of all potential occupants for the tile, or an empty set if the tile is the starting tile.
     *
     * @return a set of potential occupants.
     * @throws IllegalArgumentException if the tile is the starting tile (placer is {@code null}).
     */
    public Set<Occupant> potentialOccupants() {
        Set<Occupant> potentialOccupants = new HashSet<>();
        if (placer == null) {
            return Collections.emptySet();
        } else {
            for (Zone zones : tile.sideZones()) {
                potentialOccupants.add(new Occupant(Occupant.Kind.PAWN, zones.id()));
                if (zones instanceof Zone.Water.River river) {
                    if (river.hasLake()) {
                        potentialOccupants.add(new Occupant(Occupant.Kind.HUT, river.lake().id()));
                    } else {
                        potentialOccupants.add(new Occupant(Occupant.Kind.HUT, zones.id()));

                    }

                }
            }
        }
        return potentialOccupants;
    }

    /**
     * Returns a new {@code PlacedTile} identical to this one but with the specified occupant.
     *
     * @param occupant The occupant to assign to the new tile.
     * @return a new {@code PlacedTile} with the specified occupant.
     * @throws IllegalArgumentException if this tile already has an occupant.
     */

    public PlacedTile withOccupant(Occupant occupant) {
        Preconditions.checkArgument(occupant() == null);
        return new PlacedTile(tile(), placer(), rotation(), pos(), occupant);
    }

    /**
     * Returns a new {@code PlacedTile} identical to this one but without any occupant.
     *
     * @return a new {@code PlacedTile} with no occupant.
     */
    public PlacedTile withNoOccupant() {
        return new PlacedTile(tile(), placer(), rotation(), pos(), null);
    }

    /**
     * Returns the ID of the zone occupied by an occupant of the given kind.
     * <p>
     * This method checks if the tile has an occupant of the specified kind and returns the zone ID where
     * the occupant is located. If there is no such occupant, or if the tile does not have an occupant,
     * the method returns -1.
     *
     * @param occupantKind The kind of the occupant (e.g., PAWN, HUT) for which to find the occupied zone.
     * @return The ID of the zone occupied by the occupant of the given kind, or -1 if not applicable.
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if (occupant() == null || occupant().kind() != occupantKind) {
            return -1;
        }
        return occupant().zoneId();
    }
}
