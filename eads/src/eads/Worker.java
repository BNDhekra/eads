package eads;

import java.util.ArrayList;

public class Worker {

    private int skillNumber;
    private int workStartTime;
    private int workEndTime;
    private int earliestBreakTime;
    private int latestBreakTime;
    private int breakDuration;
    private int currentLocation;
    private int startLocation;
    private int currentTime;
    private boolean hasTakenBreak;
    private ArrayList<Service> sequenceOfService;
    private int totalDistanceTravelled;

    /**
     * @param skillNumber the skillNumber to set
     */
    public void setSkillNumber(int skillNumber) {
        this.skillNumber = skillNumber;
    }

    /**
     * @return the skillNumber
     */
    public int getSkillNumber() {
        return skillNumber;
    }

    /**
     * @param workStartTime the workStartTime to set
     */
    public void setWorkStartTime(int workStartTime) {
        this.workStartTime = workStartTime;
    }

    /**
     * @return the workStartTime
     */
    public int getWorkStartTime() {
        return workStartTime;
    }

    /**
     * @param workEndTime the workEndTime to set
     */
    public void setWorkEndTime(int workEndTime) {
        this.workEndTime = workEndTime;
    }

    /**
     * @return the workEndTime
     */
    public int getWorkEndTime() {
        return workEndTime;
    }

    /**
     * @param earliestBreakTime the earliestBreakTime to set
     */
    public void setEarliestBreakTime(int earliestBreakTime) {
        this.earliestBreakTime = earliestBreakTime;
    }

    /**
     * @return the earliestBreakTime
     */
    public int getEarliestBreakTime() {
        return earliestBreakTime;
    }

    /**
     * @param latestBreakTime the latestBreakTime to set
     */
    public void setLatestBreakTime(int latestBreakTime) {
        this.latestBreakTime = latestBreakTime;
    }

    /**
     * @return the latestBreakTime
     */
    public int getLatestBreakTime() {
        return latestBreakTime;
    }

