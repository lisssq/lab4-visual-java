package org.example.lab4visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        // Загружаем FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("CPU Emulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}