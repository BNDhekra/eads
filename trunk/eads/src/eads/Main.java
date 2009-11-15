/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eads;

import Utility.SolutionComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author stanley.2007
 */
public class Main {

    //GA properties
    public static int generationSize = 300; // total population size
    public static double crossOver = 0.9; // probability to crossover
    public static double mutation = 1 - crossOver; // probability to mutate
    public static int iterationLimit = 100; // limit to iteration before end
    public static boolean elitism = true;
    public static ArrayList<Solution> populationGA = new ArrayList<Solution>();

    public void initialize(String loadFile) {
        FileLoader.run(loadFile);
    }

    // compute solution
    public void computeSolutions() {
        buildInitialGeneration();
        System.out.println("");// break line
        System.out.println("********************************************");// break line
        System.out.println("");// break line

        System.out.println(" ~~~  INITIAL SOLUTION  ~~~ ");

        // print out initial result
        Solution tempSolution = populationGA.get(0);
        for (int i = 0; i < GlobalVariable.nc; i++) {
            ArrayList<Worker> tempWorkerList = tempSolution.getWorkerList().get(i);
            for (int j = 0; j < tempWorkerList.size(); j++) {
                Worker tempWorker = tempWorkerList.get(j);
                int workerDistanceTravelled = tempWorker.getTotalDistanceTravelled();
                System.out.println(j + " distance= " + workerDistanceTravelled + " " + tempWorker.printServiceSequence());
            }
        }
        System.out.println("Initial Solution Total Distance Travelled = " + tempSolution.getTotalDistance());


        System.out.println("");// break line
        System.out.println("********************************************");// break line
        System.out.println("");// break line


        SolutionComparator solComparator = new SolutionComparator();
        // GA crossover and mutation generations
        for (int i = 0; i < iterationLimit; i++) {
            Collections.sort(populationGA, solComparator);
            System.out.println(" generation " + i + " " + populationGA.get(0).getTotalDistance());
            improveSolutionGA();
        }


        System.out.println("");// break line
        System.out.println("********************************************");// break line
        System.out.println("");// break line


        Collections.sort(populationGA, solComparator);

        System.out.println(" ~~~  BEST SOLUTION  ~~~");

        // print out best result
        tempSolution = populationGA.get(0); // 0 is best!
        for (int i = 0; i < GlobalVariable.nc; i++) {
            ArrayList<Worker> tempWorkerList = tempSolution.getWorkerList().get(i);
            for (int j = 0; j < tempWorkerList.size(); j++) {
                Worker tempWorker = tempWorkerList.get(j);
                int workerDistanceTravelled = tempWorker.getTotalDistanceTravelled();
                System.out.println(j + " distance= " + workerDistanceTravelled + " " + tempWorker.printServiceSequence());
            }
        }

        System.out.println("Best Solution Total Distance Travelled = " + tempSolution.getTotalDistance() + " " + tempSolution.countLocation());
    }

    public Solution generateChromosome(ArrayList<ArrayList<Service>> serviceSequence) {
        ArrayList<ArrayList<Worker>> solution = new ArrayList<ArrayList<Worker>>();
        Solution newSolution = new Solution(serviceSequence);

        for (int j = 0; j < GlobalVariable.nc; j++) {
            solution.add(new ArrayList<Worker>());
            ArrayList<Worker> tempWorkerList = GlobalVariable.careWorkers.get(j);
            for (int k = 0; k < tempWorkerList.size(); k++) {
                Worker tempWorker = new Worker(tempWorkerList.get(k));
                solution.get(j).add(tempWorker);
            }
        }

        for (int i = 0; i < GlobalVariable.nc; i++) {
            ArrayList<Service> tempServiceList = serviceSequence.get(i);
            int index = 0;
            int takenBreakIndex = 0;
            do {
                Service tempService = tempServiceList.get(index);
                ArrayList<Worker> tempWorkerList = solution.get(tempService.getRequiredSkill());

                double cij;
                double bestCost = 1000000000;
                int bestCostWorker = -1;
                for (int j = 0; j < tempWorkerList.size(); j++) {
                    Worker tempWorker = tempWorkerList.get(j);
                    if (!tempWorker.isViolateConstraintIfServiceAddedAtEnd(tempService)) {
                        cij = GlobalVariable.DistanceMatrix[tempWorker.getCurrentLocation()][tempService.getCurrentLocation()];
                        if (cij < bestCost) {
                            bestCost = cij;
                            bestCostWorker = j;
                        }
                    }
                }// end of finding best worker for the service

                if (bestCostWorker != -1) {
                    tempWorkerList.get(bestCostWorker).addService(tempService);
                    index++;
                } else {
                    if (takenBreakIndex < tempWorkerList.size()) {
                        Worker tempWorker = tempWorkerList.get(takenBreakIndex);
                        if (!tempWorker.isHasTakenBreak()) {
                            tempWorker.addService(new Break());
                        }
                        takenBreakIndex++;
                    } else {
                        if (index < GlobalVariable.n) {
                            newSolution.setFeasible(false);
                            break;
                        }
                    }
                }// end of all the clients that can be served by the worker
            } while (index != tempServiceList.size());
        }
        newSolution.setWorkerList(solution);
        return newSolution;
    }

