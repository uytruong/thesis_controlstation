package sample.Model;

public class Point {
    private int x;
    private int y;
    private int id;
    private int status;

    /**
     * These methods are only for path planning
     */
    private int robotId = -1;


    public Point() {
    }

    public Point(int x, int y, int status){
        this.x      = x;
        this.y      = y;
        this.status = status;
        updateId();
    }
    public Point(int id){
        this.id = id;
    }

    public Point(int id, int status) {
        this.id     = id;
        this.status = status;
        updateXY();
    }

    public Point(Point point){
        this.id     = point.getId();
        this.status = point.getStatus();
        updateXY();
    }

    public int getRobotId() {
        return robotId;
    }
    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
        updateXY();
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
        updateId();
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
        updateId();
    }

    private void updateId(){id = MapBase.getIdFromXY(x,y);}
    private void updateXY(){x = MapBase.getXFromId(id); y = MapBase.getYFromId(id);}
}
