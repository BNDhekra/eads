/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eads;

import java.util.ArrayList;

/**
 *
 * @author stanley.2007
 */
public class Solution {

    private ArrayList<ArrayList<Service>> locationSequence = new ArrayList<ArrayList<Service>>();
    private ArrayList<ArrayList<Worker>> workerList = new ArrayList<ArrayList<Worker>>();
    private boolean feasible = true;
    private int totalDistance;

    public Solution(ArrayList<ArrayList<Service>> locationSequence){
        this.locationSequence = locationSequence;
    }

    public boolean isFeasible() {
        return feasible;
    }

    public void setFeasible(boolean feasible) {
        this.feasible = feasible;
    }

    public ArrayList<ArrayList<Worker>> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(ArrayList<ArrayList<Worker>> workerList) {
        this.workerList = workerList;
        this.totalDistance = 0;
        for(ArrayList<Worker> tempWorkerList : workerList){
            for(int i = 0; i< tempWorkerList.size(); i++){
                this.totalDistance += tempWorkerList.get(i).getTotalDistanceTravelled();
            }
        }
    }

    public int countLocation(){
        int i= 0;
        for (ArrayList<Service> tempList : locationSequence) {
            for(Service eachService : tempList){
                i++;
            }
        }
        return i;
    }

    public int totalCountService(){
        int totalCount = 0;
        for (ArrayList<Worker> tempList : workerList) {
            for (Worker eachWorker :tempList){
                totalCount += eachWorker.countService();
            }
        }
        return totalCount;
    }
    
    public ArrayList getLocationSequence() {
        return locationSequence;
    }

    public void setLocationSequence(ArrayList<ArrayList<Service>> locationSequence) {
        this.locationSequence = locationSequence;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }
    
}
