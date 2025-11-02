package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point for the JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // The AppComposer will now manage the stages and scenes
        AppComposer appComposer = new AppComposer();
        appComposer.startApplication(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}