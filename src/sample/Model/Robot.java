package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Robot { ;
    private int id, type;
    private int timeAvailable;
    private List<Point> pointList = new ArrayList<>();
    private Task task;

    public Robot(int type, Point start) {
        this.type = type;
        this.pointList.add(start);
        init();
    }

    private void init(){
        timeAvailable = pointList.size();
    }

    public void update(int timeContext){
        if (this.timeAvailable <= timeContext){
            for (int i = 0; i <= timeContext - this.timeAvailable ; i++) {
                pointList.add(new Point(pointList.get(this.timeAvailable-1)));
            }
            this.timeAvailable = pointList.size();
        }
        else { return;}
    }
    public void addPoint(Point point){
        pointList.add(new Point(point));
        timeAvailable = pointList.size();
    }



    public int getTimeAvailable() {
        return timeAvailable;
    }
    public void setTimeAvailable(int timeAvailable) {
        this.timeAvailable = timeAvailable;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public List<Point> getPointList() {
        return pointList;
    }
    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
    public Point getPoint(int time){ return pointList.get(time);}

    public void printInfo(){
        String info = "Robot Info: id = " + id + "; type = " + type + "; start at: id = " + pointList.get(0).getId() + ", end at: id = " + pointList.get(pointList.size()-1).getId()
                      + "; timeAvailable = " + timeAvailable;
        System.out.println(info);
    }


}
