package eads;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Chew Boon Heng
 */
public class Util {

    public static int vehicleLimit = 0;

    public static boolean isSolutionFeasible(List<Vehicle> solution, List<Location> customerLocations,
            boolean isMsg) {

        //check constraint not violated
        boolean isConstraintsViolated = false;
        boolean hasDuplicatedVisit = false;
        boolean isVisitMissed = false;

        for (Location cust : customerLocations) {    //set visit flags to default
            cust.isVisited = false;
        }

        for (Vehicle route : solution) {

            if (route.isViolateConstraint()) {
                isConstraintsViolated = true;
            }

            //check duplicate visits
            for (Location cust : route.visited) {
                if (customerLocations.get(cust.locationIndex).isVisited) {
                    //already visited
                    if (cust.locationIndex != 0) {
                        //System.out.println("duplicate location detected: " + customerLocations.get(cust.locationIndex).locationIndex);
                        hasDuplicatedVisit = true;
                    }
                }
                customerLocations.get(cust.locationIndex).isVisited = true;
            }
        }

        //check missed visits
        for (Location custA : customerLocations) {
            if (!custA.isVisited) {
                //System.out.println("location not visited: " + custA.locationIndex);
                isVisitMissed = true;
            }
        }
        if (isConstraintsViolated) {
            if (isMsg) {
                System.out.println("solution is not feasible. Some constraints are violated.");
            }
            return false;
        } else {
            if (isMsg) {
                System.out.println("All constraints are not violated.");
            }
        }
        if (hasDuplicatedVisit) {
            if (isMsg) {
                System.out.println("solution is not feasible. Some routes have duplicated customer visits.");
            }
            return false;
        } else {
            if (isMsg) {
                System.out.println("All customer locations are visited only once.");
            }
        }
        if (isVisitMissed) {
            if (isMsg) {
                System.out.println("solution is not feasible. Some customer locations are missed.");
            }
            return false;
        } else {
            if (isMsg) {
                System.out.println("All customer locations are visited. No location missed.");
            }
        }
        if (solution.size() > vehicleLimit) {
            if (isMsg) {
                System.out.println("solution is not feasible. total vehicles suggested by solution is over limit{" + vehicleLimit + "}.");
            }
            return false;
        } else {
            if (isMsg) {
                System.out.println("Total Vehicles needed is " + solution.size() +
                        ", which is within vehicle limit(" + vehicleLimit + ").");
            }
        }
        return true;
    }

    //return the better solution  (by minimum route, then minimum distance)
    public static List<Vehicle> compareSolution(List<Vehicle> solution1, List<Vehicle> solution2) {
        if (solution1.size() < solution2.size()) {
            return solution1;
        } else if (solution1.size() == solution2.size()) {
            double dist1 = 0, dist2 = 0;
            for (Vehicle route : solution1) {
                dist1 += route.GetDistanceTravel();
            }
            for (Vehicle route2 : solution2) {
                dist2 += route2.GetDistanceTravel();
            }
            if (dist1 < dist2) {
                return solution1;
            }
            return solution2;
        } else {
            return solution2;
        }
    }

    public static List<Vehicle> compareSolutions(List<List<Vehicle>> solutions) {
        if(solutions.size() > 1) {
            List<Vehicle> bestSol = solutions.get(0);
            for(int i=1;i<solutions.size();i++)
                bestSol = compareSolution(bestSol, solutions.get(i));
            return bestSol;
        }
        return solutions.get(0);
    }

    public static List<Vehicle> cloneSolution(List<Vehicle> solution) {

        List<Vehicle> clone = new ArrayList(solution.size());
        for (Vehicle route : solution) {
            clone.add(new Vehicle(route));
        }
        return clone;
    }

    public static List<List<Vehicle>> LS_shiftOperator(List<Vehicle> solution) {

        List<List<Vehicle>> solutions = new ArrayList();

        if (solution.size() < 2) {      //less than 2 route (no need to improve)
            solutions.add(solution);
            return solutions; // nothing to improve
        }
        
        //List<Vehicle> newSol = cloneSolution(solution);
        int i = 0, j = 0;   //route index
        for (i = 0; i <solution.size(); i++) {
            for (j = 0; j<solution.size(); j++) {
                if (i == j) continue;   //selected routes cannot be the same

                List<Vehicle> newSol = cloneSolution(solution);
                Vehicle routeI = newSol.get(i); //get selected routes to improve
                Vehicle routeJ = newSol.get(j);

                //try shifting route from i to j
                for (int t = 1; t < routeI.visited.size() - 1; t++) { //loop through the customer locations

                    VisitingLocation loc = routeI.visited.get(t);
                    int pos = routeJ.canInsert(loc);    //can shift
                    if (pos != 0) {
                        
                       
                        //routeI = newSol.get(i); //get selected routes to improve
                        //routeJ = newSol.get(j);
                        routeI.removeVisitingLocation(loc); //do the shift
                        routeJ.insert(loc, pos);
                        if(routeJ.isViolateConstraint() || routeI.isViolateConstraint() || 
                                newSol != compareSolution(solution, newSol)) {
                            routeI.insert(loc, t);  //switch back
                            routeJ.removeVisitingLocation(loc);
                        } else {
                            if(routeI.visited.size() == 2) newSol.remove(routeI);   //if route has no more customer to serve
                            solutions.add(newSol);
                        }
                    }
                }
            }
        }

//        List<Vehicle> bestSol = solution;
//        for(List<Vehicle> sol : solutions) {
//            bestSol = compareSolution (bestSol, sol);
//        }

        return solutions;   //return neighnourboods
    }
}
