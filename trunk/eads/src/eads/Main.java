package eads;
import java.util.*;
import javax.swing.JOptionPane;

public class Main {

    public static int NoOfCustomer; // include warehouse / depot

    public List<Location> customerLocations; // note: need +1 For warehouse / depot

    public static double DistanceMatrix[][];
    private InputManager inputMgr = new InputManager();
    public static double a1 = 0.8;//distance Coefficient
    public static double a2 = 0.1;//Time difference Coefficient
    public static double a3 = 0.1;//Urgency to deliver Coefficient
    static double originalSumCij = 0;

    public void initalize() {

        inputMgr.run(); //get input

        NoOfCustomer = inputMgr.getTotalCustomerLocations();

        //set the capacity
        Vehicle.Capacity = inputMgr.getVehicleCapacity();
        //set the vehicle limit
        Util.vehicleLimit = inputMgr.getVehicleLimit();

        //instantiate the customer locations
        customerLocations = new ArrayList();
        for (int i = 0; i < NoOfCustomer; i++) {
            int[] loc = inputMgr.getCustomerLocations(i);
            Location temp = new Location(loc[0], loc[4], loc[5], loc[6], loc[3], loc[1], loc[2]);
            customerLocations.add(temp);
        //System.out.println(ClientCollection[i].ClientIndex + "   " + ClientCollection[i].XCoor );
        }
        //Arrays.sort(ClientCollection);

        //calculate all distances between each possible edges   //this is also the travel cost
        DistanceMatrix = new double[NoOfCustomer][NoOfCustomer];
        for (int i = 0; i < NoOfCustomer; i++) {
            for (int j = 0; j < NoOfCustomer; j++) {
                int ciX = customerLocations.get(i).x;
                int ciY = customerLocations.get(i).y;
                int cjX = customerLocations.get(j).x;
                int cjY = customerLocations.get(j).y;

                DistanceMatrix[i][j] = Math.sqrt(Math.pow(ciX - cjX, 2) + Math.pow(ciY - cjY, 2));
            }
        }
        //paint customer locations
        GraphUI.paintGraph(customerLocations, null);
    }

    private List<Vehicle> buildInitialSolution() {

        List<Vehicle> vehicleList = new ArrayList();
        int SumOfClientServiced = 0;        // to track number of location taken up

        int BestCostClient = 0;

        double Cij = 0;
        double BestCost;


        //track whether a client already been serviced
        boolean Used[] = new boolean[NoOfCustomer];
        for (int i = 1; i < NoOfCustomer; i++) {
            Used[i] = false; // initialize all clients not yet being serviced

        }

     
        //heuristic start here
        int Counter;
        do {
        	
   
        	Vehicle TempVehicle = new Vehicle(customerLocations.get(0));    //warehouse

            originalSumCij = 0;
            do {
                Counter = 0;
                BestCost = 10000000;
                for (int i = 1; i < NoOfCustomer; i++) //for all customer location (exclude warehouse)
                {
                    if (Used[i] == false && !TempVehicle.isViolateConstraintIfCustomerAddedAtEnd(customerLocations.get(i))) {
                        //compute Cij
                        Cij = TempVehicle.computeWeightedTimeDistanceCost(a1, a2, a3,
                                TempVehicle.getLastVisited(), customerLocations.get(i));
                        if (Cij < BestCost) {

                            BestCostClient = i;
                            BestCost = Cij;
                            Counter++;
                        }
                    }
                }
                if (Counter != 0) {
                    //TempVehicle.isViolateConstraint(customerLocations.get(BestCostClient));   //recount its end service time
                    Used[BestCostClient] = true; // marked this customer already been taken

                    TempVehicle.Insert(customerLocations.get(BestCostClient));
                    originalSumCij += BestCost;
                    TempVehicle = ReOrderClient_2Opt(TempVehicle); //intra-route 2-opt
                    
                }
            } while (Counter != 0);   //only end when all location left violated constraint

            TempVehicle.BackToWarehouse();   // vehicle can't handle any more customer location, return to warehouse

            //TempVehicle = ReOrderClient_2Opt(TempVehicle); //intra-route 2-opt again with warehouse

            vehicleList.add(TempVehicle);
            SumOfClientServiced += TempVehicle.GetCustomerServed();
            
        } while (SumOfClientServiced < NoOfCustomer - 1);   //stop when all customer locations are served

        return vehicleList;
    }

