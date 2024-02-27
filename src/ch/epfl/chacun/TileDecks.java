package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

final public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    public TileDecks{
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    public  int deckSize(Tile.Kind kind){
        return switch (kind){
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };
    }

    public Tile topTile(Tile.Kind kind){

        return switch (kind){
            case START -> startTiles.size() == 0 ? null : startTiles.get(0);
            case NORMAL -> normalTiles.size() == 0 ? null : normalTiles.get(0);
            case MENHIR -> menhirTiles.size() == 0 ? null : menhirTiles.get(0);
        };
    }

    public TileDecks withTopTileDrawn(Tile.Kind kind){

        TileDecks newTileDecks= new TileDecks(startTiles,normalTiles,menhirTiles);

        if (kind == Tile.Kind.START && startTiles.size()!=0){
            startTiles.remove(0);
        }else if (kind == Tile.Kind.NORMAL && normalTiles.size()!=0){
            normalTiles.remove(0);
        }else if (kind == Tile.Kind.MENHIR && menhirTiles.size()!=0){
            menhirTiles.remove(0);}
        else {
            throw new IllegalArgumentException();

        }
        return newTileDecks;
    }


    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate){

        if (false==predicate.test(topTile(kind))){
            return withTopTileDrawn(kind);
        }else {
            return this;
        }

    }
}