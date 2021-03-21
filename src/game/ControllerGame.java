package game;

import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;
import game.dialogs.PickColorDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ControllerGame
{
    EngineGame engineGame;

    @FXML
    public Circle top_color;

    @FXML
    public GridPane gp_table;

    public ControllerGame()
    {
    }
    @FXML
    void initialize()
    {
        try
        {
            updateTopCard();
        }
        catch (Exception e)
        {
            System.out.println(e.getCause());
        }
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
        top_color.setFill(color);
    }
    public void updateTopCard() throws  Exception
    {
        Node node = FXMLLoader.load(getClass().getResource("myAssets/cards/card.fxml"));
        gp_table.add(node, 1, 1);
    }
    public void updatePlayerHands()
    {

    }
    public void chooseCard()
    {
        //MODYFIKACJA ID
        engineGame.cardOnTable(1);
    }
    public void takeOneCard()
    {
        engineGame.takeOne();
    }

}
