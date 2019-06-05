package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import sample.Model.Task;

public class TaskViewModel {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty x;
    private SimpleIntegerProperty stationX;
    private SimpleIntegerProperty y;
    private SimpleIntegerProperty timeAppear;
    private SimpleIntegerProperty timeExecute;
    private SimpleStringProperty status;
    private SimpleStringProperty heading;


    public TaskViewModel(Task task) {
        this.id = new SimpleIntegerProperty(task.getId());
        this.x = new SimpleIntegerProperty(task.getGoal().getX());
        this.stationX = new SimpleIntegerProperty(task.getStation().getX());
        this.y = new SimpleIntegerProperty(task.getGoal().getY());
        this.timeAppear = new SimpleIntegerProperty(task.getTimeAppear());
        this.timeExecute = new SimpleIntegerProperty(task.getTimeExecute());
        this.status = new SimpleStringProperty(task.getStatus().name());
        this.heading = new SimpleStringProperty(task.getGoal().getStatus().name());
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public int getStationX() {
        return stationX.get();
    }

    public SimpleIntegerProperty stationXProperty() {
        return stationX;
    }

    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public int getTimeAppear() {
        return timeAppear.get();
    }

    public SimpleIntegerProperty timeAppearProperty() {
        return timeAppear;
    }

    public int getTimeExecute() {
        return timeExecute.get();
    }

    public SimpleIntegerProperty timeExecuteProperty() {
        return timeExecute;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getHeading() {
        return heading.get();
    }

    public SimpleStringProperty headingProperty() {
        return heading;
    }
}
