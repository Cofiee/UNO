package game;

import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;
import game.visualAssets.PickColorDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
public class ControllerGame
{
    EngineGame engineGame;

    @FXML
    Circle TopColor;

    ControllerGame()
    {
        engineGame = new EngineGame(this);
    }
    public void chColorAlert()
    {
        PickColorDialog dialog = new PickColorDialog();
        dialog.showAndWait().ifPresent(response -> {
            if(response == "Red")
            {
                engineGame.changeColor(ACard.Color.RED);
            }
            else if(response == "Green")
            {
                engineGame.changeColor(ACard.Color.GREEN);
            }
            else if(response == "Blue")
            {
                engineGame.changeColor(ACard.Color.BLUE);
            }
            else
            {
                engineGame.changeColor(ACard.Color.YELLOW);
            }
        });
    }
    public void updateColorIcon(ACard.Color myColor)
    {
        Color color;
        switch (myColor)
        {
            case RED:
                color = new Color(255.0,0.0,0.0,100.0);
                break;
            case BLUE:
                color = new Color(0.0,0.0,255.0,100.0);
                break;
            case GREEN:
                color = new Color(0.0,255.0,0.0,100.0);
                break;
            case YELLOW:
                color = new Color(255.0,255.0,0.0,100.0);
                break;
            default:
                color = new Color(0.0,0.0,0.0,100.0);
        }
        TopColor.setFill(color);
    }
    public void updateTopCard()
    {

    }
    public void updatePlayersHands()
    {

    }
}
