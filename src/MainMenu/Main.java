package MainMenu;

import game.ControllerGameSp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Stack;

public class Main extends Application {

    private static Scene scene;
    private static Stage stage;
    /**
     * Metoda startowa aplikacji
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("UNO");
        scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Metoda odpowiedzialna za zmiane sceny
     * @param fxml sciezka pliku fxml sceny
     * @throws IOException
     */
    public static void setRoot(String fxml, double width, double height) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.setWidth(width);
        stage.setHeight(height);
        scene.setRoot(fxmlLoader.load());
    }

    /**
     * Metoda odpowiedzialna za zmiane sceny na scene gry
     * Nalezy podac parametry rozgrywki
     * @param fxml sciezka pliku fxml sceny
     * @param numberOfHumans liczba ludzkich graczy w grze
     * @param numberOfAi liczba sztucznych inteligencji w grze
     * @throws IOException
     */
    public static void setRootGame(String fxml, int numberOfHumans, int numberOfAi) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        stage.setWidth(1200);
        stage.setHeight(1000);
        scene.setRoot(root);
        fxmlLoader.<ControllerGameSp>getController().startGame(numberOfHumans, numberOfAi);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
