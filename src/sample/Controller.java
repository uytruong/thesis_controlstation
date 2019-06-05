package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.Algorithm.MultiPathPlanning;
import sample.Creator.MapCreator;
import sample.Creator.RobotCreator;
import sample.Creator.TaskCreator;
import sample.Manager.Context;
import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;
import sample.Model.Map;
import sample.UI.ScatterView;
import sample.UI.ViewModel.RobotViewModel;
import sample.UI.ViewModel.TaskViewModel;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private Random       random     = new Random();
    private RobotCreator robotCreator;
    private TaskCreator  taskCreator;

    private MapData      mapData;
    private RobotManager robotManager;
    private TaskManager  taskManager;

    private Timeline     timeline;
    private Timeline     solvingThread;

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


    public TextField txtEmptyMapBaseXLength;
    public TextField txtEmptyMapBaseYLength;
    public Button    btnCreateMapBaseEmpty;
    /**
     * Robot
     */
    public TextField txtRobotStartX;
    public TextField txtRobotStartY;
    public ChoiceBox<String> cbRobotHeading;
    public Button    btnCreateRobot;
    public TextField txtNumOfRandRobot;
    public Button    btnCreateRobotRandom;

    private ObservableList<String> cbRobotHeadingList = FXCollections.observableArrayList(PointInfo.Status.getRobotHeadingStringList());
    /**
     * Task
     */
    public TextField txtTaskStationX;
    public TextField txtTaskGoalX;
    public TextField txtTaskGoalY;
    public TextField txtTaskTimeAppear;
    public TextField txtTaskTimeExecute;
    public ChoiceBox<String> cbTaskHeading;
    public Button    btnCreateTask;
    public TextField txtNumOfRandTask;
    public Button    btnCreateTaskRandom;
    /**
     * Robot TableView
     */
    public TableView<RobotViewModel>            tableViewRobotList;
    public TableColumn<RobotViewModel, Integer> tableColRobotID;
    public TableColumn<RobotViewModel, Integer> tableColRobotX;
    public TableColumn<RobotViewModel, Integer> tableColRobotY;
    public TableColumn<RobotViewModel, String>  tableColRobotHeading;
    public TableColumn<RobotViewModel, Integer> tableColRobotTimeFree;
    public TableColumn<RobotViewModel, String>  tableColRobotStatus;
    public TableColumn<RobotViewModel, String>  tableColRobotTask;
    private ObservableList<RobotViewModel>      robotObservableList = FXCollections.observableArrayList();
    /**
     * Task TableView
     */
    public TableView<TaskViewModel>            tableViewTaskList;
    public TableColumn<TaskViewModel, Integer> tableColTaskID;
    public TableColumn<TaskViewModel, Integer> tableColTaskTimeAppear;
    public TableColumn<TaskViewModel, Integer> tableColTaskTimeExecute;
    public TableColumn<TaskViewModel, Integer> tableColTaskGoalX;
    public TableColumn<TaskViewModel, Integer> tableColTaskStationX;
    public TableColumn<TaskViewModel, Integer> tableColTaskGoalY;
    public TableColumn<TaskViewModel, String>  tableColTaskGoalHeading;
    public TableColumn<TaskViewModel, String>  tableColTaskStatus;
    private ObservableList<TaskViewModel>      taskObservableList = FXCollections.observableArrayList();
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
    public TextField txtTime;
    public TextField txtTimeMax;
    public TextField txtStepCost;
    public TextField txtRotateCost;
    public TextField txtTaskNumber;
    public TextField txtTaskDoneNumber;
    public TextField txtTimeSolveMin;
    public TextField txtTimeLoopMaxForPriorSearch;
    public Button    btnStartSimulation;
    public Button    btnSaveSystemConfig;
    public Button    btnReset;
    public  ChoiceBox<Integer>      cbPlaySpeed;
    private ObservableList<Integer> cbPlaySpeedList = FXCollections.observableArrayList(1,2,4);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        initializeTableView();

        cbRobotHeading.setValue(cbRobotHeadingList.get(0));
        cbRobotHeading.setItems(cbRobotHeadingList);
        cbTaskHeading.setValue(cbRobotHeadingList.get(0));
        cbTaskHeading.setItems(cbRobotHeadingList);
        cbPlaySpeed.setValue(cbPlaySpeedList.get(1));
        cbPlaySpeed.setItems(cbPlaySpeedList);


        txtTimeMax.setText(Integer.toString(Context.timeMax));

        final DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setColor(Color.GREY);
        scatterChart.setEffect(shadow);
    }

    public void btnSaveSimulationConfigClick(){
        try {
            int randomSeed = Integer.parseInt(txtRandomSeed.getText());
            int playSpeed  = cbPlaySpeed.getValue();
            int timeSolveMin  = Integer.parseInt(txtTimeSolveMin.getText());
            int loopMaxPrior = Integer.parseInt(txtTimeLoopMaxForPriorSearch.getText());

            random = new Random();
            random.setSeed(randomSeed);
            Context.playSpeed                     = playSpeed;
            MultiPathPlanning.Config.timeSolveMin = timeSolveMin;
            MultiPathPlanning.Config.timeLoopForPriorPlanMax = loopMaxPrior;

            MultiPathPlanning.Config.timeSolve = MultiPathPlanning.Config.timeSolveMin;
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Config are wrong");
        }

    }
    public void btnCreateMapBaseEmptyClick(){
        try {
            int xLength = Integer.parseInt(txtEmptyMapBaseXLength.getText());
            int yLength = Integer.parseInt(txtEmptyMapBaseYLength.getText());
            MapCreator.Bound.xLength = xLength;
            MapCreator.Bound.yLength = yLength;
            MapCreator.createEmpty();

            int lengthOfAxis = Math.max(Map.xLength,Map.yLength);
            xAxis.setUpperBound(lengthOfAxis);
            yAxis.setUpperBound(lengthOfAxis);

            mapData      = new MapData();
            robotCreator = new RobotCreator(random);
            taskCreator  = new TaskCreator(random);
            robotManager = new RobotManager(robotCreator, mapData);
            taskManager  = new TaskManager(taskCreator);

            initializeTimeline();

            updateAndViewAtTime(0);

        }
        catch (Exception e){
            viewErrorNotification("Some properties of Create Empty Map are wrong");
        }
    }


    public void btnSaveShelfConfigClick() {
        try {
            int shelfXLength         = Integer.parseInt(txtShelfXLength.getText());
            int shelfYLength         = Integer.parseInt(txtShelfYLength.getText());
            int shelfEachRowNumber   = Integer.parseInt(txtShelfEachRowNumber.getText());
            int shelfEachColNumber   = Integer.parseInt(txtShelfEachColNumber.getText());
            int distanceShelfToShelf = Integer.parseInt(txtDistanceShelfToShelf.getText());
            int distanceBoundToShelf = Integer.parseInt(txtDistanceBoundToShelf.getText());

            MapCreator.Shelf.xLength = shelfXLength;
            MapCreator.Shelf.yLength = shelfYLength;
            MapCreator.Shelf.xNumber = shelfEachRowNumber;
            MapCreator.Shelf.yNumber = shelfEachColNumber;
            MapCreator.Distance.shelfToHorShelf = distanceShelfToShelf;
            MapCreator.Distance.shelfToVerShelf = distanceShelfToShelf;
            MapCreator.Distance.boundToHorShelf = distanceBoundToShelf;
            MapCreator.Distance.boundToVerShelf = distanceBoundToShelf;
            MapCreator.create();

            int lengthOfAxis = Math.max(Map.xLength,Map.yLength);
            xAxis.setUpperBound(lengthOfAxis);
            yAxis.setUpperBound(lengthOfAxis);
            RobotCreator.Config.numberRobotMax = Map.xLength;

            mapData      = new MapData();
            robotCreator = new RobotCreator(random);
            taskCreator  = new TaskCreator(random);
            robotManager = new RobotManager(robotCreator, mapData);
            taskManager  = new TaskManager(taskCreator);

            initializeTimeline();

            updateAndViewAtTime(0);
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Map are wrong");
        }


    }
    public void btnCreateRobotClick() {
        try {
            int robotStartX = Integer.parseInt(txtRobotStartX.getText());
            int robotStartY = Integer.parseInt(txtRobotStartY.getText());
            PointInfo.Status robotStartHeading;

            if(cbRobotHeading.getValue().equals(cbRobotHeadingList.get(0))){
                robotStartHeading = PointInfo.Status.ROBOT_UP;
            }
            else if(cbRobotHeading.getValue().equals(cbRobotHeadingList.get(1))){
                robotStartHeading = PointInfo.Status.ROBOT_DOWN;
            }
            else if(cbRobotHeading.getValue().equals(cbRobotHeadingList.get(2))){
                robotStartHeading = PointInfo.Status.ROBOT_LEFT;
            }
            else {
                robotStartHeading = PointInfo.Status.ROBOT_RIGHT;
            }

            Point robotStartPoint = new Point(robotStartX, robotStartY, robotStartHeading);
            if (robotCreator.create(new Robot(robotStartPoint))) {
                updateAndViewAtTime(0);
            }
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Robot are wrong");
        }
    }
    public void btnCreateRobotRandomClick() {
        try {
            int robotNumOfRand  = Integer.parseInt(txtNumOfRandRobot.getText());
            robotCreator.createRandom(robotNumOfRand);
            updateAndViewAtTime(0);
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Robot are wrong");
        }

    }
    public void btnCreateTaskClick() {
        int taskTimeExecute = Integer.parseInt(txtTaskTimeExecute.getText());
        int taskTimeAppear  = Integer.parseInt(txtTaskTimeAppear.getText());

        PointInfo.Status taskGoalHeading;
        if(cbTaskHeading.getValue().equals(cbRobotHeadingList.get(0))){
            taskGoalHeading = PointInfo.Status.ROBOT_UP;
        }
        else if(cbTaskHeading.getValue().equals(cbRobotHeadingList.get(1))){
            taskGoalHeading = PointInfo.Status.ROBOT_DOWN;
        }
        else if(cbTaskHeading.getValue().equals(cbRobotHeadingList.get(2))){
            taskGoalHeading = PointInfo.Status.ROBOT_LEFT;
        }
        else {
            taskGoalHeading = PointInfo.Status.ROBOT_RIGHT;
        }

        int taskGoalX       = Integer.parseInt(txtTaskGoalX.getText());
        int taskGoalY       = Integer.parseInt(txtTaskGoalY.getText());
        Point taskGoalPoint    = new Point(taskGoalX,taskGoalY,taskGoalHeading);
        Point taskStationPoint = new Point(Integer.parseInt(txtTaskStationX.getText()),0, PointInfo.Status.ROBOT_DOWN);

        if (taskCreator.create(new Task(taskTimeExecute, taskTimeAppear, taskGoalPoint,taskStationPoint))) {
            updateAndViewAtTime(0);
        }

        try {

        }
        catch (Exception e){
            viewErrorNotification("Some properties of Task are wrong");
        }
    }
    public void btnCreateTaskRandomClick(){
        try {
            int taskNumOfRand  = Integer.parseInt(txtNumOfRandTask.getText());

            taskCreator.createRandom(taskNumOfRand);
            updateAndViewAtTime(0);
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Task are wrong");
        }
    }

    public void btnStartSimulationClick(){
        Context.simulating = !Context.simulating;
        if(Context.simulating){
            timeline.play();
            btnStartSimulation.setText("Stop simulation");

            btnCreateMapBase.setDisable(true);
            btnCreateRobot.setDisable(true);
            btnCreateRobotRandom.setDisable(true);
            btnCreateTask.setDisable(true);
            btnCreateTaskRandom.setDisable(true);
            btnSaveSystemConfig.setDisable(true);
            btnCreateMapBaseEmpty.setDisable(true);

        }
        else{
            timeline.pause();

            btnStartSimulation.setText("Resume simalation");
            btnCreateMapBase.setDisable(false);
            btnCreateRobot.setDisable(false);
            btnCreateRobotRandom.setDisable(false);
            btnCreateTask.setDisable(false);
            btnCreateTaskRandom.setDisable(false);
            btnSaveSystemConfig.setDisable(false);
            btnCreateMapBaseEmpty.setDisable(false);
        }
    }

    public void btnResetClick() {
        btnStartSimulation.setText("Start Simulation");
        txtTime.setText("0");
        txtStepCost.setText("0");
        txtRotateCost.setText("0");
        txtTaskNumber.setText("0");
        txtTaskDoneNumber.setText("0");
        timeline.stop();
        initializeTimeline();
        Context.reset();
        taskCreator.getTaskList().clear();
        robotCreator.getRobotList().clear();

        btnCreateMapBase.setDisable(false);
        btnCreateRobot.setDisable(false);
        btnCreateRobotRandom.setDisable(false);
        btnCreateTask.setDisable(false);
        btnCreateTaskRandom.setDisable(false);
        btnSaveSystemConfig.setDisable(false);
        btnCreateMapBaseEmpty.setDisable(false);
    }


    private void initializeTimeline(){
        MultiPathPlanning.Config.timeSolve = MultiPathPlanning.Config.timeSolveMin;
        timeline = new Timeline(new KeyFrame(Duration.millis(Context.getTimelineDurationMillis()), ev -> {
            if(taskManager.isAllTaskFinished()) {
                timeline.stop();
                Context.logData("timeRun of thread maximum in millis = " + Context.timeSolveMaxMillis);
            }
            Context.time++;
            Context.logData("\n@TIME = " + Context.time);
            updateAndViewAtTime(Context.time);
            updatePathCost(Context.time);

            if((!Context.solvingMultiPath) & (taskManager.assignable(Context.time, robotManager) | taskManager.returnnable(robotManager) )){
                Context.solvingMultiPath = true;

                int timeAssign = Context.time + MultiPathPlanning.Config.timeSolve;
                robotManager.updateByTime(timeAssign);

                solvingThread = new Timeline(new KeyFrame(Duration.seconds(MultiPathPlanning.Config.timeSolve), ev1 -> {
                    Context.timeStartThreadMillis = System.currentTimeMillis();


                    MultiPathPlanning multiPathPlanning = new MultiPathPlanning(taskManager,robotManager,mapData,timeAssign,random);
                    multiPathPlanning.execute();

                    taskManager.setLastTimeAssign(timeAssign);
                    Context.solvingMultiPath = false;
                    solvingThread.stop();
                    }));
                solvingThread.setCycleCount(1);
                solvingThread.playFrom(Duration.millis(MultiPathPlanning.Config.getTimeSolveMaxMillis()-1)); /*prevent Delay*/
            }



        }));
        timeline.setCycleCount(Context.timeMax-1);
    }

    private void initializeTableView(){
        tableColRobotID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColRobotX.setCellValueFactory(new PropertyValueFactory<>("x"));
        tableColRobotY.setCellValueFactory(new PropertyValueFactory<>("y"));
        tableColRobotHeading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tableColRobotTimeFree.setCellValueFactory(new PropertyValueFactory<>("timeFree"));
        tableColRobotStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColRobotTask.setCellValueFactory(new PropertyValueFactory<>("task"));
        tableViewRobotList.setItems(robotObservableList);

        tableColTaskID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColTaskGoalX.setCellValueFactory(new PropertyValueFactory<>("x"));
        tableColTaskStationX.setCellValueFactory(new PropertyValueFactory<>("stationX"));
        tableColTaskGoalY.setCellValueFactory(new PropertyValueFactory<>("y"));
        tableColTaskGoalHeading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tableColTaskTimeAppear.setCellValueFactory(new PropertyValueFactory<>("timeAppear"));
        tableColTaskTimeExecute.setCellValueFactory(new PropertyValueFactory<>("timeExecute"));
        tableColTaskStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewTaskList.setItems(taskObservableList);
    }
    private void viewTaskListToTableView() {
        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        for (Task task: taskCreator.getTaskList()) {
            TaskViewModel taskViewModel = new TaskViewModel(task);
            taskViewModelList.add(taskViewModel);
        }
        tableViewTaskList.getItems().clear();
        tableViewTaskList.getItems().addAll(taskViewModelList);
    }
    private void viewRobotListToTableViewAtTime(int time) {
        List<RobotViewModel> robotViewModelList = new ArrayList<>();
        for (Robot robot: robotCreator.getRobotList()) {
            RobotViewModel robotViewModel = new RobotViewModel(robot, time);
            robotViewModelList.add(robotViewModel);
        }
        tableViewRobotList.getItems().clear();
        tableViewRobotList.getItems().addAll(robotViewModelList);
    }
    private void viewMapToScatterAtTime(int time) {
        ScatterView scatterView = new ScatterView(mapData, taskManager, scatterChart);
        scatterView.display(time);
    }
    private void updateAndViewAtTime(int time) {
        taskManager.updateByTime(time);
        robotManager.updateByTime(time);
        viewTaskListToTableView();
        viewRobotListToTableViewAtTime(time);
        viewMapToScatterAtTime(time);

        txtTaskNumber.setText(Integer.toString(taskManager.getTaskList().size()));
        txtTaskDoneNumber.setText(Integer.toString(taskManager.getDoneTaskNumber()));
        txtTime.setText(Integer.toString(Context.time));
        txtStepCost.setText(Integer.toString(Context.stepCost));
        txtRotateCost.setText(Integer.toString(Context.rotateCost));


    }
    private void viewErrorNotification(String content){
        Alert alert = new Alert(Alert.AlertType.WARNING, content);
        alert.showAndWait();
    }
    private void updatePathCost(int time) {
        robotManager.updatePathCost(time);
        Context.stepCost = robotManager.getStepCost();
        Context.rotateCost = robotManager.getRotateCost();
        txtStepCost.setText(Integer.toString(Context.stepCost));
        txtRotateCost.setText(Integer.toString(Context.rotateCost));
    }
}
