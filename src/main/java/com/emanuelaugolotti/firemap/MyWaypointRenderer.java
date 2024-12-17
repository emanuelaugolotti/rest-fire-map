package com.emanuelaugolotti.firemap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MyWaypointRenderer implements WaypointRenderer<Waypoint> {
    private BufferedImage origImage;

    public MyWaypointRenderer() {
        try {
            origImage = ImageIO.read(getClass().getResource("waypoint.png"));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, Waypoint waypoint) {
        if (origImage == null) {
            return;
        }

        Point2D point = jxMapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), jxMapViewer.getZoom());

        int x = (int) point.getX() - origImage.getWidth() / 2;
        int y = (int) point.getY() - origImage.getHeight();

        graphics2D.drawImage(origImage, x, y, null);
    }
}
