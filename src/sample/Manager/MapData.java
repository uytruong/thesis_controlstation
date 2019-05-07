package sample.Manager;

import sample.Creator.MapCreator;
import sample.Model.Map;
import sample.Model.Point;
import sample.Model.PointInfo;
import sample.Model.Robot;

import java.util.List;

public class MapData {
    private Map[] maps;

    public MapData() {
        this.maps = new Map[Config.numberMapMax];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = MapCreator.map.getMapClone();
        }
    }

    public void writePointsToMaps(List<Point> pointList, int timeWrite){
        for (int time = 0; time < pointList.size(); time++) {
            Point point = pointList.get(time);
            getMapByTime(time+timeWrite).setPointInfoByPoint(point);
        }
    }
    public void clearPointsFromMaps(List<Point> pointList, int timeClear){
        for (int time = 0; time < pointList.size(); time++) {
            Point point = pointList.get(time);
            getMapByTime(time+timeClear).getPointInfoByXY(point.getX(), point.getY()).setStatus(PointInfo.Status.NONE);
            getMapByTime(time+timeClear).getPointInfoByXY(point.getX(), point.getY()).setRobot(null);
        }

    }

    public boolean emptyToGo(Point prePoint, Point point, int timeOfPoint){
        Map map = getMapByTime(timeOfPoint);
        int x   = point.getX();
        int y   = point.getY();
        PointInfo thisPointThisTime = map.getPointInfoByXY(x,y);
        if(!thisPointThisTime.isEmpty())
            return false;
        if(!Point.isCoincident(prePoint,point)) {
            int preX = prePoint.getX();
            int preY = prePoint.getY();
            Map preMap = getMapByTime(timeOfPoint - 1);
            PointInfo prePointThisTime = map.getPointInfoByXY(preX, preY);
            PointInfo thisPointPreTime = preMap.getPointInfoByXY(x, y);

            if (!thisPointPreTime.isEmpty() & !prePointThisTime.isEmpty()) {
                Robot robot = thisPointPreTime.getRobot();
                return  !(robot == prePointThisTime.getRobot());
            }
        }
        return true;
    }

    public Map getMapByTime(int time){
        return maps[time];
    }


    private static class Config{
        private static int numberMapMax = Context.timeMax;
    }
}
