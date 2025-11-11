
public class Product {
	
	private String name;
	private int price;
	private int quantity;
	
	public Product(String name, int price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	/* You may add other properties and methods */
	
	// Get product name
	public String getName() {
		return this.name;
	}
	// Get product price
	public int getPrice() {
		return this.price;
	}
	// Get product quantity
	public int getQuantity() {
		return this.quantity;
	}

	// Update quantity
	public void updateQuantity(int quantity) {
		this.quantity += quantity;
	}
}
