package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private int id, type;
    private List<Point> pointList        = new ArrayList<>();
    private List<Point> virtualPointList = new ArrayList<>();

    private int lastTimeUpdateToMap = -1;

    public Robot(int type, Point start) {
        this.type = type;
        this.pointList.add(start);
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
    public List<Point> getVirtualPointList() {
        return virtualPointList;
    }
    public void setVirtualPointList(List<Point> virtualPointList) {
        this.virtualPointList = virtualPointList;
    }

    public int getLastTimeBusy() {
        return pointList.size()-1;
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
        Point lastPoint = pointList.get(getLastTimeBusy()).getClone();
        int timeStart = getLastTimeBusy();
        for (int i = 0; i < time - timeStart; i++) {
            pointList.add(lastPoint.getClone());
        }
    }

    public void setVirtualPointsToReal() {
        pointList.addAll(virtualPointList);
        virtualPointList.clear();
    }

    public Point getLastPoint(){ return getPointByTime(getLastTimeBusy());}

}
