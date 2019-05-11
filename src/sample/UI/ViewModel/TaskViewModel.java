package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TaskViewModel {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty x;
    private SimpleIntegerProperty y;
    private SimpleIntegerProperty timeAppear;
    private SimpleIntegerProperty timeExecute;
    private SimpleStringProperty status;
    private SimpleStringProperty heading;

    public TaskViewModel(int id, int x, int y, String heading, int timeAppear, int timeExecute, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.timeAppear = new SimpleIntegerProperty(timeAppear);
        this.timeExecute = new SimpleIntegerProperty(timeExecute);
        this.status = new SimpleStringProperty(status);
        this.heading = new SimpleStringProperty(heading);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public int getX() {
        return x.get();
    }

    public void setX(int x) {
        this.x = new SimpleIntegerProperty(x);
    }

    public int getY() {
        return y.get();
    }

    public void setY(int y) {
        this.y = new SimpleIntegerProperty(y);
    }

    public int getTimeAppear() {
        return timeAppear.get();
    }

    public void setTimeAppear(int timeAppear) {
        this.timeAppear = new SimpleIntegerProperty(timeAppear);
    }

    public int getTimeExecute() {
        return timeExecute.get();
    }

    public void setTimeExecute(int timeExecute) {
        this.timeExecute = new SimpleIntegerProperty(timeExecute);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
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
}
