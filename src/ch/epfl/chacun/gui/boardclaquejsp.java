package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.*;
import java.util.function.Consumer;

public final class boardclaquejsp {

    private boardclaquejsp() {
    }

    private record CellData(Image backgroundImage, int rotationDegrees, Color overlayColor) {

        static final Map<Integer, Image> CACHE = new HashMap<>();

        static final WritableImage EMPTY_IMAGE = createEmptyTileImage();

        CellData(PlacedTile placedTile, Color color){
            this(CACHE.computeIfAbsent(placedTile.tile().id(),
                    ImageLoader::normalImageForTile),
                    placedTile.rotation().degreesCW(), color);
        }

        CellData(Color color){
            this(EMPTY_IMAGE, 0, color);
        }

        static WritableImage createEmptyTileImage() {
            WritableImage emptyTileImage = new WritableImage(1, 1);
            emptyTileImage.getPixelWriter().setColor(0, 0, Color.gray(0.98));
            return emptyTileImage;
        }

    }

    public static Node create(
            int scope,
            ObservableValue<GameState> gameStateObservable,
            ObservableValue<Rotation> tileRotationObservable,
            ObservableValue<Set<Occupant>> visibleOccupantsObservable,
            ObservableValue<Set<Integer>> highlightedTilesObservable,
            Consumer<Rotation> onRotateTile,
            Consumer<Pos> onPlaceTile,
            Consumer<Occupant> onSelectOccupant) {


        ScrollPane boardScrollPane = new ScrollPane();
        boardScrollPane.setId("board-scroll-pane");
        boardScrollPane.getStylesheets().add("board.css");

        GridPane boardGrid = new GridPane();
        boardGrid.setId("board-grid");
        boardScrollPane.setContent(boardGrid);
        List<Node> nodeList = new ArrayList<>();



        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {
                Group cellGroup = new Group();

                ImageView tileImageView = new ImageView();
                //ImageView cancelImageView = new ImageView();
                //ImageView highlightImageView = new ImageView();
                tileImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                cellGroup.getChildren().add(tileImageView);
                boardGrid.add(cellGroup, x + scope, y + scope);

                Pos currentPos = new Pos(x, y);
                ImageView previewImageView = new ImageView();
                previewImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                previewImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                ObservableValue<Board> boardObserver = gameStateObservable.map(GameState::board);
                ObservableValue<Set<Pos>> fringeObserver = boardObserver.map(Board::insertionPositions);
                GameState currentState = gameStateObservable.getValue();
                ObservableValue<PlacedTile> placedTileObserver = boardObserver.map(board -> board.tileAt(currentPos));
                //placedTileObserver.addListener((obs, oldTile, newTile) -> {
                    //cellGroup.getChildren().addAll(nodeList);
                    // markers et occupants à ajouter
               // });

                //obserable sur placedTile


//                boolean binding -> (gameStateObservable et observable de la frange, prend tile to place. si diff de nul et observablevalue sur set de pos contient la current pos)
//                si en get les highilighted value : null ou contient placedtile.id alors on met null, sinon c'est black
                BooleanBinding fringe = Bindings.createBooleanBinding(() -> fringeObserver.getValue().contains(currentPos), fringeObserver, placedTileObserver, gameStateObservable);
                ObjectBinding<CellData> cellDataBinding = Bindings.createObjectBinding(() -> {
                    if (placedTileObserver.getValue() != null) {
                        Color color = highlightedTilesObservable.getValue().isEmpty() || highlightedTilesObservable.getValue().contains(placedTileObserver.getValue().tile().id()) ? null : Color.BLACK;
                        return new CellData(placedTileObserver.getValue(), color);

                    } else if (fringe.getValue()) {
                        if (cellGroup.hoverProperty().getValue()) {
                            PlacedTile placedTile = new PlacedTile(gameStateObservable.getValue().tileToPlace(), gameStateObservable.getValue().currentPlayer(), tileRotationObservable.getValue(), currentPos);
                            Color color = gameStateObservable.getValue().board().canAddTile(placedTile) ? null : Color.WHITE;
                            return new CellData(placedTile, color);
                        } else {
                            if(gameStateObservable.getValue().currentPlayer() != null) {
                                return new CellData(ColorMap.fillColor(gameStateObservable.getValue().currentPlayer())); }
                            }
                    } else {
                        return new CellData(null);
                    }
                    return new CellData(null);
                }, gameStateObservable, fringe, placedTileObserver, highlightedTilesObservable, tileRotationObservable, cellGroup.hoverProperty());



                ColorInput color = new ColorInput(currentPos.x(), currentPos.y(), ImageLoader.NORMAL_TILE_FIT_SIZE, ImageLoader.NORMAL_TILE_FIT_SIZE, Color.TRANSPARENT);
                Blend blend = new Blend(BlendMode.SRC_OVER, null, color);
                blend.setOpacity(0.5);

                cellGroup.effectProperty().bind(cellDataBinding.map(cellData -> {
                    color.setPaint(cellData.overlayColor());
                    if (cellData.overlayColor() == null) {
                        return null;
                    } else {
                        return blend;
                    }
                }));

                tileImageView.imageProperty().bind(cellDataBinding.map(CellData::backgroundImage));
                cellGroup.rotateProperty().bind(cellDataBinding.map(CellData::rotationDegrees));

                cellGroup.setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                            onPlaceTile.accept(currentPos); }
                            //onSelectOccupant.accept(new Occupant(Occupant.Kind.PAWN, placedTileObserver.getValue().tile().id()));

                    if(event.getButton().equals(MouseButton.SECONDARY)) {
                        Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                        onRotateTile.accept(rotation);
                        //cellGroup.rotateProperty().set(rotation.degreesCW());
                    }
                });
