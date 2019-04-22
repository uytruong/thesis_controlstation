package sample.Model;

public class Node extends Point {
    private Motion.Action actionToGetThis;

    private int gScore, hScore;

    private Node previousNode;

    private Point goal;
    private int timeArrived, timeStay;

    private Node(){
    }

    /**init start Node for path planning*/
    public Node(Point start, Point goal, int timeArrived) {
        super(start); /**copy x,y,status*/
        this.goal        = goal;
        this.gScore      = 0;
        updatehScore();
        this.timeArrived = timeArrived;
    }

    public Node getNeighborNode(Motion.Action action){
        Node node = new Node();

        node.setX(getNeighborNodeX(action));
        node.setY(getNeighborNodeY(action));
        node.setStatus(getNeighborNodeStatus(action));
        node.setGoal(goal);

        node.setActionToGetThis(action);
        node.setPreviousNode(this);

        node.updatehScore();
        node.updategScore(action);

        return node;
    }
    private Status getNeighborNodeStatus(Motion.Action action){
        switch (action){
            case ROTATE_LEFT:
                switch (getStatus()){
                    case ROBOT_LEFT:
                        return Status.ROBOT_DOWN;
                    case ROBOT_RIGHT:
                        return Status.ROBOT_UP;
                    case ROBOT_UP:
                        return Status.ROBOT_LEFT;
                    default:
                        return Status.ROBOT_RIGHT;
                }
            case ROTATE_RIGHT:
                switch (getStatus()){
                    case ROBOT_LEFT:
                        return Status.ROBOT_UP;
                    case ROBOT_RIGHT:
                        return Status.ROBOT_DOWN;
                    case ROBOT_UP:
                        return Status.ROBOT_RIGHT;
                    default:
                        return Status.ROBOT_LEFT;
                }
            case ROTATE_BACK:
                switch (getStatus()){
                    case ROBOT_LEFT:
                        return Status.ROBOT_RIGHT;
                    case ROBOT_RIGHT:
                        return Status.ROBOT_LEFT;
                    case ROBOT_UP:
                        return Status.ROBOT_DOWN;
                    default:
                        return Status.ROBOT_UP;
                }
            default:
                return getStatus();
        }



    }
    private int getNeighborNodeX(Motion.Action action){
        switch (action){
            case ROTATE_LEFT:
                return getX();
            case ROTATE_RIGHT:
                return getX();
            case ROTATE_BACK:
                return getX();
        }
        switch (getStatus()){
            case ROBOT_LEFT:
                return getX()-1;
            case ROBOT_RIGHT:
                return getX()+1;
            default:
                return getX();
        }
    }
    private int getNeighborNodeY(Motion.Action action){
        switch (action){
            case ROTATE_LEFT:
                return getY();
            case ROTATE_RIGHT:
                return getY();
            case ROTATE_BACK:
                return getY();
        }
        switch (getStatus()){
            case ROBOT_UP:
                return getY()+1;
            case ROBOT_DOWN:
                return getY()-1;
            default:
                return getY();
        }
    }
    private void updatehScore(){
        hScore = MapBase.getEstimatePathCost(this,goal);
    }
    private void updategScore(Motion.Action action){
        gScore = getPreviousNode().getgScore() + Motion.getActionPathCost(action);
    }














    public void printInfo(){
        String info = "x="+getX() +", y=" + getY() + ", h="+getStatus();
        System.out.println(info);
    }

    public Motion.Action getActionToGetThis() {
        return actionToGetThis;
    }
    public void setActionToGetThis(Motion.Action actionToGetThis) {
        this.actionToGetThis = actionToGetThis;
    }
    public int getgScore() {
        return gScore;
    }
    public void setgScore(int gScore) {
        this.gScore = gScore;
    }

    public int gethScore() {
        return hScore;
    }
    public void sethScore(int hScore) {
        this.hScore = hScore;
    }
    public Node getPreviousNode() {
        return previousNode;
    }
    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }
    public Point getGoal() {
        return goal;
    }
    public void setGoal(Point goal) {
        this.goal = goal;
    }

    public int getTimeArrived() {
        return timeArrived;
    }
    public void setTimeArrived(int timeArrived) {
        this.timeArrived = timeArrived;
    }
    public int getTimeStay() {
        return timeStay;
    }
    public void setTimeStay(int timeStay) {
        this.timeStay = timeStay;
    }
}
