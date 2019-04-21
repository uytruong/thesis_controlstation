package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Point;
import sample.Model.PointInfo;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse Control Station Simulation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        PointInfo pointInfo = new Point();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
