package image;

import constants.Constants;

import javax.swing.*;

public class ImageFactory {
    public static ImageIcon createImage(Image image) {
        ImageIcon imageIcon = null;
        switch(image) {
            case Agent:
                imageIcon = new ImageIcon(Constants.AGENT_URL);
                break;
            case Path:
                imageIcon = new ImageIcon(Constants.PATH_URL);
                break;
            case Obstacle:
                imageIcon = new ImageIcon(Constants.OBSTACLE_URL);
                break;
            case BACKGROUND:
                imageIcon = new ImageIcon(Constants.BACKGROUND_URL);
                break;
            default:
                return null;
        }
        return imageIcon;
    }
}
