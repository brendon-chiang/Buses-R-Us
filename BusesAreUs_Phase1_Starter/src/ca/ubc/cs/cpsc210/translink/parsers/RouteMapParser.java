package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String routeNumber = null;
    private String patternName = null;


    private String fileName;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    private List<LatLon> elements = new ArrayList<>();

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     *
     * Each line begins with a capital N, which is not part of the route number, followed by the
     * bus route number, a dash, the pattern name, a semicolon, and a series of 0 or more real
     * numbers corresponding to the latitude and longitude (in that order) of a point in the pattern,
     * separated by semicolons. The 'N' that marks the beginning of the line is not part of the bus
     * route number.
     * NC43-EB2;49.21716;-122.667252;49.216757;-122.666235;49.216749;-122.666051;49.216821;-122.665898;49.216904;
     */
    private void parseOnePattern(String str) {
        String[] parts = str.split("-", 2);
                String firstHalf = parts[0];
                String secondHalf = parts[1];
                this.routeNumber = firstHalf.substring(firstHalf.indexOf('N')+1);

                String[] section = secondHalf.split(";");
                this.patternName = section [0];

                for (int i = 1; i < section.length; i = i +2) {
                    String latString = section[i];
                    String lonString = section[i + 1];
                    double lat = Double.parseDouble(latString);
                    double lon = Double.parseDouble(lonString);
                    elements.add(new LatLon(lat, lon));
                }
                storeRouteMap(routeNumber, patternName, elements);
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
