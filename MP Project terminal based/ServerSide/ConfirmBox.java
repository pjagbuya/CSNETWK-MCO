package Boxes;




import javax.swing.event.MenuEvent;

import Buttons.MinorButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.VBox;


/**
 * This class is a confirm box that receives user's yes or no input
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public class ConfirmBox {

    
    public ConfirmBox()
    {
        answer = false;
    }

    public boolean display(String title, String message)
    {
        Stage window = new Stage();
        
        String colorBg = "#071952";
        Font standardBoldLabel = Font.font("Arial", FontWeight.BOLD, 16);
        
        Label label = new Label();

        // Buttons
        yesButton = new MinorButton("Yes");
        noButton = new MinorButton("No");

        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(600);


        label.setText(message);
        label.setFont(standardBoldLabel);



        yesButton.setOnAction(e-> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e ->
        {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, yesButton, noButton);


        layout.setStyle("-fx-base: "+colorBg+";");
        layout.setPadding(new Insets(50));
        Scene scene = new Scene(layout);

        
        window.setScene(scene);
        window.showAndWait();

        return answer;

    }
    private Button yesButton;
    private Button noButton;
    private static boolean answer;
}
