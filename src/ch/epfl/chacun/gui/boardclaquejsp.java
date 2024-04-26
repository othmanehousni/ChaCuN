package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.function.Consumer;

public class boardclaquejsp {

    private boardclaquejsp() {
    }

    private record CellData(Image backgroundImage, int rotationDegrees, ColorInput overlayColor) {}

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


        Map<Integer, Image> cache = new HashMap<>();

        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {
                Group cellGroup = new Group();
                boardGrid.add(cellGroup, x + scope, y + scope);

                ImageView tileImageView = new ImageView();
                ImageView cancelImageView = new ImageView();
                ImageView highlightImageView = new ImageView();
                tileImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                cellGroup.getChildren().addAll(tileImageView, cancelImageView, highlightImageView);

                Pos currentPos = new Pos(x, y);
                ImageView previewImageView = new ImageView();
                previewImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                previewImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                WritableImage emptyTileImage = new WritableImage(1, 1);

                ObjectBinding<CellData> cellDataBinding = Bindings.createObjectBinding(() -> {
                    GameState currentState = gameStateObservable.getValue();
                    PlacedTile placedTile = currentState.board().tileAt(currentPos);
                    ColorInput color = new ColorInput(currentPos.x(), currentPos.y(), ImageLoader.NORMAL_TILE_FIT_SIZE, ImageLoader.NORMAL_TILE_FIT_SIZE, Color.TRANSPARENT);
                    Blend blend = new Blend(BlendMode.SRC_OVER);
                    if (placedTile != null) {
                        Image image = cache.computeIfAbsent(placedTile.tile().id(), ImageLoader::normalImageForTile);
                        tileImageView.setImage(ImageLoader.normalImageForTile(placedTile.tile().id()));
                        return new CellData(image, placedTile.rotation().degreesCW(), color);
                    } else {
                        emptyTileImage.getPixelWriter().setColor(0, 0, Color.gray(0.98));
                        if (currentState.board().insertionPositions().contains(currentPos)) {
                            cellGroup.effectProperty().bind(Bindings.createObjectBinding(() -> {
                                if (cellGroup.hoverProperty().getValue()) {
                                    previewImageView.setImage(ImageLoader.normalImageForTile(
                                            currentState.tileToPlace().id()));
                                    previewImageView.setOpacity(0.5);
                                    if (currentState.board().canAddTile(new PlacedTile(currentState.tileToPlace(), currentState.currentPlayer(), tileRotationObservable.getValue(), currentPos, null))) {
                                        cellGroup.getChildren().add(previewImageView);
                                    } else {
                                        color.setPaint(Color.WHITE.deriveColor(0, 1, 1, 0.5));
                                        blend.setTopInput(color);
                                        cellGroup.getChildren().add(previewImageView);
                                    }
                                } else {
                                    cellGroup.getChildren().removeIf(node -> node != tileImageView && node instanceof ImageView);
                                    color.setPaint(ColorMap.fillColor(currentState.currentPlayer()).deriveColor(0, 1, 1, 0.5));
                                    blend.setTopInput(color);

                                }
                                return blend;
                            }, gameStateObservable, cellGroup.hoverProperty(), tileRotationObservable));
                        }
                    }

                    cellGroup.setOnMouseClicked(event -> {
                        if (event.isSecondaryButtonDown()) {
                            Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                            onRotateTile.accept(rotation);
                            cellGroup.rotateProperty().set(rotation.degreesCW());
                        } else if (event.isPrimaryButtonDown() && gameStateObservable.getValue().nextAction().equals(GameState.Action.PLACE_TILE)) {
                            onPlaceTile.accept(currentPos);
                        }
                    });
                    return new CellData(emptyTileImage, tileRotationObservable.getValue().degreesCW(), color);
                }, gameStateObservable, tileRotationObservable, cellGroup.hoverProperty());

                cellDataBinding.addListener((obs, oldData, newData) -> {
                    tileImageView.setImage(oldData.backgroundImage());
                    cellGroup.setRotate(newData.rotationDegrees());

                });

                visibleOccupantsObservable.addListener((obs2, oldOccupants, newOccupants) -> {
                    oldOccupants.forEach(occupant -> {
                        if (gameStateObservable.getValue().board().occupants().contains(occupant)) {
                            Node node = Icon.newFor(gameStateObservable.getValue().currentPlayer(), occupant.kind());
                            node.setId(STR."\{occupant.kind().name()}-\{occupant.zoneId()}");
                            nodeList.add(Icon.newFor(gameStateObservable.getValue().currentPlayer(), occupant.kind()));
                        }
                        if (!gameStateObservable.getValue().lastTilePotentialOccupants().contains(occupant)) {
                            nodeList.forEach(node -> {
                                if (node.getId().equals(STR."\{occupant.kind().name()}-\{occupant.zoneId()}")) {
                                    node.getStyleClass().add("marker");
                                    node.setId(STR."\{occupant.kind().name()}-\{occupant.zoneId()}");
                                    cancelImageView.setImage(new Image("marker.png"));
                                    nodeList.remove(node);
                                }
                            });
                        }
                    });
                });

                highlightedTilesObservable.getValue().forEach(integer -> {
                    highlightImageView.imageProperty().bind(Bindings.createObjectBinding(() -> {
                        if (cache.containsKey(integer)) {
                            return cache.get(integer);
                        } else {
                            return ImageLoader.normalImageForTile(integer);
                        }
                    }, highlightedTilesObservable));
                });
            }
            ;


    }

        
            return boardScrollPane;
        }

    }
