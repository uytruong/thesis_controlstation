package sample.Model;


public class Point extends PointInfo {
    private int x;
    private int y;
    /**
     * CONSTRUCTOR
     * */
    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
        this.setStatus(point.getStatus());
        this.setRobot(point.getRobot());
    }

    public Point(int x, int y, Status status) {
        super(status);
        this.x = x;
        this.y = y;
    }

    /**
     * GETTER AND SETTER
     * */
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    /**
     * USER DEFINE
     * */
    public Point getPointClone(){
        return new Point(this);
    }


    public void print(){
        String info = "x=" + x + ", y=" + y + ", status=" + getStatus() + "robotId="+getRobot();
        System.out.println(info);
    }
    /**
     * ===========================   STATIC PUBLIC METHODS =========================== *
     * */
    static public boolean isCoincident(Point point1, Point point2){
        return ((point1.getX() == point2.getX()) & (point1.getY() == point2.getY()));
    }

    static public boolean headingSameDirection(Point point1, Point point2){
        return (point1.getStatus() == point2.getStatus());
    }
}
