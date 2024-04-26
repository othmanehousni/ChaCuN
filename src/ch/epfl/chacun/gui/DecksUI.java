package ch.epfl.chacun.gui;

import ch.epfl.chacun.Tile;
import ch.epfl.chacun.Occupant;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.function.Consumer;
import ch.epfl.chacun.gui.ColorMap.*;

public final class DecksUI {

    private DecksUI() {
    }

    public static Node create(
            ObservableValue<Tile> nextTileObservable,
            ObservableValue<Integer> normalTilesCountObservable,
            ObservableValue<Integer> menhirTilesCountObservable,
            ObservableValue<String> nextTileTextObservable,
            Consumer<Occupant> onSkipOccupant) {

        HBox decks = new HBox();
        VBox root = new VBox();
        root.getStylesheets().add("decks.css");
        decks.setId("decks");

        StackPane nextTileStack = createTileToPlace(nextTileObservable, nextTileTextObservable, onSkipOccupant);

        StackPane normalDeckStack = createDeckStack("NORMAL", normalTilesCountObservable);
        StackPane menhirDeckStack = createDeckStack("MENHIR", menhirTilesCountObservable);

        decks.getChildren().addAll(normalDeckStack, menhirDeckStack);
        root.getChildren().addAll(nextTileStack, decks);

        return root;
    }

    private static StackPane createDeckStack(String label, ObservableValue<Integer> tilesCountObservable) {
        ImageView deckView = new ImageView();

        deckView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        deckView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        deckView.setImage(new Image(STR."512/\{label}.jpg"));

        Text deckText = new Text();
        deckText.textProperty().bind(Bindings.convert(tilesCountObservable));
        deckText.visibleProperty().bind(tilesCountObservable.map(count -> count > 0));

        StackPane deckStack = new StackPane(deckView, deckText);
        deckStack.setId(label);

        return deckStack;
    }

    private static StackPane createTileToPlace (ObservableValue<Tile> tileObservableValue,ObservableValue<String> nextTileTextObservable, Consumer<Occupant> onSkipOccupant ) {
        StackPane nextTileStack = new StackPane();
        nextTileStack.setId("next-tile");

        ImageView nextTileView = new ImageView();
        Text nextTileText = new Text();

        nextTileView.imageProperty().bind(tileObservableValue.map(tile -> ImageLoader.largeImageForTile(tile.id())));
        nextTileView.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        nextTileView.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);

        nextTileText.setWrappingWidth(0.8);
        //nextTileText.wrappingWidthProperty().bind(nextTileView.fitWidthProperty().multiply(0.8));
        nextTileText.textProperty().bind(nextTileTextObservable);
        nextTileText.visibleProperty().bind(nextTileTextObservable.map(text -> !text.isEmpty()));

        nextTileText.setOnMouseClicked(e -> onSkipOccupant.accept(null));

        nextTileStack.getChildren().addAll(nextTileView, nextTileText);

        return nextTileStack;
    }

}
