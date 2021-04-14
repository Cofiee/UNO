package MainMenu;

import game.*;
import game.myAssets.cards.*;
import javafx.fxml.FXML;
import scoreboard.ScoreManager;

import java.io.IOException;
import java.util.LinkedList;

public class ControllerMenu
{
    @FXML
    private void switchToMultiplayer() throws IOException
    {
        Main.setRoot("../game/game_board_v2.fxml");
    }
    @FXML
    private void switchToScoreboard() throws IOException
    {
        Main.setRoot("../scoreboard/score_board.fxml");
    }

    public void test()
    {

    }
}
