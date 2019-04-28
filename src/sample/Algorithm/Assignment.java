package sample.Algorithm;

import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.Map;
import sample.Model.Robot;
import sample.Model.Task;
import sun.rmi.runtime.Log;

import java.util.List;

public class Assignment {
    private List<Task>  readyTaskList;
    private List<Robot> robotList;
    private int         timeAssignment;

    private double[][] costMatrix;
    private int[]      optimalAssignment;

    public Assignment(RobotManager robotManager, TaskManager taskManager, int timeAssignment) {
        this.robotList      = robotManager.getRobotList();
        this.readyTaskList  = taskManager.getReadyTaskList();
        this.timeAssignment = timeAssignment;
    }

    private void calculateCostMatrix(){
        int m = robotList.size();
        int n = readyTaskList.size();
        costMatrix = new double[m][n];

        /**
         * Get time offset Cost
         * */
        for (int i = 0; i < m ; i++) {
            for (int j = 0; j < n; j++) {
                costMatrix[i][j] = robotList.get(i).getLastTimeBusy() - timeAssignment;
            }
        }
        /**
         * Get estimate distance Cost
         * */
        for (int i = 0; i < m ; i++) {
            Robot robot = robotList.get(i);
            for (int j = 0; j < n; j++) {
                costMatrix[i][j] += Map.getEstimatePathCost(robot.getLastPoint(), readyTaskList.get(j).getGoal());
            }
        }
    }

    private void calculateOptimalAssignment(){
        Hungarian hungarian = new Hungarian(costMatrix);
        optimalAssignment   = hungarian.execute();

        /* convert index of readyTaskList into taskId */
        for (int robotId = 0; robotId < optimalAssignment.length; robotId++) {
            int taskId = optimalAssignment[robotId];
            if (taskId>=0)
                optimalAssignment[robotId] = readyTaskList.get(taskId).getId();
        }
    }

    public int[] calculate(){
        calculateCostMatrix();
        calculateOptimalAssignment();
        printResult();
        return optimalAssignment;
    }

    private void printResult(){
        for (int a: optimalAssignment) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

}
