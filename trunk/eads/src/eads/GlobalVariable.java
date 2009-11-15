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
public class GlobalVariable {

    protected static int m = 0; // number of medical professionals
    protected static int nc = 0; // number of skills set
    protected static int n = 0; // number of patients
    protected static ArrayList<ArrayList<Worker>> careWorkers = new ArrayList<ArrayList<Worker>>(); // list of workers
    protected static ArrayList<ArrayList<Service>> services = new ArrayList<ArrayList<Service>>(); // list of servicess
    protected static int DistanceMatrix[][];

}
