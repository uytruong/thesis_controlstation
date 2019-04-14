package sample.Manager;

import sample.Creator.MapBaseCreator;
import sample.Model.MapBase;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;

public class MapManager {


    private MapBaseCreator mapBaseCreator;
    private RobotManager   robotManager;


    private List<MapBase> mapList = new ArrayList<>();
    private int           lastTimeUpdate = -1;

    public MapManager(MapBaseCreator mapBaseCreator, RobotManager robotManager) {
        this.mapBaseCreator = mapBaseCreator;
        this.robotManager   = robotManager;
        init();
    }
    private void init(){
        for (int i = 0; i < Context.Time.timeMax; i++) {
            mapList.add(mapBaseCreator.getMapBaseClone());
        }
    }

    public void update(int timeUpdate){
        for (Robot robot: robotManager.getRobotList()) {
            if (robot.getLastTimeBusy() <= timeUpdate) {
                robot.update(timeUpdate);
                for (int i = lastTimeUpdate+1; i <= timeUpdate; i++) {
                    mapList.get(i).setStatus(robot.getPoint(i).getId(), robot.getPoint(i).getStatus());
                }
                robot.setLastTimeUpdateToMap(timeUpdate);
            }
            else{
                for (int i = robot.getLastTimeUpdateToMap()+1; i <= robot.getLastTimeBusy(); i++) {
                    mapList.get(i).setStatus(robot.getPoint(i).getId(),robot.getPoint(i).getStatus());
                }
                robot.setLastTimeUpdateToMap(robot.getLastTimeBusy());
            }
        }
        lastTimeUpdate = timeUpdate;
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
