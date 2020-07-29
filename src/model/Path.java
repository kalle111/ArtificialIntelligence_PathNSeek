package model;

import constants.Constants;

public class Path {

    public Tile start;
    public Tile end;
    public double radius;

    public Path(Tile st, Tile ed) {
        this.start = st;
        this.end = ed;
        this.radius = Constants.tile_size/2;
    }

}
