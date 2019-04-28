package sample.Manager;

import sample.Creator.TaskCreator;
import sample.Model.Point;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task>  taskList;
    private List<Task>  readyTaskList   = new ArrayList<>();
    private List<Task>  runningTaskList = new ArrayList<>();
    private int         doneTaskNumber  = 0;

    private int         lastTimeAssign  = 0;

    public TaskManager(TaskCreator taskCreator) {
        this.taskList = taskCreator.getTaskList();
    }

    public void updateByTime(int time){
        for (Task task: taskList) {
            if ((task.getStatus() == Task.Status.NEW) & (task.getTimeAppear() <= time)){
                boolean valid = true;
                for (Task runningTask: runningTaskList) {
                    if(Point.isCoincident(task.getGoal(), runningTask.getGoal()) ){
                        valid = false;
                        break;
                    }
                }
                if(valid){
                    for (Task readyTask: readyTaskList) {
                        if(Point.isCoincident(task.getGoal(), readyTask.getGoal()) ){
                            valid = false;
                            break;
                       }
                    }
                }
                if(valid){
                    task.setStatus(Task.Status.READY);
                    readyTaskList.add(task);
                }
            }
            else if(task.getTimeFinish() == time){
                changeTaskStatus(task, Task.Status.DONE);
            }
        }
    }

    public void changeTaskStatus(Task task,Task.Status status){
        if (status == Task.Status.RUNNING){
            /* Convert task <status> from READY to RUNNING then add task to <runningTaskList>
             * then remove task from <readyTaskList>*/
            task.setStatus(status);
            runningTaskList.add(task);

            for (int i = 0; i < readyTaskList.size(); i++) {
                if (task == readyTaskList.get(i)){
                    readyTaskList.remove(i);
                    break;
                }
            }

        }
        else if ((status == Task.Status.DONE)  ){
            /* Convert task <status> from RUNNING to DONE, and also counting number of tasks wereDone;
             * then remove task from <runningTaskList> */
            task.setStatus(status);
            doneTaskNumber++;

            for (int i = 0; i < runningTaskList.size(); i++) {
                if (task == runningTaskList.get(i)){
                    runningTaskList.remove(i);
                    break;
                }
            }
        }

    }

    public boolean isTaskListDone(){
        return (taskList.size() == doneTaskNumber);
    }

    public List<Task> getReadyTaskList() {
        return readyTaskList;
    }
    public List<Task> getRunningTaskList() {
        return runningTaskList;
    }
    public int getDoneTaskNumber() {
        return doneTaskNumber;
    }


    public Task getTaskById(int id){ return taskList.get(id);}

    public boolean isAssignable(int time){
        boolean condition1 = ((time-lastTimeAssign)>=Config.timeAssignDelayMax) & (readyTaskList.size()>0);
        boolean condition2 = (readyTaskList.size()>=Config.taskAssignNumberMin);
        return (condition1 | condition2);
    }

    public static class Config{
        public static int timeAssignDelayMax = 3;
        public static int taskAssignNumberMin = 3;
    }
}
