package com.emanuelaugolotti.firemap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;

public class FireWaypointRenderer implements WaypointRenderer<FireWaypoint> {
    // Classe che serve per determinare come un incendio appare fisicamente sulla mappa

    @Override
    public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, FireWaypoint waypoint) {
        int zoom = jxMapViewer.getZoom();
        Point2D point = jxMapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), zoom);

        int x = (int) point.getX();
        int y = (int) point.getY();

        //graphics2D.drawImage(origImage, x, y, null);

        Color black = Color.decode("#120203");
        Color red = Color.decode("#D61921");
        Color orange = Color.decode("#F88812");
        Color yellow=Color.decode("#FDCA40");

        int redCircleDimension = 11;
        int marginBlack = 1;
        int blackCircleDimension = redCircleDimension + 2 * marginBlack;

        graphics2D.setColor(black);
        graphics2D.fillOval(x, y, blackCircleDimension, blackCircleDimension);

        if(waypoint.getFrp() <= 50) {       // low intensity fires
            graphics2D.setColor(red);
        }
        else if (waypoint.getFrp() <= 500) {    // medium intensity fires
            graphics2D.setColor(orange);
        }
        else {  // most intense fires
            graphics2D.setColor(yellow);
        }
        graphics2D.fillOval(x + marginBlack, y + marginBlack, redCircleDimension, redCircleDimension);
    }
}
