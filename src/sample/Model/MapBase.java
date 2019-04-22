package sample.Model;

import sample.Creator.MapCreator;

import java.util.ArrayList;
import java.util.List;

public class MapBase {
    public static int xLength, yLength;
    private PointInfo[][] pointInfoMatrix;

    public MapBase() {}
    public MapBase(MapCreator mapCreator){
        this.pointInfoMatrix = mapCreator.getMapBase().getPointInfoMatrixClone();
    }


    public PointInfo[][] getPointInfoMatrix() {
        return pointInfoMatrix;
    }
    public void setPointInfoMatrix(PointInfo[][] pointInfoMatrix) {
        this.pointInfoMatrix = pointInfoMatrix;
    }


    public void setStatus(int x, int y, PointInfo.Status status){ getPointInfo(x,y).setStatus(status);}
    public PointInfo.Status getStatus(int x, int y){ return getPointInfo(x,y).getStatus();}
    public PointInfo getPointInfo(int x, int y){
        return pointInfoMatrix[x][y];
    }


    private PointInfo[][] getPointInfoMatrixClone(){
        PointInfo[][] cloneMatrix = new PointInfo[pointInfoMatrix.length][pointInfoMatrix[0].length];
        for(int i = 0; i< pointInfoMatrix.length; i++){
            for(int j = 0; j< pointInfoMatrix[i].length; j++) {
                cloneMatrix[i][j] = new PointInfo(pointInfoMatrix[i][j]);
            }
        }
        return cloneMatrix;
    }

    public static int getEstimatePathCost(Point start, Point goal){
        int deltaX = start.getX() - goal.getX();
        int deltaY = start.getY() - goal.getY();
        return (Math.abs(deltaX)+Math.abs(deltaY));
    }

    public static List<Node> getNeighborNodeList(Node node){
        List<Node> neighborNodeList = new ArrayList<>();

        Motion.Action[] suitableActions = Motion.getSuitableActions(node.getActionToGetThis());

        for (int i = 0; i < suitableActions.length; i++) {
        }





        return neighborNodeList;
    }


}
