package sample.Model;

import sample.Creator.MapCreator;

import java.util.ArrayList;
import java.util.List;

public class Node extends Point {
    private Motion.Action actionToGetThis;
    private int           gScore, hScore, pScore;

    private Node          previousNode;
    private Point         goal;
    private int           timeArrived;

    private Node(){
    }


    public static Node getStartNode(Robot robot){
        Node node   = new Node();

        Point point = robot.getLastPoint();
        node.setX(point.getX());
        node.setY(point.getY());
        node.setStatus(point.getStatus());
        node.setRobot(robot);

        node.setActionToGetThis(Motion.Action.NONE);
        node.setTimeArrived(robot.getLastTimeBusy());

        node.setGoal(robot.getTask().getGoal());
        node.setgScore(0);
        node.setpScore(0);
        node.updateHScore();

        return node;
    }

    public Node getNeighborNodeByAction(Motion.Action action){
        Node nNode = new Node();

        nNode.setActionToGetThis(action);
        nNode.setGoal(goal);
        nNode.setTimeArrived(timeArrived+Motion.getActionTimeCost(action));
        nNode.setPreviousNode(this);

        nNode.setX(getNeighborNodeXByAction(action));
        nNode.setY(getNeighborNodeYByAction(action));
        nNode.setStatus(getNeighborNodeHeadingByAction(action));
        nNode.setRobot(getRobot());

        nNode.updateHScore();
        nNode.updateGScore();
        nNode.updatePScore();

        return nNode;
    }
    private Status getNeighborNodeHeadingByAction(Motion.Action action){
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
    private int getNeighborNodeXByAction(Motion.Action action){
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
    private int getNeighborNodeYByAction(Motion.Action action){
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

    private void updateHScore(){
        //hScore = Map.getEstimatePathCost(this,goal);
        //hScore = MapCreator.getEstimatePathCost(this,goal);
        hScore = MapCreator.getEstimatePathCost2(this,goal);
    }
    private void updateGScore(){
        gScore = getPreviousNode().getgScore() + Motion.getActionPathCost(actionToGetThis);
    }
    private void updatePScore(){
        pScore = getPreviousNode().getpScore() + Motion.getPenaltyCost(previousNode.getActionToGetThis(), actionToGetThis);
    }

    public boolean isGoal(){
        if ((actionToGetThis == Motion.Action.SPEED_UP) | (actionToGetThis == Motion.Action.MOVE_CONSTANT))
            return false;
        else
            return (((isCoincident(this,goal))) & (headingSameDirection(this,goal)));
    }

    public List<Point> getCorrespondPoints(){
        List<Point> correspondPoints = new ArrayList<>();
        switch (actionToGetThis){
            case ROTATE_BACK:
                Point middlePoint = new Point(this);
                middlePoint.setStatus(getNeighborNodeHeadingByAction(Motion.Action.ROTATE_LEFT));
                correspondPoints.add(middlePoint);
                break;
            case SPEED_DOWN:
                correspondPoints.add(new Point(this));
                break;
            case SPEED_UP:
                correspondPoints.add(new Point(previousNode));
                break;
            case STEP:
                correspondPoints.add(new Point(this));
        }
        correspondPoints.add(new Point(this));
        return correspondPoints;
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
