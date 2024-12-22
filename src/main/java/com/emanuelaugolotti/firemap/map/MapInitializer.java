package com.emanuelaugolotti.firemap.map;

import com.emanuelaugolotti.firemap.rest.FireWaypoint;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import javafx.embed.swing.SwingNode;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

public class MapInitializer implements Runnable {       // il metodo run() di MapInitializer riempie lo swing node
    private final Set<FireWaypoint> fires;

    private final SwingNode swingNode;

    private final String tracestrackKey;

    private final JXMapViewer jxMapViewer;  // è un oggetto che permette di visualizzare la mappa e interagire con essa

    public MapInitializer(JXMapViewer jxMapViewer, Set<FireWaypoint> fires, SwingNode swingNode, String tracestrackKey) {
        this.jxMapViewer = jxMapViewer;
        this.fires = fires;
        this.swingNode = swingNode;
        this.tracestrackKey = tracestrackKey;
    }

    @Override
    public void run() {
        setTilesSource(jxMapViewer);
        setFocusPointAndZoom(jxMapViewer, 17);    // Set the focus map on center and set zoom when opening the map
        setInteractions(jxMapViewer);
        setWaypointPainter(jxMapViewer);
        configureWaypointInformationTable(jxMapViewer);
        swingNode.setContent(jxMapViewer);    // inserisco la componente swing dentro allo swing node
    }

    private void setTilesSource(JXMapViewer mapViewer) {        // metodo che configura le tiles della mappa
        //TileFactoryInfo tileFactoryInfo = new TracestrackTileFactoryInfo(tracestrackKey);
        TileFactoryInfo tileFactoryInfo = new ArcGisTileFactoryInfo();
        DefaultTileFactory defaultTileFactory = new DefaultTileFactory(tileFactoryInfo);
        mapViewer.setTileFactory(defaultTileFactory);
    }

    private void setFocusPointAndZoom(JXMapViewer mapViewer, int zoom) {
        GeoPosition center = new GeoPosition(11.154116472260842, 9.269048810007034);
        mapViewer.setAddressLocation(center);
        mapViewer.setZoom(zoom);
    }

    private void setInteractions(JXMapViewer mapViewer) {
        MouseInputListener mouseInputListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseInputListener);
        mapViewer.addMouseMotionListener(mouseInputListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
    }

    private void setWaypointPainter(JXMapViewer mapViewer) {
        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<FireWaypoint> painter = new WaypointPainter<>();
        painter.setRenderer(new FireWaypointRenderer());
        painter.setWaypoints(fires);
        mapViewer.setOverlayPainter(painter);
    }

    private void configureWaypointInformationTable(JXMapViewer mapViewer) {
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Point clickPoint = mouseEvent.getPoint();
                for (FireWaypoint waypoint : fires) {
                    Point2D waypointPoint = mapViewer.convertGeoPositionToPoint(waypoint.getPosition());
                    if (waypointPoint.distance(clickPoint) < 10) { // Adjust threshold as needed
                        showWaypointInformationTable(waypoint, mapViewer, mouseEvent);
                        break;
                    }
                }
            }
        });
    }

    private void showWaypointInformationTable(FireWaypoint waypoint, JXMapViewer mapViewer, MouseEvent mouseEvent) {
        String[][] rowData = {          //rowData in questo caso ha una riga e 7 colonne
                {
                        Double.toString(waypoint.getPosition().getLatitude()),
                        Double.toString(waypoint.getPosition().getLongitude()),
                        waypoint.getAcquisitionTime().toString(),
                        waypoint.getInstrument(),
                        waypoint.getConfidence(),
                        Double.toString(waypoint.getFrp()),
                        Character.toString(waypoint.getDayNight())
                }
        };

        String[] columnNames = {"LATITUDE", "LONGITUDE", "ACQUIRE TIME", "INSTRUMENT", "CONFIDENCE", "FRP", "DAYNIGHT"};
        JTable jTable = new JTable(rowData, columnNames);
        jTable.setEnabled(false); // Make the table non-editable
        jTable.getTableHeader().setReorderingAllowed(false);     // Disable column reordering
        jTable.setAutoCreateRowSorter(false);    // Disable sorting

        // Disable cell, row, and column selection
        jTable.setCellSelectionEnabled(false);
        jTable.setRowSelectionAllowed(false);
        jTable.setColumnSelectionAllowed(false);

        // Wrap the table in a JScrollPane
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jScrollPane.setPreferredSize(new Dimension(750, 55));

        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(jScrollPane);
        jPopupMenu.show(mapViewer, mouseEvent.getX(), mouseEvent.getY());
    }
}