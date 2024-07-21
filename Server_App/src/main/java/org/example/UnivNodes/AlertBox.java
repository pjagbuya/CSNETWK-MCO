package org.example.UnivNodes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is a custom alertBox that display error messages only once
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public class AlertBox {

    public void display(String title, String message, int size)
    {
        if(isDisplayed)
            return;
        isDisplayed = true;

        Stage window = new Stage();
        

        String colorBg = "black";
        Font standardBoldLabel = Font.font("Arial", FontWeight.BOLD, size);
        window.initModality(Modality.APPLICATION_MODAL); // forces user to deal with this Alertbox
        window.setTitle(title);
        window.setMinWidth(300);
        Label label = new Label();

        label.setText(message);
        label.setFont(standardBoldLabel);
        label.setStyle("-fx-text-fill: #FFA500;");
        
        Button closeButton = new Button("Close");
        closeButton.setMinWidth(100);
        closeButton.setMinHeight(50);
        closeButton.setOnAction(e->window.close());


        VBox layout = new VBox(40);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-base:"+colorBg+";");
        layout.setPadding(new Insets(50));
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.show();


    }
    public void display(String title, String message)
    {
        display(title, message, 16);

    }
    public static void setDisplayed(boolean isDisplayed) {
        AlertBox.isDisplayed = isDisplayed;
    }
    private static boolean isDisplayed = false;


}
