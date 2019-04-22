package sample.Manager;

import sample.Creator.MapCreator;
import sample.Model.MapBase;

public class MapManager {
    private MapCreator mapCreator;
    private MapBase[]  mapList;

    public MapManager(MapCreator mapCreator) {
        this.mapCreator = mapCreator;
        this.mapList    = new MapBase[Context.Time.timeMax];
        for (int i = 0; i < Context.Time.timeMax; i++) {
            mapList[i] = mapCreator.getMapBaseClone();
        }
    }




    public MapBase[] getMapList() {
        return mapList;
    }
    public MapBase getMap(int time){
        return mapList[time];
    }


}
