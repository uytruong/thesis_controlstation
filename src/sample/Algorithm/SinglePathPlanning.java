package sample.Algorithm;

import sample.Manager.MapManager;
import sample.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SinglePathPlanning {
    private final MapBase[] virtualMapList;
    private Node  start;
    private Point goal;
    private int timeStart;

    private List<Node> openList;
    private List<Node> closeList;

    public SinglePathPlanning(MapManager mapManager) {
        virtualMapList = mapManager.getMapList().clone() ;
    }

    public void setData(Point start, Point goal, int timeStart){
        this.start     = new Node(start,goal,timeStart);
        this.goal      = goal;
        this.timeStart = timeStart;
    }

    private void initialize(){
        openList  = new ArrayList<>();
        closeList = new ArrayList<>();
        openList.add(start);

    }

    public boolean execute(){
        initialize();
        while (openList.size() != 0){
            /**
             * Finding the lowest fScore value in openList.
             * If it and goal are coincident then return the PATH
             * */

            /**
             * Else: remove from openList, add To closeList
             * */

            /**
             * With each neighbor in current Node, add it to openList and find its the gScore, hScore, fScore.
             * */

        }


        return false;
    }


}
