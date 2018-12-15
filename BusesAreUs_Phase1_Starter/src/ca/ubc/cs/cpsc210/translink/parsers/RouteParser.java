package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    private String routeName;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no routes should be added to the route manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {

        JSONArray routes = new JSONArray(jsonResponse);
        RouteManager routeManager = RouteManager.getInstance();
        Boolean exceptionError = false;


        for (int i = 0; i < routes.length(); i++) {
            JSONObject currentRoute = routes.getJSONObject(i);
            if (!currentRoute.has("RouteNo") || !currentRoute.has("Name") || !currentRoute.has("Patterns")) {
                exceptionError = true;
            }
            else {
                Route createRoute = routeManager.getRouteWithNumber(currentRoute.getString("RouteNo"));
                createRoute.setName(currentRoute.getString("Name"));
                JSONArray listRouteElements = currentRoute.getJSONArray("Patterns");
                routeName = currentRoute.getString("Name");
                parseRoutePattern(listRouteElements, createRoute);
            }
        }
        if (exceptionError)
            throw new RouteDataMissingException();
    }

    public void parseRoutePattern(JSONArray listRouteElements, Route route)
            throws JSONException, RouteDataMissingException {

        Boolean exceptionError = false;


        for (int i = 0; i < listRouteElements.length(); i++) {
            JSONObject currentRoute =  listRouteElements.getJSONObject(i);
            if (!currentRoute.has("PatternNo") || !currentRoute.has("Destination")|| !currentRoute.has("Direction")) {
                exceptionError = true;
            }
            else {
                RoutePattern thePattern = new RoutePattern(routeName,
                        currentRoute.getString("Destination"),
                        currentRoute.getString("Direction"),
                        route);
                route.addPattern(thePattern);
            }
        }
        if (exceptionError) {
            throw new RouteDataMissingException();
        }
    }
}



