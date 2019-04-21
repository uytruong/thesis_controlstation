package sample.Model;

import sample.Creator.MapCreator;

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


    public void setStatus(int x, int y, int status){ getPointInfo(x,y).setStatus(status);}
    public int getStatus(int x, int y){ return getPointInfo(x,y).getStatus();}
    public PointInfo getPointInfo(int x, int y){
        return pointInfoMatrix[x][y];
    }
    public void setPointInfo(int x, int y, PointInfo pointInfo){
        pointInfoMatrix[x][y] = pointInfo;
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

    public static Point getForwardPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        int h = point.getStatus();
        Point fwPoint = new Point(point);
        switch (h){
            case Constant.PointStatus.ROBOT_RIGHT:
                if(x == xLength){return null;}
                else{
                    fwPoint.setX(x+1);
                }
                break;
            case Constant.PointStatus.ROBOT_LEFT:
                if(x == 0){return null;}
                else{
                    fwPoint.setX(x-1);
                }
                break;
            case Constant.PointStatus.ROBOT_UP:
                if(y == yLength){return null;}
                else{
                    fwPoint.setY(y+1);
                }
                break;
            default:
                if(y == 0){return null;}
                else{
                    fwPoint.setY(y-1);
                }
                break;
        }
        return fwPoint;
    }


}
