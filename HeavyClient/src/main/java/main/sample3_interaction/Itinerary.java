package main.sample3_interaction;

import com.soap.ws.client.generated.RootingServer;
import org.jxmapviewer.viewer.GeoPosition;

public class Itinerary {
    private GeoPosition start;
    private GeoPosition end;

    // fait un singleton
    private static Itinerary instance = null;

    private Itinerary() {
        this.start = null;
        this.end = null;
    }

    public static Itinerary getInstance() {
        if (instance == null) {
            instance = new Itinerary();
        }
        return instance;
    }

    public GeoPosition getEnd() {
        return end;
    }

    public GeoPosition getStart() {
        return start;
    }

    public void setEnd(GeoPosition end) {
        this.end = end;
    }

    public void setStart(GeoPosition start) {
        this.start = start;
    }

    public void askForItinerary() {
        RootingServer rootingServer = new RootingServer();
        rootingServer.getBasicHttpBindingIRootingServer().getItinerary(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
    }
}
