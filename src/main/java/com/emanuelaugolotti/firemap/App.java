package com.emanuelaugolotti.firemap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.embed.swing.SwingNode;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class App extends Application {

    static String getData() {
        try (HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()) {
            URI uri = new URI("https://firms.modaps.eosdis.nasa.gov/api/area/csv/5b19bedf728592a1122d5fee8386e778/VIIRS_SNPP_NRT/world/2");
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.statusCode());
            //System.out.println(response.body());
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            return "";
        }
    }

//    static List<Fire> parse(String string) {
//        List<Fire> fires = new ArrayList<>();
//        String[] arrayString = string.split("\n");
//        for (int i = 1; i < arrayString.length; i++) {
//            String[] tmpArrayString = arrayString[i].split(",");
//            Fire fire = new Fire(Double.parseDouble(tmpArrayString[0]), Double.parseDouble(tmpArrayString[1]));
//            fires.add(fire);
//        }
//        return fires;
//    }

    static Set<FireWaypoint> parse(String string) {
        Set<FireWaypoint> fires = new HashSet<>();
        String[] arrayString = string.split("\n");
        for (int i = 1; i < arrayString.length; i++) {
            String[] tmpArrayString = arrayString[i].split(",");
            double latitude = Double.parseDouble(tmpArrayString[0]);
            double longitude = Double.parseDouble(tmpArrayString[1]);
            String instrument = tmpArrayString[8];
            String confidence = tmpArrayString[9];
            double frp = Double.parseDouble(tmpArrayString[12]);
            char daynight = tmpArrayString[13].charAt(0);

            LocalDate date = LocalDate.parse(tmpArrayString[5]);
            String timeTmp = tmpArrayString[6];

            //funzione per trasformare l'ora nel formato giusto, ritorna un LocalDateTime
            int numberOfZero = 4 - timeTmp.length();
            StringBuilder sb = new StringBuilder();
            sb.repeat("0", numberOfZero).append(timeTmp).insert(2, ":");
            LocalTime time = LocalTime.parse(sb);
            LocalDateTime acqusitionTime = LocalDateTime.of(date, time);

            FireWaypoint fire = new FireWaypoint(latitude, longitude, acqusitionTime, frp, daynight, instrument, confidence);
            fires.add(fire);
        }
        return fires;
    }


    @Override
    public void start(Stage stage) throws Exception {

        String data = getData();
        //List<Fire> listFires = parse(data);
        Set<FireWaypoint> fires = parse(data);


//        //creazione di un set di DefaultWaypoint
//        Set<Waypoint> waypoints = new HashSet<>();
//        for (Fire fire : listFires) {
//            double latitude = fire.getLatitude();
//            double longitude = fire.getLongitude();
//            waypoints.add(new DefaultWaypoint(latitude, longitude));
//        }


        final SwingNode swingNode = new SwingNode();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                JXMapViewer mapViewer = new JXMapViewer();

                // Create a TileFactoryInfo for OpenStreetMap
                //TileFactoryInfo info = new OSMTileFactoryInfo();
                TileFactoryInfo info = new TracestrackTileFactoryInfo();
                DefaultTileFactory tileFactory = new DefaultTileFactory(info);
                mapViewer.setTileFactory(tileFactory);

                // Use 8 threads in parallel to load the tiles
                tileFactory.setThreadPoolSize(8);

                mapViewer.setZoom(18);

                //Set the focus map on center
                GeoPosition center = new GeoPosition(10.258220082499857, 25.346508274383453);
                mapViewer.setAddressLocation(center);


                // Add interactions
                MouseInputListener mia = new PanMouseInputListener(mapViewer);
                mapViewer.addMouseListener(mia);
                mapViewer.addMouseMotionListener(mia);

                mapViewer.addMouseListener(new CenterMapListener(mapViewer));

                mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

                mapViewer.addKeyListener(new PanKeyListener(mapViewer));

                // Create a waypoint painter that takes all the waypoints
                WaypointPainter<Waypoint> painter = new WaypointPainter<>();
                painter.setWaypoints(fires);

//                RepeatingWaypointPainter<Waypoint> painter = new RepeatingWaypointPainter<>();
//                painter.setWaypoints(fires);

                mapViewer.setOverlayPainter(painter);

                painter.setRenderer(new MyWaypointRenderer());

                // Add a selection painter
//                SelectionAdapter sa = new SelectionAdapter(mapViewer);
//                SelectionPainter sp = new SelectionPainter(sa);
//                mapViewer.addMouseListener(sa);
//                mapViewer.addMouseMotionListener(sa);
//                mapViewer.setOverlayPainter(sp);


                mapViewer.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Point clickPoint = e.getPoint();
                        for (Waypoint waypoint : fires) {
                            Point2D waypointPoint = mapViewer.convertGeoPositionToPoint((waypoint).getPosition());

                            if (waypointPoint.distance(clickPoint) < 10) { // Adjust threshold as needed
                                // Show tooltip
                                JPopupMenu tooltip = new JPopupMenu();
                                tooltip.add(new JLabel("Waypoint: " + (waypoint).getPosition().toString()));
                                tooltip.show(mapViewer, e.getX(), e.getY());
                                break;
                            }
                        }
                    }
                });

                swingNode.setContent(mapViewer);
            }
        });

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);
        Scene scene = new Scene(pane, 1024, 768);

//        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
//        Scene scene = new Scene(root);
        stage.setTitle("Fire Map");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}