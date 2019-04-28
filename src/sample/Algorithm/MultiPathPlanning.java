package sample.Algorithm;

import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.Map;
import sample.Model.Robot;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class MultiPathPlanning {
    private TaskManager  taskManager;
    private RobotManager robotManager;
    private MapData      mapData;
    private int          timeAssignment;

    private int[]        optimalAssignment;

    private List<Integer> conflictPathSaveByRobotId = new ArrayList<>();

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
    }

    public void calculateOptimalAssignment(){
        Assignment assignment = new Assignment(robotManager, taskManager,timeAssignment);
        optimalAssignment     = assignment.calculate();
    }

    public void singlePathPlanningForAll(){
        Map[] maps = mapData.getMapsClone();
        for (int robotId = 0; robotId < optimalAssignment.length; robotId++) {
            int taskId = optimalAssignment[robotId];
            if(taskId>=0){
                Robot robot = robotManager.getRobotById(robotId);
                Task  task  = taskManager.getTaskById(taskId);

                System.out.println(robotId + "-"+taskId);

                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(mapData,robot.getLastPoint(),task.getGoal(),timeAssignment);
                singlePathPlanning.calculate();
                singlePathPlanning.calculateCorrespondPoints();
                robot.setVirtualPointList(singlePathPlanning.getPointList());
                MapData.writePointsToMaps(maps,robot.getVirtualPointList(),timeAssignment);
                task.setTimeFinish(robot.getLastTimeBusy());
                taskManager.changeTaskStatus(task, Task.Status.RUNNING);
            }
        }
    }


    public void assignPathToRobot(){
        robotManager.setVirtualPointsToReal();
    }








    public static class Config{
        public static int timeSolve = 3;
    }

}
