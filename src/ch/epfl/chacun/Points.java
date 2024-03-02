package ch.epfl.chacun;

/**
 * *
 *
 * @author Othmane HOUSNI (375072)
 * @author Hamza Zoubayri (361522)
 * <p>
 * Utility class for calculating points based on various game elements such as forests, rivers, meadows, and more.
 * This class cannot be instantiated.
 */

public final class Points {

    /**
     * Private constructor to prevent instantiation of utility class and messing with the point calculation.
     */
    private Points() {
    }


    /**
     * Calculates points for a closed forest.
     *
     * @param tileCount          the number of tiles in the forest
     * @param mushroomGroupCount the number of groups of mushrooms in the forest
     * @return the calculated points
     * @throws IllegalArgumentException if tileCount is less than or equal to 1 or mushroomGroupCount is negative
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(mushroomGroupCount >= 0);
        return 2 * tileCount + 3 * mushroomGroupCount;
    }

    /**
     * Calculates points for a closed river.
     *
     * @param tileCount the number of tiles in the river
     * @param fishCount the number of fish in the river
     * @return the calculated points
     * @throws IllegalArgumentException if tileCount is less than or equal to 1 or fishCount is negative
     */
    public static int forClosedRiver(int tileCount, int fishCount) {
        Preconditions.checkArgument(tileCount > 1);
        Preconditions.checkArgument(fishCount >= 0);
        return tileCount + fishCount;
    }

    /**
     * Calculates points for a meadow.
     *
     * @param mammothCount the number of mammoths in the meadow
     * @param aurochsCount the number of aurochs in the meadow
     * @param deerCount    the number of deer in the meadow
     * @return the calculated points
     * @throws IllegalArgumentException if any animal count is negative
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount) {
        Preconditions.checkArgument(aurochsCount >= 0);
        Preconditions.checkArgument(deerCount >= 0);
        Preconditions.checkArgument(mammothCount >= 0);
        return 3 * mammothCount + 2 * aurochsCount + deerCount;
    }

    /**
     * Calculates points for a river system.
     *
     * @param fishCount the number of fish in the river system
     * @return the calculated points
     * @throws IllegalArgumentException if fishCount is negative
     */
    public static int forRiverSystem(int fishCount) {
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Calculates points for having logboats.
     *
     * @param lakeCount the number of lakes connected by the logboats
     * @return the calculated points
     * @throws IllegalArgumentException if lakeCount is less than or equal to 0
     */
    public static int forLogboat(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return 2 * lakeCount;
    }

    /**
     * Calculates points for having rafts.
     *
     * @param lakeCount the number of lakes connected by the rafts
     * @return the calculated points
     * @throws IllegalArgumentException if lakeCount is less than or equal to 0
     */
    public static int forRaft(int lakeCount) {
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }
}
