package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import sample.Model.Point;
import sample.Model.Robot;

public class RobotViewModel {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty x;
    private SimpleIntegerProperty y;
    private SimpleStringProperty heading;
    private SimpleStringProperty status;
    private SimpleIntegerProperty timeFree;
    private SimpleStringProperty task;

    public RobotViewModel(Robot robot, int time) {
        Point point = robot.getPointByTime(time);
        this.id = new SimpleIntegerProperty(robot.getId());
        this.x = new SimpleIntegerProperty(point.getX());
        this.y = new SimpleIntegerProperty(point.getY());
        this.heading = new SimpleStringProperty(point.getStatus().name());
        this.status = new SimpleStringProperty(robot.getStatus().name());
        this.timeFree = new SimpleIntegerProperty(robot.getTimeFree());

        String task = (robot.getTask() == null)? "NONE" :Integer.toString(robot.getTask().getId());
        this.task = new SimpleStringProperty(task);
    }

    public String getTask() {
        return task.get();
    }

    public SimpleStringProperty taskProperty() {
        return task;
    }

    public void setTask(String task) {
        this.task.set(task);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public String getHeading() {
        return heading.get();
    }

    public SimpleStringProperty headingProperty() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading.set(heading);
    }

    public int getTimeFree() {
        return timeFree.get();
    }

    public SimpleIntegerProperty timeFreeProperty() {
        return timeFree;
    }

    public void setTimeFree(int timeFree) {
        this.timeFree.set(timeFree);
    }
}
