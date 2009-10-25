package eads;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chew Boon Heng
 */
public class VisitingLocation extends Location {

    public double StartServiceTime = 0;
    public double EndServiceTime = 0;
    public double TimeSpendTravellingHere = 0; //weight of edge connecting to this location

    public VisitingLocation(int locationIndex, int startTime, int endTime, int serviceTime, int demand, int x, int y) {
        super(locationIndex, startTime, endTime, serviceTime, demand, x, y);
    }

    public VisitingLocation(Location loc) {
        super(loc.locationIndex, loc.startTime, loc.endTime, loc.serviceTime, loc.demand, loc.x, loc.y);
    }

    //for cloning
    public VisitingLocation(VisitingLocation loc) {
        super(loc.locationIndex, loc.startTime, loc.endTime, loc.serviceTime, loc.demand, loc.x, loc.y);
        StartServiceTime = loc.StartServiceTime;
        EndServiceTime = loc.EndServiceTime;
        TimeSpendTravellingHere = loc.TimeSpendTravellingHere;
    }

}
