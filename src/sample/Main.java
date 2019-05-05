package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Creator.MapCreator;
import sample.Model.Map;
import sample.Model.Point;
import sample.Model.PointInfo;

public class Main extends Application {

    private MapCreator mapCreator = new MapCreator();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse Control Station Simulation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        mapCreator.getShelf().setxLength(2);
        mapCreator.getShelf().setyLength(3);
        mapCreator.getShelf().setxNumber(2);
        mapCreator.getShelf().setyNumber(2);
        mapCreator.getDistance().setShelfToHorizontalShelf(2);
        mapCreator.getDistance().setShelfToVerticalShelf(2);
        mapCreator.getDistance().setBoundToHorizontalShelf(1);
        mapCreator.getDistance().setBoundToVerticalShelf(1);
        mapCreator.create();

        final Map map = mapCreator.getMapBaseClone();
        Point point1 = new Point(0,0, PointInfo.Status.ROBOT_LEFT);
        Point point2 = new Point(2,0, PointInfo.Status.ROBOT_LEFT);
        System.out.println(Map.getEstimatePathCost2(point1,point2,map));
    }
    public static void main(String[] args) {
        launch(args);
    }
}
