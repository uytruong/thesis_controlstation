package sample.Model;

import sample.Manager.Context;

import java.util.Random;

public class Constant {
    public class PointStatus{
        public static final int NONE = 0;
        public static final int PERM = 1;
        public static final int LEFT = 2;
        public static final int RIGHT= 3;
        public static final int UP   = 4;
        public static final int DOWN = 5;
        public static final int DONTCARE = 6;
    }

    public class TaskStatus{
        public static final int NEW = 0;
        public static final int READY = 1;
        public static final int RUNNING = 2;
        public static final int DONE = 3;
    }

    public class RobotPointStatus{
        public static final int LEFT = PointStatus.LEFT;
        public static final int RIGHT= PointStatus.RIGHT;
        public static final int UP   = PointStatus.UP;
        public static final int DOWN = PointStatus.DOWN;
    }
    public class TaskPointStatus{
        public static final int LEFT    = PointStatus.LEFT;
        public static final int RIGHT   = PointStatus.RIGHT;
        public static final int UP      = PointStatus.UP;
        public static final int DOWN    = PointStatus.DOWN;
        public static final int DONTCARE = PointStatus.DONTCARE;
    }




}
