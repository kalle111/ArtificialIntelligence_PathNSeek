package model;

import constants.Constants;
import image.Image;
import image.ImageFactory;

import javax.swing.*;
import java.util.Random;

public class UserAgent {
    public double x; //location
    public double y; //location

    public double maxSpeed;
    public double curSpeed;
    public Tile goal;

    public double velocity_x;
    public double velocity_y;

    public int acceleration; //vektor?
    public boolean isArrived = false;

    public float r;
    public double maxForce;


    public double orientation;

    /*public int x_dest;
    public int y_dest;
    public int x_act;
    public int y_act;
     */


    public UserAgent(Tile t) {
        initialize();
        this.x = coordToVecotr(t.getX());
        this.y = coordToVecotr(t.getY());

        velocity_x = 0;
        velocity_y = 0;
        curSpeed = 0;

        maxForce = Math.PI/3;
        maxSpeed = (double)((double)Constants.tile_size)/60; // 60 fps
        r= (Constants.tile_size/2);
    }
    public void arrive(Tile lastTile) {
        //System.out.println("Arrive.");
        double arrivalRad = 2;
        double xGoal = coordToVecotr(lastTile.getX());
        double yGoal = coordToVecotr(lastTile.getY());

        double xVektor = (xGoal-x)*0.1;
        double yVektor = (yGoal-y)*0.1;


        double xAgent = xVektor*0.2;
        double yAgent = yVektor*0.2;

        this.x += xAgent;
        this.y += yAgent;

        if(checkIfInRadius(arrivalRad)) {
            this.isArrived = true;
        }
    }
    public void move() {
        Boolean inRadius = checkIfInRadius(r);
        orientation =   Math.atan2(-1*(coordToVecotr(goal.getY())-y), (coordToVecotr(goal.getX())-x));
        System.out.println("ORIENT: " + orientation);
        if(!inRadius) {
            double xGoal = coordToVecotr(goal.getX());
            double yGoal = coordToVecotr(goal.getY());
            double xVektor = (xGoal-x); // desired
            double yVektor = (yGoal-y); // desired
            double rad_old = Math.atan2(-1*velocity_y, velocity_x); //negativ zeigt nach oben, positiv nach unten
            double rad_new = Math.atan2(-1*yVektor, xVektor);
            if(rad_old > 2*Math.PI) {
                rad_old = rad_old - 2*Math.PI;
            }
            double vorzeichen = 1;
            double old_vorzeichen = 1;
            //handle negative radians.
            if(rad_old < 0) {
                rad_old = 2*Math.PI + rad_old;
                old_vorzeichen = old_vorzeichen *-1;
            }
            if(rad_new < 0) {
                rad_new = 2*Math.PI + rad_new;
                vorzeichen = vorzeichen * -1;
            }
            double rad_diff = (Math.max(rad_old, rad_new) - Math.min(rad_old,rad_new));

            //radiant difference of 0.5 as a 'maxForce', ~30 Deg
            if (rad_diff != 0 && (rad_diff >= maxForce || rad_diff <= -maxForce) && !(velocity_y==0 && velocity_x==0 )) {
                //System.out.println("Rad diff higher than maxForce!");
                //System.out.println(Math.toDegrees(rad_old) + ", new: " + Math.toDegrees(rad_new));

                if(rad_old > rad_new && vorzeichen == 1 && old_vorzeichen == 1) {
                    rad_diff = rad_old-maxForce;
                } else if (rad_old > rad_new && old_vorzeichen ==-1 &&vorzeichen ==1) {
                    rad_diff = rad_new-maxForce;
                } else if(rad_old > rad_new &&vorzeichen ==-1) {
                    rad_diff = rad_old-maxForce;
                }else if (rad_old < rad_new && vorzeichen == 1) {
                    rad_diff = rad_old+(vorzeichen*maxForce);
                } else if(rad_old < rad_new && vorzeichen ==-1 && (rad_old+maxForce) >= 1.5*Math.PI && (rad_old+maxForce) < 2*Math.PI){
                    rad_diff = rad_old+(maxForce);
                } else {
                    rad_diff = rad_new+(maxForce);
                }
                //rad_diff = Math.max(rad_old,rad_new) - maxForce;

                if(rad_diff > Math.PI/2 && rad_diff < Math.PI) {
                    //quadrant II)
                    velocity_y = Math.cos(rad_diff);
                    velocity_x = -1*Math.sin(rad_diff);
                } else if(rad_diff >= 0 && rad_diff < Math.PI/2){
                    //q1
                    velocity_y = -1*Math.cos(rad_diff);
                    velocity_x = Math.sin(rad_diff);
                } else if(rad_diff >= Math.PI && rad_diff < 1.5*Math.PI) {

                    //q3
                    velocity_y = -1*Math.cos(rad_diff);
                    velocity_x = Math.sin(rad_diff);
                    System.out.println("q3");
                } else if(rad_diff >= 1.5*Math.PI && rad_diff < 2*Math.PI) {
                    //q4
                    System.out.println("q4");
                    velocity_y = Math.cos(rad_diff);
                    velocity_x = -1*Math.sin(rad_diff);
                }


                    //normalization.
                    double len = Math.sqrt(Math.pow(velocity_y, 2) + Math.pow(velocity_x, 2));
                    double xAgent = (velocity_x / len) * (maxSpeed);
                    double yAgent = (velocity_y / len) * (maxSpeed);
                    this.x = x+xAgent;
                    this.y = y+yAgent;
                    //System.out.println("Orientation_old:" + rad_old + ", Orientation_new: "+ rad_new);
                    //System.out.println("Desired coords: " + goal.getX() + "|" + goal.getY() + " -- calculated: " + this.x + "|"+this.y);
                    if(checkIfInRadius(r)) {
                        this.goal = null;
                    }
            } else {

                    double newGoal_x = this.x + xVektor;
                    double newGoal_y = this.y + yVektor;
                    velocity_y = yVektor;
                    velocity_x = xVektor;
                    //normalization
                    double len = Math.sqrt(Math.pow(velocity_y, 2) + Math.pow(velocity_x, 2));
                    double xAgent = (xVektor/len)*(maxSpeed);
                    double yAgent = (yVektor/len)*(maxSpeed);
                    this.x = x+xAgent;
                    this.y = y+yAgent;
                    if(checkIfInRadius(r)) {
                        this.goal = null;
                    }

            }
        }
    }

    public Boolean checkIfInRadius(double rad) {
        double xGoal = coordToVecotr(goal.getX()); //current goal coordinate x
        double yGoal = coordToVecotr(goal.getY());

        /*if((((Constants.tile_size/2)+ x + r) >= xGoal || ((Constants.tile_size/2)+x-r) <= xGoal) && (((Constants.tile_size/3)+y+r) >= yGoal && ((Constants.tile_size/3)+y-r)<=yGoal)) {
            System.out.println("IN RADIUS!");
            return true;
        } else {
            return false;
        }*/
        if((xGoal-rad <= this.x && xGoal+rad >= this.x) && (yGoal-rad <= this.y && yGoal+rad >= this.y)) {
            //System.out.println("In radius!");
            return true;
        } else {
            return false;
        }
    }
    private void initialize() {


    }
    public double coordToVecotr(int i) {
        int temp = (i * Constants.tile_size) + Constants.first_tile_x + (Constants.tile_size/3);
        return temp;

    }

    public void follow(Path p) {
        // follow Path.

        //1. Predict location x frames ahead:

        // line segment

        //normal point to that line
    }

}
