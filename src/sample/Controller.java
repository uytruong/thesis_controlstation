package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Creator.MapBaseCreator;
import sample.Creator.RobotCreator;
import sample.Creator.TaskCreator;
import sample.Manager.Context;
import sample.Manager.MapManager;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;
import sample.UI.ScatterView;
import sample.UI.ViewModel.RobotViewModel;
import sample.UI.ViewModel.TaskViewModel;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Random         random = new Random();
    private MapBaseCreator mapBaseCreator;
    private RobotCreator   robotCreator;
    private TaskCreator    taskCreator;

    private MapManager     mapManager;
    private RobotManager   robotManager;
    private TaskManager    taskManager;

    /**
     * Shelf
     */
    public TextField txtShelfXLength;
    public TextField txtShelfYLength;
    public TextField txtShelfEachRowNumber;
    public TextField txtShelfEachColNumber;
    public TextField txtDistanceShelfToShelf;
    public TextField txtDistanceBoundToShelf;
    public Button    btnCreateMapBase;
    /**
     * Robot
     */
    public TextField txtRobotType;
    public TextField txtRobotHeading;
    public TextField txtRobotStartPointX;
    public TextField txtRobotStartPointY;
    public Button    btnCreateRobot;
    public TextField txtNumOfRandRobot;
    public TextField txtTypeOfRandRobot;
    /**
     * Task
     */
    public TextField txtTaskType;
    public TextField txtTaskHeading;
    public TextField txtTaskGoalPointX;
    public TextField txtTaskGoalPointY;
    public TextField txtTaskTimeAppear;
    public TextField txtTaskTimeExecute;
    public Button    btnCreateTask;
    public TextField txtNumOfRandTask;
    public TextField txtTypeOfRandTask;

    /**
     * Robot properties
     */
    private int robotID;
    private int robotType;
    private int robotHeading;
    private int robotStartPointX;
    private int robotStartPointY;

    /**
     * Task properties
     */
    private int taskID;
    private int taskType;
    private int taskHeading;
    private int taskGoalPointX;
    private int taskGoalPointY;
    private int taskAppearTime;
    private int taskExecuteTime;
    private int taskNumOfRand;
    private int taskTypeOfRand;

    /**
     * Robot TableView
     */
    public TableView tableViewRobotList;
    public TableColumn<RobotViewModel, Integer> tableColRobotID;
    public TableColumn<RobotViewModel, Integer> tableColRobotType;
    public TableColumn<RobotViewModel, Integer> tableColRobotX;
    public TableColumn<RobotViewModel, Integer> tableColRobotY;
    public TableColumn<RobotViewModel, String> tableColRobotHeading;

    private ObservableList<RobotViewModel> robotObservableList = FXCollections.observableArrayList();


    /**
     * Task TableView
     */
    public TableView tableViewTaskList;
    public TableColumn<TaskViewModel, Integer> tableColTaskID;
    public TableColumn<TaskViewModel, Integer> tableColTaskType;
    public TableColumn<TaskViewModel, Integer> tableColTaskAppearTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskExecuteTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskX;
    public TableColumn<TaskViewModel, Integer> tableColTaskY;
    public TableColumn<TaskViewModel, String> tableColTaskStatus;

    private ObservableList<RobotViewModel> taskObservableList = FXCollections.observableArrayList();


    /**
     * Scatter chart
     */
    public NumberAxis xAxis;
    public NumberAxis yAxis;
    public ScatterChart<Number, Number> scatterChart;
    private ScatterView mScatterView;
    //private ScatterView mScatterView = new ScatterView(mapManager, taskManager, scatterChart);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        txtShelfXLength.setText("2");
        txtShelfYLength.setText("3");
        txtShelfEachRowNumber.setText("3");
        txtShelfEachColNumber.setText("3");
        txtDistanceShelfToShelf.setText("2");
        txtDistanceBoundToShelf.setText("1");
        txtNumOfRandRobot.setText("5");
        txtTypeOfRandRobot.setText("0");
        txtNumOfRandTask.setText("3");
        txtTypeOfRandTask.setText("0");


        // Init Robot TableView
        tableColRobotID.setCellValueFactory(new PropertyValueFactory<>("RobotID"));
        tableColRobotType.setCellValueFactory(new PropertyValueFactory<>("RobotType"));
        tableColRobotX.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointX"));
        tableColRobotY.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointY"));
        tableColRobotHeading.setCellValueFactory(new PropertyValueFactory<>("RobotHeading"));
        tableViewRobotList.setItems(robotObservableList);


        tableColTaskID.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
        tableColTaskType.setCellValueFactory(new PropertyValueFactory<>("TaskType"));
        tableColTaskX.setCellValueFactory(new PropertyValueFactory<>("TaskGoalPointX"));
        tableColTaskY.setCellValueFactory(new PropertyValueFactory<>("TaskGoalPointY"));
        tableColTaskAppearTime.setCellValueFactory(new PropertyValueFactory<>("TaskAppearTime"));
        tableColTaskExecuteTime.setCellValueFactory(new PropertyValueFactory<>("TaskExecuteTime"));
        tableColTaskStatus.setCellValueFactory(new PropertyValueFactory<>("TaskStatus"));
        // Set TableView data
        tableViewTaskList.setItems(taskObservableList);
    }

    public void btnSaveShelfClick(ActionEvent event) {
        boolean isEmpty = txtShelfXLength.getText().isEmpty() | txtShelfYLength.getText().isEmpty()
                | txtShelfEachRowNumber.getText().isEmpty() | txtShelfEachColNumber.getText().isEmpty()
                | txtDistanceShelfToShelf.getText().isEmpty() | txtDistanceBoundToShelf.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert shelf's properties!");
            alert.showAndWait();
        } else {
            int shelfXLength         = Integer.parseInt(txtShelfXLength.getText());
            int shelfYLength         = Integer.parseInt(txtShelfYLength.getText());
            int shelfEachRowNumber   = Integer.parseInt(txtShelfEachRowNumber.getText());
            int shelfEachColNumber   = Integer.parseInt(txtShelfEachColNumber.getText());
            int distanceShelfToShelf = Integer.parseInt(txtDistanceShelfToShelf.getText());
            int distanceBoundToShelf = Integer.parseInt(txtDistanceBoundToShelf.getText());

            mapBaseCreator = new MapBaseCreator();
            mapBaseCreator.getShelf().setxLength(shelfXLength);
            mapBaseCreator.getShelf().setyLength(shelfYLength);
            mapBaseCreator.getShelf().setxNumber(shelfEachRowNumber);
            mapBaseCreator.getShelf().setyNumber(shelfEachColNumber);
            mapBaseCreator.getDistance().setShelfToHorizontalShelf(distanceShelfToShelf);
            mapBaseCreator.getDistance().setShelfToVerticalShelf(distanceShelfToShelf);
            mapBaseCreator.getDistance().setBoundToHorizontalShelf(distanceBoundToShelf);
            mapBaseCreator.getDistance().setBoundToVerticalShelf(distanceBoundToShelf);

            mapBaseCreator.update();
            mapBaseCreator.getMapBase().printMapBase();

            mapManager   = new MapManager(mapBaseCreator);
            robotCreator = new RobotCreator(mapBaseCreator, random);
            taskCreator  = new TaskCreator(mapBaseCreator, random);
            robotManager = new RobotManager(robotCreator, mapManager);
            taskManager  = new TaskManager(taskCreator);

            viewScatter();
        }
    }

    public void btnCreateRobotClick(ActionEvent event) {
        boolean isEmpty = txtRobotType.getText().isEmpty() | txtRobotHeading.getText().isEmpty()
                | txtRobotStartPointX.getText().isEmpty() | txtRobotStartPointY.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert robot's properties!");
            alert.showAndWait();
        } else {
            int robotType = Integer.parseInt(txtRobotType.getText());
            int robotHeading = Integer.parseInt(txtRobotHeading.getText());
            int robotStartPointX = Integer.parseInt(txtRobotStartPointX.getText());
            int robotStartPointY = Integer.parseInt(txtRobotStartPointY.getText());
            Point robotStartPoint = new Point(robotStartPointX, robotStartPointY, robotHeading);
            if (robotCreator.createRobot(new Robot(robotType, robotStartPoint))) {
                viewRobotList();
                viewScatter();
            }
        }
    }

    public void btnCreateRobotRandomClick(ActionEvent event) {
        boolean isEmpty = txtNumOfRandRobot.getText().isEmpty() | txtTypeOfRandRobot.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert input values of robot randoming!");
            alert.showAndWait();
        } else {
            int robotNumOfRand  = Integer.parseInt(txtNumOfRandRobot.getText());
            int robotTypeOfRand = Integer.parseInt(txtTypeOfRandRobot.getText());

            robotCreator.createRobotRandom(robotNumOfRand, robotTypeOfRand);
            viewRobotList();
            viewScatter();
        }
    }

    private void viewRobotList() {
        for (int idx = robotCreator.getLastRobotNumber(); idx < robotCreator.getRobotList().size(); idx++) {
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
        robotCreator.setLastRobotNumber(robotCreator.getRobotList().size());
//        for (Robot lRobot: robotCreator.getRobotList()) {
//            lRobot.printInfo();
//        }
    }

    public void btnCreateTaskClick(ActionEvent event) {
        boolean isEmpty = txtTaskType.getText().isEmpty() | txtTaskHeading.getText().isEmpty()
                | txtTaskGoalPointX.getText().isEmpty() | txtTaskGoalPointY.getText().isEmpty()
                | txtTaskTimeAppear.getText().isEmpty() | txtTaskTimeExecute.getText().isEmpty();

        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert robot's properties!");
            alert.showAndWait();
        } else {
            int taskTimeExecute = Integer.parseInt(txtTaskTimeExecute.getText());
            int taskTimeAppear = Integer.parseInt(txtTaskTimeAppear.getText());
            int taskType = Integer.parseInt(txtTaskType.getText());
            int taskHeading = Integer.parseInt(txtTaskHeading.getText());
            int taskGoalPointX = Integer.parseInt(txtTaskGoalPointX.getText());
            int taskGoalPointY = Integer.parseInt(txtTaskGoalPointY.getText());
            Point taskGoalPoint = new Point(taskGoalPointX, taskGoalPointY, taskHeading);

            if (taskCreator.createTask(new Task(taskType, taskTimeExecute, taskTimeAppear, taskGoalPoint))) {
                viewTaskList();
                viewScatter();
            }
        }
    }

    public void btnCreateTaskRandomClick(ActionEvent event){
        boolean isEmpty = txtNumOfRandTask.getText().isEmpty() | txtTypeOfRandTask.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert input values of task randoming!");
            alert.showAndWait();
        } else {
            taskNumOfRand = Integer.parseInt(txtNumOfRandTask.getText());
            taskTypeOfRand = Integer.parseInt(txtTypeOfRandTask.getText());
            taskCreator.createTaskRandom(taskNumOfRand, taskTypeOfRand);
            viewTaskList();
            viewScatter();
        }
    }

    private void viewTaskList() {
        for (int idx = taskCreator.getLastTaskNumber(); idx < taskCreator.getTaskList().size(); idx++) {
            Task task = taskCreator.getTaskList().get(idx);
            int taskIDView          = task.getId();
            int taskTypeView        = task.getType();
            int taskXView           = MapBase.getXFromId(task.getGoal().getId());
            int taskYView           = MapBase.getYFromId(task.getGoal().getId());
            int taskAppearTimeView  = task.getTimeAppear();
            int taskExecuteTimeView = task.getTimeExecute();
            int taskStatusInt      = task.getStatus();
            String taskStatusView  = "NONE";
            if (taskStatusInt == Constant.TaskStatus.NEW) {
                taskStatusView = "NEW";
            } else if (taskStatusInt == Constant.TaskStatus.READY) {
                taskStatusView = "READY";
            } else if (taskStatusInt == Constant.TaskStatus.RUNNING) {
                taskStatusView = "RUNNING";
            } else if (taskStatusInt == Constant.TaskStatus.DONE) {
                taskStatusView = "DONE";
            }
            TaskViewModel taskViewModel = new TaskViewModel(taskIDView, taskTypeView, taskXView, taskYView,
                                                            taskAppearTimeView, taskExecuteTimeView, taskStatusView);
            tableViewTaskList.getItems().add(taskViewModel);

        }
        taskCreator.setLastTaskNumber(taskCreator.getTaskList().size());
//        for (Task ltask: taskCreator.getTaskList()) {
//            ltask.printInfo();
//        }
    }

    private void viewScatter() {
        robotManager.update(0);
        mScatterView = new ScatterView(mapManager, taskManager, scatterChart);
        mScatterView.PrepareDataToDisplay(0);
        mScatterView.DisplayScatterChart();
    }

    public void btnTestClick(ActionEvent event) {
        robotManager.update(1);
        taskManager.update(1);
        mScatterView = new ScatterView(mapManager, taskManager, scatterChart);
        mScatterView.PrepareDataToDisplay(1);
        mScatterView.DisplayScatterChart();
    }
}
