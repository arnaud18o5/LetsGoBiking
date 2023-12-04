package itineraire.sample2_waypoints;

public class Step {
    String instruction;
    int[] waypoints;

    public Step(String instruction, int[] waypoints){
        this.instruction = instruction;
        this.waypoints = waypoints;
    }

    public String getInstruction(){
        return this.instruction;
    }

    public int[] getWaypoints(){
        return this.waypoints;
    }

}
