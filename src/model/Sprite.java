package model;

import java.awt.Image;

//sprite superclass
public abstract class Sprite {
    private Image image;
    private boolean dead; //currently in the game or not identifier.
    protected int x;
    protected int y;
    protected int dx; //for small movements
    protected int orientation;

    public abstract void move();

    //constructor
    public Sprite() {
        this.dead = false;
    }

    //kill image
    public void kill() {
        this.dead = true;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
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

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
