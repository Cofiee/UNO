package MainMenu;

import game.*;
import game.myAssets.cards.*;
import javafx.fxml.FXML;

import java.io.IOException;

public class ControllerMenu
{
    @FXML
    private void switchToSecondary() throws IOException
    {
        Main.setRoot("../game/game_board_v2.fxml");
    }
    public void test()
    {

    }
}
