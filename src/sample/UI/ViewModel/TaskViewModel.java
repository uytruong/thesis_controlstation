package sample.UI.ViewModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TaskViewModel {
    private SimpleIntegerProperty taskID;
    private SimpleIntegerProperty taskGoalPointX;
    private SimpleIntegerProperty taskGoalPointY;
    private SimpleIntegerProperty taskAppearTime;
    private SimpleIntegerProperty taskExecuteTime;
    private SimpleStringProperty taskStatus;

    public TaskViewModel(int taskID, int taskGoalPointX, int taskGoalPointY, int taskAppearTime, int taskExecuteTime, String taskStatus) {
        this.taskID = new SimpleIntegerProperty(taskID);
        this.taskGoalPointX = new SimpleIntegerProperty(taskGoalPointX);
        this.taskGoalPointY = new SimpleIntegerProperty(taskGoalPointY);
        this.taskAppearTime = new SimpleIntegerProperty(taskAppearTime);
        this.taskExecuteTime = new SimpleIntegerProperty(taskExecuteTime);
        this.taskStatus = new SimpleStringProperty(taskStatus);
    }

    public int getTaskID() {
        return taskID.get();
    }

    public void setTaskID(int taskID) {
        this.taskID = new SimpleIntegerProperty(taskID);
    }

    public int getTaskGoalPointX() {
        return taskGoalPointX.get();
    }

    public void setTaskGoalPointX(int taskGoalPointX) {
        this.taskGoalPointX = new SimpleIntegerProperty(taskGoalPointX);
    }

    public int getTaskGoalPointY() {
        return taskGoalPointY.get();
    }

    public void setTaskGoalPointY(int taskGoalPointY) {
        this.taskGoalPointY = new SimpleIntegerProperty(taskGoalPointY);
    }

    public int getTaskAppearTime() {
        return taskAppearTime.get();
    }

    public void setTaskAppearTime(int taskAppearTime) {
        this.taskAppearTime = new SimpleIntegerProperty(taskAppearTime);
    }

    public int getTaskExecuteTime() {
        return taskExecuteTime.get();
    }

    public void setTaskExecuteTime(int taskExecuteTime) {
        this.taskExecuteTime = new SimpleIntegerProperty(taskExecuteTime);
    }

    public String getTaskStatus() {
        return taskStatus.get();
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = new SimpleStringProperty(taskStatus);
    }
}
