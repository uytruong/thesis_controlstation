package sample.UI;

import javafx.collections.FXCollections;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Manager.MapManager;
import sample.Manager.TaskManager;
import sample.Model.MapBase;
import sample.Model.Constant;

import java.util.ArrayList;
import java.util.List;

public class ScatterView {
    private MapBase mMapBase = new MapBase();
    private MapManager mMapManager;
    private TaskManager mTaskManager;
    private ScatterChart<Number, Number> mScatterChart;
    private List<Integer> mPointStatusList = new ArrayList<>();
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
//        ScatterChart.Series<Number, Number> series = new ScatterChart.Series<Number, Number>();
//        ScatterChart.Series<Number, Number> series1 = new ScatterChart.Series<Number, Number>();
//        ScatterChart.Series<Number, Number> series2 = new ScatterChart.Series<Number, Number>();
//        ScatterChart.Series<Number, Number> series3 = new ScatterChart.Series<Number, Number>();
//        ScatterChart.Series<Number, Number> series4 = new ScatterChart.Series<Number, Number>();
//        ScatterChart.Series<Number, Number> series5 = new ScatterChart.Series<Number, Number>();
//        series.setName("Option 1");
//        series1.setName("Option 2");
//        series2.setName("Option 3");
//        series3.setName("Option 4");
//        series4.setName("Option 5");
//        series5.setName("Option 6");
//
//        int xValue = 0;
//        int yValue = 0;
//        int count = 0;
//        series.getData().add(new ScatterChart.Data<Number, Number>(xValue + 1 + count, yValue + 1 + count));
//        series1.getData().add(new ScatterChart.Data<Number, Number>(xValue + 2 + count, yValue + 2 + count));
//        series2.getData().add(new ScatterChart.Data<Number, Number>(xValue + 3 + count, yValue + 3 + count));
//        series3.getData().add(new ScatterChart.Data<Number, Number>(xValue + 4 + count, yValue + 4 + count));
//        series4.getData().add(new ScatterChart.Data<Number, Number>(xValue + 5 + count, yValue + 5 + count));
//        series5.getData().add(new ScatterChart.Data<Number, Number>(xValue + 6 + count, yValue + 6 + count));
        initDataSeriesList();
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
    }

    public void DisplayScatterChart() {
        mScatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        //Setting the data to scatter chart
        for (int idx = 0; idx < dataSeriesList.size(); idx++) {
            mScatterChart.getData().add(dataSeriesList.get(idx));
        }
    }
}
