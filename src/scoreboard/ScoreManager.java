package scoreboard;

import javafx.scene.control.Alert;

import javax.imageio.IIOException;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class ScoreManager
{
    String path;

    public static class MyNode
    {
        public String[] row1 = new String[5];
        public String[] row2 = new String[5];
    }

    LinkedList<MyNode> scoreBoard;

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
        this.path = path;
    }

    public void saveScore(MyNode myNode)
    {
        PrintWriter printWriter = null;
        try
        {
            File file = new File(path);
            printWriter = new PrintWriter(file);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().toString());
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Please check if \"" + path + "\" exists and can be modified");
            alert.showAndWait();
        }

        if(printWriter != null)
        {
            printWriter.println(
                    myNode.row1[0] + " "
                    + myNode.row1[1] + " "
                    + myNode.row1[2] + " "
                    + myNode.row1[3] + " "
                    + myNode.row1[4]
            );
            printWriter.println(
                    myNode.row2[0] + " "
                    + myNode.row2[1] + " "
                    + myNode.row2[2] + " "
                    + myNode.row2[3] + " "
                    + myNode.row2[4]
            );
            printWriter.flush();
        }
        printWriter.close();
    }

    public LinkedList<MyNode> loadScore()
    {
        File file = new File(path);
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().toString());
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Please check if \"" + path + "\" exists and can be modified");
        }
        this.scoreBoard = new LinkedList<>();
        while (scanner.hasNext())
        {
            MyNode myNode = new MyNode();
            myNode.row1[0] = scanner.next();
            myNode.row1[1] = scanner.next();
            myNode.row1[2] = scanner.next();
            myNode.row1[3] = scanner.next();
            myNode.row1[4] = scanner.next();

            myNode.row2[0] = scanner.next();
            myNode.row2[1] = scanner.next();
            myNode.row2[2] = scanner.next();
            myNode.row2[3] = scanner.next();
            myNode.row2[4] = scanner.next();
            this.scoreBoard.add(myNode);
        }
        scanner.close();
        return this.scoreBoard;
    }
}
