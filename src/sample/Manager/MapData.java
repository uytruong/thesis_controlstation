package sample.Manager;

import sample.Creator.MapCreator;
import sample.Model.Map;
import sample.Model.Point;
import sample.Model.PointInfo;
import sample.Model.Robot;

import java.util.List;

public class MapData {
    private Map[]      maps;

    public MapData(MapCreator mapCreator) {
        this.maps = new Map[Config.numberMapMax];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = mapCreator.getMapBaseClone();
        }
    }


    public static void writePointsToMaps(Map[] maps,List<Point> pointList, int timeWrite){
        for (int time = 0; time < pointList.size(); time++) {
            Point point = pointList.get(time);
            maps[time+timeWrite].setPointInfoByPoint(point);
        }
    }




    public Map[] getMaps() {
        return maps;
    }
    public Map[] getMapsClone(){
        return maps.clone();
    }
    public Map   getMapByTime(int time){
        return maps[time];
    }

    public static class Config{
        public static int numberMapMax = Context.timeMax;
    }
}
