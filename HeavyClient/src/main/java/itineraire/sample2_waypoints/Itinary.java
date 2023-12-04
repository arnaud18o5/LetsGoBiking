package itineraire.sample2_waypoints;

import com.fasterxml.jackson.databind.JsonNode;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.*;

public class Itinary {

    private List<GeoPosition> track = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();

    private GeoPosition start;
    private GeoPosition end;
    private String profile;

    public Itinary(JsonNode itinerary){
        this.profile = itinerary.get("metadata").get("query").get("profile").asText();
        this.start = new GeoPosition(itinerary.get("metadata").get("query").get("coordinates").get(0).get(1).asDouble(), itinerary.get("metadata").get("query").get("coordinates").get(0).get(0).asDouble());
        this.end = new GeoPosition(itinerary.get("metadata").get("query").get("coordinates").get(1).get(1).asDouble(), itinerary.get("metadata").get("query").get("coordinates").get(1).get(0).asDouble());
        for(JsonNode step : itinerary.get("features").get(0).get("geometry").get("coordinates")){
            track.add(new GeoPosition(step.get(1).asDouble(), step.get(0).asDouble()));
        }
        for(JsonNode step : itinerary.get("features").get(0).get("properties").get("segments").get(0).get("steps")){
            String instruction = step.get("instruction").asText();
            int[] waypoints = new int[2];
            waypoints[0] = step.get("way_points").get(0).asInt();
            waypoints[1] = step.get("way_points").get(1).asInt();
            steps.add(new Step(instruction, waypoints));
        }
    }

    public List<GeoPosition> getTrack(){
        return track;
    }

    public String getProfile(){
        return this.profile;
    }

    public GeoPosition getStart(){
        return this.start;
    }

    public GeoPosition getEnd(){
        return this.end;
    }

    public List<Step> getSteps(){
        return this.steps;
    }

    public void nextStep(Step step){
        for(int i = step.getWaypoints()[0]; i < step.getWaypoints()[1]; i++){
            this.track.set(i, null);
        }
        //this.steps.remove(step);

    }

    public Step getStepByInstruction(String instruction){
        for(Step step : this.steps){
            if(step.getInstruction().equals(instruction)){
                return step;
            }
        }
        return null;
    }

    public Step getNextStep(String instruction){
        for(Step step : this.steps){
            if(step.getInstruction().equals(instruction)){
                return this.steps.get(this.steps.indexOf(step) + 1);
            }
        }
        return null;
    }

    public Itinary removeFromTrack(Step step){
        for(int i = step.getWaypoints()[0]; i < step.getWaypoints()[1]; i++){
            this.track.remove(i);
        }
        this.steps.remove(step);
        return this;
    }

}
