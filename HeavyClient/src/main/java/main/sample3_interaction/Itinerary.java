package main.sample3_interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soap.ws.client.generated.RootingServer;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.io.StringReader;

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

    public JsonNode askForItinerary() throws IOException {
        RootingServer rootingServer = new RootingServer();
        ObjectMapper objectMapper = new ObjectMapper();
        String response =  "";
        try{
            response = rootingServer.getBasicHttpBindingIRootingServer().getItinerary(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
        } catch(Exception e){
            return null;
        }
        System.out.println(response);
        JsonNode node = objectMapper.readTree(new StringReader(response));
        System.out.print(node);
        return node;
    }

    public JsonNode askForItinerary(String start, String end) throws IOException {
        RootingServer rootingServer = new RootingServer();
        ObjectMapper objectMapper = new ObjectMapper();
        String response =  "";
        try{
            response = rootingServer.getBasicHttpBindingIRootingServer().getItineraryViaNameLocation(start,  end);
        } catch(Exception e){
            return null;
        }
        System.out.println(response);
        JsonNode node = objectMapper.readTree(new StringReader(response));
        System.out.print(node);
        return node;
    }
}
