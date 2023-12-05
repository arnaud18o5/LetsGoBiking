
package main.sample3_interaction;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itineraire.sample2_waypoints.Itinary;
import itineraire.sample2_waypoints.RoutePainter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

/**
 * Creates a selection rectangle based on mouse input
 * Also triggers repaint events in the viewer
 * @author Martin Steiger
 */
public class SelectionAdapter extends MouseAdapter 
{

    private static Itinerary itinerary;
    private boolean dragging;
    private JXMapViewer viewer;

    private static int iItinerary = 0;

    private static int iSteps = 0;
    private Point2D startPos = new Point2D.Double();
    private Point2D endPos = new Point2D.Double();

    private static Set<Waypoint> waypoints = new HashSet<Waypoint>();

    private static List<Itinary> itinaries = new ArrayList<>();

    /**
     * @param viewer the jxmapviewer
     */
    public SelectionAdapter(JXMapViewer viewer)
    {
        this.viewer = viewer;
        this.itinerary = Itinerary.getInstance();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.getButton() != MouseEvent.BUTTON3)
            return;

        startPos.setLocation(e.getX(), e.getY());
        endPos.setLocation(e.getX(), e.getY());

        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (!dragging)
            return;

        endPos.setLocation(e.getX(), e.getY());

        viewer.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (!dragging)
            return;

        if (e.getButton() != MouseEvent.BUTTON3)
            return;

        viewer.repaint();

        dragging = false;
    }

    /**
     * @return the selection rectangle
     */
    public Rectangle getRectangle()
    {
        if (dragging)
        {
            int x1 = (int) Math.min(startPos.getX(), endPos.getX());
            int y1 = (int) Math.min(startPos.getY(), endPos.getY());
            int x2 = (int) Math.max(startPos.getX(), endPos.getX());
            int y2 = (int) Math.max(startPos.getY(), endPos.getY());

            return new Rectangle(x1, y1, x2-x1, y2-y1);
        }

        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Convertir les coordonnées du clic en GeoPosition
        GeoPosition clickedPosition = viewer.convertPointToGeoPosition(e.getPoint());
        if(waypoints.size() == 0) {
            itinerary.setStart(clickedPosition);
        }
        if (waypoints.size() == 1){
            itinerary.setEnd(clickedPosition);
        }
        if(waypoints.size() == 2) {
            itinerary.setStart(clickedPosition);
            waypoints.clear();
        }
        // Ajouter un waypoint à la position du clic
        waypoints.add(new DefaultWaypoint(clickedPosition));

        // Mettre à jour les marqueurs sur la carte
        updateMapMarkers(viewer, waypoints);
        /*if(waypoints.size() == 2) {
            try {
                JsonNode jsonNode = itinerary.askForItinerary();
                if(jsonNode == null)
                    return;
                List<Itinary> itinaries = getItinaries(jsonNode.toString());

                updateData(viewer, itinaries);
                List<GeoPosition> track = new ArrayList<>();
                for (Itinary itinary : itinaries) {
                    track.addAll(itinary.getTrack());
                }
                viewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }*/
    }


    public static String searchItinerary(JXMapViewer mapViewer){
        if(waypoints.size() == 2) {
            try {
                JsonNode jsonNode = itinerary.askForItinerary();
                if(jsonNode == null)
                    return "Impossible itinerary ! Choose other points.";
                List<Itinary> itinaries = getItinaries(jsonNode.toString());

                updateData(mapViewer, itinaries);
                List<GeoPosition> track = new ArrayList<>();
                for (Itinary itinary : itinaries) {
                    track.addAll(itinary.getTrack());
                }
                mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);
                return itinaries.get(0).getSteps().get(0).getInstruction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            return "You need a start location and an end location to search an itinerary.";
        }
    }

    private static void updateMapMarkers(JXMapViewer mapViewer, Set <? extends Waypoint> waypoints) {
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
    }



    public static java.util.List<Itinary> getItinaries(String response) throws IOException {
        // Créez un ObjectMapper (Jackson) pour lire le fichier JSON
        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        java.util.List<Itinary> iti = new ArrayList<>();
        // Utilisez l'ObjectMapper pour lire le fichier JSON en tant qu'objet JsonNode
        JsonNode jsonNode = objectMapper.readTree(new StringReader(response));
        for (JsonNode element : jsonNode) {
            JsonNode name = element.get("features").get(0).get("geometry").get("coordinates");
            iti.add(new Itinary(element));

        }
        itinaries = iti;
        return iti;
    }

    public static void updateData(JXMapViewer mapViewer, java.util.List<Itinary> itinaries) throws IOException {
        java.util.List<RoutePainter> routePainters = new ArrayList<RoutePainter>();


        // Exemple : Accédez à un champ spécifique dans le fichier JSON
        //JsonNode name = jsonNode.get(0).get("features" ).get(0).get("geometry").get("coordinates");
        java.util.List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        Set<Waypoint> waypoints = new HashSet<Waypoint>();

        for (Itinary itinary : itinaries) {
            List<GeoPosition> track = itinary.getTrack();



            RoutePainter routePainter = new RoutePainter(track, itinary.getProfile());
            // Set the focus
            //mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

            waypoints.add(new DefaultWaypoint(itinary.getStart()));
            waypoints.add(new DefaultWaypoint(itinary.getEnd()));


            // Create a waypoint painter that takes all the waypoints
            waypointPainter.setWaypoints(waypoints);

            // Create a compound painter that uses both the route-painter and the waypoint-painter
            painters.add(routePainter);
            painters.add(waypointPainter);


        }

        waypointPainter.setWaypoints(waypoints);
        painters.add(waypointPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);

    }

    public static String updateData(JXMapViewer mapViewer) throws IOException {
        if (iItinerary >= itinaries.size()) {
            return "You are arrived!";
        }
        if (iSteps >= itinaries.get(iItinerary).getSteps().size() - 1) {
            iItinerary++;
            iSteps = 0;
        }
        if (iItinerary >= itinaries.size()) {
            iItinerary = 0;
            iSteps=0;
            return "You are arrived!";
        }

        iSteps++;
        for (int i = itinaries.get(iItinerary).getSteps().get(iSteps - 1).getWaypoints()[0]; i < itinaries.get(iItinerary).getSteps().get(iSteps - 1).getWaypoints()[1]; i++) {
            itinaries.get(iItinerary).getTrack().remove(0);
        }
        updateData(mapViewer, itinaries);
        return itinaries.get(iItinerary).getSteps().get(iSteps).getInstruction();
    }
}
