package sample.Creator;

import sample.Manager.Context;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobotCreator {
    private Random      random;
    private MapBase     mapBase;
    private List<Robot> robotList = new ArrayList<>();


    public RobotCreator(MapCreator mapCreator, Random random) {
         this.mapBase = mapCreator.getMapBase();
         this.random  = random;
    }

    public boolean create(Robot robot){
        if ((robot.getPoint(0).getStatus() != Constant.RobotPointHeading.DOWN) &
            (robot.getPoint(0).getStatus() != Constant.RobotPointHeading.UP)   &
            (robot.getPoint(0).getStatus() != Constant.RobotPointHeading.LEFT) &
            (robot.getPoint(0).getStatus() != Constant.RobotPointHeading.RIGHT)){
            return false;
        }

        for (Robot otherRobot: robotList) {
            if (Point.isCoincident(robot.getPoint(0), otherRobot.getPoint(0)))
                return false;
        }

        if (mapBase.getStatus(robot.getPoint(0).getX(), robot.getPoint(0).getY()) == Constant.PointStatus.NONE){
            robot.setId(robotList.size());
            robotList.add(robot);
            return true;
        }
        return false;
    }

    public void createRandom(int numberOfRobots, int typeOfRobots){
        if ((robotList.size() + numberOfRobots) >= Context.RobotCreator.numberRobotMax){
            numberOfRobots = Context.RobotCreator.numberRobotMax-robotList.size();
        }
        for (int i = 0; i < numberOfRobots; i++) {
            while (true){
                Point start = new Point(getRandomX(), getRandomY(), getRandomHeading());
                if (create(new Robot(typeOfRobots, start))) {
                    break;
                }
            }
        }
    }

    private int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
    private int getRandomHeading(){
        return getRandomInt(Constant.RobotPointHeading.LEFT, Constant.RobotPointHeading.DOWN);
    }
    private int getRandomX(){
        return random.nextInt(MapBase.xLength);
    }
    private int getRandomY(){
        return random.nextInt(MapBase.yLength);
    }


    public List<Robot> getRobotList() {
        return robotList;
    }

}
