package com.emanuelaugolotti.firemap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;

public class FireWaypointRenderer implements WaypointRenderer<FireWaypoint> {
    // Classe che serve per determinare come un incendio appare fisicamente sulla mappa

    private final Color black = Color.decode("#120203");
    private final Color red = Color.decode("#D61921");
    private final Color orange = Color.decode("#F88812");
    private final Color yellow = Color.decode("#FDCA40");

    private final int redCircleDimension = 11;
    private final int marginBlack = 1;

    public FireWaypointRenderer() {
    }

    @Override
    public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, FireWaypoint waypoint) {
        int zoom = jxMapViewer.getZoom();
        Point2D point = jxMapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), zoom);

        int x = (int) point.getX();
        int y = (int) point.getY();

        int blackCircleDimension = redCircleDimension + 2 * marginBlack;

        graphics2D.setColor(black);
        graphics2D.fillOval(x, y, blackCircleDimension, blackCircleDimension);

        Color frpColor = getFrpColor(waypoint.getFrp());
        graphics2D.setColor(frpColor);
        graphics2D.fillOval(x + marginBlack, y + marginBlack, redCircleDimension, redCircleDimension);
    }

    private Color getFrpColor(double frp) {
        if (frp <= 50) {       // low intensity fires
            return red;
        } else if (frp <= 500) {    // medium intensity fires
            return orange;
        } else {  // most intense fires
            return yellow;
        }
    }
}