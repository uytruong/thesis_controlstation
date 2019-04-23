package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Motion;
import sample.Model.Node;
import sample.Model.Point;
import sample.Model.PointInfo;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse Control Station Simulation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        Point start = new Point(0,0, PointInfo.Status.ROBOT_RIGHT);
        Point goal  = new Point(3,9);
        Node node1 = new Node(start,goal,0);
        Node node2 = node1.getNeighborNode(Motion.Action.SPEED_UP);
        Node node3 = node2.getNeighborNode(Motion.Action.MOVE_CONSTANT);
        Node node4 = node3.getNeighborNode(Motion.Action.SPEED_DOWN);
        Node node5 = node4.getNeighborNode(Motion.Action.ROTATE_RIGHT);
        Node node6 = node5.getNeighborNode(Motion.Action.ROTATE_BACK);
        Node node7 = node6.getNeighborNode(Motion.Action.STEP);

        List<Point> pointList = new ArrayList<>();
        /*pointList.add(node1);
        pointList.addAll(node2.getCorrespondPointList());
        pointList.addAll(node3.getCorrespondPointList());
        pointList.addAll(node4.getCorrespondPointList());
        pointList.addAll(node5.getCorrespondPointList());
        pointList.addAll(node6.getCorrespondPointList());
        pointList.addAll(node7.getCorrespondPointList());
        //pointList.addAll(node1.getCorrespondPointList());
        */
        Node node = node7;
        while (node != node1){

            List<Point> correspondPointList = node.getCorrespondPointList();
            for (int i=correspondPointList.size()-1;i>= 0; i--) {
                pointList.add(0,correspondPointList.get(i));
            }
            node = node.getPreviousNode();
            System.out.println("+");
        }
        pointList.add(0,node1);
        
        for (Point point: pointList) {
            point.printInfo();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
