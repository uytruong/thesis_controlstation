package sample.Creator;

import sample.Model.MapBase;
import sample.Model.PointInfo;

public class MapCreator {
    private Shelf shelf;
    private Distance distance;
    private Bound bound;
    private MapBase mapBase;

    public MapCreator() {
        shelf    = new Shelf();
        distance = new Distance();
        bound    = new Bound();
        mapBase  = new MapBase();
    }

    public void create(){
        bound.update(distance,shelf);
        MapBase.xLength = bound.getxLength();
        MapBase.yLength = bound.getyLength();


        mapBase.setPointInfoMatrix(new PointInfo[MapBase.xLength][MapBase.yLength]);
        for (int i = 0; i < bound.getxLength() ; i++)
            for (int j = 0; j < bound.getyLength(); j++)
                mapBase.getPointInfoMatrix()[i][j] = new PointInfo(PointInfo.Status.NONE);


        for (int i = 0; i < shelf.getxLength(); i++) {
            for (int j = 0; j < shelf.getyLength(); j++) {
                for (int k = 0; k < shelf.getxNumber(); k++) {
                    for (int l = 0; l < shelf.getyNumber(); l++) {
                        int x = distance.getBoundToVerticalShelf() + i + (distance.getShelfToVerticalShelf()+shelf.getxLength())*k;
                        int y = distance.getBoundToHorizontalShelf() + j + (distance.getShelfToHorizontalShelf()+shelf.getyLength())*l;
                        mapBase.getPointInfo(x,y).setStatus(PointInfo.Status.SHELF);
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
    public MapBase getMapBase() {
        return mapBase;
    }
    public MapBase getMapBaseClone() {return new MapBase(this);}

    public class Shelf{
        private int xLength, yLength;
        private int xNumber, yNumber;

        public Shelf() {
        }

        public int getxLength() {
            return xLength;
        }
        public void setxLength(int xLength) {
            this.xLength = xLength;
        }
        public int getyLength() {
            return yLength;
        }
        public void setyLength(int yLength) {
            this.yLength = yLength;
        }
        public int getxNumber() {
            return xNumber;
        }
        public void setxNumber(int xNumber) {
            this.xNumber = xNumber;
        }
        public int getyNumber() {
            return yNumber;
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

        public int getBoundToVerticalShelf() {
            return boundToVerticalShelf;
        }
        public void setBoundToVerticalShelf(int boundToVerticalShelf) {
            this.boundToVerticalShelf = boundToVerticalShelf;
        }
        public int getBoundToHorizontalShelf() {
            return boundToHorizontalShelf;
        }
        public void setBoundToHorizontalShelf(int boundToHorizontalShelf) {
            this.boundToHorizontalShelf = boundToHorizontalShelf;
        }
        public int getShelfToVerticalShelf() {
            return shelfToVerticalShelf;
        }
        public void setShelfToVerticalShelf(int shelfToVerticalShelf) {
            this.shelfToVerticalShelf = shelfToVerticalShelf;
        }
        public int getShelfToHorizontalShelf() {
            return shelfToHorizontalShelf;
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
            this.xLength = distance.getBoundToVerticalShelf()*2 + shelf.getxNumber()*(shelf.getxLength() + distance.getShelfToVerticalShelf()) - distance.getShelfToVerticalShelf();
            this.yLength = distance.getBoundToHorizontalShelf()*2 + shelf.getyNumber()*(shelf.getyLength() + distance.getShelfToHorizontalShelf()) - distance.getShelfToHorizontalShelf();
        }

        private int getxLength() {
            return xLength;
        }
        private int getyLength() {
            return yLength;
        }
    }
}
