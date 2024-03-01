package ch.epfl.chacun;

import java.util.ArrayList;
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
            case START -> startTiles.isEmpty() ? null : startTiles.getFirst();
            case NORMAL -> normalTiles.isEmpty() ? null : normalTiles.getFirst();
            case MENHIR -> menhirTiles.isEmpty() ? null : menhirTiles.getFirst();
        };
    }

    public TileDecks withTopTileDrawn(Tile.Kind kind){
        ArrayList <Tile> startTilesToArrayList = new ArrayList<>(startTiles);
        ArrayList <Tile> normalTilesToArrayList = new ArrayList<>(normalTiles);
        ArrayList<Tile> menhirTilesToArrayList = new ArrayList<>(menhirTiles);

        if (kind == Tile.Kind.START && !startTiles.isEmpty()){
            startTilesToArrayList.removeFirst();
        }else if (kind == Tile.Kind.NORMAL && !normalTiles.isEmpty()){
            normalTilesToArrayList.removeFirst();
        }else if (kind == Tile.Kind.MENHIR && !menhirTiles.isEmpty()){
            menhirTilesToArrayList.removeFirst();}
        else {
            throw new IllegalArgumentException();
        }

        return new TileDecks(startTilesToArrayList,normalTilesToArrayList,menhirTilesToArrayList);
    }


    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate){

        if (deckSize(kind) != 0 && topTile(kind) !=null && !predicate.test(topTile(kind))){
            return withTopTileDrawn(kind);
        }else {
            return this;
        }

    }
}