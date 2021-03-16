package game;

import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;
import game.myAssets.cards.StopCard;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

public class ControllerMenu
{
    @FXML
    private void switchToSecondary() throws IOException
    {
        Main.setRoot("game_board");
    }

    public void test()
    {
        /*
        ACard card = new RegularCard(9, ACard.Color.BLACK);
        if(card instanceof RegularCard)
        {
            System.out.println(card.getClass().getName());
            System.out.println(((RegularCard) card).getDigit());
            System.out.println(card.getColor());
        }*/
        ACard card = new StopCard(ACard.Color.BLUE);
        if(card instanceof ISpecialCard)
        {
            System.out.println(card.getClass().getName());
            System.out.println(card.getColor());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();
    }
}
