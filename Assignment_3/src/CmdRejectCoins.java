import java.util.ArrayList;
import java.util.Collections;

public class CmdRejectCoins implements Command {
	@Override
	public String execute(VendingMachine v, String[] cmdParts) {
		
		// Reject The Coins
		int totalCoins = v.getTotalInsertedCoins();
		if (totalCoins == 0) {
			return "Rejected no coin!";
		} 
		else {
			
			ArrayList<Integer> rejectedCoins = v.getInsertedCoins();
			Collections.sort(rejectedCoins);
			Collections.reverse(rejectedCoins);
		
			String coinsStr = "";
			for (Integer coin : rejectedCoins) {
				coinsStr += "$" + Integer.toString(coin) + ", ";
			}

			// Remove last comma and space
			coinsStr = coinsStr.substring(0, coinsStr.length() - 2);
			// Add period at the end
			coinsStr += ".";

			v.rejectAllInsertedCoins();


			return "Rejected " + coinsStr + " " + "$" + Integer.toString(totalCoins) + " in total.";
		}
	}
}
