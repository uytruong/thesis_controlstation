package sample.Model;

import sample.Creator.MapBaseCreator;

import java.util.ArrayList;
import java.util.List;

public class MapBase {
    private List<Integer> statusList = new ArrayList<>();
    

    public static int xLength, yLength;

    public MapBase() {
    }
    public MapBase(MapBaseCreator mapBaseCreator){
        this.statusList = new ArrayList<>(mapBaseCreator.getMapBase().getStatusList());
    }

    public List<Integer> getStatusList() {
        return statusList;
    }
    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }
    public int getStatus(int id){ return this.statusList.get(id);}
    public void setStatus(int id, int status){ this.statusList.set(id,status);}
    public int getStatus(Point point){ return this.statusList.get(point.getId());}



    public MapBase getClone(){
        MapBase mapBase = new MapBase();
        mapBase.setStatusList(new ArrayList<>(this.statusList));
        return mapBase;
    }

    public void printMapBase(){
        System.out.println("MapBase.xLength = " + xLength);
        System.out.println("MapBase.yLength = " + yLength);

        for (int i = yLength-1; i>= 0; i--) {
            for (int j = 0; j < xLength; j++) {
                System.out.print(statusList.get(getIdFromXY(j,i)) + "-");
            }
            System.out.println();
        }
    }



    public static int getIdFromXY(int x, int y){
        return (x + y*xLength);
    }
    public static int getXFromId(int id){
        return id%xLength;
    }
    public static int getYFromId(int id){
        return id/xLength;
    }
    public static int getEstimateCost(Point start,Point goal){
        int deltaX = getXFromId(start.getId()) - getXFromId(goal.getId());
        int deltaY = getYFromId(start.getId()) - getYFromId(goal.getId());
        return (Math.abs(deltaX)+Math.abs(deltaY));
    }

    public static Point getForwardPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        int h = point.getStatus();
        Point fwPoint = new Point(point);
        switch (h){
            case Constant.PointStatus.ROBOT_RIGHT:
                if(x == xLength){return null;}
                else{
                    fwPoint.setX(x+1);
                }
                break;
            case Constant.PointStatus.ROBOT_LEFT:
                if(x == 0){return null;}
                else{
                    fwPoint.setX(x-1);
                }
                break;
            case Constant.PointStatus.ROBOT_UP:
                if(y == yLength){return null;}
                else{
                    fwPoint.setY(y+1);
                }
                break;
            default:
                if(y == 0){return null;}
                else{
                    fwPoint.setY(y-1);
                }
                break;
        }
        return fwPoint;
    }


}
