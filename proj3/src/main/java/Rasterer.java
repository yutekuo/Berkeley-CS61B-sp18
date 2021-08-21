import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double ROOT_WIDTH = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
    private static final double ROOT_HEIGHT = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;

    public Rasterer() {
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        double queryBoxLonDPP = getQueryBoxLonDPP(params);
        int depth = getBestDepth(queryBoxLonDPP);
        double leftX = getLeftX(params, depth);
        double rightX = getRightX(params, depth);
        double upperY = getUpperY(params, depth);
        double lowerY = getLowerY(params, depth);
        String[][] pngFiles = getFilesArray(depth, leftX, rightX, upperY, lowerY);
        boolean isQueryBoxInBound = getQuerySuccess(params);

        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", pngFiles);
        results.put("raster_ul_lon", leftX);
        results.put("raster_ul_lat", upperY);
        results.put("raster_lr_lon", rightX);
        results.put("raster_lr_lat", lowerY);
        results.put("depth", depth);
        results.put("query_success", isQueryBoxInBound);
        return results;
    }

    /**
     * Returns true if the query box is inside the bound of the ROOT, false for
     * outside the ROOT or query box's ullon, ullat is located to the right of its lrlon, lrlat.
     */
    private boolean getQuerySuccess(Map<String, Double> params) {
        if (params.get("ullon") < MapServer.ROOT_ULLON ||
                params.get("ullon") >= MapServer.ROOT_LRLON ||
                params.get("lrlon") <= MapServer.ROOT_ULLON ||
                params.get("lrlon") > MapServer.ROOT_LRLAT ||
                params.get("ullat") > MapServer.ROOT_ULLAT ||
                params.get("ullat") <= MapServer.ROOT_LRLAT ||
                params.get("lrlat") >= MapServer.ROOT_ULLAT ||
                params.get("lrlat") < MapServer.ROOT_LRLAT ||
                params.get("ullon") >= params.get("lrlon") ||
                params.get("ullat") <= params.get("lrlat")) {
            return false;
        }
        return true;
    }

    /**
     * Returns a String[][] which is composed of specific png files that can cover the query box.
     */
    private String[][] getFilesArray(int depth, double leftX,
                                     double rightX, double upperY, double lowerY) {
        double tileWidth = ROOT_WIDTH / Math.pow(2, depth);
        double tileHeight = ROOT_HEIGHT / Math.pow(2, depth);
        int col = (int) ((rightX - leftX) / tileWidth);
        int row = (int) ((upperY - lowerY) / tileHeight);
        int fixedStartX = (int) ((leftX - MapServer.ROOT_ULLON) / tileWidth);
        int startY = (int) ((MapServer.ROOT_ULLAT - upperY) / tileHeight);
        String[][] files = new String[row][col];
        for (int r = 0; r < row; r++) {
            int startX = fixedStartX;
            for (int c = 0; c < col; c++) {
                files[r][c] = "d" + depth + "_x" + startX + "_y" + startY + ".png";
                startX++;
            }
            startY++;
        }
        return files;
    }

    /**
     * Returns the bottom y-position of the tiles that can cover the query box.
     */
    private double getLowerY(Map<String, Double> params, int depth) {
        double tileHeight = ROOT_HEIGHT / Math.pow(2, depth);
        double y = MapServer.ROOT_ULLAT;
        while (y > params.get("lrlat")) {
            y -= tileHeight;
        }
        return y;
    }

    /**
     * Returns the top y-position of the tiles that can cover the query box.
     */
    private double getUpperY(Map<String, Double> params, int depth) {
        double tileHeight = ROOT_HEIGHT / Math.pow(2, depth);
        double y = MapServer.ROOT_ULLAT;
        while (y >= params.get("ullat")) {
            y -= tileHeight;
        }
        return y + tileHeight;
    }

    /**
     * Returns the rightmost x-position of the tiles that can cover the query box.
     */
    private double getRightX(Map<String, Double> params, int depth) {
        double tileWidth = ROOT_WIDTH / Math.pow(2, depth);
        double x = MapServer.ROOT_ULLON;
        while (x < params.get("lrlon")) {
            x += tileWidth;
        }
        return x;
    }

    /**
     * Returns the leftmost x-position of the tiles that can cover the query box.
     */
    private double getLeftX(Map<String, Double> params, int depth) {
        double tileWidth = ROOT_WIDTH / Math.pow(2, depth);
        double x = MapServer.ROOT_ULLON;
        while (x <= params.get("ullon")) {
            x += tileWidth;
        }
        return x - tileWidth;
    }

    /**
     * Returns the LonDPP o the query box.
     */
    private double getQueryBoxLonDPP(Map<String, Double> params) {
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double width = params.get("w");
        return (lrlon - ullon) / width;
    }

    /**
     * Returns the best depth of the tile that has the greatest LonDPP that
     * is less than or equal to the LonDPP of the query box.
     */
    private int getBestDepth(double qbLonDPP) {
        double d0LonDPP = ROOT_WIDTH / MapServer.TILE_SIZE;
        int depth = 0;
        while (d0LonDPP > qbLonDPP) {
            d0LonDPP /= 2;
            depth++;
        }
        return depth;
    }
}
