package sample.UI;

import javafx.collections.FXCollections;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Manager.MapManager;
import sample.Manager.TaskManager;
import sample.Model.MapBase;
import sample.Model.Constant;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class ScatterView {
    private MapBase mMapBase = new MapBase();
    private MapManager mMapManager;
    private TaskManager mTaskManager;
    private ScatterChart<Number, Number> mScatterChart;
    private List<Integer> mPointStatusList = new ArrayList<>();
    private List<Task> mReadyTaskList = new ArrayList<>();
    private List<Task> mRunningTaskList = new ArrayList<>();
    private List<ScatterChart.Series<Number, Number>> dataSeriesList = new ArrayList<>();




    public ScatterView(MapManager mapManager, TaskManager taskManager, ScatterChart<Number, Number> scatterChart) {
        this.mMapManager = mapManager;
        this.mTaskManager = taskManager;
        this.mScatterChart = scatterChart;
        initDataSeriesList();
    }

    public void initDataSeriesList() {
        for (int idx = 0; idx < 7; idx++) {
            dataSeriesList.add(new ScatterChart.Series<Number, Number>());
        }
        dataSeriesList.get(SymbolViewPriority.ROBOT_RIGHT).setName("Robot Right");
        dataSeriesList.get(SymbolViewPriority.ROBOT_LEFT).setName("Robot Left");
        dataSeriesList.get(SymbolViewPriority.ROBOT_DOWN).setName("Robot Down");
        dataSeriesList.get(SymbolViewPriority.ROBOT_UP).setName("Robot Up");
        dataSeriesList.get(SymbolViewPriority.RUNNING_TASK).setName("Running Task");
        dataSeriesList.get(SymbolViewPriority.READY_TASK).setName("Ready Task");
        dataSeriesList.get(SymbolViewPriority.SHELF).setName("Shelf");
    }

    public void PrepareDataToDisplay(int time) {
        // Prepare MapBase data
        mMapBase = mMapManager.getMap(time);
        mPointStatusList = mMapBase.getStatusList();
        mMapBase.printMapBase();
        for (int pointID = 0; pointID < mPointStatusList.size(); pointID++) {
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            switch(mPointStatusList.get(pointID)) {
                case Constant.PointStatus.NONE:
                    break;
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
        mReadyTaskList = mTaskManager.getReadyTaskList();
        mRunningTaskList = mTaskManager.getRunningTaskList();
        for (int taskID = 0; taskID < mReadyTaskList.size(); taskID++) {
            int pointID = mReadyTaskList.get(taskID).getGoal().getId();
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            dataSeriesList.get(SymbolViewPriority.READY_TASK).getData().add(checkingPoint);
        }
        for (int taskID = 0; taskID < mRunningTaskList.size(); taskID++) {
            int pointID = mRunningTaskList.get(taskID).getGoal().getId();
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            dataSeriesList.get(SymbolViewPriority.RUNNING_TASK).getData().add(checkingPoint);
        }
    }

    public void DisplayScatterChart() {
        mScatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        //Setting the data to scatter chart
        for (int idx = 0; idx < dataSeriesList.size(); idx++) {
            mScatterChart.getData().add(dataSeriesList.get(idx));
        }
    }





    private class SymbolViewPriority {
        /**
         * The scatter plot view data from idx = 0 to dataSeriesList.size()
         * The less priority value the more priority in view
         * */
        private static final int SHELF = 0;
        private static final int READY_TASK   = 1;
        private static final int RUNNING_TASK = 2;
        private static final int ROBOT_UP     = 3;
        private static final int ROBOT_DOWN   = 4;
        private static final int ROBOT_LEFT   = 5;
        private static final int ROBOT_RIGHT  = 6;

    }
}
