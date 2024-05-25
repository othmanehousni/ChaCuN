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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.function.Consumer;

public final class BoardUI{

    private BoardUI() {
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

   private static List<Node> visibleMarkers(PlacedTile placedTile,
                                            ObservableValue<Set<Animal>> cancelledAnimals) {

        List<Node> nodeList = new ArrayList<>();
        placedTile.meadowZones()
                .stream()
                .flatMap(meadow -> meadow.animals().stream())
                .forEach(animal -> {
                    ImageView image = new ImageView();
                    image.setFitWidth(ImageLoader.MARKER_FIT_SIZE);
                    image.setFitHeight(ImageLoader.MARKER_FIT_SIZE);
                    image.setId(STR."marker_\{animal.id()}");
                    image.getStyleClass().add("marker");
                     image.visibleProperty().bind(cancelledAnimals.map(c -> c.contains(animal)));
                    nodeList.add(image);
        });
        return nodeList;
    }

    private static List<Node> visibleOccupants(PlacedTile placedTile,
                                               ObservableValue<Set<Occupant>> visibleOccupantsO,
                                               Consumer<Occupant> onSelectOccupant) {

        List<Node> nodeList = new ArrayList<>();
        for (Occupant occupant : placedTile.potentialOccupants()) {
            Node node = Icon.newFor(placedTile.placer(), occupant.kind());
            node.setId(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}");
            node.setRotate(placedTile.rotation().negated().degreesCW());
            node.visibleProperty().bind(visibleOccupantsO.map(occupants -> occupants.contains(occupant)));
            node.setOnMouseClicked(_ -> onSelectOccupant.accept(occupant));
            nodeList.add(node);
        }
        return nodeList;
    }


    public static Node create(
            int scope,
            ObservableValue<GameState> gameStateO,
            ObservableValue<Rotation> tileRotationO,
            ObservableValue<Set<Occupant>> visibleOccupantsO,
            ObservableValue<Set<Integer>> highlightedTilesO,
            Consumer<Rotation> onRotateTile,
            Consumer<Pos> onPlaceTile,
            Consumer<Occupant> onSelectOccupant) {


        ScrollPane boardScrollPane = new ScrollPane();
        boardScrollPane.setId("board-scroll-pane");
        boardScrollPane.getStylesheets().add("board.css");
        boardScrollPane.setVvalue(0.5);
        boardScrollPane.setHvalue(0.5);

        GridPane boardGrid = new GridPane();
        boardGrid.setId("board-grid");
        boardScrollPane.setContent(boardGrid);

        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {
                Group cellGroup = new Group();
                ImageView tileImageView = new ImageView();
                tileImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                cellGroup.getChildren().add(tileImageView);
                boardGrid.add(cellGroup, x + scope, y + scope);

                Pos currentPos = new Pos(x, y);
                ObservableValue<Board> boardObserver = gameStateO.map(GameState::board);
                ObservableValue<Set<Pos>> fringeObserver = boardObserver.map(Board::insertionPositions);
                ObservableValue<PlacedTile> placedTileObserver = boardObserver.map(board -> board.tileAt(currentPos));
                ObservableValue<Set<Animal>> cancelledAnimalsO = boardObserver.map(Board::cancelledAnimals);

                ColorInput colorInput = new ColorInput(0, 0, ImageLoader.NORMAL_TILE_FIT_SIZE,
                        ImageLoader.NORMAL_TILE_FIT_SIZE, Color.TRANSPARENT);

                Blend blend = new Blend(BlendMode.SRC_OVER, null, colorInput);
                blend.setOpacity(0.5);

                placedTileObserver.addListener((_, old, newTile) -> {
                    List<Node> currentVisibleNodes = new ArrayList<>();
                    if(old == null && newTile != null) {
                        currentVisibleNodes.addAll(visibleMarkers(newTile, cancelledAnimalsO));
                        currentVisibleNodes.addAll(visibleOccupants(newTile, visibleOccupantsO, onSelectOccupant));
                        cellGroup.getChildren().addAll(currentVisibleNodes);
                    }
                });

                BooleanBinding fringe = Bindings.createBooleanBinding(() -> fringeObserver.getValue().contains(currentPos)
                        && gameStateO.getValue().tileToPlace()!=null, fringeObserver, placedTileObserver, gameStateO);
                ObjectBinding<CellData> cellDataBinding = Bindings.createObjectBinding(() -> {
                    if (placedTileObserver.getValue() != null) {
                        Color color = highlightedTilesO.getValue().isEmpty()
                                || highlightedTilesO.getValue().contains(placedTileObserver.getValue().tile().id()) ?
                                null : Color.BLACK;
                        return new CellData(placedTileObserver.getValue(), color);

                    } else if (fringe.getValue()) {
                        if (cellGroup.hoverProperty().getValue()) {
                            if (gameStateO.getValue().tileToPlace() != null) {
                                PlacedTile placedTile = new PlacedTile(gameStateO.getValue().tileToPlace(),
                                        gameStateO.getValue().currentPlayer(), tileRotationO.getValue(), currentPos);

                                Color color = boardObserver.getValue().canAddTile(placedTile) ? null : Color.WHITE;
                                return new CellData(placedTile, color);
                            }
                        } else {
                            PlayerColor player = gameStateO.getValue().currentPlayer();
                            if (player != null) {
                                return new CellData(ColorMap.fillColor(player));
                            }
                        }
                    }
                    return new CellData(null);
                }, gameStateO, fringe, placedTileObserver, highlightedTilesO, tileRotationO,
                        cellGroup.hoverProperty(), visibleOccupantsO, boardObserver);


                cellGroup.effectProperty().bind(cellDataBinding.map(cellData -> {
                    colorInput.setPaint(cellData.overlayColor());
                    if (cellData.overlayColor() == null) {
                        return null;
                    } else {
                        return blend;
                    }
                }));

                tileImageView.imageProperty().bind(cellDataBinding.map(CellData::backgroundImage));
                cellGroup.rotateProperty().bind(cellDataBinding.map(CellData::rotationDegrees));

                cellGroup.setOnMouseClicked(event -> {
                    if(event.isStillSincePress()) {
                        if (event.getButton().equals(MouseButton.PRIMARY)) {
                            PlacedTile tileAtPos = gameStateO.getValue().board().tileAt(currentPos);
                            Tile tileToPlace = gameStateO.getValue().tileToPlace();

                            if (tileAtPos == null && fringe.getValue() && tileToPlace != null) {
                                PlacedTile placedTileToPlace = new PlacedTile(tileToPlace,
                                        gameStateO.getValue().currentPlayer(),
                                        tileRotationO.getValue(), currentPos);

                                if (gameStateO.getValue().board().canAddTile(placedTileToPlace)) {
                                    onPlaceTile.accept(currentPos);
                                }
                            }
                        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                            Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                            onRotateTile.accept(rotation);
                        }
                    }
                });

                //fixme c'est le bon truc a faire mais le test marche pas avec mais garde le
                //todo methode privee : prend placedtile et observable value des visible occupants et consumer
                //todo todo methode privee : prend placedtile et observable value du set de cancelled animals
                // flots qqpart


            }
        }
        return boardScrollPane;
    }

}



//                placedTileObserver.addListener((obs, oldTile, newTile) -> {
//                    for (Occupant occupant : gameStateO.getValue().lastTilePotentialOccupants()) {
//                            Node node = Icon.newFor(newTile.placer(), occupant.kind());
//                            node.setId(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}");
//                            node.visibleProperty().bind(Bindings.createBooleanBinding(() -> placedTileObserver.getValue().potentialOccupants().contains(occupant), gameStateO, placedTileObserver));
//                            cellGroup.getChildren().add(node);
//                            nodeList.add(node);
//                            node.onMouseClickedProperty().set(event -> {
//                                if (gameStateO.getValue().freeOccupantsCount(gameStateO.getValue().currentPlayer(), occupant.kind()) > 0) {
//                                    onSelectOccupant.accept(occupant);
//                                    cellGroup.getChildren().removeAll(nodeList);
//                                    visibleOccupantsO.getValue().add(occupant);
//                                    cellGroup.getChildren().add(node);
//                                }
//                            });
//                        }
//                });


