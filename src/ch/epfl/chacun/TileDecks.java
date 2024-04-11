package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * Represents the decks of tiles of three different kinds: start, normal, and menhir.
 * It allows treating all tiles uniformly and simplifies the handling of tiles throughout the game.
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    /**
     * Ensures the immutability of TileDecks by copying the input lists.
     */
    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * Returns the right list using the given kind in the method's argument.
     *
     * @param kind The kind of tile deck from which to remove the top tile.
     * @return The right list using the given kind.
     */
    private List<Tile> rightList(Tile.Kind kind) {
        return switch (kind) {
            case Tile.Kind.START -> startTiles;
            case Tile.Kind.NORMAL -> normalTiles;
            case Tile.Kind.MENHIR -> menhirTiles;
        };
    }


    /**
     * Returns the number of tiles available in the deck of the specified kind.
     *
     * @param kind The kind of tiles to count.
     * @return The number of available tiles in the specified deck.
     */
    public int deckSize(Tile.Kind kind) {
        return rightList(kind).size();
    }

    /**
     * Returns the top tile from the deck of the specified kind, or null if the deck is empty.
     *
     * @param kind The kind of the deck from which to retrieve the top tile.
     * @return The top tile of the specified deck, or null if the deck is empty.
     */
    public Tile topTile(Tile.Kind kind) {
        List<Tile> topTile = rightList(kind);
        return topTile.isEmpty() ? null : topTile.getFirst();

    }

    /**
     * Returns a new TileDecks instance with the top tile of the specified kind removed.
     *
     * @param kind The kind of tile deck from which to remove the top tile.
     * @return A new TileDecks instance with the top tile removed.
     * @throws IllegalArgumentException If the specified deck is empty.
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(!rightList(kind).isEmpty());
        List<Tile> newList = rightList(kind);
        List<Tile> newList2 = newList.subList(1, newList.size());

        if (kind == Tile.Kind.START) {
            return new TileDecks(newList2, normalTiles, menhirTiles);
        } else if (kind == Tile.Kind.NORMAL) {
            return new TileDecks(startTiles, newList2, menhirTiles);
        } else {
            return new TileDecks(startTiles, normalTiles, newList2);
        }
    }

    /**
     * Returns a new TileDecks instance with the top tiles removed until a tile satisfies the given predicate.
     *
     * @param kind      The kind of tile deck from which to remove the top tiles.
     * @param predicate A predicate that determines which tiles to remove.
     * @return A new TileDecks instance with the top tiles removed as per the predicate.
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {

        TileDecks tileDeck = this;
        while (tileDeck.topTile(kind) != null && !predicate.test(tileDeck.topTile(kind))) {
            tileDeck = tileDeck.withTopTileDrawn(kind);
        }
        return tileDeck;
    }

}