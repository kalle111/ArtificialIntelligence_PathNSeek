package algorithms;

import model.Tile;

public class TileRecord implements Comparable<TileRecord> {
    public Tile node;
    public Tile end;
    public TileRecord connection;
    public int costSoFar;
    public int estimatedTotalCost;
    public boolean solution = false;

    public TileRecord(Tile node,TileRecord tr, int csf, Tile end) {
        //this.
        this.node = node;
        this.end = end;
        this.connection = tr;
        this.costSoFar = csf;
        this.estimatedTotalCost = calcEstimation(node);
    }

    public TileRecord(Tile node,TileRecord tr, int csf, Tile end, int estimatedTotalCost) {
        //this.
        this.node = node;
        this.end = end;
        this.connection = tr;
        this.costSoFar = csf;
        this.estimatedTotalCost = estimatedTotalCost;
    }

    public int calcEstimation(Tile node) {
        int x_dist = end.getX()-node.getX();
        int y_dist = end.getY()-node.getY();

        if(x_dist < 0) {
            x_dist = x_dist*-1;
        }
        if(y_dist < 0) {
            y_dist = y_dist*-1;
        }
        //return (this.costSoFar + (int)(Math.sqrt(Math.pow(y_dist,2) + Math.pow(x_dist,2))));
        return (this.costSoFar + (y_dist+x_dist)); // Manhatten distance



    }


    @Override
    public int compareTo(TileRecord o) {
        /*int retVal = Double.compare(node.getX(), o.node.getX());
        if(retVal != 0) {
            return retVal; // if x-coordinates not null.
        }
        return Double.compare(node.getY(), o.node.getY());*/
        int retVal = Integer.compare((this.estimatedTotalCost), (o.estimatedTotalCost));
        return retVal;
    }

    public boolean equals(Object o) {
        TileRecord tr = (TileRecord) o;
        return tr.node.getX()==this.node.getX()&&tr.node.getY()==this.node.getY();
    }
}
