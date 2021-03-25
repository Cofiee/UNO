/*
* Autor: Rafał Topolski
* Cel: Obsługa interfejsu urzytkownika
* */
package game;

import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;
import game.dialogs.PickColorDialog;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    @FXML
    Button button_take_card;
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
                color = new Color(1.0,0.0,0.0,1.0);
                break;
            case BLUE:
                color = new Color(0.0,0.0,1.0,1.0);
                break;
            case GREEN:
                color = new Color(0.0,1.0,0.0,1.0);
                break;
            case YELLOW:
                color = new Color(1.0,1.0,0.0,1.0);
                break;
            default:
                color = new Color(0.0,0.0,0.0,1.0);
        }
        top_color.setFill(color);
    }
    /*
    Metoda wyswietlajaca karty na szczycie stolu
     */
    public void updateTopCard() throws Exception
    {
        String path = "src/game/myAssets/cards/sprites/"+engineGame.parseCard(engineGame.getTopCard());
        FileInputStream inputStream = new FileInputStream(path);
        ImageView imageView = new ImageView(new Image(inputStream));
        imageView.fitWidthProperty().bind(card_stack.widthProperty());
        imageView.fitHeightProperty().bind(card_stack.heightProperty());
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
        double size = player_position_0_hbox.getWidth() / hand.size();

        for (ACard card:
             hand)
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
            FileInputStream inputStream = new FileInputStream(path);
            ImageView imageView = new ImageView(new Image(inputStream));
            imageView.fitHeightProperty().bind(player_position_0_hbox.heightProperty());
            imageView.setFitWidth(size);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event)
                {
                    engineGame.cardOnTable(1);
                    try
                    {
                        updateTopCard();
                    }catch (Exception e)
                    {
                        System.out.println(e.getMessage() + "\n" + e.getCause());
                    }
                    updateColorIcon(engineGame.getTopColor());
                }
            });
            imageViews.add(imageView);
        }
        player_position_0_hbox.getChildren().addAll(imageViews);
    }
    public void takeOneCard()
    {
        engineGame.takeOne();
        try
        {
            updatePlayerHands();
        }catch (Exception e)
        {
            System.out.println(e.getCause() +" \n"+ e.getMessage());
        }
    }

}
