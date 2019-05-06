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
        mapCreator.getShelf().setxNumber(3);
        mapCreator.getShelf().setyNumber(2);
        mapCreator.getDistance().setShelfToHorizontalShelf(2);
        mapCreator.getDistance().setShelfToVerticalShelf(2);
        mapCreator.getDistance().setBoundToHorizontalShelf(2);
        mapCreator.getDistance().setBoundToVerticalShelf(2);
        mapCreator.create();

        final Map map = mapCreator.getMapBaseClone();
        Point robotA = new Point(2,0, PointInfo.Status.ROBOT_LEFT);
        Point goal1 = new Point(6,1, PointInfo.Status.NONE); // area 2 same row
        Point goal2 = new Point(3,5, PointInfo.Status.NONE); // area 2 same col
        Point goal3 = new Point(6,5, PointInfo.Status.NONE); // area 2 else

        Point robotB = new Point(0,2, PointInfo.Status.ROBOT_LEFT);
        Point goal4 = new Point(4,3, PointInfo.Status.NONE); // area 3 same row
        Point goal5 = new Point(1,7, PointInfo.Status.NONE); // area 3 same col
        Point goal6 = new Point(4,7, PointInfo.Status.NONE); // area 3 else

//        Map.getEstimatePathCost2(robotA,goal1,map);
//        Map.getEstimatePathCost2(robotA,goal2,map);
//        Map.getEstimatePathCost2(robotA,goal3,map);

        Map.getEstimatePathCost2(robotB,goal4,map);
        Map.getEstimatePathCost2(robotB,goal5,map);
        Map.getEstimatePathCost2(robotB,goal6,map);


    }
    public static void main(String[] args) {
        launch(args);
    }
}
