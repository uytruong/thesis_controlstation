package sample.Algorithm;

import sample.Manager.MapManager;
import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SinglePathPlanning {
    private final List<MapBase> virtualMapList;
    private List<Point>         pointList;
    private Point start, goal;
    private int startTime;


    public SinglePathPlanning(MapManager mapManager) {
        virtualMapList = new ArrayList<>(mapManager.getMapList());
    }

    public void setData(Point start, Point goal, int startTime){
        this.start     = start;
        this.goal      = goal;
        this.startTime = startTime;
    }

    public void calculatePath(){


    }









    private static class Action{


        public enum Act{
            NO,
            ROTATE_RIGHT,
            ROTATE_LEFT,
            ROTATE_BACK,
            SPEED_UP,
            MOVE_CONSTANT,
            SPEED_DOWN,
            JUMP,
        }

        public List<Act> getValidAct(Act preAct){
            List<Act> validActList;
            switch (preAct){
                case SPEED_UP:
                    validActList = Arrays.asList(
                            Act.MOVE_CONSTANT,
                            Act.SPEED_DOWN);
                    break;
                case MOVE_CONSTANT:
                    validActList = Arrays.asList(
                            Act.MOVE_CONSTANT,
                            Act.SPEED_DOWN);
                    break;

                default:
                    validActList = Arrays.asList(
                            Act.NO,
                            Act.ROTATE_LEFT,
                            Act.ROTATE_RIGHT,
                            Act.ROTATE_BACK,
                            Act.SPEED_UP,
                            Act.MOVE_CONSTANT,
                            Act.SPEED_DOWN,
                            Act.JUMP);
            }
            return validActList;
        }

        public int getTimeExecute(Act act){
            switch (act){
                case ROTATE_BACK:
                    return 2;
                case SPEED_UP:
                    return 2;
                case SPEED_DOWN:
                    return 2;
                case JUMP:
                    return 2;
                default:
                    return 1;
            }
        }
        public Point getNextPoint(Point point,Act act){
            Point nextPoint = new Point();
            if (act == Act.NO){
                return new Point(point);
            }
            else if ((act == Act.ROTATE_LEFT) | (act == Act.ROTATE_RIGHT) | (act == Act.ROTATE_BACK)){
                nextPoint = new Point(point);
                nextPoint.setStatus(getHeadingIfRotate(point.getStatus(),act));
            }
            else{

            }

            return nextPoint;
        }

        private int getHeadingIfRotate(int heading, Act act){
            if (heading == Constant.RobotPointHeading.UP){
                switch (act){
                    case ROTATE_RIGHT:
                        return Constant.RobotPointHeading.RIGHT;
                    case ROTATE_LEFT:
                        return Constant.RobotPointHeading.LEFT;
                    case ROTATE_BACK:
                        return Constant.RobotPointHeading.DOWN;
                }
            }
            else if (heading == Constant.RobotPointHeading.DOWN){
                switch (act){
                    case ROTATE_RIGHT:
                        return Constant.RobotPointHeading.LEFT;
                    case ROTATE_LEFT:
                        return Constant.RobotPointHeading.RIGHT;
                    case ROTATE_BACK:
                        return Constant.RobotPointHeading.UP;
                }
            }
            else if (heading == Constant.RobotPointHeading.LEFT){
                switch (act){
                    case ROTATE_RIGHT:
                        return Constant.RobotPointHeading.UP;
                    case ROTATE_LEFT:
                        return Constant.RobotPointHeading.DOWN;
                    case ROTATE_BACK:
                        return Constant.RobotPointHeading.RIGHT;
                }
            }
            else if (heading == Constant.RobotPointHeading.RIGHT){
                switch (act){
                    case ROTATE_RIGHT:
                        return Constant.RobotPointHeading.DOWN;
                    case ROTATE_LEFT:
                        return Constant.RobotPointHeading.UP;
                    case ROTATE_BACK:
                        return Constant.RobotPointHeading.LEFT;
                }
            }
            return -1;
        }
    }
}
