import java.util.ArrayList;

public class CmdPurchase implements Command {
	@Override
	public String execute(VendingMachine v, String[] cmdParts) {
		// Purchase a product
		String productName = cmdParts[1];
		Product product = v.getProduct(productName);

		// Out of stock
		if (product.getQuantity() == 0) {
			return productName + " is out of stock!";
		}

		// Missing credit
		if (v.getTotalInsertedCoins() < product.getPrice()) {
			return "Not enough credit to buy " + productName + "! Inserted $" + Integer.toString(v.getTotalInsertedCoins()) + " but needs $" + Integer.toString(product.getPrice()) + ".";
		}

		// They have paid the exact amount
		if (v.getTotalInsertedCoins() == product.getPrice()) {
			v.updateProductQuantity(productName, -1);
			int paid = v.getTotalInsertedCoins();
			v.rejectAllInsertedCoins();
			return "Dropped " + productName + ". Paid $" + Integer.toString(paid) + ". No change.";
		}

		// Calculate change
		int change = v.getTotalInsertedCoins() - product.getPrice();
		
		String changeStr = "";
		
		// Make Coin Changer to create proper change
		ArrayList<Integer> CoinChanger = new ArrayList<>();
		CoinChanger.add(10);
		CoinChanger.add(5);
		CoinChanger.add(2);
		CoinChanger.add(1);
		
		/*  The vending machine only looks for coins in the Coin Changer, 
		where there is an infinite supply of coins. 
		The system looks for the largest coin, which is not larger than needed and adds it to the change.
*/ 
		while (change > 0) {
			int bestDifferenceToOne = 100;
			int bestCoin = 0;
			for (int coin : CoinChanger) {
				if (change / coin < 1) {
					continue;
				} else {
					if (change / coin < bestDifferenceToOne) {
						bestDifferenceToOne = change / coin;
						bestCoin = coin;
						changeStr += "$" + Integer.toString(bestCoin) + ", ";
						change -= bestCoin;
					}
				}
			}
		}

		v.updateProductQuantity(productName, -1);
		int paid = v.getTotalInsertedCoins();
		v.rejectAllInsertedCoins();
			
		// Remove last comma and space
		changeStr = changeStr.substring(0, changeStr.length() - 2);
		// Add period at the end
		changeStr += ".";

		return "Dropped " + productName + ". Paid $" + Integer.toString(paid) + ". Your change: " + changeStr;




	}
}
