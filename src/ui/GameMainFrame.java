package ui;

import constants.Constants;
import image.Image;
import image.ImageFactory;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GameMainFrame extends JFrame {
    public GamePanel gp;
    public ControlPanel cp;

    public GameMainFrame() {
        initLayout();
    }

    private void initLayout() {
        //game component
        this.gp = new GamePanel(this);
        gp.addMouseListener(gp);
        gp.addMouseMotionListener(gp);
        this.add(gp);

        //control component
        this.cp = new  ControlPanel(gp);
        cp.setLayout(new GridLayout(0,4,12,6));
        cp.addMouseListener(cp);//BorderLayout.SOUTH);
        this.add(cp, BorderLayout.SOUTH);


        //misc
        setTitle(Constants.TITLE);
        setIconImage(ImageFactory.createImage(Image.Obstacle).getImage());
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true); //show ui component

    }
}
