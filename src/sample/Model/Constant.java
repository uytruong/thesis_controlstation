package sample.Model;


public class Constant {
    public class PointStatus{
        public static final int NONE        = 0;
        public static final int SHELF       = 1;
        public static final int ROBOT_LEFT  = 2;
        public static final int ROBOT_RIGHT = 3;
        public static final int ROBOT_UP    = 4;
        public static final int ROBOT_DOWN  = 5;
        public static final int DONTCARE    = 6;
    }

    public class TaskStatus{
        public static final int NEW = 0;
        public static final int READY = 1;
        public static final int RUNNING = 2;
        public static final int DONE = 3;
    }

    public class RobotPointStatus{
        public static final int LEFT  = PointStatus.ROBOT_LEFT;
        public static final int RIGHT = PointStatus.ROBOT_RIGHT;
        public static final int UP    = PointStatus.ROBOT_UP;
        public static final int DOWN  = PointStatus.ROBOT_DOWN;
    }
    public class TaskPointStatus{
        public static final int LEFT    = PointStatus.ROBOT_LEFT;
        public static final int RIGHT   = PointStatus.ROBOT_RIGHT;
        public static final int UP      = PointStatus.ROBOT_UP;
        public static final int DOWN    = PointStatus.ROBOT_DOWN;
        public static final int DONTCARE = PointStatus.DONTCARE;
    }




}
