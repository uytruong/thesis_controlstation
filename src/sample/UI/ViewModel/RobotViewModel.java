package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class RobotViewModel {
    private SimpleIntegerProperty robotID;
    private SimpleIntegerProperty robotType;
    private SimpleIntegerProperty robotStartPointX;
    private SimpleIntegerProperty robotStartPointY;
    private SimpleStringProperty robotHeading;

    public RobotViewModel(int robotID, int robotType, int robotX, int robotY, String robotHeading) {
        this.robotID = new SimpleIntegerProperty(robotID);
        this.robotType = new SimpleIntegerProperty(robotType);
        this.robotStartPointX = new SimpleIntegerProperty(robotX);
        this.robotStartPointY = new SimpleIntegerProperty(robotY);
        this.robotHeading = new SimpleStringProperty(robotHeading);
    }

    public int getRobotID() {
        return robotID.get();
    }

    public void setRobotID(int robotID) {
        this.robotID = new SimpleIntegerProperty(robotID);
    }

    public int getRobotType() {
        return robotType.get();
    }

    public void setRobotType(int robotType) {
        this.robotType = new SimpleIntegerProperty(robotType);
    }

    public int getRobotStartPointX() {
        return robotStartPointX.get();
    }

    public void setRobotStartPointX(int robotStartPointX) {
        this.robotStartPointX = new SimpleIntegerProperty(robotStartPointX);
    }

    public int getRobotStartPointY() {
        return robotStartPointY.get();
    }

    public void setRobotStartPointY(int robotStartPointY) {
        this.robotStartPointY = new SimpleIntegerProperty(robotStartPointY);
    }

    public String getRobotHeading() {
        return robotHeading.get();
    }

    public void setRobotHeading(String robotHeading) {
        this.robotHeading = new SimpleStringProperty(robotHeading);
    }
}
