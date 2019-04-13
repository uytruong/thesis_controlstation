package sample.Model;

public class Point {
    private int id;
    private int status;

    public Point(int id, int status) {
        this.id = id;
        this.status = status;
    }

    public Point(Point point){
        this.id     = point.getId();
        this.status = point.getStatus();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
