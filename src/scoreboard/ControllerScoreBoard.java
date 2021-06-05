package scoreboard;

import MainMenu.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class ControllerScoreBoard
{
    @FXML
    ListView<GridPane> lv_scoreboard;

    public ControllerScoreBoard()
    {

    }

    @FXML
    void initialize()
    {
        drawScoreboard();
    }

    @FXML
    private void switchToMainMenu() throws IOException
    {
        Main.setRoot("sample.fxml");
    }
    private void drawScoreboard()
    {
        ScoreManager scoreManager = new ScoreManager("scoreboard.txt");
        LinkedList<ScoreManager.MyNode> myNodes = scoreManager.loadScore();
        Iterator it = myNodes.descendingIterator();
        while(it.hasNext())
        {
            ScoreManager.MyNode myNode = (ScoreManager.MyNode) it.next();
            GridPane gridPane = new GridPane();
            gridPane.add(new Label(myNode.row1[0]), 0, 0);
            gridPane.add(new Label(myNode.row1[1]), 1, 0);
            gridPane.add(new Label(myNode.row1[2]), 2, 0);
            gridPane.add(new Label(myNode.row1[3]), 3, 0);
            gridPane.add(new Label(myNode.row1[4]), 4, 0);
            gridPane.add(new Label(myNode.row2[0]), 0, 1);
            gridPane.add(new Label(myNode.row2[1]), 1, 1);
            gridPane.add(new Label(myNode.row2[2]), 2, 1);
            gridPane.add(new Label(myNode.row2[3]), 3, 1);
            gridPane.add(new Label(myNode.row2[4]), 4, 1);
            lv_scoreboard.getItems().add(gridPane);
        }
    }

    private class GridPaneCell extends ListCell<GridPane>
    {
        @Override
        public void updateItem(GridPane gridPane, boolean empty)
        {
            super.updateItem(gridPane, empty);
        }
    }
}
