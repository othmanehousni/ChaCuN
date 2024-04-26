//package ch.epfl.chacun.gui;
//
//import ch.epfl.chacun.*;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.scene.Group;
//import javafx.scene.effect.ColorAdjust;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Consumer;
//
//public final class boardclaque {
//    private static final Map<Integer, Image> tileImageCache = new HashMap<>();
//    private static final double VOILE_OPACITY = 0.5;
//
//    public static GridPane create(
//            int scope,
//            SimpleObjectProperty<GameState> gameStateProperty,
//            SimpleObjectProperty<Rotation> tileRotationProperty,
//            SimpleObjectProperty<Set<Occupant>> visibleOccupantsProperty,
//            SimpleObjectProperty<Set<Integer>> highlightedTilesProperty,
//            Consumer<Rotation> onRotation,
//            Consumer<Pos> onPlacement,
//            Consumer<Occupant> onOccupantSelection) {
//
//        GridPane boardGrid = new GridPane();
//
//        for (int x = -scope; x <= scope; x++) {
//            for (int y = -scope; y <= scope; y++) {
//                Pos pos = new Pos(x, y);
//                Group cellGroup = createCell(
//                        pos, gameStateProperty, tileRotationProperty,
//                        visibleOccupantsProperty, highlightedTilesProperty,
//                        onRotation, onPlacement, onOccupantSelection);
//                boardGrid.add(cellGroup, x + scope, y + scope);
//            }
//        }
//
//        return boardGrid;
//    }
//
//    private static Group createCell(
//            Pos pos,
//            SimpleObjectProperty<GameState> gameStateProperty,
//            SimpleObjectProperty<Rotation> tileRotationProperty,
//            SimpleObjectProperty<Set<Occupant>> visibleOccupantsProperty,
//            SimpleObjectProperty<Set<Integer>> highlightedTilesProperty,
//            Consumer<Rotation> onRotation,
//            Consumer<Pos> onPlacement,
//            Consumer<Occupant> onOccupantSelection) {
//
//        Group cellGroup = new Group();
//        ImageView tileView = new ImageView();
//        Rectangle voile = new Rectangle(ImageLoader.LARGE_TILE_FIT_SIZE, ImageLoader.LARGE_TILE_FIT_SIZE, Color.TRANSPARENT);
//        tileView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
//        tileView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
//
//        cellGroup.getChildren().add(tileView);
//        cellGroup.getChildren().add(voile);
//
//        gameStateProperty.addListener((obs, oldGameState, newGameState) -> {
//            PlacedTile placedTile = newGameState.board().tileAt(pos);
//            if (placedTile != null) {
//                tileView.setImage(tileImageCache.get(placedTile.tile().id()));
//                tileView.setRotate(placedTile.rotation().quarterTurnsCW());
//                tileImageCache.put(placedTile.tile().id(), ImageLoader.normalImageForTile(placedTile.tile().id()));
//            } else {
//                tileView.setImage(null);
//            }
//        });
//
//        visibleOccupantsProperty.addListener((obs, oldOccupants, newOccupants) -> {
//            cellGroup.getChildren().removeIf(node -> node instanceof ImageView && node != tileView);
//            for (Occupant occupant : newOccupants) {
//                if (occupant.getPosition().equals(pos)) {
//                    ImageView occupantView = new ImageView();
//                    occupantView.setImage(Icon.newFor(gameStateProperty.get().currentPlayer(),occupant.kind()));
//                    occupantView.setFitWidth(TILE_SIZE);
//                    occupantView.setFitHeight(TILE_SIZE);
//                    occupantView.setRotate(-tileRotationProperty.get().toDegrees());
//                    cellGroup.getChildren().add(occupantView);
//                }
//            }
//        });
//
//
//        highlightedTilesProperty.addListener((obs, oldIds, newIds) -> {
//            if (!newIds.contains(pos)) {
//                ColorAdjust darken = new ColorAdjust();
//                darken.setBrightness(-0.5);
//                tileView.setEffect(darken);
//            } else {
//                tileView.setEffect(null);
//            }
//        });
//
//        cellGroup.setOnMouseClicked(event -> {
//            if (event.getButton() == MouseButton.PRIMARY) {
//                onPlacement.accept(pos);
//            } else if (event.getButton() == MouseButton.SECONDARY) {
//                Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
//                onRotation.accept(rotation);
//            }
//        });
//
//        cellGroup.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//            if (gameStateProperty.get().nextAction() == GameState.Action.PLACE_TILE && isNowHovered) {
//                // Show tile to place with the current rotation
//                // Simulate tile placement preview with appropriate rotation
//            } else {
//                // Hide tile placement preview or remove voile
//                voile.setFill(Color.TRANSPARENT);
//            }
//        });
//
//        return cellGroup;
//    }
//}