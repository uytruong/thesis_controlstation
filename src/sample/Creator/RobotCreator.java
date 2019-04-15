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

    public RobotCreator(MapBaseCreator mapBaseCreator, Random random) {
         this.mapBase = mapBaseCreator.getMapBase();
         this.random  = random;
    }

    public boolean createRobot(Robot robot){
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

    public void createRobotRandom(int numberOfRobot, int type){
        if ((robotList.size() + numberOfRobot) >= Context.RobotCreator.numberRobotMax){
            numberOfRobot = Context.RobotCreator.numberRobotMax-robotList.size();
        }
        for (int i = 0; i < numberOfRobot; i++) {
            while (true){
                if (createRobot(new Robot(type, new Point(getRandomRobotPointId(),getRandomRobotPointStatus())))) {
                    break;
                }
            }
        }
    }

    private int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
    private int getRandomRobotPointStatus(){
        return getRandomInt(Constant.RobotPointStatus.LEFT,Constant.RobotPointStatus.DOWN);
    }
    private int getRandomRobotPointId(){
        return random.nextInt(mapBase.getStatusList().size());
    }


    public List<Robot> getRobotList() {
        return robotList;
    }


}
