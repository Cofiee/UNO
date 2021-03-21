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
        Main.setRoot("../game/game_board.fxml");
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
        /*
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
         */
        TakeTwoCard card = new TakeTwoCard(ACard.Color.RED);
        System.out.println(card.getClass().getSimpleName());
    }
}
