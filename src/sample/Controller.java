package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Creator.MapBaseCreator;
import sample.Creator.RobotCreator;
import sample.Creator.TaskCreator;
import sample.Manager.MapManager;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Robot;
import sample.Model.Task;
import sample.UI.ViewModel.RobotViewModel;
import sample.UI.ViewModel.TaskViewModel;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Random random = new Random();
    MapBaseCreator mapBaseCreator = new MapBaseCreator();
    RobotCreator robotCreator     = new RobotCreator(mapBaseCreator, random);
    TaskCreator taskCreator       = new TaskCreator(mapBaseCreator, random);

    MapManager mapManager;
    RobotManager robotManager;
    TaskManager taskManager;

    // Shelf
    public TextField txtShelfWidth;
    public TextField txtShelfHeight;
    public TextField txtShelfRows;
    public TextField txtShelfCols;
    public TextField txtShelfSpace;
    public TextField txtShelfBound;
    public Button btnSaveShelf;
    // Robot
    public TextField txtRobotID;
    public TextField txtRobotType;
    public TextField txtRobotHeading;
    public TextField txtRobotStartPointX;
    public TextField txtRobotStartPointY;
    public Button btnCreateRobot;
    public TextField txtNumOfRandRobot;
    public TextField txtTypeOfRandRobot;
    // Task
    public TextField txtTaskID;
    public TextField txtTaskType;
    public TextField txtTaskHeading;
    public TextField txtTaskGoalPointX;
    public TextField txtTaskGoalPointY;
    public TextField txtTaskAppearTime;
    public TextField txtTaskExecuteTime;
    public Button btnCreateTask;
    public TextField txtNumOfRandTask;
    public TextField txtTypeOfRandTask;
    
    // Shelf properties
    private int shelfWidth;
    private int shelfHeight;
    private int shelfRows;
    private int shelfCols;
    private int shelfSpace;
    private int shelfBound;
    // Robot properties
    private int robotID;
    private int robotType;
    private int robotHeading;
    private int robotStartPointX;
    private int robotStartPointY;
    private int robotNumOfRand;
    private int robotTypeOfRand;
    // Task properties
    private int taskID;
    private int taskType;
    private int taskHeading;
    private int taskGoalPointX;
    private int taskGoalPointY;
    private int taskAppearTime;
    private int taskExecuteTime;
    private int taskNumOfRand;
    private int taskTypeOfRand;

    // Robot TableView
    public TableView tableViewRobotList;
    public TableColumn<RobotViewModel,Integer> tableColRobotID;
    public TableColumn<RobotViewModel,Integer> tableColRobotType;
    public TableColumn<RobotViewModel,Integer> tableColRobotX;
    public TableColumn<RobotViewModel,Integer> tableColRobotY;
    public TableColumn<RobotViewModel,String> tableColRobotHeading;
    // Task TableView
    public TableView tableViewTaskList;
    public TableColumn<TaskViewModel, Integer> tableColTaskID;
    public TableColumn<TaskViewModel, Integer> tableColTaskType;
    public TableColumn<TaskViewModel, Integer> tableColTaskAppearTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskExecuteTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskX;
    public TableColumn<TaskViewModel, Integer> tableColTaskY;
    public TableColumn<TaskViewModel, String> tableColTaskHeading;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtShelfWidth.setText("2");
        txtShelfHeight.setText("3");
        txtShelfRows.setText("3");
        txtShelfCols.setText("3");
        txtShelfSpace.setText("2");
        txtShelfBound.setText("1");
        txtNumOfRandRobot.setText("5");
        txtTypeOfRandRobot.setText("0");
        txtNumOfRandTask.setText("3");
        txtTypeOfRandTask.setText("0");
    }

    public void btnSaveShelfClick(ActionEvent event) {
        boolean isEmpty = false;
        isEmpty = txtShelfWidth.getText().isEmpty() | txtShelfHeight.getText().isEmpty() | txtShelfRows.getText().isEmpty() |
                txtShelfCols.getText().isEmpty() | txtShelfSpace.getText().isEmpty() | txtShelfBound.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert shelf's properties!");
            alert.showAndWait();
        }
        else {
            shelfWidth = Integer.parseInt(txtShelfWidth.getText());
            shelfHeight = Integer.parseInt(txtShelfHeight.getText());
            shelfRows = Integer.parseInt(txtShelfRows.getText());
            shelfCols = Integer.parseInt(txtShelfCols.getText());
            shelfSpace = Integer.parseInt(txtShelfSpace.getText());
            shelfBound = Integer.parseInt(txtShelfBound.getText());
            mapBaseCreator.getShelf().setxLength(shelfWidth);
            mapBaseCreator.getShelf().setyLength(shelfHeight);
            mapBaseCreator.getShelf().setxNumber(shelfRows);
            mapBaseCreator.getShelf().setyNumber(shelfCols);
            mapBaseCreator.getDistance().setShelfToHorizontalShelf(shelfSpace);
            mapBaseCreator.getDistance().setShelfToVerticalShelf(shelfSpace);
            mapBaseCreator.getDistance().setBoundToHorizontalShelf(shelfBound);
            mapBaseCreator.getDistance().setBoundToVerticalShelf(shelfBound);
            mapBaseCreator.update();
            mapBaseCreator.getMapBase().printMapBase();
        }
    }

    public void btnCreateRobotClick(ActionEvent event) {
    }

    public void btnRandomRobotClick(ActionEvent event) {
        boolean isEmpty = false;
        isEmpty = txtNumOfRandRobot.getText().isEmpty() | txtTypeOfRandRobot.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert input values of robot randoming!");
            alert.showAndWait();
        }
        else {
            robotNumOfRand = Integer.parseInt(txtNumOfRandRobot.getText());
            robotTypeOfRand = Integer.parseInt(txtTypeOfRandRobot.getText());
            robotCreator.createRobotRandom(robotNumOfRand, robotTypeOfRand);
            for (Robot lRobot: robotCreator.getRobotList()) {
                lRobot.printInfo();
            }
        }

        // Init Robot TableView
        tableColRobotID.setCellValueFactory(new PropertyValueFactory<>("RobotID"));
        tableColRobotType.setCellValueFactory(new PropertyValueFactory<>("RobotType"));
        tableColRobotX.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointX"));
        tableColRobotY.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointY"));
        tableColRobotHeading.setCellValueFactory(new PropertyValueFactory<>("RobotHeading"));
        ObservableList<RobotViewModel> robotObservableList = FXCollections.observableArrayList();
        // Set TableView data
        tableViewRobotList.setItems(robotObservableList);
        for (int idx = 0; idx < robotCreator.getRobotList().size(); idx++) {
            Robot robot = robotCreator.getRobotList().get(idx);
            int robotIDView = robot.getId();
            int robotTypeView = robot.getType();
            int robotXView = MapBase.getXFromId(robot.getPoint(0).getId());
            int robotYView = MapBase.getYFromId(robot.getPoint(0).getId());
            int robotHeadingInt = robot.getHeading(0);
            String robotHeadingView = "NONE";
            if (robotHeadingInt == Constant.RobotPointStatus.UP) {
                robotHeadingView = "UP";
            } else if (robotHeadingInt == Constant.RobotPointStatus.DOWN) {
                robotHeadingView = "DOWN";
            } else if (robotHeadingInt == Constant.RobotPointStatus.LEFT) {
                robotHeadingView = "LEFT";
            } else if (robotHeadingInt == Constant.RobotPointStatus.RIGHT) {
                robotHeadingView = "RIGHT";
            }
            RobotViewModel robotViewModel = new RobotViewModel(robotIDView, robotTypeView, robotXView, robotYView, robotHeadingView);
            tableViewRobotList.getItems().add(robotViewModel);
        }
    }

    public void btnRandomTaskClick(ActionEvent event) {
        boolean isEmpty = false;
        isEmpty = txtNumOfRandTask.getText().isEmpty() | txtTypeOfRandTask.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert input values of task randoming!");
            alert.showAndWait();
        }
        else {
            taskNumOfRand = Integer.parseInt(txtNumOfRandTask.getText());
            taskTypeOfRand = Integer.parseInt(txtTypeOfRandTask.getText());
            taskCreator.createTaskRandom(taskNumOfRand, taskTypeOfRand);
            for (Task lTask: taskCreator.getTaskList()) {
                lTask.printInfo();
            }
        }

        // Init Robot TableView
        tableColTaskID.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
        tableColTaskType.setCellValueFactory(new PropertyValueFactory<>("TaskType"));
        tableColTaskX.setCellValueFactory(new PropertyValueFactory<>("TaskGoalPointX"));
        tableColTaskY.setCellValueFactory(new PropertyValueFactory<>("TaskGoalPointY"));
        tableColTaskAppearTime.setCellValueFactory(new PropertyValueFactory<>("TaskAppearTime"));
        tableColTaskExecuteTime.setCellValueFactory(new PropertyValueFactory<>("TaskExecuteTime"));
        tableColTaskHeading.setCellValueFactory(new PropertyValueFactory<>("TaskHeading"));
        ObservableList<RobotViewModel> taskObservableList = FXCollections.observableArrayList();
        // Set TableView data
        tableViewTaskList.setItems(taskObservableList);
        for (int idx = 0; idx < taskCreator.getTaskList().size(); idx++) {
            Task task = taskCreator.getTaskList().get(idx);
            int taskIDView = task.getId();
            int taskTypeView = task.getType();
            int taskXView = MapBase.getXFromId(task.getGoal().getId());
            int taskYView = MapBase.getYFromId(task.getGoal().getId());
            int taskAppearTimeView = task.getTimeAppear();
            int taskExecuteTimeView = task.getTimeExecute();
            int taskHeadingInt = task.getGoal().getStatus();
            String taskHeadingView = "NONE";
            if (taskHeadingInt == Constant.RobotPointStatus.UP) {
                taskHeadingView = "UP";
            } else if (taskHeadingInt == Constant.TaskPointStatus.DOWN) {
                taskHeadingView = "DOWN";
            } else if (taskHeadingInt == Constant.TaskPointStatus.LEFT) {
                taskHeadingView = "LEFT";
            } else if (taskHeadingInt == Constant.TaskPointStatus.RIGHT) {
                taskHeadingView = "RIGHT";
            } else if (taskHeadingInt == Constant.TaskPointStatus.DONTCARE) {
                taskHeadingView = "DONTCARE";
            }
            TaskViewModel taskViewModel = new TaskViewModel(taskIDView, taskTypeView, taskXView, taskYView,
                                            taskAppearTimeView, taskExecuteTimeView, taskHeadingView);
            tableViewTaskList.getItems().add(taskViewModel);
        }
    }
}
