package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import sample.Algorithm.Assignment;
import sample.Creator.MapCreator;
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
import java.util.*;

public class Controller implements Initializable {
    private Random         random     = new Random();
    private MapCreator     mapCreator = new MapCreator();
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
    public TextField txtRobotStartX;
    public TextField txtRobotStartY;
    public Button    btnCreateRobot;
    public TextField txtNumOfRandRobot;
    public TextField txtTypeOfRandRobot;
    /**
     * Task
     */
    public TextField txtTaskType;
    public TextField txtTaskGoalX;
    public TextField txtTaskGoalY;
    public TextField txtTaskTimeAppear;
    public TextField txtTaskTimeExecute;
    public Button    btnCreateTask;
    public TextField txtNumOfRandTask;
    public TextField txtTypeOfRandTask;
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


    /**
     * System
     * */
    public TextField txtRandomSeed;
    public Button    btnSaveSystemConfig;

    public Button    btnStartSimulation;
    public TextField txtTime;
    public TextField txtTimeMax;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);


        initializeTextField();
        initializeTableView();
    }
    public void btnSaveSimulationConfigClick(){
        int randomSeed = Integer.parseInt(txtRandomSeed.getText());
        int timeMax    = Integer.parseInt(txtTimeMax.getText());

        random.setSeed(randomSeed);
        Context.Time.timeMax = timeMax;

    }

    public void btnSaveShelfConfigClick(ActionEvent event) {
        boolean isEmpty = txtShelfXLength.getText().isEmpty() | txtShelfYLength.getText().isEmpty()
                | txtShelfEachRowNumber.getText().isEmpty()   | txtShelfEachColNumber.getText().isEmpty()
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

            mapCreator.getShelf().setxLength(shelfXLength);
            mapCreator.getShelf().setyLength(shelfYLength);
            mapCreator.getShelf().setxNumber(shelfEachRowNumber);
            mapCreator.getShelf().setyNumber(shelfEachColNumber);
            mapCreator.getDistance().setShelfToHorizontalShelf(distanceShelfToShelf);
            mapCreator.getDistance().setShelfToVerticalShelf(distanceShelfToShelf);
            mapCreator.getDistance().setBoundToHorizontalShelf(distanceBoundToShelf);
            mapCreator.getDistance().setBoundToVerticalShelf(distanceBoundToShelf);
            mapCreator.create();

            mapManager   = new MapManager(mapCreator);
            robotCreator = new RobotCreator(mapCreator, random);
            taskCreator  = new TaskCreator(mapCreator, random);
            robotManager = new RobotManager(robotCreator, mapManager);
            taskManager  = new TaskManager(taskCreator);

            updateAndView(0);
        }
    }
    public void btnCreateRobotClick(ActionEvent event) {
        boolean isEmpty = txtRobotType.getText().isEmpty()   | txtRobotHeading.getText().isEmpty()
                        | txtRobotStartX.getText().isEmpty() | txtRobotStartY.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert robot's properties!");
            alert.showAndWait();
        } else {
            int robotType                      = Integer.parseInt(txtRobotType.getText());
            int robotStartX                    = Integer.parseInt(txtRobotStartX.getText());
            int robotStartY                    = Integer.parseInt(txtRobotStartY.getText());
            PointInfo.Status robotStartHeading = PointInfo.Status.getEnum(Integer.parseInt(txtRobotHeading.getText()));

            Point robotStartPoint = new Point(robotStartX, robotStartY, robotStartHeading);
            if (robotCreator.create(new Robot(robotType, robotStartPoint))) {
                updateAndView(0);
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

            robotCreator.createRandom(robotNumOfRand, robotTypeOfRand);
            updateAndView(0);
        }
    }
    public void btnCreateTaskClick(ActionEvent event) {
        boolean isEmpty = txtTaskType.getText().isEmpty()       | txtTaskGoalX.getText().isEmpty() |
                          txtTaskGoalY.getText().isEmpty()      | txtTaskTimeAppear.getText().isEmpty() |
                          txtTaskTimeExecute.getText().isEmpty();

        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert task's properties!");
            alert.showAndWait();
        } else {
            int taskTimeExecute = Integer.parseInt(txtTaskTimeExecute.getText());
            int taskTimeAppear  = Integer.parseInt(txtTaskTimeAppear.getText());
            int taskType        = Integer.parseInt(txtTaskType.getText());
            int taskGoalX       = Integer.parseInt(txtTaskGoalX.getText());
            int taskGoalY       = Integer.parseInt(txtTaskGoalY.getText());
            Point taskGoalPoint = new Point(taskGoalX,taskGoalY);

            if (taskCreator.create(new Task(taskType, taskTimeExecute, taskTimeAppear, taskGoalPoint))) {
                updateAndView(0);
            }
        }
    }
    public void btnCreateTaskRandomClick(ActionEvent event){
        boolean isEmpty = txtNumOfRandTask.getText().isEmpty() | txtTypeOfRandTask.getText().isEmpty();
        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please insert input values of task randoming!");
            alert.showAndWait();
        } else {
            int taskNumOfRand  = Integer.parseInt(txtNumOfRandTask.getText());
            int taskTypeOfRand = Integer.parseInt(txtTypeOfRandTask.getText());

            taskCreator.createRandom(taskNumOfRand, taskTypeOfRand);
            updateAndView(0);
        }
    }




    public void btnStartSimulationClick(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            Context.Time.time++;
            updateAndView(Context.Time.time);

            if(Context.Time.time == 2){
                Assignment assignment = new Assignment(robotManager, taskManager);
                assignment.calculation(2);
                assignment.printResult();
            }
            txtTime.setText(Integer.toString(Context.Time.time));

            if (taskManager.isAssignable()){
                /**Create Thread*/
            }

            }));
        timeline.setCycleCount(Context.Time.timeMax);
        timeline.play();
    }

    public void btnTestClick(ActionEvent event) {
        List<Robot> robotList = robotManager.getRobotList();
        robotList.get(2).addPointToPointList(new Point(3,0,PointInfo.Status.ROBOT_RIGHT));
        robotList.get(2).addPointToPointList(new Point(4,0,PointInfo.Status.ROBOT_RIGHT));
        robotList.get(2).addPointToPointList(new Point(5,0,PointInfo.Status.ROBOT_DOWN));
        robotList.get(2).addPointToPointList(new Point(6,0,PointInfo.Status.ROBOT_RIGHT));
        robotList.get(2).addPointToPointList(new Point(7,0,PointInfo.Status.ROBOT_UP));
    }



    private void initializeTextField(){
        txtShelfXLength.setText("2");
        txtShelfYLength.setText("3");
        txtShelfEachRowNumber.setText("2");
        txtShelfEachColNumber.setText("2");
        txtDistanceShelfToShelf.setText("2");
        txtDistanceBoundToShelf.setText("1");
        txtNumOfRandRobot.setText("3");
        txtTypeOfRandRobot.setText("0");
        txtNumOfRandTask.setText("3");
        txtTypeOfRandTask.setText("0");
    }
    private void initializeTableView(){
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
        tableViewTaskList.setItems(taskObservableList);
    }
    private void viewTaskListToTableView() {
        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        for (int idx = 0; idx < taskCreator.getTaskList().size(); idx++) {
            Task task = taskCreator.getTaskList().get(idx);
            int taskIDView          = task.getId();
            int taskTypeView        = task.getType();
            int taskXView           = task.getGoal().getX();
            int taskYView           = task.getGoal().getY();
            int taskAppearTimeView  = task.getTimeAppear();
            int taskExecuteTimeView = task.getTimeExecute();
            Task.Status taskStatus  = task.getStatus();
            String taskStatusView;
            switch (taskStatus){
                case NEW:
                    taskStatusView = "NEW";
                    break;
                case READY:
                    taskStatusView = "READY";
                    break;
                case RUNNING:
                    taskStatusView = "RUNNING";
                    break;
                default:
                    taskStatusView = "DONE";
                    break;
            }

            TaskViewModel taskViewModel = new TaskViewModel(taskIDView, taskTypeView, taskXView, taskYView,
                    taskAppearTimeView, taskExecuteTimeView, taskStatusView);
            taskViewModelList.add(taskViewModel);
        }
        tableViewTaskList.getItems().clear();
        tableViewTaskList.getItems().addAll(taskViewModelList);
    }
    private void viewRobotListToTableView(int timeUpdate) {
        List<RobotViewModel> robotViewModelList = new ArrayList<>();
        for (int idx = 0; idx < robotCreator.getRobotList().size(); idx++) {
            Robot robot       = robotCreator.getRobotList().get(idx);
            int robotIDView   = robot.getId();
            int robotTypeView = robot.getType();
            int robotXView    = robot.getPoint(timeUpdate).getX();
            int robotYView    = robot.getPoint(timeUpdate).getY();
            PointInfo.Status robotHeading  = robot.getHeading(timeUpdate);
            String robotHeadingView;
            switch (robotHeading){
                case ROBOT_UP:
                    robotHeadingView = "UP";
                    break;
                case ROBOT_DOWN:
                    robotHeadingView = "DOWN";
                    break;
                case ROBOT_LEFT:
                    robotHeadingView = "LEFT";
                    break;
                default:
                    robotHeadingView = "RIGHT";
            }

            RobotViewModel robotViewModel = new RobotViewModel(robotIDView, robotTypeView, robotXView, robotYView, robotHeadingView);
            robotViewModelList.add(robotViewModel);
        }
        tableViewRobotList.getItems().clear();
        tableViewRobotList.getItems().addAll(robotViewModelList);
    }
    private void viewMapToScatter(int timeUpdate) {
        ScatterView scatterView = new ScatterView(mapManager, taskManager, scatterChart);
        scatterView.prepareDataToDisplay(timeUpdate);
        scatterView.display();
    }
    private void updateAndView(int timeUpdate) {
        robotManager.update(timeUpdate);
        taskManager.update(timeUpdate);
        viewRobotListToTableView(timeUpdate);
        viewTaskListToTableView();
        viewMapToScatter(timeUpdate);
    }


}
