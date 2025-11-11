
public class CmdCheckProductInfo implements Command {
	@Override
	public String execute(VendingMachine v, String[] cmdParts) {
		// Check Product Information
		String productName = cmdParts[1];
		int productPrice = v.getProduct(productName).getPrice();
		int productQuantity = v.getProduct(productName).getQuantity();

		return productName + ": Price = " + productPrice + ", Quantity = " + productQuantity;
	}
}
