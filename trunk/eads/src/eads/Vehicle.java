package eads;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    public static int Capacity;    //all capacity is the same

    private int AvailCapcity;
    private double speed = 1;
    private double DistanceTravel;
    private double TimeElapsed;
    VisitingLocation warehouse;
    List<VisitingLocation> visited = new ArrayList();

    //ClientList ServedList;
    Vehicle(Location warehouse) {
        this.warehouse = new VisitingLocation(warehouse);
        this.warehouse.StartServiceTime = warehouse.startTime; //start service time

        this.warehouse.EndServiceTime = warehouse.startTime + warehouse.serviceTime; //start service time

        this.warehouse.TimeSpendTravellingHere = 0; // travel time

        visited.add(this.warehouse);    //First Element Must be Warehouse

        TimeElapsed = 0;
        DistanceTravel = 0;
        AvailCapcity = Capacity;
    }

    //for cloning
    Vehicle(Vehicle copy) {

        AvailCapcity = copy.AvailCapcity;
        speed = copy.speed;
        DistanceTravel = copy.DistanceTravel;
        TimeElapsed = copy.TimeElapsed;

        this.warehouse = new VisitingLocation(copy.warehouse);
        visited.add(this.warehouse);    //First Element Must be Warehouse

        for (int i = 1; i < copy.visited.size() - 1; i++) {
            visited.add(new VisitingLocation(copy.visited.get(i)));
        }
        visited.add(this.warehouse);    //Last Element Must be Warehouse

    }

    void setSpeed(double speed) {
        this.speed = speed;
    }

    void setCapacity(int Capacity) {
        Vehicle.Capacity = Capacity;
    }

    double GetSpeed() {
        return speed;
    }

    int GetCapacity() {
        return Capacity;
    }

    int GetAvailbleCapacity() {
        return AvailCapcity;
    }

    double GetDistanceTravel() {
        return DistanceTravel;
    }

    double GetTimeElapsed() {
        return TimeElapsed;
    }

    VisitingLocation getLastVisited() {
        return visited.get(visited.size() - 1);
    }

    void recomputeDistance() {
        double dist = 0;
        for (int i = 1; i < visited.size(); i++) {
            dist += Main.DistanceMatrix[visited.get(i - 1).locationIndex][visited.get(i).locationIndex];
        }
        DistanceTravel = dist;
    }

    void recomputeTimeElapsed() {
        double timeEL = 0;  //time elapsed

        for (int i = 1; i < visited.size(); i++) {

            VisitingLocation prevCust = visited.get(i - 1);
            VisitingLocation cust = visited.get(i);

            double travelTime = Main.DistanceMatrix[prevCust.locationIndex][cust.locationIndex] / speed;

            if (cust != warehouse) {
                cust.StartServiceTime = Math.max(timeEL + travelTime, cust.startTime);
            }
            cust.EndServiceTime = cust.StartServiceTime + cust.serviceTime;         //departure time

            cust.TimeSpendTravellingHere = travelTime;

            timeEL += cust.EndServiceTime;
        }
        TimeElapsed = timeEL;
    }

    int GetCustomerServed() {
        int warehouseDoubleCount = 0;
        if (visited.get(0) == warehouse) //should be true
        {
            warehouseDoubleCount++;
        }
        if (visited.get(visited.size() - 1) == warehouse) //should be true
        {
            warehouseDoubleCount++;
        }
        return visited.size() - warehouseDoubleCount;
    }

    void BackToWarehouse() {

        if (getLastVisited() != warehouse) {

            double newDistTravel = Main.DistanceMatrix[getLastVisited().locationIndex][warehouse.locationIndex];
            DistanceTravel += newDistTravel;
            TimeElapsed += newDistTravel / speed; //update time elapsed

            visited.add(warehouse);
        //warehouse.StartServiceTime = TimeElapsed;
        //warehouse.EndServiceTime = TimeElapsed;
        //warehouse.TimeSpendTravellingHere = newDistTravel / speed;
        }
    }

    void removeVisitingLocation(VisitingLocation customerToShift) {
        this.visited.remove(customerToShift);
        recomputeDistance();
        recomputeTimeElapsed();
    }

    void insert(VisitingLocation newCustomer, int visitingPosition) {
        this.visited.add(visitingPosition, newCustomer);
        recomputeDistance();
        recomputeTimeElapsed();
    }

    void Insert(Location newCustomer) {

        VisitingLocation cust = new VisitingLocation(newCustomer);

        AvailCapcity -= cust.demand;
        DistanceTravel += Main.DistanceMatrix[getLastVisited().locationIndex][cust.locationIndex];

        double travelTime = Main.DistanceMatrix[getLastVisited().locationIndex][cust.locationIndex] / speed;
        cust.StartServiceTime = Math.max(TimeElapsed + travelTime, cust.startTime);
        cust.EndServiceTime = cust.StartServiceTime + cust.serviceTime;         //departure time

        cust.TimeSpendTravellingHere = travelTime;

        TimeElapsed = cust.EndServiceTime;
        visited.add(cust);
    }

    int canInsert(Location newCustomer) {

        if (AvailCapcity < newCustomer.demand) { //cannot be done

            return 0;
        }

        //determine inserting point (by looking at time serving)
        for (int i = 1; i < visited.size(); i++) {
            VisitingLocation preCust = visited.get(i - 1);
            VisitingLocation cust = visited.get(i);

            double preLocDepartureTime = preCust.EndServiceTime;
            double custServiceTime = cust.StartServiceTime;
            double travelTimeToNewCust = Main.DistanceMatrix[preCust.locationIndex][newCustomer.locationIndex] / speed;

            if (preLocDepartureTime > newCustomer.startTime && newCustomer.startTime < custServiceTime) {
                //check time window constaint (time over)
                if (preLocDepartureTime + travelTimeToNewCust > newCustomer.endTime) { //cannot be done

                    return 0;
                }

                double newCustDoneTime = Math.max(preLocDepartureTime + travelTimeToNewCust, newCustomer.startTime) +
                        newCustomer.serviceTime;
                double travelTimeFromNewToCust = Main.DistanceMatrix[newCustomer.locationIndex][cust.locationIndex] / speed;
                if (newCustDoneTime + travelTimeFromNewToCust > cust.endTime) { //new customer location added will cause problem in other window

                    return 0;
                }
                return i;
            }
        }
        return 0;
    }

    boolean hasCapacityForNewCustomerDemand(Location newCustomer) {

        if (AvailCapcity < newCustomer.demand) //check capacity constraint violation
        {
            return false;// Cannot be done

        }
        return true;
    }

    boolean isViolateConstraint() {

        double timeEL = 0;  //time elapsed

        int demands = 0;

        VisitingLocation cust = null;
        for (int i = 1; i < visited.size() - 1; i++) {

            VisitingLocation prevCust = visited.get(i - 1);
            cust = visited.get(i);

            double travelTime = Main.DistanceMatrix[prevCust.locationIndex][cust.locationIndex] / speed;

            double earlistServiceTime = Math.max(timeEL + travelTime, cust.startTime);
            double departureTime = earlistServiceTime + cust.serviceTime;         //departure time

            if (earlistServiceTime > cust.endTime) {   //time window violated
                //System.out.println("time window: " + earlistServiceTime + " : " + cust.endTime);

                return true;
            }

            timeEL = departureTime;
            demands += cust.demand;
        }

        //time window of returning to warehouse is violated
        if ((timeEL + Main.DistanceMatrix[cust.locationIndex][warehouse.locationIndex] / speed) > warehouse.endTime) {
            //System.out.println("time window: " + (timeEL + Test.DistanceMatrix[cust.locationIndex][warehouse.locationIndex] / speed) +
            //        " : " + warehouse.endTime);
            return true;
        }

        if (demands > Capacity) {
            //System.out.println("capacity: " + demands);
            return true;
        }

        return false;
    }

    boolean isViolateConstraintIfCustomerAddedAtEnd(Location NewClient) {
        int i = 0;          //vehicle position

        int j = 0;          //new customer position

        double ServiceTime = 0;

        if (visited.size() != 1) {
            i = visited.get(visited.size() - 1).locationIndex;
        }
        j = NewClient.locationIndex;

        if (!hasCapacityForNewCustomerDemand(NewClient)) { //check capacity constraint violation

            return true;// Cannot be done

        }

        // check if when vehicle reached location, time window is over
        if (Main.DistanceMatrix[i][j] / speed + TimeElapsed > NewClient.endTime) {
            return true; //Cannot be done

        } // when vehicle reached location, time window is yet to start, need to wait
        else if (Main.DistanceMatrix[i][j] / speed + TimeElapsed < NewClient.startTime) {

            ServiceTime = NewClient.startTime;      //earliest time that vehicle can serve

            //check if get back to warehouse will exceed the warehouse's time window
            if (ServiceTime + NewClient.serviceTime + Main.DistanceMatrix[j][0] / speed > warehouse.endTime) {
                return true;// cannot be done

            } else {
                return false;// Can be done

            }
        } // time window already started whe vehicle arrives
        else if (Main.DistanceMatrix[i][j] / speed + TimeElapsed >= NewClient.startTime) {   //check if get back to warehouse will exceed the warehouse's time window

            ServiceTime = Main.DistanceMatrix[i][j] / speed + TimeElapsed;

            //check if get back to warehouse will exceed the warehouse's time window
            if (ServiceTime + NewClient.serviceTime + Main.DistanceMatrix[j][0] / speed > warehouse.endTime) {
                return true;// cannot be done

            } else {
                return false;// Can be done

            }
        }
        throw new IllegalStateException();  //should never reach here

    }

    double computeWeightedTimeDistanceCost(
            double distCoefficient, double timeDiffCoefficient, double urgencyCoefficient,
            Location pervCust, Location cust) {

        int i = pervCust.locationIndex;
        int j = cust.locationIndex;
        //visited.get(visited.size()-1)
        double travelTime = Main.DistanceMatrix[i][j] / speed;

        //time unit difference between earliest service time and now
        double Tij = Math.max(TimeElapsed + travelTime, cust.startTime) - TimeElapsed;

        //time unit difference between window end time and service time
        double Vij = cust.endTime - TimeElapsed - travelTime;
        double cost = distCoefficient * Main.DistanceMatrix[i][j] +
                timeDiffCoefficient * Tij +
                urgencyCoefficient * Vij;

        return cost;
    }
}
