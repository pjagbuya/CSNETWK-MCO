package org.example.UnivNodes;
/**
 * This is an alertBox that inherits the custom alertbox but it can be called more than once
 *
 * @author Paul Josef P. Agbuya
 * @author Vince Kenneth D. Rojo
 */
public class AlertBoxRep extends AlertBox
{
    public AlertBoxRep(String title, String message)
    {
        super();
        super.display(title, message);
        super.setDisplayed(false);
    }
    public AlertBoxRep(String title, String message, int size)
    {
        super();
        super.display(title, message, size);
        super.setDisplayed(false);
    }
}
