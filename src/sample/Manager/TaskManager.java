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

    public void update(int timeUpdate){
         /* find new TaskCreator from taskCreator.taskList and add to readyTaskList if:
            - its Goal position is not assinged to other running TaskCreator.
            - there is no task in readyTaskList having same Goal
         */
        for (Task task: taskList) {
            if ((task.getStatus() == Constant.TaskStatus.NEW) & (task.getTimeAppear() <= timeUpdate)){
                boolean valid = true;
                for (Task runningTask: runningTaskList) {
                    if(task.getGoal().getId() == runningTask.getGoal().getId()){
                        valid = false;
                        break;
                    }
                }

                for (Task readyTask: readyTaskList) {
                    if (task.getGoal().getId() == readyTask.getGoal().getId()){
                        valid = false;
                        break;
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
            /* Convert task <status> from READY to RUNNING then add task to <runningTaskList>
             * then remove task from <readyTaskList>*/
            task.setStatus(status);
            runningTaskList.add(task);

            for (int i = 0; i < readyTaskList.size(); i++) {
                if (id == readyTaskList.get(i).getId()){
                    readyTaskList.remove(i);
                    break;
                }
            }

        }
        //& (task.getStatus() == Constant.TaskStatus.RUNNING)
        else if ((status == Constant.TaskStatus.DONE)  ){
            /* Convert task <status> from RUNNING to DONE, and also counting number of tasks wereDone;
             * then remove task from <runningTaskList> */
            task.setStatus(status);
            doneTaskNumber++;

            for (int i = 0; i < runningTaskList.size(); i++) {
                if (id == runningTaskList.get(i).getId()){
                    runningTaskList.remove(i);
                    break;
                }
            }
        }

    }

    public boolean isTaskListDone(){
        return (taskList.size() == doneTaskNumber);
    }


    public List<Task> getTaskList() {
        return taskList;
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


    public void printInfo(){
        System.out.println("======== TIME = " + Context.Time.time +" ============ ");
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
