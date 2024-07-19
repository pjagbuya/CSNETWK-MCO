package  org.example.UnivNodes;



import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
/**
 * This is a minor button that is by default inherits the custom button attributes
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public class MinorButton extends CustomButton{

    public MinorButton(String txt)
    {
        super(txt);
        this.setTextSize(12);

    }
    
}
