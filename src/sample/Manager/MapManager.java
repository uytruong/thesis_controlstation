package sample.Manager;

import sample.Creator.MapCreator;
import sample.Model.MapBase;
import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private MapCreator    mapCreator;
    private List<MapBase> mapList   = new ArrayList<>();

    public MapManager(MapCreator mapCreator) {
        this.mapCreator = mapCreator;
        init();
    }
    private void init(){
        for (int i = 0; i < Context.Time.timeMax; i++) {
            mapList.add(mapCreator.getMapBaseClone());
        }
    }




    public List<MapBase> getMapList() {
        return mapList;
    }
    public MapBase getMap(int time){
        return mapList.get(time);
    }


}
