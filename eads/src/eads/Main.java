/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eads;

import java.util.ArrayList;

/**
 *
 * @author stanley.2007
 */
public class Main {

    public static double a1 =0.8;//distance Coefficient
    public static double a2 = 0.1;//Time difference Coefficient
    public static double a3 = 0.1;//Urgency to deliver Coefficient

    public void initialize() {
        FileLoader.run();
    }

    public void buildInitialSolution() {
        for (int i = 0; i < FileLoader.m; i++) {
            Worker tempWorker = FileLoader.careWorkers.get(i);

            for (int m = 0; m < FileLoader.services.size(); m++) {
                double cij;
                double bestCost = 1000000000;
                int bestCostClient = -1;
                for (int j = 0; j < FileLoader.services.size(); j++) {
                    int currentTime = tempWorker.getCurrentTime();
                    Service tempService = FileLoader.services.get(j);
                    if (!tempService.isServiced()) {
                        if (!tempWorker.isViolateConstraintIfServiceAddedAtEnd(tempService)) {
                            cij = tempWorker.computeDistanceCost(a1, a2, a3, tempService);
                            if (cij < bestCost) {
                                bestCost = cij;
                                bestCostClient = j;
                            }
                        }
                    }
                } // end of finding best service for the n position
                if (bestCostClient != -1) {
                    tempWorker.addService(FileLoader.services.get(bestCostClient));
                } else if (!tempWorker.isHasTakenBreak()) {
                    Service breakService = new Service(0, 0, 0, 0, -2); //-2 location symbolize break time
                    //tempWorker.takeABreak();
                    tempWorker.addService(breakService);
                }
            }// end of all the clients that can be served by the worker
        }
        int totalDistance = 0;
        for (int i = 0; i <
                FileLoader.m; i++) {
            Worker tempWorker = FileLoader.careWorkers.get(i);
            int workerDistanceTravelled = tempWorker.getTotalDistanceTravelled();
            totalDistance += workerDistanceTravelled;
            System.out.println(i + " distance= " + workerDistanceTravelled + " " + tempWorker.printServiceSequence());
        }

        System.out.println("Total Distance Travelled = " + totalDistance);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.initialize();
        GUI.setup(main);
        main.buildInitialSolution();
        /*
        int totalDistance = 0;
        for (int i = 0; i <
                FileLoader.m; i++) {
            Worker tempWorker = FileLoader.careWorkers.get(i);
            int workerDistanceTravelled = tempWorker.getTotalDistanceTravelled();
            totalDistance += workerDistanceTravelled;
            System.out.println(i + " distance= " + workerDistanceTravelled + " " + tempWorker.printServiceSequence());
        }

        System.out.println("Total Distance Travelled = " + totalDistance);
        */
    }
}
