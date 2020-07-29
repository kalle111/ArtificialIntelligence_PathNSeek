package ui;

import constants.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlPanel extends JPanel implements MouseListener, ChangeListener {
    public boolean searchMode = false;
    public boolean moveMode = false;
    private GamePanel gp;


    //buttons
    public JButton start, stop, step, reset, startSteering, stopSteering, resetSteering;
    public JSlider gameSpeedSlider;

    public ControlPanel(GamePanel gp) {
        this.gp = gp;

        initializeLayout();
        gameSpeedSlider = new JSlider();
        gameSpeedSlider.setOrientation(JSlider.HORIZONTAL);
        gameSpeedSlider.setPaintLabels(true);
        gameSpeedSlider.setPaintTicks(true);
        gameSpeedSlider.setMinimum(5);
        gameSpeedSlider.setMaximum(255);
        gameSpeedSlider.setMinorTickSpacing(50);
        gameSpeedSlider.setValue(gp.gameSpeed);
        gameSpeedSlider.setSnapToTicks(true);


        // disclaimer: Control logic may be a little wonky, please don't mess with it by
        // hopping between resets/steps/starts after starting the steering
        //
        start = new JButton("Start Pathfinding");
        stop = new JButton("Stop Pathfinding");
        //false per default
        stop.setEnabled(false);
        step = new JButton("Step");
        reset = new JButton("Reset grid");

        startSteering = new JButton("Start Steering");
        stopSteering = new JButton("Stop Steering");
        resetSteering = new JButton("Reset Agent");

        //false per default
        startSteering.setEnabled(false);
        stopSteering.setEnabled(false);
        resetSteering.setEnabled(false);

        start.addActionListener(clickStart());
        stop.addActionListener(clickStop());
        step.addActionListener(clickStep());
        reset.addActionListener(clickReset());
        startSteering.addActionListener(clickStartSteering());
        stopSteering.addActionListener(clickStopSteering());
        resetSteering.addActionListener(clickResetAgent());
        gameSpeedSlider.addChangeListener(changeGameSpeed());


        this.add(start);
        this.add(stop);
        this.add(step);
        this.add(reset);
        this.add(startSteering);
        this.add(stopSteering);
        this.add(resetSteering);
        this.add(gameSpeedSlider);
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    }
    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.width, 80));
    }

    public ActionListener clickStart() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setEnabled(false);
                stop.setEnabled(true);
                step.setEnabled(true);
                reset.setEnabled(false);
                //System.out.println("Pathfinding started.");
                gp.pathFindingMode = true;
                gp.timer.start();

            }
        };
    }
    public ActionListener clickStartSteering() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("StartSteeringButton clicked!");
                // check if pathfinding is finished.
                if(!gp.algo_controller.keepSearching && !gp.pathFindingMode) {
                    gp.steeringMode= true;
                    gp.timer = new Timer(2, new GameLoop(gp));
                    gp.timer.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Path finding algorithm is not finished. (algorithm.keepSearching && GamePanel.pathFinding)", "ErrorInformation", JOptionPane.ERROR_MESSAGE);

                }

            }
        };
    }
    public ActionListener clickStopSteering() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("StopSteeringButton clicked!");
            }
        };
    }
    public ActionListener clickResetAgent() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ResetAgentButton clicked!");
            }
        };
    }
    public ActionListener clickStop() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop.setEnabled(false);
                start.setEnabled(true);
                step.setEnabled(true);
                reset.setEnabled(true);
                gp.timer.stop();
                start.setText("Resume Pathfinding");
            }

        };
    }
    public ActionListener clickStep() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.doOneLoop();
                start.setEnabled(true);
                step.setEnabled(true);
                reset.setEnabled(true);
                gp.timer.stop();
            }

        };
    }
    public ActionListener clickReset() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setText("Start Pathfinding");
                gp.initializeVar();
                gp.pathFindingMode = true;
                //gp.algo_controller.keepSearching = true;
                gp.algo_controller.keepSearching = true;
                gp.steeringMode = false;
                gp.algo_controller.solutionFound = false;
                gp.timer = gp.startTimer(gameSpeedSlider.getValue());
                gp.repaint();
                start.setEnabled(true);
                step.setEnabled(true);
                reset.setEnabled(true);
            }

        };
    }

    public ChangeListener changeGameSpeed() {
        return new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if(!source.getValueIsAdjusting()) {
                    if(start.isEnabled()) {
                        gp.timer = gp.startTimer(gameSpeedSlider.getValue());
                    } else {
                        stop.doClick();
                        gp.timer = gp.startTimer(gameSpeedSlider.getValue());
                        start.doClick();
                    }

                    System.out.println("Game speed set to: "+gameSpeedSlider.getValue());
                }
            }
        };
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

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
    public void stateChanged(ChangeEvent e) {
        //System.out.println(e);
       //System.out.println(this.gameSpeedSlider.getValue());
    }
}
