package sample.Manager;

import sample.Creator.TaskCreator;
import sample.Model.Constant;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task>  taskList;
    private List<Task>  readyTaskList   = new ArrayList<>();
    private List<Task>  runningTaskList = new ArrayList<>();
    private int         doneTaskNumber = 0;

    public TaskManager(TaskCreator taskCreator) {
        this.taskList = taskCreator.getTaskList();
        init();
    }

    private void init(){

    }

    public void update(){
        /* remove all Tasks which were RUNNING from readyTaskList */
        List<Integer> removeIndex = new ArrayList<>();
        for (int i = 0; i < readyTaskList.size(); i++){
            if (readyTaskList.get(i).getStatus() != Constant.TaskStatus.READY){
                removeIndex.add(i);
            }
        }
        for (int i = removeIndex.size()-1; i >= 0 ; i--) {
            readyTaskList.remove(removeIndex.get(i).intValue());
        }


        /* remove all Tasks which were DONE from runningTaskList */
        removeIndex = new ArrayList<>();
        for (int i = 0; i < runningTaskList.size(); i++){
            if (runningTaskList.get(i).getStatus() != Constant.TaskStatus.RUNNING){
                doneTaskNumber++;
                removeIndex.add(i);
            }
        }
        for (int i = removeIndex.size()-1; i >= 0 ; i--) {
            runningTaskList.remove(removeIndex.get(i).intValue());
        }

        /* find new Task from taskCreator.taskList and add to readyTaskList if:
            - its Goal position is not assinged to other running Task.
            - there is no task in readyTaskList having same Goal
         */
        for (Task task: taskList) {
            if ((task.getStatus() == Constant.TaskStatus.NEW) & (task.getTimeAppear() <= Context.time)){
                boolean valid = true;
                for (Task runningTask: runningTaskList) {
                    if(task.getGoal().getId() == runningTask.getGoal().getId()){
                        valid = false;
                    }
                }

                for (Task readyTask: readyTaskList) {
                    if (task.getGoal().getId() == readyTask.getGoal().getId()){
                        valid = false;
                    }
                }

                if(valid){
                    task.setStatus(Constant.TaskStatus.READY);
                    readyTaskList.add(task);
                }
            }
        }


    }

    public void changeTaskStatus(int id, int status){
        Task task = taskList.get(id);

        if ((status == Constant.TaskStatus.RUNNING) & (task.getStatus() == Constant.TaskStatus.READY)){
            /*converti task status from ready to running then add task to runningTaskList
             * removing task from readyTaskList is work of update() */
            task.setStatus(Constant.TaskStatus.RUNNING);
            runningTaskList.add(task);
        }
        //& (task.getStatus() == Constant.TaskStatus.RUNNING)
        else if ((status == Constant.TaskStatus.DONE) ){
            task.setStatus(status);
        }
    }


    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Task> getReadyTaskList() {
        return readyTaskList;
    }

    public void setReadyTaskList(List<Task> readyTaskList) {
        this.readyTaskList = readyTaskList;
    }

    public List<Task> getRunningTaskList() {
        return runningTaskList;
    }

    public void setRunningTaskList(List<Task> runningTaskList) {
        this.runningTaskList = runningTaskList;
    }

    public int getDoneTaskNumber() {
        return doneTaskNumber;
    }

    public void setDoneTaskNumber(int doneTaskNumber) {
        this.doneTaskNumber = doneTaskNumber;
    }

    public void printInfo(){
        System.out.println("======== TIME = " + Context.time +" ============ ");
        System.out.println("======== TASK LIST ===========");
        for (Task task: taskList) {
            task.printInfo();
        }
        System.out.println("===== READY TASK LIST ========");
        for (Task task: readyTaskList) {
            task.printInfo();
        }
        System.out.println("===== RUNNING TASK LIST ======");
        for (Task task: runningTaskList) {
            task.printInfo();
        }

    }
}
