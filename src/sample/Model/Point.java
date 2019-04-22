package sample.Model;


public class Point extends PointInfo {
    private int x;
    private int y;

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
    }

    public Point(int x, int y, Status status) {
        super(status);
        this.x = x;
        this.y = y;
    }



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
     * ===========================   STATIC PUBLIC METHODS =========================== *
     * */
    static public boolean isCoincident(Point point1, Point point2){
        return ((point1.getX() == point2.getX()) & (point1.getY() == point2.getY()));
    }
}
