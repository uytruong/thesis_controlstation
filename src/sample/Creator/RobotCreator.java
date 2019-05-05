package sample.Creator;

import sample.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobotCreator {
    private Random      random;
    private final Map   map;
    private List<Robot> robotList = new ArrayList<>();


    public RobotCreator(MapCreator mapCreator, Random random) {
         this.map     = mapCreator.getMap();
         this.random  = random;
    }

    public boolean create(Robot robot){
        if ((robot.getPointByTime(0).getStatus() != PointInfo.Status.ROBOT_DOWN) &
            (robot.getPointByTime(0).getStatus() != PointInfo.Status.ROBOT_UP)   &
            (robot.getPointByTime(0).getStatus() != PointInfo.Status.ROBOT_LEFT) &
            (robot.getPointByTime(0).getStatus() != PointInfo.Status.ROBOT_RIGHT)){
            return false;
        }

        for (Robot otherRobot: robotList) {
            if (Point.isCoincident(robot.getPointByTime(0), otherRobot.getPointByTime(0)))
                return false;
        }

        if (map.getPointInfoByXY(robot.getPointByTime(0).getX(), robot.getPointByTime(0).getY()).isEmpty()){
            robot.setId(robotList.size());
            robot.getPointByTime(0).setRobot(robot);
            robotList.add(robot);
            return true;
        }
        return false;
    }

    public void createRandom(int numberOfRobots){
        if ((robotList.size() + numberOfRobots) >= Config.numberRobotMax){
            numberOfRobots = Config.numberRobotMax-robotList.size();
        }
        for (int i = 0; i < numberOfRobots; i++) {
            while (true){
                Point start = new Point(getRandomX(), getRandomY(), getRandomHeading());
                if (create(new Robot(start))) {
                    break;
                }
            }
        }
    }

    private int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
    private PointInfo.Status getRandomHeading(){
        int status = getRandomInt(PointInfo.Status.ROBOT_UP.getValue(), PointInfo.Status.ROBOT_RIGHT.getValue());
        return PointInfo.Status.getEnum(status);
    }
    private int getRandomX(){
        return random.nextInt(Map.xLength);
    }
    private int getRandomY(){
        return random.nextInt(Map.yLength);
    }


    public List<Robot> getRobotList() {
        return robotList;
    }



    public static class Config{
        public static int numberRobotMax = 100;
    }
}
