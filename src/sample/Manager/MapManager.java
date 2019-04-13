package sample.Manager;

import sample.Creator.MapBaseCreator;
import sample.Model.Constant;
import sample.Model.MapBase;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private MapBaseCreator mapBaseCreator;
    private RobotManager   robotManager;
    private Context        context;


    private List<MapBase> mapList = new ArrayList<>();

    public MapManager(MapBaseCreator mapBaseCreator, RobotManager robotManager, Context context) {
        this.mapBaseCreator = mapBaseCreator;
        this.robotManager   = robotManager;
        this.context        = context;

        this.mapList.add(mapBaseCreator.getMapBaseClone());
    }
    public void updateMapList(){

    }


    public MapBaseCreator getMapBaseCreator() {
        return mapBaseCreator;
    }
    public void setMapBaseCreator(MapBaseCreator mapBaseCreator) {
        this.mapBaseCreator = mapBaseCreator;
    }
    public List<MapBase> getMapList() {
        return mapList;
    }
    public void setMapList(List<MapBase> mapList) {
        this.mapList = mapList;
    }
    public MapBase getMap(int time){
        return mapList.get(time);
    }


}
