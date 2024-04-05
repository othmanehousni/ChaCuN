package ch.epfl.chacun;


import ch.epfl.chacun.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ch.epfl.chacun.Tiles;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoaTest {
    Tile tile56 = Tiles.TILES.get(56);
    Tile tile57 = Tiles.TILES.get(57);
    TileDecks someTiles = new TileDecks(List.of(tile56), List.of(tile57), List.of());

    MessageBoard EMPTY = new MessageBoard(new TextMakerApo(), List.of());

    static List<Tile> NORMAL_TILES = new ArrayList<>(Tiles.TILES.subList(0, 57));
    static List<Tile> START_TILES = List.of(Tiles.TILES.get(56));

    static List<Tile> MENHIR_TILES = Tiles.TILES.subList(79, 95);

    static TileDecks allTiles = new TileDecks(START_TILES, NORMAL_TILES, MENHIR_TILES);

    @BeforeAll
    static void createTiles() {
        NORMAL_TILES.addAll(Tiles.TILES.subList(57, 79));
        allTiles = new TileDecks(START_TILES, NORMAL_TILES, MENHIR_TILES);
    }

    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new GameState(List.of(PlayerColor.YELLOW), someTiles,
                null, Board.EMPTY, GameState.Action.START_GAME, EMPTY));

        assertThrows(IllegalArgumentException.class, () -> new GameState(List.of(PlayerColor.YELLOW), someTiles,
                null, Board.EMPTY, GameState.Action.PLACE_TILE, EMPTY));

        assertThrows(IllegalArgumentException.class, () -> new GameState(List.of(PlayerColor.YELLOW), someTiles,
                tile57, Board.EMPTY, GameState.Action.START_GAME, EMPTY));

        assertThrows(NullPointerException.class, () -> new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), someTiles,
                null, Board.EMPTY, null, EMPTY));
        assertThrows(NullPointerException.class, () -> new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), null,
                null, Board.EMPTY, GameState.Action.START_GAME, EMPTY));
        assertThrows(NullPointerException.class, () -> new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), someTiles,
                null, Board.EMPTY, GameState.Action.START_GAME, null));
        assertThrows(NullPointerException.class, () -> new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), someTiles,
                null, null, GameState.Action.START_GAME, EMPTY));
    }

    @Test
    void initial() {
        TextMakerApo textClass = new TextMakerApo();
        assertEquals(new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE), someTiles, null, Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(textClass, List.of())),
                GameState.initial(List.of(PlayerColor.RED, PlayerColor.BLUE), someTiles, textClass));
    }

    @Test
    void currentPlayer() {
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass);
        //NULL if next action is START_GAME
        assertNull(normalState.currentPlayer());
        //normal case
        normalState = normalState.withStartingTilePlaced();
        assertEquals(PlayerColor.YELLOW, normalState.currentPlayer());

        //NULL if next action is END_GAME
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)))
                .withNewOccupant(null);
        assertNull(normalState.currentPlayer());
    }

    @Test
    void freeOccupantsCount() { // very good testing has been done for this method
        TextMakerApo textClass = new TextMakerApo();

        GameState normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)))
                .withNewOccupant(null);

        assertEquals(5, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));

        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)))
                .withNewOccupant(new Occupant(Occupant.Kind.PAWN, 570));

        assertEquals(4, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(Occupant.occupantsCount(Occupant.Kind.HUT), normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));


        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)))
                .withNewOccupant(new Occupant(Occupant.Kind.HUT, 578));

        assertEquals(Occupant.occupantsCount(Occupant.Kind.PAWN), normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(2, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));

        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), allTiles, textClass)
                .withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.HALF_TURN, new Pos(0, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));

        assertEquals(4, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(2, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));

        assertEquals(5, normalState.freeOccupantsCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(3, normalState.freeOccupantsCount(PlayerColor.BLUE, Occupant.Kind.HUT));


        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), allTiles, textClass)
                .withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.HALF_TURN, new Pos(0, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 28));

        assertEquals(4, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(1, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, -1))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 38));

        assertEquals(4, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));
        assertEquals(0, normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.HUT));


        GameState finalNormalState = normalState;
        assertThrows(IllegalArgumentException.class, () ->
                finalNormalState.withPlacedTile(new PlacedTile(finalNormalState.tileToPlace(),
                        PlayerColor.YELLOW, Rotation.LEFT, new Pos(0, 1))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 38)));
    }

    @Test
    void lastTilePotentialOccupants() {
        TextMakerApo textClass = new TextMakerApo();

        GameState normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)));

        Set<Occupant> occupantSetToBeEqualTo = Set.of(new Occupant(Occupant.Kind.PAWN, 570), new Occupant(Occupant.Kind.PAWN, 571),
                new Occupant(Occupant.Kind.PAWN, 572), new Occupant(Occupant.Kind.PAWN, 573),
                new Occupant(Occupant.Kind.PAWN, 574), new Occupant(Occupant.Kind.HUT, 578));

        assertEquals(occupantSetToBeEqualTo, normalState.lastTilePotentialOccupants());


        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), allTiles, textClass)
                .withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.HALF_TURN, new Pos(0, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 28));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, -1))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 38));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, -2)));

        occupantSetToBeEqualTo = Set.of(new Occupant(Occupant.Kind.PAWN, 40), new Occupant(Occupant.Kind.PAWN, 41), new Occupant(Occupant.Kind.PAWN, 42)
                , new Occupant(Occupant.Kind.PAWN, 43), new Occupant(Occupant.Kind.PAWN, 44), new Occupant(Occupant.Kind.PAWN, 45));

        assertEquals(occupantSetToBeEqualTo, normalState.lastTilePotentialOccupants());

        TileDecks newTileDecks = new TileDecks(List.of(Tiles.TILES.get(56)), List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(14)), List.of());

        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass)
                .withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 8));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.HALF_TURN, new Pos(0, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 11));


        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, -1)));

        occupantSetToBeEqualTo = Set.of(new Occupant(Occupant.Kind.PAWN, 140), new Occupant(Occupant.Kind.PAWN, 142), new Occupant(Occupant.Kind.PAWN, 143), new Occupant(Occupant.Kind.PAWN, 144)
                , new Occupant(Occupant.Kind.PAWN, 145), new Occupant(Occupant.Kind.HUT, 141));
        assertEquals(occupantSetToBeEqualTo, normalState.lastTilePotentialOccupants());


        //Check if it sends IllegalArgument Exception if board is empty
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass);
        GameState finalNormalState = normalState;
        assertThrows(IllegalArgumentException.class, finalNormalState::lastTilePotentialOccupants);
    }

    @Test
    void withStartingTilePlaced() { // Perfect
        TextMakerApo textClass = new TextMakerApo();

        TileDecks newTileDecks = new TileDecks(List.of(Tiles.TILES.get(56)), List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(14)), List.of());

        GameState normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), newTileDecks, textClass);

        TileDecks tileDecksToBeEqualTo = new TileDecks(List.of(), List.of(Tiles.TILES.get(1), Tiles.TILES.get(14)), List.of());

        assertEquals(new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), tileDecksToBeEqualTo, Tiles.TILES.get(0), Board.EMPTY
                .withNewTile(new PlacedTile(Tiles.TILES.get(56), null, Rotation.NONE, new Pos(0, 0))), GameState.Action.PLACE_TILE, new MessageBoard(textClass, List.of())), normalState.withStartingTilePlaced());

        //Error case

        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), someTiles, textClass).withStartingTilePlaced()
                .withPlacedTile(new PlacedTile(tile57, PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0)));

        assertThrows(IllegalArgumentException.class, normalState::withStartingTilePlaced);
    }


    @Test
    void withPlacedTileOccupyTile() { //Most common case //TODO seems working
        TextMakerApo textClass = new TextMakerApo();

        TileDecks newTileDecks = new TileDecks(List.of(Tiles.TILES.get(56)), List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(14)), List.of());

        GameState normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE), newTileDecks, textClass).withStartingTilePlaced();
        PlacedTile placedTileToPlace = new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0));
        normalState = normalState.withPlacedTile(placedTileToPlace);
        assertEquals(new GameState(List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE), newTileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL), null,
                Board.EMPTY.withNewTile(new PlacedTile(Tiles.TILES.get(56), null, Rotation.NONE, new Pos(0, 0))).withNewTile(placedTileToPlace), GameState.Action.OCCUPY_TILE, new MessageBoard(textClass, List.of())), normalState);
        //Normal case is handled perfectly well

        //Finish the turn (see if it sends messages and removes occupants from rivers)

        normalState = normalState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 1));
       // System.out.println(normalState.board().occupants()); // its normal because we clear fishers after


        assertEquals(new MessageBoard(textClass, List.of(new MessageBoard.Message(textClass.playersScoredRiver(Set.of(PlayerColor.YELLOW), 5, 3, 2), 5, Set.of(PlayerColor.YELLOW), Set.of(0, 56)))), normalState.messageBoard());
        assertEquals(new GameState(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW), newTileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL).withTopTileDrawn(Tile.Kind.NORMAL), Tiles.TILES.get(1),
                Board.EMPTY.withNewTile(new PlacedTile(Tiles.TILES.get(56), null, Rotation.NONE, new Pos(0, 0))).withNewTile(placedTileToPlace), GameState.Action.PLACE_TILE, normalState.messageBoard()), normalState);


        //Handle error cases
        assertThrows(IllegalArgumentException.class, () ->
                new GameState(List.of(PlayerColor.YELLOW, PlayerColor.BLUE), newTileDecks, Tiles.TILES.get(0), Board.EMPTY, GameState.Action.OCCUPY_TILE, new MessageBoard(textClass, List.of())).withPlacedTile(null));

    }



    void withPlacedTileOccupationImpossible() {
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(2), Tiles.TILES.get(3), Tiles.TILES.get(5), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(13), Tiles.TILES.get(4))
                , List.of()
        );
        //Careful design here
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        assertTrue(normalState.tileDecks().normalTiles().contains(Tiles.TILES.get(13)));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, 0))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 30));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1, -2))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 50));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.LEFT, new Pos(-1, -2))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 72));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(0, -2))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 118));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(0, -3)));

        //System.out.println(normalState.lastTilePotentialOccupants());
        //System.out.println(normalState.freeOccupantsCount(PlayerColor.YELLOW, Occupant.Kind.PAWN));

        TileDecks tileDecksToBeEqualTo = new TileDecks(
                List.of()
                , List.of()
                , List.of()
        );
        assertEquals(new GameState(List.of(PlayerColor.YELLOW, PlayerColor.RED), tileDecksToBeEqualTo, Tiles.TILES.get(4), normalState.board(), GameState.Action.PLACE_TILE, normalState.messageBoard()), normalState);
    }

    void withPlacedTileRetakePawn() {
        Zone.Meadow meadow = new Zone.Meadow(500, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide n = new TileSide.Meadow(meadow);
        TileSide e = new TileSide.Meadow(meadow);
        TileSide s = new TileSide.Meadow(meadow);
        TileSide w = new TileSide.Meadow(meadow);


        Tile chamanTile = new Tile(50, Tile.Kind.NORMAL, n, e, s, w);


        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), chamanTile, Tiles.TILES.get(2))
                , List.of()
        );
        //Careful design here
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.LEFT, new Pos(-2, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(0, -1)));
        assertEquals(GameState.Action.RETAKE_PAWN, normalState.nextAction());
        normalState = normalState.withOccupantRemoved(new Occupant(Occupant.Kind.PAWN, 10));
        //System.out.println(normalState.lastTilePotentialOccupants());
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 0)), normalState.board().occupants());
        assertEquals(GameState.Action.PLACE_TILE, normalState.nextAction());


        //Error case
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.LEFT, new Pos(-2, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));

        GameState finalNormalState = normalState;
        assertThrows(IllegalArgumentException.class, () -> finalNormalState.withOccupantRemoved(null));

        assertThrows(IllegalArgumentException.class, () -> finalNormalState.withOccupantRemoved(new Occupant(Occupant.Kind.HUT, 458)));
    }

    @Test
    void withPlacedTileReplayTurn() {
        TextMakerApo textClass = new TextMakerApo();

        /** TileDecks newTileDecks;
         GameState normalState;
         //check that when we have a menhir forest the player plays a second time

         newTileDecks = new TileDecks(
         List.of(Tiles.TILES.get(56))
         , List.of(Tiles.TILES.get(0),Tiles.TILES.get(1), Tiles.TILES.get(2),Tiles.TILES.get(3),Tiles.TILES.get(5),Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(13),Tiles.TILES.get(4))
         , List.of(Tiles.TILES.get(94))
         );
         //Careful design here
         normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks,textClass).withStartingTilePlaced();

         normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1,0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
         normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,-1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN,10));
         //Passes if we convert Tile 1 forest to WITH_MENHIR
         assertEquals(List.of(PlayerColor.RED, PlayerColor.YELLOW),normalState.players());
         assertEquals(Tiles.TILES.get(94),normalState.tileToPlace());
         normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-2,0))).withNewOccupant(null);
         assertEquals(List.of(PlayerColor.YELLOW,PlayerColor.RED), normalState.players());
         **/


        //passes all the test if we modify WITH MENHIR tile 94 and tile 1

        //check also if he is not allowed to play a third turn

    }

    void withPlacedTileEndGame() {

        //Two major cases without Traps etc or with them, wildfire
        //without traps at first

        //1. only with normal tiles and two players

        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(2))
                , List.of(Tiles.TILES.get(94))
        );
        normalState = GameState.initial(List.of(PlayerColor.RED, PlayerColor.YELLOW), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.RIGHT,
                new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE,
                new Pos(-1, -1))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 12));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(1, 0))).withNewOccupant(null);
        assertEquals(GameState.Action.END_GAME, normalState.nextAction());
        assertEquals(List.of(PlayerColor.RED, PlayerColor.YELLOW), normalState.players());


        TileDecks newTileDecksToBeEqualTo = new TileDecks(
                List.of()
                , List.of()
                , List.of(Tiles.TILES.get(94))
        );

        assertEquals(newTileDecksToBeEqualTo, normalState.tileDecks());
        var z3 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        var l1 = new Zone.Lake(56_8, 1, null);
        var z5 = new Zone.River(56_3, 0, l1);
        var a0_0 = new Animal(56_0_0, Animal.Kind.AUROCHS);
        var z0 = new Zone.Meadow(56_0, List.of(a0_0), null);
        var l2 = new Zone.Lake(2_8, 1, null);
        MessageBoard messageBoardToBeEqualTo = new MessageBoard(textClass, List.of()).withScoredForest(normalState.board().forestArea(z3))
                .withScoredRiver(normalState.board().riverArea(z5))
                .withScoredMeadow(normalState.board().meadowArea(z0), normalState.board().cancelledAnimals())
                .withScoredRiverSystem(normalState.board().riverSystemArea(l1))
                .withScoredRiverSystem(normalState.board().riverSystemArea(new Zone.River(2_1, 0, l2)))
                .withScoredRiverSystem(normalState.board().riverSystemArea(new Zone.Lake(1_8, 3, null))).withWinners(Set.of(PlayerColor.RED), 2);

        assertEquals(messageBoardToBeEqualTo, normalState.messageBoard());


        //2. contains fire
        newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(8))
                , List.of(Tiles.TILES.get(10), Tiles.TILES.get(14), Tiles.TILES.get(85))
                , List.of()
        );

        normalState = GameState.initial(List.of(PlayerColor.RED,PlayerColor.YELLOW), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.HALF_TURN, new Pos(1,0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 104));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.LEFT, new Pos(2,0))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(1,-1)));
        assertEquals(GameState.Action.END_GAME,normalState.nextAction());

        var l4 = new Zone.Lake(10_8, 1, null);
        var z4 = new Zone.River(10_3, 0, l4);
        var z1 = new Zone.River(14_1, 0, null);
        var a2_0 = new Animal(8_2_0, Animal.Kind.DEER);
        var z20 = new Zone.Meadow(8_2, List.of(a2_0), null);

        messageBoardToBeEqualTo = new MessageBoard(textClass, List.of()).withScoredRiver(normalState.board().riverArea(z4)).withScoredRiver(normalState.board().riverArea(z1))
                .withScoredMeadow(normalState.board().meadowArea(z20), Set.of()).withWinners(Set.of(PlayerColor.RED), 2);

        assertEquals(messageBoardToBeEqualTo, normalState.messageBoard());
        //3. Contains pit trap

        newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(92))
                , List.of(Tiles.TILES.get(61), Tiles.TILES.get(19), Tiles.TILES.get(6),Tiles.TILES.get(18),Tiles.TILES.get(31),Tiles.TILES.get(37),Tiles.TILES.get(60))
                , List.of()
        );

        normalState = GameState.initial(List.of(PlayerColor.RED,PlayerColor.YELLOW), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(-1,0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 610));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1,1))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(-1,-1))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(0,1))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.RIGHT, new Pos(1,1))).withNewOccupant(null);
        assertEquals(GameState.Action.PLACE_TILE,normalState.nextAction());
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-2,0))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.LEFT, new Pos(-3,0))).withNewOccupant(null);


        newTileDecks = new TileDecks(
                List.of()
                , List.of()
                , List.of()
        );
        assertEquals(newTileDecks, normalState.tileDecks());

        var z100 = new Zone.Meadow(92_1, List.of(), Zone.SpecialPower.PIT_TRAP);

        var l100 = new Zone.Lake(6_8, 1, null);
        var z15 = new Zone.River(19_1, 1, null);
        Area<Zone.Meadow> adjacentMeadow = normalState.board().adjacentMeadow(new Pos(0,0), z100);
        messageBoardToBeEqualTo = new MessageBoard(textClass, List.of()).withScoredMeadow(normalState.board().meadowArea(z100), Set.of(new Animal(3710, Animal.Kind.DEER), new Animal(6020, Animal.Kind.DEER))).withScoredPitTrap(adjacentMeadow, Set.of(new Animal(3710, Animal.Kind.DEER), new Animal(6020, Animal.Kind.DEER)))
                .withScoredRiverSystem(normalState.board().riverSystemArea(l100)).withScoredRiverSystem(normalState.board().riverSystemArea(z15)).withWinners(Set.of(PlayerColor.RED), 10);
        assertEquals(messageBoardToBeEqualTo, normalState.messageBoard());

        //4. Contains raft
        newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(91))
                , List.of(Tiles.TILES.get(59))
                , List.of()
        );

        normalState = GameState.initial(List.of(PlayerColor.RED, PlayerColor.YELLOW), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.RIGHT, new Pos(-1,0))).withNewOccupant(new Occupant(Occupant.Kind.HUT, 598));

        assertEquals(GameState.Action.END_GAME, normalState.nextAction());

        var l11 = new Zone.Lake(59_8, 1, null);
        var z200 = new Zone.River(59_1, 0, l11);
        messageBoardToBeEqualTo = new MessageBoard(textClass, List.of()).withScoredRiver(normalState.board().riverArea(z200)).withScoredRiverSystem(normalState.board().riverSystemArea(z200))
                .withScoredRaft(normalState.board().riverSystemArea(new Zone.Lake(91_8, 1, Zone.SpecialPower.RAFT))).withWinners(Set.of(PlayerColor.RED), 4);

        assertEquals(messageBoardToBeEqualTo, normalState.messageBoard());
        //Contains hunting trap and fire at the same time (it should still cancel the animals)

        newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(85))
                , List.of(Tiles.TILES.get(37), Tiles.TILES.get(61),Tiles.TILES.get(94))
                , List.of()
        );
        normalState = GameState.initial(List.of(PlayerColor.RED, PlayerColor.YELLOW), newTileDecks,textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(1,0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 371));

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(1,1)));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.RED, Rotation.NONE, new Pos(2,0))).withNewOccupant(null);

        assertEquals(GameState.Action.END_GAME, normalState.nextAction());

        newTileDecks = new TileDecks(
                List.of()
                , List.of()
                , List.of()
        );
        assertEquals(newTileDecks, normalState.tileDecks());
        assertEquals(Set.of(new Animal(3710, Animal.Kind.DEER), new Animal(6100, Animal.Kind.MAMMOTH)), normalState.board().cancelledAnimals());


        messageBoardToBeEqualTo = new MessageBoard(textClass, List.of()).withScoredHuntingTrap(PlayerColor.RED, normalState.board().adjacentMeadow(new Pos(2,0), new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP))).withScoredMeadow(normalState.board().meadowArea(new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP)),Set.of(new Animal(3710, Animal.Kind.DEER), new Animal(6100, Animal.Kind.MAMMOTH))).withWinners(Set.of(PlayerColor.RED), 4);
        assertEquals(messageBoardToBeEqualTo, normalState.messageBoard());
        //TODO seems correct


    }

    @Test
    void withPlacedTileHuntingTrap() {
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(7))
                , List.of(Tiles.TILES.get(13), Tiles.TILES.get(5), Tiles.TILES.get(94), Tiles.TILES.get(56))
                , List.of()
        );

        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(0, 1))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1, 1))).withNewOccupant(null);
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1, 0))).withNewOccupant(null);
        var z1 = new Zone.Meadow(94_1, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        Area<Zone.Meadow> adjacentMeadow = normalState.board().adjacentMeadow(new Pos(-1, 0), z1);

        var l1 = new Zone.Lake(13_8, 2, null);
        var z7 = new Zone.River(13_7, 0, l1);

        var z2 = new Zone.River(13_1, 0, l1);
        Area<Zone.River> riverArea1 = normalState.board().riverArea(z7);
        Area<Zone.River> riverArea2 = normalState.board().riverArea(z2);
        assertEquals(new MessageBoard(textClass, List.of())
                        .withScoredRiver(riverArea1)
                        .withScoredRiver(riverArea2)
                        .withScoredHuntingTrap(PlayerColor.YELLOW, adjacentMeadow)
                , normalState.messageBoard());

        assertEquals(Set.of(new Animal(700, Animal.Kind.TIGER), new Animal(1300, Animal.Kind.DEER), new Animal(500, Animal.Kind.AUROCHS)), normalState.board().cancelledAnimals());
    }

    void withPlacedTileLogBoat() {
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(93), Tiles.TILES.get(2))
                , List.of()
        );

        normalState = GameState.initial(List.of(PlayerColor.RED, PlayerColor.YELLOW), newTileDecks, textClass).withStartingTilePlaced();
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(-1, 0))).withNewOccupant(null);
        var l1 = new Zone.Lake(93_8, 1, Zone.SpecialPower.LOGBOAT);
        Area<Zone.Water> riverSystem = normalState.board().riverSystemArea(l1);
        assertEquals(new MessageBoard(textClass, List.of()).withScoredLogboat(PlayerColor.YELLOW, riverSystem), normalState.messageBoard());
    }

    @Test
    void withNewOccupant() { // I think its ok
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;

        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(2), Tiles.TILES.get(3), Tiles.TILES.get(5), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(13), Tiles.TILES.get(4))
                , List.of()
        );
        //Careful design here
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 0)), normalState.board().occupants());
        assertEquals(GameState.Action.PLACE_TILE, normalState.nextAction());
        assertEquals(List.of(PlayerColor.RED, PlayerColor.YELLOW), normalState.players());

        TileDecks newTileDecksToBeEqualTo = new TileDecks(
                List.of()
                , List.of(Tiles.TILES.get(2), Tiles.TILES.get(3), Tiles.TILES.get(5), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(13), Tiles.TILES.get(4))
                , List.of()
        );
        assertEquals(newTileDecksToBeEqualTo, normalState.tileDecks());

        //Error case

        GameState finalNormalState = normalState;
        assertThrows(IllegalArgumentException.class, () -> finalNormalState.withNewOccupant(null));
    }

    void withOccupantRemoved() {
        TextMakerApo textClass = new TextMakerApo();
        GameState normalState;


        Zone.Meadow meadow = new Zone.Meadow(500, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide n = new TileSide.Meadow(meadow);
        TileSide e = new TileSide.Meadow(meadow);
        TileSide s = new TileSide.Meadow(meadow);
        TileSide w = new TileSide.Meadow(meadow);


        Tile chamanTile = new Tile(50, Tile.Kind.NORMAL, n, e, s, w);


        TileDecks newTileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56))
                , List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), chamanTile, Tiles.TILES.get(2))
                , List.of()
        );
        //Careful design here
        normalState = GameState.initial(List.of(PlayerColor.YELLOW, PlayerColor.RED), newTileDecks, textClass).withStartingTilePlaced();

        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.RIGHT, new Pos(-1, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 0));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.LEFT, new Pos(-2, 0))).withNewOccupant(new Occupant(Occupant.Kind.PAWN, 10));
        normalState = normalState.withPlacedTile(new PlacedTile(normalState.tileToPlace(), PlayerColor.YELLOW, Rotation.NONE, new Pos(0, -1)));
        assertEquals(GameState.Action.RETAKE_PAWN, normalState.nextAction());
        normalState = normalState.withOccupantRemoved(new Occupant(Occupant.Kind.PAWN, 10));
        //System.out.println(normalState.lastTilePotentialOccupants());
        assertEquals(Set.of(new Occupant(Occupant.Kind.PAWN, 0)), normalState.board().occupants());
        assertEquals(GameState.Action.PLACE_TILE, normalState.nextAction());

        TileDecks newTileDecksToBeEqualTo = new TileDecks(
                List.of()
                , List.of()
                , List.of()
        );
        assertEquals(newTileDecksToBeEqualTo, normalState.tileDecks());
        assertEquals(GameState.Action.PLACE_TILE, normalState.nextAction());
        assertEquals(List.of(PlayerColor.RED, PlayerColor.YELLOW), normalState.players());

    }
}


