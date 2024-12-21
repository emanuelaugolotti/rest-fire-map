package com.emanuelaugolotti.firemap.controller;

import com.emanuelaugolotti.firemap.map.MapInitializer;
import com.emanuelaugolotti.firemap.rest.FireWaypoint;
import com.emanuelaugolotti.firemap.rest.NasaDataReader;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.util.Set;

public class MapController {

    @FXML
    private StackPane stackPane;

    @FXML
    private Button buttonZoomIn;

    @FXML
    private Button buttonZoomOut;

    @FXML
    public void initialize() {
        // codice eseguito dal thread di javafx
        NasaDataReader nasaDataReader = new NasaDataReader("5b19bedf728592a1122d5fee8386e778");
        Set<FireWaypoint> fires = nasaDataReader.getData(1, "world", "VIIRS_SNPP_NRT");

        final SwingNode swingNode = new SwingNode();    // creazione dello swing node

        JXMapViewer jxMapViewer = new JXMapViewer();

        // il thread di javafx chiede al thread di swing di eseguire il metodo run()
        SwingUtilities.invokeLater(new MapInitializer(jxMapViewer, fires, swingNode, "fe087be4b2b3c8f992fce4262b13938d"));
        // il metodo run() di MapInitializer riempie lo swing node

        stackPane.getChildren().add(swingNode);
        buttonZoomIn.setOnAction(e -> ZoomIn(jxMapViewer));
        buttonZoomOut.setOnAction(e -> ZoomOut(jxMapViewer));
    }

    private void ZoomIn(JXMapViewer jxMapViewer) {
        SwingUtilities.invokeLater(() -> jxMapViewer.setZoom(jxMapViewer.getZoom() - 1));
        //jxMapViewer.setZoom(jxMapViewer.getZoom() - 1);
    }

    private void ZoomOut(JXMapViewer jxMapViewer) {
        SwingUtilities.invokeLater(() -> jxMapViewer.setZoom(jxMapViewer.getZoom() + 1));
        //jxMapViewer.setZoom(jxMapViewer.getZoom() + 1);
    }
}