    private List<Vehicle> improveSolution(List<Vehicle> initialSolution, boolean showSolutionIteration) {

        //int maxIteration = 200;

//        List<Vehicle> bestSolution = initialSolution;
//        List<Vehicle> betterSolution = initialSolution;
//        do {
//            bestSolution = betterSolution;  
//            List<List<Vehicle>> solutions = Util.LS_shiftOperator(bestSolution);
//            solutions.add(bestSolution);
//            betterSolution = Util.compareSolutions(solutions);
//            //if(--maxIteration == 0) break;
//            
//            if(showSolutionIteration)
//                GraphUI.paintGraph(customerLocations, bestSolution);
//            
//        } while (betterSolution != bestSolution );

        List<Vehicle> TempSolution = initialSolution;
        List<Vehicle> NewSolution = new ArrayList();
        Vehicle CurrVehicle;
        Vehicle TempVehicle;
        Vehicle ReplaceVehicle;
        Vehicle newVehicle = new Vehicle(customerLocations.get(0));

        Location Temp = new Location(1, 1, 1, 1, 1, 1, 1); //anyway, Dummy value

        int CounterCurrClient;
        int CounterTempClient;
        int NoOfClient = 1000; //Dummy value 

        List<VisitingLocation> TempList;
        List<VisitingLocation> CurrList;

        Boolean FlagInsertDone = true;
        Boolean FeasibleInsert = true;

        double Cij = 0;
        double SumTmpCij = 0;
        double BestCijCost = 1000000;
        int NoOfVehicle = initialSolution.size();

        //System.out.println();     - debug
        //System.out.println("---------No Of Initial Solution" +NoOfVehicle+  "-------");   - debug

        for (int i = NoOfVehicle - 1; i > 0; i--) {
            //CurrVehicle = new Vehicle(customerLocations.get(0));
            CurrVehicle = TempSolution.get(i);
            CurrList = CurrVehicle.visited;

            boolean Remove[] = new boolean[CurrList.size()];//the depot too

            //System.out.println();
            //System.out.println("---------Inital TEmpVehicle : " + i + "---------");

            //for (int p = 0; p < CurrVehicle.Counter; p++) {
            //    System.out.print(CurrVehicle.Serve[p].locationIndex + "--> ");
            //}
            //System.out.println();
            //System.out.println("---------End Inital TEmpVehicle : " + i + "---------");


            //for (int p = 1; p <= CurrVehicle.visited.size() -1; p++) {  //don't include warehouse at start and end
            //    Remove[p] = false;
            //}
            for (int j = i - 1; j >= 0; j--) {
                //TempVehicle = new Vehicle(customerLocations.get(0));
                TempVehicle = TempSolution.get(j);
                TempList = TempVehicle.visited;

                for (int k = 1; k < CurrVehicle.visited.size() - 1; k++) {
                    Boolean FlagReplace = false;
                    BestCijCost = 1000000;
                    ReplaceVehicle = new Vehicle(customerLocations.get(0));
                    for (int m = 1; m < TempVehicle.visited.size() - 1; m++) {
                        FeasibleInsert = true;
                        FlagInsertDone = true;
                        SumTmpCij = 0;
                        Vehicle DummyVehicle = new Vehicle(customerLocations.get(0));
                        for (int n = 1; n < TempVehicle.visited.size() - 1; n++) {

                            if ((m == n) && (FlagInsertDone == true)) {
                                if (DummyVehicle.isViolateConstraintIfCustomerAddedAtEnd(CurrVehicle.visited.get(k))) {

                                    Cij = DummyVehicle.computeWeightedTimeDistanceCost(a1, a2, a3,
                                            DummyVehicle.getLastVisited(), customerLocations.get(k));

                                    DummyVehicle.Insert(CurrVehicle.visited.get(k));
                                    SumTmpCij += Cij;
                                    FlagInsertDone = false;
                                    n--;
                                } else {
                                    FeasibleInsert = false;
                                    break;
                                }
                            } else {
                                if (DummyVehicle.isViolateConstraintIfCustomerAddedAtEnd(CurrVehicle.visited.get(n))) {

                                    Cij = DummyVehicle.computeWeightedTimeDistanceCost(a1, a2, a3,
                                            DummyVehicle.getLastVisited(), customerLocations.get(n));
                                    DummyVehicle.Insert(TempVehicle.visited.get(n));
                                    SumTmpCij += Cij;
                                } else {
                                    FeasibleInsert = false;
                                    break;
                                }
                            }
                        }//n

                        if ((FeasibleInsert == true) && (SumTmpCij < BestCijCost)) {
                            ReplaceVehicle = DummyVehicle;
                            BestCijCost = SumTmpCij;
                            Remove[k] = true;
                            FlagReplace = true;

                        }
                    }//m

                    if (FlagReplace == true) {
                        //   System.out.println();
                        //   System.out.println("---------Final ReplaceVehicle---------");

                        //    for (int p = 0; p <= ReplaceVehicle.Counter; p++) {
                        //        System.out.print(ReplaceVehicle.Serve[p].locationIndex + "--> ");
                        //    }
                        //    System.out.println();
                        //    System.out.println("---------End of Final ReplaceVehicle---------");
                        TempVehicle = ReplaceVehicle;
                    }
                }//k

                newVehicle = new Vehicle(customerLocations.get(0));
                NoOfClient = CurrVehicle.visited.size() - 1;
                for (int w = 1; w < CurrVehicle.visited.size() - 1; w++) {
                    if (Remove[w] != true) {
                        newVehicle.Insert(CurrVehicle.visited.get(w)); //no to check vioconstraint,for sure can.

                    } else {
                        NoOfClient--;
                    }
                }

                /*
                System.out.println();
                System.out.println("---------Final TEmpVehicle---------");
                
                for ( int p = 0; p <= TempVehicle.Counter;p++)
                System.out.print(TempVehicle.Serve[p].locationIndex + "--> ");
                System.out.println();
                System.out.println("---------End of Final TEmpVehicle---------");
                 */
                TempSolution.set(j, TempVehicle); // replace the original with update one

            }//j

            if (NoOfClient > 0) {

                newVehicle.BackToWarehouse();//back to depot

                //newVehicle.Counter--;//depot not count as customer

                //System.out.println();
                //System.out.println("---------End TEmpVehicle : " + i + "---------");

                //for (int p = 0; p <= newVehicle.Counter; p++) {
                //    System.out.print(newVehicle.Serve[p].locationIndex + "--> ");
                //}
                //System.out.println();
                //System.out.println("---------End End TEmpVehicle : " + i + "---------");

                TempSolution.set(i, newVehicle);
                NewSolution.add(newVehicle);
            }
        }//i

        TempSolution.get(0).BackToWarehouse();//back to depot

        //TempSolution.get(0).Counter--;//depot not count as customer

        NewSolution.add(TempSolution.get(0));
        return NewSolution;


    //return bestSolution;
    }

