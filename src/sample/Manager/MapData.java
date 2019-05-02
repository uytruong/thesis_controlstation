package sample.Manager;

import sample.Creator.MapCreator;
import sample.Model.Map;
import sample.Model.Point;
import sample.Model.PointInfo;

import java.util.List;

public class MapData {
    private Map[]      maps;

    public MapData(MapCreator mapCreator) {
        this.maps = new Map[Config.numberMapMax];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = mapCreator.getMapBaseClone();
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




    public Map[] getMaps() {
        return maps;
    }
    public Map[] getMapsClone(){
        Map[] clone = new Map[maps.length];
        for (int i = 0; i < maps.length; i++) {
            clone[i] = maps[i].getMapClone();
        }
        return clone;
    }
    public Map getMapByTime(int time){
        return maps[time];
    }

    public static class Config{
        public static int numberMapMax = Context.timeMax;
    }
}
