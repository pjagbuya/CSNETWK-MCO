package  app.src.main.java.ServerSide.UnivNodes;


import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
/**
 * This abstract class morphs buttons for other classes to inherit
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public abstract class CustomButton extends Button
{
    /**
     * The custom Button that can be morphed by other objects
     * @param text sets teh text of the button
     * @param font the font at which the button should be set
     * @param size size of the button
     */
    public CustomButton(String text, String font, int size)
    {
        super(text);
        Font arial = new Font(font, size);
        DropShadow shadow = new DropShadow();
        this.defaultStyle = " -fx-text-fill: #399918; -fx-base: #ECFFE6; -fx-border-color: #FFAAAA;" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-style: solid;";
        this.setText(text);
        this.setFont(arial);
        
        // Normal style
        this.setStyle(defaultStyle);
        


        
        // Adding the shadow when the mouse cursor is on
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            this.setEffect(shadow);
            setBorderColor("#FF7777");
            setBorderWidth(2);
        });

        // Removing the shadow when the mouse cursor is off
        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            this.setEffect(null);
            setBorderColor("#FFAAAA");
            setBorderWidth(1);

        });
    }

    public CustomButton(String text, int size)
    {
        this(text, "Arial", size);
    }
    public CustomButton(String text)
    {
        this(text, "Arial", 16);
    }

    public void setTextWeight(String type)
    {   

        String propertyCSS = "-fx-font-weight:";
        String replacement = propertyCSS+type+";";

        replaceProperty(propertyCSS, replacement);
        
    }
    public void setTextSize(int num)
    {
        String propertyCSS = "-fx-font-size:";
        String replacement = propertyCSS+num+"pt"+";";

        replaceProperty(propertyCSS, replacement);

    }
    public void setTextFamily(String family)
    {
        String propertyCSS = "-fx-font-family:";
        String replacement = propertyCSS+family+";";

        replaceProperty(propertyCSS, replacement);
        
    }   
    public void setBorderColor(String hexVal)
    {
        String propertyCSS = "-fx-border-color:";
        String replacement = propertyCSS+hexVal+";";

        replaceProperty(propertyCSS, replacement);
        
    }

    public void setBorderWidth(int num)
    {
        String propertyCSS = "-fx-border-width:";
        String replacement = propertyCSS+num+"px"+";";

        replaceProperty(propertyCSS, replacement);
        
    }



    private void replaceProperty(String propertyCSS, String replacement)
    {
        if(defaultStyle.indexOf(propertyCSS) != -1)
        {
            defaultStyle = defaultStyle.replace(defaultStyle.substring(defaultStyle.indexOf(propertyCSS), 
                                                                       defaultStyle.indexOf(";", defaultStyle.indexOf(propertyCSS))+1 ), 
                                                replacement  );
        }     
        else
            defaultStyle += replacement;


        this.setStyle(defaultStyle);
    }


    /**The default style it wants teh set up to be */
    private String defaultStyle;
}
