import java.util.ArrayList;

public class VendingMachine {
	
	// ArrayList of Integers represents inserted coins in Coin Slot
	private ArrayList<Integer> insertedCoins;
	
	// ArrayList of Product represents inventories of products
	private ArrayList<Product> products;
	
	public VendingMachine() {
		insertedCoins = new ArrayList<Integer>();
		products = new ArrayList<Product>();
	}
	
	public void addProduct(Product p) {
		this.products.add(p);
	}
	
	public void insertCoin(Integer c) {
		this.insertedCoins.add(c);
	}
		
	public void initializeProducts() {
		
		/* Write some code here so that all the products are initialized with quantity 0 with their corresponding price. 
		 * Please refer to Notes 6 in page 5 for the products and their corresponding price. */
		
		// Creating and adding products to the vending machine
		// Cocacola
		Product Cocacola = new Product("Cocacola", 4, 0);
		addProduct(Cocacola);
		// Pepsi
		Product Pepsi = new Product("Pepsi", 5, 0);
		addProduct(Pepsi);
		// Sprite
		Product Sprite = new Product("Sprite", 6, 0);
		addProduct(Sprite);
		// Mirinda
		Product Mirinda = new Product("Mirinda", 7, 0);
		addProduct(Mirinda);
		// Gatorade
		Product Gatorade = new Product("Gatorade", 8, 0);
		addProduct(Gatorade);
		// Bonaqua
		Product Bonaqua = new Product("Bonaqua", 11, 0);
		addProduct(Bonaqua);
		// RedBull
		Product RedBull = new Product("RedBull", 12, 0);
		addProduct(RedBull);
		// Tropicana
		Product Tropicana = new Product("Tropicana", 15, 0);
		addProduct(Tropicana);
		// MinuteMaid
		Product MinuteMaid = new Product("MinuteMaid", 10, 0);
		addProduct(MinuteMaid);

	}

	/* You may add other properties and methods */

	// Get inserted coins
	public ArrayList<Integer> getInsertedCoins() {
		return this.insertedCoins;
	}

	// Get total inserted coins
	public int getTotalInsertedCoins() {
		int total = 0;
		for (Integer coin : insertedCoins) {
			total += coin;
		}
		return total;
	}
	
	// Get product
	public Product getProduct(String productName) {
		for (Product product : products) {
			if (product.getName().equals(productName)) {
				return product;
			}
		}
		return null;
	}


	// Reject all inserted coins
	public void rejectAllInsertedCoins() {
		insertedCoins.clear();
	}

	// Update product quantity
	public void updateProductQuantity(String productName, int quantityToAdd) {
		for (Product product : products) {
			if (product.getName().equals(productName)) {
				product.updateQuantity(quantityToAdd);
				break;
			}
		}
	}

}