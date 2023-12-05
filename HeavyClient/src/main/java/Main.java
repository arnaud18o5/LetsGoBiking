import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.criteria.Selection;
import javax.swing.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itineraire.sample2_waypoints.Itinary;
import itineraire.sample2_waypoints.RoutePainter;
import main.sample3_interaction.SelectionAdapter;
import main.sample3_interaction.SelectionPainter;
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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;


public class Main {

    static List<Itinary> itinaries = new ArrayList<>();
    static JXMapViewer mapViewer = new JXMapViewer();



    public static void main(String[] args) throws IOException {
        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 2");
        JButton button = new JButton("Next");
        JLabel label = new JLabel("Select a starting point and an ending point by clicking on the map.");
        Boolean over = false;
        AtomicInteger iStep = new AtomicInteger();
        AtomicInteger iItinary = new AtomicInteger();
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);

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

        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);
        //mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);

        button.addActionListener(e -> {

            try {
                label.setText(SelectionAdapter.updateData(mapViewer));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateWindowTitle(frame, mapViewer);
            }
        });

        mapViewer.addPropertyChangeListener("center", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateWindowTitle(frame, mapViewer);
            }
        });

        updateWindowTitle(frame, mapViewer);


    }

    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer)
    {
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();

        frame.setTitle(String.format("JXMapviewer2 Example 3 (%.2f / %.2f) - Zoom: %d", lat, lon, zoom));
    }



    public static void updateData(JXMapViewer mapViewer, List<Itinary> itinaries) throws IOException {
        List<RoutePainter> routePainters = new ArrayList<RoutePainter>();


        // Exemple : Accédez à un champ spécifique dans le fichier JSON
        //JsonNode name = jsonNode.get(0).get("features" ).get(0).get("geometry").get("coordinates");
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
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