package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * Represents a tile which has not yet been placed in the game.
 * Tiles are the fundamental building blocks of the game board.
 */

public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * Returns a list of the four sides of the tile, in the order: North, East, South, West.
     *
     * @return A list containing the tile's sides.
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    /**
     * Returns the set of edge zones of the tile, i.e., those that touch at least one edge.
     * This excludes lake zones which are considered internal.
     *
     * @return A set of zones that are on the edge of the tile.
     */
    public Set<Zone> sideZones() {
        Set<Zone> sideZones = new HashSet<>();
        for (TileSide side : sides()) {
            sideZones.addAll(side.zones());
        }
        return sideZones;
    }

    /**
     * Returns the set of all zones on the tile, including lakes.
     *
     * @return A set containing all zones of the tile.
     */

    public Set<Zone> zones() {
        Set<Zone> allZones = new HashSet<>();
        for (Zone zones : sideZones()) {
            allZones.add(zones);
            if (zones instanceof Zone.River river && river.hasLake()) {
                {
                    allZones.add(river.lake());
                }
            }
        }
        return allZones;
    }


    /**
     * Enumerates the types of tiles that exist in the game.
     */
    public enum Kind {
        START,
        NORMAL,
        MENHIR
    }
}
