package game.dialogs;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;

public class PickColorDialog extends Dialog<String>
{
    String[] options = new String[]{
                "Red",
                "Blue",
                "Green",
                "Yellow"
            };
    public PickColorDialog()
    {
        initStyle(StageStyle.DECORATED);
        for(String option : options)
        {
            Button optionButton = new Button(option);
        }
    }
}
