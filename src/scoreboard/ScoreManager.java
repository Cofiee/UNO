package scoreboard;

import game.myAssets.Player;
import javafx.scene.control.Alert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

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

    public void saveScore(Player[] players)
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        ScoreManager.MyNode myNode = new ScoreManager.MyNode();
        myNode.row1[0] = "Gra";
        myNode.row1[1] = players[0].getClass().toString();
        myNode.row1[2] = players[1].getClass().toString();
        myNode.row1[3] = players[2].getClass().toString();
        myNode.row1[4] = players[3].getClass().toString();
        myNode.row2[0] = date.toString();
        myNode.row2[1] = String.valueOf(players[0].getScore());
        myNode.row2[2] = String.valueOf(players[1].getScore());
        myNode.row2[3] = String.valueOf(players[2].getScore());
        myNode.row2[4] = String.valueOf(players[3].getScore());
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
