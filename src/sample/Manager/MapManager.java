package sample.Manager;

import sample.Creator.MapBaseCreator;
import sample.Model.MapBase;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private MapBaseCreator mapBaseCreator;


    private List<MapBase> mapList = new ArrayList<>();
    private int           lastTimeUpdate = -1;

    public MapManager(MapBaseCreator mapBaseCreator) {
        this.mapBaseCreator = mapBaseCreator;
        init();
    }
    private void init(){
        for (int i = 0; i < Context.Time.timeMax; i++) {
            mapList.add(mapBaseCreator.getMapBaseClone());
        }
    }




    public List<MapBase> getMapListClone(int offsetTime){
        List<MapBase> mapListClone = new ArrayList<>();
        for (int i = offsetTime; i < mapList.size(); i++) {
            mapListClone.add(mapList.get(i).getClone());
        }
        return mapListClone;
    }


    public List<MapBase> getMapList() {
        return mapList;
    }
    public MapBase getMap(int time){
        return mapList.get(time);
    }


}
