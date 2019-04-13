package sample.Creator;

import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskCreator {
    private MapBase    mapBase;
    private List<Task> taskList = new ArrayList<>();

    public TaskCreator(MapBaseCreator mapBaseCreator) {
        mapBase = mapBaseCreator.getMapBase();
    }

    public boolean createTask(Task task) {
        for (Task otherTask: taskList) {
            if((otherTask.getGoal().getId() == task.getGoal().getId()) & (otherTask.getType() == task.getType()) &(otherTask.getTimeAppear() == task.getTimeAppear())){
                return false;
            }
        }

        if (mapBase.getStatus(task.getGoal()) == Constant.PointStatus.PERM){
            return false;
        }
        else {
            task.setId(taskList.size());
            taskList.add(task);
            return true;
        }
    }


    public MapBase getMapBase() {
        return mapBase;
    }
    public void setMapBase(MapBase mapBase) {
        this.mapBase = mapBase;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
