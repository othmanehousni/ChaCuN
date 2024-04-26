package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.scene.image.Image;

public final class ImageLoader {

    public static final int LARGE_TILE_PIXEL_SIZE = 512;
    public static final int LARGE_TILE_FIT_SIZE = 256;
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;
    public static final int NORMAL_TILE_FIT_SIZE = 128;
    public static final int MARKER_PIXEL_SIZE = 96;
    public static final int  MARKER_FIT_SIZE = 48;


    private ImageLoader() {}

    public static Image normalImageForTile(int tileId) {
        String string = tileId < 9 ? "0" + tileId : "" + tileId;
        return new Image("/256/" + string + ".jpg", NORMAL_TILE_PIXEL_SIZE, NORMAL_TILE_PIXEL_SIZE, true, true);
    }

    public static Image largeImageForTile(int tileId) {
        String string = tileId < 9 ? "0" + tileId : "" + tileId;

        return new Image("/512/" + string + ".jpg", LARGE_TILE_PIXEL_SIZE, LARGE_TILE_PIXEL_SIZE, true, true);
    }
}

