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
                task.setStatus(Task.Status.DONE);
                Context.totalCost += task.getCostOfRobotPath();
                doneTaskNumber++;
            }
        }
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

    public Task getTaskById(int id){
        for (Task task: taskList ) {
            if(task.getId() == id)
                return task;
        }
        return null;
    }

    public boolean isAssignable(int time){
        List<Task> readyTaskList = getReadyTaskList();
        boolean condition1 = ((time-lastTimeAssign)>=Config.timeAssignDelayMax) & (readyTaskList.size()>0);
        boolean condition2 = (readyTaskList.size()>=Config.taskAssignNumberMin);
        return (condition1 | condition2);
    }

    public void setLastTimeAssign(int lastTimeAssign) {
        this.lastTimeAssign = lastTimeAssign;
    }

    public static class Config{
        public static int timeAssignDelayMax = 0;
        public static int taskAssignNumberMin = 3;
    }

}
