package sample.Algorithm;

import sample.Creator.MapCreator;
import sample.Manager.Context;
import sample.Manager.MapData;
import sample.Manager.RobotManager;
import sample.Manager.TaskManager;
import sample.Model.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultiPathPlanning {
    private TaskManager  taskManager;
    private RobotManager robotManager;
    private MapData      mapData;
    private int          timeAssignment;
    private Random       random;

    private int          numberTaskAssignment;
    private int          numberTaskRemoveToMinimizeCost = 0;

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;
    }


    private void assignTasks(){
        Assignment assignment = new Assignment(robotManager, taskManager);
        assignment.execute();
    }

    private void simplePlan(){
        Context.logData("### ### SIMPLE PLAN");
        List<Robot> freeRobotListWithTask = robotManager.getFreeRobotListWithTask();
        numberTaskAssignment = freeRobotListWithTask.size();

        for (Robot robot: freeRobotListWithTask) {
            Context.logData("   $$$ robotId = " + robot.getId() + " is assigned to taskId = " + robot.getTask().getId());

            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData,robotManager.getBusyRobotList());
            if(singlePathPlanning.execute()){
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                robot.setMainPlanSuccess(true);
            }
            else{
                Context.logData("  --- simplePlan is fail");
                robot.setTask(null);
            }
        }
    }

    private void priorPlan(){
        Context.logData("### ### CONFLICT CHECKING");
        List<Robot> freeRobotListWithTask = robotManager.getFreeRobotListWithTask();

        for (Robot robot: freeRobotListWithTask){
            List<Point> planPointList = robot.getMainPlanPointList();
            int         timeOffset    = robot.getTimeFree();
            Point       prePoint      = robot.getLastPoint();
            for (int idx = 0; idx < planPointList.size(); idx++) {
                Point point       = planPointList.get(idx);
                int   timeOfPoint = timeOffset + idx;
                Map   map         = mapData.getMapByTime(timeOfPoint);

                if(!mapData.isEmptyToMoveIn(prePoint,point,timeOfPoint)){
                    robot.setMainPlanSuccess(false);
                    if(mapData.isEmpty(point,timeOfPoint)) {
                        Robot other = map.getPointInfoByPoint(prePoint).getRobot();
                        other.setMainPlanSuccess(false);
                    }
                    else{
                        Robot other = map.getPointInfoByPoint(point).getRobot();
                        other.setMainPlanSuccess(false);
                    }
                }
                if(mapData.isEmpty(point,timeOfPoint))
                    map.setPointInfoByPoint(point);
                prePoint = point;
            }
        }

        for (Robot robot: robotManager.getFreeRobotListWithTask()){
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
            }
        }


        List<Robot> priorRobotList = robotManager.getFreeRobotListWithTaskButNoPlan();
        if (priorRobotList.size() == 0){
            Context.logData("### ### PRIOR PLAN: NO CONFLICT");
            return;
        }

        int totalCostMin    = 0;
        int taskCpltMax     = 0;
        for (Robot robot: priorRobotList) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData, robotManager.getBusyRobotList());
            if(singlePathPlanning.execute()) {
                taskCpltMax++;
                totalCostMin += singlePathPlanning.getPathPlanningCost();
                robot.setMainPlanSuccess(true);
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
            }
            else{
                robot.setMainPlanSuccess(false);
                robot.clearMainPlanPointList();
                Context.logData("   --- robotId = " + robot.getId() + " fail");
            }
            mapData.writePointsToMaps(robot.getMainPlanPointList(), robot.getTimeFree());
        }
        for (Robot robot: priorRobotList) {
            mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
        }

        //int timeLoop = Config.getTimeLoopForPriorPlan(priorRobotList.size());
        int timeLoop = Math.min(Config.timeLoopForPriorPlanMax,priorRobotList.size());
        Context.logData(" $$$ PRIOR PLAN: time Loop for priorSearch = " + timeLoop);
        for (int i = 0; i <  timeLoop; i++) {
            priorRobotList  = getResortRobotList(priorRobotList);
            int totalCost   = 0;
            int taskCplt    = 0;
            for (Robot robot: priorRobotList) {
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData, robotManager.getBusyRobotList());
                if(singlePathPlanning.execute()){
                    taskCplt++;
                    totalCost += singlePathPlanning.getPathPlanningCost();
                    robot.setSubPlanPointList(singlePathPlanning.getPlanPointList());
                    robot.setSubPlanSuccess(true);
                }
                else {
                    robot.setSubPlanSuccess(false);
                    robot.clearSubPlanPointList();
                }
                mapData.writePointsToMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            for (Robot robot: priorRobotList) {
                mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            if((taskCplt>taskCpltMax) | ((taskCplt==taskCpltMax) & (totalCost<totalCostMin))){
                Context.logData("   --- There is a better result");
                totalCostMin = totalCost;
                taskCpltMax  = taskCplt;
                for (Robot robot: priorRobotList) {
                    Context.logData("   +++ robotId = " + robot.getId() + " success = " + robot.isSubPlanSuccess());
                    if(robot.isSubPlanSuccess()){
                        robot.setMainPlanPointList(robot.getSubPlanPointList());
                        robot.setMainPlanSuccess(true);
                    }
                    else{
                        robot.setMainPlanSuccess(false);
                        robot.clearMainPlanPointList();
                    }
                }
            }
        }

        for (Robot robot : priorRobotList) {
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                robot.setTask(null);
            }
        }
    }

    private void optimizePlan() {
        Context.logData("### ### OPTIMIZE PLAN");
        boolean superLoop = true;
        while(superLoop){
            Context.logData("   %%% Start the SUPER_LOOP");


            boolean loop1 = true;
            boolean doneWithoutErr1 = true;
            while (loop1) {
                Context.logData("   %%% Start the LOOP1 ");

                List<Robot> busyAndFreeWithoutTaskRobotList = robotManager.getBusyAndFreeWithoutTaskRobotList();
                int timeLoopMax = busyAndFreeWithoutTaskRobotList.size();
                if(timeLoopMax == 0)
                    break;

                int timeLoop = 0;
                for (Robot robot : busyAndFreeWithoutTaskRobotList) {
                    timeLoop ++;
                    Point lastPoint = robot.getLastPoint();
                    int   timeFree  = robot.getTimeFree();
                    Robot other     = null;
                    for (int time = timeFree; time < Context.timeMax; time++) {
                        Map       map       = mapData.getMapByTime(time);
                        PointInfo pointInfo = map.getPointInfoByPoint(lastPoint);
                        if (!pointInfo.isEmpty()) {
                            other = pointInfo.getRobot();
                            break;
                        }
                    }

                    if (other != null) {
                        Context.logData(" ~~~ " + robot.getStringInfo() + " may be crashed in the future by " + other.getStringInfo());
                        mapData.clearPointsFromMaps(other.getMainPlanPointList(), other.getTimeFree());
                        other.setTask(null);
                        doneWithoutErr1 = false;
                        break;
                    }
                    if(timeLoop == timeLoopMax) {
                        loop1 = false;
                    }
                }
            }


            boolean loop2 = true;
            boolean doneWithoutErr2 = true;
            while (loop2) {
                Context.logData("   %%% Start the LOOP2 ");

                List<Robot> freeRobotListWithTask = robotManager.getFreeRobotListWithTask();
                int timeLoopMax = freeRobotListWithTask.size();
                if(timeLoopMax == 0)
                    break;
                int timeLoop = 0;
                for (Robot robot : freeRobotListWithTask) {
                    timeLoop ++;
                    Point lastPoint = robot.getLastPointByPlan();
                    int timeFree = robot.getTimeFreeByPlan();
                    Robot other     = null;
                    for (int time = timeFree; time < Context.timeMax; time++) {
                        Map       map       = mapData.getMapByTime(time);
                        PointInfo pointInfo = map.getPointInfoByPoint(lastPoint);
                        if (!pointInfo.isEmpty()) {
                            other = pointInfo.getRobot();
                            break;
                        }
                    }
                    if (other != null) {
                        Context.logData("   +++ conflict: " + robot.getStringInfo());
                        if(other.isFreeAndHaveTask()){
                            mapData.clearPointsFromMaps(other.getMainPlanPointList(), other.getTimeFree());
                            other.setTask(null);
                        }
                        else{
                            mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                            robot.setTask(null);
                        }
                        doneWithoutErr2 = false;
                        break;
                    }
                    if(timeLoop == timeLoopMax) {
                        loop2 = false;
                    }
                }
            }

            if((doneWithoutErr1) & (doneWithoutErr2)){
                superLoop = false;
            }
        }
    }

    private void optimizePlanLv2(){
        int minCost = 10;
        List<Robot> freeRobotListWithTask = robotManager.getFreeRobotListWithTask();
        List<Robot> busyRobotList         = robotManager.getBusyRobotList();
        for (Robot frobot: freeRobotListWithTask) {
            for (Robot brobot: busyRobotList) {
                int costOfBusyRobot = minCost + brobot.getTimeFree() - timeAssignment + MapCreator.getEstimateAssignmentCost(brobot.getLastPoint(),frobot.getTask().getGoal());
                int costOfFreeRobot = getCostOfPlanPointList(frobot.getMainPlanPointList());
                if(costOfBusyRobot < costOfFreeRobot){
                    mapData.clearPointsFromMaps(frobot.getMainPlanPointList(),frobot.getTimeFree());
                    frobot.unbindTask();
                    numberTaskRemoveToMinimizeCost ++;
                    break;
                }
            }
        }


    }


    public void execute(){
        assignTasks();
        simplePlan();
        priorPlan();
        optimizePlanLv2();
        optimizePlan();

        List<Robot> freeRobotListWithTask = robotManager.getFreeRobotListWithTask();
        Context.timeEndThreadMillis = System.currentTimeMillis();
        Context.timeRunThreadMillis = Context.timeEndThreadMillis - Context.timeStartThreadMillis;


        if(Context.timeRunThreadMillis > Context.timeSolveMaxMillis)
            Context.timeSolveMaxMillis = (int) Context.timeRunThreadMillis;


        if(Context.timeRunThreadMillis > (MultiPathPlanning.Config.timeSolveMin *1000/Context.playSpeed)){
            for (Robot robot: freeRobotListWithTask) {
                if(robot.isMainPlanSuccess())
                    mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                robot.unbindTask();
            }
            MultiPathPlanning.Config.timeSolve++;
            Context.logData("CANNOT BIND TASK CAUSED BY OVERTIME");
        }
        else {
            Context.logData("### ### FIRST ASSIGNMENT: number of asisignments = " + numberTaskAssignment);
            Context.logData("### ### FIRST ASSIGNMENT: number of remove to reduce cost = " + numberTaskRemoveToMinimizeCost);
            numberTaskAssignment = freeRobotListWithTask.size();
            Context.logData("### ### FINAL ASSIGNMENT: number of asisignments = " + numberTaskAssignment);
            for (Robot robot : freeRobotListWithTask) {
                Context.logData("   ~~~ " + robot.getStringInfo());
                robot.bindTask();
            }
            MultiPathPlanning.Config.timeSolve = MultiPathPlanning.Config.timeSolveMin;
        }
        Context.logData("Time of thread solving in millis: "+(Context.timeRunThreadMillis));
    }


    public static class Config{
        public static int timeSolveMin = 2;
        public static int timeSolve = timeSolveMin;
        public static int timeLoopForPriorPlanMax = 5;

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
            return (timeSolveMin *1000);
        }
    }

    private List<Robot> getResortRobotList(List<Robot> robotList){
        /*int index1 = random.nextInt(robotList.size());
        int index2 = index1;
        while (index2 == index1)
            index2 = random.nextInt(robotList.size());
        Collections.swap(robotList,index1,index2);
        */
        Collections.shuffle(robotList,random);
        return robotList;
    }

    private int getCostOfPlanPointList(List<Point> pointList){
        /*
        int cost = 1;
        for (int i = 1; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            Point prePoint = pointList.get(i-1);
            if((!Point.isCoincident(point,prePoint)) | (!Point.isHeadingSameDirection(point,prePoint)))
                cost++;
        }
        return cost;
        */
        return pointList.size();
    }
}
