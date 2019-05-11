package sample.Manager;

import sample.Creator.TaskCreator;
import sample.Model.Point;
import sample.Model.Robot;
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
                       ((otherTask.getStatus() == Task.Status.BOUND) | (otherTask.getStatus() == Task.Status.READY) | (otherTask.getStatus() == Task.Status.RUNNING))){
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    task.setStatus(Task.Status.READY);
                }
            } else if((task.getStatus() == Task.Status.BOUND) & (time >= task.getTimeArrived())) {
               task.setStatus(Task.Status.RUNNING);
            } else if((task.getStatus() == Task.Status.RUNNING)){
                int timeExecute = time - task.getTimeArrived();
                if(timeExecute >= task.getTimeExecute())
                {
                    doneTaskNumber++;
                    task.setTimeFinish(time);
                    task.setStatus(Task.Status.DONE);
                    task.getRobot().unbindTask();
                }
            }
        }
    }

    public void setLastTimeAssign(int lastTimeAssign) {
        this.lastTimeAssign = lastTimeAssign;
    }

    public List<Task> getReadyTaskList(){
        List<Task> taskList = new ArrayList<>();
        for (Task task: this.taskList) {
            if (task.getStatus() == Task.Status.READY)
                taskList.add(task);
        }
        return taskList;
    }
    public List<Task> getBoundTaskList(){
        List<Task> taskList = new ArrayList<>();
        for (Task task: this.taskList) {
            if (task.getStatus() == Task.Status.BOUND)
                taskList.add(task);
        }
        return taskList;
    }
    public List<Task> getRunningTaskList(){
        List<Task> taskList = new ArrayList<>();
        for (Task task: this.taskList) {
            if (task.getStatus() == Task.Status.RUNNING)
                taskList.add(task);
        }
        return taskList;
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
    public boolean assignable(int time, RobotManager robotManager){
        int freeRobotNum = 0;
        for (Robot robot: robotManager.getRobotList()) {
            if(robot.getStatus() == Robot.Status.FREE)
                freeRobotNum++;
        }
        if (freeRobotNum == 0)
            return false;
        else
            return ((getReadyTaskList().size()>0) &((time-lastTimeAssign)>=Config.timeDelayForNextAssign));
    }
    public boolean isAllTaskFinished(){
        return (doneTaskNumber==taskList.size());
    }

    private static class Config{
        private static int timeDelayForNextAssign = 1;
    }
}
