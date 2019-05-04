package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private int id, type;
    private List<Point> pointList         = new ArrayList<>();

    private List<Point> mainPlanPointList = new ArrayList<>();
    private List<Point> subPlanPointList  = new ArrayList<>();
    private Task        task;

    private int lastTimeUpdateToMap = -1;

    public Robot(int type, Point start) {
        this.type = type;
        this.pointList.add(start.getPointClone());
    }

    /**
     * GETTER AND SETTER
     * */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
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
    public List<Point> getMainPlanPointList() {
        return mainPlanPointList;
    }
    public List<Point> getSubPlanPointList() {
        return subPlanPointList;
    }
    public void setSubPlanPointList(List<Point> subPlanPointList) {
        this.subPlanPointList = subPlanPointList;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }

    public Point getLastPointByPlan(){
        if(mainPlanPointList.size() == 0)
            return getLastPoint();
        else
            return mainPlanPointList.get(mainPlanPointList.size()-1);
    }
    public int getTimeFreeByPlan(){
        return getTimeFree()+mainPlanPointList.size();
    }



    public int getLastTimeBusy() {
        return pointList.size()-1;
    }
    public int getTimeFree(){
        return pointList.size();
    }

    public PointInfo.Status getHeadingByTime(int time) {
        return getPointByTime(time).getStatus();
    }
    public Point getPointByTime(int time){
        return pointList.get(time);
    }
    /**
     * USER DEFINE
     * */
    /*this create is for robot doesn't have any task so it does not move*/
    public void updateByTime(int time){
        Point lastPoint = pointList.get(getLastTimeBusy()).getPointClone();
        int   lastTime  = getLastTimeBusy();
        for (int i = 0; i < time - lastTime; i++) {
            pointList.add(lastPoint.getPointClone());
        }
    }

    public void assignMainPlanPointList() {
        pointList.addAll(mainPlanPointList);
    }



    public void assignTaskByPlan(){
        if(task != null){
            task.setStatus(Task.Status.RUNNING);
            task.setTimeFinish(getLastTimeBusy()+mainPlanPointList.size());
        }
    }


    public void unassignTask(){
        if(task != null){
            task.setTimeFinish(Integer.MAX_VALUE);
            task.setStatus(Task.Status.READY);
            task = null;
        }
    }

    public Point getLastPoint(){ return getPointByTime(getLastTimeBusy());}
}
