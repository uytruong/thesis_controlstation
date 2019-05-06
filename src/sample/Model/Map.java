package sample.Model;
import sample.Creator.MapCreator;

import java.util.*;

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
        return (Math.abs(deltaX) + Math.abs(deltaY));
    }

    public static int getEstimatePathCost2(Point start, Point goal, Map map){
        Integer[][] mapAreaIndex = new Integer[xLength][yLength];
        List<Integer> shelfHorizontalLineIndexList = new ArrayList<Integer>();
        List<Integer> shelfVerticalLineIndexList = new ArrayList<Integer>();

        // Determine the horizontal and vertical line index of shelves
        String pointStatus;
        Boolean isDuplicate = false;
        for (int rowIdx = 0; rowIdx < xLength; rowIdx++) {
            for (int colIdx = 0; colIdx < yLength; colIdx++) {
                pointStatus = map.getPointInfoByXY(rowIdx, colIdx).getStatus().getString();
                if (pointStatus.equals("SHELF")) {
                    // Checking for duplicate index in two lists before adding new index
                    shelfHorizontalLineIndexList.add(rowIdx);
                    shelfVerticalLineIndexList.add(colIdx);
                }
            }
        }

        // Clear duplicate elements in the two lists
        Set<Integer> horizontalSet = new HashSet<>(shelfHorizontalLineIndexList);
        shelfHorizontalLineIndexList.clear();
        shelfHorizontalLineIndexList.addAll(horizontalSet);
        Set<Integer> verticalSet = new HashSet<>(shelfVerticalLineIndexList);
        shelfVerticalLineIndexList.clear();
        shelfVerticalLineIndexList.addAll(verticalSet);

        // Assign area index for each points (0 - shelf, 1 - cross, 2 - horizontal, 3 - vertical)
        Boolean isInHorizontalList = false;
        Boolean isInVerticalList = false;
        for (int rowIdx = 0; rowIdx < xLength; rowIdx++) {
            for (int colIdx = 0; colIdx < yLength; colIdx++) {
                pointStatus = map.getPointInfoByXY(rowIdx, colIdx).getStatus().getString();
                if (pointStatus.equals("NONE")) {
                    isInHorizontalList = shelfHorizontalLineIndexList.contains(rowIdx);
                    isInVerticalList = shelfVerticalLineIndexList.contains(colIdx);
                    if (!isInHorizontalList && !isInVerticalList) {
                        mapAreaIndex[rowIdx][colIdx] = 1;
                    }
                    else if (isInHorizontalList) {
                        mapAreaIndex[rowIdx][colIdx] = 2;
                    }
                    else if (isInVerticalList) {
                        mapAreaIndex[rowIdx][colIdx] = 3;
                    }
                }
                else {
                    mapAreaIndex[rowIdx][colIdx] = 0;
                }
            }
        }

//        System.out.println("====== DEBUG ======");
//        for (int colIdx = 0; colIdx < yLength; colIdx++) {
//            for (int rowIdx = 0; rowIdx < xLength; rowIdx++) {
//                System.out.print(mapAreaIndex[rowIdx][colIdx] + " - ");
//            }
//            System.out.println('\n');
//        }
//        System.out.println("====== END DEBUG ======");

        // Heuristic functions for the relaxed problem:
        // cheapest path from a robot to a task considering shelf block and turning cost during the path
        int xStart = start.getX();
        int yStart = start.getY();
        int xGoal = goal.getX();
        int yGoal = goal.getY();
        int deltaX = xStart - xGoal;
        int deltaY = yStart - yGoal;
        int areaIndex;
        int pathCost = 0;
        Boolean isSameCol = false;
        Boolean isSameRow = false;
        if (mapAreaIndex[xStart][yStart] == 2 && mapAreaIndex[xGoal][yGoal] == 2) {
            // Check if start and goal are in same row block or col block
            if (yStart <= yGoal) {
                for (int colIdx = yStart; colIdx <= yGoal; colIdx++) {
                    isSameRow = true;
                    areaIndex = mapAreaIndex[xStart][colIdx];
                    if (areaIndex == 0) {
                        isSameRow = false;
                        break;
                    }
                }
            }
            else {
                for (int colIdx = yStart; colIdx >= yGoal; colIdx--) {
                    isSameRow = true;
                    areaIndex = mapAreaIndex[xStart][colIdx];
                    if (areaIndex == 0) {
                        isSameRow = false;
                        break;
                    }
                }
            }
            if (xStart <= xGoal) {
                for (int rowIdx = xStart; rowIdx <= xGoal; rowIdx++) {
                    isSameCol = true;
                    areaIndex = mapAreaIndex[rowIdx][yStart];
                    if (areaIndex == 1) {
                        isSameCol = false;
                        break;
                    }
                }
            }
            else {
                for (int rowIdx = xStart; rowIdx >= xGoal; rowIdx--) {
                    isSameCol = true;
                    areaIndex = mapAreaIndex[rowIdx][yStart];
                    if (areaIndex == 1) {
                        isSameCol = false;
                        break;
                    }
                }
            }

            if (isSameCol) {
                System.out.println("Same col: " + isSameCol);
                // Find the middle point in the path
                int leftDistanceStart = 0;      // Distance from xStart to temporary xMiddlePoint on the left
                int rightDistanceStart = 0;     // Distance from xStart to temporary xMiddlePoint on the right
                int leftDistanceGoal = 0;       // Distance from xGoal to temporary xMiddlePoint on the left
                int rightDistanceGoal = 0;      // Distance from xGoal to temporary xMiddlePoint on the right
                int minSumDistance = 0;
                for (int idx = xStart; idx >= 0; idx--) {
                    areaIndex = mapAreaIndex[idx][yStart];
                    if (areaIndex == 1) {
                        leftDistanceStart = Math.abs(xStart - idx);
                        leftDistanceGoal = Math.abs(xGoal - idx);
                        break;
                    }
                }
                for (int idx = xStart; idx < xLength - 1; idx++) {
                    areaIndex = mapAreaIndex[idx][yStart];
                    if (areaIndex == 1) {
                        rightDistanceStart = Math.abs(xStart - idx);
                        rightDistanceGoal = Math.abs(xGoal - idx);
                        break;
                    }
                }

                // Determine the middle point by finding the minimum distance on the left or on the right
                minSumDistance = Math.min(leftDistanceStart + leftDistanceGoal, rightDistanceStart + rightDistanceGoal);
                pathCost = minSumDistance + Math.abs(deltaY) + 2; // 2 is the cost for turning twice
                System.out.println("Min sum dist: " + minSumDistance);
            }
            else if (isSameRow) {
                System.out.println("Same row: " + isSameRow);
                // If deltaY equals zero then robot doesn't need to turn
                if (deltaY != 0) {
                    pathCost = Math.abs(deltaX) + Math.abs(deltaY) + 1; // 1 is the cost for turning once
                }
                else {
                    pathCost = Math.abs(deltaX) + Math.abs(deltaY);
                }
            }
            else {
                System.out.println("The other cases");
                pathCost = Math.abs(deltaX) + Math.abs(deltaY) + 2; // 2 is the cost for turning twice
            }
        }
        else if (mapAreaIndex[xStart][yStart] == 3 && mapAreaIndex[xGoal][yGoal] == 3) {
            System.out.println("3 - 3 case");
            // Check if start and goal are in same row block or col block
            if (yStart <= yGoal) {
                for (int colIdx = yStart; colIdx <= yGoal; colIdx++) {
                    isSameRow = true;
                    areaIndex = mapAreaIndex[xStart][colIdx];
                    if (areaIndex == 1) {
                        isSameRow = false;
                        break;
                    }
                }
            }
            else {
                for (int colIdx = yStart; colIdx >= yGoal; colIdx--) {
                    isSameRow = true;
                    areaIndex = mapAreaIndex[xStart][colIdx];
                    if (areaIndex == 1) {
                        isSameRow = false;
                        break;
                    }
                }
            }
            if (xStart <= xGoal) {
                for (int rowIdx = xStart; rowIdx <= xGoal; rowIdx++) {
                    isSameCol = true;
                    areaIndex = mapAreaIndex[rowIdx][yStart];
                    if (areaIndex == 0) {
                        isSameCol = false;
                        break;
                    }
                }
            }
            else {
                for (int rowIdx = xStart; rowIdx >= xGoal; rowIdx--) {
                    isSameCol = true;
                    areaIndex = mapAreaIndex[rowIdx][yStart];
                    if (areaIndex == 0) {
                        isSameCol = false;
                        break;
                    }
                }
            }

            if (isSameCol) {
                System.out.println("Same col: " + isSameCol);
                // If deltaY equals zero then robot doesn't need to turn
                if (deltaX != 0) {
                    pathCost = Math.abs(deltaX) + Math.abs(deltaY) + 1; // 1 is the cost for turning once
                }
                else {
                    pathCost = Math.abs(deltaX) + Math.abs(deltaY);
                }
            }
            else if (isSameRow) {
                System.out.println("Same row: " + isSameRow);
                // Find the middle point in the path
                int upDistanceStart = 0;      // Distance from xStart to temporary xMiddlePoint on the up
                int downDistanceStart = 0;     // Distance from xStart to temporary xMiddlePoint on the down
                int upDistanceGoal = 0;       // Distance from xGoal to temporary xMiddlePoint on the up
                int downDistanceGoal = 0;      // Distance from xGoal to temporary xMiddlePoint on the down
                int minSumDistance = 0;
                for (int idx = yStart; idx >= 0; idx--) {
                    areaIndex = mapAreaIndex[xStart][idx];
                    if (areaIndex == 1) {
                        downDistanceStart = Math.abs(yStart - idx);
                        downDistanceGoal = Math.abs(yGoal - idx);
                        break;
                    }
                }
                for (int idx = yStart; idx < yLength - 1; idx++) {
                    areaIndex = mapAreaIndex[xStart][idx];
                    if (areaIndex == 1) {
                        upDistanceStart = Math.abs(yStart - idx);
                        upDistanceGoal = Math.abs(yGoal - idx);
                        break;
                    }
                }

                // Determine the middle point by finding the minimum distance on the up or on the down
                minSumDistance = Math.min(upDistanceStart + upDistanceGoal, downDistanceStart + downDistanceGoal);
                pathCost = minSumDistance + Math.abs(deltaX) + 2; // 2 is the cost for turning twice
                System.out.println("Min sum dist: " + minSumDistance);
            }
            else {
                System.out.println("The other cases");
                pathCost = Math.abs(deltaX) + Math.abs(deltaY) + 2; // 2 is the cost for turning twice
            }
        }
        else {
            pathCost = Math.abs(deltaX) + Math.abs(deltaY) + 1; // 1 is the cost for turning once
        }
        System.out.println("Path cost: " + pathCost);
        return pathCost;
    }
}