    Vehicle ReOrderClient_2Opt(Vehicle TempVehicle) {
        double TempDistance;
        double TempTimeElapsed;
        double ValueCompare, ValueOriginal; // try to maximize this value

        double SumCij = 0;

        Random RandNo;
        Boolean FeasibleFlag;
        Vehicle TranVehicle = new Vehicle(TempVehicle.warehouse);
        int i_Index, j_Index; //i, j Two Edge to swap 

        Location Temp = new Location(1, 1, 1, 1, 1, 1, 1); //anyway, Dummy value

        int CounterClient = TempVehicle.visited.size() - 1;
        List<Location> TempList = new ArrayList();
        TranVehicle = TempVehicle;
        for (Location v : TranVehicle.visited) {
            TempList.add(v);
        }
        ValueOriginal = TempVehicle.GetDistanceTravel() / TempVehicle.GetTimeElapsed();

        RandNo = new Random();

        Vehicle DummyVehicle;

        for (int i = 1; i <= CounterClient; i++) {

            i_Index = RandNo.nextInt(CounterClient) + 1;
            j_Index = RandNo.nextInt(CounterClient) + 1;


            Temp = TempList.get(i_Index);
            Temp = TempList.set(i_Index, TempList.get(j_Index));
            TempList.set(j_Index, Temp);

            FeasibleFlag = true;
            DummyVehicle = new Vehicle(TempVehicle.warehouse);
            SumCij = 0;
            for (int j = 1; j <= CounterClient; j++) {
                if (!DummyVehicle.isViolateConstraintIfCustomerAddedAtEnd(TempList.get(j))) {
                    double Cij = DummyVehicle.computeWeightedTimeDistanceCost(a1, a2, a3,
                            DummyVehicle.getLastVisited(), TempList.get(j));
                    SumCij += Cij;
                    DummyVehicle.Insert(TempList.get(j));
                } else {
                    FeasibleFlag = false;
                    break;
                }
            }

            if (FeasibleFlag != false) {


                if (originalSumCij - SumCij > 0.00001) {
                    originalSumCij = SumCij;
                    //System.out.println("Start-2Opt-------------------------");
                    //for ( int p = 0; p <= TempVehicle.Counter;p++)
                    //	System.out.print(TempVehicle.Serve[p].locationIndex + "--> ");

                    ////System.out.println("-------------------------");

                    //for ( int j = 0; j<= DummyVehicle.Counter;j++)
                    //	System.out.print(DummyVehicle.Serve[j].locationIndex + "--> ");

                    //System.out.println("-------------------------");
                    //System.out.println("ENd-2Opt-------------------------");
                    TempVehicle = DummyVehicle;
                }

            }


        }

        return TempVehicle;
    }

