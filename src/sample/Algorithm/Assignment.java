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
                costMatrix[i][j] += MapBase.getEstimatePathCost(robot.getPoint(robot.getLastTimeBusy()), readyTaskList.get(j).getGoal());
            }
        }
    }


    public void calculation(int time){
        initializeCostMatrix(time);
        HungarianBipartiteMatching hungarian = new HungarianBipartiteMatching(costMatrix);
        assign = hungarian.execute();

        /**convert index of readyTaskList into taskId*/
        for (int i = 0; i < assign.length; i++) {
            if (assign[i]>=0)
                assign[i] = readyTaskList.get(assign[i]).getId();
        }

    }

    public void printResult(){
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix[0].length; j++) {
                System.out.print(costMatrix[i][j]+"-");
            }
            System.out.println();
        }
        for (int i = 0; i < assign.length; i++) {
            System.out.print(assign[i] + " - ");
        }
    }



    public int[] getAssign() {
        return assign;
    }
}
