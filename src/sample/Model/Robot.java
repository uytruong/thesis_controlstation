package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private int id, type;
    private List<Point> pointList = new ArrayList<>();
    private int lastTimeUpdateToMap = -1;

    private Task task;

    public Robot(int type, Point start) {
        this.type = type;
        this.pointList.add(start);
    }

    /*this create is for robot doesn't have any task so it does not move*/
    public void update(int timeUpdate){
        Point lastPoint = new Point(pointList.get(getLastTimeBusy()));
        for (int i = 0; i < timeUpdate - getLastTimeBusy(); i++) {
            pointList.add(new Point(lastPoint));
        }
    }

    public void addPointToPointList(Point point){
        pointList.add(new Point(point));
    }
    public void addPointListToPointList(List<Point> pointList){
        this.pointList.addAll(pointList);
    }



    public int getLastTimeBusy() {
        return pointList.size()-1;
    }
    public PointInfo.Status getHeading(int time) {
        return pointList.get(time).getStatus();
    }
    public Point getPoint(int time){
        return pointList.get(time);
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public int getLastTimeUpdateToMap() {
        return lastTimeUpdateToMap;
    }
    public void setLastTimeUpdateToMap(int lastTimeUpdateToMap) {
        this.lastTimeUpdateToMap = lastTimeUpdateToMap;
    }
    public List<Point> getPointList() {
        return pointList;
    }



}
