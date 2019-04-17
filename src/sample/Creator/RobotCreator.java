package sample.Creator;

import sample.Manager.Context;
import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Point;
import sample.Model.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobotCreator {
    private Random      random;
    private MapBase     mapBase;
    private List<Robot> robotList = new ArrayList<>();
    private int         lastRobotNumber = 0;


    public RobotCreator(MapBaseCreator mapBaseCreator, Random random) {
         this.mapBase = mapBaseCreator.getMapBase();
         this.random  = random;
    }

    public boolean createRobot(Robot robot){
        /*
        if ((robot.getPointList().get(0).getStatus() != Constant.RobotPointStatus.DOWN) |
            (robot.getPointList().get(0).getStatus() != Constant.RobotPointStatus.UP) |
            (robot.getPointList().get(0).getStatus() != Constant.RobotPointStatus.LEFT) |
            (robot.getPointList().get(0).getStatus() != Constant.RobotPointStatus.RIGHT)){
            return false;
        }
        */

        for (Robot otherRobot: robotList) {
            if(otherRobot.getPoint(0).getId() == robot.getPoint(0).getId()){ return false;}
        }
        if (mapBase.getStatus(robot.getPoint(0).getId()) == Constant.PointStatus.NONE){
            robot.setId(robotList.size());
            robotList.add(robot);
            return true;
        }
        else {
            return false;
        }
    }

    public void createRobotRandom(int numberOfRobots, int typeOfRobots){
        if ((robotList.size() + numberOfRobots) >= Context.RobotCreator.numberRobotMax){
            numberOfRobots = Context.RobotCreator.numberRobotMax-robotList.size();
        }
        for (int i = 0; i < numberOfRobots; i++) {
            while (true){
                if (createRobot(new Robot(typeOfRobots, new Point(getRandomPointId(), getRandomPointStatus())))) {
                    break;
                }
            }
        }
    }

    private int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
    private int getRandomPointStatus(){
        return getRandomInt(Constant.RobotPointStatus.LEFT,Constant.RobotPointStatus.DOWN);
    }
    private int getRandomPointId(){
        return random.nextInt(mapBase.getStatusList().size());
    }


    public List<Robot> getRobotList() {
        return robotList;
    }

    public int getLastRobotNumber() {
        return lastRobotNumber;
    }
    public void setLastRobotNumber(int lastRobotNumber) {
        this.lastRobotNumber = lastRobotNumber;
    }
}
