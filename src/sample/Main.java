package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Algorithm.Hungarian2;
import sample.Creator.MapBaseCreator;
import sample.Creator.RobotCreator;
import sample.Creator.TaskCreator;
import sample.Manager.Context;
import sample.Manager.MapManager;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;

import java.util.Random;


public class Main extends Application {
    Random random = new Random();
    MapBaseCreator mapBaseCreator = new MapBaseCreator();
    RobotCreator robotCreator     = new RobotCreator(mapBaseCreator, random);
    TaskCreator taskCreator       = new TaskCreator(mapBaseCreator, random);

    MapManager mapManager;
    RobotManager robotManager;
    TaskManager taskManager;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse Control Station Simulation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

//        random.setSeed(Context.Random.seed);
//        mapBaseCreator.getShelf().setxLength(2);
//        mapBaseCreator.getShelf().setyLength(3);
//        mapBaseCreator.getShelf().setxNumber(2);
//        mapBaseCreator.getShelf().setyNumber(2);
//        mapBaseCreator.getDistance().setBoundToHorizontalShelf(1);
//        mapBaseCreator.getDistance().setBoundToVerticalShelf(1);
//        mapBaseCreator.getDistance().setShelfToHorizontalShelf(2);
//        mapBaseCreator.getDistance().setShelfToVerticalShelf(2);
//        mapBaseCreator.update();
//        mapBaseCreator.getMapBase().printMapBase();
//
//        robotCreator.createRobot(new Robot(0,new Point(0,Constant.PointStatus.ROBOT_LEFT)));
//        robotCreator.createRobot(new Robot(1,new Point(1,Constant.PointStatus.ROBOT_LEFT)));
//        robotCreator.createRobot(new Robot(2,new Point(2,Constant.PointStatus.ROBOT_DOWN)));
//        robotCreator.createRobotRandom(2,0);
//        robotCreator.createRobotRandom(6,0);
//        for (Robot lRobot: robotCreator.getRobotList()) {
//            lRobot.printInfo();
//        }
//        robotManager = new RobotManager(robotCreator);
//        robotManager.getRobot(2).addPoint(new Point(3,Constant.PointStatus.ROBOT_LEFT));
//        robotManager.getRobot(2).addPoint(new Point(4,Constant.PointStatus.ROBOT_LEFT));
//
//        System.out.println("=======================");
//
//        taskCreator.createTask(new Task(0,1,0,new Point(19,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTask(new Task(1,1,0,new Point(23,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTask(new Task(2,1,1,new Point(24,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTask(new Task(2,1,1,new Point(27,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTask(new Task(2,1,1,new Point(27,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTask(new Task(2,1,2,new Point(27,Constant.PointStatus.ROBOT_DOWN)));
//        taskCreator.createTaskRandom(5,0);
//        for (Task ltask: taskCreator.getTaskList()) {
//            ltask.printInfo();
//        }
//
//        System.out.println("=======================");
//
//        taskManager = new TaskManager(taskCreator);
//        mapManager  = new MapManager(mapBaseCreator, robotManager);
//
//        Context.Time.time = 0;
//        taskManager.update(Context.Time.time);
//        taskManager.printInfo();
//        mapManager.update(Context.Time.time);
//        for (int i = 0; i < 4; i++) {
//            mapManager.getMap(i).printMapBase();
//        }
//
//        Context.Time.time = 1;
//        taskManager.update(Context.Time.time);
//        taskManager.printInfo();
//        mapManager.update(Context.Time.time);
//        for (int i = 0; i < 4; i++) {
//            mapManager.getMap(i).printMapBase();
//        }
//
//        taskManager.changeTaskStatus(3,Constant.TaskStatus.RUNNING);
//
//        Context.Time.time = 2;
//        mapManager.update(Context.Time.time);
//        taskManager.update(Context.Time.time);
//        taskManager.printInfo();
//        for (int i = 0; i < 4; i++) {
//            mapManager.getMap(i).printMapBase();
//        }
//
//        taskManager.changeTaskStatus(3,Constant.TaskStatus.DONE);
//
//        Context.Time.time = 3;
//        mapManager.update(Context.Time.time);
//        taskManager.update(Context.Time.time);
//        taskManager.printInfo();
//        for (int i = 0; i < 4; i++) {
//            mapManager.getMap(i).printMapBase();
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
