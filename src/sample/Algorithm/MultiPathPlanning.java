package sample.Algorithm;

import sample.Manager.Context;
import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiPathPlanning {
    private TaskManager  taskManager;
    private RobotManager robotManager;
    private MapData      mapData;
    private int          timeAssignment;
    private Random       random;

    private Conflict     conflict = new Conflict();

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;
    }

    private void clearData(){
        for (Robot robot: robotManager.getRobotList()) {
            robot.setTask(null);
            robot.clearMainPlanPointList();
            robot.clearSubPlanPointList();
        }
    }

    private void assignTasks(){
        Assignment assignment = new Assignment(robotManager, taskManager,timeAssignment);
        assignment.execute();

        Context.logData("#The tasks which are assigned before Optimize:");
        for (Robot robot: robotManager.getRobotList()) {
            if(robot.getTask() != null){
                Context.logData("----- robotId = " + robot.getId() + " assigned to taskId =" + robot.getTask().getId());
            }
        }
    }

    private void simplePlan(){
        Context.logData("simplePlan");
        /**
         * get simple plan for each robot
         * */
        for (Robot robot: robotManager.getRobotListWithTask()) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
            if(singlePathPlanning.execute())
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
            else{
                robot.setTask(null);
            }
        }

        /**
         * write plan points into maps to get the conflict paths
         * then erase the plan paths which are conflicted
         * */
        for (Robot robot: robotManager.getRobotListWithTask()) {
            List<Point> planPointList = robot.getMainPlanPointList();
            int         timeOffset    = robot.getTimeFree();
            Point       prePoint      = robot.getLastPoint();
            for (int idx = 0; idx < planPointList.size(); idx++) {
                Point point       = planPointList.get(idx);
                int   x           = point.getX();
                int   y           = point.getY();
                int   timeOfPoint = timeOffset + idx;
                Map   map         = mapData.getMapByTime(timeOfPoint);

                PointInfo thisPointThisTime = map.getPointInfoByXY(x,y);
                if(thisPointThisTime.isEmpty()){
                    map.setPointInfoByPoint(point);

                    if(!Point.isCoincident(point,prePoint)) {
                        int preX = prePoint.getX();
                        int preY = prePoint.getY();
                        Map preMap = mapData.getMapByTime(timeOfPoint - 1);
                        PointInfo prePointThisTime = map.getPointInfoByXY(preX, preY);
                        PointInfo thisPointPreTime = preMap.getPointInfoByXY(x, y);

                        if (!thisPointPreTime.isEmpty() & !prePointThisTime.isEmpty()) {
                            if (thisPointPreTime.getRobot() == prePointThisTime.getRobot()) {
                                conflict.add(thisPointPreTime.getRobot());
                            }
                        }
                    }
                }
                else{
                    conflict.add(thisPointThisTime.getRobot());
                }
                prePoint = point;
            }
        }

        for (Robot robot: robotManager.getRobotListWithTask()){
            if(conflict.exist(robot)){
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
                robot.clearMainPlanPointList();
            }
            else{
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
        }

    }
    private void priorPlan(){
        List<Robot> robotListConflicted = conflict.robotList;

        if(robotListConflicted.size()==0){
            System.out.println("#priorPlan: no conflict");
            return;
        }
        else
            System.out.println("#priorPlan: conficted...");

        for (Robot robot: robotListConflicted) {
            System.out.println("    ~robotId = "+robot.getId()+ " is conflicted");
        }

        int totalCostMin    = 0;

        for (Robot robot: robotListConflicted) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
            singlePathPlanning.execute();

            totalCostMin   += singlePathPlanning.getPathPlanningCost();

            robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
            mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());

        }

        for (Robot robot: robotListConflicted) {
            mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
        }

        //int timeLoop = Config.getTimeLoopForPriorPlan(robotListConflicted.size());
        int timeLoop = 5;
        for (int i = 0; i <  timeLoop; i++) {
            //Collections.shuffle(robotListConflicted,random);
            conflict.randomSwapElement(random);
            int totalCost   = 0;
            for (Robot robot: robotListConflicted) {
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                singlePathPlanning.execute();

                totalCost += singlePathPlanning.getPathPlanningCost();

                robot.setSubPlanPointList(singlePathPlanning.getPlanPointList());
                mapData.writePointsToMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }
            for (Robot robot: robotListConflicted) {
                mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            if(totalCost<=totalCostMin){
                totalCostMin   = totalCost;
                for (Robot robot: robotListConflicted) {
                    robot.setMainPlanPointList(robot.getSubPlanPointList());
                }
            }

        }

        for (Robot robot : robotListConflicted) {
            mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            robot.clearSubPlanPointList();
        }
    }

    private void optimizePlan(){
        List<Robot> robotList = robotManager.getRobotList();
        Robot       lastRobot = robotList.get(robotList.size()-1);
        boolean     loop      = true;

        System.out.println("#optimizePlan: checking...");
        while (loop){
            for (Robot robot: robotList) {
                Point lastPoint;
                int   lastX,lastY;
                int   timeFree;
                if(robot.getTask() != null) {
                    lastPoint = robot.getLastPointByPlan();
                    lastX     = lastPoint.getX();
                    lastY     = lastPoint.getY();
                    timeFree  = robot.getTimeFreeByPlan();
                }
                else{
                    lastPoint = robot.getLastPoint();
                    lastX     = lastPoint.getX();
                    lastY     = lastPoint.getY();
                    timeFree  = robot.getTimeFree();
                }

                Robot other    = null;
                for (int time = timeFree; time < Context.timeMax; time++) {
                    PointInfo pointInfo = mapData.getMapByTime(time).getPointInfoByXY(lastX,lastY);
                    if(!pointInfo.isEmpty()){
                        other = pointInfo.getRobot();
                        break;
                    }
                }

                if(other != null){
                    System.out.println("~robotId = " + robot.getId() + " may be crashed in the future by robotId = " + other.getId());
                    mapData.clearPointsFromMaps(other.getMainPlanPointList(),other.getTimeFree());
                    other.clearMainPlanPointList();
                    other.setTask(null);
                    break;
                }

                if (robot == lastRobot){
                    loop = false;
                }
            }

        }
    }




    public void execute(){
        clearData();
        assignTasks();
        simplePlan();
        priorPlan();

        optimizePlan();

        Context.logData("#The tasks which are assigned:");
        for (Robot robot: robotManager.getRobotList()) {
            if(robot.getTask() != null){
                robot.assignTask();
                Context.logData("   - robotId = " + robot.getId() + " assigned to taskId =" + robot.getTask().getId());
            }
        }



    }

    public static class Config{
        public static int timeSolveMax = 1;


        private static int timeLoopForPriorPlanMax = 5;
        private static int getTimeLoopForPriorPlan(int size){
            int timeLoopMax = timeLoopForPriorPlanMax;
            int timeLoop = 1;
            for (int i = 2; i <= size; i++) {
                timeLoop *=i;
            }
            timeLoop = timeLoop/2;
            timeLoop = (timeLoopMax<timeLoop)?timeLoopMax: timeLoop;
            return timeLoop;
        }

        public static int getTimeSolveMaxMillis(){
            return (timeSolveMax*1000);
        }
    }



    private class Conflict{
        private List<Robot> robotList = new ArrayList<>();

        private void add(Robot robot){
            if(!exist(robot))
                robotList.add(robot);
        }
        private boolean exist(Robot robot){
            for (Robot other: robotList) {
                if(robot == other)
                    return true;
            }
            return false;
        }

        private void randomSwapElement(Random random){
            int index1 = random.nextInt(robotList.size());
            int index2 = random.nextInt(robotList.size());
            Collections.swap(robotList,index1,index2);
        }
    }
}
