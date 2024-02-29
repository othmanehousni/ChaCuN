package ch.epfl.chacun;

import java.util.*;

public record PlacedTile (Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {

    public PlacedTile {
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos) {
        this(tile, placer, rotation, pos, null);
    }

    public int id() {
        return tile.id();
    }

    public Tile.Kind kind() {
        return tile.kind();
    }

    public TileSide side(Direction direction) {
        return tile.sides().get((direction.ordinal() + rotation.ordinal()) % 4);
    }

    public Zone zoneWithId(int id) {
        Set<Zone> allZones = tile.zones();
        for (Zone zones : allZones) {
            if (zones.tileId() == id) {
                return zones;
            }
        }
        throw new IllegalArgumentException();
    }

    public Zone specialPowerZone() {
        Set<Zone> allZones = tile.zones();
        for (Zone zones : allZones) {
            if (zones.specialPower() != null) {
                return zones;
            }
        }
        return null;
    }

    public Set<Zone.Forest> forestZones() {
        Set<Zone.Forest> forests = new HashSet<>();
        for (Zone zones : tile.zones()) {
            if (zones instanceof Zone.Forest forest) {
                forests.add(forest);
            }
        }
        return forests;
    }

    public Set<Zone.Meadow> meadowsZones() {
        Set<Zone.Meadow> meadows = new HashSet<>();
        for (Zone zones : tile.zones()) {
            if (zones instanceof Zone.Meadow meadow) {
                meadows.add(meadow);
            }
        }
        return meadows;
    }

    public Set<Zone.River> riverZones() {
        Set<Zone.River> rivers = new HashSet<>();
        for (Zone zones : tile.zones()) {
            if (zones instanceof Zone.River river) {
                rivers.add(river);
            }
        }
        return rivers;
    }

    public Set<Occupant> potentialOccupants() {
        Set<Occupant> potentialOccupants = new HashSet<>();
        if (placer == null) {
            throw new IllegalArgumentException(); }
        else {
            for (Zone zones : tile.sideZones()) {
                if (!(zones instanceof Zone.Lake)) {
                    potentialOccupants.add(new Occupant(Occupant.Kind.PAWN, zones.id()));
                }
                if (zones instanceof Zone.River || zones instanceof Zone.Lake) {
                        potentialOccupants.add(new Occupant(Occupant.Kind.HUT, zones.id()));
                    }
                }
            }
        return potentialOccupants;
    }

    public PlacedTile withOccupant(Occupant occupant) {
        if (occupant() != null) {
            throw new IllegalArgumentException();
        }
        return new PlacedTile(tile(), placer(), rotation(), pos(), occupant);
    }

    public PlacedTile withNoOccupant() {
        return new PlacedTile(tile(), placer(), rotation(), pos(), null);
    }
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind) {
        if(occupant() == null || occupant().kind() != occupantKind) {
            return -1;
        }
        return occupant().zoneId();
    }
}
