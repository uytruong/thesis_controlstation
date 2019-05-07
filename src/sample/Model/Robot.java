package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private int         id;
    private List<Point> pointList         = new ArrayList<>();

    private List<Point> mainPlanPointList = new ArrayList<>();
    private List<Point> subPlanPointList  = new ArrayList<>();
    private Task        task;

    private int         lastTimeUpdateToMap = -1;

    public Robot(Point start) {
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
        return mainPlanPointList.get(mainPlanPointList.size()-1);
    }
    public PointInfo.Status getHeadingByTime(int time) {
        return getPointByTime(time).getStatus();
    }

    /*this create is for robot doesn't have any task so it does not move*/
    public void updateByTime(int time){
        Point lastPoint = getLastPoint();
        int   lastTime  = getLastTimeBusy();
        for (int i = 0; i < time - lastTime; i++) {
            pointList.add(lastPoint.getPointClone());
        }
    }


    public void assignTask(){
        pointList.addAll(mainPlanPointList);
        task.setStatus(Task.Status.RUNNING);
        task.setTimeFinish(getLastTimeBusy());
    }


}
