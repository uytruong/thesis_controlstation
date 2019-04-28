package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Node extends Point {
    private Motion.Action actionToGetThis;

    /**
     * gScore: cost from startNode to this node;
     * hScore: cost estimate from this node to Goal;
     * pScore: penalty cost for continous actions which are unusual;
     * */
    private int gScore, hScore, pScore;

    private Node  previousNode;
    private Point goal;
    private int   timeArrived;

    private Node(){
    }

    /**init start Node for path planning*/
    public Node(Point start, Point goal, int timeArrived) {
        super(start); /**copy x,y,status & robotId */
        this.goal        = goal;
        this.gScore      = 0;
        updatehScore();
        this.timeArrived = timeArrived;
        this.actionToGetThis = Motion.Action.NONE;
    }

    public Node getNextNodeByAction(Motion.Action action){
        Node nNode = new Node();

        nNode.setActionToGetThis(action);
        nNode.setTimeArrived(timeArrived+Motion.getActionTimeCost(action));
        nNode.setPreviousNode(this);

        nNode.setX(getNextNodeXByAction(action));
        nNode.setY(getNextNodeYByAction(action));
        nNode.setStatus(getNextNodeStatusByAction(action));
        nNode.setGoal(goal);

        nNode.updatehScore();
        nNode.updategScore();
        nNode.updatepScore();

        return nNode;
    }
    public Status getNextNodeStatusByAction(Motion.Action action){
        switch (action){
            case ROTATE_LEFT:
                switch (getStatus()){
                    case ROBOT_LEFT: return Status.ROBOT_DOWN;
                    case ROBOT_RIGHT:return Status.ROBOT_UP;
                    case ROBOT_UP:   return Status.ROBOT_LEFT;
                    default:         return Status.ROBOT_RIGHT;
                }
            case ROTATE_RIGHT:
                switch (getStatus()){
                    case ROBOT_LEFT: return Status.ROBOT_UP;
                    case ROBOT_RIGHT:return Status.ROBOT_DOWN;
                    case ROBOT_UP:   return Status.ROBOT_RIGHT;
                    default:         return Status.ROBOT_LEFT;
                }
            case ROTATE_BACK:
                switch (getStatus()){
                    case ROBOT_LEFT: return Status.ROBOT_RIGHT;
                    case ROBOT_RIGHT:return Status.ROBOT_LEFT;
                    case ROBOT_UP:   return Status.ROBOT_DOWN;
                    default:         return Status.ROBOT_UP;
                }
            default: return getStatus();
        }
    }
    public int getNextNodeXByAction(Motion.Action action){
        switch (action){
            case ROTATE_LEFT: return getX();
            case ROTATE_RIGHT:return getX();
            case ROTATE_BACK: return getX();
            case NONE:        return getX();
        }
        switch (getStatus()){
            case ROBOT_LEFT: return getX()-1;
            case ROBOT_RIGHT:return getX()+1;
            default:         return getX();
        }
    }
    public int getNextNodeYByAction(Motion.Action action){
        switch (action){
            case ROTATE_LEFT: return getY();
            case ROTATE_RIGHT:return getY();
            case ROTATE_BACK: return getY();
            case NONE:        return getY();
        }
        switch (getStatus()){
            case ROBOT_UP:  return getY()+1;
            case ROBOT_DOWN:return getY()-1;
            default:        return getY();
        }
    }

    private void updatehScore(){
        hScore = Map.getEstimatePathCost(this,goal);
    }
    private void updategScore(){
        gScore = getPreviousNode().getgScore() + Motion.getActionPathCost(actionToGetThis);
    }
    private void updatepScore(){
        pScore = getPreviousNode().getpScore() + Motion.getPenaltyCost(previousNode.getActionToGetThis(), actionToGetThis);
    }


    public boolean isGoal(){
        if ((actionToGetThis == Motion.Action.SPEED_UP) | (actionToGetThis == Motion.Action.MOVE_CONSTANT))
            return false;
        else
            return ((isCoincident(this,goal)));
    }




    public List<Point> getPointsFromNode(){
        List<Point> pointList = new ArrayList<>();
        switch (actionToGetThis){
            case ROTATE_BACK:
                pointList.add(new Point(getX(),getY(), getNextNodeStatusByAction(Motion.Action.ROTATE_LEFT)));
                break;
            case SPEED_DOWN:
                pointList.add(new Point(this));
                break;
            case SPEED_UP:
                pointList.add(new Point(previousNode));
                break;
            case STEP:
                pointList.add(new Point(this));
        }
        pointList.add(new Point(getX(),getY(),getStatus()));
        return pointList;
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

    public int getpScore() {
        return pScore;
    }

    public void setpScore(int pScore) {
        this.pScore = pScore;
    }

    public int getfScore(){return gScore+hScore+pScore;}

    public void print(){
        String info = "x=" + getX() + ", y=" + getY() + ", status=" + getStatus() + ", timeArrived=" + getTimeArrived() +"***" + getActionToGetThis()
                    + "***" + getfScore() +"=" + getgScore() + "+"+ gethScore();
        System.out.println(info);
    }
}
