package model;

import constants.Constants;

import java.awt.*;

public class Tile {
    private int x;
    private int y;
    private int x_end;
    private int y_end;
    private char type;
    private Tile[] neighbours;
    private Graphics g;
    private Rectangle r;
    private String status = "open"; //enum

    // A* algorithm
    public Tile parent;
    public int heuristic;
    public int finalCost;


    public Tile(int x, int y, char type, Graphics g, Rectangle r) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.g = g;
        this.r = r;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Tile[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Tile[] neighbours) {
        this.neighbours = neighbours;
    }

    public Graphics getG() {
        return g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    public Rectangle getR() {
        return r;
    }

    public void setR(Rectangle r) {
        this.r = r;
   }
   @Override
   public String toString() {
        String temp = "";

        return "Coords: (X="+this.getX()+", Y="+this.getY()+", Type:"+this.getType()+")";
   }
}
