package model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Map extends Sprite {
    public Tile[][] mapArray;
    public MapFileReader mfr;
    private int mapWidth;
    private int mapHeight;
    public ArrayList<Tile> tileList = new ArrayList<Tile>();

    public Map() {
        initialize();
        this.mfr = new MapFileReader();
        this.mapArray = mfr.mapArray;
    }

    private void initialize() {
        int start_x = 0;
        int start_y = 0;
    }

    @Override
    public void move() {
        //map should not be able to move.
    }


    public class MapFileReader  {
        //File mapFile = new File("")
        public String fullMapFileString;
        public String projectPath;
        public String sourcePath;
        public String fileName = "map6.txt";
        public int mapWidth;
        public int mapHeight;
        public Tile startTile;
        public Tile endTile;
        public Tile[][] mapArray;

        public MapFileReader() {
            this.projectPath = this.getCurPath();
            this.sourcePath = this.projectPath + "\\src\\image\\";
            this.fullMapFileString = this.sourcePath + fileName;
            System.out.println(fullMapFileString);
            try {
                this.readMapFile();
            } catch (Exception e) {
                System.out.println("FileNotFound.");
                JOptionPane.showMessageDialog(null, "Map: " + fullMapFileString + " couldn't be found.", "ErrorInformation: ", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                e.printStackTrace();
            }
        }

        public String getCurPath() {
            Path curPath = Paths.get("");
            String curPathString = curPath.toAbsolutePath().toString();
            //System.out.println(curPathString);
            return curPathString;
        }

        public void readMapFile() throws FileNotFoundException, IOException {
            BufferedReader br = new BufferedReader(new FileReader(this.fullMapFileString));
            ArrayList<String> arrList = new ArrayList<String>();
            String st;
            int yCounter = 0;
            while ((st = br.readLine()) != null){
                //System.out.println("Map-Width: " + st.length());
                arrList.add(st);
            }
            this.setMapHeight(arrList.size());
            this.setMapWidth(arrList.get(0).length());
            System.out.println("Height of Map: "+this.getMapHeight());
            System.out.println("Width of Map: "+this.getMapWidth());

            Tile[][] tempMapArr = this.getMapArray(arrList);
            this.setMapArray(tempMapArr);

            System.out.println("debug sout");
        }


        public Tile[][] getMapArray(ArrayList<String> arrList) {
            int y = 0;
            int x = 0;

            Tile a[][] = new Tile[this.mapHeight][this.mapWidth];
            //a[0][0] = 'a';

            for(int i = 0; i < arrList.size(); i++) {
                String tmpStr = arrList.get(i);
                for(int j = 0; j < tmpStr.toCharArray().length; j++) {
                    char temp = tmpStr.toCharArray()[j];
                    Tile t = new Tile(j, i, temp, null, null);
                    tileList.add(t);
                    a[i][j] = t;

                    if(temp =='S') {
                        this.startTile = t;
                    } else if (temp=='G') {
                        this.endTile = t;
                    }
                }
            }

            /*for(String s : arrList) {
                Char tempCharArr[] = s.toCharArray();
                for(char b : tempCharArr) {
                    Tile t = new Tile(x,y, b, null, null);
                    tileList.add(t);
                    a[y][x++] = b;

                }
                x=0;
                y++;
            }*/
            return a;
        }
        //Getter n Setter for map
        public int getMapWidth() {
            return mapWidth;
        }
        public void setMapArray(Tile[][] mapArr) {
            this.mapArray = mapArr;
        }

        public void setMapWidth(int mapWidth) {
            this.mapWidth = mapWidth;
        }

        public int getMapHeight() {
            return mapHeight;
        }

        public void setMapHeight(int mapHeight) {
            this.mapHeight = mapHeight;
        }
    }
}
