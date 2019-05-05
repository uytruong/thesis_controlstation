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
import sample.Algorithm.MultiPathPlanning;
import sample.Creator.MapCreator;
import sample.Creator.RobotCreator;
import sample.Creator.TaskCreator;
import sample.Manager.Context;
import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;
import sample.UI.ScatterView;
import sample.UI.ViewModel.RobotViewModel;
import sample.UI.ViewModel.TaskViewModel;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private Random       random     = new Random();
    private MapCreator   mapCreator = new MapCreator();
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
    /**
     * Robot
     */
    public TextField txtRobotStartX;
    public TextField txtRobotStartY;
    public ChoiceBox cbRobotHeading;
    public Button    btnCreateRobot;
    public TextField txtNumOfRandRobot;
    public Button    btnCreateRobotRandom;

    public ObservableList<String> cbRobotHeadingList = FXCollections.observableArrayList(PointInfo.Status.getRobotHeadingStringList());
    /**
     * Task
     */
    public TextField txtTaskGoalX;
    public TextField txtTaskGoalY;
    public TextField txtTaskTimeAppear;
    public TextField txtTaskTimeExecute;
    public Button    btnCreateTask;
    public TextField txtNumOfRandTask;
    public Button    btnCreateTaskRandom;
    /**
     * Robot TableView
     */
    public TableView tableViewRobotList;
    public TableColumn<RobotViewModel, Integer> tableColRobotID;
    public TableColumn<RobotViewModel, Integer> tableColRobotX;
    public TableColumn<RobotViewModel, Integer> tableColRobotY;
    public TableColumn<RobotViewModel, String>  tableColRobotHeading;
    private ObservableList<RobotViewModel>      robotObservableList = FXCollections.observableArrayList();
    /**
     * Task TableView
     */
    public TableView tableViewTaskList;
    public TableColumn<TaskViewModel, Integer> tableColTaskID;
    public TableColumn<TaskViewModel, Integer> tableColTaskAppearTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskExecuteTime;
    public TableColumn<TaskViewModel, Integer> tableColTaskX;
    public TableColumn<TaskViewModel, Integer> tableColTaskY;
    public TableColumn<TaskViewModel, String>  tableColTaskStatus;
    private ObservableList<RobotViewModel>     taskObservableList = FXCollections.observableArrayList();
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
    public Button    btnStartSimulation;
    public Button    btnSaveSystemConfig;
    public Button    btnReset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        initializeTextField();
        initializeTableView();

        cbRobotHeading.setValue(cbRobotHeadingList.get(0));
        cbRobotHeading.setItems(cbRobotHeadingList);

    }

    public void btnSaveSimulationConfigClick(){
        try {
            int randomSeed = Integer.parseInt(txtRandomSeed.getText());
            int timeMax    = Integer.parseInt(txtTimeMax.getText());

            random.setSeed(randomSeed);
            Context.timeMax             = timeMax;
            MapData.Config.numberMapMax = timeMax;
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Config are wrong");
        }

    }

    public void btnSaveShelfConfigClick(ActionEvent event) {
        try {
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

            mapData      = new MapData(mapCreator);
            robotCreator = new RobotCreator(mapCreator, random);
            taskCreator  = new TaskCreator(mapCreator, random);
            robotManager = new RobotManager(robotCreator, mapData);
            taskManager  = new TaskManager(taskCreator);

            initializeTimeline();

            updateAndViewAtTime(0);
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Map are wrong");
        }
    }
    public void btnCreateRobotClick(ActionEvent event) {
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
    public void btnCreateRobotRandomClick(ActionEvent event) {
        try {
            int robotNumOfRand  = Integer.parseInt(txtNumOfRandRobot.getText());
            robotCreator.createRandom(robotNumOfRand);
            updateAndViewAtTime(0);
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Robot are wrong");
        }

    }
    public void btnCreateTaskClick(ActionEvent event) {
        try {
            int taskTimeExecute = Integer.parseInt(txtTaskTimeExecute.getText());
            int taskTimeAppear  = Integer.parseInt(txtTaskTimeAppear.getText());
            int taskGoalX       = Integer.parseInt(txtTaskGoalX.getText());
            int taskGoalY       = Integer.parseInt(txtTaskGoalY.getText());
            Point taskGoalPoint = new Point(taskGoalX,taskGoalY);

            if (taskCreator.create(new Task(taskTimeExecute, taskTimeAppear, taskGoalPoint))) {
                updateAndViewAtTime(0);
            }
        }
        catch (Exception e){
            viewErrorNotification("Some properties of Task are wrong");
        }
    }
    public void btnCreateTaskRandomClick(ActionEvent event){
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
        }
        else{
            timeline.pause();
            btnStartSimulation.setText("Resume simalation");
        }
    }

    public void btnResetClick(ActionEvent event) {
        Context.time = 0;
        initializeTimeline();
    }


    private void initializeTimeline(){
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            int time = Context.increaseTime();

            updateTaskAtTime(time);
            viewTaskListToTableView();
            updateRobotAtTime(time);
            viewRobotListToTableViewAtTime(time);

            if((!Context.solvingMultiPath) & (taskManager.isAssignable(time))){
                    Context.solvingMultiPath = true;
                    taskManager.setLastTimeAssign(time);
                    int timeAssign = time + MultiPathPlanning.Config.timeSolveMax;
                    updateRobotAtTime(timeAssign);

                    solvingThread = new Timeline(new KeyFrame(Duration.seconds(MultiPathPlanning.Config.timeSolveMax), ev1 -> {
                        long t1 = System.currentTimeMillis();
                        System.out.println("@@@@@START_THREAD");


                        MultiPathPlanning multiPathPlanning = new MultiPathPlanning(taskManager,robotManager,mapData,timeAssign,random);
                        multiPathPlanning.execute();
                        Context.solvingMultiPath = false;



                        long t2 = System.currentTimeMillis();
                        System.out.println("timeSolving in milisecond: "+(t2-t1));
                        System.out.println("@@@@@END_THREAD");

                    }));
                    solvingThread.setCycleCount(1);
                    solvingThread.playFrom(Duration.millis(MultiPathPlanning.Config.getTimeSolveMaxMillis()-1)); /*prevent Delay*/
                    System.out.println("@@@@@END");

            }


            viewMapToScatterAtTime(time);
            txtTime.setText(Integer.toString(Context.time));

        }));
        timeline.setCycleCount(Context.timeMax-1);
    }
    private void initializeTextField(){
        txtShelfXLength.setText("2");
        txtShelfYLength.setText("5");
        txtShelfEachRowNumber.setText("2");
        txtShelfEachColNumber.setText("1");
        txtDistanceShelfToShelf.setText("2");
        txtDistanceBoundToShelf.setText("1");

        txtNumOfRandRobot.setText("7");
        txtNumOfRandTask.setText("20");

        txtRandomSeed.setText("0");
        txtTimeMax.setText("300");
    }
    private void initializeTableView(){
        tableColRobotID.setCellValueFactory(new PropertyValueFactory<>("RobotID"));
        tableColRobotX.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointX"));
        tableColRobotY.setCellValueFactory(new PropertyValueFactory<>("RobotStartPointY"));
        tableColRobotHeading.setCellValueFactory(new PropertyValueFactory<>("RobotHeading"));
        tableViewRobotList.setItems(robotObservableList);

        tableColTaskID.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
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

            TaskViewModel taskViewModel = new TaskViewModel(taskIDView, taskXView, taskYView,
                                                            taskAppearTimeView, taskExecuteTimeView, taskStatusView);
            taskViewModelList.add(taskViewModel);
        }
        tableViewTaskList.getItems().clear();
        tableViewTaskList.getItems().addAll(taskViewModelList);
    }
    private void viewRobotListToTableViewAtTime(int time) {
        List<RobotViewModel> robotViewModelList = new ArrayList<>();
        for (int robotId = 0; robotId < robotManager.getRobotList().size(); robotId++) {
            Robot robot       = robotManager.getRobotById(robotId);
            int robotIDView   = robot.getId();
            int robotXView    = robot.getPointByTime(time).getX();
            int robotYView    = robot.getPointByTime(time).getY();
            PointInfo.Status robotHeading  = robot.getHeadingByTime(time);
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

            RobotViewModel robotViewModel = new RobotViewModel(robotIDView, robotXView, robotYView, robotHeadingView);
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
        updateTaskAtTime(time);
        viewTaskListToTableView();

        updateRobotAtTime(time);
        viewRobotListToTableViewAtTime(time);

        viewMapToScatterAtTime(time);
    }
    private void viewErrorNotification(String content){
        Alert alert = new Alert(Alert.AlertType.WARNING, content);
        alert.showAndWait();
    }



    private void updateRobotAtTime(int time){robotManager.updateByTime(time);}
    private void updateTaskAtTime(int time){taskManager.updateByTime(time);}
}
