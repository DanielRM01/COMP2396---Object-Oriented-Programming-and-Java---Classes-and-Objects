import java.io.*;

public class Question1 {
	
	static class OctopusCard {
		int balance;
	}
	
	static class Route {
		String name;
		String company_code;
		int full_fare;
	}
	
	static class FirstTrip extends Route{
	}
	
	static class SecondTrip extends Route {
	}
	
	public static int remainingBalance(BufferedReader reader) throws IOException {		
		// Read the first line of input: Octopus Card Balance after 1st Trip
		String balanceLine = reader.readLine();
		
		// Read the second line of input: 1st Trip information
		String firstTripLine = reader.readLine();
		String[] firstTripParts = firstTripLine.split(" "); // Split on spaces
		
		// Read the third line of input: 2nd Trip information
		String secondTripLine = reader.readLine();
		String[] secondTripParts = secondTripLine.split(" "); // Split on spaces
		
		// Create Octopus Card
		OctopusCard o = new OctopusCard();
		o.balance = Integer.parseInt(balanceLine);
		
		// Create 1st trip object
		FirstTrip ft = new FirstTrip();
		ft.name = firstTripParts[0];
		ft.company_code = firstTripParts[1];
		ft.full_fare = Integer.parseInt(firstTripParts[2]);

		// Create 2nd trip object
		SecondTrip st = new SecondTrip();
		st.name = secondTripParts[0];
		st.company_code = secondTripParts[1];
		st.full_fare = Integer.parseInt(secondTripParts[2]);
		
		
		// Calculate the remaining balance
		boolean discountApply = true;
		
		// Evaluate whether the discounts can apply
		if (!ft.company_code.equals("BNB")) {
			discountApply = false;
		}
		else if (st.name.charAt(0) == 'A') {
			discountApply = false;
		}
		else if (o.balance < 1) {
			discountApply = false;
		}
		
		// Discounts can apply
		if (discountApply) {
			// If the station of the 1st trip does NOT start with 'P' the possible difference between 
			// the 1st and 2nd trip maybe has to be charged
			if (ft.name.charAt(0) != 'P') {
				if (ft.full_fare < st.full_fare) {
					// Octopus Card is charged with the difference price between 1st and 2nd trip
					 o.balance -= (st.full_fare - ft.full_fare); 
			}
			// If the station of the 1st trip starts with 'P' the 2nd trip is not charged
			}
		}
		// No discount is applied
		else {
			// Does the passenger have to pay in cash?
			if (o.balance < 1) {
				// The passenger pays in cash
				;
			}
			else {
				// Passenger pays for the 2nd trip
				o.balance -= st.full_fare;
			}
		}
        return o.balance;
    }

	
	public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        int result = remainingBalance(reader);
        System.out.println("The remaining balance is " + result + ".");
	}
}