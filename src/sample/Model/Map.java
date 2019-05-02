package sample.Model;
import sample.Creator.MapCreator;

public class Map {
    public static int     xLength, yLength;
    private PointInfo[][] pointInfos;

    /*** CONSTRUCTOR**/
    public Map() {}
    public Map(MapCreator mapCreator){
        pointInfos = mapCreator.getMap().getPointInfosClone();
    }

    private PointInfo[][] getPointInfosClone(){
        PointInfo[][] clone = new PointInfo[pointInfos.length][pointInfos[0].length];
        for(int i = 0; i< pointInfos.length; i++){
            for(int j = 0; j< pointInfos[i].length; j++) {
                clone[i][j] = new PointInfo(pointInfos[i][j]);
            }
        }
        return clone;
    }
    /*** GETTER AND SETTER**/
    public PointInfo[][] getPointInfos() {
        return pointInfos;
    }
    public void setPointInfos(PointInfo[][] pointInfos) {
        this.pointInfos = pointInfos;
    }

    /**
     * GETTER AND SETTER
     * USER DEFINE
     * */
    public PointInfo getPointInfoByXY(int x, int y){
        PointInfo pointInfo = new PointInfo(PointInfo.Status.SHELF);
        if((x<0)|(x>xLength-1)|(y<0)|(y>yLength-1))
            return pointInfo;
        else
            return pointInfos[x][y];
    }
    public void setPointInfoByPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        PointInfo pointInfo = getPointInfoByXY(x,y);
        pointInfo.setStatus(point.getStatus());
        pointInfo.setRobot(point.getRobot());
    }

    public Map getMapClone(){
        Map clone = new Map();
        clone.setPointInfos(getPointInfosClone());
        return clone;
    }

    public static int getEstimatePathCost(Point start, Point goal){
        int deltaX = start.getX() - goal.getX();
        int deltaY = start.getY() - goal.getY();
        return (Math.abs(deltaX)+Math.abs(deltaY));
    }
}
