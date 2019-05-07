package sample.Creator;

import sample.Manager.Context;
import sample.Model.Map;
import sample.Model.Point;
import sample.Model.PointInfo;

import java.util.ArrayList;
import java.util.List;

public class MapCreator {
    public static Map map = new Map();


    public static void create(){
        Bound.update();
        Area.update();
        Map.xLength = Bound.xLength;
        Map.yLength = Bound.yLength;

        map.setPointInfos(new PointInfo[Map.xLength][Map.yLength]);
        for (int i = 0; i < Bound.xLength ; i++)
            for (int j = 0; j < Bound.yLength; j++)
                map.getPointInfos()[i][j] = new PointInfo();


        for (int i = 0; i < Shelf.xLength; i++) {
            for (int j = 0; j < Shelf.yLength; j++) {
                for (int k = 0; k < Shelf.xNumber; k++) {
                    for (int l = 0; l < Shelf.yNumber; l++) {
                        int x = Distance.boundToVerShelf + i + (Distance.shelfToVerShelf +Shelf.xLength)*k;
                        int y = Distance.boundToHorShelf + j + (Distance.shelfToHorShelf +Shelf.yLength)*l;
                        map.getPointInfoByXY(x,y).setStatus(PointInfo.Status.SHELF);
                    }
                }
            }
        }
    }

    public static class Shelf{
        public static int xLength, yLength;
        public static int xNumber, yNumber;
    }
    public static class Distance {
        public static int boundToVerShelf, boundToHorShelf;
        public static int shelfToVerShelf, shelfToHorShelf;
    }
    private static class Bound {
        private static int xLength, yLength;

        private static void update() {
            xLength = Distance.boundToVerShelf *2 + Shelf.xNumber*(Shelf.xLength + Distance.shelfToVerShelf) - Distance.shelfToVerShelf;
            yLength = Distance.boundToHorShelf *2 + Shelf.yNumber*(Shelf.yLength + Distance.shelfToHorShelf) - Distance.shelfToHorShelf;
        }
    }
    private static class Area{

        private int xStart, xMiddle, xEnd, x;
        private int yStart, yMiddle, yEnd, y;

        private static int xLength,yLength;
        private static List<Integer> xStartList;
        private static List<Integer> xMiddleList;
        private static List<Integer> yStartList;
        private static List<Integer> yMiddleList;
        private static List<Integer> xEndList;
        private static List<Integer> yEndList;

        private static void update(){
            System.out.println(Bound.xLength + " " + Bound.yLength);

            xLength = Shelf.xLength + Distance.shelfToVerShelf;
            yLength = Shelf.yLength + Distance.shelfToHorShelf;

            xStartList  = new ArrayList<>();
            yStartList  = new ArrayList<>();
            xMiddleList = new ArrayList<>();
            yMiddleList = new ArrayList<>();
            xEndList = new ArrayList<>();
            yEndList = new ArrayList<>();
            for (int x = 0; x < Distance.boundToVerShelf; x++) {
                xStartList.add(0);
                xMiddleList.add(0);
                xEndList.add(Distance.boundToVerShelf-1);
            }
            for (int x = Distance.boundToVerShelf; x < Bound.xLength; x++) {
                int k       = (x-Distance.boundToVerShelf)/ Area.xLength;
                int xStart  = Distance.boundToVerShelf +k*(Area.xLength);
                xStartList.add(xStart);
                xMiddleList.add(xStart+Shelf.xLength);
                xEndList.add((xStart+Area.xLength-1));
            }

            for (int y = 0; y < Distance.boundToHorShelf; y++) {
                yStartList.add(0);
                yMiddleList.add(0);
                yEndList.add((Distance.boundToHorShelf-1));
            }
            for (int y = Distance.boundToHorShelf; y < Bound.yLength; y++) {
                int k       = (y-Distance.boundToHorShelf)/ Area.yLength;
                int yStart  = Distance.boundToVerShelf +k*(Area.yLength);
                yStartList.add(yStart);
                yMiddleList.add(yStart+Shelf.yLength);
                yEndList.add((yStart+Area.yLength-1));
            }

        }

        private Area(Point point){
            x = point.getX();
            y = point.getY();

            if(x<0)
                x=0;
            else if(x>=Map.xLength)
                x = Map.xLength-1;
            if(y<0)
                y=0;
            else if(y>=Map.yLength)
                y = Map.yLength-1;

            xStart  = xStartList.get(x);
            xMiddle = xMiddleList.get(x);
            yStart  = yStartList.get(y);
            yMiddle = yMiddleList.get(y);
            xEnd    = xEndList.get(x);
            yEnd    = yEndList.get(y);
        }
    }

    public static int getEstimatePathCost(Point start, Point goal){
        //return getEstimatePathCost(start,goal);
        return getEstimatePathCost1(start,goal);
        //return (Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY()) + 1);
    }
    public static int getEstimatePathCost2(Point start, Point goal){
        return (Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY()));
    }

    public static int getEstimatePathCost1(Point start, Point goal){
        Area area1 = new Area(start);
        Area area2 = new Area(goal);

        if((area1.xStart == area2.xStart) & (area1.x<area1.xMiddle) & (area2.x<area2.xMiddle)){
            if (!((area2.y>=area1.yMiddle) & (area2.y<=area1.yEnd))){
                int x1 = area1.xStart - 1;
                int x2 = area1.xMiddle;
                return Math.min(area1.x + area2.x - 2*x1, 2*x2 - area1.x - area2.x) + Math.abs(area1.y-area2.y);
            }
        }

        else if((area1.yStart == area2.yStart) & (area1.y < area1.yMiddle) & (area2.y<area2.yMiddle)){
            if (!((area2.x>=area1.xMiddle) & (area2.x<=area1.xEnd))){
                int y1 = area1.yStart - 1;
                int y2 = area1.yMiddle;
                return Math.min(area1.y + area2.y - 2*y1, 2*y2 - area1.y - area2.y) + Math.abs(area1.x-area2.x);
            }
        }
        return (Math.abs(area1.x - area2.x) + Math.abs(area1.y - area2.y));
    }

}
