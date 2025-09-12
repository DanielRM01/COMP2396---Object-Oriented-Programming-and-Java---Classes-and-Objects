import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Question2 {
	static class BusTripMetrics {
		int amountOfIntersections;
		int fixedTimeToSwitchRoads;
	}
	
	static class TuenMunRoad {
		ArrayList<Integer> timeToTravelBetweenIntersections = new ArrayList<Integer>();
	}
	
	static class CastlePeakRoad {
		ArrayList<Integer> timeToTravelBetweenIntersections = new ArrayList<Integer>();
	}

	
	
    public static int minimumTime(BufferedReader reader) throws IOException {
    	// Initializing variables
    	BusTripMetrics btm = new BusTripMetrics();
    	TuenMunRoad tmr = new TuenMunRoad();
    	CastlePeakRoad cpr = new CastlePeakRoad();
    	
    	// Read the first line of input: Amount of intersections between Tuen Mun Road and Castle Peak Road
    	btm.amountOfIntersections = Integer.parseInt(reader.readLine());
    	
    	// Read the second line of input: Fixed time to cross the intersections
    	btm.fixedTimeToSwitchRoads = Integer.parseInt(reader.readLine());
    			
    	// Read the third line of input: Time to travel between intersections for Tuen Mun Road
    	String tmrTripLine = reader.readLine();
		String[] tmrTripParts = tmrTripLine.split(" "); // Split on spaces
		for (int i = 0; i <  tmrTripParts.length; i++) {
			tmr.timeToTravelBetweenIntersections.add(Integer.parseInt(tmrTripParts[i]));
		}
    
		// Read the fourth line of input: Time to travel between intersections for Castle Road Peak
    	String cprTripLine = reader.readLine();
		String[] cprTripParts = cprTripLine.split(" "); // Split on spaces
		for (int i = 0; i <  cprTripParts.length; i++) {
			cpr.timeToTravelBetweenIntersections.add(Integer.parseInt(cprTripParts[i]));
		}
    	
		// Declaring dp1[0...amountOfIntersections] and dp2[0...amountOfIntersections] following pseudo
		// code
		List<Integer> dp1 = new ArrayList<>(btm.amountOfIntersections);
		List<Integer> dp2 = new ArrayList<>(btm.amountOfIntersections);
		
		// Base Cases
		dp1.add(0, tmr.timeToTravelBetweenIntersections.get(0));
		dp2.add(0, cpr.timeToTravelBetweenIntersections.get(0));
		
		// Calculate the fastest route
		for (int i = 1; i <= btm.amountOfIntersections; i++) {
			// For Tuen Mun Road 
			int stayOnTuenMun = dp1.get(i-1) + tmr.timeToTravelBetweenIntersections.get(i);
			int switchToTuenMun = dp2.get(i-1) + btm.fixedTimeToSwitchRoads + tmr.timeToTravelBetweenIntersections.get(i);
			dp1.add(i, Math.min(stayOnTuenMun, switchToTuenMun));
			
			// For Castle Peak Road
			int stayOnCastlePeak = dp2.get(i-1) + cpr.timeToTravelBetweenIntersections.get(i);
			int switchToCastlePeak = dp1.get(i-1) + btm.fixedTimeToSwitchRoads + cpr.timeToTravelBetweenIntersections.get(i);
			dp2.add(i, Math.min(stayOnCastlePeak, switchToCastlePeak));
		}
        return Math.min(dp1.getLast(), dp2.getLast());
    }

	public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int result = minimumTime(reader);
        System.out.println("The minimum time needed is " + result + ".");
	}
}