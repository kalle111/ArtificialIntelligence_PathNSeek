package algorithms;

import model.Tile;

import java.util.ArrayList;
import java.util.List;

public class AStartAlgorithm {
    public static final int jump_cost = 1;
    private Tile[][] map;
    private Tile[][] newMap;
    private Tile currentTile;
    private Tile goalTile;
    private int distanceToGoal;
    private int mapWidth;
    private int mapHeight;

    public AStartAlgorithm(Tile[][] tileMap, Tile currentTile, Tile goalTile) {
        this.map = tileMap;
        this.currentTile = currentTile;
        this.goalTile = goalTile;
        this.mapWidth = this.map[0].length;
        this.mapHeight = this.map.length;
        this.newMap = calculate();

    }
    public Tile[][] calculate() {
        //currentTile.setType('P');
        List<Tile> neighbourList = new ArrayList<Tile>();
        List<Tile> visitedList = new ArrayList<Tile>();
        int x = currentTile.getX();
        int y = currentTile.getY();

        if(map[y][x].getType() != 'S') {
            map[y][x].setType('P');
        }
        if(x==0 && y== mapHeight-1) {
            neighbourList.add(map[currentTile.getY()-1][currentTile.getX()]);
            neighbourList.add(map[currentTile.getY()][currentTile.getX()+1]);
        } else if(x==mapWidth-1 && y==mapHeight-1) {
            neighbourList.add(map[y -1][x]);
            neighbourList.add(map[y][x - 1]);
        } else if(x==mapWidth-1 && y==0) {
            neighbourList.add(map[y][x - 1]);
            neighbourList.add(map[y + 1][x]);
        }else if(x==0 && y==0) {
            neighbourList.add(map[y][x + 1]);
            neighbourList.add(map[y + 1][x]);
        } else if(x==0 && y!= 0 &&y!= mapHeight-1) {
            neighbourList.add(map[y - 1][x]);
            neighbourList.add(map[y][x + 1]);
            neighbourList.add(map[y + 1][x]);
        }else if(x!=0 && x!=mapWidth-1 && y==0) {
            neighbourList.add(map[y + 1][x]);
            neighbourList.add(map[y][x - 1]);
            neighbourList.add(map[y][x + 1]);
        } else if(x==mapWidth-1 && y!= 0 && y!=mapHeight-1) {
            neighbourList.add(map[y - 1][x]);
            neighbourList.add(map[y + 1][x]);
            neighbourList.add(map[y][x - 1]);
        } else if (y==mapHeight-1 && x!= 0 && x!= mapWidth-1) {
            neighbourList.add(map[y-1][x]);
            neighbourList.add(map[y][x+1]);
            neighbourList.add(map[y][x-1]);
        } else {
            neighbourList.add(map[y-1][x]);
            neighbourList.add(map[y+1][x]);
            neighbourList.add(map[y][x-1]);
            neighbourList.add(map[y][x+1]);
        }

        double lowestDist = 0.0;
        Tile lowestDistTile = null;
        for(Tile t : neighbourList) {
            if(t.getType()== ' ') {
                t.setType('V');
                visitedList.add(t);
                int xDist = goalTile.getX() - t.getX();
                int yDist = goalTile.getY() - t.getY();
                double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
                if (lowestDist == 0.0) {
                    lowestDist = dist;
                    lowestDistTile = t;
                    continue;
                } else if (lowestDist > dist) {
                    lowestDist = dist;
                    lowestDistTile = t;
                }
            } else {
                System.out.println("Obstacle detected");
                if(t.getType() ==' ') {
                    t.setType('V');
                }
            }
        }
        lowestDistTile.setType('C');
        if(currentTile.getType() =='S') {
            // do nothing
        } else {
            currentTile.setType('V');
        }
        return map;
    }

    public Tile[][] getNewMap() {
        return newMap;
    }

    public void setNewMap(Tile[][] newMap) {
        this.newMap = newMap;
    }
}
