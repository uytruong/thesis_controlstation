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
        for (int idx = 0; idx < pointList.size(); idx++) {
            Point point = pointList.get(idx);
            getMapByTime(idx+timeWrite).setPointInfoByPoint(point);
        }
    }
    public void clearPointsFromMaps(List<Point> pointList, int timeClear){
        for (int idx = 0; idx < pointList.size(); idx++) {
            Point point = pointList.get(idx);
            getMapByTime(idx+timeClear).getPointInfoByXY(point.getX(), point.getY()).setStatus(PointInfo.Status.NONE);
            getMapByTime(idx+timeClear).getPointInfoByXY(point.getX(), point.getY()).setRobot(null);
        }

    }

    public boolean isEmpty(Point point, int timeOfPoint){
        Map map = getMapByTime(timeOfPoint);
        int x   = point.getX();
        int y   = point.getY();
        PointInfo infoOfPointInMap = map.getPointInfoByXY(x,y);
        return (infoOfPointInMap.isEmpty());
    }
    public boolean isEmptyToMoveIn(Point prePoint, Point point, int timeOfPoint){
        if(isEmpty(point,timeOfPoint))
        {
            if((!isEmpty(prePoint,timeOfPoint)) & (!isEmpty(point,timeOfPoint-1))){
                Robot other = getMapByTime(timeOfPoint).getPointInfoByPoint(prePoint).getRobot();
                if(other == getMapByTime(timeOfPoint-1).getPointInfoByPoint(point).getRobot()) {
                    return false;
                }
            }
            else
                return true;
        }
        return false;
    }


    public Map getMapByTime(int time){
        return maps[time];
    }

    private static class Config{
        private static int numberMapMax = Context.timeMax;
    }
}
