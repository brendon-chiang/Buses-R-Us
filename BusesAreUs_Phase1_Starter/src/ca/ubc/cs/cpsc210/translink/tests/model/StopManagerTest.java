package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the StopManager
 */
public class StopManagerTest {
    StopManager sM;

    @Before
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testGetStopWithNumber() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }

    @Test
    public void testGetStopWithNumberNameLocn() {
        Stop s10 = new Stop(10, "Mordor", new LatLon(-50.0, 120.0));
        Stop r = StopManager.getInstance().getStopWithNumber(10, "Mordor", new LatLon(-50.0,120.0));
        assertEquals(s10, r);
    }

    /*
    @Test
    public void testStop() throws StopException{
        Stop s10 = new Stop(10, "Mordor", new LatLon(-50.0, 120.0));
        sM.
        sM.setSelected(s10);
        assertEquals(s10, sM.getSelected());
        sM.clearSelectedStop();
    }

    @Test
    public void findNearestTo() {
        Stop s1 = new Stop(1, "Hobbiton", new LatLon(-1.0,1.0));
        Stop s2 = new Stop (2, "Mt. Doom", new LatLon(-100.0, 100.0));
        assertEquals(s1, sM.findNearestTo(new LatLon(-2.0,2.0)));
        assertEquals(s2, sM.findNearestTo(new LatLon(-99.0, 99.0)));
    }
*/
}
