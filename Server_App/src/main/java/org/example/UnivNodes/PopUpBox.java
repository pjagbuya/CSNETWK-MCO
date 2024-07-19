package org.example.UnivNodes;

import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * This class simulates a popup box dispensing an item
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public class PopUpBox 
{
    

    public PopUpBox(StackPane stackPane) 
    {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        

        // Convert StackPane to an image for use as a background
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage snapshot = stackPane.snapshot(params, null);

        // Use the snapshot as a background image
        BackgroundImage backgroundImage = new BackgroundImage(snapshot,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        

        // Create a new Pane for the popup window and set its background
        Pane pane = new Pane();
        pane.setBackground(background);

        // Create a Scene with the Pane and set it to the stage
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setWidth(stackPane.getWidth());
        stage.setHeight(stackPane.getHeight());
    }

    public void showAndWait() 
    {
        stage.showAndWait();
        stage.centerOnScreen();
    }


    private Stage stage;
}
