package sample.Creator;

import sample.Model.Map;
import sample.Model.PointInfo;

public class MapCreator {
    private Shelf shelf;
    private Distance distance;
    private Bound bound;
    private Map map;

    public MapCreator() {
        shelf    = new Shelf();
        distance = new Distance();
        bound    = new Bound();
        map      = new Map();
    }

    public void create(){
        bound.update(distance,shelf);
        Map.xLength = bound.xLength;
        Map.yLength = bound.yLength;

        map.setPointInfos(new PointInfo[Map.xLength][Map.yLength]);
        for (int i = 0; i < bound.xLength ; i++)
            for (int j = 0; j < bound.yLength; j++)
                map.getPointInfos()[i][j] = new PointInfo();


        for (int i = 0; i < shelf.xLength; i++) {
            for (int j = 0; j < shelf.yLength; j++) {
                for (int k = 0; k < shelf.xNumber; k++) {
                    for (int l = 0; l < shelf.yNumber; l++) {
                        int x = distance.boundToVerticalShelf + i + (distance.shelfToVerticalShelf+shelf.xLength)*k;
                        int y = distance.boundToHorizontalShelf + j + (distance.shelfToHorizontalShelf+shelf.yLength)*l;
                        map.getPointInfoByXY(x,y).setStatus(PointInfo.Status.SHELF);
                    }
                }
            }
        }

    }

    public Shelf getShelf() {
        return shelf;
    }
    public Distance getDistance() {
        return distance;
    }
    public Map getMap() {
        return map;
    }
    public Map getMapBaseClone() {return new Map(this);}

    public class Shelf{
        private int xLength, yLength;
        private int xNumber, yNumber;

        public Shelf() {
        }

        public void setxLength(int xLength) {
            this.xLength = xLength;
        }
        public void setyLength(int yLength) {
            this.yLength = yLength;
        }
        public void setxNumber(int xNumber) {
            this.xNumber = xNumber;
        }
        public void setyNumber(int yNumber) {
            this.yNumber = yNumber;
        }
    }
    public class Distance {
        private int boundToVerticalShelf, boundToHorizontalShelf;
        private int shelfToVerticalShelf, shelfToHorizontalShelf;

        public Distance() {
        }

        public void setBoundToVerticalShelf(int boundToVerticalShelf) {
            this.boundToVerticalShelf = boundToVerticalShelf;
        }
        public void setBoundToHorizontalShelf(int boundToHorizontalShelf) {
            this.boundToHorizontalShelf = boundToHorizontalShelf;
        }
        public void setShelfToVerticalShelf(int shelfToVerticalShelf) {
            this.shelfToVerticalShelf = shelfToVerticalShelf;
        }
        public void setShelfToHorizontalShelf(int shelfToHorizontalShelf) {
            this.shelfToHorizontalShelf = shelfToHorizontalShelf;
        }
    }
    private class Bound {
        private int xLength, yLength;
        private Bound() {
        }

        private void update(Distance distance, Shelf shelf) {
            this.xLength = distance.boundToVerticalShelf*2 + shelf.xNumber*(shelf.xLength + distance.shelfToVerticalShelf) - distance.shelfToVerticalShelf;
            this.yLength = distance.boundToHorizontalShelf*2 + shelf.yNumber*(shelf.yLength + distance.shelfToHorizontalShelf) - distance.shelfToHorizontalShelf;
        }

    }
}
