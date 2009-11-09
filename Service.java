package eads;

public class Service {

    private int earliestStartTime;
    private int latestStartTime;
    private int duration;
    private int requiredSkill;
    private int currentLocation;
    private boolean serviced = false;

    /**
     * @return the earliestStartTime
     */
    public int getEarliestStartTime() {
        return earliestStartTime;
    }

    /**
     * @param earliestStartTime the earliestStartTime to set
     */
    public void setEarliestStartTime(int earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
    }

    /**
     * @return the latestStartTime
     */
    public int getLatestStartTime() {
        return latestStartTime;
    }

    /**
     * @param latestStartTime the latestStartTime to set
     */
    public void setLatestStartTime(int latestStartTime) {
        this.latestStartTime = latestStartTime;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the requiredSkill
     */
    public int getRequiredSkill() {
        return requiredSkill;
    }

    /**
     * @param requiredSkill the requiredSkill to set
     */
    public void setRequiredSkill(int requiredSkill) {
        this.requiredSkill = requiredSkill;
    }

    /**
     * @param currentLocation the currentLocation to set
     */
    public void setCurrentLocation(int currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * @return the currentLocation
     */
    public int getCurrentLocation() {
        return currentLocation;
    }

    /**
     * @param serviced the serviced to set
     */
    public void setServiced(boolean serviced) {
        this.serviced = serviced;
    }

    /**
     * @return the serviced status
     */
    public boolean isServiced() {
        return serviced;
    }

    /**
     * @param earliestStartTime
     * @param latestStartTime
     * @param duration
     * @param requiredSkill
     * @param currentLocation
     */
    public Service(int earliestStartTime, int latestStartTime, int duration,
            int requiredSkill, int currentLocation) {
        this.earliestStartTime = earliestStartTime;
        this.latestStartTime = latestStartTime;
        this.duration = duration;
        this.requiredSkill = requiredSkill;
        this.currentLocation = currentLocation;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return "Service [earliestStartTime=" + earliestStartTime +
                ", latestStartTime=" + latestStartTime +
                ", duration=" + duration +
                ", requiredSkill=" + requiredSkill +
                ", currentLocation=" + currentLocation + "]";
    }
}
