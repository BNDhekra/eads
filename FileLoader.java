package eads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import Utility.ServiceComparator;

public class FileLoader {

    static BufferedReader br;
    public static int m = 0; // number of medical professionals
    public static int nc = 0; // number of skills set
    public static int n = 0; // number of patients
    public static ArrayList<Worker> careWorkers = new ArrayList<Worker>(); // list of workers
    public static ArrayList<Service> services = new ArrayList<Service>(); // list of servicess
    public static int DistanceMatrix[][];
    static boolean initializeDistanceMatrix = false;

    public static void main(String[] args) {
        run(null);
    }

    public static boolean run(String loadFile) {
        String fileName;
        
        //40-3-had_d_18.txt
        //30-2-had_d_10.txt
        //20-2-had_d_5.txt
        //10-1-had_d_0.txt
        fileName = (loadFile == null) ? "data\\20-2-had_d_5.txt" : loadFile;
        System.out.println("Read fileName is: " + fileName);
        return parse(fileName);
    }

    private static boolean parse(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
            String s;

            boolean initDone = false;

            boolean initWorkers = false;
            int noOfWorkers = 0;

            boolean initLocations = false;
            int noOfLocations = 0;

            boolean initService = false;
            int noOfServices = 0;

            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.equals("")) {
                    continue;
                }


                if (!initDone) {
                    if (s.startsWith("m=")) {
                        String[] temp = s.split("=");
                        m = Integer.parseInt(temp[1]);
                        System.out.println("M is " + m);
                    }

                    if (s.startsWith("nc=")) {
                        String[] temp = s.split("=");
                        nc = Integer.parseInt(temp[1]);
                        System.out.println("NC is " + nc);
                    }

                    if (s.startsWith("n=")) {
                        String[] temp = s.split("=");
                        n = Integer.parseInt(temp[1]);
                        System.out.println("N is " + n);
                        initDone = true;
                        br.readLine();
                    }

                } else if (!initWorkers) {
                    if (!initializeDistanceMatrix) {
                        DistanceMatrix = new int[m + n][m + n];
                        initializeDistanceMatrix = true;
                    }
                    String[] temp = s.split("\t");
                    int skillNumber = Integer.parseInt(temp[1].replace(" ", ""));
                    int workStartTime = Integer.parseInt(temp[2].replace(" ", ""));
                    int workEndTime = Integer.parseInt(temp[3].replace(" ", ""));
                    int earliestBreakTime = Integer.parseInt(temp[4].replace(" ", ""));
                    int latestBreakTime = Integer.parseInt(temp[5].replace(" ", ""));
                    int breakDuration = Integer.parseInt(temp[6].replace(" ", ""));
                    Worker w = new Worker(skillNumber, workStartTime, workEndTime,
                            earliestBreakTime, latestBreakTime, breakDuration, noOfWorkers + n);
                    careWorkers.add(w);
                    System.out.println(w.toString());
                    noOfWorkers++;
                    if (noOfWorkers == m) {
                        initWorkers = true;
                        br.readLine();
                    }
                } else if (!initLocations) {
                    String[] temp = s.split("\t");
                    System.out.print("location = " + noOfLocations + "\t");
                    for (int j = 0; j < temp.length; j++) {
                        String tempVal = temp[j].replace(" ", "");
                        if (tempVal.length() >= 1 && j < n+m) {
                            DistanceMatrix[noOfLocations][j] = Integer.parseInt(temp[j].replace(" ", ""));
                            System.out.print(DistanceMatrix[noOfLocations][j] + "\t");
                        }
                    }
                    System.out.println("");
                    noOfLocations++;
                    if (noOfLocations == (n + m)) {
                        initLocations = true;
                        br.readLine();
                    }
                } else if (!initService) {
                    String[] temp = s.split("\t");
                    int earliestStartTime = Integer.parseInt(temp[0].replace(" ", ""));
                    int latestStartTime = Integer.parseInt(temp[1].replace(" ", ""));
                    int duration = Integer.parseInt(temp[2].replace(" ", ""));
                    int requiredSkill = Integer.parseInt(temp[3].replace(" ", ""));
                    Service ser = new Service(earliestStartTime, latestStartTime, duration, requiredSkill, noOfServices);
                    services.add(ser);
                    System.out.println(ser.toString());
                    noOfServices++;
                    if (noOfServices == (n)) {
                        initService = true;
                    }
                } else {
                    System.out.println("DONE");
                }

                System.out.println(s);
            }
            ServiceComparator servComparator = new ServiceComparator();
            Collections.sort(services, servComparator);
            for (int i = 0; i < n; i++) {
                Service temp = services.get(i);

                System.out.println("service location = " + temp.getCurrentLocation() +
                        ", earliest = " + temp.getEarliestStartTime() +
                        ", latest = " + temp.getLatestStartTime() +
                        ", duration = " + temp.getDuration() +
                        ", skill =" + temp.getRequiredSkill());
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
