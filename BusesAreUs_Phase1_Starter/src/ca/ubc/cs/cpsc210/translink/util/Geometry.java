package ca.ubc.cs.cpsc210.translink.util;

import java.awt.geom.Line2D;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        Double maxUp = northWest.getLatitude();
        Double maxLeft = northWest.getLongitude();
        Double maxDown = southEast.getLatitude();
        Double maxRight = southEast.getLongitude();
        Double lat = point.getLatitude();
        Double lon = point.getLongitude();
        if ((maxDown <= lat) && (lat <= maxUp) && (maxLeft <= lon) && (lon <= maxRight)) {
            return true;
        }
        return false;
    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        Double maxUp = northWest.getLatitude();
        Double maxLeft = northWest.getLongitude();
        Double maxDown = southEast.getLatitude();
        Double maxRight = southEast.getLongitude();
        Double lineTop = src.getLatitude();
        Double lineLeft = src.getLongitude();
        Double lineDown = dst.getLatitude();
        Double lineRight = dst.getLongitude();
        Line2D line = new Line2D.Double(lineLeft, lineTop, lineRight, lineDown);
        return (line.intersects(maxLeft, maxUp, maxRight, maxDown) || (rectangleContainsPoint(northWest, southEast, src))
                || (rectangleContainsPoint(northWest, southEast, dst)));
    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
