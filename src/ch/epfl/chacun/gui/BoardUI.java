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

/**
 * The BoardUI class is a public, non-instantiable class that contains the code
 * for creating the part of the graphical user interface that displays the game board.
 * @author Othmane HOUSNI (375072)
 * @author Hamza ZOUBAYRI (361522)
 */
 public final class BoardUI{

     private static final double PERCENTAGE_50 = 0.5;
    /**
     * Private constructor to prevent instantiation.
     */
    private BoardUI() {
    }

    /**
     * Represents the data for a cell in the board.
     */
    private record CellData(Image backgroundImage, int rotationDegrees, Color overlayColor) {

        static final Map<Integer, Image> CACHE = new HashMap<>();

        static final WritableImage EMPTY_IMAGE = createEmptyTileImage();

        /**
         * Creates a CellData instance for a placed tile with a given overlay color.
         *
         * @param placedTile the placed tile
         * @param color the overlay color
         */
        CellData(PlacedTile placedTile, Color color){
            this(CACHE.computeIfAbsent(placedTile.tile().id(),
                    ImageLoader::normalImageForTile),
                    placedTile.rotation().degreesCW(), color);
        }

        /**
         * Creates a CellData instance for an empty cell with a given overlay color.
         *
         * @param color the overlay color
         */
        CellData(Color color){
            this(EMPTY_IMAGE, 0, color);
        }

        /**
         * Creates an empty tile image.
         *
         * @return the empty tile image
         */
        static WritableImage createEmptyTileImage() {
            WritableImage emptyTileImage = new WritableImage(1, 1);
            emptyTileImage.getPixelWriter().setColor(0, 0, Color.gray(0.98));
            return emptyTileImage;
        }
    }

    /**
     * Creates a list of visible markers for a placed tile.
     *
     * @param placedTile the placed tile
     * @param cancelledAnimals the observable set of cancelled animals
     * @return the list of visible markers
     */
   private static List<Node> visibleMarkers(PlacedTile placedTile,
                                            ObservableValue<Set<Animal>> cancelledAnimals) {

        List<Node> nodeList = new ArrayList<>();
        // the markers are the animals that are on the meadow zones and that are cancelled
        placedTile.meadowZones()
                .stream()
                .flatMap(meadow -> meadow.animals().stream())
                .forEach(animal -> {
                    // creating the icon for each animal that is cancelled in the meadow
                    ImageView image = new ImageView();
                    image.setFitWidth(ImageLoader.MARKER_FIT_SIZE);
                    image.setFitHeight(ImageLoader.MARKER_FIT_SIZE);
                    image.setId(STR."marker_\{animal.id()}");
                    image.getStyleClass().add("marker");
                     image.visibleProperty().bind(cancelledAnimals.map(cancelledA -> cancelledA.contains(animal)));
                    nodeList.add(image);
        });
        return nodeList;
    }

    /**
     * Creates a list of visible occupants for a placed tile.
     *
     * @param placedTile the placed tile
     * @param visibleOccupantsO the observable set of visible occupants
     * @param onSelectOccupant the event handler for selecting an occupant
     * @return the list of visible occupants
     */
    private static List<Node> visibleOccupants(PlacedTile placedTile,
                                               ObservableValue<Set<Occupant>> visibleOccupantsO,
                                               Consumer<Occupant> onSelectOccupant) {

        List<Node> nodeList = new ArrayList<>();
        //looping through all the potential occupants
        for (Occupant occupant : placedTile.potentialOccupants()) {
            //creating the icon for the occupant
            Node node = Icon.newFor(placedTile.placer(), occupant.kind());
            node.setId(STR."\{occupant.kind().name().toLowerCase()}_\{occupant.zoneId()}");
            //keeping it from following the tile's rotation, this keeping it straight
            node.setRotate(placedTile.rotation().negated().degreesCW());
            // its visibility is based on the visible occupants given by the main
            node.visibleProperty().bind(visibleOccupantsO.map(occupants -> occupants.contains(occupant)));
            node.setOnMouseClicked(_ -> onSelectOccupant.accept(occupant));
            nodeList.add(node);
        }
        return nodeList;
    }

    /**
     * Creates the root JavaFX node of the scene graph that displays the game board.
     *
     * @param scope the scope of the board
     * @param gameStateO the observable game state
     * @param tileRotationO the observable rotation to apply to the tile to place
     * @param visibleOccupantsO the observable set of visible occupants
     * @param highlightedTilesO the observable set of highlighted tile IDs
     * @param onRotateTile the event handler for rotating the tile to place
     * @param onPlaceTile the event handler for placing the tile
     * @param onSelectOccupant the event handler for selecting an occupant
     * @return the root node of the scene graph
     */
    public static Node create(
            int scope,
            ObservableValue<GameState> gameStateO,
            ObservableValue<Rotation> tileRotationO,
            ObservableValue<Set<Occupant>> visibleOccupantsO,
            ObservableValue<Set<Integer>> highlightedTilesO,
            Consumer<Rotation> onRotateTile,
            Consumer<Pos> onPlaceTile,
            Consumer<Occupant> onSelectOccupant) {

        //creating the scroll-pane, centered, where you can scroll
        ScrollPane boardScrollPane = new ScrollPane();
        boardScrollPane.setId("board-scroll-pane");
        boardScrollPane.getStylesheets().add("board.css");
        boardScrollPane.setVvalue(PERCENTAGE_50);
        boardScrollPane.setHvalue(PERCENTAGE_50);

        //creating the board-grip where the tiles will be put according to their position x and y
        GridPane boardGrid = new GridPane();
        boardGrid.setId("board-grid");
        boardScrollPane.setContent(boardGrid);

        //looping through the (x,y) coordinates that will be used to access each tile
        for (int x = -scope; x <= scope; x++) {
            for (int y = -scope; y <= scope; y++) {

                //creation of all the variables used
                Group cellGroup = new Group();
                ImageView tileImageView = new ImageView();
                tileImageView.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                tileImageView.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                cellGroup.getChildren().add(tileImageView);

                //adding the position to the grid, translated by the scope to keep it positive
                boardGrid.add(cellGroup, x + scope, y + scope);

                Pos currentPos = new Pos(x, y); //current aimed position

                // all the observable values
                ObservableValue<Board> boardO = gameStateO.map(GameState::board);
                ObservableValue<Set<Pos>> fringeO = boardO.map(Board::insertionPositions);
                ObservableValue<PlacedTile> placedTileO = boardO.map(board -> board.tileAt(currentPos));
                ObservableValue<Integer> placedTileIdO = placedTileO.map(placedTile ->
                        placedTile == null ? null : placedTile.tile().id());
                ObservableValue<Set<Animal>> cancelledAnimalsO = boardO.map(Board::cancelledAnimals);
                ObservableValue<Tile> tileToPlaceO = gameStateO.map(GameState::tileToPlace);
                ObservableValue<PlayerColor> currentPlayerO = gameStateO.map(GameState::currentPlayer);

                //creating the overlay color that will be used to cover the tiles
                ColorInput colorInput = new ColorInput(0, 0, ImageLoader.NORMAL_TILE_FIT_SIZE,
                        ImageLoader.NORMAL_TILE_FIT_SIZE, Color.TRANSPARENT);

                Blend blend = new Blend(BlendMode.SRC_OVER, null, colorInput);
                blend.setOpacity(PERCENTAGE_50);

                // At each placement, the nodes must be updated accordingly.
                // Potential occupants must be shown and markers too.
                placedTileO.addListener((_, old, newTile) -> {
                    List<Node> currentVisibleNodes = new ArrayList<>();
                    if(old == null && newTile != null) {
                        currentVisibleNodes.addAll(visibleMarkers(newTile, cancelledAnimalsO));
                        currentVisibleNodes.addAll(visibleOccupants(newTile, visibleOccupantsO, onSelectOccupant));
                        cellGroup.getChildren().addAll(currentVisibleNodes);
                    }
                });

                // The fringe represents the positions where the player could place his tile in the 4 directions.
                BooleanBinding fringeBinding = Bindings.createBooleanBinding(() -> fringeO.getValue().contains(currentPos)
                        && tileToPlaceO.getValue() != null, fringeO, placedTileO, gameStateO);

                // The cellData holds the data for each cell.
                // We're saving each data into the biding in order to use it later.
                ObjectBinding<CellData> cellDataBinding = Bindings.createObjectBinding(() -> {
                    // if the player does place a tile
                    if (placedTileO.getValue() != null) {
                        Color color = highlightedTilesO.getValue().isEmpty() || //checking if there is indeed tiles to highlight
                                // message highlighted, is related to the tile isn't highlighted
                                highlightedTilesO.getValue().contains(placedTileIdO.getValue()) ? null
                                // message highlighted, black put on the tiles that aren't concerned
                                : Color.BLACK;
                        return new CellData(placedTileO.getValue(), color);

                        // if the player hasn't placed a tile and is hovering over the insertion positions
                    } else if (fringeBinding.getValue()) {
                        if (cellGroup.hoverProperty().getValue()) {

                            // the player can still place a tile
                            if (tileToPlaceO.getValue() != null) {
                                PlacedTile placedTile = new PlacedTile(tileToPlaceO.getValue(),
                                        currentPlayerO.getValue(), tileRotationO.getValue(), currentPos);

                                // if the player can place the tile, the overlay color is null, else it's white
                                Color color = boardO.getValue().canAddTile(placedTile) ? null : Color.WHITE;
                                return new CellData(placedTile, color);
                            }
                            // if the player isn't hovering over the insertion positions,
                            // the overlay color is covered by the player's color
                        } else {
                            PlayerColor player = currentPlayerO.getValue();
                            if (player != null) {
                                return new CellData(ColorMap.fillColor(player));
                            }
                        }
                    }
                    return new CellData(null);
                }, gameStateO, fringeBinding, placedTileO, highlightedTilesO, tileRotationO,
                        cellGroup.hoverProperty(), visibleOccupantsO, boardO);


                // binding the effect property of the group to the overlay color and the blend
                cellGroup.effectProperty().bind(cellDataBinding.map(cellData -> {
                    colorInput.setPaint(cellData.overlayColor());
                    if (cellData.overlayColor() == null) {
                        return null;
                    } else {
                        return blend;
                    }
                }));

                // binding the image and its rotation based on the cellData content
                tileImageView.imageProperty().bind(cellDataBinding.map(CellData::backgroundImage));
                cellGroup.rotateProperty().bind(cellDataBinding.map(CellData::rotationDegrees));

                // handling the mouse events, left click : place the tile, right click : rotate the tile
                cellGroup.setOnMouseClicked(event -> {
                    if(event.isStillSincePress()) {
                        // if the player left clicks
                        if (event.getButton().equals(MouseButton.PRIMARY)) {

                            //checking if the player can place the tile :
                            // is in the insertion positions, the tile isn't placed, the tile to place isn't null
                            if (placedTileO.getValue() == null && fringeBinding.getValue() && tileToPlaceO.getValue() != null) {
                                PlacedTile placedTileToPlace = new PlacedTile(tileToPlaceO.getValue(),
                                        gameStateO.getValue().currentPlayer(),
                                        tileRotationO.getValue(), currentPos);

                                // if the game permits the player adding the tile
                                if (boardO.getValue().canAddTile(placedTileToPlace)) {
                                    onPlaceTile.accept(currentPos);
                                }
                            }
                            // if the player right clicks
                        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                            //rotate left of right if the player is holding the alt (option on Mac) key
                            Rotation rotation = event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                            onRotateTile.accept(rotation);
                        }
                    }
                });
            }
        }
        return boardScrollPane;
    }

}


