package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainTest extends Application {
    //todelete



    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));
        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var textMaker = new TextMakerFr(playerNames);

        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        var gameStateO = new SimpleObjectProperty<>(gameState);
        var messageList = gameStateO.map(gameState1 -> gameState1.messageBoard().messages());
        var tileToPlace = gameStateO.map(GameState::tileToPlace);
        var normalCount = gameStateO.map(gameState1 -> gameState1.tileDecks().deckSize(Tile.Kind.NORMAL));
        var menhirCount = gameStateO.map(gameState1 -> gameState1.tileDecks().deckSize(Tile.Kind.MENHIR));
        var text = gameStateO.map(gameState1->gameState1.nextAction()== GameState.Action.OCCUPY_TILE?textMaker.clickToOccupy():gameState1.nextAction()== GameState.Action.RETAKE_PAWN?textMaker.clickToUnoccupy():"");

        var actions = new SimpleObjectProperty<List<String>>(new ArrayList<>());
        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        Consumer<Occupant> consumer = e->{
            gameStateO.set(gameStateO.get().withNewOccupant(e));
        };

//        Consumer<String> actionConsumer = string -> {
//            if(ActionEncoder.decodeAndApply(gameStateO.get() , string)!=null) {
//                List<String> newList = new ArrayList<>(actions.getValue());
//                newList.add(string);
//                actions.set(newList);
//                gameStateO.set(ActionEncoder.decodeAndApply(gameStateO.get() , string).gameState());
//            }
//        };

        gameState=gameState.withStartingTilePlaced().
                withPlacedTile(new PlacedTile(tileDecks.topTile(Tile.Kind.NORMAL) ,PlayerColor.RED , Rotation.RIGHT , new Pos(-1,0)  ));
        //withNewOccupant(new Occupant(Occupant.Kind.PAWN , 0_1));

        var playersNode = PlayersUI.create(gameStateO, textMaker);
        var messagesNode = MessageBoardUI.create(messageList , new SimpleObjectProperty<>(Set.of()));
       // var actionNode = ActionsUI.create(actions , actionConsumer);
        var decksNode = DecksUI.create(tileToPlace,normalCount,menhirCount,text,consumer);


        var boardNode = BoardUI
                .create(5,
                        gameStateO,
                        tileToPlaceRotationP,
                        visibleOccupantsP,
                        highlightedTilesP,
                        r -> tileToPlaceRotationP.set(tileToPlaceRotationP.get().add(r)),
                        t -> {
                            PlacedTile justPlaced = new PlacedTile(gameStateO.getValue().tileToPlace(), gameStateO.get().currentPlayer(), tileToPlaceRotationP.getValue(), t);
                            if(gameStateO.get().board().canAddTile(justPlaced) && gameStateO.get().nextAction()== GameState.Action.PLACE_TILE) {
                                gameStateO.set(gameStateO.getValue().withPlacedTile(justPlaced));
                                Set<Occupant> newSet = new HashSet<>(visibleOccupantsP.get());
                                newSet.addAll(justPlaced.potentialOccupants());
                                visibleOccupantsP.set(newSet);
                            }
                        },
                        o -> {
                            if(gameStateO.get().nextAction()== GameState.Action.OCCUPY_TILE) {
                                if (o == null )
                                    gameStateO.set(gameStateO.getValue().withNewOccupant(null));
                                else {
                                    Set<Occupant> newSet = new HashSet<>(visibleOccupantsP.getValue());
                                    newSet.removeAll(gameStateO.get().board().tileWithId(o.zoneId() / 10).potentialOccupants());
                                    newSet.add(o);
                                    visibleOccupantsP.set(newSet);
                                    gameStateO.set(gameStateO.getValue().withNewOccupant(o));
                                }
                            }
                        });

        var rootNodePlayerUI = new VBox(playersNode,messagesNode, decksNode);
        var rootNodeBoardUI = new BorderPane(boardNode);

        var rootNode = new HBox(rootNodeBoardUI, rootNodePlayerUI);

        gameStateO.set(gameStateO.get().withStartingTilePlaced());

        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }

}