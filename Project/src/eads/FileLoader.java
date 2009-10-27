package eads;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileLoader {
	
	BufferedReader br;
	String fileName;
	
	public static void main(String [] args) {
		
		new FileLoader().run();
	}
	
	public boolean run() {
		fileName = "data\\dataset";
		System.out.println("Read fileName is: " +fileName);
		return parse(fileName);
	}
	
	private boolean parse(String fileName) {
		try {
			br = new BufferedReader(new FileReader(fileName));
			String s;
			
			boolean initDone = false;
			int m = 0;
			int nc = 0;
			int n = 0;
			
			boolean initWorkers = false;
			ArrayList<Worker> careWorkers = new ArrayList<Worker>();
			int noOfWorkers = 0;
			
			boolean initLocations = false;
			ArrayList<Location> locations = new ArrayList<Location>();
			int noOfLocations = 0;
			
			boolean initService = false;
			ArrayList<Service> services = new ArrayList<Service>();
			int noOfServices = 0;
			
			while ((s = br.readLine()) != null) {
				s = s.trim();
				if (s.equals("")) {
					continue;
				}
				
				
				if (!initDone) {
					if (s.startsWith("m=")) {
						String [] temp = s.split("=");
						m = Integer.parseInt(temp[1]);
						System.out.println("M is " +m);
					}
					
					if (s.startsWith("nc=")) {
						String [] temp = s.split("=");
						nc = Integer.parseInt(temp[1]);
						System.out.println("NC is " +nc);
					}
					
					if (s.startsWith("n=")) {
						String [] temp = s.split("=");
						n = Integer.parseInt(temp[1]);
						System.out.println("N is " +n);
						initDone = true;
						br.readLine();
					}
				} else if (!initWorkers) {
					String [] temp = s.split("\t");
					int skillNumber = Integer.parseInt(temp[1]);
					int workStartTime = Integer.parseInt(temp[2]);
					int workEndTime = Integer.parseInt(temp[3]);
					int earliestBreakTime = Integer.parseInt(temp[4]);
					int latestBreakTime = Integer.parseInt(temp[5]);
					int breakDuration = Integer.parseInt(temp[6]);
					Worker w = new Worker(skillNumber, workStartTime, workEndTime, earliestBreakTime, latestBreakTime, breakDuration);
					careWorkers.add(w);
					System.out.println(w.toString());
					noOfWorkers++;
					if (noOfWorkers == m) {
						initWorkers = true;
						br.readLine();
					}
				} else if (!initLocations) {
					String [] temp = s.split("\t"); 
					ArrayList<Integer> e = new ArrayList<Integer>(n+m);
					for (int j = 0; j < temp.length; j++) {
						e.add(Integer.parseInt(temp[j]));
					}
					Location loc = new Location(e);
					locations.add(loc);
					System.out.println(loc.toString());
					noOfLocations++;
					if (noOfLocations == (n+m)) {
						initLocations = true;
						br.readLine();
					}
				} else if (!initService) {
					String [] temp = s.split("\t");
					int earliestStartTime = Integer.parseInt(temp[0]);
					int latestStartTime = Integer.parseInt(temp[1]);
					int duration = Integer.parseInt(temp[2]);
					int requiredSkill = Integer.parseInt(temp[3]);
					Service ser = new Service(earliestStartTime, latestStartTime, duration, requiredSkill);
					services.add(ser);
					System.out.println(ser.toString());
					noOfServices++;
					if (noOfServices == (n)) {
						initService = true;
					}
				} else {
					System.out.println("DONE");
				}
				
				System.out.println(s);
			}
			br.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
            return false;
		}
	}
}
