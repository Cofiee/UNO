package game;

import MainMenu.Main;
import game.myAssets.AI.AIPlayer;
import game.myAssets.EngineGameSpV2;
import game.myAssets.cards.ACard;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

public class ControllerGameSp
{
    final EngineGameSpV2 engineGameSp;

    public enum Mode
    {
        SINGLEPLAYER,
        MULTIPLAYER
    }

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
    @FXML
    VBox score_board;

    /**
     * Konstruktor domyslny
     * Tworzy silnik logiczny gry
     */
    public ControllerGameSp()
    {
        this.engineGameSp = new EngineGameSpV2(this);
    }

    @FXML
    private void initialize() {}

    /**
     * Metoda odpowiedzialna za rozpoczecie rozgrywki
     * @param numberOfHumans liczba ludzkich graczy w grze
     * @param numberOfAi liczba sztucznych inteligencji w grze
     */
    public void startGame(int numberOfHumans, int numberOfAi)
    {
        engineGameSp.initializePlayers(numberOfHumans,numberOfAi);
        engineGameSp.prepareGame();
        updatePlayerHand();
    }

    /**
     * Metoda wyswietlajaca powiadomienie o potrzebie zmiany koloru
     * @return ACard.Color zwraca wybrany kolor karty
     */
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
    /**
     * Metoda aktualizujaca ikone koloru gornej karty
     */
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

    /**
     * Metoda wyswietlajaca karty na szczycie stolu
     */
    public void updateTopCard()
    {
        String path = "src/game/myAssets/cards/sprites/"+engineGameSp.parseCard(engineGameSp.getTopCard());
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

    /**
     * Metoda odpowiadajaca za wyswietlanie kart graczy w polach graczy
     * Kazda karta ma zainicjalizowany event
     */
    public void updatePlayerHand()
    {
        player_position_0_hbox.getChildren().removeAll(player_position_0_hbox.getChildren());
        Vector<ACard> hand = engineGameSp.actualPlayer().getHand();
        List<ImageView> imageViews = new LinkedList<>();
        double size = player_position_0_hbox.getWidth() / hand.size();
        int id = 0;
        for (ACard card:
                hand)
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGameSp.parseCard(card);
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
                        engineGameSp.cardOnTable(Integer.parseInt(imageView.getId()));
                        updateTopCard();
                        updatePlayerHand();
                        updateColorIcon(engineGameSp.getTopCard().getColor());
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

    /**
     * Inicjuje dobranie jednej karty w silniku
     */
    public void takeOneCard()
    {
        engineGameSp.takeOne();
    }

    public boolean matchCardDialog(ACard card)
    {
        try
        {
            String path = "src/game/myAssets/cards/sprites/" + engineGameSp.parseCard(card);
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
            String path = "src/game/myAssets/cards/sprites/" + engineGameSp.parseCard(card);
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
    public void disableAll()
    {

        player_position_0_hbox.getChildren().removeAll(player_position_0_hbox.getChildren());
        player_position_1_vbox.getChildren().removeAll(player_position_1_vbox.getChildren());
        player_position_2_hbox.getChildren().removeAll(player_position_2_hbox.getChildren());
        player_position_3_vbox.getChildren().removeAll(player_position_3_vbox.getChildren());
        button_take_card.setDisable(true);
    }
    public void enableAll()
    {
        button_take_card.setDisable(false);
        updatePlayerHand();
        updateTopCard();
        updateColorIcon(engineGameSp.getTopCard().getColor());
    }
    public void nextPlayerDialog()
    {
        if(engineGameSp.actualPlayer() instanceof AIPlayer)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("It is now Computer " + (engineGameSp.getIActualPlayer()) + " turn.");
            alert.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("It is now player " + (engineGameSp.getIActualPlayer()) + " turn.");
            alert.showAndWait();
            button_take_card.setDisable(false);
            updatePlayerHand();
        }
    }

    /**
     * Odswierzanie tabeli wynikow po udanej rozgrywce
     * @param points
     */
    public void refreshScoreboard(int[] points)
    {
        score_board.getChildren().removeAll(score_board.getChildren());
        int i = 0;
        for (int point:
                points)
        {
            score_board.getChildren().add(new Label("Player " + i + ":  " + point));
        }
    }
    public void updateHandSizes(int indexUpdatedPlayer, int handSize)
    {
        if(indexUpdatedPlayer == 1)
        {
            player_position_1_vbox.getChildren().removeAll(player_position_1_vbox.getChildren());
            player_position_1_vbox.getChildren().add(new Label("Player " + indexUpdatedPlayer + ":  " + handSize));
        }
        else if(indexUpdatedPlayer == 2)
        {
            player_position_2_hbox.getChildren().removeAll(player_position_2_hbox.getChildren());
            player_position_2_hbox.getChildren().add(new Label("Player " + indexUpdatedPlayer + ":  " + handSize));
        }
        else if(indexUpdatedPlayer == 3)
        {
            player_position_3_vbox.getChildren().removeAll(player_position_3_vbox.getChildren());
            player_position_3_vbox.getChildren().add(new Label("Player " + indexUpdatedPlayer + ":  " + handSize));
        }
    }

    /**
     * Metoda zmienia scene na sample.fxml
     */
    @FXML
    public void switchToMainMenu()
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
