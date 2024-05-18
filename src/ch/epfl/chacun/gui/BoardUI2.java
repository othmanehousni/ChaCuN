package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.function.Consumer;

public final class BoardUI2 {

    private BoardUI2() {
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

        Map<Integer, Image> cache = new HashMap<>();

        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {

                Group cellGroup = new Group();
                boardGrid.add(cellGroup, x + scope, y + scope);

                ImageView tileImageView = new ImageView();
                ImageView cancelImageView = new ImageView();
                tileImageView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                tileImageView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
                cellGroup.getChildren().addAll(tileImageView, cancelImageView);
                cellGroup.getChildren().addAll(nodeList);


                Pos currentPos = new Pos(x, y);
                ObservableValue<PlacedTile> placedTileObserver = gameStateObservable.map(gameState -> gameState.board().tileAt(currentPos));

                ImageView previewImageView = new ImageView();
                Blend blend = new Blend(BlendMode.SRC_OVER);

                System.out.println(gameStateObservable.getValue().tileToPlace());

                WritableImage emptyTileImage = new WritableImage(1, 1);

                gameStateObservable.addListener((obs, oldState, newState) -> {
                            PlacedTile placedTile = newState.board().tileAt(currentPos);
                    System.out.println(placedTileObserver.getValue());
                            if (placedTile != null) {
                                Image currentImage = ImageLoader.largeImageForTile(placedTile.tile().id());
                                tileImageView.setImage(currentImage);
                                cache.put(placedTile.tile().id(), currentImage);
                            } else {
                                emptyTileImage.getPixelWriter().setColor(0, 0, Color.gray(0.98));
                                tileImageView.setImage(emptyTileImage);
                                if (newState.board().insertionPositions().contains(currentPos)) {
                                    cellGroup.effectProperty().bind(Bindings.createObjectBinding(() -> {
                                        if (cellGroup.hoverProperty().getValue()) {
                                            previewImageView.setImage(ImageLoader.normalImageForTile(
                                                    newState.tileToPlace().id()));
                                            previewImageView.setOpacity(0.5);
                                            if (newState.board().canAddTile(new PlacedTile(newState.tileToPlace(), newState.currentPlayer(), tileRotationObservable.getValue(), currentPos, null))) {
                                                cellGroup.getChildren().add(previewImageView);
                                            } else {
                                                ColorInput color = new ColorInput(currentPos.x(), currentPos.y(), ImageLoader.LARGE_TILE_FIT_SIZE, ImageLoader.LARGE_TILE_FIT_SIZE, Color.TRANSPARENT);
                                                color.setPaint(Color.WHITE.deriveColor(0, 1, 1, 0.5));
                                                blend.setTopInput(color);
                                                cellGroup.getChildren().add(previewImageView);
                                            }
                                        } else {
                                            cellGroup.getChildren().remove(previewImageView);
                                            ColorInput color = new ColorInput(currentPos.x(), currentPos.y(), ImageLoader.LARGE_TILE_FIT_SIZE, ImageLoader.LARGE_TILE_FIT_SIZE, Color.TRANSPARENT);
                                            color.setPaint(ColorMap.fillColor(newState.currentPlayer()).deriveColor(0, 1, 1, 0.5));
                                            blend.setTopInput(color);
                                        }
                                        return blend;
                                    }, gameStateObservable, cellGroup.hoverProperty()));
                                }
                            }
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

                        cellGroup.setOnMouseClicked(event -> {
                            if (event.isSecondaryButtonDown()) {
                                Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                                onRotateTile.accept(rotation);
                                cellGroup.rotateProperty().set(rotation.degreesCW());
                            } else if (event.isPrimaryButtonDown() && gameStateObservable.getValue().nextAction().equals(GameState.Action.PLACE_TILE)) {
                                onPlaceTile.accept(currentPos);
                                if (gameStateObservable.getValue().nextAction().equals(GameState.Action.OCCUPY_TILE)) {
                                    onSelectOccupant.accept(null);
                                }
                            }
                        });

                        highlightedTilesObservable.addListener((obs, oldTiles, newTiles) -> {
                            oldTiles.forEach(integer -> {
                                tileImageView.imageProperty().bind(Bindings.createObjectBinding(() -> {
                                    if (cache.containsKey(integer)) {
                                        return cache.get(integer);
                                    } else {
                                        return ImageLoader.largeImageForTile(integer);
                                    }
                                    }, highlightedTilesObservable));
                            });
                        });
            }
        }

        return boardScrollPane;
    }

}
