package MainMenu;

import game.ControllerGameSp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    /**
     * Metoda startowa aplikacji
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("UNO");
        scene = new Scene(root, 1200, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Metoda odpowiedzialna za zmiane sceny
     * @param fxml sciezka pliku fxml sceny
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
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
        scene.setRoot(root);
        fxmlLoader.<ControllerGameSp>getController().startGame(numberOfHumans, numberOfAi);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
