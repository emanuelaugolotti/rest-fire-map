package com.emanuelaugolotti.firemap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.embed.swing.SwingNode;
import javax.swing.*;
import java.util.*;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // codice eseguito dal thread di javafx
        NasaDataReader nasaDataReader = new NasaDataReader("5b19bedf728592a1122d5fee8386e778");
        Set<FireWaypoint> fires = nasaDataReader.getData(1, "world", "VIIRS_SNPP_NRT");

        final SwingNode swingNode = new SwingNode();    // creazione dello swing node


        // il thread di javafx chiede al thread di swing di eseguire il metodo run()
        SwingUtilities.invokeLater(new MapInitializer(fires, swingNode, "fe087be4b2b3c8f992fce4262b13938d"));
        // il metodo run() di MapInitializer riempie lo swing node

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);
        Scene scene = new Scene(pane, 1024, 768);   // creazione della scena

//        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
//        Scene scene = new Scene(root);

        stage.setTitle("Fire Map");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        stage.setScene(scene);  // inserimento della scena dentro allo stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}