    public void buildInitialGeneration() {
        int currentPopulationSize = 0;
        ArrayList scheduleSequence = new ArrayList();

        //generate initial solution
        ArrayList<ArrayList<Service>> initialServiceSequence = (ArrayList<ArrayList<Service>>) GlobalVariable.services.clone();
        Solution initialSolution = generateChromosome(initialServiceSequence);

        String scheduleString = "";
        for (ArrayList<Service> tempServiceList : initialServiceSequence) {
            for (int i = 0; i < tempServiceList.size(); i++) {
                scheduleString += tempServiceList.get(i).getCurrentLocation();
            }
        }

        if (initialSolution.isFeasible() && initialSolution.totalCountService() == GlobalVariable.n) {
            populationGA.add(initialSolution);
            currentPopulationSize++;
        }

        //generate population (Chromosomes)
        do {
            ArrayList<ArrayList<Service>> newServiceSequence = (ArrayList<ArrayList<Service>>) GlobalVariable.services.clone();
            newServiceSequence = randomizeServiceSequence(newServiceSequence, 5);

            for (ArrayList<Service> tempServiceList : newServiceSequence) {
                for (int i = 0; i < tempServiceList.size(); i++) {
                    scheduleString += tempServiceList.get(i).getCurrentLocation();
                }
            }

            boolean unique = true;
            for (int i = 0; i < scheduleSequence.size(); i++) {
                if (scheduleString.equals(scheduleSequence.get(i))) {
                    unique = false;
                    break;
                }
            }

            if (unique) {
//                for (ArrayList<Service> tempServiceList : newServiceSequence) {
//                    for (int i = 0; i < tempServiceList.size(); i++) {
//                        System.out.print(tempServiceList.get(i).getCurrentLocation() + " ");
//                    }
//                    System.out.println("");
//                }
                Solution newSolution = generateChromosome(newServiceSequence);

                if (newSolution.isFeasible() && newSolution.totalCountService() == GlobalVariable.n) {
                    populationGA.add(newSolution);
                    currentPopulationSize++;
                }
            }
        } while (currentPopulationSize != generationSize);
    }

