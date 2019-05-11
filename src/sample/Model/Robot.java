package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    public enum Status{
        FREE,
        BUSY,
    }

    private int         id;
    private Status      status              = Status.FREE;
    private List<Point> pointList           = new ArrayList<>();
    private int         lastTimeUpdateToMap = -1;

    private List<Point> mainPlanPointList = new ArrayList<>();
    private List<Point> subPlanPointList  = new ArrayList<>();
    private Task        task;
    private boolean     mainPlanSuccess   = true;
    private boolean     subPlanSuccess    = true;


    public Robot(Point start) {
        this.pointList.add(start.getPointClone());
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getLastTimeUpdateToMap() {
        return lastTimeUpdateToMap;
    }
    public void setLastTimeUpdateToMap(int lastTimeUpdateToMap) {
        this.lastTimeUpdateToMap = lastTimeUpdateToMap;
    }

    public void setMainPlanPointList(List<Point> mainPlanPointList) {
        this.mainPlanPointList = mainPlanPointList;
    }
    public void setSubPlanPointList(List<Point> subPlanPointList) {
        this.subPlanPointList = subPlanPointList;
    }
    public List<Point> getMainPlanPointList() {
        return mainPlanPointList;
    }
    public List<Point> getSubPlanPointList() {
        return subPlanPointList;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public boolean isMainPlanSuccess() {
        return mainPlanSuccess;
    }
    public void setMainPlanSuccess(boolean mainPlanSuccess) {
        this.mainPlanSuccess = mainPlanSuccess;
    }
    public boolean isSubPlanSuccess() {
        return subPlanSuccess;
    }
    public void setSubPlanSuccess(boolean subPlanSuccess) {
        this.subPlanSuccess = subPlanSuccess;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * USER DEFINE
     * */

    public void clearMainPlanPointList(){
        this.mainPlanPointList = new ArrayList<>();
    }
    public void clearSubPlanPointList(){
        this.subPlanPointList = new ArrayList<>();
    }

    public int getLastTimeBusy() {
        return pointList.size()-1;
    }
    public int getTimeFree(){
        return pointList.size();
    }
    public int getTimeFreeByPlan(){
        return getTimeFree()+ mainPlanPointList.size();
    }

    public Point getPointByTime(int time){
        return pointList.get(time);
    }
    public Point getLastPoint(){ return getPointByTime(getLastTimeBusy());}
    public Point getLastPointByPlan(){
        int size = mainPlanPointList.size();
        if (size == 0)
            return getLastPoint();
        else
            return mainPlanPointList.get(size-1);
    }

    public PointInfo.Status getHeadingByTime(int time) {
        return getPointByTime(time).getStatus();
    }

    public void updateByTime(int time){
        Point lastPoint = getLastPoint();
        int   lastTime  = getLastTimeBusy();
        for (int i = 0; i < time - lastTime; i++) {
            pointList.add(lastPoint.getPointClone());
        }
    }

    public void bindTask(){

        pointList.addAll(mainPlanPointList);
        status = Status.BUSY;

        task.setStatus(Task.Status.BOUND);
        task.setTimeArrived(getLastTimeBusy());
        task.setRobot(this);
    }

    public boolean isFreeAndHaveTask(){
        return (status == Status.FREE) & (task != null);
    }

    public String getStringInfo(){
        String taskId = (task == null)? "null" : Integer.toString(task.getId());
        return "robotId = " + id + ", task = " + taskId + ", status = " + status + " planSuccess = " + mainPlanSuccess;
    }

    public void unbindTask(){
        status = Status.FREE;
        task   = null;
        mainPlanSuccess = false;
        clearMainPlanPointList();
        clearSubPlanPointList();
    }
}
