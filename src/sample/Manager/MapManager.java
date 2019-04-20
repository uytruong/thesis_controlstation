package sample.Manager;

import sample.Creator.MapBaseCreator;
import sample.Model.MapBase;
import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private MapBaseCreator mapBaseCreator;


    private List<MapBase> mapList = new ArrayList<>();
    public MapManager(MapBaseCreator mapBaseCreator) {
        this.mapBaseCreator = mapBaseCreator;
        init();
    }
    private void init(){
        for (int i = 0; i < Context.Time.timeMax; i++) {
            mapList.add(mapBaseCreator.getMapBaseClone());
        }
    }




    public List<MapBase> getMapList() {
        return mapList;
    }
    public MapBase getMap(int time){
        return mapList.get(time);
    }


}
