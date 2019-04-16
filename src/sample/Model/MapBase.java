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
}
