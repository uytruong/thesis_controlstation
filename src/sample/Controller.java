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
    public TableColumn<TaskViewModel, Integer> tableColTaskX;
    public TableColumn<TaskViewModel, Integer> tableColTaskY;
    public TableColumn<TaskViewModel, String>  tableColTaskHeading;
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
    public TextField txtTimeSolve;
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
        cbPlaySpeed.setValue(cbPlaySpeedList.get(0));
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
            int timeSolve  = Integer.parseInt(txtTimeSolve.getText());
            int loopMaxPrior = Integer.parseInt(txtTimeLoopMaxForPriorSearch.getText());

            random.setSeed(randomSeed);
            Context.playSpeed                     = playSpeed;
            MultiPathPlanning.Config.timeSolve = timeSolve;
            MultiPathPlanning.Config.timeLoopForPriorPlanMax = loopMaxPrior;

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
        try {
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
            Point taskGoalPoint = new Point(taskGoalX,taskGoalY,taskGoalHeading);

            if (taskCreator.create(new Task(taskTimeExecute, taskTimeAppear, taskGoalPoint))) {
                updateAndViewAtTime(0);
            }
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
        timeline = new Timeline(new KeyFrame(Duration.millis(Context.getTimelineDurationMillis()), ev -> {
            if(taskManager.isAllTaskFinished()) {
                timeline.stop();
                Context.logData("timeRun of thread maximum in millis = " + Context.timeSolveMaxMillis);
            }
            Context.time++;
            Context.logData("\n@TIME = " + Context.time);
            updateAndViewAtTime(Context.time);
            updatePathCost(Context.time);

            if((!Context.solvingMultiPath) & (taskManager.assignable(Context.time, robotManager))){
                Context.solvingMultiPath = true;

                int timeAssign = Context.time + MultiPathPlanning.Config.timeSolve;
                robotManager.updateByTime(timeAssign);

                solvingThread = new Timeline(new KeyFrame(Duration.seconds(MultiPathPlanning.Config.timeSolve), ev1 -> {
                    long timeStartThread = System.currentTimeMillis();

                    taskManager.setLastTimeAssign(timeAssign);

                    MultiPathPlanning multiPathPlanning = new MultiPathPlanning(taskManager,robotManager,mapData,timeAssign,random);
                    multiPathPlanning.execute();

                    Context.solvingMultiPath = false;

                    long timeEndThread = System.currentTimeMillis();
                    long timeRunThread = timeEndThread - timeStartThread;
                    if(timeRunThread > Context.timeSolveMaxMillis)
                        Context.timeSolveMaxMillis = (int) timeRunThread;

                    Context.logData("Time of thread solving in millis: "+(timeRunThread));
                    if(timeRunThread > (MultiPathPlanning.Config.timeSolve *1000/Context.playSpeed)){
                        Context.logData("\n ============================\n ERROR \n ============================\n");
                    }
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
        tableColTaskX.setCellValueFactory(new PropertyValueFactory<>("x"));
        tableColTaskY.setCellValueFactory(new PropertyValueFactory<>("y"));
        tableColTaskHeading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        tableColTaskTimeAppear.setCellValueFactory(new PropertyValueFactory<>("timeAppear"));
        tableColTaskTimeExecute.setCellValueFactory(new PropertyValueFactory<>("timeExecute"));
        tableColTaskStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewTaskList.setItems(taskObservableList);
    }
    private void viewTaskListToTableView() {
        List<TaskViewModel> taskViewModelList = new ArrayList<>();
        for (Task task: taskCreator.getTaskList()) {
            int  taskIDView = task.getId();
            int  taskXView  = task.getGoal().getX();
            int  taskYView  = task.getGoal().getY();
            int  taskTimeAppearView  = task.getTimeAppear();
            int  taskTimeExecuteView = task.getTimeExecute();
            Task.Status taskStatus   = task.getStatus();

            PointInfo.Status taskHeading  = task.getGoal().getStatus();
            String taskHeadingView;
            switch (taskHeading){
                case ROBOT_UP:
                    taskHeadingView = "UP";
                    break;
                case ROBOT_DOWN:
                    taskHeadingView = "DOWN";
                    break;
                case ROBOT_LEFT:
                    taskHeadingView = "LEFT";
                    break;
                default:
                    taskHeadingView = "RIGHT";
            }

            String taskStatusView;
            switch (taskStatus){
                case NEW:
                    taskStatusView = "NEW";
                    break;
                case READY:
                    taskStatusView = "READY";
                    break;
                case BOUND:
                    taskStatusView = "BOUND";
                    break;
                case RUNNING:
                    taskStatusView = "RUNNING";
                    break;
                default:
                    taskStatusView = "DONE";
                    break;
            }
            TaskViewModel taskViewModel = new TaskViewModel(taskIDView, taskXView, taskYView, taskHeadingView, taskTimeAppearView, taskTimeExecuteView, taskStatusView);
            taskViewModelList.add(taskViewModel);
        }
        tableViewTaskList.getItems().clear();
        tableViewTaskList.getItems().addAll(taskViewModelList);
    }
    private void viewRobotListToTableViewAtTime(int time) {
        List<RobotViewModel> robotViewModelList = new ArrayList<>();
        for (Robot robot: robotCreator.getRobotList()) {
            int robotIDView   = robot.getId();
            int robotXView    = robot.getPointByTime(time).getX();
            int robotYView    = robot.getPointByTime(time).getY();
            int robotTimeFreeView = robot.getTimeFree();
            String robotHeadingView;
            switch (robot.getHeadingByTime(time)){
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

            String robotStatusView = "UNKNOWN";
            switch (robot.getStatus()){
                case FREE:
                    robotStatusView = "FREE";
                    break;
                case BUSY:
                    robotStatusView = "BUSY";
                    break;
            }
            String robotTaskView = "NONE";
            if(robot.getTask() != null)
                robotTaskView = Integer.toString(robot.getTask().getId());
            RobotViewModel robotViewModel = new RobotViewModel(robotIDView, robotXView, robotYView, robotHeadingView, robotTimeFreeView, robotStatusView, robotTaskView);
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
