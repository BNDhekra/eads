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
        if (sequenceOfService.size() > 0) {
            totalDistanceTravelled = 0;
            int previousLocation = -1;
            for (int i = 0; i < sequenceOfService.size(); i++) {
                if (sequenceOfService.get(i).getCurrentLocation() != -2) {
                    previousLocation = (previousLocation == -1) ? startLocation : previousLocation;
                    totalDistanceTravelled += FileLoader.DistanceMatrix[previousLocation][sequenceOfService.get(i).getCurrentLocation()];
                    previousLocation = sequenceOfService.get(i).getCurrentLocation();
                }
            }
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
        this.hasTakenBreak =false;
        this.currentLocation = this.startLocation;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return "Worker [skillNumber=" + skillNumber + ", workStartTime=" +
                workStartTime + ", workEndTime=" + workEndTime + ", earliestBreakTime=" +
                earliestBreakTime + ", latestBreakTime=" + latestBreakTime + ", breakDuration=" +
                breakDuration + ", currentLocation=" + currentLocation + "]";
    }

    public boolean isViolateConstraintIfServiceAddedAtEnd(Service service) {
        int distance = FileLoader.DistanceMatrix[currentLocation][service.getCurrentLocation()];
        int realStartTime = Math.max(currentTime,service.getEarliestStartTime());
        // if there is mismatch of skills
        if (skillNumber != service.getRequiredSkill()) {
            return true;
        } // if the worker can finish the service before his latestBreakTime
        else if ( realStartTime+ distance + service.getDuration() > latestBreakTime && !hasTakenBreak) {
            return true;
        } // if the worker can reach on time before the latest service time
        else if (realStartTime + distance > service.getLatestStartTime()) {
            return true;
        } // if the service is out of the worker work time
        else if (realStartTime + distance + service.getDuration() > workEndTime) {
            return true;
        } // if nothing wrong 
        else {
            return false;
        }
    }

    public void addService(Service newService) {
        if (newService.getCurrentLocation() != -2) {
            sequenceOfService.add(newService);
            newService.setServiced(true);
            int distance = FileLoader.DistanceMatrix[newService.getCurrentLocation()][currentLocation];
            currentTime = ((currentTime + distance) > newService.getEarliestStartTime())?
                currentTime + distance + newService.getDuration(): newService.getEarliestStartTime() + newService.getDuration();
            currentLocation = newService.getCurrentLocation();
        } else { // the service is to take a break
            currentTime = (currentTime < earliestBreakTime)? earliestBreakTime: currentTime;
            currentTime += breakDuration;
            hasTakenBreak =true;
            sequenceOfService.add(newService);
        }
    }

    public String printServiceSequence() {
        String sequence = String.valueOf(startLocation);
        for (int i = 0; i < sequenceOfService.size(); i++) {
            int tempLocation = sequenceOfService.get(i).getCurrentLocation();
            sequence += (tempLocation != -2)? " -> " + tempLocation : " -> break" ;
        }
        return sequence;
    }

    public double computeDistanceCost(double distCoefficient, double timeDiffCoefficient,
            double urgencyCoefficient, Service service){

        //visited.get(visited.size()-1)
        double travelTime = FileLoader.DistanceMatrix[service.getCurrentLocation()][currentLocation];

        //time unit difference between earliest service time and now
        double Tij = Math.max(currentTime + travelTime, service.getEarliestStartTime()) - currentTime;

        //time unit difference between window end time and service time
        double Vij = service.getLatestStartTime() - currentTime - travelTime;
        double cost = distCoefficient * travelTime+
                timeDiffCoefficient * Tij +
                urgencyCoefficient * Vij;
        return cost;
    }
}
