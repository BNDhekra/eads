package eads;
import java.util.*;
import java.io.*;
import javax.swing.JFileChooser;

public class InputManager {

    BufferedReader br;
    int NoOfAttribute = 7;

    String filename = null;
    String caseId = null;
    int[] vehicle = new int[2];     //number, capacity
    List<int[]> customerLocations;

    public InputManager() {}

    public String getCaseId() {
        return caseId;
    }

    public String getFilename() {
        return filename;
    }

    public int getVehicleLimit() {
        return vehicle[0];
    }

    public int getVehicleCapacity() {
        return vehicle[1];
    }

    public int[] getCustomerLocations(int index) {

        int[] loc = customerLocations.get(index);

        if(loc[0] == index)
            return loc;
        else {
            for(int[] l : customerLocations) {
                if(l[0] == index) return l;
            }
            return null;
        }
    }

    public int getTotalCustomerLocations() {
        return customerLocations.size();
    }

    public boolean run() {

        this.filename = GraphUI.getInputFile();
        //filename = "C:\\Users\\Chew Boon Heng\\Desktop\\VRPTW2\\VRPTW2\\solomon_100\\c103.txt";
        System.out.println("Read in filename: " + filename);
        return parse(filename);
    }

    private boolean parse(String filename) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

            caseId = br.readLine();

            String s;
            String strVehicleLineCheck = "VEHICLE", strCustomerLineCheck = "CUSTOMER";

            boolean isVehicle = false;
            boolean isCustomer = false;
            customerLocations = new ArrayList();

            while ((s = br.readLine()) != null) {
                s = s.trim();		// trim all the spaces before and behind
                if (s.equals("")) {
                    continue;
                } // if it is a empty line, skip

                if (isVehicle) {    //read data
                    String[] fields = s.split(" ");
                    int count = 0;
                    for (int i = 0; i < fields.length; i++) {
                        if (!fields[i].equals("")) {
                            vehicle[count++] = Integer.parseInt(fields[i]);
                        }
                        if (count == 2) {
                            isVehicle = false;
                            break;
                        }
                    }
                }

                if (isCustomer) {   //read data
                    String[] fields = s.split(" ");
                    int count = 0;
                    int[] location = new int[NoOfAttribute];
                    for (int i = 0; i < fields.length; i++) {
                        if (!fields[i].equals("")) {
                            location[count++] = Integer.parseInt(fields[i]);
                        }
                        if (count == NoOfAttribute) {
                            customerLocations.add(location);
                            break;
                        }
                    }
                }

                if (s.startsWith(strVehicleLineCheck)) {
                    isVehicle = true;
                    br.readLine();  //skip column names
                    continue;
                }

                if (s.startsWith(strCustomerLineCheck)) {
                    isCustomer = true;
                    br.readLine();  //skip column names
                    continue;
                }

            }
            br.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
	

	