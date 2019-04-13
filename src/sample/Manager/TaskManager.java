package sample.Manager;

import sample.Creator.TaskCreator;
import sample.Model.Constant;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private TaskCreator taskCreator;

    private List<Task>  taskList = new ArrayList<>();
    private int         taskDone = 0;

    public TaskManager(TaskCreator taskCreator) {
        this.taskCreator = taskCreator;
        init();
    }

    private void init(){
        updateTaskList();

    }
    public void updateTaskList(){
        List<Integer> removeIndex = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++){
            if (taskList.get(i).getStatus() == Constant.TaskStatus.DONE){
                removeIndex.add(i);
            }
        }
        for (int i = removeIndex.size()-1; i >= 0 ; i--) {
            taskList.remove(removeIndex.get(i).intValue());
        }

        for (Task task: taskCreator.getTaskList()) {
            if ((task.getTimeValid() == Context.time)){
                taskList.add(task);
            }
        }
    }


    public TaskCreator getTaskCreator() {
        return taskCreator;
    }
    public void setTaskCreator(TaskCreator taskCreator) {
        this.taskCreator = taskCreator;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
    public int getTaskDone() {
        return taskDone;
    }
    public void setTaskDone(int taskDone) {
        this.taskDone = taskDone;
    }

}