//                cellGroup.onMouseClickedProperty().bind(Bindings.createObjectBinding(() -> {
//                    if (placedTileObserver.getValue() != null) {
//                        return event -> {
//                            if (event.isPrimaryButtonDown()) {
//                                Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
//                                onRotateTile.accept(rotation);
//                                cellGroup.rotateProperty().set(rotation.degreesCW());
//                                onSelectOccupant.accept(new Occupant(Occupant.Kind.PAWN, placedTileObserver.getValue().tile().id()));
//                            }
//                        };
//                    } else if (fringe.getValue()) {
//                        return event -> {
//                            if (event.isPrimaryButtonDown()) {
//                                onPlaceTile.accept(currentPos);
//                            }
//                        };
//                    } else {
//                        return null;
//                    }
//                }, placedTileObserver, fringe, gameStateObservable, tileRotationObservable));


                ObservableValue<Set<Animal>> cancelledAnimals = gameStateObservable.map(gameState -> gameState.board().cancelledAnimals());
                cancelledAnimals.getValue().forEach(animal -> {
                    SVGPath svgPath = new SVGPath();
                    svgPath.setContent("marker.png");
                    //Node node = Icon.newFor(gameStateObservable.getValue().board().tileWithId(animal.zoneId() / 10).placer(), animal.kind());
                    svgPath.setId(STR."marker_\{animal.id()}");
                    svgPath.getStyleClass().add("marker");
                    nodeList.add(svgPath);
                    cellGroup.getChildren().addAll(nodeList);
                });

                ObservableValue<Set<Occupant>> visibleOccupants = gameStateObservable.map(gameState -> gameState.board().occupants());
                visibleOccupants.getValue().forEach(occupant -> {
                    if (gameStateObservable.getValue().board().tileWithId(occupant.zoneId() / 10) == placedTileObserver.getValue()) {
                        Node node = Icon.newFor(gameStateObservable.getValue().board().tileWithId(occupant.zoneId() / 10).placer(), occupant.kind());
                        node.setId(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}");
                        nodeList.add(node);
                        cellGroup.getChildren().addAll(nodeList);

//                        if (!gameStateObservable.getValue().lastTilePotentialOccupants().contains(occupant)) {
//                            boolean remove = false;
//                            Node toremove = null;
//                            for (Node node1 : nodeList)
//                                if (node1.getId().equals(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}")) {
//                                    node1.getStyleClass().add("marker");
//                                    node1.setId(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}");
//                                    remove = true;
//                                    toremove = node1;
//                                    //tileImageView.setImage(new Image("marker.png"));
//                                    //nodeList.remove(node1);
//
//                                }
//                            if (remove) {
//                                nodeList.remove(toremove);
//
//                            }
//                        }
                    }
                });
            }

            ;


    }

        
            return boardScrollPane;
        }

    }
