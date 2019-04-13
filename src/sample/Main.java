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
    MapBaseCreator mapBaseCreator = new MapBaseCreator();
    RobotCreator robotCreator     = new RobotCreator(mapBaseCreator);
    TaskCreator taskCreator       = new TaskCreator(mapBaseCreator);

    MapManager mapManager;
    RobotManager robotManager;
    TaskManager taskManager;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        mapBaseCreator.getShelf().setxLength(2);
        mapBaseCreator.getShelf().setyLength(3);
        mapBaseCreator.getShelf().setxNumber(5);
        mapBaseCreator.getShelf().setyNumber(5);

        mapBaseCreator.getDistance().setBoundToHorizontalShelf(1);
        mapBaseCreator.getDistance().setBoundToVerticalShelf(1);
        mapBaseCreator.getDistance().setShelfToHorizontalShelf(2);
        mapBaseCreator.getDistance().setShelfToVerticalShelf(2);

        mapBaseCreator.update();
        mapBaseCreator.getMapBase().printMapBase();

        /*
        Robot robot = new Robot(0,new Point(0,Constant.PointStatus.LEFT));
        robotCreator.createRobot(robot);
        robot = new Robot(1,new Point(1,Constant.PointStatus.LEFT));
        robotCreator.createRobot(robot);
        robot = new Robot(2,new Point(2,Constant.PointStatus.DOWN));
        robotCreator.createRobot(robot);
        for (Robot lrobot: robotCreator.getRobotList()) {
            lrobot.printInfo();
        }

        System.out.println("========================");
        robotManager = new RobotManager(robotCreator);
        for (Robot lrobot: robotManager.getRobotList()) {
            lrobot.printInfo();
        }
        //Context.time = 2;
        robotManager.getRobot(2).addPoint(new Point(58,Constant.PointStatus.LEFT));
        robotManager.getRobot(2).addPoint(new Point(59,Constant.PointStatus.LEFT));

        robotManager.update();
        for (Robot lrobot: robotManager.getRobotList()) {
            lrobot.printInfo();
        }
        System.out.println("=======================");
*/
        taskCreator.createTask(new Task(0,1,0,new Point(19,Constant.PointStatus.DOWN)));
        taskCreator.createTask(new Task(1,1,0,new Point(23,Constant.PointStatus.DOWN)));
        taskCreator.createTask(new Task(2,1,1,new Point(24,Constant.PointStatus.DOWN)));
        taskCreator.createTask(new Task(2,1,1,new Point(27,Constant.PointStatus.DOWN)));
        taskCreator.createTask(new Task(2,1,1,new Point(27,Constant.PointStatus.DOWN)));
        taskCreator.createTask(new Task(2,1,2,new Point(27,Constant.PointStatus.DOWN)));


        for (Task ltask: taskCreator.getTaskList()) {
            ltask.printInfo();
        }

        System.out.println("=======================");
        taskManager = new TaskManager(taskCreator);

        Context.time = 0;
        taskManager.update();
        taskManager.printInfo();

        taskManager.changeTaskStatus(1,Constant.TaskStatus.RUNNING);
        taskManager.changeTaskStatus(0,Constant.TaskStatus.DONE);

        Context.time = 1;
        taskManager.update();
        taskManager.printInfo();

        taskManager.changeTaskStatus(0,Constant.TaskStatus.DONE);
        Context.time = 2;
        taskManager.update();
        taskManager.printInfo();


        taskManager.changeTaskStatus(2,Constant.TaskStatus.DONE);
        taskManager.changeTaskStatus(3,Constant.TaskStatus.DONE);

        Context.time = 3;
        taskManager.update();
        taskManager.printInfo();

        taskManager.changeTaskStatus(4,Constant.TaskStatus.DONE);
        Context.time = 4;
        taskManager.update();
        taskManager.printInfo();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
