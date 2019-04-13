package sample.Creator;

import sample.Model.Constant;
import sample.Model.MapBase;

import java.util.ArrayList;

public class MapBaseCreator {
    private Shelf shelf       = new Shelf();
    private Distance distance = new Distance();
    private Bound bound       = new Bound();
    private MapBase mapBase   = new MapBase();

    public MapBaseCreator() {
    }

    public void update(){
        bound.updateBound(distance,shelf);
        MapBase.xLength = bound.getxLength();
        MapBase.yLength = bound.getyLength();
        mapBase.setStatusList(new ArrayList<>());
        for (int i = 0; i < bound.getxLength() ; i++) {
            for (int j = 0; j < bound.getyLength(); j++) {
                mapBase.getStatusList().add(Constant.PointStatus.NONE);
            }
        }

        for (int i = 0; i < shelf.getxLength(); i++) {
            for (int j = 0; j < shelf.getyLength(); j++) {
                for (int k = 0; k < shelf.getxNumber(); k++) {
                    for (int l = 0; l < shelf.getyNumber(); l++) {
                        int x = distance.getBoundToVerticalShelf() + i + (distance.getShelfToVerticalShelf()+shelf.getxLength())*k;
                        int y = distance.getBoundToHorizontalShelf() + j + (distance.getShelfToHorizontalShelf()+shelf.getyLength())*l;
                        int id = MapBase.getIdFromXY(x,y);
                        mapBase.getStatusList().set(id,Constant.PointStatus.PERM);
                    }
                }
            }
        }

    }

    public Shelf getShelf() {
        return shelf;
    }
    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }
    public Bound getBound() {
        return bound;
    }
    public void setBound(Bound bound) {
        this.bound = bound;
    }
    public Distance getDistance() {
        return distance;
    }
    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    public MapBase getMapBase() {
        return mapBase;
    }
    public void setMapBase(MapBase mapBase) {
        this.mapBase = mapBase;
    }
    public MapBase getMapBaseClone() {return new MapBase(this);}

    public class Shelf{
        private int xLength, yLength;
        private int xNumber, yNumber;

        public Shelf() {
        }

        public Shelf(int xLength, int yLength, int xNumber, int yNumber) {
            this.xLength = xLength;
            this.yLength = yLength;
            this.xNumber = xNumber;
            this.yNumber = yNumber;
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

        public Distance(int boundToVerticalShelf, int boundToHorizontalShelf, int shelfToVerticalShelf, int shelfToHorizontalShelf) {
            this.boundToVerticalShelf   = boundToVerticalShelf;
            this.boundToHorizontalShelf = boundToHorizontalShelf;
            this.shelfToVerticalShelf   = shelfToVerticalShelf;
            this.shelfToHorizontalShelf = shelfToHorizontalShelf;
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
    public class Bound {
        private int xLength, yLength;
        public Bound() {
        }

        public void updateBound(Distance distance, Shelf shelf) {
            this.xLength = distance.getBoundToVerticalShelf()*2 + shelf.getxNumber()*(shelf.getxLength() + distance.getShelfToVerticalShelf()) - distance.getShelfToVerticalShelf();
            this.yLength = distance.getBoundToHorizontalShelf()*2 + shelf.getyNumber()*(shelf.getyLength() + distance.getShelfToHorizontalShelf()) - distance.getShelfToHorizontalShelf();
        }

        public void setxLength(int xLength) {
            this.xLength = xLength;
        }
        public void setyLength(int yLength) {
            this.yLength = yLength;
        }
        public int getxLength() {
            return xLength;
        }
        public int getyLength() {
            return yLength;
        }
    }
}
