package eads;

import java.util.ArrayList;

public class Location {
	private ArrayList<Integer> location;
	
	/**
	 * @return the location
	 */
	public ArrayList<Integer> getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(ArrayList<Integer> location) {
		this.location = location;
	}

	/**
	 * @param location
	 */
	public Location(ArrayList<Integer> location) {
		super();
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location [location=" + location + "]";
	}
	
}
