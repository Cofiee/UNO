/*
* Autor: Rafał Topolski
* Cel: Obsługa interfejsu urzytkownika
* */
package game;

import MainMenu.Main;
import game.myAssets.AIPlayer;
import game.myAssets.EngineGame;
import game.myAssets.cards.ACard;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ControllerGame
{
    final EngineGame engineGame;

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
        engineGame.initializePlayers(numberOfPlayersDialog(), 0);
        engineGame.prepareGame();
        updatePlayerHand();
    }
    /*
    * Wyswietla dialog z zapytaniem o liczbe graczy
    * Zwraca wybrana liczbe graczy
    * */
    public int numberOfPlayersDialog()
    {
        List<Integer> choices = new ArrayList<>();
        choices.add(2);
        choices.add(3);
        choices.add(4);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(2, choices);
        Optional<Integer> result = dialog.showAndWait();
        return result.get();
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
    public void updateTopCard()
    {
        String path = "src/game/myAssets/cards/sprites/"+engineGame.parseCard(engineGame.getTopCard());
        try
        {
            FileInputStream inputStream = new FileInputStream(path);
            ImageView imageView = new ImageView(new Image(inputStream));
            imageView.setFitHeight(300.0);
            imageView.setFitWidth(190.0);
            card_stack.getChildren().add(imageView);
        }
        catch (java.io.FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprite not found exception");
            alert.setHeaderText("java.io.FileNotFoundException");
            alert.setContentText(e.getMessage());
        }
    }
    /*
    * Metoda odpowiadajaca za wyswietlanie kart graczy w polach graczy
    * Kazda karta ma zainicjalizowany event
    * */
    public void updatePlayerHand()
    {
        player_position_0_hbox.getChildren().removeAll(player_position_0_hbox.getChildren());
        Vector<ACard> hand = engineGame.actualPlayer().getHand();
        List<ImageView> imageViews = new LinkedList<>();
        double size = player_position_0_hbox.getWidth() / hand.size();
        int id = 0;
        for (ACard card:
             hand)
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
            try
            {
                FileInputStream inputStream = new FileInputStream(path);
                ImageView imageView = new ImageView(new Image(inputStream));
                imageView.setFitHeight(300.0);
                imageView.setFitWidth(190.0);
                imageView.setId(Integer.toString(id));
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        engineGame.cardOnTable(Integer.parseInt(imageView.getId()));
                        updateTopCard();
                        updatePlayerHand();
                        updateColorIcon(engineGame.getTopCard().getColor());
                        updateTopCard();
                    }
                });
                imageView.setOnMouseEntered(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(310);
                    }
                });
                imageView.setOnMouseExited(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        imageView.setFitWidth(190);
                        imageView.setFitHeight(300);
                    }
                });
                imageViews.add(imageView);
            }
            catch (java.io.FileNotFoundException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sprite not found exception");
                alert.setHeaderText("java.io.FileNotFoundException");
                alert.setContentText(e.getMessage());
            }
            ++id;
        }
        player_position_0_hbox.getChildren().addAll(imageViews);
    }
    public void updateOponentsHands()
    {
        player_position_1_vbox.getChildren().removeAll(player_position_1_vbox.getChildren());
        player_position_2_hbox.getChildren().removeAll(player_position_2_hbox.getChildren());
        player_position_3_vbox.getChildren().removeAll(player_position_3_vbox.getChildren());
    }
    /*
    * Inicjuje dobranie jednej karty w silniku
    * */
    public void takeOneCard()
    {
        engineGame.takeOne();
    }
    /*
    *
    * */
    public boolean matchCardDialog(ACard card)
    {
        try
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
            InputStream inputStream = new FileInputStream(path);
            ImageView imageView = new ImageView(new Image(inputStream));
            final String[] options = {"Throw it", "Take it"};
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you woant throw it or keep it?");
            alert.setGraphic(imageView);
            alert.setTitle("Card match!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && (result.get() == ButtonType.OK))
                return true;
            else
                return false;
        }catch (java.io.FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprite not found exception");
            alert.setHeaderText("java.io.FileNotFoundException");
            alert.setContentText(e.getMessage());
        }
        return false;
    }
    public void takeCardDialog(ACard card)
    {
        try
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGame.parseCard(card);
            InputStream inputStream = new FileInputStream(path);
            ImageView imageView = new ImageView(new Image(inputStream));
            final String[] options = {"Throw it", "Take it"};
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(imageView);
            alert.setTitle("Card match!");
            Optional<ButtonType> result = alert.showAndWait();
        }catch (java.io.FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprite not found exception");
            alert.setHeaderText("java.io.FileNotFoundException");
            alert.setContentText(e.getMessage());
        }
    }
    public void nextTurn2(boolean isGameEnded)
    {
        updateTopCard();
        updateColorIcon(engineGame.getTopCard().getColor());
        if(isGameEnded)
        {
            switchToMainMenu();
            return;
        }
        player_position_0_hbox.getChildren().removeAll(player_position_0_hbox.getChildren());
        player_position_1_vbox.getChildren().removeAll(player_position_1_vbox.getChildren());
        player_position_2_hbox.getChildren().removeAll(player_position_2_hbox.getChildren());
        player_position_3_vbox.getChildren().removeAll(player_position_3_vbox.getChildren());
        button_take_card.setDisable(true);
        if(engineGame.actualPlayer() instanceof AIPlayer)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("It is now Computer " + (engineGame.getIActualPlayer()) + " turn.");
            alert.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("It is now player " + (engineGame.getIActualPlayer()) + " turn.");
            alert.showAndWait();
            button_take_card.setDisable(false);
            updatePlayerHand();
        }
    }
    @FXML
    private void switchToMainMenu()
    {
        try
        {
            Main.setRoot("../MainMenu/sample.fxml");
        }catch(IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IOException");
            alert.setHeaderText("Could not find specified path: ../MainMenu/sample.fxml");
            alert.setContentText("Check if path is proper");
            alert.showAndWait();
            System.exit(1);
        }

    }
}
