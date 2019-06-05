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

import static sample.Model.Robot.Status.FREE;
import static sample.Model.Robot.Status.FULL;
import static sample.Model.Task.Status.REPORTING;

public class MultiPathPlanning {
    private TaskManager  taskManager;
    private RobotManager robotManager;
    private MapData      mapData;
    private int          timeAssignment;
    private Random       random;


    List<Robot> robotListForPlan;

    public MultiPathPlanning(TaskManager taskManager, RobotManager robotManager, MapData mapData, int timeAssignment, Random random) {
        this.taskManager    = taskManager;
        this.robotManager   = robotManager;
        this.mapData        = mapData;
        this.timeAssignment = timeAssignment;
        this.random         = random;


    }

    private void assignTasks(){
        Context.logData("[ASSIGN_PLAN]");
        if(taskManager.assignable(timeAssignment,robotManager)) {
            Assignment assignment = new Assignment(robotManager, taskManager);
            assignment.execute();
        }
    }

    private void simplePlan(){
        Context.logData("[SIMPLE_PLAN]");
        robotListForPlan = robotManager.getRobotListWithFullReportOrFreeTask();

        Context.logData("[LIST_OF_ROBOT_FOR_SIMPLE]");
        for (Robot robot : robotListForPlan) {
            Context.logData("___ ID = " + robot.getId() + " ___");
        }

        List<Robot> removeRobotList = new ArrayList<>();
        for (Robot robot: robotListForPlan) {
            Context.logData("   $$$ robotId = " + robot.getId() + " status: "+ robot.getStatus() + " is assigned to taskId = " + robot.getTask().getId());
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData,robotManager.getOtherRobotListExceptRobot(robotListForPlan,robot));
            if(singlePathPlanning.execute()){
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
                robot.setMainPlanSuccess(true);
            }
            else {
                removeRobotList.add(robot);
                Context.logData("[SIMPLE_PLAN IS FAIL]");
            }
        }
        for (Robot robot: removeRobotList) {
            if(robot.getStatus() == FREE)
                robot.setTask(null);
            robotListForPlan = robotManager.removeRobotFromRobotList(robotListForPlan,robot);
        }
    }

    private void priorPlan(){
        Context.logData("[CONFLICT CHECKING ...]");

        for (Robot robot: robotListForPlan){
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

        for (Robot robot: robotListForPlan){
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
            }
        }


        List<Robot> priorRobotList = robotManager.getRobotListWithoutPlan(robotListForPlan);
        if (priorRobotList.size() == 0){
            Context.logData("[PRIOR_PLAN: NO CONFLICT]");
            return;
        }
        Context.logData("[PRIOR_PLAN: EXIST CONFLICT]");

        int totalCostMin    = 0;
        int taskCpltMax     = 0;
        for (Robot robot: priorRobotList) {
            SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData, robotManager.getOtherRobotListExceptRobot(robotListForPlan,robot));
            if(singlePathPlanning.execute()) {
                taskCpltMax++;
                totalCostMin += singlePathPlanning.getPathPlanningCost();
                robot.setMainPlanSuccess(true);
                robot.setMainPlanPointList(singlePathPlanning.getPlanPointList());
            }
            else{
                robot.setMainPlanSuccess(false);
                robot.clearMainPlanPointList();
            }
            mapData.writePointsToMaps(robot.getMainPlanPointList(), robot.getTimeFree());
        }
        for (Robot robot: priorRobotList) {
            mapData.clearPointsFromMaps(robot.getMainPlanPointList(), robot.getTimeFree());
        }

        //int timeLoop = Math.min(Config.timeLoopForPriorPlanMax,priorRobotList.size());
        int timeLoop = Config.timeLoopForPriorPlanMax;
        Context.logData(" - number Of TimeLoop = " + timeLoop);
        for (int i = 0; i <  timeLoop; i++) {
            List<Robot> priorRobotListTemp  = getResortRobotList(priorRobotList);
            int totalCost   = 0;
            int taskCplt    = 0;
            for (Robot robot: priorRobotListTemp) {
                SinglePathPlanning singlePathPlanning = new SinglePathPlanning(robot,mapData, robotManager.getOtherRobotListExceptRobot(robotListForPlan,robot));
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

            for (Robot robot: priorRobotListTemp) {
                mapData.clearPointsFromMaps(robot.getSubPlanPointList(),robot.getTimeFree());
            }

            if((taskCplt>taskCpltMax) | ((taskCplt==taskCpltMax) & (totalCost<totalCostMin))){
                Context.logData("   - There is a better solution");
                priorRobotList = priorRobotListTemp;
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

        List<Robot> removeRobotList = new ArrayList<>();
        for (Robot robot : priorRobotList) {
            if(robot.isMainPlanSuccess()){
                mapData.writePointsToMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            else{
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
                removeRobotList.add(robot);
            }
        }
        for (Robot robot: removeRobotList) {
            if(robot.getStatus() == FREE)
                robot.setTask(null);
            robotListForPlan = robotManager.removeRobotFromRobotList(robotListForPlan,robot);
        }
    }

    private void optimizePlan() {
        Context.logData("[OPTIMIZE_PLAN]");

        boolean loop = true;
        List<Robot> robotList = robotManager.getRobotList();
        while (loop) {
            Context.logData("   %%% Start the LOOP1 ");
            int timeLoopMax = robotList.size();
            if(timeLoopMax == 0)
                break;

            int timeLoop = 0;
            for (Robot robot : robotList) {
                timeLoop ++;
                Point lastPoint = robot.getLastPoint();
                int   timeFree  = robot.getTimeFree();

                if((robot.getStatus() == FREE) & (robot.getTask() != null)){
                    lastPoint = robot.getLastPointByPlan();
                    timeFree  = robot.getTimeFreeByPlan();
                }
                else if(robot.getStatus() == FULL){
                    if(robot.getTask().getStatus() == REPORTING){
                        if(robot.isMainPlanSuccess()){
                            lastPoint = robot.getLastPointByPlan();
                            timeFree  = robot.getTimeFreeByPlan();
                        }
                    }
                }

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
                    Robot rmRobot = robot;
                    if((other.getStatus() == FREE) & (other.getTask() != null))
                        rmRobot = other;
                    else if (other.getStatus() == FULL){
                        if(other.getTask().getStatus() == REPORTING){
                            if(other.isMainPlanSuccess())
                                rmRobot = other;
                        }
                    }
                    Context.logData(" ~~~ " + robot.getStringInfo() + " may be crashed in the future by " + other.getStringInfo());
                    if(rmRobot == other)
                        Context.logData("remove Robot is other");
                    mapData.clearPointsFromMaps(rmRobot.getMainPlanPointList(), rmRobot.getTimeFree());
                    if(rmRobot.getStatus() == FREE)
                        rmRobot.setTask(null);
                    else
                        rmRobot.setMainPlanSuccess(false);
                    robotListForPlan = robotManager.removeRobotFromRobotList(robotListForPlan,rmRobot);
                    break;
                }
                if(timeLoop == timeLoopMax) {
                    loop = false;
                }
            }

        }


    }




    public void execute(){
        assignTasks();
        simplePlan();
        priorPlan();
        optimizePlan();
        Context.timeEndThreadMillis = System.currentTimeMillis();
        Context.timeRunThreadMillis = Context.timeEndThreadMillis - Context.timeStartThreadMillis;


        if(Context.timeRunThreadMillis > Context.timeSolveMaxMillis)
            Context.timeSolveMaxMillis = (int) Context.timeRunThreadMillis;


        if(Context.timeRunThreadMillis > (MultiPathPlanning.Config.timeSolve*1000/Context.playSpeed)){
            for (Robot robot: robotListForPlan) {
                mapData.clearPointsFromMaps(robot.getMainPlanPointList(),robot.getTimeFree());
            }
            MultiPathPlanning.Config.timeSolve++;
            Context.logData("time solve = " + MultiPathPlanning.Config.timeSolve);
            Context.logData("[OVERTIME_ERROR]");
        }
        else {
            Context.logData("[LIST OF BOUND TASK + ROBOT]");
            for (Robot robot : robotListForPlan) {
                Context.logData("___ robotID = " + robot.getId() + " & taskID = " + robot.getTask().getId());
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

        public static int getTimeSolveMaxMillis(){
            return (timeSolve *1000);
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
}
