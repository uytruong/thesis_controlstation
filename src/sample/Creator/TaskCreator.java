package sample.Creator;

import sample.Manager.Context;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskCreator {
    private Random     random;
    private final Map  map;
    private List<Task> taskList = new ArrayList<>();

    public TaskCreator(MapCreator mapCreator, Random random) {
        this.map     = mapCreator.getMap();
        this.random  = random;
    }

    public boolean create(Task task) {
        if (!map.getPointInfoByXY(task.getGoal().getX(),task.getGoal().getY()).isEmpty()){
            return false;
        }
        else {
            task.setId(taskList.size());
            taskList.add(task);
            return true;
        }
    }

    public void createRandom(int numberOfTasks, int typeOfTasks){
        if ((taskList.size() + numberOfTasks) >= Config.numberTaskMax){
            numberOfTasks = Config.numberTaskMax - taskList.size();
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
        return random.nextInt(Config.timeAppearMax);
    }
    private int getRandomTimeExecute(){
        return random.nextInt(Config.timeExecuteMax);
    }
    private int getRandomTaskPointX(){
        return random.nextInt(Map.xLength);
    }
    private int getRandomTaskPointY(){
        return random.nextInt(Map.yLength);
    }

    public List<Task> getTaskList() {
        return taskList;
    }


    public static class Config {
        public static int numberTaskMax  = 100;
        public static int timeAppearMax  = 3;
        public static int timeExecuteMax = 4;
    }
}
