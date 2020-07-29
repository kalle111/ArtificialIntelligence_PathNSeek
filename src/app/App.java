package app;

import ui.GameMainFrame;

import java.awt.*;

public class App {

    public static void main(String[] args) {
	// write your code here
        System.out.println("hello");

        EventQueue.invokeLater( () ->  {
            new GameMainFrame();
        });
    }
}
