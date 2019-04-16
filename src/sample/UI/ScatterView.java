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
    private List<Task> mTaskList = new ArrayList<>();
    private List<ScatterChart.Series<Number, Number>> dataSeriesList = new ArrayList<>();

    public ScatterView(MapManager mapManager, TaskManager taskManager, ScatterChart<Number, Number> scatterChart) {
        this.mMapManager = mapManager;
        this.mTaskManager = taskManager;
        this.mScatterChart = scatterChart;
        initDataSeriesList();
    }

    public void initDataSeriesList() {
        for (int idx = 0; idx < 10; idx++) {
            dataSeriesList.add(new ScatterChart.Series<Number, Number>());
        }
    }

    public void PrepareDataToDisplay(int time) {
        initDataSeriesList();
        // Prepare MapBase data
        mMapBase = mMapManager.getMap(time);
        mPointStatusList = mMapBase.getStatusList();
        for (int pointID = 0; pointID < mPointStatusList.size(); pointID++) {
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            switch(mPointStatusList.get(pointID)) {
                case Constant.PointStatus.NONE:
                    break;
                case Constant.PointStatus.PERM:
                    dataSeriesList.get(0).setName("Shelf");
                    dataSeriesList.get(0).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.UP:
                    dataSeriesList.get(1).setName("Robot Up");
                    dataSeriesList.get(1).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.DOWN:
                    dataSeriesList.get(2).setName("Robot Down");
                    dataSeriesList.get(2).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.LEFT:
                    dataSeriesList.get(3).setName("Robot Left");
                    dataSeriesList.get(3).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.RIGHT:
                    dataSeriesList.get(4).setName("Robot Right");
                    dataSeriesList.get(4).getData().add(checkingPoint);
                    break;
                default:
                    break;
            }
        }
        // Prepare Task data
        mTaskList = mTaskManager.getTaskList();
        for (int taskID = 0; taskID < mTaskList.size(); taskID++) {
            int pointID = mTaskList.get(taskID).getGoal().getId();
            int goalPointStatus = mTaskList.get(taskID).getGoal().getStatus();
            int xPoint, yPoint;
            xPoint = MapBase.getXFromId(pointID);
            yPoint = MapBase.getYFromId(pointID);
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(xPoint, yPoint);
            switch(goalPointStatus) {
                case Constant.PointStatus.NONE:
                    break;
                case Constant.PointStatus.UP:
                    dataSeriesList.get(5).setName("Task Up");
                    dataSeriesList.get(5).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.DOWN:
                    dataSeriesList.get(6).setName("Task Down");
                    dataSeriesList.get(6).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.LEFT:
                    dataSeriesList.get(7).setName("Task Left");
                    dataSeriesList.get(7).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.RIGHT:
                    dataSeriesList.get(8).setName("Task Right");
                    dataSeriesList.get(8).getData().add(checkingPoint);
                    break;
                case Constant.PointStatus.DONTCARE:
                    dataSeriesList.get(9).setName("Task Don'tCare");
                    dataSeriesList.get(9).getData().add(checkingPoint);
                    break;
                default:
                    break;
            }
        }
    }

    public void DisplayScatterChart() {
        mScatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        //Setting the data to scatter chart
        for (int idx = 0; idx < dataSeriesList.size(); idx++) {
            mScatterChart.getData().add(dataSeriesList.get(idx));
        }
    }
}
