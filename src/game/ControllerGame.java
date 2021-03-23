package game;

import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;
import game.dialogs.PickColorDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ControllerGame
{
    EngineGame engineGame;

    @FXML
    Circle top_color;
    @FXML
    AnchorPane card_stack;
    @FXML
    HBox player_position_0_hbox;
    @FXML
    VBox player_position_1_vbox;
    @FXML
    HBox player_position_2_hbox;
    @FXML
    VBox player_position_3_vbox;
    //@FXML
    //public GridPane gp_table;

    public ControllerGame()
    {
        this.engineGame = new EngineGame(this);
        engineGame.prepareGame();
    }
    @FXML
    void initialize()
    {

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
    /*
    Metoda wyswietlajaca karty na szczycie stolu
     */
    public void updateTopCard() throws  Exception
    {
        String path = "src/game/myAssets/cards/sprites/"+"blue_0_large.png";//engineGame.parseCard()
        FileInputStream inputStream = new FileInputStream(path);
        ImageView imageView = new ImageView(new Image(inputStream));
        card_stack.getChildren().add(imageView);
    }
    /*
    Metoda odpowiadajaca za wyswietlanie kart graczy w polach graczy
     */
    public void updatePlayerHands() throws Exception
    {
        player_position_0_hbox.getChildren().removeAll();
        Vector<ACard> hand = engineGame.ActualPlayer().getHand();
        List<ImageView> imageViews = new LinkedList<>();
        for (ACard card:
             hand)
        {
            String path = engineGame.parseCard(card);
            FileInputStream inputStream = new FileInputStream(path);
            imageViews.add(new ImageView(new Image(inputStream)));
        }
        player_position_0_hbox.getChildren().addAll(imageViews);
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
