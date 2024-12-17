package com.emanuelaugolotti.firemap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import java.awt.*;
import java.awt.geom.Point2D;

public class RepeatingWaypointPainter<W extends Waypoint> extends WaypointPainter<W> {

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        for (Waypoint waypoint : getWaypoints()) {
            GeoPosition pos = waypoint.getPosition();

            int wrapCount = 2;

            for (int wrap = -wrapCount; wrap <= wrapCount; wrap++) {
                double wrappedLongitude = pos.getLongitude() + wrap * 360.0;
                GeoPosition wrappedPosition = new GeoPosition(pos.getLatitude(), wrappedLongitude);

                Point2D point = map.convertGeoPositionToPoint(wrappedPosition);

                if (point.getX() >= -50 && point.getX() <= width + 50 && point.getY() >= -50 && point.getY() <= height + 50) {
                    drawWaypoint(g, point);
                }
            }
        }
    }

    private void drawWaypoint(Graphics2D g, Point2D point) {
        // Draw a simple circle for the waypoint
        int size = 10;
        g.setColor(Color.RED);
        g.fillOval((int)(point.getX() - size / 2), (int)(point.getY() - size / 2), size, size);
        g.setColor(Color.BLACK);
        g.drawOval((int)(point.getX() - size / 2), (int)(point.getY() - size / 2), size, size);
    }
}
