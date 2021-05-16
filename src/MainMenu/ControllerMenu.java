package MainMenu;

import game.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControllerMenu
{
    /**
     * Przelacza scene na gre singleplayer
     * Pyta gracza o liczbe AI w grze
     * @throws IOException
     */
    @FXML
    private void switchToSingleplayer() throws IOException
    {
        Optional<Integer> numberOfAi = numberOfAiPlayersDialog(4,1);
        if(!numberOfAi.isPresent())
            return;
        Main.setRootGame("../game/game_board_sp.fxml", 1, numberOfAi.get());
    }

    /**
     * Przelacza scene na gre multiplayer
     * Pyta gracza o liczbe graczy i AI w grze
     * @throws IOException
     */
    @FXML
    private void switchToMultiplayer() throws IOException
    {
        Optional<Integer> numberOfhumans = numberOfPlayersDialog(4);
        if(!numberOfhumans.isPresent())
            return;
        Optional<Integer> numberOfAi = numberOfAiPlayersDialog(4,numberOfhumans.get());
        if(!numberOfAi.isPresent())
            return;
        Main.setRootGame("../game/game_board_v2.fxml", numberOfhumans.get(), numberOfAi.get());
    }

    /**
     * Przelacza scene na tabele z wynikami
     * @throws IOException
     */
    @FXML
    private void switchToScoreboard() throws IOException
    {
        Main.setRoot("../scoreboard/score_board.fxml");
    }

    /**
     * Wyswietla dialog z zapytaniem o liczbe graczy
     * Nie pozwala na wybranie liczby graczy wiecej niz podano w parametrach
     * @param maxPlayerNumber - maksymalna liczba graczy
     * @return - zwraca obiekt typu Optional z liczba calkowita
     */
    public Optional<Integer> numberOfPlayersDialog(int maxPlayerNumber)
    {
        List<Integer> choices = new ArrayList<>();
        for(int i = 1; i <= maxPlayerNumber; ++i)
        {
            choices.add(i);
        }
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(2, choices);
        dialog.setTitle("Enter number of human players");
        dialog.setHeaderText("Please choose\nnumber of human players\nsfrom list");
        dialog.setContentText("");
        Optional<Integer> result = dialog.showAndWait();
        return result;
    }

    /**
     * Wyswietla dialog z zapytaniem o liczbe sztucznych inteligencji
     * Liczba sztucznych inteligencji to roznica pomiedzy liczba graczy a maksymalna liczba
     * Nie pozwala na wybranie liczby graczy wiecej niz podano w parametrach
     * @param maxPlayerNumber - maksymalna liczba graczy
     * @param humansNumber - liczba ludzkich graczy
     * @return - zwraca obiekt typu Optional z liczba calkowita
     */
    public Optional<Integer> numberOfAiPlayersDialog(int maxPlayerNumber,int humansNumber)
    {
        List<Integer> choices = new ArrayList<>();
        int maxAi = maxPlayerNumber - humansNumber;
        int minAi = humansNumber == 1 ? 1 : 0;
        for (int i = minAi; i <= maxAi; ++i)
        {
            choices.add(i);
        }
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(minAi, choices);
        dialog.setTitle("Enter number of AI");
        dialog.setHeaderText("Please choose\nnumber of AI players\nfrom list");
        dialog.setContentText("");
        Optional<Integer> result = dialog.showAndWait();
        return result;
    }
}
