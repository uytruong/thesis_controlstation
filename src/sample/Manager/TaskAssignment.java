package sample.Manager;

public class TaskAssignment {
    private RobotManager robotManager;
    private TaskManager  taskManager;
    private MapManager   mapManager;
    private Context      context;

    public TaskAssignment(RobotManager robotManager, TaskManager taskManager, MapManager mapManager, Context context) {
        this.robotManager = robotManager;
        this.taskManager = taskManager;
        this.mapManager = mapManager;
        this.context = context;
    }


    public RobotManager getRobotManager() {
        return robotManager;
    }
    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }
    public TaskManager getTaskManager() {
        return taskManager;
    }
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    public MapManager getMapManager() {
        return mapManager;
    }
    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
}
