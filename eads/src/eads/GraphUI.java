package eads;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 *
 * @author Chew Boon Heng
 */
public class GraphUI extends Canvas {

    private static int maxWidth = 600;
    private static int maxHeight = 600;
    private static GraphUI instance;
    private static JFrame mainFrame;
    private static ConfigDialog dialog;
    private static JFileChooser fc = new JFileChooser();
    
    private int scale = 0;
    private Color[] colors = {Color.BLUE, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.CYAN,
        Color.GREEN, Color.BLACK, Color.RED, new Color(139, 69, 19), new Color(250, 128, 114), new Color(135, 206, 235),
        new Color(0, 255, 127), new Color(138, 43, 226), new Color(95, 158, 160), new Color(189, 183, 107),
        new Color(85, 107, 1), new Color(255, 140, 0), new Color(238, 221, 130), new Color(119, 136, 153), new Color(176, 48, 96),
        new Color(102, 205, 170), new Color(107, 142, 35), new Color(219, 112, 147),
        new Color(205, 133, 63), new Color(255, 248, 220)
    };
    List<Location> locations;
    List<Vehicle> solution;

    private static GraphUI getInstance() {
        if (instance == null) {
            instance = new GraphUI();
        }
        return instance;
    }
    
    private static ConfigDialog getConfigDialog() {
        if (dialog == null) {
            dialog = new ConfigDialog(getMainFrame(), true);
        }
        return dialog;
    }
    
    

    public static JFrame getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new JFrame("IS421: Vehicle Routing Problem with Time Window");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            //JPanel panel = new JPanel();
            //panel.add(getInstance(), BorderLayout.CENTER);
            mainFrame.getContentPane().add(getInstance(), BorderLayout.CENTER);
            
            mainFrame.setSize(maxWidth, maxHeight+50);
            //mainFrame.setVisible(true);
        }
        return mainFrame;
    }

    public GraphUI() {
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        //setBorder(BorderFactory.createLineBorder(Color.black));

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, maxWidth, maxHeight);

        if (locations != null) {

            Location warehouse = locations.get(0);

            for (Location custLoc : locations) {
                if (custLoc == warehouse) {
                    continue;
                //paint customer location
                }
                g2d.setColor(Color.BLUE);
                g2d.fillOval(translatePoint(custLoc.x), translatePoint(custLoc.y), 5, 5);
            }

            //paint warehouse
            g2d.setColor(Color.BLACK);
            g2d.fillRect(translatePoint(warehouse.x), translatePoint(warehouse.y), 5, 5);
        }

        //draw legend
        g2d.setColor(Color.BLUE);
        g2d.fillOval(20, maxHeight-10, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Customer Location", 30, maxHeight-5);
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(20, maxHeight-30, 5, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Warehouse", 30, maxHeight-25);
        
        //paint routes or solution
        if (solution != null) {
            int i = 0;
            for (Vehicle route : solution) {

                g2d.setColor(colors[i++]);
                for (int j = 1; j < route.visited.size(); j++) {
                    Location prev = route.visited.get(j - 1);
                    Location curr = route.visited.get(j);
                    g2d.drawLine(translatePoint(prev.x), translatePoint(prev.y),
                            translatePoint(curr.x), translatePoint(curr.y));
                }
                if (i == colors.length) {
                    i = 0;
                }
            }
        }
        
        
    }

    public void updateScale() {
        int maxX = 0, maxY = 0;
        for (Location l : locations) {
            maxX = Math.max(maxX, l.x);
            maxY = Math.max(maxY, l.y);
        }
        if (maxX > maxY) {
            scale = (maxWidth - 100) / maxX;
        } else {
            scale = (maxHeight - 100) / maxY;
        }
    }

    public int translatePoint(int coordinate) {
        return coordinate * scale;
    }

    public static void paintGraph(List<Location> locations, List<Vehicle> solution) {

        getInstance().locations = locations;
        getInstance().solution = solution;
        getInstance().updateScale();

        getInstance().repaint();
    }

    public static void setup(Main main) {

        final Main m = main;
        JFrame f = getMainFrame();

        MenuBar menuBar = new MenuBar(); //INIT
        Menu fileMenu = new Menu("Action");//INIT
        MenuItem restart = new MenuItem("Load New Problem", null);

        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //perform restart
                System.out.println("***************************************************");
                m.initalize();
            }
        });

        MenuItem runSolution = new MenuItem("Run Solution", null);
        runSolution.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m.computeSolution();
            }
        });
        
        MenuItem config = new MenuItem("Change Config", null);
        config.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //pop up dialog box
                getConfigDialog().setVisible(true);
                getConfigDialog().setLocation(getMainFrame().getLocation().x, 
                        getMainFrame().getLocation().y);
            }
        });
        
        fileMenu.add(restart);
        fileMenu.add(runSolution);
        fileMenu.add(config);
        menuBar.add(fileMenu);
        f.setMenuBar(menuBar);
        mainFrame.setVisible(true);
        
    }
    
    public static String getInputFile() {
        int retVal = -1;
        while(retVal != JFileChooser.APPROVE_OPTION) {
            retVal = fc.showOpenDialog(GraphUI.getMainFrame());
        }
        return fc.getSelectedFile().getAbsolutePath();
    }
}