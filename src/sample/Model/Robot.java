package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot { ;
    private int id, type;
    private List<Point> pointList = new ArrayList<>();
    private Task task;

    private int lastTimeUpdateToMap = -1;

    public Robot(int type, Point start) {
        this.type = type;
        this.pointList.add(start);
        init();
    }

    private void init(){

    }

    /*this update is for robot doesn't have any task so it does not move*/
    public void update(int timeUpdate){
        if (getLastTimeBusy() <= timeUpdate) {
            Point lastPoint = new Point(pointList.get(getLastTimeBusy()));
            for (int i = 0; i < timeUpdate - getLastTimeBusy(); i++) {
                pointList.add(new Point(lastPoint));
            }
        }
    }

    public void addPoint(Point point){
        pointList.add(new Point(point));
    }



    public int getLastTimeBusy() {
        return pointList.size()-1;
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
    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    public Point getPoint(int time){ return pointList.get(time);}

    public void printInfo(){
        String info = "Robot id = " + id + "; type = " + type + "; start at: id = " + pointList.get(0).getId() + ", end at: id = " + pointList.get(getLastTimeBusy()).getId()
                      + "; timeAvailable = " + getLastTimeBusy();
        System.out.println(info);
    }


}
