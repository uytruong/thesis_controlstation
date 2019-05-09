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

    private Conflict     conflict;

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;

        this.conflict = new Conflict();
    }

    private void clearData(){
        for (Robot robot: robotManager.getRobotList()) {
            robot.abortTask();
        }
    }

    private void assignTasks(){
        Context.logData("### ### assignTasks");

        Assignment assignment = new Assignment(robotManager, taskManager, timeAssignment);
        assignment.execute();

        for (Robot robot: robotManager.getRobotListWithTask()) {
            Context.logData("   --- robotId = " + robot.getId() + " assigned to taskId =" + robot.getTask().getId());
        }
    }

    private void simplePlan(){
        Context.logData("### ### simplePlan");

        for (Robot robot: robotManager.getRobotListWithTask()) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
            if(singlePathPlanning.execute()){
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                robot.setMainPlanSuccess(true);
                Context.logData("  --- success with robot Id = " + robot.getId());
            }
            else{
                Context.logData("  --- fail with robot Id = " + robot.getId() + " then abort TaskId = " + robot.getTask().getId());
                robot.abortTask();
            }
        }
    }

    private void priorPlan(){
        Context.logData("### ### conflict checking");

        for (Robot robot: robotManager.getRobotListWithTask()) {
            Context.logData("   --- checking robotId = " + robot.getId());
            List<Point> planPointList = robot.getMainPlanPointList();
            int         timeOffset    = robot.getTimeFree();
            Point       prePoint      = robot.getLastPoint();
            for (int idx = 0; idx < planPointList.size(); idx++) {
                Point point       = planPointList.get(idx);
                int   timeOfPoint = timeOffset + idx;
                Map   map         = mapData.getMapByTime(timeOfPoint);

                if(mapData.isEmptyToMoveIn(prePoint,point,timeOfPoint)){
                    map.setPointInfoByPoint(point);
                }
                else{
                    robot.setMainPlanSuccess(false);
                    if(mapData.isEmpty(point,timeOfPoint)) {
                        map.getPointInfoByPoint(prePoint).getRobot().setMainPlanSuccess(false);
                    }
                    else{
                        map.getPointInfoByPoint(point).getRobot().setMainPlanSuccess(false);
                    }
                }
                prePoint = point;
            }
        }


        for (Robot robot: robotManager.getRobotListWithTask()){
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
                robot.clearMainPlanPointList();
                conflict.add(robot);
            }
        }
        List<Robot> robotListConflicted = conflict.robotList;

        if(robotListConflicted.size()==0){
            Context.logData("### ### priorPlan: no conflict");
            return;
        }

        Context.logData("### ### priorPlan search first plan");
        int totalCostMin    = 0;
        int taskCpltMax     = 0;
        for (Robot robot: robotListConflicted) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
            if(singlePathPlanning.execute()) {
                taskCpltMax++;
                totalCostMin += singlePathPlanning.getPathPlanningCost();
                robot.setMainPlanSuccess(true);
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                mapData.writePointsToMaps(robot.getMainPlanPointList(), robot.getTimeFree());
            }
            else{
                robot.setMainPlanSuccess(false);
            }
            Context.logData("$$$ robotId = " + robot.getId() + " success = " + robot.isMainPlanSuccess());
        }

        for (Robot robot: robotListConflicted) {
            if(robot.isMainPlanSuccess()){
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
            }
        }

        int timeLoop = Config.getTimeLoopForPriorPlan(robotListConflicted.size());
        Context.logData("time Loop for priorPlan is : " + timeLoop);
        for (int i = 0; i <  timeLoop; i++) {
            conflict.randomSwapElement(random);
            int totalCost   = 0;
            int taskCplt    = 0;
            for (Robot robot: robotListConflicted) {
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData);
                if(singlePathPlanning.execute()){
                    taskCplt++;
                    totalCost += singlePathPlanning.getPathPlanningCost();
                    robot.setSubPlanPointList(singlePathPlanning.getPlanPointList());
                    robot.setSubPlanSuccess(true);
                    mapData.writePointsToMaps(robot.getSubPlanPointList(),robot.getTimeFree());
                }
                else {
                    robot.setSubPlanSuccess(false);
                }
            }
            for (Robot robot: robotListConflicted) {
                if(robot.isSubPlanSuccess())
                    mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            if((taskCplt>taskCpltMax) | ((taskCplt==taskCpltMax) &(totalCost<totalCostMin))){
                Context.logData("There is better case for robotId");
                totalCostMin = totalCost;
                taskCpltMax  = taskCplt;
                for (Robot robot: robotListConflicted) {
                    Context.logData("robotId = " + robot.getId() + " sucess = " + robot.isSubPlanSuccess());
                    if(robot.isSubPlanSuccess()){
                        robot.setMainPlanPointList(robot.getSubPlanPointList());
                        robot.setMainPlanSuccess(true);
                    }
                    else{
                        robot.setMainPlanSuccess(false);
                    }
                }
            }

        }

        for (Robot robot : robotListConflicted) {
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                robot.abortTask();
            }
        }
    }

    private void optimizePlan(){
        Context.logData("### ### optimizePlan");
        List<Robot> robotList = robotManager.getRobotList();
        boolean     loop      = true;

        while (loop){
            Context.logData("   --- reloop again");
            int loopRequired = 0;
            for (Robot robot: robotList) {
                Context.logData("      +++ checking robotId =" + robot.getId());
                loopRequired++;
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

                Robot other = null;
                for (int time = timeFree; time < Context.timeMax; time++) {
                    PointInfo pointInfo = mapData.getMapByTime(time).getPointInfoByXY(lastX,lastY);
                    if(!pointInfo.isEmpty()){
                        other = pointInfo.getRobot();
                        break;
                    }
                }

                if(other != null){
                    Context.logData("~ robotId = " + robot.getId() + " may be crashed in the future by otherId = " + other.getId());
                    if(other.getTask() != null){
                        Context.logData("~ otherId = " + other.getId() + " have taskId = " + other.getTask().getId() + " is aborted");
                        mapData.clearPointsFromMaps(other.getMainPlanPointList(),other.getTimeFree());
                        other.abortTask();
                        break;
                    }
                    else{
                        if(robot.getTask() != null){
                            Context.logData("~ robotId = " + robot.getId() + " have taskId = " + robot.getTask().getId() + " is aborted");
                            mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                            robot.abortTask();
                            break;
                        }
                        else {
                            Context.logData("\n\n\n ERROR2 \n\n\n");
                        }
                    }
                }
                if(loopRequired == robotList.size()){
                    loop = false;
                }
            }
        }
    }

    public void execute(){
        clearData();
        assignTasks();
        if(Context.time == 12)
            Context.time = 12;

        simplePlan();

        priorPlan();
        optimizePlan();


        Context.logData("#The tasks which are assigned:");
        for (Robot robot: robotManager.getRobotList()) {
            if(robot.getTask() != null){
                robot.assignTask();
                robot.updateByTime(timeAssignment);
            }
        }

    }


    public static class Config{
        public static int timeSolveMax = 1;


        public static int timeLoopForPriorPlanMax = 5;
        public static int getTimeLoopForPriorPlan(int size){
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
