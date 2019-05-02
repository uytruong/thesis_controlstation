package sample.Creator;

import sample.Model.*;

import java.util.ArrayList;
import java.util.Comparator;
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
            sortTaskList();
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
        return random.nextInt(Config.timeAppearMax-Config.timeAppearMin)+Config.timeAppearMin;
    }
    private int getRandomTimeExecute(){
        return random.nextInt(Config.timeExecuteMax-1)+1;
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
    private void sortTaskList(){
        taskList.sort(Comparator.comparing(Task::getTimeAppear));
    }

    public static class Config {
        public static int numberTaskMax  = 100;
        public static int timeAppearMax  = 10;
        public static int timeAppearMin  = 1;
        public static int timeExecuteMax = 2;
    }
}
