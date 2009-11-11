/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eads;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Lancer-Matrix
 */
public class GUI extends Canvas {
    private static int maxWidth = 600;
    private static int maxHeight = 600;
    private static GUI instance;
    private static JFrame mainFrame;
    private static JFileChooser fc = new JFileChooser(".\\data");

    private int scale = 0;

    private Color[] colors = {Color.BLUE, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.CYAN,
        Color.GREEN, Color.BLACK, Color.RED, new Color(139, 69, 19), new Color(250, 128, 114), new Color(135, 206, 235),
        new Color(0, 255, 127), new Color(138, 43, 226), new Color(95, 158, 160), new Color(189, 183, 107),
        new Color(85, 107, 1), new Color(255, 140, 0), new Color(238, 221, 130), new Color(119, 136, 153), new Color(176, 48, 96),
        new Color(102, 205, 170), new Color(107, 142, 35), new Color(219, 112, 147),
        new Color(205, 133, 63), new Color(255, 248, 220)
    };

    private static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    public static JFrame getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new JFrame("IS421: Coding Project");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.getContentPane().add(getInstance(),BorderLayout.CENTER);

            mainFrame.setSize(maxWidth, maxHeight);
        }

        return mainFrame;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public static String getInputFile() {
        int retVal = -1;
        while (retVal != JFileChooser.APPROVE_OPTION) {
            retVal = fc.showOpenDialog(getMainFrame());
        }
        return fc.getSelectedFile().getAbsolutePath();
    }

    public static void setup(Main main) {
        final Main m = main;
        JFrame f = getMainFrame();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Action");

        MenuItem restart = new MenuItem("Load New Problems");
        restart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("*****************************************");
                m.initialize();
            }
        });

        MenuItem runSolution = new MenuItem("Run Solution", null);
        runSolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m.buildInitialSolution();
            }
        });

        fileMenu.add(restart);
        fileMenu.add(runSolution);
        menuBar.add(fileMenu);
        f.setMenuBar(menuBar);
        mainFrame.setVisible(true);
    }
}
