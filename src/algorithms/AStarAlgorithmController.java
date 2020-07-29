package algorithms;

import model.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithmController {
    private Tile[][] nodes;
    private Tile start;
    private Tile end;
    private TileRecord current;
    public TileRecord startRecord;
    public boolean success = false;

    //private PriorityQueue<TileRecord> openList;
    public List<TileRecord> openList;
    public List<TileRecord> closedList;
    public List<TileRecord> perfectList;
    public boolean keepSearching = true;
    public boolean solutionFound = false;

    public AStarAlgorithmController(Tile[][] map, Tile start, Tile end) {
        this.nodes = map;
        if(start == null || end == null) {
            this.solutionFound = false;
            this.keepSearching = false;
        } else {
            this.start = start;
            this.end = end;
            this.startRecord = new TileRecord(this.start,null, 0, this.end);

            this.openList = new ArrayList<TileRecord>();
            openList.add(startRecord);
            this.closedList = new ArrayList<TileRecord>();
            this.perfectList = new ArrayList<TileRecord>();
        }

    }



    public Tile[][] getNodes() {
        return nodes;
    }
    public void setNodes(Tile[][] nodes) {
        this.nodes = nodes;
    }

    public Tile[][] iterateOnce() {
        if(!keepSearching) {
            return this.nodes;
        }
        //get neighbours + add to openList.
        if(current == null) {
            this.current = startRecord;
        }
            List<TileRecord> neighbourList;
            neighbourList = getNeighbours(this.current); //should get the lowest estimated cost nodeRecord
            for(TileRecord t1:  neighbourList) {
                if(t1.node.getType() != 'X') {
                    //openList.add(t1);
                }

            }
        if(this.current.node.getType() != 'S' && this.current.node.getType() != 'G') {
            this.current.node.setType('C');
        }


            for(TileRecord r : neighbourList) {
                if(r.node.getType() =='X') {
                    continue;
                }
                int endNodeCost = r.estimatedTotalCost;
                Tile endNode = r.node;
                //openList.add(r);
                if(closedList.contains(r)) {
                    TileRecord endNodeRecord = closedList.get(closedList.indexOf(r));
                    if (endNodeRecord.estimatedTotalCost <= endNodeCost) {
                        continue;
                    } else {
                        //remove old NodeRecord.
                        closedList.remove(endNodeRecord);
                    }
                } else if(openList.contains(r)) {
                    TileRecord endNodeRecord = openList.get(openList.indexOf(r));
                    if(endNodeRecord.estimatedTotalCost <= r.estimatedTotalCost) {
                        //openList.add(r);
                        continue;
                    } else {
                        //openList.remove(endNodeRecord);
                        r.estimatedTotalCost = endNodeRecord.estimatedTotalCost;
                        r.costSoFar = endNodeRecord.costSoFar;
                        r.connection = endNodeRecord.connection;
                    }
                } else {
                    openList.add(r);
                }
                  if(r.node.getType() != 'G' && r.node.getType() != 'S') {
                        r.node.setType('V');
                  }
                }

            openList.remove(current);
            closedList.add(current);
            if(current.node.getType() != 'G' && current.node.getType() != 'S') {
                current.node.setType('F');
            }




        if(openList.size() > 0  && !(current.node.getX() == end.getX() && current.node.getY() == end.getY())) {

            Collections.sort(openList);
            this.current = openList.get(0);
            if(this.current.node.getType() != 'G') {
                this.current.node.setType('C');
            }

        } else {
                if(current.node.getX() == end.getX() && current.node.getY() == end.getY()) {
                    // set pathfindingBool == false in gamePanel!
                    this.keepSearching = false;
                    solutionFound = true;

                    System.out.println("FINISHED.");
                    do {
                        if(current.node.getType() != 'G' && current.node.getType() != 'S') {
                            current.node.setType('P');
                            perfectList.add(current);
                        } else {
                            perfectList.add(current);
                        }
                        current = current.connection;
                    } while(current.connection != null);
                    return nodes;
                } else {
                    this.keepSearching = false;
                    solutionFound = false;
                    System.out.println("no solution.");
                    return nodes;
                }

            }




        return nodes;
    }



// should probably just be a list of tiles instead of TileRecords.
    public List<TileRecord> getNeighbours(TileRecord t) {
        List<TileRecord> neighbourList = new ArrayList<>();
        int x = t.node.getX();
        int y = t.node.getY();
        int mapHeight = nodes.length;
        int mapWidth = nodes[0].length;//length of first line

        //logic for comparison
        if(x==0 && y== mapHeight-1) {

          neighbourList.add(new TileRecord(nodes[t.node.getY()-1][t.node.getX()],t, t.costSoFar+1, this.end));
          neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));
        } else if(x==mapWidth-1 && y==mapHeight-1) {
            neighbourList.add(new TileRecord(nodes[t.node.getY() -1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));
        } else if(x==mapWidth-1 && y==0) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));
        }else if(x==0 && y==0) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));

        } else if(x==0 && y!= 0 &&y!= mapHeight-1) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()-1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));
        }else if(x!=0 && x!=mapWidth-1 && y==0) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));

        } else if(x==mapWidth-1 && y!= 0 && y!=mapHeight-1) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()-1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));
        } else if (y==mapHeight-1 && x!= 0 && x!= mapWidth-1) {
            neighbourList.add(new TileRecord(nodes[t.node.getY()-1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));

        } else {
            neighbourList.add(new TileRecord(nodes[t.node.getY()-1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()+1][t.node.getX()],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()-1],t, t.costSoFar+1, this.end));
            neighbourList.add(new TileRecord(nodes[t.node.getY()][t.node.getX()+1],t, t.costSoFar+1, this.end));
        }
    return neighbourList;
    }
}
