package itineraire.sample2_waypoints;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * A simple sample application that shows
 * a OSM map of Europe containing a route with waypoints
 * @author Martin Steiger
 */
public class Sample2
{
    /**
     * @param args the program args (ignored)
     */


    static List<Itinary> itinaries = new ArrayList<>();
    static JXMapViewer mapViewer = new JXMapViewer();


    public static void main(String[] args) throws IOException {
        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 2");
        JButton button = new JButton("Next");
        JLabel label = new JLabel("texteee");
        Boolean over = false;
        AtomicInteger iStep = new AtomicInteger();
        AtomicInteger iItinary = new AtomicInteger();
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        String text = "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
        frame.add(button, BorderLayout.SOUTH);
        frame.add(label, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        //itinaries = getItinaries();
        updateData(mapViewer, itinaries);
        List<GeoPosition> track = new ArrayList<>();
        for (Itinary itinary : itinaries) {
            track.addAll(itinary.getTrack());
        }
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        button.addActionListener(e -> {
            if(iItinary.get() >= itinaries.size()){
                return;
            }
            if(iStep.get() >= itinaries.get(iItinary.get()).getSteps().size()-1){
                iItinary.getAndIncrement();
                iStep.set(0);
            }
            if(iItinary.get() >= itinaries.size()){
                return;
            }

            iStep.getAndIncrement();
            label.setText(itinaries.get(iItinary.get()).getSteps().get(iStep.get()).getInstruction());
            try {
                updateData(iStep.get(), iItinary.get());

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        /*
        e -> {
            /*if(!over.get()) {
                try {
                    label.setText(finalItinaries.get(itinaryIndex.get()).getSteps().get(stepIndex.get()).getInstruction());
                    stepIndex.getAndIncrement();
                    finalItinaries.get(itinaryIndex.get()).nextStep(finalItinaries.get(itinaryIndex.get()).getSteps().get(stepIndex.get() - 1));
                    if (stepIndex.get() == finalItinaries.get(itinaryIndex.get()).getSteps().size()) {
                        stepIndex.set(0);
                        finalItinaries.remove(itinaryIndex.get());
                        itinaryIndex.getAndIncrement();
                        if (itinaryIndex.get() == finalItinaries.size()) {
                            itinaryIndex.set(0);
                            over.set(true);
                        }

                        updateData(mapViewer, finalItinaries);
                    }
                    System.out.println();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }*/

    //}



    }

    public static void updateData(int iStep, int iItinerary) throws IOException {
        if(iItinerary == itinaries.size()){
            return;
        }
        for(int i = itinaries.get(iItinerary).getSteps().get(iStep-1).getWaypoints()[0]; i < itinaries.get(iItinerary).getSteps().get(iStep-1).getWaypoints()[1]; i++){
            itinaries.get(iItinerary).getTrack().remove(0);
        }
        updateData(mapViewer, itinaries);
    }

    /*public static void nextStep() throws IOException {
        String instruction  = label.getText();
        if(iItinary == itinaries.size()){
            over = true;
            return;
        }

        if(!over){
            if(itinaries.get(iItinary).getTrack().size() == 0) {
                iItinary++;
                iSegments = 0;
            }
            do{
                itinaries.get(iItinary).getTrack().remove(0);
                iSegments++;
            }while(itinaries.get(iItinary).getStepByInstruction(instruction).getWaypoints()[1] > iSegments);
            itinaries.get(iItinary).getSteps().remove(0);
            if (!itinaries.get(iItinary).getSteps().isEmpty())
                label.setText(itinaries.get(iItinary).getSteps().get(0).getInstruction());
        }

        updateData(mapViewer, itinaries);
    }*/

    public static List<Itinary> getItinaries(String response) throws IOException {
        // Créez un ObjectMapper (Jackson) pour lire le fichier JSON
        ObjectMapper objectMapper = new ObjectMapper();
        List<Itinary> itinaries = new ArrayList<>();
        // Utilisez l'ObjectMapper pour lire le fichier JSON en tant qu'objet JsonNode
        JsonNode jsonNode = objectMapper.readTree(new StringReader(response));
        for (JsonNode element : jsonNode) {
            JsonNode name = element.get("features").get(0).get("geometry").get("coordinates");
            itinaries.add(new Itinary(element));

        }
        return itinaries;
    }

    public static void updateData(JXMapViewer mapViewer, List<Itinary> itinaries) throws IOException {
        List<RoutePainter> routePainters = new ArrayList<RoutePainter>();


            // Exemple : Accédez à un champ spécifique dans le fichier JSON
            //JsonNode name = jsonNode.get(0).get("features" ).get(0).get("geometry").get("coordinates");
            List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
            Set<Waypoint> waypoints = new HashSet<Waypoint>();

            for (Itinary itinary : itinaries) {
                List<GeoPosition> track = itinary.getTrack();
/*
                if (track.isArray()) {
                    List<List<Double>> listOfArrays = new ArrayList<>();

                    // Parcourez les éléments du tableau
                    for (JsonNode e : name) {
                        List<Double> array = new ArrayList<>();

                        // Parcourez les éléments du tableau interne
                        for (JsonNode coordinate : e) {
                            array.add(coordinate.asDouble());
                        }

                        // Ajoutez le tableau à la liste
                        listOfArrays.add(array);
                    }

                    List<GeoPosition> track = new ArrayList<>();

                    for (List<Double> array : listOfArrays) {
                        track.add(new GeoPosition(array.get(1), array.get(0)));
                    }
*/

                    RoutePainter routePainter = new RoutePainter(track, itinary.getProfile());
                    // Set the focus
                    //mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

                    waypoints.add(new DefaultWaypoint(itinary.getStart()));
                    waypoints.add(new DefaultWaypoint(itinary.getEnd()));


                    // Create a waypoint painter that takes all the waypoints
                    //waypointPainter.setWaypoints(waypoints);

                    // Create a compound painter that uses both the route-painter and the waypoint-painter
                    painters.add(routePainter);
                    painters.add(waypointPainter);


                }

                waypointPainter.setWaypoints(waypoints);
                painters.add(waypointPainter);
                CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
                mapViewer.setOverlayPainter(painter);

            }



    }

