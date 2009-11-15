package eads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import Utility.ServiceComparator;

public class FileLoader {

    static BufferedReader br;
    static boolean initializeDistanceMatrix = false;

    public static void main(String[] args) {
        run(null);
    }

    public static boolean run(String loadFile) {
        /*
         *     Test Files
         */
            //50-3-had_d_9.txt
            //40-3-had_d_2.txt
            //30--2-had_d_0.txt
            //20-3-had_d_8.txt
            //10-3-had_d_11.txt
            //10-1-had_d_1.txt

            //40-3-had_d_18.txt
            //30-2-had_d_10.txt
            //20-2-had_d_5.txt
            //10-1-had_d_0.txt

        //String fileName = GUI.getInputFile(); // for GUI enable this and comment below
        String fileName = (loadFile == null) ? "data\\40-3-had_d_18.txt" : loadFile;
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
                        GlobalVariable.m = Integer.parseInt(temp[1]);
                        System.out.println("M is " + GlobalVariable.m);
                    }

                    if (s.startsWith("nc=")) {
                        String[] temp = s.split("=");
                        GlobalVariable.nc = Integer.parseInt(temp[1]);
                        System.out.println("NC is " + GlobalVariable.nc);

                        for (int i = 0; i < GlobalVariable.nc; i++) {
                            GlobalVariable.careWorkers.add(new ArrayList<Worker>());
                            GlobalVariable.services.add(new ArrayList<Service>());
                        }
                    }

                    if (s.startsWith("n=")) {
                        String[] temp = s.split("=");
                        GlobalVariable.n = Integer.parseInt(temp[1]);
                        System.out.println("N is " + GlobalVariable.n);
                        initDone = true;
                        br.readLine();
                    }

                } else if (!initWorkers) {
                    if (!initializeDistanceMatrix) {
                        GlobalVariable.DistanceMatrix = new int[GlobalVariable.m +GlobalVariable.n]
                                [GlobalVariable.m + GlobalVariable.n];
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
                            earliestBreakTime, latestBreakTime, breakDuration, noOfWorkers + GlobalVariable.n);
                    GlobalVariable.careWorkers.get(skillNumber).add(w);
                    System.out.println(w.toString());
                    noOfWorkers++;
                    if (noOfWorkers == GlobalVariable.m) {
                        initWorkers = true;
                        br.readLine();
                    }
                } else if (!initLocations) {
                    String[] temp = s.split("\t");
                    System.out.print("location = " + noOfLocations + "\t");
                    for (int j = 0; j < temp.length; j++) {
                        String tempVal = temp[j].replace(" ", "");
                        if (tempVal.length() >= 1 && j < GlobalVariable.n + GlobalVariable.m) {
                            GlobalVariable.DistanceMatrix[noOfLocations][j] = Integer.parseInt(temp[j].replace(" ", ""));
                            System.out.print(GlobalVariable.DistanceMatrix[noOfLocations][j] + "\t");
                        }
                    }
                    System.out.println("");
                    noOfLocations++;
                    if (noOfLocations == (GlobalVariable.n + GlobalVariable.m)) {
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
                    GlobalVariable.services.get(requiredSkill).add(ser);
                    //System.out.println(ser.toString());
                    noOfServices++;
                    if (noOfServices == (GlobalVariable.n)) {
                        initService = true;
                    }
                } else {
                    System.out.println("DONE");
                }
            }
            ServiceComparator servComparator = new ServiceComparator();
            for (int i = 0; i < GlobalVariable.nc; i++) {
                Collections.sort(GlobalVariable.services.get(i), servComparator);
            }
            for (int i = 0; i < GlobalVariable.nc; i++) {
                ArrayList<Service> tempList = GlobalVariable.services.get(i);
                for (int j = 0; j < tempList.size(); j++) {
                    Service temp = tempList.get(j);
                    System.out.println(temp.toString());
                }
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
