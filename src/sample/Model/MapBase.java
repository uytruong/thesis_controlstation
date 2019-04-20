package sample.Model;

import sample.Creator.MapBaseCreator;



public class MapBase {
    private Point[][] pointMatrix;
    public static int xLength, yLength;

    public MapBase() {}
    public MapBase(MapBaseCreator mapBaseCreator){
        this.pointMatrix = mapBaseCreator.getMapBase().getPointMatrixClone();
    }

    public Point[][] getPointMatrix() {
        return pointMatrix;
    }
    public void setPointMatrix(Point[][] pointMatrix) {
        this.pointMatrix = pointMatrix;
    }

    public int getStatus(int id){ return getPoint(id).getStatus();}
    public void setStatus(int id, int status){ getPoint(id).setStatus(status);}
    public int getStatus(Point point){ return getPoint(point.getId()).getStatus();}






    public Point getPoint(int id){
        return pointMatrix[getXFromId(id)][getYFromId(id)];
    }
    public Point[][] getPointMatrixClone(){
        Point[][] cloneMatrix = new Point[pointMatrix.length][pointMatrix[0].length];
        for(int i=0; i<pointMatrix.length; i++){
            for(int j=0; j<pointMatrix[i].length; j++) {
                cloneMatrix[i][j] = new Point(pointMatrix[i][j]);
            }
        }
        return cloneMatrix;
    }

    public static int getIdFromXY(int x, int y){
        return (x + y*xLength);
    }
    public static int getXFromId(int id){
        return id%xLength;
    }
    public static int getYFromId(int id){
        return id/xLength;
    }
    public static int getEstimateCost(Point start,Point goal){
        int deltaX = getXFromId(start.getId()) - getXFromId(goal.getId());
        int deltaY = getYFromId(start.getId()) - getYFromId(goal.getId());
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