    /**
     * @param breakDuration the breakDuration to set
     */
    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    /**
     * @return the breakDuration
     */
    public int getBreakDuration() {
        return breakDuration;
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
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @return the currentTime
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * @param hasTakenBreak the hasTakenBreak to set
     */
    public void setHasTakenBreak(boolean hasTakenBreak) {
        this.hasTakenBreak = hasTakenBreak;
    }

    /**
     * @return the boolean hasTakenBreak
     */
    public boolean isHasTakenBreak() {
        return hasTakenBreak;
    }

    public int getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(int startLocation) {
        this.startLocation = startLocation;
    }

    public ArrayList<Service> getSequenceOfService() {
        return sequenceOfService;
    }

    public void setSequenceOfService(ArrayList<Service> sequenceOfService) {
        this.sequenceOfService = sequenceOfService;
    }

    public int getTotalDistanceTravelled() {
        int previousLocation = -1;
        if (sequenceOfService.size() > 0) {
            totalDistanceTravelled = 0;
            for (int i = 0; i < sequenceOfService.size(); i++) {
                if (sequenceOfService.get(i).getCurrentLocation() != -2) {
                    previousLocation = (previousLocation == -1) ? startLocation : previousLocation;
                    totalDistanceTravelled += GlobalVariable.DistanceMatrix[previousLocation][sequenceOfService.get(i).getCurrentLocation()];
                    previousLocation = sequenceOfService.get(i).getCurrentLocation();
                }
            }
        }
        if (previousLocation != -1) {
            // return back to worker's starting location
            totalDistanceTravelled += GlobalVariable.DistanceMatrix[previousLocation][startLocation];
        }
        return totalDistanceTravelled;
    }

    /**
     * @param skillNumber
     * @param workStartTime
     * @param workEndTime
     * @param earliestBreakTime
     * @param latestBreakTime
     * @param breakDuration
     * @param currentLocation
     */
    public Worker(int skillNumber, int workStartTime, int workEndTime,
            int earliestBreakTime, int latestBreakTime, int breakDuration, int startLocation) {
        this.skillNumber = skillNumber;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.earliestBreakTime = earliestBreakTime;
        this.latestBreakTime = latestBreakTime;
        this.breakDuration = breakDuration;
        this.startLocation = startLocation;
        this.sequenceOfService = new ArrayList<Service>();
        this.totalDistanceTravelled = 0;
        this.hasTakenBreak = false;
        this.currentLocation = this.startLocation;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    public Worker(Worker clone) {
        this.skillNumber = clone.skillNumber;
        this.workStartTime = clone.workStartTime;
        this.workEndTime = clone.workEndTime;
        this.earliestBreakTime = clone.earliestBreakTime;
        this.latestBreakTime = clone.latestBreakTime;
        this.breakDuration = clone.breakDuration;
        this.startLocation = clone.startLocation;
        this.sequenceOfService = new ArrayList<Service>();
        this.totalDistanceTravelled = 0;
        this.hasTakenBreak = false;
        this.currentLocation = clone.startLocation;
    }

    @Override
    public String toString() {
        return "Worker [skillNumber=" + skillNumber + ", workStartTime=" +
                workStartTime + ", workEndTime=" + workEndTime + ", earliestBreakTime=" +
                earliestBreakTime + ", latestBreakTime=" + latestBreakTime + ", breakDuration=" +
                breakDuration + ", currentLocation=" + currentLocation + "]";
    }

    public boolean isViolateConstraintIfServiceAddedAtEnd(Service service) {
        int distance = GlobalVariable.DistanceMatrix[currentLocation][service.getCurrentLocation()];
        int arrivalTime = currentTime + distance;
        int realStartTime = Math.max(arrivalTime, service.getEarliestStartTime()); // choose earlieststarttime if the currentTime + distance is less
        int completionTime = arrivalTime + service.getDuration();
        int breakTime = (arrivalTime < earliestBreakTime) ? earliestBreakTime : currentTime + distance;
        int completionTimeWithBreak = breakTime + breakDuration + service.getDuration();

        // if the worker can reach on time before the latest service time
        if (realStartTime > service.getLatestStartTime()) {
            return true;
        }

        // if the service is out of the worker work time
        if (completionTime > workEndTime) {
            return true;
        }

        // if the worker can finish the service before his latestBreakTime
        if (!hasTakenBreak) {
            if (completionTime > latestBreakTime) {
                // if the worker can grab a break before the next service
                if (service.getLatestStartTime() > completionTimeWithBreak) {
                    //if (service.getEarliestStartTime() - breakTime >= breakDuration && !hasTakenBreak) {
                    return true;
                }
            }
        }

        // if service start time is too early for the worker
        if (realStartTime < workStartTime) {
            return true;
        }

        // if nothing wrong
        return false;
    }

    public void addService(Service newService) {
        if (newService.getCurrentLocation() != -2) {
            int distance = GlobalVariable.DistanceMatrix[newService.getCurrentLocation()][currentLocation];
            int arrivalTime = currentTime + distance;
            int completionTime = arrivalTime + newService.getDuration();
            int breakTime = (arrivalTime < earliestBreakTime) ? earliestBreakTime : currentTime + distance;
            int completionTimeWithBreak = breakTime + breakDuration + newService.getDuration();

            // add a break if there can be a delay from currenttime + distance travel > breakDuration
            // after the earliest break time and have yet to take a break.
            if (newService.getLatestStartTime() > completionTimeWithBreak && !hasTakenBreak) {
                //if (newService.getEarliestStartTime() - breakTime >= breakDuration && !hasTakenBreak) 
                currentTime += breakDuration;
                hasTakenBreak = true;
                arrivalTime = currentTime + distance;
                completionTime = arrivalTime + newService.getDuration();
                sequenceOfService.add(new Break());
            }

            sequenceOfService.add(newService);
            currentTime = (arrivalTime > newService.getEarliestStartTime()) ? completionTime : newService.getEarliestStartTime() + newService.getDuration();
            currentLocation = newService.getCurrentLocation();
        } else if(!hasTakenBreak) { // the service is to take a break
            currentTime = (currentTime < earliestBreakTime) ? earliestBreakTime : currentTime;
            currentTime += breakDuration;
            hasTakenBreak = true;
            sequenceOfService.add(newService);
        }
    }

    public String printServiceSequence() {
        String sequence = String.valueOf(startLocation);
        for (int i = 0; i < sequenceOfService.size(); i++) {
            int tempLocation = sequenceOfService.get(i).getCurrentLocation();
            sequence += (tempLocation != -2) ? " -> " + tempLocation : " -> break";
        }
        return sequence + " ( " + sequenceOfService.size() + " )";
    }

    public int countService() {
        int count = 0;
        for (int i = 0; i < sequenceOfService.size(); i++) {
            int tempLocation = sequenceOfService.get(i).getCurrentLocation();
            if (tempLocation != -2) {
                count++;
            }
        }
        return count;
    }
}
