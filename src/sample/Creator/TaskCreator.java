package sample.Creator;

import sample.Manager.Context;
import sample.Model.Constant;
import sample.Model.MapBase;
import sample.Model.Point;
import sample.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskCreator {
    private Random     random;
    private MapBase    mapBase;
    private List<Task> taskList = new ArrayList<>();

    public TaskCreator(MapBaseCreator mapBaseCreator, Random random) {
        this.mapBase = mapBaseCreator.getMapBase();
        this.random = random;
    }

    public boolean createTask(Task task) {
        if (mapBase.getStatus(task.getGoal()) == Constant.PointStatus.PERM){
            return false;
        }
        else {
            task.setId(taskList.size());
            taskList.add(task);
            return true;
        }
    }

    public void createTaskRandom(int number, int type){
        if ((taskList.size()+number) >= Context.TaskCreator.numberTaskMax){
            number = Context.TaskCreator.numberTaskMax-taskList.size();
        }
        for (int i = 0; i < number; i++) {
            while (true){
                if (createTask(new Task(type,getRandomTimeExecute(),getRandomTimeAppear(), new Point(getRandomTaskPointId(),getRandomTaskPointStatus())))) {
                    break;
                }
            }
        }
    }
    private int getRandomTimeAppear(){
        return random.nextInt(Context.TaskCreator.timeAppearMax);
    }
    private int getRandomTimeExecute(){
        return random.nextInt(Context.TaskCreator.timeExecuteMax);
    }

    private int getRandomInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
    private int getRandomTaskPointStatus(){
        return getRandomInt(Constant.TaskPointStatus.LEFT,Constant.TaskPointStatus.DONCARE);
    }
    private int getRandomTaskPointId(){
        return random.nextInt(mapBase.getStatusList().size());
    }

    public List<Task> getTaskList() {
        return taskList;
    }



}
