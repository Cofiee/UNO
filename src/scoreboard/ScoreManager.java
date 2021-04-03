package scoreboard;

import javafx.scene.control.Alert;

import javax.imageio.IIOException;
import java.io.*;
import java.util.LinkedList;
import java.util.Vector;

public class ScoreManager
{
    String path;

    class MyNode
    {
        public String[] row1 = new String[5];
        public String[] row2 = new String[5];
    }

    //LinkedList<MyNode> scoreBoard;

    public ScoreManager(String path)
    {
        File file = new File(path);
        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }catch (IOException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(e.getClass().toString());
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Please check if \"" + path + "\" exists and can be modified");
            }
        }
    }

    public void saveScore(MyNode myNode)
    {
        PrintWriter printWriter = null
        try
        {
            printWriter = new PrintWriter(path);
        }
        catch (FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().toString());
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Please check if \"" + path + "\" exists and can be modified");
        }
        if(printWriter != null)
        {
            printWriter.println(
                    myNode.row1[0] + ";"
                    + myNode.row1[1] + ";"
                    + myNode.row1[2] + ";"
                    + myNode.row2[3] + ";"
                    + myNode.row2[4]
            );
            printWriter.println(
                    myNode.row2[0] + ";"
                    + myNode.row2[1] + ";"
                    + myNode.row2[2] + ";"
                    + myNode.row2[3] + ";"
                    + myNode.row2[4]
            );
        }
    }

    public void loadScore(){}
}
