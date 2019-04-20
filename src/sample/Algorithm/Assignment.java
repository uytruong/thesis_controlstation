package sample.Algorithm;

import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.MapBase;
import sample.Model.Robot;
import sample.Model.Task;

import java.util.List;

public class Assignment {
    private double[][] costMatrix;
    private int[]      assign;

    private List<Task>  readyTaskList;
    private List<Robot> robotList;


    public Assignment(RobotManager robotManager, TaskManager taskManager) {
        this.robotList     = robotManager.getRobotList();
        this.readyTaskList = taskManager.getReadyTaskList();
    }

    private void initializeCostMatrix(int time){
        costMatrix = new double[robotList.size()][readyTaskList.size()];

        /**
         * Get time offset Cost
         * */
        for (int i = 0; i < robotList.size() ; i++) {
            for (int j = 0; j < readyTaskList.size(); j++) {
                costMatrix[i][j] = robotList.get(i).getLastTimeBusy() - time;
            }
        }
        /**
         * Get distance Cost
         * */
        for (int i = 0; i < robotList.size() ; i++) {
            Robot robot = robotList.get(i);
            for (int j = 0; j < readyTaskList.size(); j++) {
                costMatrix[i][j] += MapBase.getEstimateCost(robot.getPoint(robot.getLastTimeBusy()), readyTaskList.get(j).getGoal());
            }
        }
    }


    public void execute(int time){
        initializeCostMatrix(time);
        HungarianBipartiteMatching hungarian = new HungarianBipartiteMatching(costMatrix);
        assign = hungarian.execute();
    }

    public void printCostMatrix(){
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix[0].length; j++) {
                System.out.println(costMatrix[i][j]+"-");
            }
            System.out.println();
        }
    }


    public int[] getAssign() {
        return assign;
    }
}
