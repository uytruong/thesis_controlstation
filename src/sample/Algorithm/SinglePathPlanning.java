package sample.Algorithm;

import sample.Manager.MapData;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SinglePathPlanning {
    private final Map[]     maps;

    private Node  startNode, goalNode;
    private List<Point> pointList = new ArrayList<>();
    private List<Node>  openList  = new ArrayList<>();
    private List<Node>  closeList = new ArrayList<>();

    public SinglePathPlanning(MapData mapData, Point start, Point goal, int timeStart){
        this.maps      = mapData.getMaps();
        this.startNode = new Node(start,goal,timeStart);
        openList.add(startNode);
    }

    public boolean calculate(){
        int     timeLoop = 0;
        boolean loop     = true;
        while (loop){
            timeLoop++;
            /*Finding the lowest fScore value in openList*/
            int minfScore      = openList.get(0).getfScore();
            int minfScoreIndex = 0;
            for (int i = 1; i < openList.size(); i++) {
                if (minfScore > openList.get(i).getfScore()) {
                    minfScoreIndex = i;
                    minfScore      = openList.get(i).getfScore();
                }
            }

            Node node = openList.get(minfScoreIndex);

            /**
             * If it and goal are coincident then return the PATH
             * Else: remove from openList, add To closeList
             * */
            if (node.isGoal()) {
                goalNode = node;
                return true;
            }
            else{
                closeList.add(node);
                openList.remove(minfScoreIndex);
            }
            /** With each neighbor in current Node, add it to openList and find its the gScore, hScore, fScore.**/
            openList.addAll(getNextNodes(node));


            if((timeLoop >= Config.timeLoopMax) | (openList.size() == 0))
                loop = false;
        }
        return false;
    }

    public void calculateCorrespondPoints(){
        Node node = goalNode;
        while (node != startNode) {
            List<Point> pointsFromNode = node.getPointsFromNode();
            for (int i = pointsFromNode.size() - 1; i >= 0; i--) {
                pointList.add(0, pointsFromNode.get(i));
            }
            node = node.getPreviousNode();
        }
        pointList.add(0, startNode);
    }




    private List<Node> getNextNodes(Node node){
        /**
         * get Suitable actions which fit to previous action !!!
         * */
        Motion.Action[] suitableActions = Motion.getSuitableActions(node.getActionToGetThis());
        List<Node>      nextNodes       = new ArrayList<>();
        /**
         * get each neighbor Node corresponding to each suitable action !!!
         * check in the map if it's blocked or not to add its to <neighborNodes> !!!!
         * */
        for (Motion.Action action: suitableActions) {
            Node suitableNode             = node.getNextNodeByAction(action);
            List<Point> pointListFromNode = suitableNode.getPointsFromNode();

            boolean valid = true;
            for (int i = 0; i < pointListFromNode.size(); i++) {
                int x       = pointListFromNode.get(i).getX();
                int y       = pointListFromNode.get(i).getY();
                int tOffset = suitableNode.getTimeArrived()-pointListFromNode.size()+1;
                if (!getMapByTime(tOffset+i).getPointInfoByXY(x,y).isEmpty()){
                    valid = false;
                    break;
                }
                if ((action == Motion.Action.SPEED_UP) | (action == Motion.Action.MOVE_CONSTANT)){
                    Node        farFutureNode           = suitableNode.getNextNodeByAction(Motion.Action.SPEED_DOWN);
                    List<Point> pointListFromFutureNode = farFutureNode.getPointsFromNode();
                    for (int j = 0; j < pointListFromNode.size(); j++) {
                        int farFutureX       = pointListFromFutureNode.get(j).getX();
                        int farFutureY       = pointListFromFutureNode.get(j).getY();
                        int farFutureTOffest = farFutureNode.getTimeArrived() - pointListFromFutureNode.size() + 1;
                        if (!getMapByTime(farFutureTOffest+j).getPointInfoByXY(farFutureX,farFutureY).isEmpty()){
                            valid = false;
                            break;
                        }
                    }
                }
            }
            if(valid)
                nextNodes.add(suitableNode);
        }
        return nextNodes;
    }
    private Map getMapByTime(int time){
        return maps[time];
    }
    public List<Point> getPointList() {
        return pointList;
    }

    public static class Config{
        public static int timeLoopMax = 200;

    }
}
