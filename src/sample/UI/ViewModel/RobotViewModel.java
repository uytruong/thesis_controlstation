package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class RobotViewModel {
    private SimpleIntegerProperty robotID;
    private SimpleIntegerProperty robotStartPointX;
    private SimpleIntegerProperty robotStartPointY;
    private SimpleStringProperty  robotHeading;
    private SimpleIntegerProperty robotTimeFree;

    public RobotViewModel(int robotID, int robotX, int robotY, String robotHeading, int robotTimeFree) {
        this.robotID = new SimpleIntegerProperty(robotID);
        this.robotStartPointX = new SimpleIntegerProperty(robotX);
        this.robotStartPointY = new SimpleIntegerProperty(robotY);
        this.robotHeading = new SimpleStringProperty(robotHeading);
        this.robotTimeFree = new SimpleIntegerProperty(robotTimeFree);
    }

    public int getRobotID() {
        return robotID.get();
    }

    public SimpleIntegerProperty robotIDProperty() {
        return robotID;
    }

    public void setRobotID(int robotID) {
        this.robotID.set(robotID);
    }

    public int getRobotStartPointX() {
        return robotStartPointX.get();
    }

    public SimpleIntegerProperty robotStartPointXProperty() {
        return robotStartPointX;
    }

    public void setRobotStartPointX(int robotStartPointX) {
        this.robotStartPointX.set(robotStartPointX);
    }

    public int getRobotStartPointY() {
        return robotStartPointY.get();
    }

    public SimpleIntegerProperty robotStartPointYProperty() {
        return robotStartPointY;
    }

    public void setRobotStartPointY(int robotStartPointY) {
        this.robotStartPointY.set(robotStartPointY);
    }

    public String getRobotHeading() {
        return robotHeading.get();
    }

    public SimpleStringProperty robotHeadingProperty() {
        return robotHeading;
    }

    public void setRobotHeading(String robotHeading) {
        this.robotHeading.set(robotHeading);
    }

    public int getRobotTimeFree() {
        return robotTimeFree.get();
    }

    public SimpleIntegerProperty robotTimeFreeProperty() {
        return robotTimeFree;
    }

    public void setRobotTimeFree(int robotTimeFree) {
        this.robotTimeFree.set(robotTimeFree);
    }
}
