/*
* Autor: Rafał Topolski
* Cel: Obsługa interfejsu urzytkownika
* */
package game;

import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public ControllerGame()
    {
        this.engineGame = new EngineGame(this);
    }
    @FXML
    void initialize()
    {
        engineGame.prepareGame();
        try
        {
            updatePlayerHands();
        }
        catch (Exception e)
        {

        }
    }
    /*
    * Metoda wyswietlajaca powiadomienie o potrzebie zmiany koloru
    * */
    public ACard.Color chColorAlert()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose color");
        alert.setHeaderText("Choose one color by clicking on button");
        ButtonType buttonTypeRed = new ButtonType("Red");
        ButtonType buttonTypeBlue = new ButtonType("Blue");
        ButtonType buttonTypeGreen = new ButtonType("Green");
        ButtonType buttonTypeYellow = new ButtonType("Yellow");
        alert.getButtonTypes().setAll(buttonTypeRed, buttonTypeBlue, buttonTypeGreen, buttonTypeYellow);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeRed)
            return ACard.Color.RED;
        else if(result.get() == buttonTypeBlue)
            return ACard.Color.BLUE;
        else if(result.get() == buttonTypeGreen)
            return ACard.Color.GREEN;
        else
            return ACard.Color.YELLOW;
    }
    /*
    * Metoda aktualizujaca ikone koloru gornej karty
    * */
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
    * Metoda wyswietlajaca karty na szczycie stolu
    * */
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
    * Metoda odpowiadajaca za wyswietlanie kart graczy w polach graczy
    * Kazda karta ma zainicjalizowany event
    * */
    public void updatePlayerHands() throws Exception
    {
        player_position_0_hbox.getChildren().removeAll(player_position_0_hbox.getChildren());
        Vector<ACard> hand = engineGame.ActualPlayer().getHand();
        List<ImageView> imageViews = new LinkedList<>();
        double size = player_position_0_hbox.getWidth() / hand.size();
        int id = 0;
        for (ACard card:
             hand)
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
            FileInputStream inputStream = new FileInputStream(path);
            ImageView imageView = new ImageView(new Image(inputStream));
            imageView.fitHeightProperty().bind(player_position_0_hbox.heightProperty());
            imageView.setFitWidth(size);
            imageView.setId(Integer.toString(id));
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event)
                {
                    engineGame.cardOnTable(Integer.parseInt(imageView.getId()));
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
            ++id;
        }
        player_position_0_hbox.getChildren().addAll(imageViews);
    }
    /**/
    public void takeOneCard()
    {
        engineGame.takeOne();
    }
    /**/
    public boolean takeOrFake(ACard card) throws Exception
    {
        String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
        FileInputStream inputStream = new FileInputStream(path);
        ImageView imageView = new ImageView(new Image(inputStream));
        final String[] options = {"Throw it", "Take it"};
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you woant throw it or keep it?");
        alert.setTitle("Card match!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK))
            return true;
        else
            return false;
    }
}
