package eads;

public class Service {

    private int earliestStartTime;
    private int latestStartTime;
    private int duration;
    private int requiredSkill;
    private int currentLocation;

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

    public Service(Service clone){
        this.earliestStartTime = clone.earliestStartTime;
        this.latestStartTime = clone.latestStartTime;
        this.duration = clone.duration;
        this.requiredSkill = clone.requiredSkill;
        this.currentLocation = clone.currentLocation;
    }

    @Override
    public String toString() {
        return "Service [" +
                " location=" + currentLocation +
                ", earliest=" + earliestStartTime +
                ", latest=" + latestStartTime +
                ", duration=" + duration +
                ", skill=" + requiredSkill +
                " ]";
    }
}
