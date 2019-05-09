package sample.Model;
import sample.Creator.MapCreator;
import sample.Manager.Context;

import java.util.*;

public class Map {
    public static final int MAP_TYPE_EMPTY = 0;
    public static final int MAP_TYPE_WAREHOUSE = 1;

    public static int     xLength, yLength;
    public static int     type;
    private PointInfo[][] pointInfos;


    private PointInfo[][] getPointInfosClone(){
        PointInfo[][] clone = new PointInfo[pointInfos.length][pointInfos[0].length];
        for(int i = 0; i< pointInfos.length; i++){
            for(int j = 0; j< pointInfos[i].length; j++) {
                clone[i][j] = new PointInfo(pointInfos[i][j]);
            }
        }
        return clone;
    }
    /*** GETTER AND SETTER**/
    public PointInfo[][] getPointInfos() {
        return pointInfos;
    }
    public void setPointInfos(PointInfo[][] pointInfos) {
        this.pointInfos = pointInfos;
    }

    /**
     * GETTER AND SETTER
     * USER DEFINE
     * */
    public PointInfo getPointInfoByXY(int x, int y){
        if((x<0)|(x>xLength-1)|(y<0)|(y>yLength-1))
            return new PointInfo(PointInfo.Status.SHELF);
        else
            return pointInfos[x][y];
    }
    public PointInfo getPointInfoByPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        PointInfo pointInfo = new PointInfo(PointInfo.Status.SHELF);
        if((x<0)|(x>xLength-1)|(y<0)|(y>yLength-1))
            return pointInfo;
        else
            return pointInfos[x][y];
    }

    public void setPointInfoByPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        PointInfo pointInfo = getPointInfoByXY(x,y);
        pointInfo.setStatus(point.getStatus());
        pointInfo.setRobot(point.getRobot());
    }

    public Map getMapClone(){
        Map clone = new Map();
        clone.setPointInfos(getPointInfosClone());
        return clone;
    }
}
