
public class CmdAddProduct implements Command {
	@Override
	public String execute(VendingMachine v, String[] cmdParts) {

		// Get product name and quantity from cmdParts
		String productName = cmdParts[1];
		int quantityToAdd = Integer.parseInt(cmdParts[2]);
		
		// Update product quantity in vending machine
		v.updateProductQuantity(productName, quantityToAdd);

		return "Added " + productName + " " + "for " + Integer.toString(quantityToAdd) + " can(s).";    
	}
}

