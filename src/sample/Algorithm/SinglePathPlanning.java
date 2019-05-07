package sample.Algorithm;
import sample.Manager.MapData;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SinglePathPlanning {
    private MapData     mapData;
    private Task        task;

    private Node        startNode, goalNode;
    private List<Node>  openList      = new ArrayList<>();
    private List<Node>  closeList     = new ArrayList<>();
    private List<Point> planPointList = new ArrayList<>();

    public SinglePathPlanning(Robot robot,MapData mapData){
        this.mapData   = mapData;
        this.task      = robot.getTask();

        this.startNode = Node.getStartNode(robot);
        this.openList.add(startNode);
    }

    public boolean execute(){
        while (openList.size() != 0){
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
                return true;
            }
            else{
                closeList.add(node);
                openList.remove(nearestNodeIndex);
            }
            /** With each neighbor in current Node, add it to openList and find its the gScore, hScore, fScore.**/
            openList.addAll(getNeighborNodes(node));
        }
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

            boolean emptyToGo = true;
            Point   prePoint  = node;
            for (int idx = 0; idx < correspondPoints.size(); idx++) {
                Point point     = correspondPoints.get(idx);
                int timeOffset  = suitableNode.getTimeArrived()- correspondPoints.size()+1;
                int timeOfPoint = timeOffset + idx;

                emptyToGo = mapData.emptyToGo(prePoint,point,timeOfPoint);
                if(!emptyToGo)
                    break;
                prePoint = point;
            }
            if(emptyToGo)
                neighborNodes.add(suitableNode);
        }
        return neighborNodes;
    }


    public List<Point> getPlanPointList(){ return planPointList;}
    public int getPathPlanningCost(){return goalNode.getgScore();}



}
