package sample.Algorithm;

import sample.Creator.MapCreator;
import sample.Manager.Context;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.Robot;
import sample.Model.Task;

import java.util.List;

public class Assignment {
    private List<Task>  readyTaskList;
    private List<Robot> freeRobotList;

    private double[][] costMatrix;
    private int[]      assignment;

    public Assignment(RobotManager robotManager, TaskManager taskManager) {
        this.freeRobotList  = robotManager.getFreeRobotList();
        this.readyTaskList  = taskManager.getReadyTaskList();
    }

    private void calculateCostMatrix(){
        int m = freeRobotList.size();
        int n = readyTaskList.size();
        costMatrix = new double[m][n];

        for (int i = 0; i < m ; i++) {
            Robot robot = freeRobotList.get(i);
            for (int j = 0; j < n; j++) {
                costMatrix[i][j] += MapCreator.getEstimateAssignmentCost(robot.getLastPoint(), readyTaskList.get(j).getGoal());
            }
        }
    }

    private void assignment(){
        Hungarian hungarian = new Hungarian(costMatrix);
        assignment          = hungarian.execute();

        /* convert index of readyTaskList into taskId */
        for (int robotId = 0; robotId < assignment.length; robotId++) {
            Robot robot = freeRobotList.get(robotId);
            int taskIdx = assignment[robotId];
            if (taskIdx>=0){
                Task task = readyTaskList.get(taskIdx);
                robot.setTask(task);
            }
            else{
                robot.setTask(null);
            }
        }
    }

    public void execute(){
        calculateCostMatrix();
        assignment();
    }

}
