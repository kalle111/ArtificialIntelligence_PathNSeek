package ui;

import algorithms.AStarAlgorithmController;
import algorithms.AStartAlgorithm;
import algorithms.TileRecord;
import constants.Constants;
import image.Image;
import image.ImageFactory;
import model.Tile;
import model.UserAgent;
import model.Map;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Date;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    public int gameSpeed = 55;
    private ImageIcon backgroundImage;
    public Timer timer;
    private UserAgent userAgent;
    public Map map;
    public GameMainFrame gmf;

    public ArrayList<Tile> labelList = new ArrayList<>();
    private Tile startTile;
    private Tile endTile;
    private Tile currentTile;
    public Boolean pathFindingMode = true;
    public Boolean createListMode = true;
    private Boolean neighbourMode = true;
    private Boolean nextNodeMode = false;
    private Boolean optimalPath = false;

    public Boolean steeringMode = false;

    public AStarAlgorithmController algo_controller;

    public GamePanel(GameMainFrame g) {
        initializeGameMainFrame(g);
        initializeVar();
        initializeLayout();
    }

    public void initializeGameMainFrame(GameMainFrame g) {
        this.gmf = g;
    }

    public void initializeVar() {


        this.map = new Map();
        if(map.mfr.startTile == null || map.mfr.endTile == null) {
            JOptionPane.showMessageDialog(null, "Error: your map has no Start or end point!", "ErrorInformation", JOptionPane.ERROR_MESSAGE);

            System.exit(0);
        }
        this.startTile = map.mfr.startTile;
        this.endTile = map.mfr.endTile;
        this.userAgent = new UserAgent(startTile);
        this.algo_controller = new AStarAlgorithmController(this.map.mapArray, this.startTile, this.endTile);
        this.backgroundImage = ImageFactory.createImage(Image.BACKGROUND);
        this.timer = startTimer(gameSpeed);

        //this.timer.start(); //this should be started by clicking the Start-button.
    }
    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.width, Constants.height));
    }
    // getter setter

    public Timer startTimer(int period) {
        if(this.timer != null) {
            this.timer.stop();
        }
        return new Timer(period, new GameLoop(this));
    }
    public ArrayList<Tile> getLabelList() {
        return labelList;
    }

    //drawings
    private void drawAgent(Graphics g) {
        //g.drawImage(userAgent.getImage(), userAgent.getX(), userAgent.getY(), this);
        g.setColor(Color.black);
        g.fillOval((int)(this.userAgent.x), (int)(this.userAgent.y),Constants.tile_size/3, Constants.tile_size/3);
    }
    private void drawMapTile(Graphics g) {
        //System.out.println("Array Length = Zeilen: " + this.map.mapArray.length);
        //System.out.println("Array[0] Length = Spalten: "+ this.map.mapArray[0].length);
        for(int i = 0; i < this.map.mapArray.length; i++) {
            for(int j = 0; j < this.map.mapArray[0].length; j++) {
                int yDist = Constants.tile_size*i + Constants.first_tile_y;
                int xDist = Constants.first_tile_x+(Constants.tile_size*j);
                Rectangle r = new Rectangle(xDist,yDist,Constants.tile_size,Constants.tile_size);
                if(createListMode) { //list will only be created once with Graphic g.
                    Tile t = new Tile(j,i,this.map.mapArray[i][j].getType(), g, r);
                    t.setR(r);
                    labelList.add(t);
                    char type = t.getType();
                    if(t.getType() == 'S') {
                        this.startTile = t;
                        this.currentTile = t;
                    } else if(t.getType() =='G') {
                        this.endTile = t;
                    }
                }

                // X => Obstacle
                // S => Start
                // G => Goal
                // B => explored, but bad
                // V => visited, lowest cost
                // P => perfect
                // C => current Tile
                // F => closed Tile
                switch(this.map.mapArray[i][j].getType()) {
                    case 'X':
                        g.setColor(new Color(0,0,0));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'F':
                        g.setColor(new Color(111,111,111));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'S':
                        g.setColor(new Color(43,195,255));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.drawString("S",  (int)r.getX()+2, (int)r.getY()+Constants.tile_size-2);
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+(Constants.tile_size/3));
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'G':
                        g.setColor(new Color(43,195,255));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        if(!this.steeringMode) {
                            g.drawString("G",  (int)r.getX()+2, (int)r.getY()+Constants.tile_size-2);
                        }

                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case ' ':
                        g.setColor(new Color(165,82,45));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'B': //path finding visited algorithm
                        g.setColor(new Color(120,175,120));
                        g.setColor(new Color(0,100,50));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'V':
                        g.setColor(new Color(0,100,50));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'C':
                        this.currentTile = this.map.mapArray[i][j];
                        g.setColor(new Color(0,100,50));
                        g.setColor(new Color(120,200,120));
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                    case 'P':
                        g.setColor(Color.yellow);
                        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.lightGray);
                        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),(int)r.getHeight());
                        g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
                        g.drawString("("+j+","+i+")", (int)r.getX(), (int)r.getY()+Constants.tile_size/3);
                        this.map.mapArray[i][j].setR(r);
                        break;
                }


            }

        }
        //System.out.println("CreateListMode: " + this.createListMode);
        if(this.createListMode) {

        }
        this.createListMode = false;

    }


    //important
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAgent(g);
        g.drawImage(backgroundImage.getImage(), 0,0, null);
        //System.out.println(new Date() + " >> Repaint.");
        drawMisc(g);
    }

    private void drawMisc(Graphics g) {
        drawMapTile(g);
        drawAgent(g);
    }

    public void doOneLoop() {
        //Pathfinding one step at a time
        //SteeringMode => Agent should be moved
        //pathfindingMode => A-Star-Algorithm will be further executed
        if(this.steeringMode && !pathFindingMode) {
            // steering+pathfollowing algorithm
            //System.out.println("Current PerfectList-Size: "+this.algo_controller.perfectList.size() + ", Current goal node coords: " + this.algo_controller.perfectList.get(this.algo_controller.perfectList.size()-1).node.toString());
            if(this.userAgent.goal == null && this.algo_controller.perfectList.size() > 0) {
                this.userAgent.goal = this.algo_controller.perfectList.get(this.algo_controller.perfectList.size()-1).node;
                this.algo_controller.perfectList.remove(this.algo_controller.perfectList.size()-1);
            } else if(this.userAgent.goal != null && (this.algo_controller.perfectList.size() < 1)){
                System.out.println("no more Nodes in perfect list!");

            }
            if(this.algo_controller.perfectList.size() > 0) {
                if(this.algo_controller.perfectList.size() < 35) {
                    System.out.println("aaaa");
                }
                userAgent.move();
            } else if(this.algo_controller.perfectList.size() == 0 && this.algo_controller.solutionFound && !userAgent.isArrived) {
                userAgent.arrive(endTile);
            } else if(userAgent.isArrived) {
                JOptionPane.showMessageDialog(this, "Agend successfully arrived!", "Tile clicked: " + "InfoBar", JOptionPane.INFORMATION_MESSAGE);
                this.timer.stop();
            }

            //System.out.println("folow and steer");
        }
        if(this.algo_controller.keepSearching && pathFindingMode) {
            runAStarAlgorithm();
            if(this.algo_controller.keepSearching) {
                gmf.cp.reset.setEnabled(false);
            }
        } else if(!steeringMode) {
            gmf.cp.stop.setEnabled(false);
            gmf.cp.reset.setEnabled(true);
            gmf.cp.startSteering.setEnabled(true);
            this.repaint();
            JOptionPane.showMessageDialog(this, this.algo_controller.solutionFound ? "success" : "no possible solution", "Tile clicked: " + "InfoBar", this.algo_controller.solutionFound ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            this.pathFindingMode = false;
            this.algo_controller.keepSearching = false;
            //this.steeringMode = true;
            this.timer.stop();
        }
        update();
        repaint();
    }
    private void runAStarAlgorithm() {
        if(this.pathFindingMode) {
            //this.algo_controller
            //System.out.println(new Date() + " >> Neighbour-Search");
            this.map.mapArray = this.algo_controller.iterateOnce();

            this.neighbourMode = false;
            this.nextNodeMode = true;
        }
    }
    private void stepNextNode() {
        if(this.pathFindingMode)
        {
            //first test.
            AStartAlgorithm a = new AStartAlgorithm(this.map.mapArray, this.currentTile, this.endTile);
            this.map.mapArray = a.getNewMap();
        }
    }
    private void updateTileColor(Tile t, Color c) {
        Graphics g;
        g = t.getG();
        g.setColor(c);
        g.fillRect(0,0,getWidth(), getHeight());
        //System.out.println(new Date() + " >> update-tile-color-func");
    }
    private void update() {
        //System.out.println(new Date() + " >>update-func");

        labelList.clear();
        for(Tile[] t1 : this.map.mapArray) {
            for(Tile t2 : t1) {
                labelList.add(t2);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e);
        Point p = e.getPoint();
        Tile temp = null;

        //System.out.println(labelList.get(1));

        for(Tile t : labelList) {
            if(t.getR().contains(p)) {
                temp = t;
            }
        }
        if(temp != null) {
            TileRecord tr = new TileRecord(temp, null, 0, null, 0);
            int index;
            if (this.algo_controller.perfectList.contains(tr)) {
                tr = this.algo_controller.perfectList.get(this.algo_controller.perfectList.indexOf(tr));
                JOptionPane.showMessageDialog(null, "Node is on perfect path! " + "\n" + "cost till this point: " + tr.costSoFar + "\n" + temp.toString(), "Info: Node found in perfect path!", JOptionPane.INFORMATION_MESSAGE);
            } else if (this.algo_controller.openList.contains(tr) && tr.node.getType() != 'G' && tr.node.getType() != 'S') {
                tr = this.algo_controller.openList.get(this.algo_controller.openList.indexOf(tr));
                JOptionPane.showMessageDialog(null, "Node is on openList: " + "\n" + "costSoFar: " + tr.costSoFar + "\n" + "estimatedTotalCost: " + tr.estimatedTotalCost + "\n" + "Connection: " + tr.connection.node.toString() + "\n" + temp.toString(), "Info: Node found in open list.", JOptionPane.INFORMATION_MESSAGE);
            } else if (this.algo_controller.closedList.contains(tr) && tr.node.getType() != 'G' && tr.node.getType() != 'S') {
                tr = this.algo_controller.closedList.get(this.algo_controller.closedList.indexOf(tr));
                JOptionPane.showMessageDialog(null, "Node is on closedList: " + "\n" + "costSoFar: " + tr.costSoFar + "\n" + "estimatedTotalCost: " + tr.estimatedTotalCost + "\n" + "Connection: " + tr.connection.node.toString() + "\n" + temp.toString(), "Info: Node found in closed list", JOptionPane.INFORMATION_MESSAGE);
            } else if(tr.node.getType() =='S' || tr.node.getType() == 'G') {
                if(tr.node.getType() =='S') {
                    tr = this.algo_controller.startRecord;
                } else {

                }

                JOptionPane.showMessageDialog(null, "Start/Goal Node!" + "\n" + "cost till this point: " + tr.costSoFar + "\n" + temp.toString(), "Start/End node", JOptionPane.INFORMATION_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(null, "Node/Tile has not been visited yet!"+"\n"+temp.toString(), "is not visited yet", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //System.out.println(e.getPoint() + ">> mouseClicked...getPoint()");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getPoint() + ">> mousePressed...getPoint()");
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /*public void updateMap(Map map) {
        this.map = map;
    }*/
}