    public void printSolution(List<Vehicle> solution) {

        double SumofTotalDistance = 0;
        for (Vehicle route : solution) {
            for (int i = 0; i < route.visited.size(); i++) {
                System.out.print(route.visited.get(i).locationIndex);
                if (i < route.visited.size() - 1) {
                    System.out.print("-->");
                }
            }
            System.out.println();
            //route.recomputeDistance();
            SumofTotalDistance += route.GetDistanceTravel();
        }

        System.out.println("Number Of Vehicle  " + solution.size());
        System.out.println("Total Distance  " + SumofTotalDistance);

        //check solution is feasible
        Util.isSolutionFeasible(solution, customerLocations, true);

        //paint solution
        GraphUI.paintGraph(customerLocations, solution);
    }

    public void computeSolution() {


        if (customerLocations == null) {
            JOptionPane.showMessageDialog(GraphUI.getMainFrame(), "Problem has not been loaded.");
            return;
        }

        int MinNoOfVehicle = 100;
        double TheDistance = 1000000;
        double TempDistance = 1000000;
        List<Vehicle> OptimalSolution = new ArrayList();

        for (double i = 0.1; i <= 0.5; i += 0.1) 
        {
            for (double j = 0.1; j <= 0.5; j += 0.1) 
        	{
                for (double k = 0; k <= 1 - (i + j); k += 0.1) 
        		{
                    a1 = i;
                    a2 = j;
                    a3 = k;
                    List<Vehicle> solution = buildInitialSolution();
                    solution = improveSolution(solution, true);  //apply local search
                    TempDistance = 0;
                    for (Vehicle l : solution) {
                        TempDistance += l.GetDistanceTravel();
                    }
                    if (solution.size() < MinNoOfVehicle || (solution.size() == MinNoOfVehicle && TempDistance < TheDistance)) {
                        OptimalSolution = solution;
                        MinNoOfVehicle = solution.size();
                        TheDistance = TempDistance;

                    }

                }
            }
        }  
        printSolution(OptimalSolution);
    }

    public static void main(String[] args) {

        Main m = new Main();
        GraphUI.setup(m);
    }
}
