package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {


    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when:
     *                                      <ul>
     *                                      <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                                      <li>JSON response is not an array</li>
     *                                      </ul>
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {

        JSONArray arrivals = new JSONArray(jsonResponse);
        Route route;

        for (int i = 0; i < arrivals.length(); i++) {
            JSONObject currentStop = arrivals.getJSONObject(i);
            route = RouteManager.getInstance().getRouteWithNumber(currentStop.getString("RouteNo"));
            JSONArray listArrivalElements = currentStop.getJSONArray("Schedules");

            for (int l = 0; l < listArrivalElements.length(); l++) {
                JSONObject listSpecificElements = listArrivalElements.getJSONObject(l);
                if (listSpecificElements.has("ExpectedCountdown") && listSpecificElements.has("Destination") && listSpecificElements.has("ScheduleStatus")) {
                    Arrival arrival = new Arrival(listSpecificElements.getInt("ExpectedCountdown"), listSpecificElements.getString("Destination"),
                            route);
                    stop.addArrival(arrival);
                }
            }
        }
        if (arrivals.length() == 0)
            throw new ArrivalsDataMissingException();
    }
}

