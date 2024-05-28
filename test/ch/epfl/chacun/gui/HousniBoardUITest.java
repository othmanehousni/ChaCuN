package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;


public final class HousniBoardUITest extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard", PlayerColor.YELLOW, "Alice",
                PlayerColor.PURPLE, "Bob");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        var normalTiles = tilesByKind.get(Tile.Kind.NORMAL);
        Collections.shuffle(normalTiles);
        var menhirTiles = tilesByKind.get(Tile.Kind.MENHIR);
        Collections.shuffle(menhirTiles);

        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        normalTiles,
                        menhirTiles);

        var textMaker = new TextMakerFr(playerNames);

        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);




//        gameState = gameState.withPlacedTile(new PlacedTile(TILES.get(57),
//                PlayerColor.RED,
//                Rotation.NONE,
//                new Pos(1, 0),
//                null));
//        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN,571));

//        gameState = gameState.withPlacedTile(new PlacedTile(TILES.get(58),
//                PlayerColor.BLUE,
//                Rotation.NONE,
//                new Pos(1, -1),
//                null));
        //gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.HUT, 572));






        var tileToPlaceRotationP =
                new SimpleObjectProperty<>(Rotation.NONE);
        var visibleOccupantsP =
                new SimpleObjectProperty<>(Set.<Occupant>of());
        var highlightedTilesP =
                new SimpleObjectProperty<>(Set.<Integer>of());

        var gameState0 = new SimpleObjectProperty<>(gameState);

        var rootNode = new BorderPane();
        var remainingN = gameState0.map(g-> g.tileDecks().normalTiles().size());
        var remainingM = gameState0.map(g-> g.tileDecks().menhirTiles().size());
        var str = new SimpleObjectProperty<String>(
                textMaker.clickToOccupy());
        var deck = DecksUI.create(gameState0.map(GameState::tileToPlace), remainingN, remainingM, str, o-> gameState0.setValue(gameState0.get().withNewOccupant(null)));

        var messages = gameState0.map(GameState::messageBoard).map(MessageBoard::messages);
        var misaj = gameState0.getValue().messageBoard().messages();
        var tileSet = new HashSet<Integer>();
        var tileSetProp = new SimpleObjectProperty<Set<Integer>>(tileSet);


        misaj.stream().forEach(m -> tileSet.addAll(m.tileIds()));

        var messageBoardNode = MessageBoardUI.create(messages, tileSetProp);

         var allOccupantsO = gameState0.map(g-> {

             visibleOccupantsP.set(g.board().occupants());
             if (g.board().lastPlacedTile() != null && g.nextAction() == GameState.Action.OCCUPY_TILE) {
                 visibleOccupantsP.getValue().addAll(g.board().lastPlacedTile().potentialOccupants());
             }
                return visibleOccupantsP.get();
         });

        //var allOccupants = gameState.lastTilePotentialOccupants();
      //  var allOccupantsO = new SimpleObjectProperty<Set<Occupant>>();

//        allOccupantsO.addListener((o, ov, nv) -> {
//            System.out.println("old value: " + ov);
//            System.out.println("new value: " + nv);
//        });



//        getParameters().getNamed().forEach((k, v) -> {
//            System.out.println(k + " : " + v);
//        });
       // Set<Occupant> allOccupants = gameState.lastTilePotentialOccupants();
        //ObservableValue<PlacedTile> lastPlacedTile = gs.map(g-> g.board().lastPlacedTile());
        //ObservableValue<Set<Occupant>> allOccupants= new SimpleObjectProperty<>(allOccupants);



        gameState0.set(gameState);
        var boardNode = BoardUI
                .create(6,
                        gameState0,
                        tileToPlaceRotationP,
                        allOccupantsO,
                        tileSetProp,
                        r -> {
                            tileToPlaceRotationP.set(tileToPlaceRotationP.getValue().add(r));

                        },
                        t ->  {
                    gameState0.setValue(gameState0.get().withPlacedTile(new PlacedTile(gameState0.get().tileToPlace(), gameState0.get().currentPlayer(), tileToPlaceRotationP.getValue(), t)));
                   // gameStateO.setValue(gameStateO.get()

                        },
                        o -> {gameState0.setValue(gameState0.get().withNewOccupant(o));
                    //allOccupantsO.getValue().add(o);

    });


        gameState0.set(gameState0.get().withStartingTilePlaced());

        //var gamestate2 = new GameState(gameState.players(), tileDecks.withTopTileDrawn(Tile.Kind.NORMAL), TILES.get(56), Board.EMPTY, GameState.Action.PLACE_TILE, new MessageBoard(textMaker, List.of()));

//        var gamestate3 = gamestate2.withPlacedTile(new PlacedTile(TILES.get(56),
//                PlayerColor.RED,
//                Rotation.NONE,
//                new Pos(1, 0),
//                null));
//






//        var message1 = new MessageBoard.Message(
//                textMaker.playersScoredForest(scorers1, 69, 3, 4),
//                69,
//                scorers1,
//                Set.of(7, 9)
//        );
//        var message2 = new MessageBoard.Message(textMaker.playerScoredHuntingTrap(PlayerColor.BLUE, 96, Map.of(Animal.Kind.MAMMOTH, 2, Animal.Kind.DEER,1)),
//                96,
//                scorers2,
//                Set.of(4, 6));


        //var messageList = List.of(message1, message2);
        //messageListO.setValue(messageList);
        //tileSet0.setValue(tileSet);


       // var rootPane = new VBox(messageBoardNode);

        var playersNode = PlayersUI.create(gameState0, textMaker);


        var scrollPane = new ScrollPane(messageBoardNode);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        var paneRight = new BorderPane();

       // paneRight.setMaxWidth(100);
//        List<String > str1 = new ArrayList<>();
//        gameState0.addListener((o, ov, nv) -> {
//            String s = nv.nextAction().toString();
//            str1.add(Base32.en)
//        });

        ObservableValue<List<String>> obsStrList = gameState0.map(g -> Collections.singletonList(g.nextAction().toString()));
        Base32.decode(obsStrList.getValue().getFirst());
        var actions = ActionUI.create(obsStrList, o-> System.out.println("action: " + o));
        var vboxdeckAndActions = new VBox();
        vboxdeckAndActions.getChildren().addAll(actions, deck);
        paneRight.setBottom(vboxdeckAndActions);
        paneRight.setTop(playersNode);
        paneRight.setCenter(scrollPane);
       // paneRight.getChildren().addAll(scrollPane, playersNode);


        rootNode.setCenter(boardNode);
        rootNode.setRight(paneRight);

       // rootNode.getChildren().addAll(boardNode, paneRight);

//        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
//            Platform.runLater(() -> {
//                Stage exceptionStage = new Stage();
//                StackPane exceptionPane = new StackPane();
//                exceptionPane.getChildren().add(new Label("check la console "+ throwable.getMessage()));
//                Scene exceptionScene = new Scene(exceptionPane, 300, 100);
//
//                exceptionStage.setTitle("l'exception est catch");
//                exceptionStage.setScene(exceptionScene);
//                exceptionStage.show();
//            });
//        });


            primaryStage.setScene(new Scene(rootNode, 1440, 1000));
            primaryStage.setTitle("housni test");
            primaryStage.show();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }

        }




    }