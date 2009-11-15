/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utility;

/**
 *
 * @author stanley.2007
 */
import java.util.Comparator;
import eads.Solution;

public class SolutionComparator implements Comparator<Solution>{

	@Override
    public int compare(Solution sol1, Solution sol2) {

        int cost1 = sol1.getTotalDistance();
        int cost2 = sol2.getTotalDistance();

        // first - arrange skill in descending order
        if (cost1 < cost2){
            return -1;
        }
        else if (cost1 > cost2) {
            return +1;
        }else{
            return 0;
        }
    }
}
