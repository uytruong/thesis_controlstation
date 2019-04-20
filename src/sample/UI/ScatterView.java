package sample.UI;

import javafx.collections.FXCollections;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Manager.MapManager;
import sample.Manager.TaskManager;
import sample.Model.MapBase;
import sample.Model.Constant;
import sample.Model.Point;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class ScatterView {
    private MapManager  mapManager;
    private TaskManager taskManager;
    private ScatterChart<Number, Number> mScatterChart;

    private Point[][] pointMatrix;

    private List<Task> mReadyTaskList = new ArrayList<>();
    private List<Task> mRunningTaskList = new ArrayList<>();
    private List<ScatterChart.Series<Number, Number>> dataSeriesList = new ArrayList<>();




    public ScatterView(MapManager mapManager, TaskManager taskManager, ScatterChart<Number, Number> scatterChart) {
        this.mapManager = mapManager;
        this.taskManager = taskManager;
        this.mScatterChart = scatterChart;
        initDataSeriesList();
    }

    public void initDataSeriesList() {
        for (int idx = 0; idx < 7; idx++) {
            dataSeriesList.add(new ScatterChart.Series<>());
        }
        dataSeriesList.get(SymbolViewPriority.ROBOT_RIGHT).setName("Robot Right");
        dataSeriesList.get(SymbolViewPriority.ROBOT_LEFT).setName("Robot Left");
        dataSeriesList.get(SymbolViewPriority.ROBOT_DOWN).setName("Robot Down");
        dataSeriesList.get(SymbolViewPriority.ROBOT_UP).setName("Robot Up");
        dataSeriesList.get(SymbolViewPriority.TASK_RUNNING).setName("Running Task");
        dataSeriesList.get(SymbolViewPriority.TASK_READY).setName("Ready Task");
        dataSeriesList.get(SymbolViewPriority.SHELF).setName("Shelf");
    }

    public void prepareDataToDisplay(int time) {
        // Prepare MapBase data
        MapBase mapBase = mapManager.getMap(time);
        pointMatrix = mapBase.getPointMatrix();
        for (int idx = 0; idx < (pointMatrix.length*pointMatrix[0].length); idx++) {
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(idx);
            yPoint = MapBase.getYFromId(idx);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            switch(pointMatrix[xPoint][yPoint].getStatus()) {
                case Constant.PointStatus.SHELF:
                    dataSeriesList.get(SymbolViewPriority.SHELF).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.ROBOT_UP:
                    dataSeriesList.get(SymbolViewPriority.ROBOT_UP).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.ROBOT_DOWN:
                    dataSeriesList.get(SymbolViewPriority.ROBOT_DOWN).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.ROBOT_LEFT:
                    dataSeriesList.get(SymbolViewPriority.ROBOT_LEFT).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.ROBOT_RIGHT:
                    dataSeriesList.get(SymbolViewPriority.ROBOT_RIGHT).getData().add(checkingPoint);
                    break;
                default:
                    break;
            }
        }
        // Prepare Task data
        mReadyTaskList = taskManager.getReadyTaskList();
        mRunningTaskList = taskManager.getRunningTaskList();
        for (int taskID = 0; taskID < mReadyTaskList.size(); taskID++) {
            int pointID = mReadyTaskList.get(taskID).getGoal().getId();
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            dataSeriesList.get(SymbolViewPriority.TASK_READY).getData().add(checkingPoint);
        }
        for (int taskID = 0; taskID < mRunningTaskList.size(); taskID++) {
            int pointID = mRunningTaskList.get(taskID).getGoal().getId();
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            dataSeriesList.get(SymbolViewPriority.TASK_RUNNING).getData().add(checkingPoint);
        }
    }

    public void display() {
        mScatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        //Setting the data to scatter chart
        for (ScatterChart.Series<Number, Number> dataSeries: dataSeriesList) {
            mScatterChart.getData().add(dataSeries);
        }

    }



    private class   SymbolViewPriority {
        /**
         * The scatter plot view data from idx = 0 to dataSeriesList.size()
         * The less priority value the more priority in view
         * */
        private static final int SHELF        = 0;
        private static final int TASK_READY   = 1;
        private static final int TASK_RUNNING = 2;
        private static final int ROBOT_UP     = 3;
        private static final int ROBOT_DOWN   = 4;
        private static final int ROBOT_LEFT   = 5;
        private static final int ROBOT_RIGHT  = 6;

    }
}
