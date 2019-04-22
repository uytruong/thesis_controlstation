package sample.Creator;

import sample.Manager.Context;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskCreator {
    private Random     random;
    private MapBase    mapBase;
    private List<Task> taskList = new ArrayList<>();

    public TaskCreator(MapCreator mapCreator, Random random) {
        this.mapBase = mapCreator.getMapBase();
        this.random  = random;
    }

    public boolean create(Task task) {
        if (mapBase.getStatus(task.getGoal().getX(),task.getGoal().getY()) == PointInfo.Status.SHELF){
            return false;
        }
        else {
            task.setId(taskList.size());
            taskList.add(task);
            return true;
        }
    }

    public void createRandom(int numberOfTasks, int typeOfTasks){
        if ((taskList.size() + numberOfTasks) >= Context.TaskCreator.numberTaskMax){
            numberOfTasks = Context.TaskCreator.numberTaskMax - taskList.size();
        }
        for (int i = 0; i < numberOfTasks; i++) {
            while (true){
                Point goal = new Point(getRandomTaskPointX(),getRandomTaskPointY());
                Task  task = new Task(typeOfTasks,getRandomTimeExecute(),getRandomTimeAppear(),goal);
                if (create(task)) {
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
    private int getRandomTaskPointX(){
        return random.nextInt(MapBase.xLength);
    }
    private int getRandomTaskPointY(){
        return random.nextInt(MapBase.yLength);
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
