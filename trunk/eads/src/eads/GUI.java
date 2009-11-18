/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eads;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    private int loc[][] = {{10,10}, {100,100}, {200,200}};

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

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, maxWidth, maxHeight);

        ArrayList area = new ArrayList();
        ArrayList<Integer> temp = new ArrayList<Integer>();

        if (GlobalVariable.DistanceMatrix != null) {
            for (int i = 0; i < GlobalVariable.DistanceMatrix.length; i++) {
                for (int j = i; j < GlobalVariable.DistanceMatrix[i].length; j++) {
                    if (GlobalVariable.DistanceMatrix[i][j] == 0) {
                        temp.add(j);
                    }
                }
                area.add(temp);
                i += temp.size()-1;
                temp = new ArrayList<Integer>();
            }
            System.out.println(area);
            int num = 0;
            Location [] location = new Location [GlobalVariable.m+GlobalVariable.n];
            for (int i = 0; i < area.size(); i++) {
                temp = (ArrayList<Integer>) area.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    g2d.setColor(colors[i]);
                    g2d.fillOval(loc[i][0]+j*30, loc[i][1], 5, 5);
                    location[num] = new Location(loc[i][0]+j*30, loc[i][1]);
                    g2d.drawString(""+num, loc[i][0]+j*30, loc[i][1]);
                    num++;
                }
            }

            for (int i = 0; i < GlobalVariable.careWorkers.size(); i++) {
                g2d.setColor(colors[num]);
                ArrayList<Worker> careWorkersList = GlobalVariable.careWorkers.get(i);
                for (int k = 0; k < careWorkersList.size(); k++) {
                    Worker careWorkers = careWorkersList.get(k);
                    ArrayList<Service> seqServ = careWorkers.getSequenceOfService();
                    int start = careWorkers.getStartLocation();
                    int curr = seqServ.get(0).getCurrentLocation();
                    g2d.drawLine(location[start].getX(), location[start].getY(), location[curr].getX(), location[curr].getY());
                    for (int j = 1; j < seqServ.size(); j++) {
                        int previousLocation = seqServ.get(j-1).getCurrentLocation();
                        int currentLocation = seqServ.get(j).getCurrentLocation();

                        if (currentLocation != -2) {
                            g2d.drawLine(location[previousLocation].getX(), location[previousLocation].getY(), location[currentLocation].getX(), location[currentLocation].getY());
                        }
                    }
                }
            }
        }
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
                m.initialize(null);
            }
        });

        MenuItem runSolution = new MenuItem("Run Solution", null);
        runSolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //m.buildInitialSolution();
                m.computeSolutions();
            }
        });

        fileMenu.add(restart);
        fileMenu.add(runSolution);
        menuBar.add(fileMenu);
        f.setMenuBar(menuBar);
        mainFrame.setVisible(true);
    }
}

class Location {
    int x;
    int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}