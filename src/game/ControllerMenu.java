package game;

import game.myAssets.cards.ACard;
import game.myAssets.cards.RegularCard;
import javafx.fxml.FXML;

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
        ACard card = new RegularCard(9, ACard.Color.BLACK);
        System.out.println(((RegularCard)card).getDigit());
        System.out.println(card.getColor());
    }
}
