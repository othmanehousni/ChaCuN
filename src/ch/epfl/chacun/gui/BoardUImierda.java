//package ch.epfl.chacun.gui;
//
//import ch.epfl.chacun.*;
//import javafx.beans.binding.Bindings;
//import javafx.beans.value.ObservableValue;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.image.ImageView;
//import javafx.scene.image.WritableImage;
//import javafx.scene.layout.GridPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.SVGPath;
//
//import java.util.Set;
//import java.util.function.Consumer;
//
//public final class BoardUImierda {
//    // Size of the tile in pixels
//    private static final double TILE_SIZE = 256;
//    private static final Color FRINGE_COLOR = Color.BLUE; // This should be set to the current player's color
//
//    private BoardUImierda() {}
//
//    public static Node create(
//            int scope,
//            ObservableValue<GameState> gameStateObservable,
//            ObservableValue<Rotation> tileRotationObservable,
//            ObservableValue<Set<Occupant>> visibleOccupantsObservable,
//            ObservableValue<Set<Integer>> highlightedTilesObservable,
//            Consumer<Rotation> onRotateTile,
//            Consumer<Pos> onPlaceTile,
//            Consumer<Occupant> onSelectOccupant) {
//
//        ScrollPane boardScrollPane = new ScrollPane();
//        boardScrollPane.setId("board-scroll-pane");
//
//        GridPane boardGrid = new GridPane();
//        boardGrid.setId("board-grid");
//        boardScrollPane.setContent(boardGrid);
//
//        // Iterate over the grid scope to create cells
//        for (int x = -scope; x <= scope; x++) {
//            for (int y = -scope; y <= scope; y++) {
//                Pos pos = new Pos(x, y);
//                Group cellGroup = new Group();
//                ImageView tileImageView = new ImageView();
//                tileImageView.setFitWidth(TILE_SIZE);
//                tileImageView.setFitHeight(TILE_SIZE);
//
//                // Add the default empty tile
//                tileImageView.setImage(createEmptyTileImage());
//
//                // Check if the cell is at a fringe position
//                if (gameStateObservable.getValue().board().insertionPositions().contains(pos)) {
//                    // Apply fringe styling
//                    tileImageView.setStyle("-fx-opacity: 0.5; -fx-background-color: " + FRINGE_COLOR.toString().replace("0x", "#"));
//                }
//
//                // Setup tile rotation based on observable value
//                tileImageView.rotateProperty().bind(Bindings.createDoubleBinding(() ->
//                        tileRotationObservable.getValue().quarterTurnsCW(), tileRotationObservable));
//
//                // Add mouse click event handlers
//                cellGroup.setOnMouseClicked(event -> {
//                    if (event.isSecondaryButtonDown()) {
//                        onRotateTile.accept(event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT);
//                    } else if (event.isPrimaryButtonDown()) {
//                        onPlaceTile.accept(pos);
//                    }
//                });
//
//                // Add event handlers for mouse hover to display tile placement preview
//                cellGroup.hoverProperty().addListener((observable, oldValue, newValue) -> {
//                    if (newValue) {
//                        ImageView previewImageView = createTilePreviewImageView(
//                                gameStateObservable.getValue().tileToPlace(), tileRotationObservable.getValue());
//                        cellGroup.getChildren().add(previewImageView);
//                    } else {
//                        cellGroup.getChildren().removeIf(node -> node instanceof ImageView && node != tileImageView);
//                    }
//                });
//
//                // Setup for placing occupants and cancellation tokens
//                setupOccupantsAndCancellationTokens(cellGroup, pos, visibleOccupantsObservable, gameStateObservable, onSelectOccupant);
//
//                boardGrid.add(cellGroup, x + scope, y + scope);
//            }
//        }
//
//        return boardScrollPane;
//    }
//
//    private static void setupOccupantsAndCancellationTokens(Group cellGroup, Pos pos,
//                                                            ObservableValue<Set<Occupant>> visibleOccupantsObservable,
//                                                            ObservableValue<GameState> gameStateObservable,
//                                                            Consumer<Occupant> onSelectOccupant) {
//        // Binding to add or remove occupants and cancellation tokens
//        visibleOccupantsObservable.addListener((obs, oldOccupants, newOccupants) -> {
//            // First, clear any existing occupants or tokens
//            cellGroup.getChildren().removeIf(node -> node.getId() != null && (node.getId().startsWith("pawn_") || node.getId().startsWith("marker_")));
//
//            // Now add back any current occupants
//            newOccupants.forEach(occupant -> {
//                SVGPath occupantPath = new SVGPath();
//                occupantPath.setId(STR."pawn_\{occupant.zoneId()}"); // Example ID format
//                occupantPath.setContent(Icon.newFor(gameStateObservable.getValue().currentPlayer(), occupant.kind())); // You need to define how to get the SVG path content for each occupant type
//                occupantPath.getStyleClass().add("occupant"); // Make sure this class is defined in your CSS
//
//                // Click event for placing an occupant
//                occupantPath.setOnMouseClicked(event -> onSelectOccupant.accept(occupant));
//
//                cellGroup.getChildren().add(occupantPath);
//            });
//
//            // Update cancellation tokens based on the game state
//            gameStateObservable.getValue().board().cancelledAnimals().forEach(animalId -> {
//                SVGPath cancellationPath = new SVGPath();
//                cancellationPath.setId("marker_" + animalId); // Example ID format
//                cancellationPath.setContent(getSvgPathContentForCancellationToken()); // Define the SVG path content for a cancellation token
//                cancellationPath.getStyleClass().add("cancellation-token"); // Make sure this class is defined in your CSS
//
//                // Add the cancellation token behind occupants
//                cellGroup.getChildren().add(0, cancellationPath); // Add at index 0 to be behind other elements
//            });
//        });
//    }
//
//
//    private static String getSvgPathContentForCancellationToken() {
//        // Return SVG path content for a cancellation token
//        // This is a placeholder, you will need to replace it with actual SVG path content
//        return "M5 5 L25 5 L25 25 L5 25 Z"; // This represents a larger square as an example
//    }
//
//
//    private static ImageView createTilePreviewImageView(Tile tile, Rotation rotation) {
//        ImageView imageView = new ImageView(ImageLoader.normalImageForTile(tile.id()));
//        imageView.setOpacity(0.5); // Make the preview semi-transparent
//        imageView.rotateProperty().bind();
//        return imageView;
//    }
//
//    private static WritableImage createEmptyTileImage() {
//        WritableImage emptyTileImage = new WritableImage(1, 1);
//        emptyTileImage.getPixelWriter().setColor(0, 0, Color.gray(0.98)); // Nearly white color
//        return emptyTileImage;
//    }
//}
