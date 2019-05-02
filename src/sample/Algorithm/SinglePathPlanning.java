package sample.Algorithm;
import sample.Manager.MapData;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SinglePathPlanning {
    private MapData     mapData;
    private Robot       robot;
    private Task        task;

    private Node        startNode, goalNode;
    private List<Node>  openList      = new ArrayList<>();
    private List<Node>  closeList     = new ArrayList<>();
    private List<Point> planPointList = new ArrayList<>();
    private boolean     success       = true;

    public SinglePathPlanning(Robot robot,MapData mapData){
        this.robot     = robot;
        this.mapData   = mapData;
        this.task      = robot.getTask();

        this.startNode = Node.getStartNode(robot);
        this.openList.add(startNode);
    }

    public boolean execute(){
        int     timeLoop = 0;
        boolean loop     = true;
        while (loop){
            timeLoop++;

            /*Finding the lowest fScore value in openList*/
            int nearestNodefScore = openList.get(0).getfScore();
            int nearestNodeIndex  = 0;
            for (int i = 1; i < openList.size(); i++) {
                if (nearestNodefScore > openList.get(i).getfScore()) {
                    nearestNodeIndex  = i;
                    nearestNodefScore = openList.get(i).getfScore();
                }
            }
            Node node = openList.get(nearestNodeIndex);

            if (node.isGoal()){
                goalNode = node;
                calculatePlanPointList();
                task.setCostOfRobotPath(goalNode.getgScore());
                return true;
            }
            else{
                closeList.add(node);
                openList.remove(nearestNodeIndex);
            }
            /** With each neighbor in current Node, add it to openList and find its the gScore, hScore, fScore.**/
            openList.addAll(getNeighborNodes(node));


            if((timeLoop >= Config.timeLoopMax) | (openList.size() == 0))
                loop = false;
        }
        success = false;
        return false;
    }

    private void calculatePlanPointList(){
        Node  node  = goalNode;
        while(node != startNode) {
            List<Point> correspondPoints = node.getCorrespondPoints();
            for (int i = correspondPoints.size() - 1; i >= 0; i--)
                planPointList.add(0, correspondPoints.get(i));
            node = node.getPreviousNode();
        }
        for (int i = 0; i < task.getTimeExecute(); i++) {
            planPointList.add(goalNode.getPointClone());
        }
    }



    private List<Node> getNeighborNodes(Node node){
        /**
         * get Suitable actions which fit to previous action !!!
         * */
        Motion.Action[] suitableActions = Motion.getSuitableActions(node.getActionToGetThis());
        List<Node>      neighborNodes   = new ArrayList<>();
        /**
         * get each neighbor Node corresponding to each suitable action !!!
         * check in the map if it's blocked or not to add its to <neighborNodes> !!!!
         * */
        for (Motion.Action action: suitableActions) {
            Node        suitableNode     = node.getNeighborNodeByAction(action);
            List<Point> correspondPoints = suitableNode.getCorrespondPoints();

            boolean valid = true;
            for (int idx = 0; idx < correspondPoints.size(); idx++) {
                int x       = correspondPoints.get(idx).getX();
                int y       = correspondPoints.get(idx).getY();
                int tOffset = suitableNode.getTimeArrived()-correspondPoints.size()+1;
                if (!mapData.getMapByTime(tOffset+idx).getPointInfoByXY(x,y).isEmpty()){
                    valid = false;
                    break;
                }
                if(valid){
                    if ((action == Motion.Action.SPEED_UP) | (action == Motion.Action.MOVE_CONSTANT)){
                        Node        farFutureNode                  = suitableNode.getNeighborNodeByAction(Motion.Action.SPEED_DOWN);
                        List<Point> correspondPointsFromFutureNode = farFutureNode.getCorrespondPoints();
                        for (int j = 0; j < correspondPoints.size(); j++) {
                            int farFutureX       = correspondPointsFromFutureNode.get(j).getX();
                            int farFutureY       = correspondPointsFromFutureNode.get(j).getY();
                            int farFutureTOffest = farFutureNode.getTimeArrived() - correspondPointsFromFutureNode.size() + 1;
                            if (!mapData.getMapByTime(farFutureTOffest+j).getPointInfoByXY(farFutureX,farFutureY).isEmpty()){
                                valid = false;
                                break;
                            }
                        }
                    }
                }
            }
            if(valid)
                neighborNodes.add(suitableNode);
        }
        return neighborNodes;
    }


    public List<Point> getPlanPointList(){ return planPointList;}
    public boolean isSuccess() {
        return success;
    }
    public int getPathPlanningCost(){return goalNode.getgScore();}
    public static class Config{
        public static int timeLoopMax = Integer.MAX_VALUE;

    }


}
