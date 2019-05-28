package sample.Algorithm;
import sample.Manager.MapData;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SinglePathPlanning {
    private MapData     mapData;

    private Node        startNode, goalNode;
    private List<Node>  openList      = new ArrayList<>();
    private List<Node>  closeList     = new ArrayList<>();
    private List<Point> planPointList = new ArrayList<>();

    public SinglePathPlanning(Robot robot,MapData mapData){
        this.mapData   = mapData;
        this.startNode = Node.getStartNode(robot);
        this.openList.add(startNode);
    }

    public boolean execute(){
        int timeLoop = 0;
        while (openList.size() != 0){
            timeLoop++;

            float nearestNodefScore = openList.get(0).getfScore();
            int   nearestNodeIndex  = 0;
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

            openList.addAll(getNeighborNodes(node));

            if(timeLoop == Config.timeLoopMax){
                break;
            }
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
    }



    private List<Node> getNeighborNodes(Node node){
        Motion.Action[] suitableActions = Motion.getSuitableActions(node.getActionToGetThis());
        List<Node>      neighborNodes   = new ArrayList<>();

        for (Motion.Action action: suitableActions) {
            Node        suitableNode     = node.getNeighborNodeByAction(action);
            List<Point> correspondPoints = suitableNode.getCorrespondPoints();
            int         timeOffset       = suitableNode.getTimeArrived()- correspondPoints.size()+1;

            boolean emptyToGo = true;
            Point   prePoint  = suitableNode.getPreviousNode().getPointClone();
            for (int idx = 0; idx < correspondPoints.size(); idx++) {
                int   timeOfPoint = timeOffset + idx;
                Point point       = correspondPoints.get(idx);

                emptyToGo = mapData.isEmptyToMoveIn(prePoint,point,timeOfPoint);
                if(!emptyToGo){
                    break;
                }
                prePoint = point;
            }
            if(emptyToGo){
                neighborNodes.add(suitableNode);
            }
        }
        return neighborNodes;
    }

    public List<Point> getPlanPointList(){ return planPointList;}
    public int getPathPlanningCost(){return goalNode.getgScore();}

    private static class Config{
        private static int timeLoopMax = 5000;
    }
}
