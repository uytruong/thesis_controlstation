package sample.UI;

import javafx.collections.FXCollections;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Manager.MapManager;
import sample.Manager.TaskManager;
import sample.Model.PointInfo;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class ScatterView {
    private MapManager                   mapManager;
    private TaskManager                  taskManager;
    private ScatterChart<Number, Number> scatterChart;
    private List<ScatterChart.Series<Number, Number>> dataSeriesList = new ArrayList<>();


    public ScatterView(MapManager mapManager, TaskManager taskManager, ScatterChart<Number, Number> scatterChart) {
        this.mapManager   = mapManager;
        this.taskManager  = taskManager;
        this.scatterChart = scatterChart;
        initDataSeriesList();
    }

    private void initDataSeriesList() {
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
        PointInfo[][] pointInfoMatrices = mapManager.getMap(time).getPointInfoMatrix();

        for (int x = 0; x < pointInfoMatrices.length; x++) {
            for (int y = 0; y < pointInfoMatrices[0].length; y++) {
                ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(x, y);
                switch (pointInfoMatrices[x][y].getStatus()) {
                    case SHELF:
                        dataSeriesList.get(SymbolViewPriority.SHELF).getData().add(checkingPoint);
                        break;
                    case ROBOT_UP:
                        dataSeriesList.get(SymbolViewPriority.ROBOT_UP).getData().add(checkingPoint);
                        break;
                    case ROBOT_DOWN:
                        dataSeriesList.get(SymbolViewPriority.ROBOT_DOWN).getData().add(checkingPoint);
                        break;
                    case ROBOT_LEFT:
                        dataSeriesList.get(SymbolViewPriority.ROBOT_LEFT).getData().add(checkingPoint);
                        break;
                    case ROBOT_RIGHT:
                        dataSeriesList.get(SymbolViewPriority.ROBOT_RIGHT).getData().add(checkingPoint);
                        break;
                }
            }
        }

        // Prepare Task data
        List<Task> readyTaskList   = taskManager.getReadyTaskList();
        List<Task> runningTaskList = taskManager.getRunningTaskList();

        for (Task readyTask: readyTaskList) {
            int x = readyTask.getGoal().getX();
            int y = readyTask.getGoal().getY();
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(x, y);
            dataSeriesList.get(SymbolViewPriority.TASK_READY).getData().add(checkingPoint);
        }
        for (Task runningTask: runningTaskList) {
            int x = runningTask.getGoal().getX();
            int y = runningTask.getGoal().getY();
            ScatterChart.Data<Number, Number> checkingPoint = new ScatterChart.Data<Number, Number>(x, y);
            dataSeriesList.get(SymbolViewPriority.TASK_RUNNING).getData().add(checkingPoint);
        }

    }

    public void display() {
        scatterChart.setData(FXCollections.<XYChart.Series<Number, Number>>observableArrayList());
        //Setting the data to scatter chart
        for (ScatterChart.Series<Number, Number> dataSeries: dataSeriesList) {
            scatterChart.getData().add(dataSeries);
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
