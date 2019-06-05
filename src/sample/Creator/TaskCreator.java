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

    public TaskCreator(Random random) {
        this.map     = MapCreator.map;
        this.random  = random;
    }

    public boolean create(Task task) {
        if (!map.getPointInfoByXY(task.getGoal().getX(),task.getGoal().getY()).isEmpty()){
            return false;
        }
        if (!map.getPointInfoByPoint(task.getStation()).isEmpty()){
            return false;
        }
        else {
            task.setId(taskList.size());
            taskList.add(task);
            sortTaskList();
            return true;
        }
    }

    public void createRandom(int numberOfTasks){
        if ((taskList.size() + numberOfTasks) >= Config.numberTaskMax){
            numberOfTasks = Config.numberTaskMax - taskList.size();
        }
        for (int i = 0; i < numberOfTasks; i++) {
            while (true){
                Point goal = new Point(getRandomX(), getRandomY(),getRandomHeading());
                Point station = getRandomStation();
                Task  task    = new Task(getRandomTimeExecute(),getRandomTimeAppear(),goal,station);
                if (create(task)) {
                    break;
                }
            }
        }
    }
    private int getRandomTimeAppear(){
        return random.nextInt(Config.timeAppearMax-Config.timeAppearMin+1)+Config.timeAppearMin;
    }
    private int getRandomTimeExecute(){
        return random.nextInt(Config.timeExecuteMax-Config.timeExecuteMin)+Config.timeExecuteMin;
    }
    private int getRandomX(){
        return random.nextInt(Map.xLength);
    }
    private int getRandomY(){
        return random.nextInt(Map.yLength-1)+1;
    }
    private PointInfo.Status getRandomHeading(){
        int status = random.nextInt(PointInfo.Status.ROBOT_RIGHT.getValue()-PointInfo.Status.ROBOT_UP.getValue()+1)+PointInfo.Status.ROBOT_UP.getValue();
        return PointInfo.Status.getEnum(status);
    }

    private Point getRandomStation(){
        Point station;
        while (true) {
            station = new Point(getRandomX(), 0, PointInfo.Status.ROBOT_DOWN);
            if (map.getPointInfoByPoint(station).isEmpty())
                break;
        }
        return station;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
    private void sortTaskList(){
        taskList.sort(Comparator.comparing(Task::getTimeAppear));
    }

    private static class Config {
        private static int numberTaskMax  = 200;
        private static int timeAppearMax  = 35;
        private static int timeAppearMin  = 1;
        private static int timeExecuteMax = 5;
        private static int timeExecuteMin = 1;
    }
}