    // improve solution with GA
    public void improveSolutionGA() {
        ArrayList<Solution> newGeneration = new ArrayList<Solution>();
        int index = 0;
        do {
            //under elitism, copy over the best 2 solution to next generation
            if (elitism & (index == 0 || index == 1)) {
                newGeneration.add(populationGA.get(0));
                index++;
            } else {
                Random r = new Random();

                int firstParentIndex = r.nextInt(generationSize);
                int secondParentIndex = r.nextInt(generationSize);

                // randomly take the better half as the parent
                firstParentIndex = firstParentIndex / 2;
                secondParentIndex = secondParentIndex / 2;

                ArrayList<ArrayList<Service>> parentA = populationGA.get(firstParentIndex).getLocationSequence();
                ArrayList<ArrayList<Service>> parentB = populationGA.get(secondParentIndex).getLocationSequence();

                double odds = r.nextInt(10) * 0.1;

                if (odds < crossOver) { // crossover
                    int crossOverCount = 0;
                    int split = 3;
                    int count = 0;
                    int retry = 0;
                    do {
                        ArrayList<ArrayList<Service>> newOffSpring = new ArrayList<ArrayList<Service>>();
                        for (int o = 0; o < GlobalVariable.nc; o++) {
                            ArrayList<Service> tempServiceListA = parentA.get(o);
                            ArrayList<Service> tempServiceListB = parentB.get(o);
                            ArrayList<Service> tempList = new ArrayList<Service>();

                            int serviceSize = tempServiceListA.size();
                            int splitIndex = (serviceSize < split) ? 1 : serviceSize / split;
                            int additional = serviceSize % split;
                            split = (serviceSize < split) ? serviceSize : split;
                            int upperIndex = 0;
                            int crossOverIndex = r.nextInt(split);

                            for (int j = 0; j < split; j++) {
                                for (int n = upperIndex; n < splitIndex + upperIndex; n++) {
                                    if (j < crossOverIndex) {
                                        tempList.add(new Service(tempServiceListA.get(n)));
                                        count++;
                                    } else {
                                        tempList.add(new Service(tempServiceListB.get(n)));
                                        count++;
                                    }
                                }

                                upperIndex += splitIndex;
                                splitIndex = ((j == split - 2)) ? splitIndex + additional : splitIndex;
                            }
                            newOffSpring.add(tempList);
                        }
                        count = count +0;
                        Solution newSolution = generateChromosome((newOffSpring));
                        if (newSolution.isFeasible() && newSolution.totalCountService() == GlobalVariable.n) {
                            newGeneration.add(newSolution);
                            index++;
                            crossOverCount++;
                        } else {
                            retry++;
                            if(retry ==5){
                                break;
                            }
                        }
                        
                    } while (crossOverCount != 2);
                } else { //mutation
                    ArrayList<ArrayList<Service>> newOffSpring = (ArrayList<ArrayList<Service>>) parentA.clone();
                    newOffSpring = randomizeServiceSequence(newOffSpring, 1);

                    Solution newSolution = generateChromosome((newOffSpring));
                    if (newSolution.isFeasible() && newSolution.totalCountService() == GlobalVariable.n) {
                        newGeneration.add(newSolution);
                        index++;
                    }
                }
            }
        } while (index < generationSize);

        cloneNewPopulation(newGeneration);
    }

    public ArrayList<ArrayList<Service>> randomizeServiceSequence(ArrayList<ArrayList<Service>> serviceSequence, int iteration) {
        int split = 3; // number of split in chromosome

        for (ArrayList<Service> tempSequence : serviceSequence) {
            int serviceSize = tempSequence.size();
            int splitIndex = (serviceSize < split) ? 1 : serviceSize / split;
            int additional = serviceSize % split;
            split = (serviceSize < split) ? serviceSize : split;
            Random r = new Random();
            int upperIndex = 0;

            for (int j = 0; j < split; j++) {
                for (int i = 0; i < iteration; i++) {
                    int firstIndex = r.nextInt(splitIndex) + upperIndex;
                    int secondIndex = r.nextInt(splitIndex) + upperIndex;

                    Service dummy = new Service(tempSequence.get(firstIndex)); // clone the temp service
                    tempSequence.set(firstIndex, tempSequence.get(secondIndex));
                    tempSequence.set(secondIndex, dummy);
                }

                upperIndex += splitIndex;
                splitIndex = ((j == split - 1)) ? splitIndex + additional : splitIndex;
            }
        }

        return serviceSequence;
    }

    // clear and clone new generation into populationGA
    public void cloneNewPopulation(ArrayList<Solution> newGeneration) {
        populationGA.clear();
        for (int i = 0; i < newGeneration.size(); i++) {
            populationGA.add(newGeneration.get(i));
        }
    }

    // main methods
    public static void main(String[] args) {
        Main main = new Main();

        main.initialize(null);
        GUI.setup(main); //for GUI enable this

        main.computeSolutions();
    }
}
