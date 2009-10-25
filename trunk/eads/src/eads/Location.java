package eads;


public class Location implements Comparable<Location> {

	public int locationIndex;       //location index of 0 implies warehouse / depot
	
	public int startTime;
	public int endTime;
	public int serviceTime;
	public int demand;

    public int x;
	public int y;

    public boolean isVisited = false;

    public Location(int locationIndex, int startTime, int endTime, int serviceTime, int demand, int x, int y) {

        this.locationIndex = locationIndex;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceTime = serviceTime;
        this.demand = demand;
        this.x = x;
        this.y = y;
    }



    /*
	 * Compares two Location objects.
	 * Rules:
	 * 1. If _this_ location's ReadyTime is later than loc2's ReadyTime,
	 *    this customer is "bigger" than loc2 and is put later.
	 * 2. If the ReadyTime of this and loc2 is the same, we compare their
	 *    demand. If this.demand is smaller than loc2.Demand, this customer
	 *    is "bigger" than loc2 and is put later.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Location loc2)
	{
		int diffReady = this.endTime - loc2.endTime;
		if (diffReady != 0) return diffReady;
		else
		{
			int diffDue = this.demand - loc2.demand;
			return diffDue;
		}
	}	
	
}

