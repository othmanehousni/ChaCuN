package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Tile (int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {
    public enum Kind {
        START,
        NORMAL,
        MENHIR;
    }

    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    public Set<Zone> sideZones() {
        Set<Zone> sideZones = new HashSet<>();
        for (TileSide side : sides()) {
            sideZones.addAll(side.zones());
        }
        return sideZones;
    }

    public Set<Zone> zones() {
        Set<Zone> allZones = new HashSet<>(sideZones());
        for (Zone zones : allZones) {
            if(zones instanceof Zone.River && (((Zone.River) zones).hasLake())) {{
                    allZones.add(((Zone.River) zones).lake());
                }
            }
        }
        return allZones;
    }
}
