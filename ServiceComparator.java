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
import eads.Service;

public class ServiceComparator implements Comparator<Service>{

	@Override
    public int compare(Service serv1, Service serv2) {

        int skill1 = serv1.getRequiredSkill();
        int skill2 = serv2.getRequiredSkill();

        int start1 = serv1.getEarliestStartTime();
        int start2 = serv2.getEarliestStartTime();

        // first - arrange skill in ascending order
        if (skill1 < skill2){
            return -1;
        }
        else if (skill1 > skill2) {
            return +1;
        }

        // second - arrange earliest start time in ascending order
        else if (start1 > start2){
            return +1;
        }else if (start1 < start2){
            return -1;
        }else{
            return 0;
        }
    }
}
