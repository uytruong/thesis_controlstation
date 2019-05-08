package sample.Manager;

import sample.Creator.TaskCreator;
import sample.Model.Point;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task>  taskList;
    private int         doneTaskNumber = 0;
    private int         lastTimeAssign = 0;

    public TaskManager(TaskCreator taskCreator) {
        this.taskList = taskCreator.getTaskList();
    }

    public void updateByTime(int time) {
        for (Task task : taskList) {
            if ((task.getStatus() == Task.Status.NEW) & (task.getTimeAppear() <= time)) {
                boolean valid = true;
                for (Task otherTask : taskList) {
                    if ((task != otherTask) & ((Point.isCoincident(task.getGoal(), otherTask.getGoal()))) &
                       ((otherTask.getStatus() == Task.Status.RUNNING) | (otherTask.getStatus() == Task.Status.READY))){
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    task.setStatus(Task.Status.READY);
                }
            } else if (task.getTimeFinish() == time) {
                doneTaskNumber++;
                task.setStatus(Task.Status.DONE);
            }
        }
    }

    public void setLastTimeAssign(int lastTimeAssign) {
        this.lastTimeAssign = lastTimeAssign;
    }

    public List<Task> getReadyTaskList(){
        List<Task> readyTaskList = new ArrayList<>();
        for (Task task: taskList) {
            if (task.getStatus() == Task.Status.READY){
                readyTaskList.add(task);
            }
        }
        return readyTaskList;
    }
    public List<Task> getRunningTaskList(){
        List<Task> runningTaskList = new ArrayList<>();
        for (Task task: taskList) {
            if (task.getStatus() == Task.Status.RUNNING){
                runningTaskList.add(task);
            }
        }
        return runningTaskList;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public int getDoneTaskNumber() {
        return doneTaskNumber;
    }

    public boolean assignable(){
        return (getReadyTaskList().size()>0);
    }
    public boolean assignable(int time){
        return ((getReadyTaskList().size()>0) &((time-lastTimeAssign)>8));
    }
    public boolean finishAllTasks(){
        return (doneTaskNumber==taskList.size());
    }

}
